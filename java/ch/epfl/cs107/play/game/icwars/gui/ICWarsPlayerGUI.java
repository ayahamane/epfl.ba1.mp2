package ch.epfl.cs107.play.game.icwars.gui;

import ch.epfl.cs107.play.game.actor.Graphics;
import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.game.icwars.actor.player.AIPlayer;
import ch.epfl.cs107.play.game.icwars.actor.player.RealPlayer;
import ch.epfl.cs107.play.game.icwars.actor.unit.Unit;
import ch.epfl.cs107.play.game.icwars.actor.player.ICWarsPlayer;
import ch.epfl.cs107.play.game.icwars.actor.unit.action.Action;
import ch.epfl.cs107.play.game.icwars.area.ICWarsBehavior;
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
    public static final float FONT_SIZE = 20.f;
    private Integer timer = 100;
    private TextGraphics messageTimer;

    public ICWarsPlayerGUI(float cameraScaleFactor , ICWarsPlayer player){
        camScaleFactor = cameraScaleFactor;
        icWarsPlayer = player;
        infoPanel = new ICWarsInfoPanel(cameraScaleFactor);
        actionsPanel = new ICWarsActionsPanel(cameraScaleFactor);
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
                messageTimer = new TextGraphics(Integer.toString(timer), 0.8f, Color.DARK_GRAY);
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
        //Est-ce que cette donnée est prise en compte ici?(WARNING: A REVOIR)
        // RealPlayer: à savoir l’unité sur laquelle il est éventuellement positionné
    }

}

