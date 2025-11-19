package io.github.some_example_name;

import static io.github.some_example_name.GameSettings.POSITION_ITERATIONS;
import static io.github.some_example_name.GameSettings.STEP_TIME;
import static io.github.some_example_name.GameSettings.VELOCITY_ITERATIONS;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.World;

public class MyGdxGame extends Game {
    public SpriteBatch batch;
    public OrthographicCamera camera;
    public OrthographicCamera cameraFixed;
    public World world;
    public Vector3 touch;
    float accumulator = 0;
    public BitmapFont commonWhiteFont, commonBlackFont, largeWhiteFont;
    GameSession gameSession;

    public GameScreen gameScreen;
    public MenuScreen menuScreen;

    @Override
    public void create() {
        Box2D.init();
        world = new World(new Vector2(0, -50), true);
        batch = new SpriteBatch();


        touch = new Vector3();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, GameSettings.SCREEN_WIDTH, GameSettings.SCREEN_HEIGHT);

        cameraFixed = new OrthographicCamera();
        cameraFixed.setToOrtho(false, GameSettings.SCREEN_WIDTH, GameSettings.SCREEN_HEIGHT);
        cameraFixed.position.set(GameSettings.SCREEN_WIDTH / 2, GameSettings.SCREEN_HEIGHT / 2, 0);
        cameraFixed.update();


        createFonts();

        gameSession = new GameSession();
        gameSession.startGame();

        menuScreen = new MenuScreen(this);
        gameScreen = new GameScreen(this);

        setScreen(menuScreen);
    }

    private void createFonts() {
        try {

            FileHandle fontFile = Gdx.files.internal("fonts/arial.ttf");

            if (fontFile.exists()) {
                FreeTypeFontGenerator generator = new FreeTypeFontGenerator(fontFile);
                FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

                parameter.size = 24;
                parameter.color = Color.WHITE;
                commonWhiteFont = generator.generateFont(parameter);

                parameter.size = 48;
                largeWhiteFont = generator.generateFont(parameter);

                parameter.size = 24;
                parameter.color = Color.BLACK;
                commonBlackFont = generator.generateFont(parameter);

                generator.dispose();
                Gdx.app.log("FONT", "FreeType fonts created successfully");
            } else {
                throw new Exception("Font file not found");
            }
        } catch (Exception e) {

            Gdx.app.log("FONT", "Using default fonts: " + e.getMessage());

            commonWhiteFont = new BitmapFont();
            largeWhiteFont = new BitmapFont();
            commonBlackFont = new BitmapFont();

            commonWhiteFont.setColor(Color.WHITE);
            largeWhiteFont.setColor(Color.WHITE);
            commonBlackFont.setColor(Color.BLACK);
            largeWhiteFont.getData().setScale(2.0f);
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        if (commonWhiteFont != null) commonWhiteFont.dispose();
        if (largeWhiteFont != null) largeWhiteFont.dispose();
        if (commonBlackFont != null) commonBlackFont.dispose();
        if (menuScreen != null) {
            menuScreen.dispose();
        }
        if (gameScreen != null) {
            gameScreen.dispose();
        }
        if (world != null) {
            world.dispose();
        }
    }


    public void stepWorld() {
        float delta = Gdx.graphics.getDeltaTime();
        accumulator += delta;

        if (accumulator >= STEP_TIME) {
            accumulator -= STEP_TIME;
            world.step(STEP_TIME, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
        }
    }


    public void setGameScreen() {
        if (gameScreen == null) {
            gameScreen = new GameScreen(this);
        }
        setScreen(gameScreen);
    }

    public void setMenuScreen() {
        if (menuScreen == null) {
            menuScreen = new MenuScreen(this);
        }
        setScreen(menuScreen);
    }
}
