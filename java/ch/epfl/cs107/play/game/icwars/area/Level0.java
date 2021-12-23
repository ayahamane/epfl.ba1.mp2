package ch.epfl.cs107.play.game.icwars.area;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

/**
 * Specific area
 */
public class Level0 extends ICWarsArea {

    protected void createArea() {registerActor(new Background(this)) ;}
    /**
     * Returns the title of the game.
     * @return
     */
    @Override
    public String getTitle() {
        return "icwars/Level0";
    }

    public DiscreteCoordinates getPlayerAllySpawnPosition() { return new DiscreteCoordinates(0,0); }

    public DiscreteCoordinates getPlayerEnemySpawnPosition() { return new DiscreteCoordinates(7,4); }

    public DiscreteCoordinates getTankAllySpawnPosition() { return new DiscreteCoordinates(2,5); }

    public DiscreteCoordinates getTankEnemySpawnPosition() { return new DiscreteCoordinates(8,5); }

    public DiscreteCoordinates getSoldierAllySpawnPosition() { return new DiscreteCoordinates(3,5); }

    public DiscreteCoordinates getSoldierEnemySpawnPosition() { return new DiscreteCoordinates(9,5); }
}
