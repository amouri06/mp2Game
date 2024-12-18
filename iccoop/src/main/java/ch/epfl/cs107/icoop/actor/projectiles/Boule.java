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

public class Boule extends Projectile implements ElementalEntity {
    private AttackType attackType;
    private final static int ANIMATION_DURATION = 12;
    private Animation animation;

    @Override
    public Element element() {
        return attackType.getElement();
    }

    /**
     *Enum Foe the different types of attacks depending on the element of the attacker.
     */
    public enum AttackType{
        FEU("icoop/magicFireProjectile", Element.FEU, Vulnerability.FIRE),
        EAU("icoop/magicWaterProjectile", Element.EAU, Vulnerability.WATER );

        private String animationString;
        private Element element;
        private Vulnerability vulnerability;

        private AttackType(String animationString, Element element, Vulnerability vulnerability){
            this.animationString=animationString;
            this.vulnerability=vulnerability;
            this.element=element;
        }

        public Element getElement() {
            return element;
        }

        public String getAnimationString() {
            return animationString;
        }

        public Vulnerability getVulnerability() {
            return vulnerability;
        }

    }

    /**
     * Constructor for Boule
     * @param area (Area)
     * @param orientation (Orientation)
     * @param position (DiscreteCoordinates)
     * @param maxDistance (int)
     * @param attackType (AttackType)
     */
    public Boule(Area area, Orientation orientation, DiscreteCoordinates position, int maxDistance, AttackType attackType) {
        super(area, orientation, position, ANIMATION_DURATION, 2, maxDistance);
        this.attackType = attackType;
        this.animation = new Animation((attackType.getAnimationString()), 4, 1, 1, this, 32, 32,
                ANIMATION_DURATION / 4, true);
    }

    ///Implements Interactor through extending Projectile
    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {
        other.acceptInteraction(new Boule.BouleInteractionHandler(), isCellInteraction);
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICoopInteractionVisitor)v).interactWith(this, isCellInteraction);
    }

    /**
     * Updates the attack's animation
     * @param deltaTime elapsed time since last update, in seconds, non-negative
     */
    public void update(float deltaTime){
        animation.update(deltaTime);
        super.update(deltaTime);
    }


    @Override
    public void draw(Canvas canvas) {
        animation.draw(canvas);
    }

    public String getAnimationString() {
        return attackType.getAnimationString();
    }

    /**
     * Sets all the interaction between the projectile and different elements of the game
     */
    private class BouleInteractionHandler implements ICoopInteractionVisitor {
        @Override
        public void interactWith(Foe foe, boolean isCellInteraction) {
            foe.decreaseHealth(1, attackType.getVulnerability());
            stop();
        }

        @Override
        public void interactWith(Rock rock, boolean isCellInteraction) {
            getOwnerArea().unregisterActor(rock);
        }

        @Override
        public void interactWith(Explosive explosive, boolean isCellInteraction) {
            explosive.activate();
            stop();
        }
    }
}
