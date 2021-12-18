package ch.epfl.cs107.play.game.icwars.actor.unit.action;

import ch.epfl.cs107.play.game.actor.Graphics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.icwars.actor.player.ICWarsPlayer;
import ch.epfl.cs107.play.game.icwars.actor.unit.Unit;
import ch.epfl.cs107.play.game.icwars.area.ICWarsArea;
import ch.epfl.cs107.play.window.Keyboard;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.security.Key;
import java.util.List;

abstract public class Action implements Graphics
{

    private Unit unit;
    private ICWarsArea area;
    private String name;
    private int key;//A METTRE EN FINAL

    public Action (Unit u, ICWarsArea a){
        unit = u;
        area = a;
    }

    abstract public void doAction(float dt, ICWarsPlayer player, Keyboard keyboard) throws UnsupportedAudioFileException, IOException;

    abstract public void doAutoAction(float dt, ICWarsPlayer player, List<Action> list);

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

    public int getKey(){return key;}

    public String getName(){return name;}
}