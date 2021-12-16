package ch.epfl.cs107.play.game.icwars.actor.unit.action;

import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.game.icwars.actor.ICWarsActor;
import ch.epfl.cs107.play.game.icwars.actor.player.ICWarsPlayer;
import ch.epfl.cs107.play.game.icwars.actor.unit.Unit;
import ch.epfl.cs107.play.game.icwars.area.ICWarsArea;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

import java.util.ArrayList;

public class Attack extends Action
{

    ArrayList<Integer> unitsIndex = new ArrayList<Integer>();
    //Position par défaut de l'unité à attaquer.
    int unitToAttack = 0;

    private ImageGraphics cursor;

    public Attack (Unit u, ICWarsArea a) {
        super(u, a);
        setName("(A)ttack");
        setKey(65);
        cursor = new ImageGraphics(ResourcePath.getSprite("icwars/UIpackSheet"),
                1f, 1f, new RegionOfInterest(4*18, 26*18,16,16));

    }

    public void draw(Canvas canvas){
        if (unitsIndex != null){
            getArea().centerOnUnit(unitToAttack);
            cursor.setAnchor(canvas.getPosition().add(1,0));
            cursor.draw(canvas);
        }
    }


    public void doAction(float dt, ICWarsPlayer player, Keyboard keyboard){

        ICWarsActor.Faction playerFaction = player.getFaction();
        int unitRadius = getUnit().getRadius();
        int x = getUnit().getCurrentCells().get(0).x;
        int y = getUnit().getCurrentCells().get(0).y;


        //Liste d'unités potentiellement attackable que va me livrer Area.
        unitsIndex = getArea().attackableUnits(playerFaction, unitRadius, x, y);



        if ((keyboard.get(keyboard.RIGHT).isReleased()) && (unitToAttack < unitsIndex.size() - 1) ){
            ++unitToAttack;
        }

        if (keyboard.get(keyboard.LEFT).isReleased() && unitToAttack > 0){
            --unitToAttack;
        }

        if (keyboard.get(Keyboard.ENTER).isReleased()){
            int damageToApply = getUnit().getDamage();
            getArea().applyDamage(unitToAttack, damageToApply);
            getUnit().setHasBeenUsed(true);
            player.centerCamera();
            player.setCurrentState(ICWarsPlayer.playerState.NORMAL);
        }
    }


}
