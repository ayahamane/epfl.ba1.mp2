package ch.epfl.cs107.play.game.icwars.actor.player;

import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icwars.actor.unit.action.Action;
import ch.epfl.cs107.play.game.icwars.actor.unit.action.Attack;
import ch.epfl.cs107.play.game.icwars.area.ICWarsArea;
import ch.epfl.cs107.play.game.icwars.actor.unit.Unit;
import ch.epfl.cs107.play.game.icwars.gui.ICWarsPlayerGUI;
import ch.epfl.cs107.play.game.icwars.handler.ICWarsInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RealPlayer extends ICWarsPlayer{

    private Sprite sprite;
    private ICWarsPlayerGUI playerGUI;
    private final static int MOVE_DURATION = 1;
    private Unit selectedUnit;
    private Keyboard keyboard= getOwnerArea().getKeyboard();
    private ICWarsPlayerInteractionHandler handler;
    private Faction faction;
    private boolean canPass = false;
    private Action actionToExecute;

    /**
     * Demo actor
     *
     */
    public RealPlayer(ICWarsArea area, DiscreteCoordinates position, Faction fac,
                      String spriteName, Unit... unit) {
        super(area, position, fac, unit);
        sprite = new Sprite(spriteName, 1.f, 1.f, this);
        this.handler = new ICWarsPlayerInteractionHandler();
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
        Keyboard keyboard = getOwnerArea().getKeyboard();
        if (getCurrentState() == playerState.NORMAL || getCurrentState() == playerState.SELECT_CELL ||
                getCurrentState() == playerState.MOVE_UNIT){  //je sais qu'on peut rendre ça moins long mais
            //jsais plus cmt ^^. J'y reviendrai plus tard.
            //Can I improve this method?
            moveIfPressed(Orientation.UP, keyboard.get(Keyboard.UP));
            moveIfPressed(Orientation.LEFT, keyboard.get(Keyboard.LEFT));
            moveIfPressed(Orientation.RIGHT, keyboard.get(Keyboard.RIGHT));
            moveIfPressed(Orientation.DOWN, keyboard.get(Keyboard.DOWN));
        }
        changeState(deltaTime);
    }

    protected void changeState(float dt){
        switch (getCurrentState()){
            case IDLE:
                break;
            case NORMAL:
                if (keyboard.get(Keyboard.ENTER).isReleased()) {
                    currentState = playerState.SELECT_CELL; }
                if (keyboard.get(Keyboard.TAB).isReleased()) {
                    currentState = playerState.IDLE;}
                break;
            case SELECT_CELL:
                if (canPass && !unitInMemory.hasBeenUsed()){
                    currentState = playerState.MOVE_UNIT; }
                break;
            case MOVE_UNIT:
             if (keyboard.get(Keyboard.ENTER).isReleased()) {
                    if (unitInMemory.changePosition(this.getCurrentMainCellCoordinates())) {
                        unitInMemory.setHasBeenUsed(true);
                        //currentState = playerState.NORMAL;
                        canPass = false;
                    }
                    if (unitInMemory.hasBeenUsed()){
                        currentState = playerState.ACTION_SELECTION;
                    }
                }
                break;
            case ACTION_SELECTION:
                List<Action> list= new ArrayList<>();
                for(int i = 0; i< unitInMemory.getListOfActionsSize(); ++i){
                    list.add(unitInMemory.getElementListOfActions(i));
                }
                for( int i=0; i<list.size(); ++i){
                    int theKey = list.get(i).getKey();
                    if(keyboard.get(theKey).isReleased()){
                        actionToExecute = list.get(i) ;
                        currentState = playerState.ACTION;
                    }
                }
                break;
            case ACTION:
                //NEW:
                actionToExecute.doAction(dt,this, keyboard);
                break;
            default:
        }
    }

    /**
     * Orientate and Move this player in the given orientation if the given button is down
     * @param orientation (Orientation): given orientation, not null
     * @param b (Button): button corresponding to the given orientation, not null
     */
    private void moveIfPressed(Orientation orientation, Button b){
        if(b.isDown()) {
            if (!isDisplacementOccurs()) {
                orientate(orientation);
                move(MOVE_DURATION);
            }
        }
    }

    /**
     * Selects one of the units of the player
     */
    public void selectUnit(int index){
        if(index < unit.size()){
            selectedUnit = unit.get(index);
            playerGUI.setSelectedUnit(selectedUnit);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        if(!(getCurrentState() == playerState.IDLE)){sprite.draw(canvas);}
        if((!(unitInMemory == null)) && (getCurrentState() == playerState.MOVE_UNIT)){
            playerGUI.draw(canvas);}
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() { return Collections.singletonList(getCurrentMainCellCoordinates()); }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
        ((ICWarsInteractionVisitor)v).interactWith(this);}

    @Override
    public void interactWith(Interactable other) {
        if (!isDisplacementOccurs()) {
            other.acceptInteraction(handler);
        }
    }
    public class ICWarsPlayerInteractionHandler implements ICWarsInteractionVisitor {
        @Override
        public void interactWith(Unit u) {
            if (currentState == playerState.SELECT_CELL && u.getFaction() == faction) {
                unitInMemory = u;
                canPass = true;
                playerGUI.setSelectedUnit(u);
            }
        }
    }
}
