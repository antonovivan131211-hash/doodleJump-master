package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.ScreenUtils;
import io.github.some_example_name.objects.DoodleObject;
import io.github.some_example_name.objects.PlateObject;
import java.util.ArrayList;

public class GameScreen extends ScreenAdapter {
    MyGdxGame myGdxGame;
    int r = 0;
    Batch batch;
    boolean gyroscopeAvail;

    DoodleObject doodleObject;
    ArrayList<PlateObject> plates;
    float nextPlateY;
    final float PLATE_SPAWN_INTERVAL_Y = 200f;

    AchievementManager achievementManager;
    boolean firstLaunch = true;
    boolean achievementShown = false;

    private boolean moveLeft = false;
    private boolean moveRight = false;

    public GameScreen(MyGdxGame myGdxGame) {
        this.myGdxGame = myGdxGame;
        batch = myGdxGame.batch;

        doodleObject = new DoodleObject(GameResorses.DOODLE_PATH, 100f, 150f, 301f / 2f, 453f / 2f, GameSettings.DOODLE_BIT, myGdxGame.world);

        plates = new ArrayList<>();
        nextPlateY = 100f;

        int screenHeight = Gdx.graphics.getHeight();
        while (nextPlateY < screenHeight * 3) {
            spawnNewPlate();
        }

        gyroscopeAvail = Gdx.input.isPeripheralAvailable(Input.Peripheral.Gyroscope);

        achievementManager = new AchievementManager();

        setupCollisionListener();
    }

    private void setupCollisionListener() {
        myGdxGame.world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Object userDataA = contact.getFixtureA().getBody().getUserData();
                Object userDataB = contact.getFixtureB().getBody().getUserData();

                if ((userDataA != null && userDataB != null)) {
                    if ((userDataA.equals("doodle") && userDataB.equals("platform")) ||
                        (userDataA.equals("platform") && userDataB.equals("doodle"))) {

                        Vector2 doodleVelocity = doodleObject.body.getLinearVelocity();
                        if (doodleVelocity.y <= 0) {
                            doodleObject.onPlatformCollision();
                        }
                    }
                }
            }

            @Override
            public void endContact(Contact contact) {
            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {
            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {
            }
        });
    }

    private void spawnNewPlate() {
        float plateWidth = 120f;
        float plateHeight = 30f;

        PlateObject newPlate = new PlateObject(
            nextPlateY,
            plateWidth,
            plateHeight,
            GameSettings.PLATE_BIT,
            myGdxGame.world,
            Gdx.graphics.getWidth()
        );
        plates.add(newPlate);

        nextPlateY += PLATE_SPAWN_INTERVAL_Y;
    }

    @Override
    public void render(float delta) {
        myGdxGame.stepWorld();
        doodleObject.update(delta);

        achievementManager.update(delta);

        checkAchievements();

        handleMovement();

        if (gyroscopeAvail) {
            float gyroscopeX = Gdx.input.getGyroscopeX();
            doodleObject.moveByGyroscope(gyroscopeX, delta);
        }

        float spawnThreshold = myGdxGame.camera.position.y + myGdxGame.camera.viewportHeight;
        while (nextPlateY < spawnThreshold + PLATE_SPAWN_INTERVAL_Y * 3) {
            spawnNewPlate();
        }

        for (int i = plates.size() - 1; i >= 0; i--) {
            PlateObject plate = plates.get(i);
            if (plate.getY() < myGdxGame.camera.position.y - myGdxGame.camera.viewportHeight - 100) {
                plate.dispose();
                plates.remove(i);
            } else {
                plate.update(delta);
            }
        }

        if (doodleObject.getY() > myGdxGame.camera.position.y - myGdxGame.camera.viewportHeight / 3) {
            myGdxGame.camera.position.y = doodleObject.getY() + myGdxGame.camera.viewportHeight / 3;
        }

        draw();
    }

    private void handleMovement() {
        if (moveLeft) {
            doodleObject.moveLeft();
        }
        if (moveRight) {
            doodleObject.moveRight();
        }
    }

    private void checkAchievements() {
        if (firstLaunch && !achievementShown) {
            achievementManager.unlockAchievement("welcome");
            firstLaunch = false;
            achievementShown = true;
            Gdx.app.log("ACHIEVEMENT", "Welcome achievement unlocked");
        }

        float doodleY = doodleObject.getY();

        if (doodleY > 100f && !achievementManager.isAchievementUnlocked("first_jump")) {
            achievementManager.unlockAchievement("first_jump");
            Gdx.app.log("ACHIEVEMENT", "First jump achievement unlocked");
        }

        if (doodleY > 1000f && !achievementManager.isAchievementUnlocked("height_100")) {
            achievementManager.unlockAchievement("height_100");
            Gdx.app.log("ACHIEVEMENT", "Height 100 achievement unlocked");
        }

        if (doodleY > 5000f && !achievementManager.isAchievementUnlocked("height_500")) {
            achievementManager.unlockAchievement("height_500");
            Gdx.app.log("ACHIEVEMENT", "Height 500 achievement unlocked");
        }
    }

    private void draw() {
        myGdxGame.camera.update();
        ScreenUtils.clear(Color.CLEAR);

        handleInput();
        r++;

        batch.begin();
        batch.setProjectionMatrix(myGdxGame.camera.combined);

        for (PlateObject plate : plates) {
            plate.draw(myGdxGame.batch);
        }

        doodleObject.draw(myGdxGame.batch);

        batch.end();

        batch.begin();
        batch.setProjectionMatrix(myGdxGame.cameraFixed.combined);
        achievementManager.draw(batch, myGdxGame.commonWhiteFont);

        myGdxGame.commonWhiteFont.draw(batch, "Height: " + (int)doodleObject.getY(), 20, myGdxGame.cameraFixed.viewportHeight - 20);
        myGdxGame.commonWhiteFont.draw(batch, "Velocity: " + String.format("%.1f", doodleObject.getVerticalVelocity()), 20, myGdxGame.cameraFixed.viewportHeight - 50);

        batch.end();
    }

    private void handleInput() {
        moveLeft = false;
        moveRight = false;

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
            moveLeft = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            moveRight = true;
        }

        if (Gdx.input.isTouched()) {
            float touchX = Gdx.input.getX();
            float screenWidth = Gdx.graphics.getWidth();

            if (touchX < screenWidth / 3) {
                moveLeft = true;
            } else if (touchX > screenWidth * 2 / 3) {
                moveRight = true;
            }
        }

        if (gyroscopeAvail && r >= 100) {
            String gyroX = String.format("%.2f", Gdx.input.getGyroscopeX());
            String gyroY = String.format("%.2f", Gdx.input.getGyroscopeY());
            String gyroZ = String.format("%.2f", Gdx.input.getGyroscopeZ());
            Gdx.app.log("Gyroscope", "x - " + gyroX + "   y- " + gyroY + "   z - " + gyroZ);
            r = 0;
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        doodleObject.dispose();
        for (PlateObject plate : plates) {
            plate.dispose();
        }
        achievementManager.saveAchievements();
    }
}
