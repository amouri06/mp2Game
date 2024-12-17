package ch.epfl.cs107.icoop.actor.collectables;

import ch.epfl.cs107.icoop.actor.ElementalEntity;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.signal.logic.Logic;

public abstract class ElementalItem extends ICoopCellCollectable implements ElementalEntity, Logic {
    private Element element;

    public ElementalItem(Area area, Orientation orientation, DiscreteCoordinates position, Element element) {
        super(area, orientation, position);
        this.element = element;
    }

    @Override
    public boolean isOn() {
        return (isCollected());
    }

    @Override
    public boolean isOff() {
        return (!isCollected());
    }

    @Override
    public Element element() {
        return element;
    }


}
