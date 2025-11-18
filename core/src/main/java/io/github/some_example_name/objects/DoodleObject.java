package io.github.some_example_name.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.math.Vector2;
import io.github.some_example_name.GameSettings;

public class DoodleObject extends GameObject {

    private static final float GYRO_SENSITIVITY = 150f;

    public DoodleObject(String texturePath, float x, float y, float width, float height, short cBits, World world) {
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

    }

    public void moveByGyroscope(float gyroscopeX, float delta) {
        Vector2 velocity = body.getLinearVelocity();
        float targetVelocityX = -gyroscopeX * GYRO_SENSITIVITY;
        body.setLinearVelocity(targetVelocityX, velocity.y);
    }
}
