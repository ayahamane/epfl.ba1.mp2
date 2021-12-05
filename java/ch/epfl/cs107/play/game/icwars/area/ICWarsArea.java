package ch.epfl.cs107.play.game.icwars.area;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Window;

public abstract class ICWarsArea extends Area{
    private ICWarsBehavior behavior;
    private final static float CAMERA_SCALE_FACTOR = 10.f;
    //The camera scale factor was in ICWars at first,
    // but in the subject she said that it should be here.
    //Also,I changed it to a private instead of a public
    /**
     * Create the area by adding it all actors
     * called by begin method
     * Note it set the Behavior as needed !
     */
    protected abstract void createArea();

    /// EnigmeArea extends Area
    @Override
    public final float getCameraScaleFactor() {
        return CAMERA_SCALE_FACTOR;
        //Here, it was an ICWars.CAMERA.... but since now it's declared here, there is no need.
    }

    public abstract DiscreteCoordinates getPlayerSpawnPosition();

   // Demo2Area implements Playable
    @Override
    public boolean begin(Window window, FileSystem fileSystem) {
        if (super.begin(window, fileSystem)) {
            // Set the behavior map
            behavior = new ICWarsBehavior(window, getTitle());
            setBehavior(behavior);
            createArea();
            return true;
        }
        return false;
    }

    @Override
    public String getTitle() {
        return null;
    }
}
