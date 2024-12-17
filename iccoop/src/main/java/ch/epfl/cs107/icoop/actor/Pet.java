package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.KeyBindings;
import ch.epfl.cs107.play.areagame.actor.MovableAreaEntity;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.Entity;
import ch.epfl.cs107.play.engine.actor.OrientedAnimation;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.util.List;

import static ch.epfl.cs107.play.math.Orientation.*;
import static ch.epfl.cs107.play.math.Orientation.LEFT;

public class Pet extends MovableAreaEntity {
    private final static int ANIMATION_DURATION = 24;
    private final ICoopPlayer player;
    private OrientedAnimation sprite;

    public Pet(Area owner, Orientation orientation, DiscreteCoordinates coordinates, ICoopPlayer player, String prefix ) {
        super(owner, orientation, coordinates);
        this.player = player;
        Vector anchor = new Vector(0, 0);
        Orientation[] orders = {DOWN, UP, LEFT, RIGHT};
        sprite = new OrientedAnimation(prefix, ANIMATION_DURATION, this, anchor, orders, 4, 2, 2, 64, 64, true);
    }

    public void update(float deltaTime){
        targetedMove();
        if (isDisplacementOccurs()) {
            sprite.update(deltaTime);
            sprite.update(deltaTime);
            sprite.update(deltaTime);

        } else {
            sprite.reset();
        }
        super.update(deltaTime);
    }
    public void enterArea(Area area, DiscreteCoordinates position) {
        area.registerActor(this);
        setOwnerArea(area);
        setCurrentPosition(position.toVector());
        resetMotion();
    }

    public void draw(Canvas canvas){
        sprite.draw(canvas);
    }

    private void targetedMove() {
        Vector v = getPosition().sub(((Entity) player).getPosition());
        float deltaX = v.getX(); float deltaY = v.getY();
        if (deltaX != 0 || deltaY != 0) {
            boolean orientationOccured;
            if (Math.abs(deltaX) > Math.abs(deltaY)) {
                orientationOccured = !orientate(Orientation.fromVector(new Vector(-deltaX, 0)));
            } else {
                orientationOccured = !orientate(Orientation.fromVector(new Vector(0, -deltaY)));
            }
            if (!orientationOccured) {
                move((int) (ANIMATION_DURATION / 2.25));
            }
        }
    }


    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return List.of();
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

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {

    }
}
