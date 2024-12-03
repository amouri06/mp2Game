package ch.epfl.cs107.icoop.area;

import ch.epfl.cs107.icoop.actor.Door;
import ch.epfl.cs107.icoop.actor.ElementalEntity;
import ch.epfl.cs107.icoop.actor.Heart;
import ch.epfl.cs107.icoop.actor.ICoopPlayer;
import ch.epfl.cs107.play.engine.actor.Background;
import ch.epfl.cs107.play.engine.actor.Foreground;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.signal.logic.Logic;

import java.util.ArrayList;

public final class OrbWay extends ICoopArea {


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
    }

}