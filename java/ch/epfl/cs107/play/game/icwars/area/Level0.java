package ch.epfl.cs107.play.game.icwars.area;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

/**
 * Specific area
 */
public class Level0 extends ICWarsArea {

    @Override
    public String getTitle() {
        return "icwars/Level0";
    }

    public DiscreteCoordinates getPlayerSpawnPosition() {
        return new DiscreteCoordinates(0,0);
    }

    protected void createArea() {registerActor(new Background(this)) ;}
}
