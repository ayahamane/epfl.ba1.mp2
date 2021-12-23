package ch.epfl.cs107.play.game.icwars.actor.player;

import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icwars.actor.unit.action.Action;
import ch.epfl.cs107.play.game.icwars.area.ICWarsArea;
import ch.epfl.cs107.play.game.icwars.actor.unit.Unit;
import ch.epfl.cs107.play.game.icwars.area.ICWarsBehavior;
import ch.epfl.cs107.play.game.icwars.gui.ICWarsPlayerGUI;
import ch.epfl.cs107.play.game.icwars.handler.ICWarsInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;
import java.util.Collections;
import java.util.List;

public class RealPlayer extends ICWarsPlayer {

    private ICWarsBehavior.ICWarsCellType cellTypePlayer;
    private Sprite sprite;
    private Unit posUnit;
    private ICWarsPlayerGUI playerGUI;
    private final static int MOVE_DURATION = 3;
    private Keyboard keyboard = getOwnerArea().getKeyboard();
    private ICWarsPlayerInteractionHandler handler;
    private Faction faction;
    //Becomes true when the interaction between the player and the unit to select has occured.
    private boolean canPass = false;
    private Action actionToExecute;
    private List<Action> list;
    private boolean tPressed = false;    //secretAttribute


    /**
     * Demo actor
     */
    public RealPlayer(ICWarsArea area, DiscreteCoordinates position, Faction fac,
                      String spriteName, Unit... unit) {
        super(area, position, fac, unit);
        sprite = new Sprite(spriteName, 1.f, 1.f, this);
        this.handler = new ICWarsPlayerInteractionHandler();
        this.faction = fac;
        resetMotion();
        if (fac == Faction.ally) {
            sprite = new Sprite("icwars/allyCursor", 1f, 1f, this,
                    null, new Vector(0, 0));
        } else {
            sprite = new Sprite("icwars/enemyCursor", 1f, 1f, this, null,
                    new Vector(0, 0));
        }
        playerGUI = new ICWarsPlayerGUI(getOwnerArea().getCameraScaleFactor(), this);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        Keyboard keyboard = getOwnerArea().getKeyboard();
        if (keyboard.get(Keyboard.T).isPressed()) {
            tPressed = true;
        }
        if (getCurrentState() == playerState.NORMAL || getCurrentState() == playerState.SELECT_CELL ||
                getCurrentState() == playerState.MOVE_UNIT) {
            moveIfPressed(Orientation.UP, keyboard.get(Keyboard.UP));
            moveIfPressed(Orientation.LEFT, keyboard.get(Keyboard.LEFT));
            moveIfPressed(Orientation.RIGHT, keyboard.get(Keyboard.RIGHT));
            moveIfPressed(Orientation.DOWN, keyboard.get(Keyboard.DOWN));
        }
        if (getCurrentState() == playerState.MOVE_UNIT) {
            if (playerGUI.getTimer() > 0) {
                playerGUI.setTimer(playerGUI.getTimer() - 1);
            }
            if (playerGUI.getTimer() < 0) playerGUI.setTimer(0);
        }
        if (getCurrentState() != playerState.MOVE_UNIT && tHasBeenPressed()) {
            playerGUI.setTimer(200);
        } else if (getCurrentState() != playerState.MOVE_UNIT) {
            playerGUI.setTimer(100);
        }
        changeState(deltaTime);
    }

    /**
     * @return true if T has been pressed during the game.
     */
    private boolean tHasBeenPressed() {
        return tPressed;
    }

    /**
     * Describes the different states of a real player
     * @param dt is the delta time
     */
    private void changeState(float dt) {
        switch (getCurrentState()) {
            case IDLE:
                break;
            case NORMAL:
                posUnit = null;
                actionToExecute = null;
                if (keyboard.get(Keyboard.ENTER).isReleased()) {
                    setCurrentState(playerState.SELECT_CELL);
                }
                if (keyboard.get(Keyboard.TAB).isReleased()) {
                    setCurrentState(playerState.IDLE);
                }
                int count = 0;
                //If the RealPlayer has no units available to select, his state becomes IDLE.
                for (int i = 0; i < getUnits().size(); ++i) {
                    if (getUnits().get(i).hasBeenUsed()) {
                        ++count;
                    }
                }
                if (count == getUnits().size()) {
                    setCurrentState(playerState.IDLE);
                }
                break;
            case SELECT_CELL:
                if (canPass && !getUnitInMemory().hasBeenUsed()) {
                    setCurrentState(playerState.MOVE_UNIT);
                }
                break;
            case MOVE_UNIT:
                if (keyboard.get(Keyboard.ENTER).isReleased()) {
                    if (getUnitInMemory().changePosition(this.getCurrentMainCellCoordinates())) {
                        setCurrentState(playerState.ACTION_SELECTION);
                        canPass = false;
                    }
                }
                if (playerGUI.getTimer() == 0) {
                    setCurrentState(playerState.IDLE);
                }
                break;
            case ACTION_SELECTION:
                list = getUnitInMemory().getListOfActions();
                for (int i = 0; i < list.size(); ++i) {
                    int theKey = list.get(i).getKey();
                    if (keyboard.get(theKey).isReleased()) {
                        actionToExecute = list.get(i);
                        setCurrentState(playerState.ACTION);
                    }
                }
                break;
            case ACTION:
                actionToExecute.doAction(dt, this, keyboard);
                break;
            default:
        }
    }


    /**
         * Orientate and Move this player in the given orientation if the given button is down
         * @param orientation (Orientation): given orientation, not null
         * @param b (Button): button corresponding to the given orientation, not null
         */
        private void moveIfPressed (Orientation orientation, Button b){
            if (b.isDown()) {
                if (!isDisplacementOccurs()) {
                    orientate(orientation);
                    move(MOVE_DURATION);
                }
            }
        }

        /**
         * Draws the player if he is not defeated and is in the state IDLE. Represents also the action exectuded
         * and all what is related to its movement.
         * @param canvas
         */
        @Override
        public void draw (Canvas canvas){
            if (!(getCurrentState() == playerState.IDLE) && !this.isDefeated()) {
                sprite.draw(canvas);
            }
            playerGUI.draw(canvas);
            if (actionToExecute != null) {
                actionToExecute.draw(canvas);
            }
        }

        /**
         * @return current cells of the player
         */
        @Override
        public List<DiscreteCoordinates> getCurrentCells () {
            return Collections.singletonList(getCurrentMainCellCoordinates());
        }

        /**
         * Accepts interaction between a player and a visitor.
         * @param v (AreaInteractionVisitor) : the visitor
         */
        @Override
        public void acceptInteraction (AreaInteractionVisitor v){
            ((ICWarsInteractionVisitor) v).interactWith(this);
        }

        /**
         * Describes the interaction between the player and an interactable.
         * @param other (Interactable). Not null
         */
        @Override
        public void interactWith (Interactable other){
            if (!isDisplacementOccurs()) {
                other.acceptInteraction(handler);
            }
        }

        //This clas handles the RealPlayer's specific interactions.
        public class ICWarsPlayerInteractionHandler implements ICWarsInteractionVisitor {

            /**
             * Describes the interaction between a unit and a real player.
             * @param u unit
             */
            @Override
            public void interactWith(Unit u) {
                posUnit = u;
                playerGUI.setPosUnit(u);
                if (getCurrentState() == playerState.SELECT_CELL && u.getFaction() == faction) {
                    setUnitInMemory(u);
                    canPass = true;
                    playerGUI.setSelectedUnit(u);
                }
            }

            /**
             * Describes the interaction between a cellType and a real player.
             * @param cellType
             */
            @Override
            public void interactWith(ICWarsBehavior.ICWarsCell cellType) {
                cellTypePlayer = cellType.getI();
                playerGUI.setCellTypePlayerGUI(cellTypePlayer);
            }
        }
}

