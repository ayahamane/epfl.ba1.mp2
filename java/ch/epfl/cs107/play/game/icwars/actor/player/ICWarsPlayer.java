package ch.epfl.cs107.play.game.icwars.actor.player;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icwars.actor.ICWarsActor;
import ch.epfl.cs107.play.game.icwars.actor.Unit;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ICWarsPlayer extends ICWarsActor {
    protected List<Unit> unit;

    public ICWarsPlayer(Area area, DiscreteCoordinates position, Faction fac, Unit... units) {
        super(area, position, fac);
        unit = new ArrayList<>(Arrays.asList(units));
        for(int i = 0;i < units.length; ++i){
            getOwnerArea().registerActor(unit.get(i));
        }
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
    public void acceptInteraction(AreaInteractionVisitor v) {}
}