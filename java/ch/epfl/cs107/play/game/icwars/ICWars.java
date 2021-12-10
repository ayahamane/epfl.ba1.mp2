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

import java.util.ArrayList;
import java.util.List;

public class ICWars extends AreaGame {

    private Window windowReset;
    private FileSystem fileSystemReset;
    private final String[] areas = {"icwars/Level0", "icwars/Level1"};
    private int areaIndex;
    private List<ICWarsPlayer> players;
    private List<ICWarsPlayer> playersWaitingForCurrentRound;
    private List<ICWarsPlayer> playersWaitingForNextRound;
    private ICWarsPlayer activePlayer;
    private gameState gameCurrentState;
    protected enum gameState{
        INIT, CHOOSE_PLAYER, START_PLAYER_TURN, PLAYER_TURN, END_PLAYER_TURN, END_TURN, END;
    }

    /**
     * Add all the areas
     */
    private void createAreas(){
        addArea(new Level0());
        addArea(new Level1());
    }

    @Override
    public boolean begin(Window window, FileSystem fileSystem){
        windowReset = window;
        fileSystemReset = fileSystem;
        if (super.begin(window, fileSystem)) {
            createAreas();
            areaIndex = 0;
            initArea("icwars/Level0");
            return true;
        }
        return false;
    }

    private void initArea(String areaKey) {
        players = new ArrayList<>();
        playersWaitingForNextRound = new ArrayList<>();
        playersWaitingForCurrentRound = new ArrayList<>();
        ICWarsArea area = (ICWarsArea)setCurrentArea(areaKey, true);
        Tank tank1 = new Tank(area, area.getTankAllySpawnPosition(), ICWarsActor.Faction.ally);
        Soldier soldier1 = new Soldier(area, area.getSoldierAllySpawnPosition(), ICWarsActor.Faction.ally);
        Tank tank2 = new Tank(area, area.getTankEnemySpawnPosition(), ICWarsActor.Faction.ally);
        Soldier soldier2 = new Soldier(area, area.getSoldierEnemySpawnPosition(), ICWarsActor.Faction.ally);
        players.add(new RealPlayer(area, area.getPlayerAllySpawnPosition(), ICWarsActor.Faction.ally, "1", tank1, soldier1));
        players.add(new RealPlayer(area, area.getPlayerEnemySpawnPosition(), ICWarsActor.Faction.enemy, "2",tank2, soldier2));
        for(int i = 0; i < players.size(); ++i) {
            if (players.get(i).getFaction() == ICWarsActor.Faction.ally) {
                players.get(i).enterArea(area, area.getPlayerAllySpawnPosition());
            } else {
                players.get(i).enterArea(area, area.getPlayerEnemySpawnPosition());
            }
            players.get(i).centerCamera();
        }
        /*for(int i = 0; i < players.size(); ++i) {//Not sure if that's how and where active player should be initialised.
           if(players.get(i).getCurrentState() == ICWarsPlayer.playerState.IDLE){
               activePlayer=players.get(i);
               i= players.size();
           }
        }*/
        gameCurrentState = gameState.INIT;
    }

    @Override
    public void update(float deltaTime) {
        Keyboard keyboard = getCurrentArea().getKeyboard();
        super.update(deltaTime);
        changeGameState();
        if((keyboard.get(Keyboard.N).isReleased())){//Button N(same que END)
            if(areaIndex == 0){
                switchArea();
            } else {
                System.out.println(end());//Peut être upgraded
            }
        }
        if ((keyboard.get(Keyboard.R).isReleased())) {
            initArea(getCurrentArea().getTitle());
        }
        if (keyboard.get(Keyboard.U).isReleased()) {
            ((RealPlayer)players).selectUnit (1);
        }
        if (keyboard.get(Keyboard.V).isReleased()) {
            ((RealPlayer)players).selectUnit (0);
        }
    }
    protected void changeGameState(){
        switch (gameCurrentState){
            case INIT:
                for(int i = 0; i < players.size(); ++i) {
                    playersWaitingForCurrentRound.add(players.get(i));
                }
                gameCurrentState = gameState.CHOOSE_PLAYER;
                break;
            case CHOOSE_PLAYER:
                if (playersWaitingForCurrentRound.isEmpty()) {
                    gameCurrentState = gameState.END_TURN;
                } else {
                    activePlayer = playersWaitingForCurrentRound.get(0);
                    playersWaitingForCurrentRound.remove(0);
                    gameCurrentState = gameState.START_PLAYER_TURN;
                }
                break;
            case START_PLAYER_TURN:
                activePlayer.start_turn();
                gameCurrentState = gameState.PLAYER_TURN;
                break;
            case PLAYER_TURN:
                if(activePlayer.getCurrentState() == ICWarsPlayer.playerState.IDLE){
                    gameCurrentState = gameState.END_PLAYER_TURN;
                }
                break;
            case END_PLAYER_TURN:
                if(activePlayer.isDefeated()){
                    getCurrentArea().unregisterActor(activePlayer);
                } else {
                    playersWaitingForNextRound.add(activePlayer);
                    activePlayer.unitsReusable();
                }
                gameCurrentState = gameState.CHOOSE_PLAYER;
                break;
            case END_TURN:
                for(int i = 0; i < playersWaitingForNextRound.size(); ++i) {
                    if(playersWaitingForNextRound.get(i).isDefeated()){
                        getCurrentArea().unregisterActor(playersWaitingForNextRound.get(i));
                    }
                }
                if(playersWaitingForNextRound.size() == 1){//One player left
                    gameCurrentState = gameState.END;
                } else {
                    for(int i = 0; i < playersWaitingForNextRound.size(); ++i) {
                        playersWaitingForCurrentRound.add(playersWaitingForNextRound.get(i));
                    }
                }
                gameCurrentState = gameState.CHOOSE_PLAYER;
                break;
            case END:
                if(areaIndex == 0){
                    switchArea();
                } else {
                    System.out.println(end());//May be upgraded
                }
                break;
        }
    }

    @Override
    public String end() { return "Game Over :("; }

    @Override
    public String getTitle() { return "ICWars";
    }

    protected void switchArea() {
        for(int i = 0; i < players.size(); ++i) {
            players.get(i).leaveArea();
        }
        areaIndex = (areaIndex == 0) ? 1 : 0;
        ICWarsArea currentArea = (ICWarsArea) setCurrentArea(areas[areaIndex], false);
        initArea(currentArea.getTitle());
    }
}