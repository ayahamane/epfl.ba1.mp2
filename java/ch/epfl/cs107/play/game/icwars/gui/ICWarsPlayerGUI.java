package ch.epfl.cs107.play.game.icwars.gui;

import ch.epfl.cs107.play.game.actor.Graphics;
import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.game.icwars.actor.player.AIPlayer;
import ch.epfl.cs107.play.game.icwars.actor.player.RealPlayer;
import ch.epfl.cs107.play.game.icwars.actor.unit.Unit;
import ch.epfl.cs107.play.game.icwars.actor.player.ICWarsPlayer;
import ch.epfl.cs107.play.game.icwars.actor.unit.action.Action;
import ch.epfl.cs107.play.game.icwars.area.ICWarsBehavior;
import ch.epfl.cs107.play.game.icwars.menu.GameOver;
import ch.epfl.cs107.play.game.icwars.menu.StartGame;
import ch.epfl.cs107.play.game.icwars.menu.YouWon;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;
import java.awt.Color;
import java.util.List;

public class ICWarsPlayerGUI implements Graphics {

    private ICWarsBehavior.ICWarsCellType cellTypePlayerGUI;
    private Unit posUnit;
    private float camScaleFactor;
    private ICWarsPlayer icWarsPlayer;
    private Unit selectedUnitGui;
    private ICWarsActionsPanel actionsPanel;
    private ICWarsInfoPanel infoPanel;

    //extensions
    private GameOver gameOver;
    private StartGame startGame;
    private YouWon youWon;
    public static final float FONT_SIZE = 20.f;
    private Integer timer = 100;
    private TextGraphics messageTimer;

    public ICWarsPlayerGUI(float cameraScaleFactor , ICWarsPlayer player){
        camScaleFactor = cameraScaleFactor;
        icWarsPlayer = player;
        infoPanel = new ICWarsInfoPanel(cameraScaleFactor);
        actionsPanel = new ICWarsActionsPanel(cameraScaleFactor);
        gameOver = new GameOver();
        startGame = new StartGame();
        youWon = new YouWon();
    }

    /**
     * Sets the selected unit.
     * @param selectedUnit
     */
    public void setSelectedUnit(Unit selectedUnit) {
        this.selectedUnitGui = selectedUnit;
    }

    /**
     * Sets the setCellTypePlayerGUI.
     * @param cellTypePlayer
     */
    public void setCellTypePlayerGUI(ICWarsBehavior.ICWarsCellType cellTypePlayer) {
        cellTypePlayerGUI = cellTypePlayer;
    }

    /**
     * Sets the posUnit.
     * @param posUnit
     */
    public void setPosUnit(Unit posUnit) {
        this.posUnit = posUnit;
    }

    /**
     * Sets the timer.
     * @param time
     */
    public void setTimer(Integer time){
        timer = time;
    }

    /**
     * Gets the timer.
     * @return
     */
    public Integer getTimer(){
        return timer;
    }

    /**
     * Depending on the ICWars player's state and whether it is the start or the end of the game,
     * calls draws of different objects.
     * @param canvas target, not null
     */
    @Override
    public void draw(Canvas canvas) {
        if(selectedUnitGui!=null && icWarsPlayer.getCurrentState() == ICWarsPlayer.playerState.MOVE_UNIT) {
            selectedUnitGui.drawRangeAndPathTo(icWarsPlayer.getCurrentCells().get(0), canvas);
            if (icWarsPlayer instanceof RealPlayer){
                messageTimer = new TextGraphics(Integer.toString(timer), 1f, Color.RED);
                messageTimer.setParent(selectedUnitGui);
                messageTimer.setAnchor(new Vector(-0.5f, 0.1f));
                messageTimer.draw(canvas);
            }
        }
        if(icWarsPlayer.getCurrentState() == ICWarsPlayer.playerState.ACTION_SELECTION) {
            List<Action> listGui= selectedUnitGui.getListOfActions();
            actionsPanel.setActions(listGui);
            actionsPanel.draw(canvas);
        }
        if(icWarsPlayer.getCurrentState() == ICWarsPlayer.playerState.NORMAL ||
                icWarsPlayer.getCurrentState() == ICWarsPlayer.playerState.SELECT_CELL) {
            infoPanel.setCurrentCell(cellTypePlayerGUI);
            infoPanel.setUnit(posUnit);
            infoPanel.draw(canvas);
        }
        //If the RealPlayer is defeated, (so AIPlayer has won), Game Over appears.
        if (icWarsPlayer instanceof AIPlayer && !icWarsPlayer.isDefeated()){
            if (icWarsPlayer.getInEnd()){
                gameOver.draw(canvas);
            }
        }
        //In the beginning of the game, Start Game appears.
        if(icWarsPlayer.getInStart()){
            startGame.draw(canvas);
       }
        //If the RealPlayer has won, You Won appears.
        if(icWarsPlayer instanceof RealPlayer && !icWarsPlayer.isDefeated()){
            if (icWarsPlayer.getInEnd()){
                youWon.draw(canvas);
            }
        }
    }
}

