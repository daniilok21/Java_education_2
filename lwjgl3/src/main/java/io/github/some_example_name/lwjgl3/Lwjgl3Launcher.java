package io.github.some_example_name.lwjgl3;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import io.github.some_example_name.MyGdxGame;

public class Lwjgl3Launcher {
    public static void main(String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setForegroundFPS(60);
        config.setWindowedMode(1280, 720);
        config.setTitle("FlappyBird");
        new Lwjgl3Application(new MyGdxGame(), config);
    }
}
