package ch.epfl.cs107.play.game.icwars.area;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

/**
 * Specific area
 */
public class Level1 extends ICWarsArea {

    @Override
    public String getTitle() {
        return "icwars/Level1";
    }

    public DiscreteCoordinates getPlayerAllySpawnPosition() { return new DiscreteCoordinates(2,5); }

    public DiscreteCoordinates getPlayerEnemySpawnPosition() { return new DiscreteCoordinates(17,5); }

    public DiscreteCoordinates getTankAllySpawnPosition() { return new DiscreteCoordinates(2,5); }

    public DiscreteCoordinates getTankEnemySpawnPosition() { return new DiscreteCoordinates(8,5); }

    public DiscreteCoordinates getSoldierAllySpawnPosition() { return new DiscreteCoordinates(3,5); }

    public DiscreteCoordinates getSoldierEnemySpawnPosition() { return new DiscreteCoordinates(9,5); }

    protected void createArea() {registerActor(new Background(this));}
}

