package ch.epfl.cs107.play.game.icwars.actor;

import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.awt.*;

public class Soldier extends Unit
{
    private final static int SOLDIER_RADIUS = 2;
    private final static int damage = 2;

    public Soldier (Area area, DiscreteCoordinates position, Faction fac){
        super(area, position, fac);
        setHp((float) 5);
        setName("Soldier");
        if (fac == Faction.ally){
            setSprite(new Sprite("icwars/friendlySoldier" , 1.5f, 1.5f, this , null , new
                    Vector(-0.25f, -0.25f)));
        } else {
            setSprite(new Sprite("icwars/enemySoldier" , 1.5f, 1.5f, this , null , new
                    Vector(-0.25f, -0.25f)));
        }
    }


    @Override
    public int getDamage(){
        return damage;
    }

    @Override
    protected int getRadius(){
        return SOLDIER_RADIUS;
    }

}