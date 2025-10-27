package io.github.some_example_name.characters;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

public class Boss {

    private float x, y;
    private Texture texture = new Texture("boss/boss.png");
    private int width, height;

    public Boss(float x, float y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void draw(Batch batch) {
        batch.draw(texture, x, y, width, height);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public Texture getTexture() {
        return texture;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void dispose() {
        texture.dispose();
    }
}
