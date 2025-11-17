package io.github.some_example_name.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef;

public class PlateObject extends GameObject {
    private Texture texture;
    private float x, y;
    private float width, height;

    public PlateObject(float x, float y, float width, float height, short cBits, World world) {
        super("i1.png", (int)x, (int)y, (int)width, (int)height, cBits, world);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        texture = new Texture(Gdx.files.internal("i1.png"));

        if (super.body != null) {
            super.body.setType(BodyDef.BodyType.StaticBody);
        }
    }

    @Override
    public void draw(SpriteBatch batch) {
        super.draw(batch);
    }
}
