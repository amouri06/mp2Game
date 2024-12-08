package ch.epfl.cs107.icoop.area;

import ch.epfl.cs107.icoop.actor.*;
import ch.epfl.cs107.icoop.handler.DialogHandler;
import ch.epfl.cs107.play.engine.actor.Background;
import ch.epfl.cs107.play.engine.actor.Foreground;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.signal.logic.Logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Maze extends ICoopArea {

    public Maze(DialogHandler dialogHandler) {
        super(dialogHandler);
    }
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

        for (int i = 0; i < 2; ++i) {
            registerActor(new ElementalWall(this, Orientation.LEFT, new DiscreteCoordinates(4, 35 + i), ElementalEntity.Element.EAU, Logic.TRUE, "water_wall"));
        }

        PressurePlate firstPressurePlate = new PressurePlate(this, new DiscreteCoordinates(6,33));
        registerActor(firstPressurePlate);
        for (int i = 0; i < 2; ++i) {
            registerActor(new ElementalWall(this, Orientation.LEFT, new DiscreteCoordinates(6, 35 + i), ElementalEntity.Element.FEU, firstPressurePlate, "fire_wall"));
        }

        for (int i = 0; i < 2; ++i) {
            registerActor(new ElementalWall(this, Orientation.DOWN, new DiscreteCoordinates(2 + i, 34), ElementalEntity.Element.FEU, Logic.TRUE, "fire_wall"));
        }

        registerActor(new Explosive(this, Orientation.LEFT, new DiscreteCoordinates(6, 25)));

        for (int i = 0; i < 2; ++i) {
            registerActor(new ElementalWall(this, Orientation.DOWN, new DiscreteCoordinates(5+i, 24), ElementalEntity.Element.EAU, Logic.TRUE, "water_wall"));
        }

        PressurePlate secondPressurePlate = new PressurePlate(this, new DiscreteCoordinates(9,25));
        registerActor(secondPressurePlate);
        registerActor(new ElementalWall(this, Orientation.DOWN, new DiscreteCoordinates(8, 21), ElementalEntity.Element.FEU, secondPressurePlate, "fire_wall"));


        registerActor(new Heart(this, Orientation.DOWN, new DiscreteCoordinates(15, 18)));
        registerActor(new Heart(this, Orientation.DOWN, new DiscreteCoordinates(16, 19)));
        registerActor(new Heart(this, Orientation.DOWN, new DiscreteCoordinates(14, 19)));
        registerActor(new Heart(this, Orientation.DOWN, new DiscreteCoordinates(14, 17)));

        registerActor(new ElementalWall(this, Orientation.DOWN, new DiscreteCoordinates(8, 4), ElementalEntity.Element.EAU, Logic.TRUE, "water_wall"));
        registerActor(new ElementalWall(this, Orientation.DOWN, new DiscreteCoordinates(13, 4), ElementalEntity.Element.FEU, Logic.TRUE, "fire_wall"));

        registerActor(new Staff(this, Orientation.DOWN, new DiscreteCoordinates(13,2), Staff.StaffType.FEU));
        registerActor(new Staff(this, Orientation.DOWN, new DiscreteCoordinates(8,2), Staff.StaffType.EAU));

        for (int i = 0; i < 9; i += 2) {
            registerActor(new HellSkull(this, Orientation.RIGHT, new DiscreteCoordinates(12, 25 + i), 38));
        }

        for (int i = 0; i < 7; i += 2) {
            registerActor(new HellSkull(this, Orientation.RIGHT, new DiscreteCoordinates(10, 26 + i), 49));
        }

        registerActor(new BombFoe(this, new DiscreteCoordinates(5, 15)));
        registerActor(new BombFoe(this, new DiscreteCoordinates(6, 17)));
        registerActor(new BombFoe(this, new DiscreteCoordinates(10, 17)));
        registerActor(new BombFoe(this, new DiscreteCoordinates(5, 14)));

        List<DiscreteCoordinates> arrivalCoordinates = new ArrayList<DiscreteCoordinates>();
        arrivalCoordinates.add(new DiscreteCoordinates(4,5));
        arrivalCoordinates.add(new DiscreteCoordinates(14, 15));
        registerActor(new Door("Arena", Logic.TRUE, arrivalCoordinates, this, new DiscreteCoordinates(19,6), Collections.singletonList(new DiscreteCoordinates(19,7))));

        registerActor(new Rock(this, Orientation.DOWN, new DiscreteCoordinates(15, 6)));
        registerActor(new Rock(this, Orientation.DOWN, new DiscreteCoordinates(15, 7)));


    }
}
