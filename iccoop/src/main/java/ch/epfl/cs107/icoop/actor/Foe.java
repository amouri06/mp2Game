package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.actor.Interactor;
import ch.epfl.cs107.play.areagame.actor.MovableAreaEntity;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.Animation;
import ch.epfl.cs107.play.engine.actor.OrientedAnimation;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

public abstract class Foe extends MovableAreaEntity implements Interactor, Interactable {

    protected int ANIMATION_DURATION = 24;
    private final int IMMUNITY_DURATION = 24;
    private int deathTimer;
    private int hp;
    private int immuneTimer;
    private Animation deathAnimation;
    private final Vulnerability[] vulnerabilityList;

    /**
     * Default Foe constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity. Not null
     * @param position    (Coordinate): Initial position of the entity. Not null
     */
    public Foe(Area area, Orientation orientation, DiscreteCoordinates position, int hp, Vulnerability[] vulnerabilityList) {
        super(area, orientation, position);
        this.hp = hp;
        this.vulnerabilityList = vulnerabilityList.clone();
        immuneTimer = 0;
        deathTimer = 24;
        deathAnimation = new Animation("icoop/vanish", 7, 2, 2, this, 32, 32, new Vector(-0.5f, 0f), ANIMATION_DURATION/7, false);
    }

    @Override
    public void update(float deltaTime) {
        if (hp <= 0) {
            deathTimer--;
            deathAnimation.update(deltaTime);
        }
        if (deathTimer == 0) {
            getOwnerArea().unregisterActor(this);
        }
        if (immuneTimer > 0) {
            immuneTimer--;
        }
        super.update(deltaTime);
    }

    public boolean isAlive() {
        return hp > 0;
    }

    public void decreaseHealth(int amount, Vulnerability attackType) {
        for (Vulnerability vulnerability : vulnerabilityList) {
            if (attackType == vulnerability && immuneTimer == 0) {
                hp = Math.max(hp - amount, 0);
                immuneTimer = IMMUNITY_DURATION;
            }
        }
    }

    protected int getImmuneTimer() { return immuneTimer; }

    @Override
    public void draw(Canvas canvas) {
        if (hp <= 0) {
            deathAnimation.draw(canvas);
        }
    }

    public Vulnerability[] getVulnerabilityList() {
        return vulnerabilityList.clone();
    }

    public boolean isImmune(){
        return immuneTimer > 0;
    }

    ///Implements Interactor
    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates().jump(getOrientation().toVector()));
    }

    @Override
    public boolean wantsCellInteraction() {
        return false;
    }

    @Override
    public boolean wantsViewInteraction() {
        return false;
    }

    ///Implements Interactable
    @Override
    public boolean takeCellSpace() {
        return hp != 0;
    }

    @Override
    public boolean isCellInteractable() {
        return true;
    }

    @Override
    public boolean isViewInteractable() {
        return true;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
    }
}
