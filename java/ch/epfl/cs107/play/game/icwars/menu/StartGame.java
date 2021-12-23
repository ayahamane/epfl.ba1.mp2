package ch.epfl.cs107.play.game.icwars.menu;

import ch.epfl.cs107.play.game.actor.Graphics;
import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class StartGame implements Graphics {
    private Vector anchor = new Vector(.0f, .0f);
    private ImageGraphics startGame;

    public StartGame (float width, float height){
        startGame = new ImageGraphics(ResourcePath.getForeground("startGame"),width, height,
                null,anchor,1f,3001f, true);
    }

    @Override
    public void draw(Canvas canvas) {
        startGame.draw(canvas);
    }
}