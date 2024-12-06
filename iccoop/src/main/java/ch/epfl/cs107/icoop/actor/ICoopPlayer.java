package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.KeyBindings;
import ch.epfl.cs107.icoop.area.ICoopArea;
import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.icoop.handler.ICoopInventory;
import ch.epfl.cs107.icoop.handler.ICoopItem;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.actor.Interactor;
import ch.epfl.cs107.play.areagame.actor.MovableAreaEntity;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.OrientedAnimation;
import ch.epfl.cs107.play.engine.actor.Sprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.math.Transform;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static ch.epfl.cs107.play.math.Orientation.*;

/**
 * A ICoopPlayer is a player for the ICoop game.
 */
public class ICoopPlayer extends MovableAreaEntity implements ElementalEntity, Interactor {

    private final static int MOVE_DURATION = 8;
    private final static int ANIMATION_DURATION = 4;
    private final static int MAX_LIFE = 5;
    private final Element element;
    private final String prefix;
    private final KeyBindings.PlayerKeyBindings keys;
    private OrientedAnimation sprite;
    private Door isLeavingAreaDoor;
    private Health health;
    private int immuneTimer;
    private boolean isElementImmune;
    private ICoopInventory inventory;
    private ICoopItem currentItem;



    /**
     * Default ICoopPlayer constructor
     *
     * @param owner (Area) area to which the player belong
     * @param orientation (Orientation) the initial orientation of the player
     * @param coordinates (DiscreteCoordinates) the initial position in the grid
     * @param element    (String): Element of the entity. Not null
     * @param prefix   (String) name of the player
     */
    public ICoopPlayer(Area owner, Orientation orientation, DiscreteCoordinates coordinates, Element element, String prefix, KeyBindings.PlayerKeyBindings keys) {
        super(owner, orientation, coordinates);
        this.element = element;
        this.prefix = prefix;
        this.keys = keys;
        isLeavingAreaDoor = null;
        Vector anchor = new Vector(0, 0);
        Orientation[] orders = {DOWN, RIGHT, UP, LEFT};
        sprite = new OrientedAnimation(prefix, ANIMATION_DURATION, this, anchor, orders, 4, 1, 2, 16, 32, true);
        health = new Health(this, Transform.I.translated(0, 1.75f), MAX_LIFE, true);
        inventory = new ICoopInventory();
        inventory.addPocketItem(ICoopItem.Sword, 1);
        inventory.addPocketItem(ICoopItem.Explosive, 5);
        currentItem = ICoopItem.Sword;
        resetMotion();

    }

    /**
     * returns the element of the ICoopPlayer
     */
    public Element element() {
        return element;
    }

    public void restoreHealth() {
        health.increase(MAX_LIFE);
    }

    public void decreaseHealth(int amount) {
        if (!isImmune()) {
            health.decrease(amount);
            immuneTimer = 24;
        }
    }

    private boolean isImmune() {
        return (immuneTimer > 0);
    }

    public boolean isAlive() {
        return health.isOn();
    }

    public boolean isElementImmune() { return isElementImmune; }

    public String getCurrentItemName() {
        if (currentItem == null) {
            return "";
        }
        return currentItem.getName();
    }

    private void switchItem() {

    }



    /**
     * @param deltaTime elapsed time since last update, in seconds, non-negative
     */
    @Override
    public void update(float deltaTime) {
        Keyboard keyboard = getOwnerArea().getKeyboard();
        moveIfPressed(LEFT, keyboard.get(keys.left()));
        moveIfPressed(UP, keyboard.get(keys.up()));
        moveIfPressed(RIGHT, keyboard.get(keys.right()));
        moveIfPressed(DOWN, keyboard.get(keys.down()));

        if (isDisplacementOccurs()) {
            sprite.update(deltaTime);
        } else {
            sprite.reset();
        }

        if (immuneTimer > 0) {
            immuneTimer--;
        }

        super.update(deltaTime);
    }

    /**
     * @param canvas target, not null
     */
    @Override
    public void draw(Canvas canvas) {
        if (immuneTimer % 8 == 0) {
            sprite.draw(canvas);
        }
        health.draw(canvas);
    }


    ///Implements Interactable

    @Override
    public boolean takeCellSpace() {
        return true;
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

    /// Implements Interactor

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
        return true;
    }

    @Override
    public boolean wantsViewInteraction() {
        Keyboard keyboard = getOwnerArea().getKeyboard();
        if (keyboard.get(keys.useItem()).isDown()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {
        other.acceptInteraction(new ICoopPlayerInteractionHandler(), isCellInteraction);
    }


    /**
     * Orientate and Move this player in the given orientation if the given button is down
     *
     * @param orientation (Orientation): given orientation, not null
     * @param b           (Button): button corresponding to the given orientation, not null
     */
    private void moveIfPressed(Orientation orientation, Button b) {
        if (b.isDown()) {
            if (!isDisplacementOccurs()) {
                orientate(orientation);
                move(MOVE_DURATION);
            }
        }
    }

    /**
     * Leave an area by unregister this player
     */
    public void leaveArea() {
        getOwnerArea().unregisterActor(this);
    }

    /**
     * makes the player entering a given area
     * @param area     (Area):  the area to be entered, not null
     * @param position (DiscreteCoordinates): initial position in the entered area, not null
     */
    public void enterArea(Area area, DiscreteCoordinates position) {
        area.registerActor(this);
        setOwnerArea(area);
        setCurrentPosition(position.toVector());
        resetMotion();
    }

    public Door getIsLeavingAreaDoor() {
        return isLeavingAreaDoor;
    }

    public void nullifyIsLeavingAreaDoor() {
        isLeavingAreaDoor = null;
    }

    private class ICoopPlayerInteractionHandler implements ICoopInteractionVisitor {
        @Override
        public void interactWith(Door door, boolean isCellInteraction) {
            if (door.getSignal().isOn()) {
                isLeavingAreaDoor = door;
            }
        }

        @Override
        public void interactWith(Explosive explosive, boolean isCellInteraction) {
            if (isCellInteraction) {
                explosive.collect();
                inventory.addPocketItem(ICoopItem.Explosive, 1);
            }
            else {
                explosive.activate();
            }
        }

        @Override
        public void interactWith(ElementalItem elementalItem, boolean isCellInteraction) {
            if (isCellInteraction && element == elementalItem.element()) {
                elementalItem.collect();
            }
        }

        @Override
        public void interactWith(Orb orb, boolean isCellInteraction) {
            if (isCellInteraction) {
                interactWith((ElementalItem) orb, true);
                isElementImmune = true;
                ((ICoopArea) getOwnerArea()).publish(orb.getMessage());
            }
        }

        @Override
        public void interactWith(Heart heart, boolean isCellInteraction) {
            heart.collect();
            health.increase(1);
        }

        @Override
        public void interactWith(PressurePlate pressurePlate, boolean isCellInteraction){
            if (pressurePlate.timerIsZero()) {
                pressurePlate.playerIsOn();
            }
            pressurePlate.playerIsOn();
        }
    }


}
