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

    public void draw(Canvas canvas){}

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

        //NEW:
        if(listCibleàAttaquer.isEmpty() || keyboard.get(Keyboard.TAB).isReleased()){
            player.centerCamera();
            // EHHHHHHHH OHHHHHHHHHH!!!!!!!!!!!!!!!!!!!!!!!!
            //QUESTION POUR LINA: Est-ce que je peux fiare une méthode setCurrentState
            // en public dans ICWarsPlayer?
            currentState = ICWarsPlayer.playerState.ACTION_SELECTION;
        }
        //Instructions:
       /* Complétez également la méthode doAction de Attack de sorte que si la liste des cibles à
        attaquer et vide (ou si le clavier perçoit la touche Tab), la caméra se recentre sur le joueur
        qui doit repasser en état SELECT_ACTION. La méthode doAttack ne fera alors rien de plus (le
       but est ici de redonner une chance de jouer au joueur s’il a inutilement essayé de déclencher
                une attaque alors qu’aucune cible n’est atteignable*/
        //Suite:
        //Complétez enfin la méthode draw de ICWarsPlayerGUI de sorte à


    }


}
