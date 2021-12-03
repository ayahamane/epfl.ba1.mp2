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
    private List<Unit> unit;

    public ICWarsPlayer(Area area, DiscreteCoordinates position, Faction fac, Unit... unit) {
        super(area, position, fac);
        this.unit= new ArrayList<>(Arrays.asList(unit));
        for(int i=0;i<unit.length;++i){
           getOwnerArea().registerActor(this.unit.get(i));
        }
    }

    public void update(float deltatime){ //Why should I change the name?
        super.update(deltatime);
        //Elimination des entités non opérationnelles à faire dans update même s'ils disent à "chaque pas".
        for(int i=0;i<unit.size();++i){
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
        for(int i=0;i<unit.size();++i){
            getOwnerArea().unregisterActor(unit.get(i));
        }
    }

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
    //J'ai pris exactement celle de ghostPlayer, est-ce problématique?

    @Override
    public void draw(Canvas canvas) {

    }

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
    public void acceptInteraction(AreaInteractionVisitor v) {

    }
}
