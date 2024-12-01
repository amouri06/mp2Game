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

    public Door(String destination, Logic signal, List<DiscreteCoordinates>arrivalCoordinates, Area owner, DiscreteCoordinates mainCellPosition, List<DiscreteCoordinates> occupiedCells) {
        super(owner, Orientation.UP, mainCellPosition);
        this.destination=destination;
        this.signal=signal;
        this.arrivalCoordinates=arrivalCoordinates;
        this.occupiedCells= occupiedCells;
    }

    public DiscreteCoordinates getPlayer1ArrivalCoordinates() {
        return arrivalCoordinates.getFirst();
    }

    public DiscreteCoordinates getPlayer2ArrivalCoordinates() {
        return arrivalCoordinates.get(1);
    }

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

    public Logic getSignal() {
        return signal;
    }

    public String getDestination() {
        return destination;
    }

}

