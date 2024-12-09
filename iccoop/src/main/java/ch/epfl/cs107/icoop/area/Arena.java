package ch.epfl.cs107.icoop.area;

import ch.epfl.cs107.icoop.ICoopBehavior;
import ch.epfl.cs107.icoop.actor.ElementalEntity;
import ch.epfl.cs107.icoop.actor.ElementalKey;
import ch.epfl.cs107.icoop.handler.DialogHandler;
import ch.epfl.cs107.play.engine.actor.Background;
import ch.epfl.cs107.play.engine.actor.Foreground;
import ch.epfl.cs107.play.io.ResourcePath;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.window.Image;

public class Arena extends ICoopArea {

    private Image behaviorMap;

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

        int height = getHeight();
        int width = getWidth();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
            }
        }

        registerActor(new ElementalKey(this, new DiscreteCoordinates(9,16), ElementalEntity.Element.FEU));
        registerActor(new ElementalKey(this, new DiscreteCoordinates(9,4), ElementalEntity.Element.EAU));
    }

    @Override
    public String getTitle() {
        return "Arena";
    }
}
