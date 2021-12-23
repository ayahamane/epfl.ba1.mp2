package ch.epfl.cs107.play.game.icwars.actor;

import ch.epfl.cs107.play.game.actor.Actor;
import ch.epfl.cs107.play.game.actor.SoundAcoustics;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.math.Transform;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Audio;
import ch.epfl.cs107.play.window.Canvas;

public class SoundActor implements Actor
{

    private final static SoundAcoustics sound = new SoundAcoustics(ResourcePath.getSound("gameSound"),1.0f,false,
            false,true,false);



    @Override
    public void draw(Canvas canvas){

    }

    @Override
    public Transform getTransform(){
        return null;
    }

    @Override
    public Vector getVelocity(){ return null;}

    public SoundAcoustics getSound(){ return sound;}
}
