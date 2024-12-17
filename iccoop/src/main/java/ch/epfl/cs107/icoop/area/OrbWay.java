package ch.epfl.cs107.icoop.area;

import ch.epfl.cs107.icoop.actor.*;
import ch.epfl.cs107.icoop.handler.DialogHandler;
import ch.epfl.cs107.play.engine.actor.Background;
import ch.epfl.cs107.play.engine.actor.Foreground;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.signal.logic.Logic;

import java.util.ArrayList;
import java.util.List;

public final class OrbWay extends ICoopArea implements Logic{
    private AreaComplete areaComplete;



    public OrbWay(DialogHandler dialogHandler) {
        super(dialogHandler);
    }


    @Override
    public boolean isOn() {
        if (areaComplete==null){
            return false;
        }
        return areaComplete.isOn();
    }

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
        registerActor(new Background(this));
        registerActor(new Foreground(this));

        ArrayList<DiscreteCoordinates> door1ArrivalCoords = new ArrayList<DiscreteCoordinates>();
        door1ArrivalCoords.add(new DiscreteCoordinates(18,15));
        door1ArrivalCoords.add(new DiscreteCoordinates(18,16));

        ArrayList<DiscreteCoordinates> door1OtherCoords = new ArrayList<DiscreteCoordinates>();
        door1OtherCoords.add(new DiscreteCoordinates(0,13)); door1OtherCoords.add(new DiscreteCoordinates(0,12)); door1OtherCoords.add(new DiscreteCoordinates(0,11)); door1OtherCoords.add(new DiscreteCoordinates(0,10));

        registerActor(new Door("Spawn", Logic.TRUE, door1ArrivalCoords, this, new DiscreteCoordinates( 0,14), door1OtherCoords));

        ArrayList<DiscreteCoordinates> door2OtherCoords = new ArrayList<DiscreteCoordinates>();
        door1OtherCoords.add(new DiscreteCoordinates(0,7)); door1OtherCoords.add(new DiscreteCoordinates(0,6)); door1OtherCoords.add(new DiscreteCoordinates(0,5)); door1OtherCoords.add(new DiscreteCoordinates(0,4));

        registerActor(new Door("Spawn", Logic.TRUE, door1ArrivalCoords, this, new DiscreteCoordinates( 0,8), door2OtherCoords));

        registerActor(new Heart(this, Orientation.DOWN, new DiscreteCoordinates(8,4)));
        registerActor(new Heart(this, Orientation.DOWN, new DiscreteCoordinates(10,6)));
        registerActor(new Heart(this, Orientation.DOWN, new DiscreteCoordinates(5,13)));
        registerActor(new Heart(this, Orientation.DOWN, new DiscreteCoordinates(10,11)));

        Orb fireOrb = new Orb(this, new DiscreteCoordinates(17,12), ElementalEntity.Element.FEU);
        registerActor(fireOrb);
        Orb waterOrb = new Orb(this, new DiscreteCoordinates(17,6), ElementalEntity.Element.EAU);
        registerActor(waterOrb);
        areaComplete = new AreaComplete(fireOrb, waterOrb);

        PressurePlate firstPressurePlate = new PressurePlate(this, new DiscreteCoordinates(5, 7));
        registerActor(firstPressurePlate);
        for (int i = 0; i < 5; ++i) {
            registerActor(new ElementalWall(this, Orientation.LEFT, new DiscreteCoordinates(12, 10 + i), firstPressurePlate, ElementalWall.WallType.FEU ));
        }

        PressurePlate secondPressurePlate = new PressurePlate(this, new DiscreteCoordinates(5, 10));
        registerActor(secondPressurePlate);
        for (int i = 0; i < 5; ++i) {
            registerActor(new ElementalWall(this, Orientation.LEFT, new DiscreteCoordinates(12, 4 + i), secondPressurePlate, ElementalWall.WallType.EAU));
        }


        registerActor(new ElementalWall(this, Orientation.LEFT, new DiscreteCoordinates(7, 6), ElementalWall.WallType.FEU ));
        registerActor(new ElementalWall(this, Orientation.LEFT, new DiscreteCoordinates(7, 12), ElementalWall.WallType.EAU));


    }
}