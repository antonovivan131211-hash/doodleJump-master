package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class MenuScreen extends ScreenAdapter {

    private static final float BUTTON_WIDTH = 250f;
    private static final float BUTTON_HEIGHT = 70f;
    private static final float BUTTON_SPACING = 30f;
    private static final float FONT_SCALE = 5f;

    private MyGdxGame game;
    private Stage stage;
    private Skin skin;
    private BitmapFont font;

    public MenuScreen(MyGdxGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        skin = new Skin();
        font = new BitmapFont();

        // Масштаб шрифта
        font.getData().setScale(FONT_SCALE);

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;
        buttonStyle.fontColor = Color.WHITE;
        skin.add("default", buttonStyle);

        // Кнопка Start
        TextButton startButton = new TextButton("Start", skin);
        startButton.setPosition(
            Gdx.graphics.getWidth() / 2f - BUTTON_WIDTH / 2f,
            Gdx.graphics.getHeight() / 2f + BUTTON_SPACING
        );
        startButton.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);

        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(game.gameScreen);
            }
        });

        // Кнопка Exit
        TextButton exitButton = new TextButton("Exit", skin);
        exitButton.setPosition(
            Gdx.graphics.getWidth() / 2f - BUTTON_WIDTH / 2f,
            Gdx.graphics.getHeight() / 2f - BUTTON_HEIGHT - BUTTON_SPACING
        );
        exitButton.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        stage.addActor(startButton);
        stage.addActor(exitButton);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        if (stage != null) {
            stage.dispose();
        }
        if (skin != null) {
            skin.dispose();
        }
        if (font != null) {
            font.dispose();
        }
    }
}
