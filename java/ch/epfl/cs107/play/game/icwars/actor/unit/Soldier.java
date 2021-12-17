package ch.epfl.cs107.play.game.icwars.actor.unit;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.icwars.actor.unit.action.Action;
import ch.epfl.cs107.play.game.icwars.actor.unit.action.Attack;
import ch.epfl.cs107.play.game.icwars.actor.unit.action.Wait;
import ch.epfl.cs107.play.game.icwars.area.ICWarsArea;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;

import java.util.ArrayList;
import java.util.List;

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
        Action attack = new Attack(this, (ICWarsArea)(getOwnerArea()));
        Action wait = new Wait(this, ((ICWarsArea) getOwnerArea()));
        List<Action> actionList = new ArrayList<>();
        actionList.add(wait);
        actionList.add(attack);
        setListOfActions(actionList);
    }

    @Override
    public int getDamage(){
        return damage;
    }

    @Override
    public int getRadius(){
        return SOLDIER_RADIUS;
    }

}