package ch.epfl.cs107.play.game.icwars;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.AreaGame;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icwars.actor.ICWarsActor;
import ch.epfl.cs107.play.game.icwars.actor.Unit;
import ch.epfl.cs107.play.game.icwars.actor.player.ICWarsPlayer;
import ch.epfl.cs107.play.game.icwars.area.ICWarsArea;
import ch.epfl.cs107.play.game.icwars.area.Level0;
import ch.epfl.cs107.play.game.icwars.area.Level1;
import ch.epfl.cs107.play.game.tutosSolution.actor.GhostPlayer;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Window;

public class ICWars extends AreaGame {

    private ICWarsPlayer player;
    private final String[] areas = {"icwars/Level0", "icwars/Level1"};
    private int areaIndex;
    
    /**
     * Add all the areas
     */
    private void createAreas(){

        addArea(new Level0());
        addArea(new Level1());

    }

    @Override
    public boolean begin(Window window, FileSystem fileSystem) {


        if (super.begin(window, fileSystem)) {
            createAreas();
            areaIndex = 0;
            return true;
        }
        return false;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

    }

    @Override
    public void end() {
    }

    @Override
    public String getTitle() {
        return "ICWars";
    }

    protected void switchArea() {
        player.leaveArea();
        areaIndex = (areaIndex == 0) ? 1 : 0;
        ICWarsArea currentArea = (ICWarsArea) setCurrentArea(areas[areaIndex], false);
        player.enterArea(currentArea, currentArea.getPlayerSpawnPosition());
    }
}
