package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.engine.actor.RPGSprite;
import ch.epfl.cs107.play.engine.actor.Sprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.window.Canvas;

public class Orb extends ElementalItem {

    private final Sprite[] sprites;
    private OrbType orbType;
    private final static int ANIMATION_FRAMES = 6;
    private final static int ANIMATION_DURATION = 24;
    private int drawnSprite;

    private enum OrbType {

        FEU("orb_fire_msg", 64, Element.FEU),
        EAU("orb_water_msg", 0, Element.EAU),;

        private Element element;
        private String dialog;
        private int spriteYDelta;

        private OrbType(String dialog, int spriteYDelta, Element element) {
            this.dialog = dialog;
            this.spriteYDelta = spriteYDelta;
            this.element = element;
        }

    }


    public Orb(Area area, DiscreteCoordinates position, Element element) {
        super(area, Orientation.UP, position, element);
        for (OrbType orbType : OrbType.values()) {
            if (orbType.element == element) {
                this.orbType = orbType;
            }
        }
        final Sprite[] sprites = new Sprite[ANIMATION_FRAMES];
        for (int i = 0; i < ANIMATION_FRAMES; i++) {
            sprites[i] = new RPGSprite("icoop/orb", 1, 1, this, new RegionOfInterest(i * 32, orbType.spriteYDelta, 32, 32));
        }
        this.sprites = sprites;
        System.out.println(sprites.length);
        drawnSprite = 0;
    }

    @Override
    public void draw(Canvas canvas) {
        sprites[drawnSprite].draw(canvas);
    }

    @Override
    public void update(float deltaTime) {
        drawnSprite = (drawnSprite + 1) % ANIMATION_FRAMES;
    }

}
