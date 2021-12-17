package ch.epfl.cs107.play.game.icwars.gui;

import ch.epfl.cs107.play.game.actor.Graphics;
import ch.epfl.cs107.play.game.icwars.actor.unit.Unit;
import ch.epfl.cs107.play.game.icwars.actor.player.ICWarsPlayer;
import ch.epfl.cs107.play.game.icwars.actor.unit.action.Action;
import ch.epfl.cs107.play.window.Canvas;

import java.util.ArrayList;
import java.util.List;


public class ICWarsPlayerGUI implements Graphics {

    private float camScaleFactor;
    private ICWarsPlayer icWarsPlayer;
    private Unit selectedUnitGui;
    private ICWarsActionsPanel actionsPanel;
    private ICWarsInfoPanel infoPanel;
    public static final float FONT_SIZE = 20.f;

    public ICWarsPlayerGUI(float cameraScaleFactor , ICWarsPlayer player){
        camScaleFactor = cameraScaleFactor;
        icWarsPlayer = player;
        infoPanel = new ICWarsInfoPanel(cameraScaleFactor);
        actionsPanel = new ICWarsActionsPanel(cameraScaleFactor);
    }

    public void setSelectedUnit(Unit selectedUnit) {
        this.selectedUnitGui = selectedUnit;
    }

    @Override
    public void draw(Canvas canvas) {
        if(selectedUnitGui!=null && icWarsPlayer.getCurrentState() == ICWarsPlayer.playerState.MOVE_UNIT){
            selectedUnitGui.drawRangeAndPathTo(icWarsPlayer.getCurrentCells().get(0), canvas);
        }
        //System.out.println("dfhug " + icWarsPlayer.getCurrentState());
        if(icWarsPlayer.getCurrentState() == ICWarsPlayer.playerState.ACTION_SELECTION) {
            //Here, I need the private listofActions :(
            //That's why I added this forloop.
            List<Action> listGui= selectedUnitGui.getListOfActions();
            actionsPanel.setActions(listGui);
            actionsPanel.draw(canvas);
        }
        if(icWarsPlayer.getCurrentState() == ICWarsPlayer.playerState.NORMAL ||
                icWarsPlayer.getCurrentState() == ICWarsPlayer.playerState.SELECT_CELL) {
            infoPanel.draw(canvas);
        }
        //Est-ce que cette donnée est prise en compte ici?(WARNING: A REVOIR)
        // RealPlayer: à savoir l’unité sur laquelle il est éventuellement positionné
    }
}

