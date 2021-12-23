package ch.epfl.cs107.play.game.icwars.menu;

import ch.epfl.cs107.play.game.actor.Graphics;
import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class YouWon implements Graphics {
    private Vector anchor = new Vector(2f, 3f);
    private ImageGraphics youWon;

    public YouWon (){
        youWon = new ImageGraphics(ResourcePath.getForeground("youWon"),6, 4,
                null,anchor,1f,3001f, true);
    }

    /**
     * Displays the message "You won" at the end of the game if the real player won
     * @param canvas target, not null
     */
    @Override
    public void draw(Canvas canvas) {
        youWon.draw(canvas);
    }
}
