package ch.epfl.cs107.play.game.icwars.actor;

import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Path;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.icwars.area.ICWarsRange;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Queue;

abstract public class Unit extends ICWarsActor {

    private String name;
    private float hp;
    private Sprite sprite;
    private ICWarsRange range = new ICWarsRange();
    private static int radius;


    public Unit(Area area, DiscreteCoordinates position, Faction fac){
        super(area, position, fac);
        setRange(position);
    }

    private void setRange(DiscreteCoordinates position){
            radius = getRadius();
            for (int x = - radius; x <= radius; ++x) {
                for (int y= -radius; y <= radius; ++y) {
                    DiscreteCoordinates newPosition = new DiscreteCoordinates(x+position.x,y+position.y);
                    boolean hasLeftEdge = false;
                    boolean hasRightEdge = false;
                    boolean hasUpEdge = false;
                    boolean hasDownEdge = false;

                    //Code Mamoun:
                    //if( n > -radius && position.x+n>0 ) { hasLeftEdge = true; }
                    //if( n < radius && position.x+n>0 ) { hasRightEdge = true; }
                    //if( m >= -radius && position.y+m>=0 ) { hasUpEdge = true; }
                    //if( m <= radius && position.y+m>=0 ) { hasDownEdge = true; }

                    //Code Aya qui essaye de merge les deux lul:
                    //Merci Aya.
                    //Code final (askip).
                    if( x > -radius && newPosition.x > 0 ) { hasLeftEdge = true; }
                    if( x < radius &&  newPosition.x < getOwnerArea().getWidth()-1 ) { hasRightEdge = true; }
                    if( y > -radius && newPosition.y > 0 ) { hasDownEdge = true; }
                    if( y < radius && newPosition.y < getOwnerArea().getHeight()-1 ) { hasUpEdge = true; }

                    range.addNode(newPosition, hasLeftEdge, hasUpEdge, hasRightEdge, hasDownEdge);
                }
            }
    }

    public void draw(Canvas canvas) {
        sprite.draw(canvas);
    }

    /**
     * Draw the unit's range and a path from the unit position to
     destination
     * @param destination path destination
     * @param canvas canvas
     */
    public void drawRangeAndPathTo(DiscreteCoordinates destination,
                                          Canvas canvas) {
        range.draw(canvas);
        Queue<Orientation> path =
                range.shortestPath(getCurrentMainCellCoordinates(),
                        destination);
        //Draw path only if it exists (destination inside the range)
        if (path != null){
            new Path(getCurrentMainCellCoordinates().toVector(),
                    path).draw(canvas);
        }
    }

    public boolean isDead(){
        if (hp == 0){
            return true;
        }
        return false;
    }

    public void undergoDamage(float minus){
        hp = hp - minus;
        if (hp < 0){
            hp = 0;
        }
    }

    public void fix(float plus){
        hp = hp + plus;
    }

    abstract int getDamage();

    //This method has only been created to figure in the code.
    //This is not its real way of functioning (Haven't figure it yet).
    public boolean moveInRadius(){
        return true;
    }

    @Override
    public boolean takeCellSpace() { return true; }

    /**@return (boolean): true if this is able to have cell interactions*/
    public boolean isCellInteractable(){
        return true;
    }

    /**@return (boolean): true if this is able to have view interactions*/
    public boolean isViewInteractable(){
        return false;
    }

    public String getName(){
        return name;
    }

    public float getHp(){
        return hp;
    }

    //These methods are here to help us in the coming code. Can we keep them?
    protected void setName(String n){
        name = n;
    }

    protected void setHp(float energy){
        hp = energy;
    }

    protected void setSprite(Sprite s){
        sprite = s;
    }

    protected int getRadius(){
        return radius;
    }
}
