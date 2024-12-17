package ch.epfl.cs107.icoop.actor.projectiles;

import ch.epfl.cs107.icoop.actor.Unstoppable;
import ch.epfl.cs107.play.areagame.actor.Interactor;
import ch.epfl.cs107.play.areagame.actor.MovableAreaEntity;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;

import java.util.Collections;
import java.util.List;

public abstract class Projectile extends MovableAreaEntity implements Interactor, Unstoppable {


    private int speed;
    private int MOVE_DURATION;
    private int maxDistance;
    /**
     * Default Projectile constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity. Not null
     * @param position    (Coordinate): Initial position of the entity. Not null
     */
    public Projectile(Area area, Orientation orientation, DiscreteCoordinates position, int MOVE_DURATION, int speed, int maxDistance) {
        super(area, orientation, position);
        this.MOVE_DURATION = MOVE_DURATION;
        this.speed = speed;
        this.maxDistance = maxDistance;
    }

    @Override
    public void update(float deltaTime) {
        if (maxDistance >= 0) {
            move(MOVE_DURATION/speed);
            maxDistance -= 1;
        }
        super.update(deltaTime);
        if (maxDistance == 0) {
            stop();
        }
    }

    protected void stop() {
        getOwnerArea().unregisterActor(this);
    }

    ///Implements Interactable
    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public boolean takeCellSpace() {
        return false;
    }

    @Override
    public boolean isCellInteractable() {
        return false;
    }

    @Override
    public boolean isViewInteractable() {
        return false;
    }

    ///Implements Interactor
    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public boolean wantsCellInteraction() {
        return maxDistance != 0;
    }

    @Override
    public boolean wantsViewInteraction() {
        return false;
    }

}
