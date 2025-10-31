package io.github.some_example_name.characters;

import static io.github.some_example_name.MyGdxGame.SCR_HEIGHT;
import static io.github.some_example_name.MyGdxGame.SCR_WIDTH;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

public class Fireball {

    Texture texture;
    private float x, y;
    private int speed;
    private int width;
    private int height;
    private int fireIndex;

    public Fireball(int fireIndex, float x, int speed) {
        this.texture = new Texture("fireball/fireball.png");
        this.x = x;
        this.y = SCR_HEIGHT / 5f * fireIndex;
        this.width = SCR_HEIGHT / 5 * 2;
        this.height = width / 2;
        this.fireIndex = fireIndex;
        this.speed = speed;
    }

    public void draw(Batch batch) {
        batch.draw(texture, x, y, width, height);
    }

    public void move() {
        x -= speed;
        if (x < -width) {
            dispose();
        }
    }

    public boolean isHit(Bird bird) {
        if (x < bird.getHeadX() + bird.getHeadWidth() && x + width / 2.0f > bird.getHeadX() &&
            y < bird.getHeadY() + bird.getHeadHeight() && y + height > bird.getHeadY()) {
            return true;
        }
        if (x < bird.getBodyX() + bird.getBodyWidth() && x + width / 2.0f > bird.getBodyX() &&
            y < bird.getBodyY() + bird.getBodyHeight() && y + height > bird.getBodyY()) {
            return true;
        }
        return false;
    }
    public float getX() { return x; }
    public float getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public int getFireIndex() {return fireIndex; }

    public void dispose() {
        texture.dispose();
    }
}
