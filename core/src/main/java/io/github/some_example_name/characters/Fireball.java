// io/github/some_example_name/characters/Fireball.java
package io.github.some_example_name.characters;

import static io.github.some_example_name.MyGdxGame.SCR_HEIGHT;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

public class Fireball {

    int frameCounter;
    Texture[] framesArray;
    private float x, y;
    private int speed;
    private int width;
    private int height;
    private int fireIndex;
    private float hitboxRadius;
    private float hitboxCenterX;
    private float hitboxCenterY;

    public Fireball(int fireIndex, float x, int speed) {
        framesArray = new Texture[]{
            new Texture("fireball/fireball1.png"),
            new Texture("fireball/fireball2.png"),
            new Texture("fireball/fireball3.png"),
            new Texture("fireball/fireball4.png"),
        };
        this.x = x;
        this.y = SCR_HEIGHT / 5f * fireIndex;
        this.width = (int) (SCR_HEIGHT / 5.0f * 2.0f);
        this.height = width / 2;
        this.fireIndex = fireIndex;
        this.speed = speed;
        this.frameCounter = 0;
        this.hitboxRadius = this.height / 2.0f;
        this.hitboxCenterX = this.x + this.width / 4.0f;
        this.hitboxCenterY = this.y + this.height / 2.0f;
    }

    public void draw(Batch batch) {
        int frameMultiplier = 10;
        Texture currentFrame = framesArray[frameCounter / frameMultiplier];
        batch.draw(currentFrame, x, y, width, height);
        if (frameCounter++ == framesArray.length * frameMultiplier - 1) frameCounter = 0;
    }

    public void move() {
        x -= speed;
        hitboxCenterX = x + width / 4.0f;
        if (x < -width) {
            dispose();
        }
    }
    public boolean isHit(Bird bird) {
        if (checkCircleRectCollision(bird.getHeadX(), bird.getHeadY(), bird.getHeadWidth(), bird.getHeadHeight())) {
            return true;
        }
        if (checkCircleRectCollision(bird.getBodyX(), bird.getBodyY(), bird.getBodyWidth(), bird.getBodyHeight())) {
            return true;
        }

        return false;
    }
    private boolean checkCircleRectCollision(float x, float y, float w, float h) {
        float tempX = hitboxCenterX - Math.max(x, Math.min(hitboxCenterX, x + w));
        float tempY = hitboxCenterY - Math.max(y, Math.min(hitboxCenterY, y + h));
        return tempX * tempX + tempY * tempY <= hitboxRadius * hitboxRadius;
    }


    public float getX() { return x; }
    public float getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public int getFireIndex() {return fireIndex; }
    public float getHitboxCenterX() { return hitboxCenterX; }
    public float getHitboxCenterY() { return hitboxCenterY; }
    public float getHitboxRadius() { return hitboxRadius; }
    public void dispose() {
        for (Texture texture : framesArray) {
            texture.dispose();
        }
    }
}
