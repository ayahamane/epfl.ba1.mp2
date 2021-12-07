package ch.epfl.cs107.play.game.icwars.actor.player;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icwars.actor.Tank;
import ch.epfl.cs107.play.game.icwars.actor.Unit;
import ch.epfl.cs107.play.game.icwars.gui.ICWarsPlayerGUI;
import ch.epfl.cs107.play.game.icwars.handler.ICWarInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

import javax.swing.*;
import java.util.Collections;
import java.util.List;

public class RealPlayer extends ICWarsPlayer{



    private Sprite sprite;
    private ICWarsPlayerGUI playerGUI;
    /// Animation duration in frame number
    private final static int MOVE_DURATION = 1;
    private Unit selectedUnit;



    /**
     * Demo actor
     *
     */
    public RealPlayer(Area area, DiscreteCoordinates position, Faction fac,
                      String spriteName, Unit... unit) {
        super(area, position, fac, unit);
        sprite = new Sprite(spriteName, 1.f, 1.f,this);
        resetMotion();
        if (fac== Faction.ally){
            setSprite(new Sprite("icwars/allyCursor", 1f, 1f, this,
                    null , new Vector(0, 0)));
        } else {
            setSprite(new Sprite("icwars/enemyCursor", 1f, 1f, this, null ,
                    new Vector(0, 0)));
        }
        playerGUI=new ICWarsPlayerGUI(getOwnerArea().getCameraScaleFactor(), this);
    }



    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        Keyboard keyboard= getOwnerArea().getKeyboard();
        if (getCurrentState() == State.NORMAL || getCurrentState() == State.SELECT_CELL ||
                getCurrentState() == State.MOVE_UNIT){  //je sais qu'on peut rendre ça moins long mais
            //jsais plus cmt ^^. J'y reviendrai plus tard.
            moveIfPressed(Orientation.UP, keyboard.get(Keyboard.UP));
            moveIfPressed(Orientation.LEFT, keyboard.get(Keyboard.LEFT));
            moveIfPressed(Orientation.RIGHT, keyboard.get(Keyboard.RIGHT));
            moveIfPressed(Orientation.DOWN, keyboard.get(Keyboard.DOWN));
        }
        changeState();
    }
    //Can I do this instead in order to improve the method update?
    /*@Override
    public void update(float deltaTime, Orientation orientation, Button b) {
        super.update(deltaTime);
        Keyboard keyboard= getOwnerArea().getKeyboard();
        //moveIfPressed(orientation, b);
        //I chose to use only the line above to avoid duplicated code. Is it correct?
     }*/

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
        if(index<unit.size()){
            selectedUnit = unit.get(index);
            playerGUI.setSelectedUnit(selectedUnit);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        getSprite().draw(canvas);
        if((!(selectedUnit ==null)) && (getCurrentState() == State.MOVE_UNIT)){
            playerGUI.draw(canvas);}
    }

    @Override
    public boolean takeCellSpace() {
        return true;
    }

    @Override
    public boolean isCellInteractable() {
        return true;
    }

    @Override
    public boolean isViewInteractable() {
        return true;
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());}

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {}

    //Is it really needed?
    protected void setSprite(Sprite s){
        sprite = s;
    }

    protected Sprite getSprite(){
        return sprite;
    }


    public class ICWarsPlayerInteractionHandler implements ICWarInteractionVisitor{

        @Override
        public void interactWith(Unit u) {
            if (RealPlayer.this.getCurrentState() == State.SELECT_CELL &&
                    u.getFaction() == RealPlayer.this.getFaction()) {
                RealPlayer.this.memorizeUnit(u);
            }

        }
    }

}
