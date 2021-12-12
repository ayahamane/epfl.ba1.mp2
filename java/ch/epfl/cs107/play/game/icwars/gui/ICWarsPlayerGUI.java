package ch.epfl.cs107.play.game.icwars.gui;

import ch.epfl.cs107.play.game.actor.Graphics;
import ch.epfl.cs107.play.game.icwars.actor.Unit;
import ch.epfl.cs107.play.game.icwars.actor.player.ICWarsPlayer;
import ch.epfl.cs107.play.window.Canvas;


public class ICWarsPlayerGUI implements Graphics {

    private float camScaleFactor;
    private ICWarsPlayer icWarsPlayer;
    private Unit selectedUnitGui;

    public ICWarsPlayerGUI(float cameraScaleFactor , ICWarsPlayer player){
        camScaleFactor = cameraScaleFactor;
        icWarsPlayer = player;
    }

    public void setSelectedUnit(Unit selectedUnit) {
        this.selectedUnitGui = selectedUnit;
    }

    @Override
    public void draw(Canvas canvas) {
        if(selectedUnitGui!=null){
            selectedUnitGui.drawRangeAndPathTo(icWarsPlayer.getCurrentCells().get(0), canvas);
        }

    }
}

