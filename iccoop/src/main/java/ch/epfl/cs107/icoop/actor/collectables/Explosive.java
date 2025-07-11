package ch.epfl.cs107.icoop.actor.collectables;

import ch.epfl.cs107.icoop.actor.ElementalWall;
import ch.epfl.cs107.icoop.actor.ICoopPlayer;
import ch.epfl.cs107.icoop.actor.Rock;
import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.actor.Interactor;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.Animation;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

public class Explosive extends ICoopCellCollectable implements Interactor, Interactable {

    private static final int ANIMATION_DURATION = 24;
    private boolean activated;
    private int timer;
    private Animation animation;

    /**
     * Default Explosive constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity in the Area. Not null
     * @param position    (DiscreteCoordinate): Initial position of the entity in the Area. Not null
     */
    public Explosive(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);
        timer = 4 * 24;
        activated = false;
        animation = new Animation ("icoop/explosive", 2, 1, 1, this , 16 , 16 , ANIMATION_DURATION /2 , true );
    }

    /**
     * @param deltaTime (float) elapsed time since last update, in seconds, non-negative
     */
    @Override
    public void update(float deltaTime) {
        //Checks if the explosive is activated, and if the timer is not 0, in that case, reduces the timer and updates the animation
        if (activated && timer != 0) {
            timer--;
            animation.update(deltaTime);
        }
        //When the timer gets to 24, the animation changes from explosive to explosion. (The animation actually explodes)
        if (timer == 24) {
            animation = new Animation ("icoop/explosion", 7, 1, 1, this , 32 , 32 , ANIMATION_DURATION /7 , false );
        }
        //Updates were too slow so we update the animation twice in a single update (for visual reasons)
        if (timer < 24) {
            animation.update(deltaTime);
        }
        //Removes the exlposive from the area
        if (timer == 0) {
            getOwnerArea().unregisterActor(this);
        }
    }

    /**
     * Draws the explosive in the game
     * @param canvas target, not null
     */
    @Override
    public void draw(Canvas canvas) {
        animation.draw(canvas);
    }

    /**
     * Activates the bomb's timer
     */
    public void activate() {
        activated = true;
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
        return !activated;
    }

    @Override
    public boolean isViewInteractable() {
        return true;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICoopInteractionVisitor)v).interactWith(this, isCellInteraction );
    }

    ///Implements Interactor
    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        return getCurrentMainCellCoordinates().getNeighbours();
    }

    @Override
    public boolean wantsCellInteraction() {
        return (timer == 23 );
    }

    @Override
    public boolean wantsViewInteraction() {
        return (timer == 23);
    }

    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {
        other.acceptInteraction(new ExplosiveInteractionHandler(), isCellInteraction);
    }
    /**
     * Sets all the interaction between the projectile and different elements of the game
     */
    private class ExplosiveInteractionHandler implements ICoopInteractionVisitor {
        //Unregisters the rock from the area
        @Override
        public void interactWith(Rock rock, boolean isCellInteraction) {
            getOwnerArea().unregisterActor(rock);
        }
        //Decreases the players health
        @Override
        public void interactWith(ICoopPlayer player, boolean isCellInteraction) {
            player.decreaseHealth(2);
        }
        //Unregisters the wall from the area
        @Override
        public void interactWith(ElementalWall elementalWall, boolean isCellInteraction) { getOwnerArea().unregisterActor(elementalWall); }
        //Activates the other explosives
        @Override
        public void interactWith(Explosive explosive, boolean isCellInteraction) { explosive.activate(); }


    }


}
