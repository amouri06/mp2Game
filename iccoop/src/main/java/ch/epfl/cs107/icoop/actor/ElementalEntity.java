package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.play.areagame.actor.MovableAreaEntity;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;

/**
 * Behaving as an elemental entity entails being assigned an element
 */
public interface ElementalEntity {

    public enum Element {
        FEU,
        EAU;

    }

    /**
     * returns the element of the ICoopPlayer
     */
    public Element element();
}
