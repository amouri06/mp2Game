package ch.epfl.cs107.icoop.area;

import ch.epfl.cs107.icoop.actor.Door;
import ch.epfl.cs107.icoop.actor.Explosive;
import ch.epfl.cs107.icoop.actor.Rock;
import ch.epfl.cs107.play.engine.actor.Background;
import ch.epfl.cs107.play.engine.actor.Foreground;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.signal.logic.Logic;

import java.util.ArrayList;

public final class Spawn extends ICoopArea {
    /**
     *
     * @return red player's spawn coordinates in this area
     */
    @Override
    public DiscreteCoordinates getRedPlayerSpawnPosition() {
        return new DiscreteCoordinates(13,6);
    }

    /**
     *
     * @return blue player's spawn coordinates in this area
     */
    public DiscreteCoordinates getBluePlayerSpawnPosition() {
        return new DiscreteCoordinates(14,6);
    }

    /**
     *
     * @return area's name
     */
    @Override
    public String getTitle() {
        return "Spawn";
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
        door1ArrivalCoords.add(new DiscreteCoordinates(1,12));
        door1ArrivalCoords.add(new DiscreteCoordinates(1,5));
        ArrayList<DiscreteCoordinates> door1OtherCoords = new ArrayList<DiscreteCoordinates>();
        door1OtherCoords.add(new DiscreteCoordinates(19,16));

        registerActor(new Door("OrbWay", Logic.TRUE, door1ArrivalCoords, this, new DiscreteCoordinates( 19,15), door1OtherCoords));

        registerActor(new Explosive(this, Orientation.DOWN, new DiscreteCoordinates(11, 10)));

        registerActor(new Rock(this, Orientation.DOWN, new DiscreteCoordinates(10, 10)));
    }

}
