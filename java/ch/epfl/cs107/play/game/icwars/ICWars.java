package ch.epfl.cs107.play.game.icwars;

import ch.epfl.cs107.play.game.areagame.AreaGame;
import ch.epfl.cs107.play.game.icwars.actor.ICWarsActor;
import ch.epfl.cs107.play.game.icwars.actor.Soldier;
import ch.epfl.cs107.play.game.icwars.actor.Tank;
import ch.epfl.cs107.play.game.icwars.actor.player.ICWarsPlayer;
import ch.epfl.cs107.play.game.icwars.actor.player.RealPlayer;
import ch.epfl.cs107.play.game.icwars.area.ICWarsArea;
import ch.epfl.cs107.play.game.icwars.area.Level0;
import ch.epfl.cs107.play.game.icwars.area.Level1;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Keyboard;
import ch.epfl.cs107.play.window.Window;

public class ICWars extends AreaGame {

    private ICWarsPlayer player;
    private final String[] areas = {"icwars/Level0", "icwars/Level1"};
    private int areaIndex;
    private Window windowReset;
    private FileSystem fileSystemReset;

    /**
     * Add all the areas
     */
    private void createAreas(){
        addArea(new Level0());
        addArea(new Level1());
    }

    @Override
    public boolean begin(Window window, FileSystem fileSystem){
        windowReset=window;
        fileSystemReset=fileSystem;
        if (super.begin(window, fileSystem)) {
            createAreas();
            areaIndex = 0;
            initArea("icwars/Level0");
            return true;
        }
        return false;
    }

    private void initArea(String areaKey) {

        ICWarsArea area = (ICWarsArea)setCurrentArea(areaKey, true);
        DiscreteCoordinates coords = area.getPlayerSpawnPosition();
        DiscreteCoordinates coordsTank = new DiscreteCoordinates(2,5);
        DiscreteCoordinates coordsSoldier = new DiscreteCoordinates(3,5);
        Tank tank=new Tank(area,coordsTank, ICWarsActor.Faction.ally);
        Soldier soldier=new Soldier(area, coordsSoldier, ICWarsActor.Faction.ally);

        player = new RealPlayer(area, coords, ICWarsActor.Faction.ally,"player1",soldier,tank);
        //Demander sur piazza pour ellipse!!
        player.enterArea(area, coords);
        player.centerCamera();

    }

    @Override
    public void update(float deltaTime) {
        Keyboard keyboard= getCurrentArea().getKeyboard();
        super.update(deltaTime);
        if((keyboard.get(Keyboard.N).isReleased())){
            //If the areaIndex is 0, it means that the player is in the Level0 (en tout cas
            //      pour l'instant, c'est ainsi vu que c'est ce qui est demander
            //            dans l'énoncé),so he can go up to the level1.
            if(areaIndex==0){
                switchArea();
            } else {
                System.out.println(end());
                //Ask if that's what they are expecting!
            }
        }
        if ((keyboard.get(Keyboard.R).isReleased())) {
            begin(windowReset, fileSystemReset);
            //Est-ce possible d'avoir une méthode plus générale?
        }
        if (keyboard.get(Keyboard.U).isReleased()) {
            ((RealPlayer)player).selectUnit (1);
        }
        if (keyboard.get(Keyboard.V).isReleased()) {
            ((RealPlayer)player).selectUnit (0);
        }
    }

    @Override
    public String end() {
        //I made end return a String (before it wasn't supposed to return anything(void)).
        return "Game Over :(";
    }

    @Override
    public String getTitle() {
        return "ICWars";
    }

    protected void switchArea() {
        player.leaveArea();
        areaIndex = (areaIndex == 0) ? 1 : 0;
        ICWarsArea currentArea = (ICWarsArea) setCurrentArea(areas[areaIndex], false);
        //player.enterArea(currentArea, currentArea.getPlayerSpawnPosition());
        initArea(currentArea.getTitle());
    }
}
