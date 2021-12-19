package ch.epfl.cs107.play.game.icwars.actor.player;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icwars.actor.unit.Unit;
import ch.epfl.cs107.play.game.icwars.actor.unit.action.Action;
import ch.epfl.cs107.play.game.icwars.area.ICWarsArea;
import ch.epfl.cs107.play.game.icwars.area.ICWarsBehavior;
import ch.epfl.cs107.play.game.icwars.gui.ICWarsPlayerGUI;
import ch.epfl.cs107.play.game.icwars.handler.ICWarsInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;
import java.util.Collections;
import java.util.List;

import static ch.epfl.cs107.play.math.DiscreteCoordinates.distanceBetween;

public class AIPlayer extends ICWarsPlayer{
    private float counter;
    private boolean counting;
    private Sprite sprite;
    private Unit selectedUnitAi;
    private ICWarsPlayerGUI playerGUI;
    private Faction faction;
    private DiscreteCoordinates coordsNearestUnit;
    private List<Action> list;
    private Unit posUnit;
    private ICWarsBehavior.ICWarsCellType cellTypePlayer;

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
        System.out.println("paramètre X: " + position.x + " paramètre Y: " + position.y);
        System.out.println("selectedUnitAi X: " + selectedUnitAi.getPosition().x +
                " selectedUnitAi Y: " + selectedUnitAi.getPosition().y);
        if(0 <= position.x && position.x < getOwnerArea().getWidth()
                && 0 <= position.y && position.y < getOwnerArea().getHeight()) {
            if (-selectedUnitAi.getRadius() <= position.x && position.x <= selectedUnitAi.getRadius()
                    && -selectedUnitAi.getRadius() <= position.y && position.y <= -selectedUnitAi.getRadius()) {
                System.out.println("On est ds le radius");
                return position;
            }
            DiscreteCoordinates abscissa1 = new DiscreteCoordinates(
                    (int) selectedUnitAi.getPosition().x - selectedUnitAi.getRadius(),
                    (int) selectedUnitAi.getPosition().y);
            System.out.println("abscissa1 X: " + abscissa1.x + " abscissa1 Y: " + abscissa1.y);
            DiscreteCoordinates abscissa2 = new DiscreteCoordinates(
                    (int) selectedUnitAi.getPosition().x + selectedUnitAi.getRadius(),
                    (int) selectedUnitAi.getPosition().y);
            System.out.println("abscissa2 X: " + abscissa2.x + " abscissa2 Y: " + abscissa2.y);
            float leftEdge = distanceBetween(abscissa1, position);
            float rightEdge = distanceBetween(abscissa2, position);
            int finalAbcsissa = (int) selectedUnitAi.getPosition().x;
            if (leftEdge < rightEdge) {
                finalAbcsissa = (int) selectedUnitAi.getPosition().x - selectedUnitAi.getRadius();
                System.out.println("FinalAbscissa pour leftEdge < rightEdge: " + finalAbcsissa);
            }
            if(leftEdge > rightEdge){
                finalAbcsissa = (int) selectedUnitAi.getPosition().x + selectedUnitAi.getRadius();
                System.out.println("FinalAbscissa pour leftEdge > rightEdge: " + finalAbcsissa);
            }
            if(leftEdge == rightEdge) {
                System.out.println("FinalAbscissa pour leftEdge == rightEdge: " + finalAbcsissa);
            }

            DiscreteCoordinates ordinate1 = new DiscreteCoordinates(finalAbcsissa,
                    (int) selectedUnitAi.getPosition().y - selectedUnitAi.getRadius());
            System.out.println("ordinate1 X: " + ordinate1.x + " ordinate1 Y: " + ordinate1.y);
            DiscreteCoordinates ordinate2 = new DiscreteCoordinates(finalAbcsissa,
                    (int) selectedUnitAi.getPosition().y + selectedUnitAi.getRadius());
            System.out.println("ordinate2 X: " + ordinate2.x + " ordinate2 Y: " + ordinate2.y);
            DiscreteCoordinates intermediatePosition = new DiscreteCoordinates(finalAbcsissa, (int) selectedUnitAi.getPosition().y);
            System.out.println("intermediatePosition X: " + intermediatePosition.x +
                    " intermediatePosition Y: " + intermediatePosition.y);
            float lowEdge = distanceBetween(ordinate1, position);
            float highEdge = distanceBetween(ordinate2, position);
            int finalOrdinate = (int) selectedUnitAi.getPosition().y;
            float previousDistance = distanceBetween(intermediatePosition, position);
            if (lowEdge < highEdge) { //On prend pas en compte le cas où c'est égale
                finalOrdinate = (int) selectedUnitAi.getPosition().y - selectedUnitAi.getRadius();
                previousDistance = lowEdge;
                System.out.println("PreviousFinalOrdinate pour lowEdge < highEdge: " + finalOrdinate);
                for (int i = finalOrdinate + 1; i < (int) selectedUnitAi.getPosition().y; ++i) {
                    System.out.println("in the forLoop1");
                    DiscreteCoordinates positionToTest = new DiscreteCoordinates(finalAbcsissa, i);
                    if (distanceBetween(positionToTest, position) < previousDistance
                            && i <= selectedUnitAi.getPosition().y + selectedUnitAi.getRadius()) {
                        finalOrdinate = i;
                         //System.out.println("distanceBetween(positionToTest, position) < previousDistance");
                         //System.out.println("NewFinalOrdinate: " + finalOrdinate);
                    }
                }
            }
            if (lowEdge > highEdge) {
                finalOrdinate =  (int) selectedUnitAi.getPosition().y + selectedUnitAi.getRadius();
                previousDistance = highEdge;
                System.out.println("PreviousFinalOrdinate pour lowEdge > highEdge: " + finalOrdinate);
                for (int i = finalOrdinate - 1; i > (int) selectedUnitAi.getPosition().y; --i) {
                    System.out.println("in the forLoop2");
                    DiscreteCoordinates positionToTest = new DiscreteCoordinates(finalAbcsissa, i);
                    if (distanceBetween(positionToTest, position) < previousDistance
                            && i >= selectedUnitAi.getPosition().y - selectedUnitAi.getRadius()) {
                        finalOrdinate = i;
                         //System.out.println("distanceBetween(positionToTest, position) < previousDistance");
                         //System.out.println("NewFinalOrdinate: " + finalOrdinate);
                    }
                }
            }
            if (lowEdge == highEdge) {
                System.out.println("PreviousFinalOrdinate pour lowEdge == highEdge: " + finalOrdinate);
            }
            if (-selectedUnitAi.getRadius() <= finalAbcsissa && finalAbcsissa <= selectedUnitAi.getRadius()
                    && -selectedUnitAi.getRadius() <= finalOrdinate && finalOrdinate <= -selectedUnitAi.getRadius()) {
                DiscreteCoordinates newPosition = new DiscreteCoordinates(finalAbcsissa, finalOrdinate);
                System.out.println("newX = " + newPosition.x + " newY = " + newPosition.y);
                return newPosition;
            }
        }
        return null;
    }

    protected void changeState(float dt){
        switch (getCurrentState()){
            case IDLE:
                System.out.println("Je suis dans IDLE");
                break;
            case NORMAL:
                System.out.println("Je suis dans NORMAL");
                selectUnitAi();
                setCurrentPosition(selectedUnitAi.getPosition());
                if(waitFor(2,dt)){
                    coordsNearestUnit = ((ICWarsArea)getOwnerArea()).getCoordsNearestUnit(selectedUnitAi);
                    System.out.println("coordsNearestUnit dans AIPlayer après le getOwnerArea");
                    System.out.println("X = " + coordsNearestUnit.x + " Y = "+ coordsNearestUnit.y );
                    currentState = playerState.MOVE_UNIT;
                }
                break;
            case MOVE_UNIT:
                System.out.println("Je suis dans MOVE_UNIT");
                System.out.println("coordsNearestUnit dans AIPlayer dans le MOVE_UNIT");
                System.out.println("X = " + coordsNearestUnit.x + "Y = "+ coordsNearestUnit.y );
                if(waitFor(2,dt)) {
                    selectedUnitAi.changePosition(newCoords(coordsNearestUnit));
                    setCurrentPosition(selectedUnitAi.getPosition());
                    currentState = playerState.ACTION;
                }
                break;
            case ACTION:
                System.out.println("Je suis dans ACTION");
                list = selectedUnitAi.getListOfActions();
                selectedUnitAi.getListOfActions().get(0).doAutoAction(dt,this, list = selectedUnitAi.getListOfActions());
                selectedUnitAi.setHasBeenUsed(true);
                currentState = playerState.IDLE;
                break;
            default:
        }
    }

    /**
     * Selects one of the units of the player
     */
    public void selectUnitAi(){
        for(int i = 0; i < unit.size(); ++i) {
            if(unit.get(i).getFaction() == this.faction){
                if(!unit.get(i).hasBeenUsed()){
                    selectedUnitAi = unit.get(i);
                    playerGUI.setSelectedUnit(selectedUnitAi);
                    break;
                }
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
