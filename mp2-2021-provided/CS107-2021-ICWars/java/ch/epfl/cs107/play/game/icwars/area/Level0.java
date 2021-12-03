package ch.epfl.cs107.play.game.icwars.area;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Foreground;
import ch.epfl.cs107.play.game.tutosSolution.actor.SimpleGhost;
import ch.epfl.cs107.play.game.icwars.area.ICWarsArea;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;

/**
 * Specific area
 */
public class Level0 extends ICWarsArea {

    @Override
    public String getTitle() {
        return "icwars/Level0";
    }

    @Override
    public DiscreteCoordinates getPlayerSpawnPosition() {
        return new DiscreteCoordinates(5,15);
    }

    protected void createArea() {
        // Base

        registerActor(new Background(this)) ;
        registerActor(new Foreground(this)) ;
        registerActor(new SimpleGhost(new Vector(20, 10), "ghost.2"));
    }



}
