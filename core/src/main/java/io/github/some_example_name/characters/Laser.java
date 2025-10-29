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
    public boolean isKequalTargetK() {
        return k == targetK;
    }
    public boolean isHit(Bird bird) {
        if (checkCollisionWithLaser(bird.getHeadX(), bird.getHeadY(), bird.getHeadWidth(), bird.getHeadHeight())) {
            return true;
        }
        return checkCollisionWithLaser(bird.getBodyX(), bird.getBodyY(), bird.getBodyWidth(), bird.getBodyHeight());
    }
    private boolean checkCollisionWithLaser(float rectX, float rectY, float rectWidth, float rectHeight) {
        float rectRightX = rectX + rectWidth;
        if (isUpperLaser) {
            if (k < 0) {
                return rectY + rectHeight > k * rectRightX + b;
            }
        }
        else {
            if (k > 0) {
                return rectY < k * rectRightX + b;
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
