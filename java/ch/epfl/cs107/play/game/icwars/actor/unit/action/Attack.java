package ch.epfl.cs107.play.game.icwars.actor.unit.action;

import ch.epfl.cs107.play.game.icwars.actor.ICWarsActor;
import ch.epfl.cs107.play.game.icwars.actor.player.ICWarsPlayer;
import ch.epfl.cs107.play.game.icwars.actor.unit.Unit;
import ch.epfl.cs107.play.game.icwars.area.ICWarsArea;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

import java.util.List;

public class Attack extends Action
{

    public Attack (Unit u, ICWarsArea a) {
        super(u, a);
        setName("(A)ttack");
        setKey(65);
    }

    public void draw(Canvas canvas){

    }


    public void doAction(float dt, ICWarsPlayer player, Keyboard keyboard){

        ICWarsActor.Faction playerFaction = player.getFaction();
        int unitRadius = getUnit().getRadius();

        //Je suis censée avoir un tableau avec les unités que je "pourrai" attaquer ms
        //jsp cmt faire pour l'avoir, je ferai ça qd je concretiserai mes idées.
        List<Integer> unitsIndex = getArea().attackableUnits(playerFaction, unitRadius);


        int unitToAttack = 0;
        //Position par défaut de l'unité à attaquer.


        if (keyboard.get(keyboard.RIGHT).isReleased() && unitToAttack < unitsIndex.size() - 1 ){
            ++unitToAttack;
        }

        if (keyboard.get(keyboard.LEFT).isReleased() && unitToAttack > 0){
            --unitToAttack;
        }

        if (keyboard.get(Keyboard.ENTER).isReleased()){
            int damageToApply = getUnit().getDamage();
            getArea().applyDamage(unitToAttack, damageToApply, 2); //Les stars sont
            //la classe Cells que j'ai pas codée, donc je sais pas trop cmt y avoir accés,
            //y a des indications dans l'énoncé mais je verrai ça plus tard.
        }



    }


}
