package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.actor.Interactor;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.RPGSprite;
import ch.epfl.cs107.play.engine.actor.Sprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Canvas;

import java.util.List;

public class ElementalWall extends AreaEntity implements ElementalEntity, Interactable, Interactor {


    public enum WallType {
        EAU("water_wall", Element.EAU, Logic.TRUE),
        FEU("fire_wall", Element.FEU, Logic.TRUE);

        private Element element;
        private String spriteName;
        private Logic logic;

        private WallType(String spriteName, Element element, Logic logic){
            this.spriteName=spriteName;
            this.element=element;
            this.logic=logic;
        }

        public Element getElement() {
            return element;
        }

        public Logic getLogic() {
            return logic;
        }

        public String getSpriteName() {
            return spriteName;
        }
    }

    private final Element element;
    private final Logic logic;
    private final Sprite[] wallSprites;

    /**
     * @param owner (Owner)
     * @param orientation (Orientation)
     * @param mainCellPosition (DiscreteCoordinates)
     * @param wallType (WallType)
     */
    public ElementalWall(Area owner, Orientation orientation, DiscreteCoordinates mainCellPosition, WallType wallType) {
        super(owner, orientation, mainCellPosition);
        this.element = wallType.getElement();
        this.logic = wallType.getLogic();
        wallSprites = RPGSprite.extractSprites(wallType.getSpriteName(), 4, 1, 1, this, Vector.ZERO, 256, 256);
    }

    /**
     *
     * @param owner (Owner)
     * @param orientation (Orientation)
     * @param mainCellPosition (DiscreteCoordinates)
     * @param logic (Logic)
     * @param wallType (WallType)
     */
    public ElementalWall(Area owner, Orientation orientation, DiscreteCoordinates mainCellPosition, Logic logic, WallType wallType) {
        super(owner, orientation, mainCellPosition);
        this.element = wallType.getElement();
        wallSprites = RPGSprite.extractSprites(wallType.getSpriteName(), 4, 1, 1, this, Vector.ZERO, 256, 256);
        this.logic = logic;
    }

    /**
     * Draws the wall in the game, only if it's on
     * @param canvas target, not null
     */
    public void draw(Canvas canvas) {
        if (logic.isOn()) {
            wallSprites[getOrientation().ordinal()].draw(canvas);
        }
    }


    ///Implements ElementalEntity
    @Override
    public Element element() {
        if (logic.isOff()) {
            return null;
        } else {
            return element;
        }
    }

    ///Implements Interactor
    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return List.of(getCurrentMainCellCoordinates());
    }

    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        return List.of(getCurrentMainCellCoordinates());
    }

    @Override
    public boolean wantsCellInteraction() {
        return true;
    }

    @Override
    public boolean wantsViewInteraction() {
        return false;
    }

    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {
        other.acceptInteraction(new ElementalWallInteractionHandler(), isCellInteraction);
    }

    ///Implements Interactable
    @Override
    public boolean takeCellSpace() {
        return false;
    }

    @Override
    public boolean isCellInteractable() {
        return true;
    }

    @Override
    public boolean isViewInteractable() {
        return true;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICoopInteractionVisitor)v).interactWith(this, isCellInteraction );
    }

    private class ElementalWallInteractionHandler implements ICoopInteractionVisitor {

        @Override
        public void interactWith(ICoopPlayer player, boolean isCellInteraction) {
            if (!player.isElementImmune() && logic.isOn()) {
                player.decreaseHealth(1);
            }
        }
    }
}
