package ch.epfl.cs107.play.game.icwars.actor;

import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.awt.*;

public class Soldier extends Unity
{
    private final static int radius = 2;
    private final static int damage = 2;

    public Soldier (Area area, DiscreteCoordinates position, Faction fac){
        super(area, position, fac);
        setHp((float) 5);
        setName("Tank");
        setMessage(new TextGraphics(Integer.toString((int)getHp()), 0.4f, Color.BLUE));
        getMessage().setParent(this);
        getMessage().setAnchor(new Vector(-0.3f, 0.1f));
        if (getFaction() == Faction.ally){
            setSprite(new Sprite("icwars/friendlySoldier" , 1.5f, 1.5f, this , null , new
                    Vector(-0.25f, -0.25f)));
        } else {
            setSprite(new Sprite("icwars/enemySolider" , 1.5f, 1.5f, this , null , new
                    Vector(-0.25f, -0.25f)));
        }
    }

    @Override
    public void draw(Canvas canvas) {
        getSprite().draw(canvas);
        getMessage().draw(canvas);
    }

    @Override
    public int getDamage(){
        return damage;
    }

}

//compilation mistakes are because of methods that should be implemented but they
//didn't say anything about them so idk what to do in them.

