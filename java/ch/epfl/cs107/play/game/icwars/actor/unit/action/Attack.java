package ch.epfl.cs107.play.game.icwars.actor.unit.action;

import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.game.icwars.actor.ICWarsActor;
import ch.epfl.cs107.play.game.icwars.actor.player.ICWarsPlayer;
import ch.epfl.cs107.play.game.icwars.actor.unit.Unit;
import ch.epfl.cs107.play.game.icwars.area.ICWarsArea;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;
import java.util.ArrayList;
import java.util.List;

public class Attack extends Action {

    private ArrayList<Integer> attackableUnitsIndex;
    private int unitToAttack;
    private ImageGraphics cursor;
    boolean firstTimeSet = true;

    public Attack(Unit u, ICWarsArea a) {
        super(u, a);
        setName("(A)ttack");
        setKey(65);
        cursor = new ImageGraphics(ResourcePath.getSprite("icwars/UIpackSheet"),
                1f, 1f, new RegionOfInterest(4 * 18, 26 * 18, 16, 16));
    }

    @Override
    public void draw(Canvas canvas) {
        if (attackableUnitsIndex != null && !attackableUnitsIndex.isEmpty()) {
            cursor.setAnchor(canvas.getPosition().add(1, 0));
            cursor.draw(canvas);
        }
    }

    @Override
    public void doAction(float dt, ICWarsPlayer player, Keyboard keyboard) {
        ICWarsActor.Faction playerFaction = player.getFaction();
        int unitRadius = getUnit().getRadius();
        int x = getUnit().getCurrentCells().get(0).x;
        int y = getUnit().getCurrentCells().get(0).y;

        // sound test

        attackableUnitsIndex = new ArrayList<>();
        attackableUnitsIndex = getArea().attackableUnits(playerFaction, unitRadius, x, y);
        if (firstTimeSet && !attackableUnitsIndex.isEmpty()) {
            unitToAttack = 0;
            firstTimeSet = false;
        }
        if (keyboard.get(keyboard.RIGHT).isReleased()) {
            if (unitToAttack < attackableUnitsIndex.size() - 1) {
                ++unitToAttack;
            } else if (unitToAttack == attackableUnitsIndex.size() - 1) {
                unitToAttack = 0;
            }
        }
        if (keyboard.get(keyboard.LEFT).isReleased()) {
            if (unitToAttack > 0) {
                --unitToAttack;
            } else if (unitToAttack == 0) {
                unitToAttack = attackableUnitsIndex.size() - 1;
            }
        }
        if (!attackableUnitsIndex.isEmpty()) {
            getArea().centerOnUnit(attackableUnitsIndex.get(unitToAttack));
        }
        if (keyboard.get(Keyboard.ENTER).isReleased()) {
            int damageToApply = getUnit().getDamage();
            getArea().applyDamage(attackableUnitsIndex.get(unitToAttack), damageToApply);
            getUnit().setHasBeenUsed(true);
            player.centerCamera();
            firstTimeSet = true;
            player.setCurrentState(ICWarsPlayer.playerState.NORMAL);
        }
        if (attackableUnitsIndex.isEmpty() || keyboard.get(Keyboard.TAB).isReleased()) {
            player.centerCamera();
            player.setCurrentState(ICWarsPlayer.playerState.ACTION_SELECTION);
        }
    }

    @Override
    public void doAutoAction(float dt, ICWarsPlayer player, List<Action> actionList) {
        ICWarsActor.Faction playerFaction = player.getFaction();
        int unitRadius = getUnit().getRadius();
        int x = getUnit().getCurrentCells().get(0).x;
        int y = getUnit().getCurrentCells().get(0).y;
        attackableUnitsIndex = new ArrayList<>();
        attackableUnitsIndex = getArea().attackableUnits(playerFaction, unitRadius, x, y);
        if (!attackableUnitsIndex.isEmpty() && attackableUnitsIndex != null) {
            unitToAttack = getArea().autoAttackableUnit(attackableUnitsIndex);
            getArea().applyDamage(attackableUnitsIndex.get(unitToAttack), getUnit().getDamage());
        }
    }
}