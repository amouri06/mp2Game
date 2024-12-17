package ch.epfl.cs107.icoop.actor.miscellaneous;

import ch.epfl.cs107.icoop.actor.collectables.ElementalItem;
import ch.epfl.cs107.play.signal.logic.Logic;

public class AreaComplete implements Logic {

    private ElementalItem[] items;

    public AreaComplete(ElementalItem... items) {
        this.items = items.clone();
    }

    @Override
    public boolean isOn() {
        for (ElementalItem item: items) {
            if (item.isOff()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isOff() {
        return !isOn();
    }
}
