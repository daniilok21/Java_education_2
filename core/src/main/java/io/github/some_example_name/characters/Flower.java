package io.github.some_example_name.characters;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

import io.github.some_example_name.characters.Tube;

public class Flower {

    private Texture texture = new Texture("flower.png");
    private float x, y;
    private int width, height;
    private static final int TARGET_TUBE_INDEX = 0;

    private int speed = 20;
    private boolean isActive = false;

    public Flower(float x, float y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public boolean isHit(float touchX, float touchY) {
        return touchX >= x && touchX <= x + width &&
            touchY >= y && touchY <= y + height;
    }

    public void setPos(float tX, float tY) {
        x = tX;
        y = tY;
    }

    public void draw(Batch batch) {
        batch.draw(texture, x, y, width, height);
    }

    public void move() {
        x -= speed;
    }

    public float getX() { return x; }
    public float getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public boolean getIsActive() { return isActive; }
    public int getTargetTubeIndex() {return TARGET_TUBE_INDEX; }
    public void swtichActiveTrue () {
        isActive = true;
    }
    public void swtichActiveFalse () {
        isActive = false;
    }

    public void dispose() {
        texture.dispose();
    }
}
