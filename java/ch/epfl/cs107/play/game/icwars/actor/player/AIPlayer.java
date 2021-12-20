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
     * Returns the new position of the selected unit.
     */
    private DiscreteCoordinates newCoords( DiscreteCoordinates position){
        int finalAbcsissa = (int)selectedUnitAi.getPosition().x;
        int finalOrdinate = (int)selectedUnitAi.getPosition().y;
        if(0 <= position.x && position.x < getOwnerArea().getWidth()
                && 0 <= position.y && position.y < getOwnerArea().getHeight()) {
            if (selectedUnitAi.getPosition().x - selectedUnitAi.getRadius() <= position.x
                    && position.x <= selectedUnitAi.getPosition().x + selectedUnitAi.getRadius()
                    && selectedUnitAi.getPosition().y - selectedUnitAi.getRadius() <= position.y
                    && position.y <= selectedUnitAi.getPosition().y + selectedUnitAi.getRadius()) {
                return new DiscreteCoordinates(finalAbcsissa, finalOrdinate);
            } else {
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

    protected void changeState(float dt){
        switch (getCurrentState()){
            case IDLE:
                break;
            case NORMAL:
                canMove = false;
                selectUnitAi();
                setCurrentPosition(selectedUnitAi.getPosition());
                if(waitFor(2,dt) && canMove){
                    coordsNearestUnit = ((ICWarsArea)getOwnerArea()).getCoordsNearestUnit(selectedUnitAi);
                    currentState = playerState.MOVE_UNIT;
                }
                break;
            case MOVE_UNIT:
                selectedUnitAi.changePosition(newCoords(coordsNearestUnit));
                setCurrentPosition(selectedUnitAi.getPosition());
                if(waitFor(2,dt)) {
                    currentState = playerState.ACTION;
                }
                break;
            case ACTION:
                list = selectedUnitAi.getListOfActions();
                selectedUnitAi.getListOfActions().get(0).doAutoAction(dt,this, list);
                selectedUnitAi.setHasBeenUsed(true);
                currentState = playerState.IDLE;
                break;
            default:
        }
    }

    /**
     * Selects one of the units of the player
     */
    public void selectUnitAi() {
        for (int i = 0; i < unit.size(); ++i) {
            if (!unit.get(i).isDead()) {
                selectedUnitAi = unit.get(i);
                playerGUI.setSelectedUnit(selectedUnitAi);
                canMove = true;
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
}
