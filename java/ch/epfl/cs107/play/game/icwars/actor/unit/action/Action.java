package ch.epfl.cs107.play.game.icwars.actor.unit.action;

import ch.epfl.cs107.play.game.actor.Graphics;
import ch.epfl.cs107.play.game.icwars.actor.player.ICWarsPlayer;
import ch.epfl.cs107.play.game.icwars.actor.unit.Unit;
import ch.epfl.cs107.play.game.icwars.area.ICWarsArea;
import ch.epfl.cs107.play.window.Keyboard;


import java.util.List;

abstract public class Action implements Graphics
{

    private Unit unit;
    private ICWarsArea area;
    private String name;
    private int key;

    public Action (Unit u, ICWarsArea a){
        unit = u;
        area = a;
    }

    /**
     * execute the action
     * @param dt  time
     * @param player   player that will execute the action to its unit.
     * @param keyboard  to enter the keyboard's value
     */
    abstract public void doAction(float dt, ICWarsPlayer player, Keyboard keyboard);


    /**
     * execute an action automatically
     * @param dt  time
     * @param player   player that will execute the action to its unit.
     * @param list     list of the unit's possible actions
     */
    abstract public void doAutoAction(float dt, ICWarsPlayer player, List<Action> list);

    /**
     * set the action's name to a new value
     * @param n    the new action's name
     */
    protected void setName(String n){
        name = n;
    }

    /**
     * set the action's key to a new value
     * @param k  new key value
     */
    protected void setKey(int k){
        key = k;
    }

    /**
     *
     * @return the action's unit
     */
    protected Unit getUnit(){;
        return unit;
    }


    /**
     *
     * @return the action's area
     */
    protected ICWarsArea getArea(){
        return area;
    }

    /**
     *
     * @return the action's key
     */
    public int getKey(){return key;}

    /**
     *
     * @return the action's name
     */
    public String getName(){return name;}
}