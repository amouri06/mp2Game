package ch.epfl.cs107.icoop.area.maps;

import ch.epfl.cs107.icoop.actor.*;
import ch.epfl.cs107.icoop.actor.collectables.Coin;
import ch.epfl.cs107.icoop.actor.collectables.Explosive;
import ch.epfl.cs107.icoop.actor.doors.Door;
import ch.epfl.cs107.icoop.area.ICoopArea;
import ch.epfl.cs107.icoop.handler.DialogHandler;
import ch.epfl.cs107.play.engine.actor.Background;
import ch.epfl.cs107.play.engine.actor.Foreground;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.signal.logic.Logic;

import java.util.ArrayList;
import java.util.Collections;

public final class Spawn extends ICoopArea {

    private Logic gameComplete;

    private boolean firstCall = true;

    public Spawn(DialogHandler dialogHandler, Logic gameComplete) {
        super(dialogHandler);
        this.gameComplete = gameComplete;
    }

    @Override
    public void update(float deltaTime) {
        if (firstCall) {
            publish("welcome");
            firstCall = false;
        }
        super.update(deltaTime);
    }


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

        ArrayList<DiscreteCoordinates> door2ArrivalCoords = new ArrayList<DiscreteCoordinates>();
        door2ArrivalCoords.add(new DiscreteCoordinates(2,39));
        door2ArrivalCoords.add(new DiscreteCoordinates(3,39));
        ArrayList<DiscreteCoordinates> door2OtherCoords = new ArrayList<DiscreteCoordinates>();
        door2OtherCoords.add(new DiscreteCoordinates(5,0));

        registerActor(new Door("Maze", Logic.TRUE, door2ArrivalCoords, this, new DiscreteCoordinates( 4,0), door2OtherCoords));

        registerActor(new Explosive(this, Orientation.DOWN, new DiscreteCoordinates(11, 10)));
        registerActor(new Explosive(this, Orientation.DOWN, new DiscreteCoordinates(12, 10)));
        registerActor(new Explosive(this, Orientation.DOWN, new DiscreteCoordinates(12, 11)));

        registerActor(new Coin(this, Orientation.DOWN, new DiscreteCoordinates(14, 14)));

        registerActor(new Rock(this, Orientation.DOWN, new DiscreteCoordinates(10, 10)));
        registerActor(new Rock(this, Orientation.DOWN, new DiscreteCoordinates(4, 9)));
        registerActor(new Rock(this, Orientation.DOWN, new DiscreteCoordinates(3, 10)));
        registerActor(new Rock(this, Orientation.DOWN, new DiscreteCoordinates(5, 10)));

        registerActor(new Coin(this, Orientation.DOWN, new DiscreteCoordinates(15, 13)));

        registerActor(new Door("Spawn", gameComplete, null, this, new DiscreteCoordinates( 6,11), Collections.singletonList(new DiscreteCoordinates(6, 11)), "victory", "key_required"));

    }
}
