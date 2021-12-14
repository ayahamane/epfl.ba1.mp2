package ch.epfl.cs107.play.game.icwars.actor.player;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Interactor;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icwars.ICWars;
import ch.epfl.cs107.play.game.icwars.actor.ICWarsActor;
import ch.epfl.cs107.play.game.icwars.actor.Unit;
import ch.epfl.cs107.play.game.icwars.area.ICWarsArea;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class ICWarsPlayer extends ICWarsActor implements Interactor {

    public enum playerState{
        IDLE, NORMAL, SELECT_CELL, MOVE_UNIT, ACTION_SELECTION, ACTION
    }
    protected List<Unit> unit;
    protected playerState currentState;
    private Keyboard keyboard= getOwnerArea().getKeyboard();
    protected Unit unitInMemory;

    public ICWarsPlayer(ICWarsArea area, DiscreteCoordinates position, Faction fac, Unit... units) {
        super(area, position, fac);
        unit = new ArrayList<>(Arrays.asList(units));
        for(int i = 0; i < units.length; ++i){
            getOwnerArea().registerActor(unit.get(i));
            area.addToUnitInArea(unit.get(i));
        }
        currentState = playerState.IDLE;
    }

    public void startTurn(){
        currentState = playerState.NORMAL;
        this.unitsReusable();
        centerCamera();
    }

    @Override
    public void onLeaving(List<DiscreteCoordinates> coordinates) {
        if (currentState == playerState.SELECT_CELL) {
            currentState = playerState.NORMAL;
        }
    }

    public void update(float deltaTime){
        super.update(deltaTime);
        for(int i = 0; i < unit.size(); ++i){
            if(unit.get(i).isDead()){
                getOwnerArea().unregisterActor(unit.get(i));
            }
            if(unit.get(i).isHasBeenUsed()){
                unit.get(i).getSprite().setAlpha(0.5f);
            }
        }
    }

    /**
     * Leave an area by unregister this player
     */
    @Override
    public void leaveArea(){
        super.leaveArea();
        for(int i = 0; i < unit.size(); ++i){
            getOwnerArea().unregisterActor(unit.get(i));
        }
    }

    /**
     * Tells if the player is defeated or not.
     */
    public boolean isDefeated(){
        if(unit.isEmpty()){
            return true;
        }
        return false;
    }


    /**
     * Center the camera on the player
     */
    public void centerCamera() { getOwnerArea().setViewCandidate(this); }

    @Override
    public void draw(Canvas canvas) {}

    @Override
    public boolean takeCellSpace() {return false;}

    @Override
    public boolean isCellInteractable() { return false; }

    @Override
    public boolean isViewInteractable() { return false; }

    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() { return null; }

    @Override
    public boolean wantsCellInteraction() { return true; }

    @Override
    public boolean wantsViewInteraction() { return false; }

    public playerState getCurrentState() { return currentState; }

    /**
     * Makes all players units reusable.
     */
    public void unitsReusable(){
        for(int i = 0; i < unit.size(); ++i){
            unit.get(i).getSprite().setAlpha(1.f);
            unit.get(i).setHasBeenUsed(false);
        }
    }

    /**
     *Returns true if player ended his turn.
     */
    public boolean playerEndedTurn(){
        if(currentState == ICWarsPlayer.playerState.IDLE){
          return true;
        }
        return false;
    }
}