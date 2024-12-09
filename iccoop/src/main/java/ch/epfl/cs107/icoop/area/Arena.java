package ch.epfl.cs107.icoop.area;

import ch.epfl.cs107.icoop.ICoop;
import ch.epfl.cs107.icoop.ICoopBehavior;
import ch.epfl.cs107.icoop.actor.*;
import ch.epfl.cs107.icoop.handler.DialogHandler;
import ch.epfl.cs107.play.engine.actor.Background;
import ch.epfl.cs107.play.engine.actor.Foreground;
import ch.epfl.cs107.play.io.ResourcePath;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Image;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Arena extends ICoopArea {

    private Image behaviorMap;
    private ICoop.AreaCompleteLogic areaCompleteLogic;

    public Arena(DialogHandler dialogHandler) {
        super(dialogHandler);
    }

    public Arena(DialogHandler dialogHandler, Image behaviorMap) {
        this(dialogHandler);
        this.behaviorMap = behaviorMap;
    }

    public Arena(DialogHandler dialogHandler, Image behaviorMap, ICoop.AreaCompleteLogic areaCompleteLogic) {
        this(dialogHandler, behaviorMap);
        this.areaCompleteLogic = areaCompleteLogic;
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

        int height = getHeight();
        int width = getWidth();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                ICoopBehavior.ICoopCellType color = ICoopBehavior.ICoopCellType.toType(behaviorMap.getRGB(height - 1 - y, x));
                switch (color) {
                    case ROCK -> registerActor(new Rock(this, Orientation.DOWN, new DiscreteCoordinates(x,y)));
                    case OBSTACLE -> registerActor(new Obstacle(this, Orientation.DOWN, new DiscreteCoordinates(x,y)));
                }
            }
        }

        registerActor(new ElementalKey(this, new DiscreteCoordinates(9,16), ElementalEntity.Element.FEU));
        registerActor(new ElementalKey(this, new DiscreteCoordinates(9,4), ElementalEntity.Element.EAU));

        List<DiscreteCoordinates> arrivalCoordinates = new ArrayList<DiscreteCoordinates>();
        arrivalCoordinates.add(new DiscreteCoordinates(13,6)); arrivalCoordinates.add(new DiscreteCoordinates(14,6));
        registerActor(new Teleporter("Spawn", areaCompleteLogic, arrivalCoordinates, this, new DiscreteCoordinates(10,11)));
    }

    @Override
    public String getTitle() {
        return "Arena";
    }
}
