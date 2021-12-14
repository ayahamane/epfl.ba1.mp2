package ch.epfl.cs107.play.game.icwars.actor.unit.action;

import ch.epfl.cs107.play.game.icwars.actor.player.ICWarsPlayer;
import ch.epfl.cs107.play.game.icwars.actor.unit.Unit;
import ch.epfl.cs107.play.game.icwars.area.ICWarsArea;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

import java.util.ArrayList;

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

        //Je suis censée avoir un tableau avec les unités que je "pourrai" attaquer ms
        //jsp cmt faire pour l'avoir, je ferai ça qd je concretiserai mes idées.
        ArrayList<Integer> unitsIndex = new ArrayList<>();


        int unitToAttack = 0;
        //Position par défaut de l'unité à attaquer.


        //Dans ma tête les conditions doivent pouvoir être vérifiées un nombre indeterminé de fois,
        //je ne sais pas cmt faire ça sans invoquer le update, et je suis pas trop à l'aise avec le
        //update, ou bien si on invoque cette méthode dans son update alors ça va.

        if (keyboard.get(keyboard.RIGHT).isReleased() && unitToAttack < unitsIndex.size() - 1 ){
            ++unitToAttack;
        }

        if (keyboard.get(keyboard.LEFT).isReleased() && unitToAttack > 0){
            --unitToAttack;
        }

        if (keyboard.get(Keyboard.ENTER).isReleased()){
            getUnit().getDamage();
        }



    }


}
