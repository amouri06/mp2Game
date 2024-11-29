package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.engine.actor.Sprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;

public class Rock extends Obstacle {

    /**
     * Default Obstacle constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity in the Area. Not null
     * @param position    (DiscreteCoordinate): Initial position of the entity in the Area. Not null
     */
    public Rock(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);
        sprite = new Sprite("rock.1", 1.f, 1.f, this);
    }

}
