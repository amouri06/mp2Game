package ch.epfl.cs107.icoop.actor.miscellaneous;

import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Canvas;

import java.util.List;

public class PressurePlate extends AreaEntity implements Interactable, Logic {

    private RPGSprite rpgSprite;
    private int timer;

    /**
     *
     * @param owner (Owner)
     * @param mainCellPosition (DiscreteCoordinates)
     */
    public PressurePlate(Area owner, DiscreteCoordinates mainCellPosition){
        super(owner, Orientation.DOWN, mainCellPosition);
        rpgSprite = new RPGSprite("GroundPlateOff", 1.f, 1.f, this);
        timer = 0;
    }

    /**
     * Draws the PressurePlate in the game
     * @param canvas target, not null
     */
    @Override
    public void draw(Canvas canvas) {
        rpgSprite.draw(canvas);
    }

    @Override
    public void update(float deltaTime) {
        //At each update, reduces the timer by one until it gets to 0.
        timer = Math.max(0, timer - 1);
    }

    /**
     * Adds one to the timer
     */
    public void playerIsOn() {
        timer += 1;
    }

    /**
     * Returns true if the timer is equal to 0
     * @return
     */
    public boolean timerIsZero() {
        return timer == 0;
    }

    ///Implements Interactable
    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return List.of(getCurrentMainCellCoordinates());
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
        ((ICoopInteractionVisitor)v).interactWith(this, isCellInteraction );
    }

    ///Implements Logic
    /**
     * Returns true if the timer is 0
     * @return
     */
    @Override
    public boolean isOn() {
        return timerIsZero();
    }

    /**
     * Returns true is the timer is not 0
     * @return
     */
    @Override
    public boolean isOff() {
        return !timerIsZero();
    }

}
