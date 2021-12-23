package ch.epfl.cs107.play.game.icwars.area;


import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.icwars.actor.ICWarsActor;
import ch.epfl.cs107.play.game.icwars.actor.unit.Unit;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Window;

import java.util.ArrayList;
import java.util.List;

import static ch.epfl.cs107.play.math.DiscreteCoordinates.distanceBetween;

public abstract class ICWarsArea extends Area {
    private List<Unit> unitInArea = new ArrayList<>();
    private ICWarsBehavior behavior;
    private final static float CAMERA_SCALE_FACTOR = 10.f;

    /**
     * Create the area by adding it all actors
     * called by begin method
     * Note it set the Behavior as needed !
     */
    protected abstract void createArea();


    /**
     * Gets the camera scale factor.
     * @return
     */
    @Override
    public final float getCameraScaleFactor() {
        return CAMERA_SCALE_FACTOR;
    }

    /**
     * Returns the position of an ally player.
     * @return
     */
    public abstract DiscreteCoordinates getPlayerAllySpawnPosition();

    /**
     * Returns the position of an enemy player.
     * @return
     */
    public abstract DiscreteCoordinates getPlayerEnemySpawnPosition();

    /**
     *Returns the position of an ally tank.
     * @return
     */
    public abstract DiscreteCoordinates getTankAllySpawnPosition();

    /**
     * Returns the position of an enemy tank.
     * @return
     */
    public abstract DiscreteCoordinates getTankEnemySpawnPosition();

    /**
     * Returns the position of an ally soldier.
     * @return
     */
    public abstract DiscreteCoordinates getSoldierAllySpawnPosition();

    /**
     * Returns the position of an enemy soldier.
     * @return
     */
    public abstract DiscreteCoordinates getSoldierEnemySpawnPosition();


    @Override
    public boolean begin(Window window, FileSystem fileSystem) {
        if (super.begin(window, fileSystem)) {
            behavior = new ICWarsBehavior(window, getTitle());
            setBehavior(behavior);
            createArea();
            return true;
        }
        return false;
    }

    /**
     * Lists all the units of the area
     * @param unitToMemorise
     */
    public void addToUnitInArea(Unit unitToMemorise) {
        unitInArea.add(unitToMemorise);
    }

    /**
     * Method that selects the potentially attackable units.
     * @param faction   player's to attack faction
     * @param radius    unit's to attack radius of
     * @param positionX  position x of the unit to attack
     * @param positionY  position y of the unit to attack
     * @return    a list of the potentially attackable units
     */
    public ArrayList<Integer> attackableUnits(ICWarsActor.Faction faction, int radius, int positionX, int positionY) {
        ArrayList<Integer> unitsToAttack = new ArrayList<>();
        for (int index = 0; index < unitInArea.size(); ++index) {
            //test if the unit's is an enemy
            if (unitInArea.get(index).getFaction() != faction) {
                int x = unitInArea.get(index).getCurrentCells().get(0).x;
                int y = unitInArea.get(index).getCurrentCells().get(0).y;
                double distance = Math.sqrt(Math.pow(positionX - x, 2) + Math.pow(positionY - y, 2));
                //test if the unit is in the attackable radius
                if (distance <= radius) {
                    unitsToAttack.add(index);
                }
            }
        }
        return unitsToAttack;
    }

    /**
     * Method that selects the potentially attackable units for the AIPlayer.
     * @param attackableUnits
     * @return
     */
    public int autoAttackableUnit(ArrayList<Integer> attackableUnits) {
        int previousValue = Integer.MAX_VALUE;
        int unitToAutoAttack = 0;
        for (int index = 0; index < attackableUnits.size(); ++index) {
                if((int)unitInArea.get(attackableUnits.get(index)).getHp()<previousValue){
                    unitToAutoAttack = index;
                    previousValue = (int)unitInArea.get(attackableUnits.get(index)).getHp();
            }
        }
        return unitToAutoAttack;
    }

    /**
     * Applies damage to a specific unit to attack.
     * @param unitToAttack   the index of the unit to attack
     * @param damage         the damage to apply to it
     */
    public void applyDamage(int unitToAttack, int damage) {
        unitInArea.get(unitToAttack).undergoDamage(damage);
        if (unitInArea.get(unitToAttack).isDead()) {
            unitInArea.remove(unitToAttack);
        }
    }

    /**
     * Centers on unit.
     * @param index     the index of the unit to be centered on.
     */
    public void centerOnUnit(int index) {
        unitInArea.get(index).centerCamera();
    }

    /**
     * Returns the position of the nearest unit to attack
     * @param unitAi selected by AI player
     * @return coordinates of the nearest unit
     */
    public DiscreteCoordinates getCoordsNearestUnit(Unit unitAi){
        DiscreteCoordinates coordsNearestUnit = new DiscreteCoordinates(0,0);
        double previousDistance = 0;
        for (int index = 0; index < unitInArea.size(); ++index) {
            if (unitAi.getFaction() != unitInArea.get(index).getFaction()) {
                DiscreteCoordinates unitAiPosition = new DiscreteCoordinates((int) unitAi.getPosition().x,
                        (int) unitAi.getPosition().y);
                DiscreteCoordinates unitInAreaPosition = new DiscreteCoordinates((int) unitInArea.get(index).getPosition().x,
                        (int) unitInArea.get(index).getPosition().y);
                double potentialDistance = distanceBetween(unitAiPosition, unitInAreaPosition);
                if (index == 0) {
                    previousDistance = potentialDistance;
                    coordsNearestUnit = unitInAreaPosition;
                } else {
                    if (potentialDistance < previousDistance) {
                        previousDistance = potentialDistance;
                        coordsNearestUnit = unitInAreaPosition;
                    }
                }
            }
        }
        return coordsNearestUnit;
    }

    @Override
    public String getTitle() {
        return null;
    }
}

