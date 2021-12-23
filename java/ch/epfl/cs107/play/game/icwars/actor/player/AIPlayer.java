package ch.epfl.cs107.play.game.icwars.actor.player;

import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icwars.actor.unit.Unit;
import ch.epfl.cs107.play.game.icwars.actor.unit.action.Action;
import ch.epfl.cs107.play.game.icwars.area.ICWarsArea;
import ch.epfl.cs107.play.game.icwars.gui.ICWarsPlayerGUI;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;
import java.util.Collections;
import java.util.List;

public class AIPlayer extends ICWarsPlayer{
    private float counter;
    private boolean counting;
    private Sprite sprite;
    private Unit selectedUnitAi;
    private ICWarsPlayerGUI playerGUI;
    private Faction faction;
    private DiscreteCoordinates coordsNearestUnit;
    private List<Action> list;
    private boolean canMove;

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
    }

    /**
     * Returns the new position of the selected unit
     * @param position of the nearest unit to attack
     * @return new coordinates of the selected unit
     */
    private DiscreteCoordinates newCoords( DiscreteCoordinates position){
        int finalAbcsissa = (int)selectedUnitAi.getPosition().x;
        int finalOrdinate = (int)selectedUnitAi.getPosition().y;
        //Verify if the position of the unit in parameter is in the grid, and if that's the case,
        // returns the position in parameter as the new coordinates.
        if(0 <= position.x && position.x < getOwnerArea().getWidth()
                && 0 <= position.y && position.y < getOwnerArea().getHeight()) {
            if (selectedUnitAi.inRadius(position)){
                return new DiscreteCoordinates(finalAbcsissa, finalOrdinate);
            } else {
                //If not in the grid, test if the position is way too high or way to low, and also if it is
                //not inside the left limit or right limit and choose the extreme position in each case.
                if(position.x < selectedUnitAi.getPosition().x - selectedUnitAi.getRadius()){
                    finalAbcsissa = (int)(selectedUnitAi.getPosition().x - selectedUnitAi.getRadius());
                }
                if(position.x > selectedUnitAi.getPosition().x + selectedUnitAi.getRadius()){
                    finalAbcsissa = (int)(selectedUnitAi.getPosition().x + selectedUnitAi.getRadius());
                }
                if(position.y < selectedUnitAi.getPosition().y - selectedUnitAi.getRadius()){
                    finalOrdinate = (int)(selectedUnitAi.getPosition().y - selectedUnitAi.getRadius());
                }
                if(position.y > selectedUnitAi.getPosition().y + selectedUnitAi.getRadius()){
                    finalOrdinate = (int)(selectedUnitAi.getPosition().y + selectedUnitAi.getRadius());
                }
                return new DiscreteCoordinates(finalAbcsissa, finalOrdinate);
            }
        }
        return null;
    }

    /**
     * Describes the different states of a virtual player
     * @param dt
     */
    private void changeState(float dt){
        switch (getCurrentState()){
            case IDLE:
                break;
            case NORMAL:
                canMove = false;
                selectUnitAi();
                setCurrentPosition(selectedUnitAi.getPosition());
                if(waitFor(2,dt) && canMove){
                    coordsNearestUnit = ((ICWarsArea)getOwnerArea()).getCoordsNearestUnit(selectedUnitAi);
                    setCurrentState(playerState.MOVE_UNIT);
                }
                break;
            case MOVE_UNIT:
                selectedUnitAi.changePosition(newCoords(coordsNearestUnit));
                setCurrentPosition(selectedUnitAi.getPosition());
                if(waitFor(2,dt)) {
                    setCurrentState(playerState.ACTION);
                }
                break;
            case ACTION:
                list = selectedUnitAi.getListOfActions();
                selectedUnitAi.getListOfActions().get(0).doAutoAction(dt,this, list);
                selectedUnitAi.setHasBeenUsed(true);
                setCurrentState(playerState.IDLE);
                break;
            default:
        }
    }

    /**
     * Selects one of the units of the AI player
     */
    public void selectUnitAi() {
        for (int i = 0; i < getUnits().size(); ++i) {
            if (!getUnits().get(i).isDead()) {
                selectedUnitAi = getUnits().get(i);
                playerGUI.setSelectedUnit(selectedUnitAi);
                canMove = true;
                break;
            }
        }
    }

    /**
     * Draws the AI player if it is not in the state Idle.
     * @param canvas
     */
    @Override
    public void draw(Canvas canvas) {
        if(!(getCurrentState() == playerState.IDLE)){sprite.draw(canvas);}
        playerGUI.draw(canvas);
    }

    /**
     * Gets the current cells of the AI player
     * @return
     */
    @Override
    public List<DiscreteCoordinates> getCurrentCells() { return Collections.singletonList(getCurrentMainCellCoordinates()); }

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

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {}

    @Override
    public void interactWith(Interactable other) {}
}












