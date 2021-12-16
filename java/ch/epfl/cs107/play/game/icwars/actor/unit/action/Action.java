package ch.epfl.cs107.play.game.icwars.actor.unit.action;

import ch.epfl.cs107.play.game.actor.Graphics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.icwars.actor.player.ICWarsPlayer;
import ch.epfl.cs107.play.game.icwars.actor.unit.Unit;
import ch.epfl.cs107.play.game.icwars.area.ICWarsArea;
import ch.epfl.cs107.play.window.Keyboard;

abstract public class Action implements Graphics
{

    private Unit unit;
    private ICWarsArea area;
    private static String name;
    private static int key;

    public Action (Unit u, ICWarsArea a){
        unit = u;
        area = a;
    }

    abstract public void doAction(float dt, ICWarsPlayer player, Keyboard keyboard);

    protected void setName(String n){
        name = n;
    }

    protected void setKey(int k){
        key = k;
    }

    protected Unit getUnit(){;
        return unit;
    }

    protected ICWarsArea getArea(){
        return area;
    }

}
