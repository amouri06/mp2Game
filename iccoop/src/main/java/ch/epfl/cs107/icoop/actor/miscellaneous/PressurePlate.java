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

    public PressurePlate(Area owner, DiscreteCoordinates mainCellPosition){
        super(owner, Orientation.DOWN, mainCellPosition);
        rpgSprite = new RPGSprite("GroundPlateOff", 1.f, 1.f, this);
        timer = 0;
    }

    @Override
    public void draw(Canvas canvas) {
        rpgSprite.draw(canvas);
    }

    @Override
    public void update(float deltaTime) {
        timer = Math.max(0, timer - 1);
    }

    public void playerIsOn() {
        timer += 1;
    }

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
    @Override
    public boolean isOn() {
        return timerIsZero();
    }

    @Override
    public boolean isOff() {
        return !timerIsZero();
    }

}
