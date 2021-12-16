package ch.epfl.cs107.play.game.icwars.area;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.icwars.actor.ICWarsActor;
import ch.epfl.cs107.play.game.icwars.actor.unit.Unit;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Window;

import java.util.ArrayList;
import java.util.List;

public abstract class ICWarsArea extends Area{
    private List<Unit> unitInArea;
    private List<Integer> unitsToAttack;
    private ICWarsBehavior behavior;
    private final static float CAMERA_SCALE_FACTOR = 10.f;
    //The camera scale factor was in ICWars at first,
    // but in the subject she said that it should be here.
    //Also,I changed it to a private instead of a public
    /**
     * Create the area by adding it all actors
     * called by begin method
     * Note it set the Behavior as needed !
     */
    protected abstract void createArea();
    /// EnigmeArea extends Area
    @Override
    public final float getCameraScaleFactor() {return CAMERA_SCALE_FACTOR;}

    //Améliorer ça avec new tableau d'unités
    public abstract DiscreteCoordinates getPlayerAllySpawnPosition();

    public abstract DiscreteCoordinates getPlayerEnemySpawnPosition();

    public abstract DiscreteCoordinates getTankAllySpawnPosition();

    public abstract DiscreteCoordinates getTankEnemySpawnPosition();

    public abstract DiscreteCoordinates getSoldierAllySpawnPosition();

    public abstract DiscreteCoordinates getSoldierEnemySpawnPosition();

   // Demo2Area implements Playable
    @Override
    public boolean begin(Window window, FileSystem fileSystem) {
        if (super.begin(window, fileSystem)) {
            // Set the behavior map
            behavior = new ICWarsBehavior(window, getTitle());
            setBehavior(behavior);
            createArea();
            return true;
        }
        return false;
    }

    @Override
    public String getTitle() {return null;}

    /**
     * Lists all the units of the area
     */
    public void addToUnitInArea(Unit unitToMemorise){
        unitInArea = new ArrayList<Unit>();
        unitInArea.add(unitToMemorise);
    }


    /**
     * Method that selects the potentially attackable units.
     * @param faction
     * @param radius
     * @return
     */
    public List<Integer> attackableUnits(ICWarsActor.Faction faction, int radius){
        unitsToAttack = new ArrayList<>();
        for (int i = 0; i < unitInArea.size(); ++i) {
            if (unitInArea.get(i).getFaction() != faction){
                if (true){    //je ne sais pas encore exactement comment faire
                    //cette condition.
                    unitsToAttack.add(i);
                }
            }
        }
        return unitsToAttack;
    }

    public void applyDamage(int unitToAttack, int damage, int stars){
        unitInArea.get(unitToAttack).undergoDamage(damage, stars);
    }


}
