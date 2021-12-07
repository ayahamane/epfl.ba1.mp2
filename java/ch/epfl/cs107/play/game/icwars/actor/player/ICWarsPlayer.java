package ch.epfl.cs107.play.game.icwars.actor.player;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Interactor;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icwars.actor.ICWarsActor;
import ch.epfl.cs107.play.game.icwars.actor.Unit;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ICWarsPlayer extends ICWarsActor implements Interactor {

    protected enum State{
        IDLE, NORMAL, SELECT_CELL, MOVE_UNIT, ACTION_SELECTION, ACTION;
    }

    protected List<Unit> unit;
    private State currentState;
    private Keyboard keyboard= getOwnerArea().getKeyboard();
    private Unit unitInMemory;

    public ICWarsPlayer(Area area, DiscreteCoordinates position, Faction fac, Unit... units) {
        super(area, position, fac);
        unit = new ArrayList<>(Arrays.asList(units));
        for(int i = 0;i < units.length; ++i){
            getOwnerArea().registerActor(unit.get(i));
        }
        currentState = State.IDLE;
    }

    //Pour cette méthode, l'utilisation des case c qlqch avec lql je suis pas trop habituée,
    //je vais donc très probablement revenir dessus quand je me serai bien documentée sur ça
    //pck mettre case et if je trouve ça chelou.
    protected void changeState(){
        switch (currentState){
            case IDLE: break;
            case NORMAL:
                if (keyboard.get(Keyboard.ENTER).isReleased()) { currentState = State.SELECT_CELL; }
                if (keyboard.get(Keyboard.TAB).isReleased()) { currentState = State.IDLE; }
                break;
            case SELECT_CELL:
                if (unitInMemory != null){ currentState = State.MOVE_UNIT; }
                //Pas vraiment null mais juste si y a une unit dans sa currentCell.
                break;
            case MOVE_UNIT:
                if (keyboard.get(Keyboard.ENTER).isReleased()) {
                    //changer la position de l'unité, plus tard.
                    //unitInMemory.setHasBeenUsed(true);
                }
                break;
            case ACTION:
            case ACTION_SELECTION:
        }
    }

    protected void startTurn(){
        currentState = State.NORMAL;
        centerCamera();
    }

    @Override
    public void onLeaving(List<DiscreteCoordinates> coordinates) {
        if (currentState == State.SELECT_CELL && unitInMemory == null) { currentState = State.NORMAL;}
    }

    public void update(float deltaTime){
        super.update(deltaTime);
        for(int i = 0; i < unit.size(); ++i){
            if(unit.get(i).isDead()){
                getOwnerArea().unregisterActor(unit.get(i));
                unit.remove(i);
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
    public void centerCamera() {
        getOwnerArea().setViewCandidate(this);
    }

    @Override
    public void draw(Canvas canvas) {}

    @Override
    public boolean takeCellSpace() {
        return false;
    }

    @Override
    public boolean isCellInteractable() {
        return false;
    }

    @Override
    public boolean isViewInteractable() {
        return false;
    }

    @Override
    public boolean wantsCellInteraction(){
        return true;
    }

    @Override
    public boolean wantsViewInteraction(){
        return false;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {}

    protected State getCurrentState(){
        return currentState;
    }

    protected void memorizeUnit (Unit u){
        unitInMemory = u;
    }
}