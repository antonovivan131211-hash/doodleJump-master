package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.ScreenUtils;
import io.github.some_example_name.objects.DoodleObject;
import io.github.some_example_name.objects.PlateObject;
import java.util.ArrayList;

public class GameScreen extends ScreenAdapter {
    MyGdxGame myGdxGame;
    int r = 0;
    Batch batch;
    boolean gyroscopeAvail = Gdx.input.isPeripheralAvailable(Input.Peripheral.Gyroscope);
    DoodleObject doodleObject;
    ArrayList<PlateObject> plates;
    float nextPlateY;
    final float PLATE_SPAWN_INTERVAL_Y = 150f;

    public GameScreen(MyGdxGame myGdxGame) {
        this.myGdxGame = myGdxGame;
        batch = myGdxGame.batch;
        doodleObject = new DoodleObject(GameResorses.DOODLE_PATH, 100f, 150f, 301f / 2f, 453f / 2f, GameSettings.DOODLE_BIT, myGdxGame.world);

        plates = new ArrayList<>();
        nextPlateY = 100f;

        int screenHeight = Gdx.graphics.getHeight();
        while (nextPlateY < screenHeight * 2) {
            spawnNewPlate();
        }
    }

    private void spawnNewPlate() {
        float plateWidth = 150f;
        float plateHeight = 50f;

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

        float spawnThreshold = myGdxGame.camera.position.y + myGdxGame.camera.viewportHeight / 2;
        while (nextPlateY < spawnThreshold + PLATE_SPAWN_INTERVAL_Y * 2) {
            spawnNewPlate();
        }

        for (int i = 0; i < plates.size(); i++) {
            PlateObject plate = plates.get(i);

            if (plate.getY() < myGdxGame.camera.position.y - myGdxGame.camera.viewportHeight / 2 - plate.height) {
                plate.dispose();
                plates.remove(i);
                i--;
            } else {
                plate.update(delta);
            }
        }

        draw();
    }

    private void draw() {
        myGdxGame.camera.update();
        myGdxGame.batch.setProjectionMatrix(myGdxGame.camera.combined);
        ScreenUtils.clear(Color.CLEAR);

        handleInput();
        doodleObject.jump();
        r++;

        batch.begin();
        doodleObject.draw(myGdxGame.batch);

        for (PlateObject plate : plates) {
            plate.draw(myGdxGame.batch);
        }

        batch.end();
    }

    private void handleInput() {
        if (Gdx.input.isPeripheralAvailable(Input.Peripheral.Gyroscope)) {
            if (r >= 100) {
                String gyroX = String.format("%.2f", Gdx.input.getGyroscopeX());
                String gyroY = String.format("%.2f", Gdx.input.getGyroscopeY());
                String gyroZ = String.format("%.2f", Gdx.input.getGyroscopeZ());
                Gdx.app.log("кординаты", "x - " + gyroX + "   y- " + gyroY + "   z - " + gyroZ);
                r -= 100;
            }
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        doodleObject.dispose();
        for (PlateObject plate : plates) {
            plate.dispose();
        }
    }
}
