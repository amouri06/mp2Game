package ch.epfl.cs107.icoop.actor.projectiles;

import ch.epfl.cs107.icoop.actor.*;
import ch.epfl.cs107.icoop.actor.collectables.Explosive;
import ch.epfl.cs107.icoop.actor.foes.Foe;
import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.Animation;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.window.Canvas;

public class Fire extends Projectile implements ElementalEntity {

    private Vulnerability vulnerability;
    private Element element;
    private Animation animation;
    private final static int ANIMATION_DURATION = 12;


    /**
     * Default Fire constructor
     *
     * @param area          (Area): Owner area. Not null
     * @param orientation   (Orientation): Initial orientation of the entity. Not null
     * @param position      (Coordinate): Initial position of the entity. Not null
     * @param maxDistance
     */
    public Fire(Area area, Orientation orientation, DiscreteCoordinates position, int maxDistance) {
        super(area, orientation, position, ANIMATION_DURATION, 2, maxDistance);
        vulnerability = Vulnerability.FIRE;
        element = Element.FEU;
        animation = new Animation("icoop/fire", 4, 1, 1, this, 16, 32, ANIMATION_DURATION/4, true);
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICoopInteractionVisitor)v).interactWith(this, isCellInteraction);
    }

    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {
        other.acceptInteraction(new FireInteractionHandler(), isCellInteraction);
    }

    @Override
    public Element element() {
        return element;
    }

    public Vulnerability getVulnerability() {
        return vulnerability;
    }

    @Override
    public void draw(Canvas canvas) {
        animation.draw(canvas);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        animation.update(deltaTime);
    }

    private class FireInteractionHandler implements ICoopInteractionVisitor {

        @Override
        public void interactWith(ICoopPlayer player, boolean isCellInteraction) {
            if (!(player.element() == element && player.isElementImmune())) {
                player.decreaseHealth(1);
            }
        }

        @Override
        public void interactWith(Explosive explosive, boolean isCellInteraction) {
            explosive.activate();
        }

        @Override
        public void interactWith(Foe foe, boolean isCellInteraction) {
            foe.decreaseHealth(1, vulnerability);
        }


    }
}
