package ch.epfl.cs107.play.game.icwars.actor.unit;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.*;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icwars.actor.ICWarsActor;
import ch.epfl.cs107.play.game.icwars.actor.unit.action.Action;
import ch.epfl.cs107.play.game.icwars.area.ICWarsBehavior;
import ch.epfl.cs107.play.game.icwars.area.ICWarsRange;
import ch.epfl.cs107.play.game.icwars.handler.ICWarsInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;


import java.util.Collections;
import java.util.List;
import java.util.Queue;

abstract public class Unit extends ICWarsActor implements Interactor {

    private String name;
    private float hp;
    private Sprite sprite;
    private ICWarsRange range;
    private static int radius;
    private boolean hasBeenUsed;
    private int cellDefenseStars;
    private ICWarsUnitInteractionHandler handler;
    private List<Action> listOfActions;

    //List<String> list2 = Collections.unmodifiableList(list);

    public Unit(Area area, DiscreteCoordinates position, Faction fac){
        super(area, position, fac);
        setRange(position);
        hasBeenUsed = false;
        handler = new ICWarsUnitInteractionHandler();
    }

    protected void setListOfActions(List<Action> unitsActions){
        listOfActions = Collections.unmodifiableList(unitsActions);
    }

    private void setRange(DiscreteCoordinates position) {
        range = new ICWarsRange();
        radius = getRadius();
        for (int x = -radius; x <= radius; ++x) {
            for (int y = -radius; y <= radius; ++y) {
                DiscreteCoordinates newPosition = new DiscreteCoordinates(x + position.x, y + position.y);
                boolean hasLeftEdge = false;
                boolean hasRightEdge = false;
                boolean hasUpEdge = false;
                boolean hasDownEdge = false;
                if (0 <= newPosition.x && newPosition.x < getOwnerArea().getWidth()
                        && 0 <= newPosition.y && newPosition.y < getOwnerArea().getHeight()) {
                    if (x > -radius && newPosition.x > 0) {
                        hasLeftEdge = true;
                    }
                    if (x < radius && newPosition.x < getOwnerArea().getWidth() - 1) {
                        hasRightEdge = true;
                    }
                    if (y > -radius && newPosition.y > 0) {
                        hasDownEdge = true;
                    }
                    if (y < radius && newPosition.y < getOwnerArea().getHeight() - 1) {
                        hasUpEdge = true;
                    }
                    range.addNode(newPosition, hasLeftEdge, hasUpEdge, hasRightEdge, hasDownEdge);

                }
            }
        }
    }

    public void draw(Canvas canvas) { sprite.draw(canvas); }

    /**
     * Draw the unit's range and a path from the unit position to
     destination
     * @param destination path destination
     * @param canvas canvas
     */
    public void drawRangeAndPathTo(DiscreteCoordinates destination,
                                          Canvas canvas) {
        range.draw(canvas);
        Queue<Orientation> path =
                range.shortestPath(getCurrentMainCellCoordinates(),
                        destination);
        //Draw path only if it exists (destination inside the range)
        if (path != null){
            new Path(getCurrentMainCellCoordinates().toVector(),
                    path).draw(canvas);
        }
    }

    public boolean isDead(){
        if (hp == 0){
            return true;
        }
        return false;
    }

    public void undergoDamage(float minus){
        hp = hp - minus + cellDefenseStars;
        if (hp < 0){
            hp = 0;
        }
    }

    /**
     * Center the camera on the unit
     */
    public void centerCamera() { getOwnerArea().setViewCandidate(this); }

    public void fix(float plus) { hp = hp + plus; }

    abstract public int getDamage();

    @Override
    public boolean changePosition(DiscreteCoordinates newPosition) {
        if ((range.nodeExists(newPosition)&&(super.changePosition(newPosition)))){
            setRange(newPosition);
            return true;
        }
        return false;
    }

    @Override
    public boolean takeCellSpace() { return true; }

    /**@return (boolean): true if this is able to have cell interactions*/
    public boolean isCellInteractable() { return true; }

    /**@return (boolean): true if this is able to have view interactions*/
    public boolean isViewInteractable() { return false; }

    public String getName() { return name; }

    public float getHp() { return hp; }

    //These methods are here to help us in the coming code. Can we keep them?
    protected void setName(String n) { name = n; }

    protected void setHp(float energy) { hp = energy; }

    protected void setSprite(Sprite s) { sprite = s; }

    //IsThisAnIntrusiveGetter?I think that a sprite doesn't change throughout the time.
    public Sprite getSprite() { return sprite; }

    public int getRadius() { return radius; }

    //I changed it to public cause I use it in ICWarsPlayer update.
    public boolean hasBeenUsed() { return hasBeenUsed; }

    //IsThisAnIntrusiveSetter?
    public void setHasBeenUsed(boolean used) { hasBeenUsed = used; }

    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() { return null; }

    @Override
    public boolean wantsCellInteraction() { return true; }

    @Override
    public boolean wantsViewInteraction() { return false; }

    @Override
    public void interactWith(Interactable other){
        other.acceptInteraction(handler);
    }


    public void acceptInteraction(AreaInteractionVisitor v)
    {
        ((ICWarsInteractionVisitor)v).interactWith(this);
    }

    public Action getElementListOfActions(int i){ return listOfActions.get(i); }

    public int getListOfActionsSize() { return listOfActions.size(); }


    public class ICWarsUnitInteractionHandler implements ICWarsInteractionVisitor {
        @Override
        public void interactWith(ICWarsBehavior.ICWarsCellType cellType){
            cellDefenseStars = cellType.getDefenseStar();
        }
    }
}
