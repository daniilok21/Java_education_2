package io.github.some_example_name.screens;

import static io.github.some_example_name.MyGdxGame.SCR_HEIGHT;
import static io.github.some_example_name.MyGdxGame.SCR_WIDTH;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import java.util.Timer;
import java.util.TimerTask;

import io.github.some_example_name.MyGdxGame;
import io.github.some_example_name.characters.Bird;
import io.github.some_example_name.characters.Boss;
import io.github.some_example_name.characters.Fireball;
import io.github.some_example_name.characters.Flower;
import io.github.some_example_name.characters.Tube;
import io.github.some_example_name.components.MovingBackground;
import io.github.some_example_name.components.PointCounter;

public class ScreenGame implements Screen {

    final int pointCounterMarginTop = 60;
    final int pointCounterMarginRight = 400;

    MyGdxGame myGdxGame;

    Bird bird;
    PointCounter pointCounter;
    MovingBackground background;
    Flower flower;
    Boss boss;
    private ShapeRenderer shapeRenderer;
    int tubeCount = 3;
    Tube[] tubes;

    int gamePoints;
    boolean isGameOver;
    boolean bossTransition;
    boolean bossFight;

    float timeTime = 0f;
    private Timer timer;
    boolean firstTask = true;
    boolean task1Compleated = false;
    private static final float frameTime = 1/60f;
    Sound deathSound = Gdx.audio.newSound(Gdx.files.internal("sounds/death.mp3"));
    Sound transitionSound = Gdx.audio.newSound(Gdx.files.internal("sounds/transition.mp3"));
    Music backgroundMusic;
    Music bossFightMusic;
    float fpsTimer = 0f;
    final float fpsLogInterval = 1f;
    private BitmapFont fpsFont;
    int currentFPS = 0;

    public ScreenGame(MyGdxGame myGdxGame) {
        this.myGdxGame = myGdxGame;

        initTubes();
        background = new MovingBackground("backgrounds/game_bg.png");
        bird = new Bird(20, SCR_HEIGHT / 2, 250, 200);
        pointCounter = new PointCounter(SCR_WIDTH - pointCounterMarginRight, SCR_HEIGHT - pointCounterMarginTop);
        flower = new Flower(0, SCR_HEIGHT + 128, 70, 130);
        boss = new Boss(0, 0, 128, 128);
        this.shapeRenderer = new ShapeRenderer();
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/jumper.mp3"));
        bossFightMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/boss_fight.mp3"));
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(0.5f); // 0.0f - 1.0f
        bossFightMusic.setVolume(0.5f); // 0.0f - 1.0f
        this.fpsFont = new BitmapFont();
        this.fpsFont.setColor(Color.WHITE);
        this.fpsFont.getData().setScale(1.0f);
    }
    public void startTimer(int timee, int idOfTask) {
        // 1 - смена бг для боссфайта + боссфайт + фаеры
        // 2 - лазеры
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (firstTask) {
                    firstTask = false;
                }
                else {
                    System.out.println("ID = " + idOfTask);
                    if (idOfTask == 1) {
                        task1Compleated = true;
                        bossFight = true;
                    }
                    else if (idOfTask == 2) {
                        boss.initLasers(-30f, -10f, 30f, 10f, 0.1f);
                        System.out.println("HELLO");
                    }
                    firstTask = true;
                    stopTimer();
                }
            }
        }, 0, timee); // Интервал в timee миллисекунд (timee / 1000 секунд)
    }
    public void stopTimer() {
        if (timer != null) {
            timer.cancel();
        }
    }

    @Override
    public void show() {
        gamePoints = 0;
        isGameOver = false;
        bossTransition = false;
        bossFight = false;
        task1Compleated = false;
        firstTask = true;
        bird.setY(SCR_HEIGHT / 2);
        bird.setSpeedY();
        flower.setPos(0, SCR_HEIGHT + 128);
        flower.swtichActiveFalse();
        boss.setPosition(SCR_WIDTH * 0.9f - boss.getWidth(), SCR_HEIGHT / 2f - boss.getHeight() / 2f);
        boss.resetLasers();
        boss.resetFireballs();
        boss.resetLava();
        stopTimer();
        initTubes();
        backgroundMusic.play();
        background.changeBG("backgrounds/game_bg.png");
        fpsTimer = 0f;
        bird.changeGravity(-800f);
    }

    @Override
    public void render(float delta) {
        timeTime += delta;
        fpsTimer += delta;
        if (fpsTimer >= fpsLogInterval) {
            currentFPS = Gdx.graphics.getFramesPerSecond();
            fpsTimer = 0f;
        }
        if (Gdx.input.justTouched()) {
            Vector3 touch = myGdxGame.camera.unproject(
                new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0)
            );
            if (flower.isHit((int) touch.x, (int) touch.y) && !bossTransition) {
                bossTransition = true;
                // ДОПИСАТЬ
                System.out.println("ЦВЯТОЧЕК");
                for (int i = 0; i < tubeCount; i++) {
                    tubes[i].dispose();
                }
                background.changeBG("backgrounds/transition_bg.png");
                backgroundMusic.stop();
                startTimer(2700, 1);
                transitionSound.play();
            }
            else {
                bird.onClick();
            }
        }
        while (timeTime >= frameTime) {
            timeTime -= frameTime;
            if (isGameOver) {
                deathSound.play();
                backgroundMusic.stop();
                bossFightMusic.stop();
                stopTimer();
                myGdxGame.screenRestart.gamePoints = gamePoints;
                myGdxGame.setScreen(myGdxGame.screenRestart);
            }

            background.move();
            bird.fly(frameTime);
            if (!bird.isInField()) {
                System.out.println("not in field");
                isGameOver = true;
            }
            for (Tube tube : tubes) {
                tube.move();
                if (tube.getTubeIndex() == flower.getTargetTubeIndex()) {
                    if (!flower.getIsActive()) {
                        flower.swtichActiveTrue();
                        flower.setPos(tube.getBottomTubeX() + tube.getTubeWidth() / 2f - 15, tube.getYForFlower());
                    }
                    flower.move();
                }
                if (tube.isHit(bird) && !bossTransition && !bossFight) {
                    isGameOver = true;
                    System.out.println("hit");
                } else if (tube.needAddPoint(bird)) {
                    gamePoints += 1;
                    tube.setPointReceived();
                    System.out.println(gamePoints);
                }
            }
            if (bossFight) {
                if (boss.getLasersCollisionActive()) {
                    boss.updateLasers(frameTime);
                }
                if (boss.checkLaserCollision(bird)) {
                    System.out.println("Laser hit!");
                    isGameOver = true;
                }
                if (boss.checkFireballCollision(bird)) {
                    System.out.println("Fireball hit!");
                    isGameOver = true;
                }
               if (boss.checkLavaCollision(bird)) {
                   System.out.println("Lava hit!");
                   isGameOver = true;
               }
                if (bossTransition) {
                    bossFightMusic.play();
                    background.changeBG("backgrounds/boss_bg.png");
                    bossTransition = false;
                    // boss.initLava(bird, SCR_HEIGHT / 3, 1, -3000f);
                    // boss.initFireball(0, SCR_WIDTH, 20);
                    // boss.initFireball(1, SCR_WIDTH + 50, 20);
                    // boss.initFireball(2, SCR_WIDTH + 100, 20);
                    // startTimer(5000, 2);
                    // boss.initFireball(3);
                    // boss.initFireball(4);
                    // boss.initLasers(-30, -10, 30, 10, 0.1f);
                    System.out.println("Подготовка к боссфайту завершена!");
                }
            }
            else if (bossTransition) {
                if (task1Compleated) {
                    background.changeBG("backgrounds/game_bg.png");
                    task1Compleated = false;
                }
            }
        }
        ScreenUtils.clear(1, 0, 0, 1);
        myGdxGame.camera.update();
        myGdxGame.batch.setProjectionMatrix(myGdxGame.camera.combined);

        myGdxGame.batch.begin();
        background.draw(myGdxGame.batch);
        if (!(bossTransition || bossFight)) {
            for (Tube tube : tubes) tube.draw(myGdxGame.batch);
            flower.draw(myGdxGame.batch);
            pointCounter.draw(myGdxGame.batch, gamePoints);
        }
        if (!bossTransition ) {
            bird.draw(myGdxGame.batch);
        }
        else {
            bird.setY(SCR_HEIGHT / 2);
            bird.setSpeedY();
        }
        if (bossFight) {
            boss.draw(myGdxGame.batch);
            boss.renderFireball(myGdxGame.batch);
            boss.renderLava(myGdxGame.batch);
        }

        float fontHeight = fpsFont.getData().capHeight - 10;
        fpsFont.draw(myGdxGame.batch, "FPS: " + currentFPS, 0, SCR_HEIGHT - fontHeight);

        myGdxGame.batch.end();
        if (bossFight) {
            shapeRenderer.setProjectionMatrix(myGdxGame.camera.combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            boss.renderLasers(shapeRenderer);

            shapeRenderer.end();
        }
        showHitboxes();
    }

    private void showHitboxes() {
        shapeRenderer.setProjectionMatrix(myGdxGame.camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(bird.getHeadX(), bird.getHeadY(), bird.getHeadWidth(), bird.getHeadHeight());
        shapeRenderer.setColor(Color.BLUE);
        shapeRenderer.rect(bird.getBodyX(), bird.getBodyY(), bird.getBodyWidth(), bird.getBodyHeight());
        shapeRenderer.end();
    }
    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        stopTimer();
    }

    @Override
    public void dispose() {
        stopTimer();
        bird.dispose();
        background.dispose();
        pointCounter.dispose();
        for (int i = 0; i < tubeCount; i++) {
            tubes[i].dispose();
        }
        flower.dispose();
        boss.dispose();
        deathSound.dispose();
        backgroundMusic.dispose();
        transitionSound.dispose();
        bossFightMusic.dispose();
        shapeRenderer.dispose();
        fpsFont.dispose();
    }

    void initTubes() {
        tubes = new Tube[tubeCount];
        for (int i = 0; i < tubeCount; i++) {
            tubes[i] = new Tube(tubeCount, i);
        }
    }
}
