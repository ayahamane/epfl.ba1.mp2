package ch.epfl.cs107.play.game.icwars.area;

import ch.epfl.cs107.play.game.areagame.AreaBehavior;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icwars.handler.ICWarsInteractionVisitor;
import ch.epfl.cs107.play.window.Window;

public class ICWarsBehavior extends AreaBehavior {
    public enum ICWarsCellType {
        NONE(0,0),
        ROAD(-16777216, 0),
        PLAIN(-14112955, 1),
        WOOD(-65536, 3),
        RIVER(-16776961, 0),
        MOUNTAIN(-256, 4),
        CITY(-1,2);

        private final int star;
        private int i;

        ICWarsCellType(int i, int type){
            this.i = i;
            this.star = type;
        }

        /**
         * @return the type as a string.
         */
        public String typeToString(){
            return toString();
        }

        /**
         * Used to compare cell types.
         * @param type is an int representing the number of defense stars
         * @return the type of  cell
         */
        public static ICWarsCellType toType(int type){
            for(ICWarsCellType ict : ICWarsCellType.values()){
                if(ict.i == type)
                    return ict;
            }
            return null;
        }

        /**
         * @return number of defense stars.
         */
        public int getDefenseStar(){
            int t = star;
            return t;
        }
    }

    /**
     * Default ICWarsBehavior Constructor
     * @param window (Window), not null
     * @param name (String): Name of the Behavior, not null
     */
    public ICWarsBehavior(Window window, String name){
        super(window, name);
        int height = getHeight();
        int width = getWidth();
        for(int y = 0; y < height; y++) {
            for (int x = 0; x < width ; x++) {
                ICWarsCellType color = ICWarsCellType.toType(getRGB(height-1-y, x));
                setCell(x,y, new ICWarsCell(x,y,color));
            }
        }
    }

    /**
     * Cell adapted to the Tuto2 game
     */
    public class ICWarsCell extends AreaBehavior.Cell {
        private final ICWarsCellType type;

        public ICWarsCellType getI(){
            return type;
        }


        /**
         * @return number of defense stars.
         */
        public int getDefenseStar(){
            int t = type.star;
            return t;
        }

        /**
         * Default ICWarsCell Constructor
         * @param x (int): x coordinate of the cell
         * @param y (int): y coordinate of the cell
         * @param type (EnigmeCellType), not null
         */
        public ICWarsCell(int x, int y, ICWarsCellType type){
            super(x, y);
            this.type = type;
        }

        /**
         * Returns true if an entity can enter in a cell.
         * @param entity (Interactable), not null
         * @return
         */
        @Override
        protected boolean canLeave(Interactable entity) {
            return true;
        }

        /**
         * Verfifies if an entity can enter in a specific cell considering if it takes space and if the
         * entities that are already there take space.
         * @param entity (Interactable), not null
         * @return
         */
        @Override
        protected boolean canEnter(Interactable entity) {
            for(Interactable element:entities){
                if(entity.takeCellSpace() && element.takeCellSpace()){
               return false;
                }
            }
            return true;
        }

        /**
         * Deals with the interactions between a cell and a visitor.
         * @param v (AreaInteractionVisitor) : the visitor
         */
        @Override
        public void acceptInteraction(AreaInteractionVisitor v) {
            ((ICWarsInteractionVisitor)v).interactWith(this);
        }

        @Override
        public boolean isCellInteractable() {
            return true;
        }

        @Override
        public boolean isViewInteractable() {
            return false;
        }
    }
}
