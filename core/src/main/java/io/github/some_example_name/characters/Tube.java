package io.github.some_example_name.characters;

import static io.github.some_example_name.MyGdxGame.SCR_HEIGHT;
import static io.github.some_example_name.MyGdxGame.SCR_WIDTH;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

import java.util.Random;

public class Tube {

    Texture textureUpperTube;
    Texture textureDownTube;

    Random random;

    int x, gapY;
    int distanceBetweenTubes;

    boolean isPointReceived;

    int speed = 20;
    final int width = 200;
    final int height = 700;
    int gapHeight = 400;
    int padding = 100;
    private int tubeIndex;

    public Tube(int tubeCount, int tubeIdx) {
        this.tubeIndex = tubeIdx;
        random = new Random();

        gapY = gapHeight / 2 + padding + random.nextInt(SCR_HEIGHT - 2 * (padding + gapHeight / 2));
        distanceBetweenTubes = (SCR_WIDTH + width) / (tubeCount - 1);
        x = distanceBetweenTubes * tubeIdx + SCR_WIDTH;

        textureUpperTube = new Texture("tubes/tube_flipped.png");
        textureDownTube = new Texture("tubes/tube.png");

        isPointReceived = false;
    }

    public void draw(Batch batch) {
        batch.draw(textureUpperTube, x, gapY + gapHeight / 2, width, height);
        batch.draw(textureDownTube, x, gapY - gapHeight / 2 - height, width, height);
    }

    public void move() {
        x -= speed;
        if (x < -width) {
            isPointReceived = false;
            x = SCR_WIDTH + distanceBetweenTubes;
            gapY = gapHeight / 2 + padding + random.nextInt(SCR_HEIGHT - 2 * (padding + gapHeight / 2));
            tubeIndex += 3;
        }
    }

    public boolean isHit(Bird bird) {
        if (bird.getHeadY() <= gapY - gapHeight / 2 && bird.getHeadX() + bird.getHeadWidth() >= x && bird.getHeadX() <= x + width) {
            return true;
        }
        if (bird.getHeadY() + bird.getHeadHeight() >= gapY + gapHeight / 2 && bird.getHeadX() + bird.getHeadWidth() >= x && bird.getHeadX() <= x + width) {
            return true;
        }
        if (bird.getBodyY() <= gapY - gapHeight / 2 && bird.getBodyX() + bird.getBodyWidth() >= x && bird.getBodyX() <= x + width) {
            return true;
        }
        if (bird.getBodyY() + bird.getBodyHeight() >= gapY + gapHeight / 2 && bird.getBodyX() + bird.getBodyWidth() >= x && bird.getBodyX() <= x + width) {
            return true;
        }
        return false;
    }

    public float getBottomTubeX() {
        return x;
    }
    public float getBottomTubeY() {
        return gapY - gapHeight / 2 - height;
    }
    public int getTubeWidth() {
        return width;
    }
    public int getTubeHeight() {
        return height;
    }
    public int getTubeIndex() {
        return tubeIndex;
    }
    public boolean needAddPoint(Bird bird) {
        return !isPointReceived && bird.x > x + width;
    }
    public int getYForFlower() {
        return gapY - gapHeight / 2;
    }

    public void setPointReceived() {
        isPointReceived = true;
    }

    public void dispose() {
        textureDownTube.dispose();
        textureUpperTube.dispose();
    }
}
