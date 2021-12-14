package ch.epfl.cs107.play.game.icwars.actor.unit;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.icwars.actor.unit.Unit;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;

public class Tank extends Unit {

    final static int TANK_RADIUS = 4;
    private final static int damage = 7;

    public Tank (Area area, DiscreteCoordinates position, Faction fac){
        super(area, position, fac);
        setHp((float) 10);
        setName("Tank");
        if (fac == Faction.ally){
            setSprite(new Sprite("icwars/friendlyTank" , 1.5f, 1.5f, this , null , new
                    Vector(-0.25f, -0.25f)));
        } else {
            setSprite(new Sprite("icwars/enemyTank" , 1.5f, 1.5f, this , null , new
                    Vector(-0.25f, -0.25f)));
        }
    }


    @Override
    public int getDamage(){
        return damage;
    }

    @Override
    public int getRadius(){
        return TANK_RADIUS;
    }
}
