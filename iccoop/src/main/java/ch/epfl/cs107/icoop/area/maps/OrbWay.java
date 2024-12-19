package ch.epfl.cs107.icoop.area.maps;

import ch.epfl.cs107.icoop.actor.*;
import ch.epfl.cs107.icoop.actor.collectables.Heart;
import ch.epfl.cs107.icoop.actor.collectables.Orb;
import ch.epfl.cs107.icoop.actor.doors.Door;
import ch.epfl.cs107.icoop.actor.miscellaneous.AreaComplete;
import ch.epfl.cs107.icoop.actor.miscellaneous.PressurePlate;
import ch.epfl.cs107.icoop.area.ICoopArea;
import ch.epfl.cs107.icoop.handler.DialogHandler;
import ch.epfl.cs107.play.engine.actor.Background;
import ch.epfl.cs107.play.engine.actor.Foreground;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.signal.logic.Logic;

import java.util.ArrayList;

public final class OrbWay extends ICoopArea implements Logic{
    private AreaComplete areaComplete;

    /**
     * OrbWay constructor
     * @param dialogHandler (DialogHandler): in order to publish dialog
     */
    public OrbWay(DialogHandler dialogHandler) {
        super(dialogHandler);
    }

    /**@return (boolean): true if the signal is the area is completed*/
    @Override
    public boolean isOn() {
        if (areaComplete==null){
            return false;
        }
        return areaComplete.isOn();
    }

    /**@return (boolean): true if the signal is the area is not completed*/
    @Override
    public boolean isOff() {
        if (areaComplete==null){
            return true;
        }
        return areaComplete.isOff();
    }

    /**
     *
     * @return red player's spawn coordinates in this area
     */
    @Override
    public DiscreteCoordinates getRedPlayerSpawnPosition() {
        return new DiscreteCoordinates(1,12);
    }


    /**
     *
     * @return blue player's spawn coordinates in this area
     */
    @Override
    public DiscreteCoordinates getBluePlayerSpawnPosition() {
        return new DiscreteCoordinates(1,5);
    }

    /**
     *
     * @return area's name
     */
    @Override
    public String getTitle() {
        return "OrbWay";
    }

    /**
     *
     * Associates corresponding background and foreground to area
     */
    @Override
    protected void createArea() {
        //Registers backgrounds and foregrounds of area
        registerActor(new Background(this));
        registerActor(new Foreground(this));

        //Registers doors
        ArrayList<DiscreteCoordinates> doorArrivalCoords = new ArrayList<DiscreteCoordinates>();
        doorArrivalCoords.add(new DiscreteCoordinates(18,15));
        doorArrivalCoords.add(new DiscreteCoordinates(18,16));

        ArrayList<DiscreteCoordinates> firstDoorArrivalCoords = new ArrayList<DiscreteCoordinates>();
        firstDoorArrivalCoords.add(new DiscreteCoordinates(0,13)); firstDoorArrivalCoords.add(new DiscreteCoordinates(0,12)); firstDoorArrivalCoords.add(new DiscreteCoordinates(0,11)); firstDoorArrivalCoords.add(new DiscreteCoordinates(0,10));

        registerActor(new Door("Spawn", Logic.TRUE, doorArrivalCoords, this, new DiscreteCoordinates( 0,14), firstDoorArrivalCoords));

        ArrayList<DiscreteCoordinates> secondDoorArrivalCoords = new ArrayList<DiscreteCoordinates>();
        secondDoorArrivalCoords.add(new DiscreteCoordinates(0,7)); firstDoorArrivalCoords.add(new DiscreteCoordinates(0,6)); firstDoorArrivalCoords.add(new DiscreteCoordinates(0,5)); firstDoorArrivalCoords.add(new DiscreteCoordinates(0,4));

        registerActor(new Door("Spawn", Logic.TRUE, doorArrivalCoords, this, new DiscreteCoordinates( 0,8), secondDoorArrivalCoords));

        //Registers 4 hearts to the area
        registerActor(new Heart(this, Orientation.DOWN, new DiscreteCoordinates(8,4)));
        registerActor(new Heart(this, Orientation.DOWN, new DiscreteCoordinates(10,6)));
        registerActor(new Heart(this, Orientation.DOWN, new DiscreteCoordinates(5,13)));
        registerActor(new Heart(this, Orientation.DOWN, new DiscreteCoordinates(10,11)));

        //Registers orbs to area
        Orb fireOrb = new Orb(this, new DiscreteCoordinates(17,12), ElementalEntity.Element.FEU);
        registerActor(fireOrb);
        Orb waterOrb = new Orb(this, new DiscreteCoordinates(17,6), ElementalEntity.Element.EAU);
        registerActor(waterOrb);

        //instance areaComplete associated with the collection of both orbs
        areaComplete = new AreaComplete(fireOrb, waterOrb);

        //Registers a pressure plate
        PressurePlate firstPressurePlate = new PressurePlate(this, new DiscreteCoordinates(5, 7));
        registerActor(firstPressurePlate);
        //Registers walls assocaied with the previous pressure plate
        for (int i = 0; i < 5; ++i) {
            registerActor(new ElementalWall(this, Orientation.LEFT, new DiscreteCoordinates(12, 10 + i), firstPressurePlate, ElementalWall.WallType.FEU ));
        }

        //Registers a pressure plate
        PressurePlate secondPressurePlate = new PressurePlate(this, new DiscreteCoordinates(5, 10));
        registerActor(secondPressurePlate);
        //Registers walls assocaied with the previous pressure plate
        for (int i = 0; i < 5; ++i) {
            registerActor(new ElementalWall(this, Orientation.LEFT, new DiscreteCoordinates(12, 4 + i), secondPressurePlate, ElementalWall.WallType.EAU));
        }

        //registers elemental walls
        registerActor(new ElementalWall(this, Orientation.LEFT, new DiscreteCoordinates(7, 6), ElementalWall.WallType.FEU ));
        registerActor(new ElementalWall(this, Orientation.LEFT, new DiscreteCoordinates(7, 12), ElementalWall.WallType.EAU));


    }
}