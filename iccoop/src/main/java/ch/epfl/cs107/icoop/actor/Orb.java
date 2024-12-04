package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.engine.actor.RPGSprite;
import ch.epfl.cs107.play.engine.actor.Sprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.window.Canvas;

public class Orb extends ElementalItem {

    private Sprite[] sprites;
    private OrbType orbType;
    private final static int ANIMATION_FRAMES = 6;
    private final static int ANIMATION_DURATION = 24;
    private int drawnSprite;

    private enum OrbType {

        FEU("orb_fire_msg", 32),
        EAU("orb_water_msg", 32),;

        private String dialog;
        private int spriteYDelta;

        private OrbType(String dialog, int spriteYDelta) {
            this.dialog = dialog;
            this.spriteYDelta = spriteYDelta;
        }

    }


    public Orb(Area area, DiscreteCoordinates position, Element element) {
        super(area, Orientation.UP, position, element);
        final Sprite[] sprites = new Sprite[ANIMATION_FRAMES];
        for (int i = 0; i < ANIMATION_FRAMES; i++) {
            sprites[i] = new RPGSprite("icoop/orb", 1, 1, this, new RegionOfInterest(i * 32, 32, 32, 32));
        }
        drawnSprite = 0;
    }

    @Override
    public void draw(Canvas canvas) {
        sprites[drawnSprite].draw(canvas);
    }

    @Override
    public void update(float deltaTime) {
        drawnSprite = (drawnSprite + 1) % ANIMATION_DURATION;
    }
}
