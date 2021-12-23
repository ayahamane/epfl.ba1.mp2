package ch.epfl.cs107.play.game.icwars;

import ch.epfl.cs107.play.game.areagame.AreaGame;
import ch.epfl.cs107.play.game.icwars.actor.ICWarsActor;
import ch.epfl.cs107.play.game.icwars.actor.player.AIPlayer;
import ch.epfl.cs107.play.game.icwars.actor.unit.Soldier;
import ch.epfl.cs107.play.game.icwars.actor.unit.Tank;
import ch.epfl.cs107.play.game.icwars.actor.player.ICWarsPlayer;
import ch.epfl.cs107.play.game.icwars.actor.player.RealPlayer;
import ch.epfl.cs107.play.game.icwars.area.ICWarsArea;
import ch.epfl.cs107.play.game.icwars.area.Level0;
import ch.epfl.cs107.play.game.icwars.area.Level1;
import ch.epfl.cs107.play.io.FileSystem;
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
    private Keyboard keyboard;
    private float counter;
    private boolean counting;
    private boolean start = true;
    private enum gameState{
        INIT, CHOOSE_PLAYER, START_PLAYER_TURN, PLAYER_TURN, END_PLAYER_TURN, END_TURN, END
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

    @Override
    public String end() { return null; }

    /**
     * Initiate area of the game
     * @param areaKey
     */
    private void initArea(String areaKey) {
        players = new ArrayList<>();
        playersWaitingForNextRound = new ArrayList<>();
        playersWaitingForCurrentRound = new ArrayList<>();
        ICWarsArea area = (ICWarsArea)setCurrentArea(areaKey, true);
        Tank tank1 = new Tank(area, area.getTankAllySpawnPosition(), ICWarsActor.Faction.ally);
        Soldier soldier1 = new Soldier(area, area.getSoldierAllySpawnPosition(), ICWarsActor.Faction.ally);
        Tank tank2 = new Tank(area, area.getTankEnemySpawnPosition(), ICWarsActor.Faction.enemy);
        Soldier soldier2 = new Soldier(area, area.getSoldierEnemySpawnPosition(), ICWarsActor.Faction.enemy);
        players.add(new RealPlayer(area, area.getPlayerAllySpawnPosition(), ICWarsActor.Faction.ally, "1", tank1, soldier1));
        players.add(new AIPlayer(area, area.getPlayerEnemySpawnPosition(), ICWarsActor.Faction.enemy, "2",tank2, soldier2));
        if (start){
            players.get(0).soundCanBeStarted();
        }
        for(int i = 0; i < players.size(); ++i) {
            if (players.get(i).getFaction() == ICWarsActor.Faction.ally) {
                players.get(i).enterArea(area, area.getPlayerAllySpawnPosition());
            } else {
                players.get(i).enterArea(area, area.getPlayerEnemySpawnPosition());
            }
            players.get(0).centerCamera();
        }
        for(int i = 0; i < players.size(); ++i) {
            playersWaitingForCurrentRound.add(players.get(i));
            if(start){
                players.get(i).setInStart(true);}
        }
        start = false;
        gameCurrentState = gameState.INIT;
    }

    @Override
    public void update(float deltaTime) {
        keyboard = getCurrentArea().getKeyboard();
        super.update(deltaTime);
        changeGameState(deltaTime);
        if ((keyboard.get(Keyboard.R).isReleased())) {
            initArea("icwars/Level0");
            begin(windowReset, fileSystemReset);
        }
        changeGameState(deltaTime);
    }

    /**
     * Describes the different states of a game
     * @param deltaTime
     */
    private void changeGameState(float deltaTime){
        if(gameCurrentState != null) {
            switch (gameCurrentState){
                case INIT:
                        if(waitFor(7,deltaTime)){
                            for(int i = 0; i < players.size(); ++i) {
                                players.get(i).setInStart(false);
                            }
                            gameCurrentState = gameState.CHOOSE_PLAYER;
                        }
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
                    activePlayer.startTurn();
                    gameCurrentState = gameState.PLAYER_TURN;
                    break;
                case PLAYER_TURN:
                    if(activePlayer.playerEndedTurn()){
                        gameCurrentState = gameState.END_PLAYER_TURN;
                    }
                    break;
                case END_PLAYER_TURN:
                    if(activePlayer.isDefeated()){
                        activePlayer.leaveArea();
                    } else {
                        playersWaitingForNextRound.add(activePlayer);
                        activePlayer.unitsReusable();
                    }
                    gameCurrentState = gameState.CHOOSE_PLAYER;
                    break;
                case END_TURN:
                    for(int i = 0; i< playersWaitingForNextRound.size(); ++i){
                        if(playersWaitingForNextRound.get(i).isDefeated()){
                            playersWaitingForNextRound.get(i).leaveArea();
                            playersWaitingForNextRound.remove(i);
                        }
                    }
                    if(playersWaitingForNextRound.size() == 1){
                        gameCurrentState = gameState.END;
                        break;
                    } else {
                        for(int i = 0; i < playersWaitingForNextRound.size(); ++i) {
                            playersWaitingForCurrentRound.add(playersWaitingForNextRound.get(i));
                            playersWaitingForNextRound.remove(i);
                        }
                    }
                    gameCurrentState = gameState.CHOOSE_PLAYER;
                    break;
                case END:
                    if(areaIndex == 0){
                        switchArea();
                    } else {
                        for (int i = 0; i < players.size(); ++i){
                            players.get(i).setInEnd(true);
                        }
                        if (keyboard != null && keyboard.get(Keyboard.ENTER).isPressed()){
                            for (int i = 0; i < players.size(); ++i){
                                players.get(i).setInEnd(false);
                            }
                            switchArea();
                        }
                    }
                    break;
                default:
            }
        }
    }

    /**
     * Used to switch from level0 to level1
     */
    private void switchArea() {
        playersWaitingForNextRound.get(0).leaveArea();
        playersWaitingForNextRound.remove(playersWaitingForNextRound.get(0));
        areaIndex = (areaIndex == 0) ? 1 : 0;
        ICWarsArea currentArea = (ICWarsArea) setCurrentArea(areas[areaIndex], false);
        initArea(currentArea.getTitle());
    }

    /**
     * Ensures that value time elapsed before returning true
     * @param dt elapsed time
     * @param value waiting time (in seconds)
     * @return true if value seconds has elapsed , false otherwise
     */
    private boolean waitFor(float value , float dt) {
        if (counting) {
            counter += dt;
            if (counter > value) {
                counting = false;
                return true;
            }
        } else {
            counter = 0f;
            counting = true;
        }
        return false;
    }

    @Override
    public String getTitle() { return "ICWars"; }
}