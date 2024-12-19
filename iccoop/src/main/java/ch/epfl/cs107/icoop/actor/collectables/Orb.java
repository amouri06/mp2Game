package ch.epfl.cs107.icoop.actor.collectables;

import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.RPGSprite;
import ch.epfl.cs107.play.engine.actor.Sprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

public class Orb extends ElementalItem implements Interactable {

    private final Sprite[] sprites;
    private OrbType orbType;
    private final static int ANIMATION_FRAMES = 6;
    private final static int ANIMATION_DURATION = 24;
    private int drawnSprite;

    //enum tracking values associated to ceratin orbs
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

    /**
     * Returns the dialog associated with the different element of the orb
     * @return
     */
    public String getMessage() {
        return orbType.dialog;
    }

    /**
     *
     * @param area (Orientation)
     * @param position (DiscreteCoordinates)
     * @param element (Element)
     */
    public Orb(Area area, DiscreteCoordinates position, Element element) {
        super(area, Orientation.UP, position, element);
        for (OrbType orbType : OrbType.values()) {
            if (orbType.element == element) {
                this.orbType = orbType;
            }
        }
        Sprite[] sprites = new Sprite[ANIMATION_FRAMES];
        for (int i = 0; i < ANIMATION_FRAMES; i++) {
            sprites[i] = new RPGSprite("icoop/orb", 1, 1, this, new RegionOfInterest(i * 32, orbType.spriteYDelta, 32, 32));
        }
        this.sprites = sprites;
        System.out.println(sprites.length);
        drawnSprite = 0;
    }
    ///Implements Interactable
    @Override
    public void draw(Canvas canvas) {
        sprites[drawnSprite].draw(canvas);
    }

    @Override
    public void update(float deltaTime) {
        drawnSprite = (drawnSprite + 1) % ANIMATION_FRAMES;
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    ///Implements Interactor

    @Override
    public boolean takeCellSpace() {
        return false;
    }

    @Override
    public boolean isCellInteractable() {
        return true;
    }

    @Override
    public boolean isViewInteractable() {
        return false;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICoopInteractionVisitor)v).interactWith(this, isCellInteraction);
    }

}
