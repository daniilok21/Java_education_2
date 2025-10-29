package io.github.some_example_name.characters;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Boss {

    private float x, y;
    private Texture texture = new Texture("boss/boss.png");
    private int width, height;
    private Laser upperLaser;
    private Laser lowerLaser;
    private float laserEndX;
    private static final float LASER_CHANGE_SPEED = 0.1f;
    private boolean lasersInited = false;
    private boolean lasersRenderActive = false;
    private boolean lasersCollisionActive = false;
    public Boss(float x, float y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.laserEndX = -100;
        this.lasersInited = false;
        this.lasersRenderActive = false;
        this.lasersCollisionActive = false;
    }
    public void resetLasers() {
        if (upperLaser != null) {
            upperLaser = null;
        }
        if (lowerLaser != null) {
            lowerLaser = null;
        }
        this.lasersInited = false;
        this.lasersRenderActive = false;
        this.lasersCollisionActive = false;
    }
    public void initializeLasers() {
        resetLasers();
        float laserStartX = this.x + this.width / 2.0f;
        float laserStartY = this.y + this.height / 2.0f;
        float initUpperAngle = -30.0f;
        float targetUpperAngle = -10.0f;
        float initLowerAngle = 30.0f;
        float targetLowerAngle = 10.0f;

        this.upperLaser = new Laser(laserStartX, laserStartY, initUpperAngle, targetUpperAngle, LASER_CHANGE_SPEED, laserEndX, true);
        this.lowerLaser = new Laser(laserStartX, laserStartY, initLowerAngle, targetLowerAngle, LASER_CHANGE_SPEED, laserEndX, false);
        this.lasersInited = true;
        this.lasersRenderActive = true;
        this.lasersCollisionActive = false;
    }
    public void enableLaserCollision() {
        if (lasersInited) {
            this.lasersCollisionActive = true;
        }
    }
    public boolean getLasersCollisionActive() {
        return this.lasersCollisionActive;
    }
    public void draw(Batch batch) {
        batch.draw(texture, x, y, width, height);
    }
    public void renderLasers(ShapeRenderer shapeRenderer) {
        if (lasersRenderActive && lasersInited) {
            upperLaser.setBossPos(this.x + this.width / 2.0f, this.y + this.height / 2.0f);
            lowerLaser.setBossPos(this.x + this.width / 2.0f, this.y + this.height / 2.0f);

            upperLaser.render(shapeRenderer);
            lowerLaser.render(shapeRenderer);
        }
    }
    public void updateLasers(float deltaTime) {
        if (lasersInited) {
            upperLaser.update(deltaTime);
            lowerLaser.update(deltaTime);
        }
    }
    public boolean checkLaserCollision(Bird bird) {
        if (!(lasersCollisionActive && lasersInited)) {
            return false;
        }
        return upperLaser.isHit(bird) || lowerLaser.isHit(bird);
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
        if (upperLaser != null) {
            upperLaser = null;
        }
        if (lowerLaser != null) {
            lowerLaser = null;
        }
    }
}
