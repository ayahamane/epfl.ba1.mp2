package ch.epfl.cs107.play.game.icwars.menu;

import ch.epfl.cs107.play.game.actor.Graphics;
import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class StartGame implements Graphics {
    private Vector anchor = new Vector(.0f, .0f);
    private ImageGraphics startGame;

    public StartGame (){
        startGame = new ImageGraphics(ResourcePath.getForeground("startGame"),5, 6,
                null,anchor,1f,3001f, true);
    }

    /**
     * Displays the title of the game as its start
     * @param canvas target, not null
     */
    @Override
    public void draw(Canvas canvas) {
        startGame.draw(canvas);
    }
}
