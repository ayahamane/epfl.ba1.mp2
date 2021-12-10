package ch.epfl.cs107.play.game.icwars.handler;

import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icwars.actor.Unit;

public interface ICWarInteractionVisitor extends AreaInteractionVisitor
{
    default void interactWith(Unit u){}

}