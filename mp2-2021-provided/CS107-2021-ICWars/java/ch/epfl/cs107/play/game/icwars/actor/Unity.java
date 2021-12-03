package ch.epfl.cs107.play.game.icwars.actor;

import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.actor.Text;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;

abstract public class Unity extends ICWarsActor
{

    private String name;
    private float hp;
    private TextGraphics message;
    private Sprite sprite;


    public Unity (Area area, DiscreteCoordinates position, Faction fac){
        super(area, position, fac);
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

    abstract public void draw(Canvas canvas);

    //This method has only been created to figure in the code.
    //This is not its real way of functioning (Haven't figure it yet).
    public boolean moveInRadius(){
        return true;
    }

    @Override
    public boolean takeCellSpace() {
        return false;
    }

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

    protected Faction getFaction(){
        return getFaction(); //Wtf j'ai jamais mis de getFaction et
    }

    protected Sprite getSprite(){
        return sprite;
    }

    protected TextGraphics getMessage(){
        return message;
    }

    protected void setMessage(TextGraphics m){
        message = m;
    }

}
