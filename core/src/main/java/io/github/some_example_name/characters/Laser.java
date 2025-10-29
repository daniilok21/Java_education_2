package io.github.some_example_name.characters;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Laser {
    private float k;
    private float b;
    private final float initK;
    private float targetK;
    private float kChangeSpeed;
    private float bossX;
    private float bossY;
    private float startX;
    private float endX;

    private final boolean isUpperLaser;

    public Laser(float bossStartX, float bossStartY, float initAngleD, float targetAngleD, float kChangeSpeed, float endX, boolean isUpperLaser) {
        this.isUpperLaser = isUpperLaser;
        this.startX = bossStartX;
        this.bossX = bossStartX;
        this.bossY = bossStartY;
        this.endX = endX;

        this.k = (float) Math.tan((float) Math.toRadians(initAngleD));
        this.initK = this.k;
        this.targetK = (float) Math.tan((float) Math.toRadians(targetAngleD));
        this.kChangeSpeed = kChangeSpeed;

        // y = kx + b |=> b = y - kx
        this.b = this.bossY - this.k * this.startX;
    }
    public void update(float deltaTime) {
        if (k < targetK) {
            k = Math.min(k + kChangeSpeed * deltaTime, targetK);
        } else if (k > targetK) {
            k = Math.max(k - kChangeSpeed * deltaTime, targetK);
        }
    }
    public void setBossPos(float bossX, float bossY) {
        this.bossX = bossX;
        this.bossY = bossY;
        this.b = this.bossY - this.k * this.startX;
        this.startX = bossX;
    }
    public boolean isHit(Bird bird) {
        float birdCheckX, birdCheckY;

        if (isUpperLaser) {
            birdCheckX = bird.getX() + bird.getWidth();
            birdCheckY = bird.getY() + bird.getHeight();
        }
        else {
            birdCheckX = bird.getX() + bird.getWidth();
            birdCheckY = bird.getY();
        }

        float laserYwhithColX = k * birdCheckX + b;

        if (isUpperLaser) {
            if (k < 0) {
                return birdCheckY > laserYwhithColX;
            }
        }
        else {
            if (k > 0) {
                return birdCheckY < laserYwhithColX;
            }
        }
        return false;
    }

    public void render(ShapeRenderer shapeRenderer) {
        float startY = k * startX + b;
        float endY = k * endX + b;

        shapeRenderer.setColor(Color.RED);
        shapeRenderer.line(startX, startY, endX, endY);
    }
    public void reset() {
        this.k = this.initK;
    }
}
