package io.github.some_example_name.characters;

import static io.github.some_example_name.MyGdxGame.SCR_HEIGHT;
import static io.github.some_example_name.MyGdxGame.SCR_WIDTH;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
public class FlipGravitation {
    Texture texture;
    private float x, y;
    private int width;
    private int height;
    private int speed;
    boolean isActivated;
    public FlipGravitation(float x, int speed) {
        this.x = x;
        this.speed = speed;
        texture = new Texture("flip_gravity.png");
        this.width = 64;
        this.height = 64;
        this.y = SCR_HEIGHT / 2 - height / 2;
        isActivated = false;
    }
    public void move() {
        x -= speed;
        if (x + width <= 0) {
            dispose();
        }
    }
    public void draw(Batch batch) {
        batch.draw(texture, x, y, width, height);
    }
    public void needFleepGravity(Bird bird) {
        if (!isActivated && isHit(bird)) {
            isActivated = true;
            bird.flipGravity();
        }
    }
    public boolean isHit(Bird bird) {
        if (bird.getHeadX() + bird.getHeadWidth() >= x) {
            return true;
        }
        return false;
    }
    public float getX() { return x; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }

    public void dispose() {
        texture.dispose();
    }
}

