package ch.epfl.cs107.icoop;

import ch.epfl.cs107.icoop.actor.ElementalEntity;
import ch.epfl.cs107.icoop.actor.Unstoppable;
import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.area.AreaBehavior;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.window.Window;

public final class ICoopBehavior extends AreaBehavior {

    /**
     * Default ICoopBehavior Constructor
     *
     * @param window (Window), not null
     * @param name   (String): Name of the Behavior, not null
     */
    public ICoopBehavior(Window window, String name) {
        super(window, name);
        int height = getHeight();
        int width = getWidth();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                ICoopCellType color = ICoopCellType.toType(getRGB(height - 1 - y, x));
                setCell(x, y, new ICoopCell(x, y, color));
            }
        }
    }

    /**
     * Types of cells adapted to the ICoop game
     */
    public enum ICoopCellType {
        NULL(0, false, false),
        WALL(-16777216, false, false),
        IMPASSABLE(-8750470, false, true),
        INTERACT(-256, true, true),
        DOOR(-195580, true, true),
        WALKABLE(-1, true, true),
        ROCK(-16777204, true, true),
        OBSTACLE(-16723187, true, true);

        final int type;
        final boolean canWalk;
        final boolean canFly;

        ICoopCellType(int t, boolean w, boolean f) {
            type = t;
            canWalk = w;
            canFly = f;
        }

        /**
         *
         * @param type (int)
         * @return ICoopCellType associated with type
         */
        public static ICoopCellType toType(int type) {
            for (ICoopCellType ict : ICoopCellType.values()) {
                if (ict.type == type)
                    return ict;
            }
            return NULL;
        }
    }


    /**
     * Cell adapted to the ICoop game
     */
    public class ICoopCell extends Cell {

        private final ICoopCellType type;

        /**
         * Default ICoopCell Constructor
         *
         * @param x    (int): x coordinate of the cell
         * @param y    (int): y coordinate of the cell
         * @param type (ICoopCellType), not null
         */
        public ICoopCell(int x, int y, ICoopCellType type) {
            super(x, y);
            this.type = type;
        }

        /// Cell implements interactable

        @Override
        protected boolean canLeave(Interactable entity) {
            return true;
        }

        @Override
        protected boolean canEnter(Interactable entity) {
            if (!entity.takeCellSpace()) {
                return true;
            }
            if (entity instanceof Unstoppable) {
                return true;
            }
            for (Interactable cellEntity : entities) {
                if (cellEntity.takeCellSpace()) {
                    return false;
                }
                if (cellEntity instanceof ElementalEntity && entity instanceof ElementalEntity && ((ElementalEntity) entity).element() != ((ElementalEntity) cellEntity).element()  && (((ElementalEntity) cellEntity).element() != null)) {
                    if (!(cellEntity instanceof Unstoppable)) {
                        return false;
                    }
                }
            }
            return type.canWalk;
        }

        @Override
        public boolean isCellInteractable() {
            return true;
        }

        @Override
        public boolean isViewInteractable() {
            return false;
        }

        @Override
        public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
            ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
        }

    }
}
