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

    //Enum tracking different states of BombFoe
    private enum State {
        IDLE(2),
        ATTACKING(6),
        PROTECTING(1);

        private State(int speed) {
            speedFactor = speed;
        }

        /**
         *
         * @return speed associated to state
         */
        public int getSpeedFactor() {
            return speedFactor;
        }

        private int speedFactor;
    }
    /**
     * Default BombFoe constructor
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
     * @param deltaTime (float) elapsed time since last update, in seconds, non-negative
     */
    @Override
    public void update(float deltaTime) {
        //Checks if the BombFoe is immune, if he is, his state becomes Idle and his inaction time goes to 0.
        if (isImmune()) {
            state = State.IDLE;
            inactionTime = 0;
        }
        //Otherwise, creates a swich that explains what the BombFoe does in each State
        else {
            switch (state) {
                case IDLE -> {
                    idleUpdate(deltaTime);
                }
                case ATTACKING -> {
                    attackingUpdate(deltaTime);
                }
                case PROTECTING -> {
                    protectingupdate(deltaTime);
                }
            }
        }
        if (isDisplacementOccurs()) {
            normalAnimation.update(deltaTime);
        }
        super.update(deltaTime);
    }

    /**
     * @param deltaTime (float) elapsed time since last update, in seconds, non-negative
     */
    private void attackingUpdate(float deltaTime) {
        // if the player is not close to the BombFoe get closer
        if (DiscreteCoordinates.distanceBetween(getCurrentMainCellCoordinates(), ((AreaEntity) target).getCurrentMainCellCoordinates()) > MIN_ATTACKING_DISTANCE) {
            targetedMove();
        } // place a bomb if the player is close enough
        else {
            Explosive explosive = new Explosive(getOwnerArea(), DOWN, super.getFieldOfViewCells().getFirst());
            if (getOwnerArea().canEnterAreaCells(explosive, super.getFieldOfViewCells())) {
                getOwnerArea().registerActor(explosive);
                explosive.activate();
            }
            state = State.PROTECTING;
        }
    }

    /**
     * @param deltaTime (float) elapsed time since last update, in seconds, non-negative
     */
    private void idleUpdate(float deltaTime) {
        double randDouble = RandomGenerator.getInstance().nextDouble();
        //Checks if the inaction time is below 0, indicating the BombFoe can move
        if (inactionTime <= 0) {
            //Randommly orientates the BombFoe if randDouble is below 0.4
            if (randDouble <= 0.4) {
                orientate(orders[RandomGenerator.getInstance().nextInt(4)]);
            }
            //Move according to the state's speed
            move(ANIMATION_DURATION / state.getSpeedFactor());
            inactionTime = RandomGenerator.getInstance().nextInt(MAX_INACTION_TIME);
        } else {
            --inactionTime;
        }
    }

    /**
     * @param deltaTime (float) elapsed time since last update, in seconds, non-negative
     */
    private void protectingupdate(float deltaTime) {
        //Nulifies the target and sets a protectingTime
        if (target != null) {
            target = null;
            protectingTime = MIN_PROTECTING_TIME + RandomGenerator.getInstance().nextInt(MAX_PROTECTING_TIME - MIN_PROTECTING_TIME);
        } else if (protectingTime > 0) {
            protectingTime--;
            protectingAnimation.update(deltaTime);
        }
        //If the protecting time is below zero change the state to Idle
        else {
            state = State.IDLE;
        }
        protectingAnimation.update(deltaTime);
    }

    /**
     * moves according to the target
     */
    private void targetedMove() {
        //Calculates the x, y cooridnates to get to player's position
        Vector v = getPosition().sub(((Entity) target).getPosition());
        float deltaX = v.getX(); float deltaY = v.getY();
        boolean orientationOccured;
        if (deltaX != 0 || deltaY != 0) {
            //Orientates if the deltaX is greater than deltaY
            if (Math.abs(deltaX) > Math.abs(deltaY)) {
                orientationOccured = !orientate(Orientation.fromVector(new Vector(-deltaX, 0)));
            } else {
                //Orientates if the deltaY is greater than deltaX
                orientationOccured = !orientate(Orientation.fromVector(new Vector(0, -deltaY)));
            }
            //If no oreintation occured move
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
        //returns the 8 cells in front of him
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
