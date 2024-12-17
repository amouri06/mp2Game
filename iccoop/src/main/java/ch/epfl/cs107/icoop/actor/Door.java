package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.signal.logic.Logic;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Door extends AreaEntity implements Interactable {
    private String destination;
    private final List<DiscreteCoordinates> arrivalCoordinates;
    private Logic signal;
    private final List<DiscreteCoordinates> occupiedCells;
    private final String onDialog;
    private final String offDialog;

    /**
     * Constructor n1 for the door
     * @param destination
     * @param signal
     * @param arrivalCoordinates
     * @param owner
     * @param mainCellPosition
     * @param occupiedCells
     * @param onDialog
     * @param offDialog
     */
    public Door(String destination, Logic signal, List<DiscreteCoordinates>arrivalCoordinates, Area owner, DiscreteCoordinates mainCellPosition, List<DiscreteCoordinates> occupiedCells, String onDialog, String offDialog) {
        super(owner, Orientation.UP, mainCellPosition);
        this.destination=destination;
        this.signal=signal;
        this.arrivalCoordinates=arrivalCoordinates;
        this.occupiedCells= occupiedCells;
        this.onDialog = onDialog;
        this.offDialog = offDialog;
    }

    /**
     * Constructor n2 for the door
     * @param door
     */
    public Door(Door door) {
        this(door.destination, door.signal, door.arrivalCoordinates, door.getOwnerArea(), door.getCurrentMainCellCoordinates(), door.occupiedCells, door.onDialog, door.offDialog);
    }

    /**
     * Constructor n3 for the door
     * @param destination
     * @param signal
     * @param arrivalCoordinates
     * @param owner
     * @param mainCellPosition
     * @param occupiedCells
     */
    public Door(String destination, Logic signal, List<DiscreteCoordinates>arrivalCoordinates, Area owner, DiscreteCoordinates mainCellPosition, List<DiscreteCoordinates> occupiedCells) {
        this(destination, signal, arrivalCoordinates, owner, mainCellPosition, occupiedCells, null, null);
    }

    public List<DiscreteCoordinates> getArrivalCoordinates() {
        if (arrivalCoordinates == null) {
            return null;
        }
        return new ArrayList<>(arrivalCoordinates);
    }
    ///Implements Interactable
    @Override
    public List<DiscreteCoordinates> getCurrentCells(){
        List<DiscreteCoordinates> currentCells = new ArrayList<DiscreteCoordinates>(occupiedCells);
        currentCells.add(getCurrentMainCellCoordinates());
        return currentCells;
    }

    @Override
    public boolean takeCellSpace() {
        return false;
    }

    @Override
    public boolean isCellInteractable() {
        return true;
    }

    @Override
    public boolean isViewInteractable(){
        return false;
    }


    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICoopInteractionVisitor)v).interactWith(this, isCellInteraction );
    }

    /**
     * Return whether the door is activated or not
     * @return
     */
    public Logic getSignal() {
        return signal;
    }

    /**
     * Returns where the door leads
     * @return
     */
    public String getDestination() {
        return destination;
    }


    public String getDialog() {
        if (signal.isOn()) {
            return onDialog;
        } else {
            return offDialog;
        }
    }

}

