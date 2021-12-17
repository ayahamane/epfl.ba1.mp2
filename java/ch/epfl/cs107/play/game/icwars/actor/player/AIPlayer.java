package ch.epfl.cs107.play.game.icwars.actor.player;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icwars.actor.unit.Unit;
import ch.epfl.cs107.play.game.icwars.actor.unit.action.Action;
import ch.epfl.cs107.play.game.icwars.gui.ICWarsPlayerGUI;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AIPlayer extends ICWarsPlayer{
    private Sprite sprite;
    private Unit selectedUnitAi;
    private ICWarsPlayerGUI playerGUI;
    private Keyboard keyboard= getOwnerArea().getKeyboard();
    private Faction faction;
    private boolean canPass = false;
    private Action actionToExecute;
    private DiscreteCoordinates coordsNearestUnit;
    private int newX;
    private int newY;

    public AIPlayer(Area area, DiscreteCoordinates position, Faction fac, String spriteName, Unit... units) {
        super(area, position, fac, units);
        sprite = new Sprite(spriteName, 1.f, 1.f, this);
        this.faction = fac; //Not needed si?
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
        Keyboard keyboard = getOwnerArea().getKeyboard();
        coordsNearestUnit = getCurrentMainCellCoordinates();
    }

    /**
     * Returns the new position of the selected unit.
     */
    //QUESTION: Méthode à faire valider d'URGENCE!!!
    public DiscreteCoordinates newCoords( DiscreteCoordinates position){

        DiscreteCoordinates abscissa1=new DiscreteCoordinates(-selectedUnitAi.getRadius(),position.y);
        DiscreteCoordinates abscissa2=new DiscreteCoordinates(selectedUnitAi.getRadius(),position.y);
        DiscreteCoordinates ordinate1=new DiscreteCoordinates(position.x,-selectedUnitAi.getRadius());
        DiscreteCoordinates ordinate2=new DiscreteCoordinates(position.x,selectedUnitAi.getRadius());

        double abscissaNegative = distanceBetween(abscissa1,position);
        double abscissaPositive = distanceBetween(position,abscissa2);
        double ordinateNegative = distanceBetween(ordinate1,position);
        double ordinatePositive = distanceBetween(position,ordinate2);

        if(0 <= position.x && position.x < getOwnerArea().getWidth()
                && 0 <= position.y && position.y < getOwnerArea().getHeight()){
            if(-selectedUnitAi.getRadius() <= position.x && position.x <= selectedUnitAi.getRadius()
                    && -selectedUnitAi.getRadius() <= position.y && position.y <= -selectedUnitAi.getRadius()){
                return position;
            } else {
                if(abscissaPositive<abscissaNegative){
                    newX = selectedUnitAi.getRadius();
                } else {
                    newX = -selectedUnitAi.getRadius();
                }
                if(ordinatePositive<ordinateNegative){
                    newY = selectedUnitAi.getRadius();
                } else {
                    newY = -selectedUnitAi.getRadius();
                }
                return new DiscreteCoordinates(newX,newY);
            }
        }
        return null;
    }

    /**
     * Return the euclidean Distance between two discrete coordinate
     * @param a (DiscreteCoordinates). Not null
     * @param b (DiscreteCoordinates). Not null
     * @return (float): the euclidean distance between the two coordinates
     */
    //QUESTION: Est-ce que je peux utiliser la méthode déjà codée??
    public static double distanceBetween(DiscreteCoordinates a, DiscreteCoordinates b){
        return  Math.sqrt((a.x-b.x)*(a.x-b.x) + (a.y-b.y)*(a.y-b.y));
    }

    //QUESTION: Méthode à faire valider d'URGENCE!!!
    protected void changeState(float dt){
        switch (getCurrentState()){
            case IDLE:
                break;
            case NORMAL:
                //Comment sélectionner chacune de ses unités en séquence?
                for(int i = 0; i<unit.size();++i){
                    selectUnitAi(i);
                }
                coordsNearestUnit = getCurrentMainCellCoordinates();
                newCoords(coordsNearestUnit);
//                if(waitFor())
//                currentState = playerState.MOVE_UNIT;

                break;
            case MOVE_UNIT:
                selectedUnitAi.changePosition(coordsNearestUnit);
                break;
            case ACTION_SELECTION:
                List<Action> list= new ArrayList<>();
                for(int i = 0; i< selectedUnitAi.getListOfActionsSize(); ++i){
                    list.add(selectedUnitAi.getElementListOfActions(i));
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
                actionToExecute.doAction(dt,this, keyboard);
                break;
            default:
        }
    }

    /**
     * Selects one of the units of the player
     */
    public void selectUnitAi(int index){
        if(index < unit.size()){
            selectedUnitAi = unit.get(index);
            playerGUI.setSelectedUnit(selectedUnitAi);
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
    public void acceptInteraction(AreaInteractionVisitor v) {}

    @Override
    public void interactWith(Interactable other) {}

    /**
     * Ensures that value time elapsed before returning true
     * @param dt elapsed time
     * @param value waiting time (in seconds)
     * @return true if value seconds has elapsed , false otherwise
     */
//    private boolean waitFor(float value , float dt) {
//        if (counting) {
//            counter += dt;
//            if (counter > value) {
//                counting = false;
//                return true;
//            }
//
//        } else {
//            counter = 0f;
//            counting = true;
//        }
//        return false;
//    }
}
