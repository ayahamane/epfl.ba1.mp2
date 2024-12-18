package ch.epfl.cs107.play.game.icwars.actor.unit;

import ch.epfl.cs107.play.game.actor.SoundAcoustics;
import ch.epfl.cs107.play.game.areagame.actor.*;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.game.icwars.actor.ICWarsActor;
import ch.epfl.cs107.play.game.icwars.actor.unit.action.Action;
import ch.epfl.cs107.play.game.icwars.area.ICWarsArea;
import ch.epfl.cs107.play.game.icwars.area.ICWarsBehavior;
import ch.epfl.cs107.play.game.icwars.area.ICWarsRange;
import ch.epfl.cs107.play.game.icwars.handler.ICWarsInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Audio;
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

    //Extensions
    private final static SoundAcoustics sound = new SoundAcoustics(ResourcePath.getSound("canon"),1.0f,
            false, false, false, false);


    public Unit(ICWarsArea area, DiscreteCoordinates position, Faction fac) {
        super(area, position, fac);
        setRange(position);
        hasBeenUsed = false;
        handler = new ICWarsUnitInteractionHandler();

    }

    /**
     * set the unit's list of actions.
     * @param unitsActions
     */
    protected void setListOfActions(List<Action> unitsActions) {
        listOfActions = Collections.unmodifiableList(unitsActions);
    }

    /**
     * set the range of the unit
     * @param position
     */
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
                        hasLeftEdge = true;}
                    if (x < radius && newPosition.x < getOwnerArea().getWidth() - 1) {
                        hasRightEdge = true;}
                    if (y > -radius && newPosition.y > 0) {
                        hasDownEdge = true;}
                    if (y < radius && newPosition.y < getOwnerArea().getHeight() - 1) {
                        hasUpEdge = true;}
                    range.addNode(newPosition, hasLeftEdge, hasUpEdge, hasRightEdge, hasDownEdge);
                }
            }
        }
    }

    @Override
    public void draw(Canvas canvas) {
        sprite.draw(canvas);
    }


    /**
     * Draw the unit's range and a path from the unit position to
     * destination
     *
     * @param destination path destination
     * @param canvas      canvas
     */
    public void drawRangeAndPathTo(DiscreteCoordinates destination,
                                   Canvas canvas) {
        range.draw(canvas);
        Queue<Orientation> path =
                range.shortestPath(getCurrentMainCellCoordinates(),
                        destination);
        if (path != null) {
            new Path(getCurrentMainCellCoordinates().toVector(),
                    path).draw(canvas);
        }
    }

    /**
     *
     * @return true if the unit is dead
     */
    public boolean isDead() {
        return (hp <= 0);
    }

    /**
     * apply damage on the unit
     * @param minus energy removed from the unit's energy
     */
    public void undergoDamage(float minus) {
        sound.shouldBeStarted();
        hp = hp - minus + cellDefenseStars;
        if (hp < 0) {
            hp = 0;
            leaveArea();
        }
    }


    @Override
    public void bip(Audio audio){
        super.bip(audio);
        sound.bip(audio);
    }

    /**
     * Center the camera on the unit
     */
    public void centerCamera() {
        getOwnerArea().setViewCandidate(this);
    }

    /**
     * add energy to the unit
     * @param plus energy added to the the unit's energy
     */
    public void fix(float plus) {
        hp = hp + plus;
    }


    /**
     *
     * @return the value of unit's damage
     */
    abstract public int getDamage();

    @Override
    public boolean changePosition(DiscreteCoordinates newPosition) {
        if (inRadius(newPosition) && (super.changePosition(newPosition))) {
            setRange(newPosition);
            return true;
        }
        return false;
    }


    /**
     * Check if the position entered exists in the unit's radius.
     * @param position
     * @return true if the position entered exists in the unit's radius.
     */
    public boolean inRadius(DiscreteCoordinates position){
        if (range.nodeExists(position)){
            return true;
        }
        return false;
    }

    @Override
    public boolean takeCellSpace() {
        return true;
    }

    /**
     * @return (boolean): true if this is able to have cell interactions
     */
    public boolean isCellInteractable() {
        return true;
    }

    /**
     * @return (boolean): true if this is able to have view interactions
     */
    public boolean isViewInteractable() {
        return false;
    }

    public String getName() {
        return name;
    }

    public float getHp() {
        return hp;
    }

    protected void setName(String n) {
        name = n;
    }

    protected void setHp(float energy) {
        hp = energy;
    }

    protected void setSprite(Sprite s) {
        sprite = s;
    }

    public void changeTransparency(float f){
        sprite.setAlpha(f);
    }

    public int getRadius() {
        return radius;
    }

    public boolean hasBeenUsed() {
        return hasBeenUsed;
    }

    /**
     * set the unit's hasBeenUsed attribute
     * @param used hasBeenUsed expected after the execution
     */
    public void setHasBeenUsed(boolean used) {
        hasBeenUsed = used;
    }

    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        return null;
    }

    @Override
    public boolean wantsCellInteraction() {
        return true;
    }

    @Override
    public boolean wantsViewInteraction() {
        return false;
    }

    @Override
    public void interactWith(Interactable other) {
        other.acceptInteraction(handler);
    }


    public void acceptInteraction(AreaInteractionVisitor v) {
        ((ICWarsInteractionVisitor) v).interactWith(this);
    }

    /**
     *
     * @return a list of the unit's possible actions.
     */
    public List<Action> getListOfActions() {
        return listOfActions;
    }


    /**
     * Class that handles the specific's unit interactions.
     */
    public class ICWarsUnitInteractionHandler implements ICWarsInteractionVisitor {
        @Override
        public void interactWith(ICWarsBehavior.ICWarsCell cellType) {
            cellDefenseStars = cellType.getDefenseStar();
        }
    }
}
