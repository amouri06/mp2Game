package ch.epfl.cs107.icoop.area.maps;

import ch.epfl.cs107.icoop.actor.*;
import ch.epfl.cs107.icoop.actor.collectables.Explosive;
import ch.epfl.cs107.icoop.actor.collectables.Heart;
import ch.epfl.cs107.icoop.actor.collectables.Staff;
import ch.epfl.cs107.icoop.actor.doors.Door;
import ch.epfl.cs107.icoop.actor.foes.BombFoe;
import ch.epfl.cs107.icoop.actor.foes.HellSkull;
import ch.epfl.cs107.icoop.actor.miscellaneous.AreaComplete;
import ch.epfl.cs107.icoop.actor.miscellaneous.PressurePlate;
import ch.epfl.cs107.icoop.area.ICoopArea;
import ch.epfl.cs107.icoop.handler.DialogHandler;
import ch.epfl.cs107.play.engine.actor.Background;
import ch.epfl.cs107.play.engine.actor.Foreground;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Maze extends ICoopArea {

    /**
     * Maze constructor
     * @param dialogHandler (DialogHandler): in order to publish dialog
     */
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
     * @return (String) area's name
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
        //Maze background and foreground added
        registerActor(new Background(this));
        registerActor(new Foreground(this));

        //Elemental walls added
        for (int i = 0; i < 2; ++i) {
            registerActor(new ElementalWall(this, Orientation.LEFT, new DiscreteCoordinates(4, 35 + i), ElementalWall.WallType.EAU));
        }

        //Pressure plate added
        PressurePlate firstPressurePlate = new PressurePlate(this, new DiscreteCoordinates(6,33));
        registerActor(firstPressurePlate);

        //Elemental walls associated to previous pressure plate added
        for (int i = 0; i < 2; ++i) {
            registerActor(new ElementalWall(this, Orientation.LEFT, new DiscreteCoordinates(6, 35 + i), firstPressurePlate,ElementalWall.WallType.FEU));
        }

        //Elemental walls added
        for (int i = 0; i < 2; ++i) {
            registerActor(new ElementalWall(this, Orientation.DOWN, new DiscreteCoordinates(2 + i, 34), ElementalWall.WallType.FEU));
        }
        for (int i = 0; i < 2; ++i) {
            registerActor(new ElementalWall(this, Orientation.DOWN, new DiscreteCoordinates(5+i, 24), ElementalWall.WallType.EAU));
        }

        //Explosive added
        registerActor(new Explosive(this, Orientation.LEFT, new DiscreteCoordinates(6, 25)));

        //Pressure plate added
        PressurePlate secondPressurePlate = new PressurePlate(this, new DiscreteCoordinates(9,25));
        registerActor(secondPressurePlate);
        //Elemental wall associated to previous pressure plate added
        registerActor(new ElementalWall(this, Orientation.DOWN, new DiscreteCoordinates(8, 21), secondPressurePlate, ElementalWall.WallType.FEU));

        //Hearts added
        registerActor(new Heart(this, Orientation.DOWN, new DiscreteCoordinates(15, 18)));
        registerActor(new Heart(this, Orientation.DOWN, new DiscreteCoordinates(16, 19)));
        registerActor(new Heart(this, Orientation.DOWN, new DiscreteCoordinates(14, 19)));
        registerActor(new Heart(this, Orientation.DOWN, new DiscreteCoordinates(14, 17)));

        //Elemental walls added
        registerActor(new ElementalWall(this, Orientation.DOWN, new DiscreteCoordinates(8, 4), ElementalWall.WallType.EAU));
        registerActor(new ElementalWall(this, Orientation.DOWN, new DiscreteCoordinates(13, 4), ElementalWall.WallType.FEU));

        //Staffs added
        Staff waterStaff = new Staff(this, Orientation.DOWN, new DiscreteCoordinates(13,2), Staff.StaffType.FEU);
        Staff fireStaff = new Staff(this, Orientation.DOWN, new DiscreteCoordinates(8,2), Staff.StaffType.EAU);
        registerActor(waterStaff);
        registerActor(fireStaff);

        //Door logic instanced
        AreaComplete areaComplete = new AreaComplete(fireStaff, waterStaff);

        //HellSkulls added
        for (int i = 0; i < 9; i += 2) {
            registerActor(new HellSkull(this, Orientation.RIGHT, new DiscreteCoordinates(12, 25 + i), 38));
        }

        //HellSkulls added
        for (int i = 0; i < 7; i += 2) {
            registerActor(new HellSkull(this, Orientation.RIGHT, new DiscreteCoordinates(10, 26 + i), 49));
        }

        //BombFoes added
        registerActor(new BombFoe(this, new DiscreteCoordinates(5, 15)));
        registerActor(new BombFoe(this, new DiscreteCoordinates(6, 17)));
        registerActor(new BombFoe(this, new DiscreteCoordinates(10, 17)));
        registerActor(new BombFoe(this, new DiscreteCoordinates(5, 14)));

        //Door added
        List<DiscreteCoordinates> arrivalCoordinates = new ArrayList<DiscreteCoordinates>();
        arrivalCoordinates.add(new DiscreteCoordinates(4,5));
        arrivalCoordinates.add(new DiscreteCoordinates(14, 15));
        registerActor(new Door("Arena", areaComplete, arrivalCoordinates, this, new DiscreteCoordinates(19,6), Collections.singletonList(new DiscreteCoordinates(19,7))));

        //Rocks added
        registerActor(new Rock(this, Orientation.DOWN, new DiscreteCoordinates(15, 6)));
        registerActor(new Rock(this, Orientation.DOWN, new DiscreteCoordinates(15, 7)));

    }
}
