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
        FEU("icoop/player"),
        EAU("icoop/player2"),;

        final String name;

        Element(String name) {
            this.name = name;
        }

        /**
         *
         * @return Element's corresponding name
         */
        public String getName() {
            return name;
        }
    }

    /**
     * returns the element of the ICoopPlayer
     */
    public Element element();
}
