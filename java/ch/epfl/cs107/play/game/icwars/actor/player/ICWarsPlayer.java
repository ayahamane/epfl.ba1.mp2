package ch.epfl.cs107.play.game.icwars.actor.player;

import ch.epfl.cs107.play.game.actor.SoundAcoustics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Interactor;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.game.icwars.actor.ICWarsActor;
import ch.epfl.cs107.play.game.icwars.area.ICWarsArea;
import ch.epfl.cs107.play.game.icwars.actor.unit.Unit;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Audio;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class ICWarsPlayer extends ICWarsActor implements Interactor {

    protected ArrayList<Unit> unit;
    protected playerState currentState;
    protected Keyboard keyboard= getOwnerArea().getKeyboard();
    protected Unit unitInMemory;
    private boolean inEnd = false;
    private boolean inStart = false;
    private final static SoundAcoustics sound = new SoundAcoustics(ResourcePath.getSound("gameSound"),1.0f,false,
            false,true,false);
    public enum playerState{
        IDLE, NORMAL, SELECT_CELL, MOVE_UNIT, ACTION_SELECTION, ACTION
    }

    public ICWarsPlayer(ICWarsArea area, DiscreteCoordinates position, Faction fac, Unit... units) {
        super(area, position, fac);
        unit = new ArrayList<>(Arrays.asList(units));
        for(int i = 0; i < units.length; ++i){
            getOwnerArea().registerActor(unit.get(i));
            area.addToUnitInArea(unit.get(i));
        }
        currentState = playerState.IDLE;
    }

    /**
     * Allows the sound to be started
     */
    public void soundCanBeStarted(){
        sound.shouldBeStarted();
    }

    /**
     * sets the value of inEnd
     * @param inEnd
     */
    public void setInEnd(boolean inEnd){
        this.inEnd = inEnd;
    }

    /**
     * gets the value of inEnd
     * @return
     */
    public boolean getInEnd(){
        return inEnd;
    }

    /**
     * sets the value of inStart
     * @param inStart
     */
    public void setInStart(boolean inStart){
        this.inStart = inStart;
    }

    /**
     * gets the value of inStart
     * @return
     */
    public boolean getInStart(){
        return inStart;
    }

    /**
     * gets the width of the player's area
     * @return
     */
    public float areaWidth(){
        return getOwnerArea().getWidth();
    }

    /**
     * gets the height of the player's area
     * @return
     */
    public float areaHeight(){
        return getOwnerArea().getHeight();
    }

    /**
     * sets the player's current state
     * @param state
     */
    public void setCurrentState(playerState state){
        currentState = state;
    }

    /**
     * Used when a player starts his turn
     */
    public void startTurn(){
        currentState = playerState.NORMAL;
        unitsReusable();
        centerCamera();
    }

    //LINA
    @Override
    public void onLeaving(List<DiscreteCoordinates> coordinates) {
        if (currentState == playerState.SELECT_CELL) {
            currentState = playerState.NORMAL;
        }
    }

    //LINA
    @Override
    public void bip(Audio audio){
        super.bip(audio);
        sound.bip(audio);
    }

    @Override
    public void update(float deltaTime){
        super.update(deltaTime);
        for(int i = 0; i< unit.size(); ++i){
            if(unit.get(i).hasBeenUsed()){
                unit.get(i).getSprite().setAlpha(0.5f);
            }
            if(unit.get(i).isDead()){
                unit.remove(i);
            }
        }
    }

    /**
     * Leave an area by unregister this player
     */
    @Override
    public void leaveArea(){
        for(int i = 0; i< unit.size(); ++i){
            unit.get(i).leaveArea();
        }
        super.leaveArea();
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
}