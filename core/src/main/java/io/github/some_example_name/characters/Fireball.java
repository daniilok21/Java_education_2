package io.github.some_example_name.characters;

import static io.github.some_example_name.MyGdxGame.SCR_HEIGHT;
import static io.github.some_example_name.MyGdxGame.SCR_WIDTH;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

import java.util.Random;

public class Fireball {

    Texture texture;
    private float x, y;
    private int speed = 10;
    private int width;
    private int height;
    private int fireIndex;

    public Fireball(int fireIndex) {
        this.texture = new Texture("fireball/fireball.png");
        this.x = SCR_WIDTH;
        this.y = SCR_HEIGHT / 3f * fireIndex;
        this.width = SCR_HEIGHT / 3 * 2;
        this.height = width / 2;
        this.fireIndex = fireIndex;
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
        if (bird.getBodyX() + bird.getBodyHeight() >= x && bird.getBodyX() <= x + width) {
            return true;
        }
        if (bird.getBodyY() + bird.getBodyHeight() >= y && bird.getBodyY() + bird.getBodyHeight() <= y + height) {
            return true;
        }
        return false;
    }

    public void dispose() {
        texture.dispose();
    }
}
