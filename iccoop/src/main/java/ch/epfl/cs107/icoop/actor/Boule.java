package ch.epfl.cs107.icoop.actor;

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
    public Animation animation;
    public Element element;
    public Vulnerability vulnerability;


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

    public Boule(Area area, Orientation orientation, DiscreteCoordinates position, int maxDistance, AttackType attackType) {
        super(area, orientation, position, ANIMATION_DURATION, 2, maxDistance);
        this.element = attackType.getElement();
        this.animation = new Animation((attackType.getAnimationString()), 4, 1, 1, this, 32, 32,
                ANIMATION_DURATION / 4, true);
        this.vulnerability= attackType.getVulnerability();
    }


    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {
        other.acceptInteraction(new Boule.BouleInteractionHandler(), isCellInteraction);
    }
    @Override
    public Element element() {
        return element;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICoopInteractionVisitor)v).interactWith(this, isCellInteraction);
    }
    public void update(float deltaTime){
        animation.update(deltaTime);
        super.update(deltaTime);
    }


    @Override
    public void draw(Canvas canvas) {
        animation.draw(canvas);
    }
    private class BouleInteractionHandler implements ICoopInteractionVisitor {
        @Override
        public void interactWith(Foe foe, boolean isCellInteraction) {
            if (!foe.Immune()){
                foe.decreaseHealth(1);
            }
            stop();
        }

        @Override
        public void interactWith(Rock rock, boolean isCellInteraction) {
            getOwnerArea().unregisterActor(rock);
            stop();
        }

        @Override
        public void interactWith(Explosive explosive, boolean isCellInteraction) {
            explosive.activate();
            stop();
        }
    }
}
