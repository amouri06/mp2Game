package ch.epfl.cs107.icoop.handler;

import ch.epfl.cs107.icoop.actor.ICoopCellCollectable;
import ch.epfl.cs107.play.areagame.handler.InventoryItem;
import ch.epfl.cs107.play.engine.actor.Sprite;

public enum ICoopItem implements InventoryItem {
    Sword("icoop/sword.icon"),
    FireKey("icoop/key_red"),
    WaterKey("icoop/key_blue"),
    FireStaff( "icoop/staff_fire.icon"),
    WaterStaff("icoop/staff_water.icon"),
    Explosive("icoop/explosive"),;
    private String spriteName;

    private ICoopItem(String spriteName) {
        this.spriteName = spriteName;
    }


    @Override
    public int getPocketId() {
        return 0;
    }

    @Override
    public String getName() {
        return spriteName;
    }

}
