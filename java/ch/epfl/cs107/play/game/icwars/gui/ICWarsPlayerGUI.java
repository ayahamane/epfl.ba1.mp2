package ch.epfl.cs107.play.game.icwars.gui;

import ch.epfl.cs107.play.game.actor.Graphics;
import ch.epfl.cs107.play.game.icwars.actor.unit.Unit;
import ch.epfl.cs107.play.game.icwars.actor.player.ICWarsPlayer;
import ch.epfl.cs107.play.window.Canvas;


public class ICWarsPlayerGUI implements Graphics {

    private float camScaleFactor;
    private ICWarsPlayer icWarsPlayer;
    private Unit selectedUnitGui;
    private ICWarsActionsPanel actionsPanel;


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
        //QUESTION POUR LINA: Est-ce que je peux fiare une méthode setCurrentState
        // en public dans ICWarsPlayer?
        //AND est ce que ta une listActionsExecutable for each unit?
        if(currentState == ICWarsPlayer.playerState.ACTION_SELECTION) {
            actionsPanel.setActions(selectedUnit.listActionsExecutable);
            draw(actionsPanel);
            //Is that how I should draw cet attribut??
        }
    }
    /*Complétez enfin la méthode draw de ICWarsPlayerGUI de sorte à ce qu’elle affiche :

  • Les actions exécutables par l’unité sélectionnée (voir la figure 7 en (a)).
    (Cela permet d’informer le joueur de quelles actions il peut demander l’exécution pour une unité
    sélectionnée.)
    ==>Pour cela, vous décommenterz le contenu de ICWarsActionPanel et créerez
    un attribut de ce type dans ICWarsPlayerGUI.
    Il suffira à ICWarsPlayerGUI d’utiliser la méthode setActions sur cet attribut, en
    lui passant en paramètre les actions exécutables par l’unité sélectionnée, puis de le
    dessiner. Ceci ne doit bien sûr être réalisé que dans l’état ACTION_SELECTION du
    joueur.

  • Les caractéristiques des cellules sur lesquelles il est positionné (voir la figure 7 en (b)).
    ==>Pour cela, ICWarsPlayerGUI peut utiliser un attribut du type fourni ICWarsInfoPanel.
    Décommentez le contenu de ce fichier qui se trouve dans icwars.gui. Un objet
    ICWarsInfoPanel a pour charge de représenter graphiquement les informations liées
    à un type de cellule et à une unité données. L’enjeu ici est de compléter le code de
    sorte à ce que RealPlayer transmette au bon moment à son ICWarsPlayerGUI les
    informations qui sont nécessaires au ICWarsInfoPanel, à savoir l’unité sur laquelle il
    est éventuellement positionné ainsi que le type de la cellule sur laquelle il se trouve.
    ICWarsPlayerGUI devra dessiner son attribut ICWarsInfoPanel si le joueur associé
    est dans l’état NORMAL ou SELECT_CELL seulement.*/
}

