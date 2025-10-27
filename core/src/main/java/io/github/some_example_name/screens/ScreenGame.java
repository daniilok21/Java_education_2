package io.github.some_example_name.screens;

import static io.github.some_example_name.MyGdxGame.SCR_HEIGHT;
import static io.github.some_example_name.MyGdxGame.SCR_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.audio.Music;

import io.github.some_example_name.MyGdxGame;
import io.github.some_example_name.characters.Bird;
import io.github.some_example_name.characters.Boss;
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

    int tubeCount = 3;
    Tube[] tubes;

    int gamePoints;
    boolean isGameOver;
    boolean bossFight;
    float timeTime = 0f;
    private static final float frameTime = 1/60f;
    Sound deathSound = Gdx.audio.newSound(Gdx.files.internal("sounds/death.mp3"));
    Music backgroundMusic;

    public ScreenGame(MyGdxGame myGdxGame) {
        this.myGdxGame = myGdxGame;

        initTubes();
        background = new MovingBackground("backgrounds/game_bg.png");
        bird = new Bird(20, SCR_HEIGHT / 2, 250, 200);
        pointCounter = new PointCounter(SCR_WIDTH - pointCounterMarginRight, SCR_HEIGHT - pointCounterMarginTop);
        flower = new Flower(0, SCR_HEIGHT + 128, 70, 130);
        boss = new Boss(0, SCR_HEIGHT + 128, 128, 128);

        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/jumper.mp3"));
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(0.5f); // 0.0f - 1.0f
    }


    @Override
    public void show() {
        gamePoints = 0;
        isGameOver = false;
        bossFight = false;
        bird.setY(SCR_HEIGHT / 2);
        bird.setSpeedY();
        flower.setPos(0, SCR_HEIGHT + 128);
        flower.swtichActiveFalse();
        boss.setPosition(0, SCR_HEIGHT + 128);
        initTubes();
        backgroundMusic.play();
    }

    @Override
    public void render(float delta) {
        timeTime += delta;
        if (Gdx.input.justTouched()) {
            Vector3 touch = myGdxGame.camera.unproject(
                new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0)
            );
            if (flower.isHit((int) touch.x, (int) touch.y) && !bossFight) {
                bossFight = true;
                // ДОПИСАТЬ
                System.out.println("ЦВЯТОЧЕК");
                boss.setPosition(SCR_WIDTH * 0.9f - boss.getWidth(), SCR_HEIGHT / 2f - boss.getHeight() / 2f);
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
                        flower.setPos(tube.getBottomTubeX() + tube.getTubeWidth() / 2 - 15, tube.getYForFlower());
                    }
                    flower.move();
                }
                if (tube.isHit(bird)) {
                    isGameOver = true;
                    System.out.println("hit");
                } else if (tube.needAddPoint(bird)) {
                    gamePoints += 1;
                    tube.setPointReceived();
                    System.out.println(gamePoints);
                }
            }
        }
        ScreenUtils.clear(1, 0, 0, 1);
        myGdxGame.camera.update();
        myGdxGame.batch.setProjectionMatrix(myGdxGame.camera.combined);
        myGdxGame.batch.begin();

        background.draw(myGdxGame.batch);
        flower.draw(myGdxGame.batch);
        boss.draw(myGdxGame.batch);
        bird.draw(myGdxGame.batch);
        for (Tube tube : tubes) tube.draw(myGdxGame.batch);
        pointCounter.draw(myGdxGame.batch, gamePoints);

        myGdxGame.batch.end();

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

    }

    @Override
    public void dispose() {
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
    }

    void initTubes() {
        tubes = new Tube[tubeCount];
        for (int i = 0; i < tubeCount; i++) {
            tubes[i] = new Tube(tubeCount, i);
        }
    }

}
