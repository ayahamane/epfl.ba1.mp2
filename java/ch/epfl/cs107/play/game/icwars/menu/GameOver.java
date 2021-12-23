package ch.epfl.cs107.play.game.icwars.menu;

import ch.epfl.cs107.play.game.actor.Graphics;
import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class GameOver implements Graphics {
    private Vector anchor = new Vector(.0f, .0f);
    private ImageGraphics gameOver;


    public GameOver (float width, float height){
        gameOver = new ImageGraphics(ResourcePath.getForeground("gameOver"),width, height,
                null,anchor,1f,3001f, true);
    }

    /**
     * Displays the message "Game Over" at the end of the game if the real player lost
     * @param canvas target, not null
     */
    @Override
    public void draw(Canvas canvas) {
        gameOver.draw(canvas);
    }
}

