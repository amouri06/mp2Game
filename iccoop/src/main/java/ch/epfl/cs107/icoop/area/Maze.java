package ch.epfl.cs107.icoop.area;

import ch.epfl.cs107.icoop.actor.Door;
import ch.epfl.cs107.play.engine.actor.Background;
import ch.epfl.cs107.play.engine.actor.Foreground;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;

import java.util.ArrayList;

public class Maze extends ICoopArea {
    /**
     *
     * @return red player's spawn coordinates in this area
     */
    @Override
    public DiscreteCoordinates getRedPlayerSpawnPosition() {
        return new DiscreteCoordinates(2,39);
    }


    /**
     *
     * @return blue player's spawn coordinates in this area
     */
    @Override
    public DiscreteCoordinates getBluePlayerSpawnPosition() {
        return new DiscreteCoordinates(3,39);
    }

    /**
     *
     * @return area's name
     */
    @Override
    public String getTitle() {
        return "Maze";
    }

    /**
     *
     * Associates corresponding background and foreground to area
     */
    @Override
    protected void createArea() {
        registerActor(new Background(this));
        registerActor(new Foreground(this));
    }
}
