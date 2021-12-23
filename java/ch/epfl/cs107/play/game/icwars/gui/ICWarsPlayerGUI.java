package ch.epfl.cs107.play.game.icwars.gui;

import ch.epfl.cs107.play.game.actor.Graphics;
import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.icwars.actor.player.AIPlayer;
import ch.epfl.cs107.play.game.icwars.actor.player.RealPlayer;
import ch.epfl.cs107.play.game.icwars.actor.unit.Unit;
import ch.epfl.cs107.play.game.icwars.actor.player.ICWarsPlayer;
import ch.epfl.cs107.play.game.icwars.actor.unit.action.Action;
import ch.epfl.cs107.play.game.icwars.area.ICWarsArea;
import ch.epfl.cs107.play.game.icwars.area.ICWarsBehavior;
import ch.epfl.cs107.play.game.icwars.menu.GameOver;
import ch.epfl.cs107.play.game.icwars.menu.StartGame;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class ICWarsPlayerGUI implements Graphics {

    private ICWarsBehavior.ICWarsCellType cellTypePlayerGUI;
    private Unit posUnit;
    private float camScaleFactor;
    private ICWarsPlayer icWarsPlayer;
    private Unit selectedUnitGui;
    private ICWarsActionsPanel actionsPanel;
    private ICWarsInfoPanel infoPanel;
    private GameOver gameOver;
    private StartGame startGame;
    public static final float FONT_SIZE = 20.f;
    private Integer timer = 100;
    private TextGraphics messageTimer;

    public ICWarsPlayerGUI(float cameraScaleFactor , ICWarsPlayer player){
        camScaleFactor = cameraScaleFactor;
        icWarsPlayer = player;
        infoPanel = new ICWarsInfoPanel(cameraScaleFactor);
        actionsPanel = new ICWarsActionsPanel(cameraScaleFactor);
        gameOver = new GameOver(icWarsPlayer.areaWidth(), icWarsPlayer.areaHeight());
        startGame = new StartGame(icWarsPlayer.areaWidth(), icWarsPlayer.areaHeight());
    }

    public void setSelectedUnit(Unit selectedUnit) {
        this.selectedUnitGui = selectedUnit;
    }

    public void setCellTypePlayerGUI(ICWarsBehavior.ICWarsCellType cellTypePlayer) {
        cellTypePlayerGUI = cellTypePlayer;
    }

    public void setPosUnit(Unit posUnit) {
        this.posUnit = posUnit;
    }

    public void setTimer(Integer time){
        timer = time;
    }

    public Integer getTimer(){
        return timer;
    }


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

        if (icWarsPlayer instanceof AIPlayer && !icWarsPlayer.isDefeated()){
            if (icWarsPlayer.getInEnd()){
                gameOver.draw(canvas);
            }
        }
        if(icWarsPlayer.getInStart()){
            startGame.draw(canvas);
       }
    }
}

