package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.handler.ICoopItem;
import ch.epfl.cs107.play.signal.logic.Logic;

import java.util.ArrayList;
import java.util.List;

public class And implements Logic {

    private ElementalItem[] items;

    public And(ElementalItem... items) {
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
