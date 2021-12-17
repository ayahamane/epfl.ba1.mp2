package ch.epfl.cs107.play.game.icwars.actor.unit.action;

import ch.epfl.cs107.play.game.icwars.actor.player.ICWarsPlayer;
import ch.epfl.cs107.play.game.icwars.actor.unit.Unit;
import ch.epfl.cs107.play.game.icwars.area.ICWarsArea;
import ch.epfl.cs107.play.window.Keyboard;
import ch.epfl.cs107.play.window.Canvas;


//En principe le codage de cette classe est finie pour ma part.
public class Wait extends Action
{

    public Wait (Unit u, ICWarsArea a){
        super(u,a);
        setName("(W)ait");
        setKey(87);
    }

    public void draw(Canvas canvas){

    }

    public void doAction(float dt, ICWarsPlayer player, Keyboard keyboard){
        System.out.println("WAIT");
        getUnit().setHasBeenUsed(true);
        player.setCurrentState(ICWarsPlayer.playerState.NORMAL);
    }

}
