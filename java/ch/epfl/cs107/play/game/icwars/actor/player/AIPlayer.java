package ch.epfl.cs107.play.game.icwars.actor.player;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icwars.actor.unit.Unit;
import ch.epfl.cs107.play.game.icwars.actor.unit.action.Action;
import ch.epfl.cs107.play.game.icwars.area.ICWarsArea;
import ch.epfl.cs107.play.game.icwars.area.ICWarsBehavior;
import ch.epfl.cs107.play.game.icwars.gui.ICWarsPlayerGUI;
import ch.epfl.cs107.play.game.icwars.handler.ICWarsInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;
import java.util.Collections;
import java.util.List;

import static ch.epfl.cs107.play.math.DiscreteCoordinates.distanceBetween;

public class AIPlayer extends ICWarsPlayer{
    private float counter;
    private boolean counting;
    private Sprite sprite;
    private Unit selectedUnitAi;
    private ICWarsPlayerGUI playerGUI;
    private Faction faction;
    private DiscreteCoordinates coordsNearestUnit;
    private List<Action> list;
    private Unit posUnit;
    private ICWarsBehavior.ICWarsCellType cellTypePlayer;

    public AIPlayer(ICWarsArea area, DiscreteCoordinates position, Faction fac, String spriteName, Unit... units) {
        super(area, position, fac, units);
        sprite = new Sprite(spriteName, 1.f, 1.f, this);
        this.faction = fac;
        resetMotion();
        if (fac == Faction.ally){
            sprite = new Sprite("icwars/allyCursor", 1f, 1f, this,
                    null , new Vector(0, 0));
        } else {
            sprite = new Sprite("icwars/enemyCursor", 1f, 1f, this, null ,
                    new Vector(0, 0));
        }
        playerGUI = new ICWarsPlayerGUI(getOwnerArea().getCameraScaleFactor(), this);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        changeState(deltaTime);
        coordsNearestUnit = getCurrentMainCellCoordinates();
    }

    /**
     * Returns the new position of the selected unit.
     */
    private DiscreteCoordinates newCoords(DiscreteCoordinates position){
        System.out.println(position);
//        if(0 <= position.x && position.x < getOwnerArea().getWidth()
//                && 0 <= position.y && position.y < getOwnerArea().getHeight()) {
//            if (-selectedUnitAi.getRadius() <= position.x && position.x <= selectedUnitAi.getRadius()
//                    && -selectedUnitAi.getRadius() <= position.y && position.y <= -selectedUnitAi.getRadius()) {
//                return position;
//            }
//        }
        DiscreteCoordinates abscissa1 = new DiscreteCoordinates(-selectedUnitAi.getRadius(),
                (int) selectedUnitAi.getPosition().y);
        DiscreteCoordinates abscissa2 = new DiscreteCoordinates(selectedUnitAi.getRadius(),
                (int) selectedUnitAi.getPosition().y);
        float leftEdge = distanceBetween(abscissa1, position);
        float rightEdge = distanceBetween(abscissa2, position);
        int finalAbcsissa;
        if (leftEdge < rightEdge) {
            finalAbcsissa = -selectedUnitAi.getRadius();
        } else {
            finalAbcsissa = selectedUnitAi.getRadius();
        }
        DiscreteCoordinates ordinate1 = new DiscreteCoordinates(finalAbcsissa, -selectedUnitAi.getRadius());
        DiscreteCoordinates ordinate2 = new DiscreteCoordinates(finalAbcsissa, selectedUnitAi.getRadius());
        float lowEdge = distanceBetween(ordinate1, position);
        float highEdge = distanceBetween(ordinate2, position);
        int finalOrdinate;
        float previousOrdinate;
        if (lowEdge < highEdge) {
            finalOrdinate = -selectedUnitAi.getRadius();
            previousOrdinate = lowEdge;
        } else {
            finalOrdinate = selectedUnitAi.getRadius();
            previousOrdinate = highEdge;
        }
        for (int i = finalOrdinate + 1; i < selectedUnitAi.getRadius(); ++i) {
            DiscreteCoordinates positionToTest = new DiscreteCoordinates(finalAbcsissa, i);
            if (distanceBetween(positionToTest, position) < previousOrdinate) {
                finalOrdinate = i;
            }
        }
        DiscreteCoordinates newPosition = new DiscreteCoordinates(finalAbcsissa, finalOrdinate);
        System.out.println(newPosition);
        return newPosition;
    }

    protected void changeState(float dt){
        switch (getCurrentState()){
            case IDLE:
                break;
            case NORMAL:
                selectUnitAi();
                coordsNearestUnit = ((ICWarsArea)getOwnerArea()).getCoordsNearestUnit(selectedUnitAi);
                newCoords(coordsNearestUnit);
                if(waitFor(3,dt)){
                currentState = playerState.MOVE_UNIT;}
                break;
            case MOVE_UNIT:
                selectedUnitAi.changePosition(coordsNearestUnit);
                if(waitFor(3,dt)){
                    currentState = playerState.ACTION;
                }
                break;
            case ACTION:
                list = selectedUnitAi.getListOfActions();
                selectedUnitAi.getListOfActions().get(0).doAutoAction(dt,this, list = selectedUnitAi.getListOfActions());
                currentState = playerState.IDLE;
                break;
            default:
        }
    }

    /**
     * Selects one of the units of the player
     */
    public void selectUnitAi(){
        int i = 0;
        while(i < unit.size()) {
            selectedUnitAi = unit.get(i);
            playerGUI.setSelectedUnit(selectedUnitAi);
            if (selectedUnitAi.hasBeenUsed()) {
                ++i;
            } else {
                break;
            }
        }
    }

    @Override
    public void draw(Canvas canvas) {
        if(!(getCurrentState() == playerState.IDLE)){sprite.draw(canvas);}
        playerGUI.draw(canvas);
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() { return Collections.singletonList(getCurrentMainCellCoordinates()); }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {}

    @Override
    public void interactWith(Interactable other) {}

  /**
     * Ensures that value time elapsed before returning true
     * @param dt elapsed time
     * @param value waiting time (in seconds)
     * @return true if value seconds has elapsed , false otherwise
     */
    private boolean waitFor(float value , float dt) {
        if (counting) {
            counter += dt;
            if (counter > value) {
                counting = false;
                return true;
            }
        } else {
            counter = 0f;
            counting = true;
        }
        return false;
    }

    public class ICWarsPlayerInteractionHandler implements ICWarsInteractionVisitor {
        @Override
        public void interactWith(Unit u) {
            posUnit = u;
            playerGUI.setPosUnit(u);
            if (currentState == playerState.SELECT_CELL && u.getFaction() == faction) {
                unitInMemory = u;
                //canPass = true;
                playerGUI.setSelectedUnit(u);
            }
        }
        public void interactWith(ICWarsBehavior.ICWarsCell cellType){
            cellTypePlayer = cellType.getI();
            playerGUI.setCellTypePlayerGUI(cellTypePlayer);
        }
    }
}
