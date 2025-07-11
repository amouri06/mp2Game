package ch.epfl.cs107.icoop.actor.doors;

import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.signal.logic.Logic;
import java.util.ArrayList;
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
     * @param destination (String)
     * @param signal (Logic)
     * @param arrivalCoordinates (List of DiscreteCoordinates)
     * @param owner (Area)
     * @param mainCellPosition (DiscreteCoordinates)
     * @param occupiedCells (List of DiscreteCoordinates)
     * @param onDialog (String)
     * @param offDialog (String)
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
     * @param door (Door)
     */
    public Door(Door door) {
        this(door.destination, door.signal, door.arrivalCoordinates, door.getOwnerArea(), door.getCurrentMainCellCoordinates(), door.occupiedCells, door.onDialog, door.offDialog);
    }

    /**
     * Constructor n1 for the door
     * @param destination (String)
     * @param signal (Logic)
     * @param arrivalCoordinates (List of DiscreteCoordinates)
     * @param owner (Area)
     * @param mainCellPosition (DiscreteCoordinates)
     * @param occupiedCells (List of DiscreteCoordinates)
     */
    public Door(String destination, Logic signal, List<DiscreteCoordinates>arrivalCoordinates, Area owner, DiscreteCoordinates mainCellPosition, List<DiscreteCoordinates> occupiedCells) {
        this(destination, signal, arrivalCoordinates, owner, mainCellPosition, occupiedCells, null, null);
    }

    /**
     * Returns an array of the arrivalCoordinates of the door
     * @return
     */
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


    /**
     * Returns a dialog associated with the door depending on whether the door is activated.
     * @return
     */
    public String getDialog() {
        if (signal.isOn()) {
            return onDialog;
        } else {
            return offDialog;
        }
    }

}

