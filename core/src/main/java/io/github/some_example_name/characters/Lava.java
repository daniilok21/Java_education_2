package io.github.some_example_name.characters;

import static io.github.some_example_name.MyGdxGame.SCR_HEIGHT;
import static io.github.some_example_name.MyGdxGame.SCR_WIDTH;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

public class Lava {

    Texture textureUpperLava;
    Texture textureDownLava;
    int x, yUpper, yLower;
    int distanceBetweenLaves;
    int speed;
    int width;
    int height;
    boolean isUpperLava;
    boolean goBack = false;

    public Lava(int distanceBetweenLaves, int speed, boolean isUpperLava) {
        this.distanceBetweenLaves = distanceBetweenLaves;
        this.x = 0;
        this.yLower = -(SCR_HEIGHT - distanceBetweenLaves) / 2;
        this.height = -this.yLower;
        this.yUpper = SCR_HEIGHT;
        this.speed = speed;
        this.width = SCR_WIDTH;
        this.isUpperLava = isUpperLava;
        this.goBack = false;

        textureUpperLava = new Texture("lava/lava.png");
        textureDownLava = new Texture("lava/lava.png");
    }

    public void draw(Batch batch) {
        batch.draw(textureUpperLava, x, yUpper, width, height);
        batch.draw(textureDownLava, x, yLower, width, height);
    }

    public void move() {
        if (!goBack && SCR_HEIGHT > (yLower + height) + (SCR_HEIGHT - yUpper) + distanceBetweenLaves) {
            yUpper -= speed;
            yLower += speed;
        }
        else {
            yUpper += speed;
            yLower -= speed;
            if (x + width <= 0) {
                dispose();
            }
        }

    }
    public void setGoBack() {
        this.goBack = true;
    }
    public boolean isHit(Bird bird) {
        if (!bird.getIsFlipped()) {
            if (bird.getHeadY() + bird.getHeadHeight() >= yUpper) {
                return true;
            }
            if (bird.getBodyY() <= yLower + height) {
                return true;
            }
        }
        else {
            if (bird.getBodyY() + bird.getBodyHeight() >= yUpper) {
                return true;
            }
            if (bird.getHeadY() <= yLower + height) {
                return true;
            }
        }
        return false;
    }
    public float getX() { return x; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }

    public void dispose() {
        textureDownLava.dispose();
        textureUpperLava.dispose();
    }
}
