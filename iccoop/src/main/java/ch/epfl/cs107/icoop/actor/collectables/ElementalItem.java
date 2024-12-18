package ch.epfl.cs107.icoop.actor.collectables;

import ch.epfl.cs107.icoop.actor.ElementalEntity;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.signal.logic.Logic;

public abstract class ElementalItem extends ICoopCellCollectable implements ElementalEntity, Logic {
    private Element element;

    /**
     *
     * @param area (Area)
     * @param orientation (Orientation)
     * @param position (DiscreteCoordinates)
     * @param element (Element)
     */
    public ElementalItem(Area area, Orientation orientation, DiscreteCoordinates position, Element element) {
        super(area, orientation, position);
        this.element = element;
    }

    /**
     * Returns true if the item was colleceted
     * @return
     */
    @Override
    public boolean isOn() {
        return (isCollected());
    }

    /**
     * Returns true if the item was not collected
     * @return
     */
    @Override
    public boolean isOff() {
        return (!isCollected());
    }

    /**
     * Returns the element of the item
     * @return
     */
    @Override
    public Element element() {
        return element;
    }


}
