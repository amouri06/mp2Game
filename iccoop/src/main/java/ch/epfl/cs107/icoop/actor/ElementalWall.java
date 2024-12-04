package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.play.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.actor.Interactor;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.signal.logic.Logic;

import java.util.List;

public class ElementalWall extends AreaEntity implements ElementalEntity, Interactable, Interactor {


    private final Element element;
    private Logic logic;
    private final String spriteName;

    public ElementalWall(Area owner, Orientation orientation, DiscreteCoordinates mainCellPosition, Element element, Logic logic, String spriteName) {
        super(owner, orientation, mainCellPosition);
        this.element = element;
        this.logic = logic;
        this.spriteName = spriteName;
    }

    ///Implements ElementalEntity
    @Override
    public Element element() {
        return null;
    }

    ///Implements Interactor
    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return List.of();
    }

    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        return List.of();
    }

    @Override
    public boolean wantsCellInteraction() {
        return false;
    }

    @Override
    public boolean wantsViewInteraction() {
        return false;
    }

    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {

    }

    @Override
    public boolean takeCellSpace() {
        return false;
    }

    @Override
    public boolean isCellInteractable() {
        return false;
    }

    @Override
    public boolean isViewInteractable() {
        return false;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {

    }

    @Override
    public void onLeaving(List<DiscreteCoordinates> coordinates) {

    }

    @Override
    public void onEntering(List<DiscreteCoordinates> coordinates) {

    }
}
