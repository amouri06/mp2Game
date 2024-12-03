package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.engine.actor.RPGSprite;
import ch.epfl.cs107.play.engine.actor.Sprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;

public class Orb extends ElementalItem {

    private Sprite[] sprites;
    private final static int ANIMATION_FRAMES = 6;
    private final static int ANIMATION_DURATION = 24;


    public Orb(Area area, DiscreteCoordinates position, Element element) {
        super(area, Orientation.UP, position, element);
        final Sprite[] sprites = new Sprite[ANIMATION_FRAMES];
        for (int i = 0; i < ANIMATION_FRAMES; i++) {
            //sprites[i] = new RPGSprite("icoop/orb", 1, 1, this, new RegionOfInterest(i * 32, spriteYDelta, 32, 32));
        }
    }

}
