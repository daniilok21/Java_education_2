package io.github.some_example_name.characters;

import static io.github.some_example_name.MyGdxGame.SCR_HEIGHT;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

public class Bird {

    int x, y;
    int width, height;
    float speedY;
    private float gravity = -800f;
    int frameCounter;
    Texture[] framesArray;
    private float headX, headY, headWidth, headHeight;
    private float bodyX, bodyY, bodyWidth, bodyHeight;
    private float headOffsetXRel, headOffsetYRel;
    private float bodyOffsetXRel, bodyOffsetYRel;
    private boolean isFliped = false;

    private static final float jump = 400f;
    public Bird(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speedY = 0f;
        frameCounter = 0;

        framesArray = new Texture[]{
            new Texture("birdTiles/bird0.png"),
            new Texture("birdTiles/bird1.png"),
            new Texture("birdTiles/bird2.png"),
            new Texture("birdTiles/bird1.png"),
        };
        this.headOffsetXRel = 0.55f;
        this.headOffsetYRel = 0.4f;
        this.bodyOffsetXRel = 0.0f;
        this.bodyOffsetYRel = 0.0f;
        this.headWidth = 0.375f * width;
        this.headHeight = 0.5f * height;
        this.bodyWidth = 0.75f * width;
        this.bodyHeight = 0.9f * height;
        this.headX = this.x + this.headOffsetXRel * this.width;
        this.headY = this.y + this.headOffsetYRel * this.height;
        this.bodyX = this.x + this.bodyOffsetXRel * this.width;
        this.bodyY = this.y + this.bodyOffsetYRel * this.height;
    }
    public void changeIsFliped(boolean isFliped) {
        this.isFliped = isFliped;
    }
    public void flipGravity() {
        isFliped = !isFliped;
        gravity = -gravity;
        setSpeedY();
    }
    public void setY(int y) {
        this.y = y;
        updateHitboxes();
    }

    public void setSpeedY() {
        this.speedY = 0f;
    }

    public void onClick() {
        if (!isFliped) {
            speedY = jump;
        }
        else {
            speedY = -jump;
        }
    }

    public void fly(float deltaTime) {
        // v = v0 + a * t
        speedY += gravity * deltaTime;
        // y = y0 + v * t + 0.5 * a * t^2
        y += speedY * deltaTime + (gravity * (deltaTime * deltaTime) / 2);

        updateHitboxes();
    }
    private void updateHitboxes() {
        if (!isFliped) {
            this.headX = this.x + this.headOffsetXRel * this.width;
            this.headY = this.y + this.headOffsetYRel * this.height;
            this.bodyX = this.x + this.bodyOffsetXRel * this.width;
            this.bodyY = this.y + this.bodyOffsetYRel * this.height;
        } else {
            this.headX = this.x + this.headOffsetXRel * this.width;
            this.headY = this.y + height - this.headOffsetYRel * this.height - headHeight;
            this.bodyX = this.x + this.bodyOffsetXRel * this.width;
            this.bodyY = this.y + height - this.bodyOffsetYRel * this.height - bodyHeight;
        }
    }
    public boolean isInField() {
        return y + getBodyHeight() > 0 && y + getBodyHeight() < SCR_HEIGHT;
    }
    public void changeGravity(float gravity) {
        if (!isFliped) {
            this.gravity = gravity;
        }
        else {
            this.gravity = -gravity;
        }
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
    public float getHeadX() { return headX; }
    public float getHeadY() { return headY; }
    public float getHeadWidth() { return headWidth; }
    public float getHeadHeight() { return headHeight; }
    public float getBodyX() { return bodyX; }
    public float getBodyY() { return bodyY; }
    public float getBodyWidth() { return bodyWidth; }
    public float getBodyHeight() { return bodyHeight; }
    public boolean getIsFlipped() { return isFliped; }

    public void draw(Batch batch) {
        int frameMultiplier = 10;
        Texture currentFrame = framesArray[frameCounter / frameMultiplier];
        if (!isFliped) {
            batch.draw(currentFrame, x, y, width, height);
        } else {
            batch.draw(currentFrame, x, y + height, width, -height);
        }
        if (frameCounter++ == framesArray.length * frameMultiplier - 1) frameCounter = 0;
    }

    public void dispose() {
        for (Texture texture : framesArray) {
            texture.dispose();
        }
    }
}
