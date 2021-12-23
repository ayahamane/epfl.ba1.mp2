package ch.epfl.cs107.play.game.icwars.actor.unit.action;

import ch.epfl.cs107.play.game.icwars.actor.ICWarsActor;
import ch.epfl.cs107.play.game.icwars.actor.player.ICWarsPlayer;
import ch.epfl.cs107.play.game.icwars.actor.unit.Unit;
import ch.epfl.cs107.play.game.icwars.area.ICWarsArea;
import ch.epfl.cs107.play.window.Keyboard;
import ch.epfl.cs107.play.window.Canvas;

import java.util.ArrayList;
import java.util.List;

public class Wait extends Action {

    private ArrayList<Integer> attackableUnitsIndex;
    private int unitToAttack;
    public Wait (Unit u, ICWarsArea a){
        super(u,a);
        setName("(W)ait");
        setKey(87);
    }

    public void draw(Canvas canvas) { }

    public void doAction(float dt, ICWarsPlayer player, Keyboard keyboard){
        getUnit().setHasBeenUsed(true);
        player.setCurrentState(ICWarsPlayer.playerState.NORMAL);
    }

    /**
     * Describes the actions executed by the AI player.
     * @param dt delta time
     * @param player AI player
     * @param list of actions
     */
    @Override
    public void doAutoAction(float dt, ICWarsPlayer player, List<Action> list) {
        ICWarsActor.Faction playerFaction = player.getFaction();
        int unitRadius = getUnit().getRadius();
        int x = getUnit().getCurrentCells().get(0).x;
        int y = getUnit().getCurrentCells().get(0).y;
        attackableUnitsIndex = new ArrayList<>();
        attackableUnitsIndex = getArea().attackableUnits(playerFaction, unitRadius, x, y);
       if (!attackableUnitsIndex.isEmpty()) {
           unitToAttack = getArea().autoAttackableUnit(attackableUnitsIndex);
           getArea().applyDamage(attackableUnitsIndex.get(unitToAttack), getUnit().getDamage());
       }
    }
}
