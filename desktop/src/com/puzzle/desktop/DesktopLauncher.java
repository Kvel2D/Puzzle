package com.puzzle.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.puzzle.Constants;
import com.puzzle.Main;

public class DesktopLauncher
{
    public static void main(String[] arg)
    {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = Constants.VIEWPORT_WIDTH;
        config.height = Constants.VIEWPORT_HEIGHT;
        config.resizable = false;
        config.title = "Puzzle";

        new LwjglApplication(new Main(), config);
    }
}

