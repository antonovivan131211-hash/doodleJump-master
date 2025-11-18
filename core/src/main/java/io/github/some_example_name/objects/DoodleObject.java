package io.github.some_example_name.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef;
import io.github.some_example_name.GameSettings;

public class DoodleObject extends GameObject {

    // Обновленный конструктор, чтобы принимать float и BodyDef.BodyType
    public DoodleObject(String texturePath, float x, float y, float width, float height, short cBits, World world) {
        // Здесь была ошибка: отсутствовал BodyDef.BodyType.DynamicBody
        super(texturePath, x, y, width, height, cBits, world, BodyDef.BodyType.DynamicBody);
    }

    @Override
    public void draw(SpriteBatch batch) {
        super.draw(batch);
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    @Override
    public void update(float delta) {
        super.update(delta);
    }

    public void jump() {
        // Логика прыжка
    }
}
