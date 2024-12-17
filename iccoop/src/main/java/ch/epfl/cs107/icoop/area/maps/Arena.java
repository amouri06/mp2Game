package ch.epfl.cs107.icoop.area.maps;

import ch.epfl.cs107.icoop.actor.*;
import ch.epfl.cs107.icoop.actor.collectables.ElementalKey;
import ch.epfl.cs107.icoop.actor.doors.Teleporter;
import ch.epfl.cs107.icoop.actor.miscellaneous.AreaComplete;
import ch.epfl.cs107.icoop.area.ICoopArea;
import ch.epfl.cs107.icoop.handler.DialogHandler;
import ch.epfl.cs107.play.engine.actor.Background;
import ch.epfl.cs107.play.engine.actor.Foreground;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;

import java.util.ArrayList;
import java.util.List;

public final class Arena extends ICoopArea implements Logic {
    private AreaComplete areaComplete;

    @Override
    public boolean isOn() {
        if (areaComplete==null){
            return false;
        }
        return areaComplete.isOn();
    }

    @Override
    public boolean isOff() {
        if (areaComplete==null){
            return true;
        }
        return areaComplete.isOff();
    }

    public Arena(DialogHandler dialogHandler) {
        super(dialogHandler);
    }

    @Override
    public DiscreteCoordinates getRedPlayerSpawnPosition() {
        return new DiscreteCoordinates(4,5);
    }

    @Override
    public DiscreteCoordinates getBluePlayerSpawnPosition() {
        return new DiscreteCoordinates(14,15);
    }

    @Override
    protected void createArea() {
        registerActor(new Background(this));
        registerActor(new Foreground(this));


        ElementalKey fireKey = new ElementalKey(this, new DiscreteCoordinates(9,16), ElementalEntity.Element.FEU);
        ElementalKey waterKey = new ElementalKey(this, new DiscreteCoordinates(9,4), ElementalEntity.Element.EAU);
        registerActor(fireKey);
        registerActor(waterKey);
        areaComplete= new AreaComplete(fireKey, waterKey);

        List<DiscreteCoordinates> arrivalCoordinates = new ArrayList<DiscreteCoordinates>();
        arrivalCoordinates.add(new DiscreteCoordinates(13,6)); arrivalCoordinates.add(new DiscreteCoordinates(14,6));
        registerActor(new Teleporter("Spawn", areaComplete, arrivalCoordinates, this, new DiscreteCoordinates(10,11)));
    }

    @Override
    public String getTitle() {
        return "Arena";
    }

}
