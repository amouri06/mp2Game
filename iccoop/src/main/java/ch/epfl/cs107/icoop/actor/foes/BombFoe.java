package ch.epfl.cs107.icoop.actor.foes;

import ch.epfl.cs107.icoop.actor.collectables.Explosive;
import ch.epfl.cs107.icoop.actor.ICoopPlayer;
import ch.epfl.cs107.icoop.actor.Vulnerability;
import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.engine.actor.Entity;
import ch.epfl.cs107.play.engine.actor.OrientedAnimation;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.math.random.RandomGenerator;
import ch.epfl.cs107.play.window.Canvas;

import java.util.ArrayList;
import java.util.List;

import static ch.epfl.cs107.play.math.Orientation.*;

public class BombFoe extends Foe {

    private static final int MAX_LIFE = 2;
    private static final int MIN_ATTACKING_DISTANCE = 2;
    private static final int MAX_INACTION_TIME = 24;
    private static final int MIN_PROTECTING_TIME = 72;
    private static final int MAX_PROTECTING_TIME = 120;
    private final Vector anchor = new Vector(-0.5f, 0);
    private final Orientation[] orders = {DOWN, RIGHT, UP, LEFT};
    private State state;
    private final OrientedAnimation normalAnimation;
    private final OrientedAnimation protectingAnimation;
    private int inactionTime;
    private int protectingTime;
    private Interactable target;

    private enum State {
        IDLE(2),
        ATTACKING(6),
        PROTECTING(1);

        private State(int speed) {
            speedFactor = speed;
        }

        public int getSpeedFactor() {
            return speedFactor;
        }

        private int speedFactor;
    }
    /**
     * Default BombFoe constructor
     *
     * @param area              (Area): Owner area. Not null
     * @param position          (Coordinate): Initial position of the entity. Not null
     */
    public BombFoe(Area area, DiscreteCoordinates position) {
        super(area, RIGHT, position, MAX_LIFE, new Vulnerability[]{Vulnerability.PHYSIQUE, Vulnerability.FIRE});
        state = State.IDLE;
        inactionTime = 0;
        protectingTime = 0;
        target = null;
        normalAnimation = new OrientedAnimation("icoop/bombFoe", ANIMATION_DURATION/3, this, anchor, orders, 4, 2, 2, 32, 32, true);
        protectingAnimation = new OrientedAnimation("icoop/bombFoe.protecting", ANIMATION_DURATION/3, this, anchor, orders, 4, 2, 2, 32, 32, false);
    }

    /**
     * Repeatedly calls this method, chances the course of action of the BombFoe depending on his current state (States given in the enum)
     * @param deltaTime elapsed time since last update, in seconds, non-negative
     */
    @Override
    public void update(float deltaTime) {
        if (isImmune()) {
            state = State.IDLE;
            inactionTime = 0;
        }
        else if (state == State.IDLE) {
            double randDouble = RandomGenerator.getInstance().nextDouble();
            if (inactionTime <= 0) {
                if (randDouble <= 0.4) {
                    orientate(orders[RandomGenerator.getInstance().nextInt(4)]);
                }
                move(ANIMATION_DURATION / state.getSpeedFactor());
                inactionTime = RandomGenerator.getInstance().nextInt(MAX_INACTION_TIME);
            }
            else {
                --inactionTime;
            }
        }
        else if (state == State.ATTACKING) {
            if (DiscreteCoordinates.distanceBetween(getCurrentMainCellCoordinates(), ((AreaEntity) target).getCurrentMainCellCoordinates()) > MIN_ATTACKING_DISTANCE) {
                targetedMove();
            }
            else {
                Explosive explosive = new Explosive(getOwnerArea(), DOWN, super.getFieldOfViewCells().getFirst());
                if (getOwnerArea().canEnterAreaCells(explosive, super.getFieldOfViewCells())) {
                    getOwnerArea().registerActor(explosive);
                    explosive.activate();
                }
                state = State.PROTECTING;
            }
        } else if (state == State.PROTECTING) {
            if (target != null) {
                target = null;
                protectingTime = MIN_PROTECTING_TIME + RandomGenerator.getInstance().nextInt(MAX_PROTECTING_TIME - MIN_PROTECTING_TIME);
            } else if (protectingTime > 0) {
                protectingTime--;
                protectingAnimation.update(deltaTime);
            }
            else {
                state = State.IDLE;
            }
            protectingAnimation.update(deltaTime);
        }
        if (isDisplacementOccurs()) {
            normalAnimation.update(deltaTime);
        }
        super.update(deltaTime);
    }

    private void targetedMove() {
        Vector v = getPosition().sub(((Entity) target).getPosition());
        float deltaX = v.getX(); float deltaY = v.getY();
        boolean orientationOccured;
        if (deltaX != 0 || deltaY != 0) {
            if (Math.abs(deltaX) > Math.abs(deltaY)) {
                orientationOccured = !orientate(Orientation.fromVector(new Vector(-deltaX, 0)));
            } else {
                orientationOccured = !orientate(Orientation.fromVector(new Vector(0, -deltaY)));
            }
            if (!orientationOccured) {
                move(ANIMATION_DURATION / state.getSpeedFactor());
            }
        }
    }

    /**
     * Draws the animation of the BombFoe depending on his state
     * @param canvas target, not null
     */
    @Override
    public void draw(Canvas canvas) {
        if (isAlive()) {
            if (state == State.PROTECTING) {
                protectingAnimation.draw(canvas);
            }
            else {
                normalAnimation.draw(canvas);
            }
        }
        super.draw(canvas);
    }

    /**
     * Decreases the BombFoe's health iff it it's not prottecting. Since it is not vulnerable during that time.
     * @param amount
     * @param attackType
     */
    @Override
    public void decreaseHealth(int amount, Vulnerability attackType) {
        if (state != State.PROTECTING) {
            super.decreaseHealth(amount, attackType);
        }
    }



    ///Implements Interactor
    @Override
    public boolean wantsViewInteraction() {
        return true;
    }

    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {
        if (state != State.PROTECTING) {
            other.acceptInteraction(new BombFoeInteractionHandler(), isCellInteraction);
        }
    }


    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        List<DiscreteCoordinates> fieldOfView = new ArrayList<DiscreteCoordinates>();
        fieldOfView.add(super.getFieldOfViewCells().getFirst());
        for (int i = 0; i < 7; ++i) {
            fieldOfView.add((fieldOfView.get(i)).jump(getOrientation().toVector()));
        }
        return fieldOfView;
    }

    private class BombFoeInteractionHandler implements ICoopInteractionVisitor {
        /**
         * Interaction between a BombFoe and a ICoopPlayer. The interaction consists in the BombFoe's state to become Attacking and to target the player.
         * @param player
         * @param isCellInteraction
         */
        @Override
        public void interactWith(ICoopPlayer player, boolean isCellInteraction) {
            state = State.ATTACKING;
            target = player;
        }
    }
}
