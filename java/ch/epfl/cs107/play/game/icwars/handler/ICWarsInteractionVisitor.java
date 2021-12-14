package ch.epfl.cs107.play.game.icwars.handler;

import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icwars.actor.unit.Unit;
import ch.epfl.cs107.play.game.icwars.actor.player.ICWarsPlayer;

public interface ICWarsInteractionVisitor extends AreaInteractionVisitor{
    default void interactWith(Unit u){}
    //Added right now
    default void interactWith(ICWarsPlayer player){
    }
}
