package ch.epfl.cs107.play.game.icwars.area;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Foreground;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

/**
 * Specific area
 */
public class Level1 extends ICWarsArea {

    @Override
    public String getTitle() {
        return "icwars/Level1";
    }

    public DiscreteCoordinates getPlayerSpawnPosition() {return new DiscreteCoordinates(2,5);}

    protected void createArea() {
        // Base
        registerActor(new Background(this));
    }
}

