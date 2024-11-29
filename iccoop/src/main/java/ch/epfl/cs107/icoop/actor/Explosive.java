package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.actor.Interactor;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Explosive extends AreaEntity implements Interactable, Interactor {

    private boolean explosed;
    private boolean activated;
    private int timer;
    /**
     * Default Explosive constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity in the Area. Not null
     * @param position    (DiscreteCoordinate): Initial position of the entity in the Area. Not null
     */
    public Explosive(Area area, Orientation orientation, DiscreteCoordinates position, int timer) {
        super(area, orientation, position);
        this.timer = timer;
        explosed = false;
        activated = false;
    }

    @Override
    public void update(float deltaTime) {
        if (timer == 0 & ! explosed) {
            explode();
        }
        if (activated & !explosed) {
            timer--;
        }
    }

    public void explode() {

    }


    ///Implements Interactable
    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public boolean takeCellSpace() {
        return false;
    }

    @Override
    public boolean isCellInteractable() {
        return (!activated && !explosed);
    }

    @Override
    public boolean isViewInteractable() {
        return !explosed;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {

    }

    ///Implements Interactor
    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        List<DiscreteCoordinates> list = new ArrayList<DiscreteCoordinates>();
        DiscreteCoordinates currentCell = getCurrentMainCellCoordinates();
        for (int i = 1; i < 5; ++i) {
            list.add(currentCell.jump(i,0));
            list.add(currentCell.jump(-i,0));
            list.add(currentCell.jump(0,i));
            list.add(currentCell.jump(0,-i));
        }
        return list;
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

    private class ExplosiveInteractionHandler implements ICoopInteractionVisitor {
        @Override
        public void interactWith(Rock rock) {
            if (getFieldOfViewCells().contains(rock)) {
                getOwnerArea().unregisterActor(rock);
            }
        }
    }


}
