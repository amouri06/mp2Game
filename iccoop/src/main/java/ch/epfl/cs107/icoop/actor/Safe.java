package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.Sprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Canvas;

import java.util.List;

public class Safe extends AreaEntity implements Interactable {
    private boolean safeOpened=false;
    private Sprite sprite;
    private Logic orbWay;

    /**
     *
     * @param area (Area) : owner area
     * @param orientation (Orientation)
     * @param position (DiscreteCoordinates)
     * @param orbWay (Logic) : logic indicating that an area has been completed
     */
    public Safe(Area area, Orientation orientation, DiscreteCoordinates position, Logic orbWay) {
        super(area, orientation, position);
        this.orbWay=orbWay;
    }

    /**
     *
     * @return true if the area has been completed
     */
    public boolean isOrbWayComplete(){
        if (orbWay.isOn()){
            return true;
        }
        else return false;
    }

    /**
     * Returns
     * @param canvas target, not null
     */
    public void draw(Canvas canvas){
        sprite.draw(canvas);
    }

    ///Implements Interactable
    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return List.of(getCurrentMainCellCoordinates());
    }

    @Override
    public boolean takeCellSpace() {
        return true;
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
        ((ICoopInteractionVisitor)v).interactWith(this, isCellInteraction );
    }
}
