package ch.epfl.cs107.play.game.icwars.actor.player;

import ch.epfl.cs107.play.game.actor.SoundAcoustics;
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

    private ArrayList<Unit> units;
    private playerState currentState;
    private Keyboard keyboard= getOwnerArea().getKeyboard();
    private Unit unitInMemory;
    private boolean inEnd = false;      //true if the player is in the game's end state.
    private boolean inStart = false;   // true if the game has just started.

    //Stores the player's state.
    public enum playerState{
        IDLE, NORMAL, SELECT_CELL, MOVE_UNIT, ACTION_SELECTION, ACTION
    }

    //extensions
    private final static SoundAcoustics sound = new SoundAcoustics(ResourcePath.getSound("gameSound"),1.0f,false,
            false,true,false);


    public ICWarsPlayer(ICWarsArea area, DiscreteCoordinates position, Faction fac, Unit... allUnits) {
        super(area, position, fac);
        units = new ArrayList<>(Arrays.asList(allUnits));
        for(int i = 0; i < allUnits.length; ++i){
            getOwnerArea().registerActor(units.get(i));
            area.addToUnitInArea(units.get(i));
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

    //if the player leaves a cell in a SELECT_CELL state without actually selecting a unit,
    // his state goes back to normal.
    @Override
    public void onLeaving(List<DiscreteCoordinates> coordinates) {
        if (currentState == playerState.SELECT_CELL) {
            currentState = playerState.NORMAL;
        }
    }

    /**
     * Allows the sound of all the game to be generated.
     * @param audio
     */
    @Override
    public void bip(Audio audio){
        super.bip(audio);
        sound.bip(audio);
    }

    @Override
    public void update(float deltaTime){
        super.update(deltaTime);
        for(int i = 0; i< units.size(); ++i){
            if(units.get(i).hasBeenUsed()){
                units.get(i).changeTransparency(0.5f);
            }
            if(units.get(i).isDead()){
                units.remove(i);
            }
        }
    }

    /**
     * Leave an area by unregister this player
     */
    @Override
    public void leaveArea(){
        for(int i = 0; i< units.size(); ++i){
            units.get(i).leaveArea();
        }
        super.leaveArea();
    }

    /**
     * Tells if the player is defeated or not.
     */
    public boolean isDefeated(){
        if(units.isEmpty()){
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
        for(int i = 0; i < units.size(); ++i){
            units.get(i).changeTransparency(1.f);
            units.get(i).setHasBeenUsed(false);
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

    /**
     *
     * @return the current state of the player
     */
    public playerState getCurrentState() { return currentState; }

    protected Unit getUnitInMemory(){
        return unitInMemory;
    }

    protected void setUnitInMemory(Unit u){
        unitInMemory = u;
    }

    protected ArrayList<Unit> getUnits(){
        return units;
    }
}