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
        super.update(deltaTime);
        //Code fourni dans l'énoncé
        /*if (keyboard.get(Keyboard.U).isReleased()) {
            ((RealPlayer)player).selectUnit (1); // 0, 1 ...
        }
        //Code added by me pour traiter l'unité en indice 0 dans l'ellipse.
        if (keyboard.get(Keyboard.V).isReleased()) {
            ((RealPlayer)player).selectUnit (0); // 0, 1 ...
        }*/
        //J'ai choisi de coder la méthode selectUnit dans ICWarsPlayer
        // car c'est lui qui contient l'ellipse avec ses unités.
    }

    public void nextLevel(){
        Keyboard keyboard= getCurrentArea().getKeyboard();
        if((keyboard.get(Keyboard.N).isReleased())){
            //If the areaIndex is 0, it means that the player is in the Level0 (en tout cas
            //      pour l'instant, c'est ainsi vu que c'est ce qui est demander
            //            dans l'énoncé),so he can go up to the level1.
            if(areaIndex==0){
                switchArea();
            } else {
                end();
            }
        }
    }
    public void reset() {
        Keyboard keyboard = getCurrentArea().getKeyboard();
        if ((keyboard.get(Keyboard.R).isReleased())) {
            initArea("icwars/Level0");
            //Est-ce possible d'avoir une méthode plus générale?
        }
    }
    @Override
    public String end() {
        //I made end return a String (before it wasn't supposed to return anything(void)).
        return "Game Over";
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

    /*protected ICWarsPlayer getRealPlayer(){
        return player;
    }*/
}
