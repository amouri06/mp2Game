package ch.epfl.cs107.icoop.actor.collectables;

import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.Animation;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

public class Heart extends ICoopCellCollectable implements Interactable {

    private Animation animation;
    private final static int ANIMATION_DURATION = 24;

    /**
     *
     * @param area (Area)
     * @param orientation (Orientation)
     * @param position (DiscreteCoordinates)
     */
    public Heart(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);
        animation = new Animation("icoop/heart", 4, 1, 1, this, 16, 16, ANIMATION_DURATION/4, true);
    }

    /**
     * Draws the heart in game
     * @param canvas (Canvas) target, not null
     */
    @Override
    public void draw(Canvas canvas) { animation.draw(canvas); }

    /**
     * Updates the animation
     * @param deltaTime (float) elapsed time since last update, in seconds, non-negative
     */
    @Override
    public void update(float deltaTime) { animation.update(deltaTime); }
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
