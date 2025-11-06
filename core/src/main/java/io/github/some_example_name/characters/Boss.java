package io.github.some_example_name.characters;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;

public class Boss {
    int frameCounter;
    Texture[] framesArray;
    private float x, y;
    private int width, height;
    private Laser upperLaser;
    private Laser lowerLaser;
    private Lava lava;
    private float laserEndX;
    private static float LASER_CHANGE_SPEED = 0.1f;
    private boolean lasersInited = false;
    private boolean lasersRenderActive = false;
    private boolean lasersCollisionActive = false;
    private boolean lasersVisible = true;
    private Array<Fireball> fireballArray;
    private Array<FlipGravitation> flipGravitationArray;
    public Boss(float x, float y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.laserEndX = -100;
        this.lasersInited = false;
        this.lasersRenderActive = false;
        this.lasersCollisionActive = false;
        this.fireballArray = new Array<Fireball>();
        this.flipGravitationArray = new Array<FlipGravitation>();
        this.frameCounter = 0;
        framesArray = new Texture[]{
            new Texture("boss/demon1.png"),
            new Texture("boss/demon2.png"),
            new Texture("boss/demon3.png"),
            new Texture("boss/demon4.png"),
        };
    }
    public void initFlipGravitation(float x, int speed) {
        FlipGravitation flipGravitation = new FlipGravitation(x, speed);
        flipGravitationArray.add(flipGravitation);
    }
    public void renderFlipGravitation(Batch batch, Bird bird) {
        for (int i = flipGravitationArray.size - 1; i >= 0; i--) {
            FlipGravitation flipGravitation = flipGravitationArray.get(i);
            flipGravitation.move();
            if (flipGravitation.getX() + flipGravitation.getWidth() < 0) {
                flipGravitation.dispose();
                flipGravitationArray.removeIndex(i);
            } else {
                flipGravitation.draw(batch);
                flipGravitation.needFleepGravity(bird);
            }
        }
    }
    public void resetFlipGravitation() {
        for (FlipGravitation flipGravitation : flipGravitationArray) {
            flipGravitation.dispose();
        }
        flipGravitationArray.clear();
    }
    public void initLava(Bird bird, int distanceBetweenLaves, int speed, float gravity) {
        resetLava();
        lava = new Lava(distanceBetweenLaves, speed, true);
        bird.changeGravity(gravity);
    }
    public void resetLava() {
        lava = null;
    }
    public void backGoLava(int speed) {
        lava.setGoBack(speed);
    }
    public void renderLava(Batch batch, Bird bird) {
        if (lava != null) {
            lava.move(bird);
            lava.draw(batch);
        }
    }
    public void renderFireball(Batch batch) {
        for (int i = fireballArray.size - 1; i >= 0; i--) {
            Fireball fireball = fireballArray.get(i);
            fireball.move();
            if (fireball.getX() + fireball.getWidth() < 0) {
                fireball.dispose();
                fireballArray.removeIndex(i);
            } else {
                fireball.draw(batch);
            }
        }
    }
    public boolean checkLavaCollision(Bird bird) {
        if (lava != null) {
            return lava.isHit(bird);
        }
        return false;
    }
    public boolean checkFireballCollision(Bird bird) {
        for (Fireball fireball : fireballArray) {
            if (fireball.isHit(bird)) {
                return true;
            }
        }
        return false;
    }
    public void initFireball(int id, float x, int speed) {
        // default x = SCR_WIDTH
        Fireball fireball = new Fireball(id, x, speed);
        fireballArray.add(fireball);
    }
    public void resetFireballs() {
        for (Fireball fireball : fireballArray) {
            fireball.dispose();
        }
        fireballArray.clear();
    }
    public void resetLasers() {
        upperLaser = null;
        lowerLaser = null;
        this.lasersInited = false;
        this.lasersRenderActive = false;
        this.lasersCollisionActive = false;
        this.lasersVisible = true;
    }
    public void initLasers(float initUpperAngle, float targetUpperAngle, float initLowerAngle, float targetLowerAngle, float speed) {
        // default: -30f, -10f, 30f, 10f, 0.1f;
        resetLasers();
        float laserStartX = this.x + this.width * 0.38f;
        float laserStartY = this.y + this.height * 0.38f;
        this.LASER_CHANGE_SPEED = speed;
        this.upperLaser = new Laser(laserStartX, laserStartY, initUpperAngle, targetUpperAngle, LASER_CHANGE_SPEED, laserEndX, true);
        this.lowerLaser = new Laser(laserStartX, laserStartY, initLowerAngle, targetLowerAngle, LASER_CHANGE_SPEED, laserEndX, false);
        this.lasersInited = true;
        this.lasersRenderActive = true;
        this.lasersCollisionActive = true;
    }
    public void renderLasers(ShapeRenderer shapeRenderer) {
        if (lasersRenderActive && lasersInited && lasersVisible) {
            upperLaser.setBossPos(this.x + this.width * 0.38f, this.y + this.height * 0.38f);
            lowerLaser.setBossPos(this.x + this.width * 0.38f, this.y + this.height * 0.38f);

            upperLaser.render(shapeRenderer);
            lowerLaser.render(shapeRenderer);
        }
    }
    public void enableLaserCollision() {
        if (lasersInited) {
            this.lasersCollisionActive = true;
        }
    }
    public void setLasersVisible(boolean visible) {
        this.lasersVisible = visible;
    }
    public void disableLasers() {
        setLasersVisible(false);
        disableLaserCollision();
    }
    public void disableLaserCollision() {
        this.lasersCollisionActive = false;
    }
    public boolean getLasersCollisionActive() {
        return this.lasersCollisionActive;
    }
    public void draw(Batch batch) {
        int frameMultiplier = 10;
        Texture currentFrame = framesArray[frameCounter / frameMultiplier];
        batch.draw(currentFrame, x, y, width, height);
        if (frameCounter++ == framesArray.length * frameMultiplier - 1) frameCounter = 0;
    }
    public boolean isKofLaserEqualTargetK() {
        if (lasersRenderActive && lasersInited) {
            return upperLaser.isKequalTargetK();
        }
        return false;
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
    public boolean isLasersVisible() {
        return this.lasersVisible;
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
    public Array<Fireball> getFireballs() {
        return this.fireballArray;
    }
    public void setY(float y) {
        this.y = y;
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void dispose() {
        for (Texture texture : framesArray) {
            texture.dispose();
        }
        if (upperLaser != null) {
            upperLaser = null;
        }
        if (lowerLaser != null) {
            lowerLaser = null;
        }
    }
}
