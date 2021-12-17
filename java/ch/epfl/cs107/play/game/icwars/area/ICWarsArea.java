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
    private ArrayList<Integer> unitsToAttack;
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

    public abstract DiscreteCoordinates getPlayerAllySpawnPosition();
    //I added those methods, can I improve these?
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
    public ArrayList<Integer> attackableUnits(ICWarsActor.Faction faction, int radius, int positionX, int positionY){
        unitsToAttack = new ArrayList<>();
        for (int index = 0; index < unitInArea.size(); ++index) {
            if (unitInArea.get(index).getFaction() != faction){
                int x =  unitInArea.get(index).getCurrentCells().get(0).x;
                int y = unitInArea.get(index).getCurrentCells().get(0).y;
                //D'après Piazza la condition qui suit marcherait mais il y a peut-être mieux à faire.
                double distance = Math.sqrt(Math.pow(positionX - x, 2) + Math.pow(positionY - y, 2));
                if (distance <= radius){
                    unitsToAttack.add(index);
                }
            }
        }
        return unitsToAttack;
    }

    public void applyDamage(int unitToAttack, int damage){
        unitInArea.get(unitToAttack).undergoDamage(damage);
    }

    public void centerOnUnit(int index){
        unitInArea.get(index).centerCamera();
    }
}
