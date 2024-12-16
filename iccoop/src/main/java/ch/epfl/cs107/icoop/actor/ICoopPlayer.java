package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.ICoop;
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
import ch.epfl.cs107.play.areagame.handler.Inventory;
import ch.epfl.cs107.play.engine.actor.Actor;
import ch.epfl.cs107.play.engine.actor.Animation;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static ch.epfl.cs107.play.math.Orientation.*;

/**
 * A ICoopPlayer is a player for the ICoop game.
 */
public class ICoopPlayer extends MovableAreaEntity implements ElementalEntity, Interactor, Interactable {

    private final static int MOVE_DURATION = 3;
    private final static int ANIMATION_DURATION = 4;
    private final static int STAFF_ANIMATION_DURATION = 2;
    private final static int SWORD_ANIMATION_DURATION = 2;
    private final static int MAX_LIFE = 5;
    private final static int PROJECTILE_MAX_DISTANCE= 20;

    private final Orientation [] orders = { DOWN , UP , RIGHT , LEFT };
    private final Element element;
    private final String prefix;
    public final KeyBindings.PlayerKeyBindings keys;

    private OrientedAnimation sprite;
    private Door isLeavingAreaDoor;
    private Health health;
    private int immuneTimer;
    private boolean isElementImmune;
    private ICoopInventory inventory;
    private final ICoopItem[] items;
    private ICoopItem currentItem;
    private int currentItemIndex;
    private int itemAnimationTimer;
    private OrientedAnimation useItemAnimation;

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
        items = ICoopItem.values();
        currentItem = ICoopItem.Sword;
        currentItemIndex = 0;
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

    public void switchItem() {
        boolean switched = false;
        for (int i = 1; i < items.length; ++i) {
            if (!switched && inventory.contains(items[(currentItemIndex + i) % items.length])) {
                currentItemIndex = (currentItemIndex + i) % items.length;
                currentItem = items[currentItemIndex];
                switched = true;
            }
        }
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

        if (keyboard.get(keys.switchItem()).isPressed()) {
            switchItem();
        }

        if (keyboard.get(keys.useItem()).isPressed()) {
            switch (currentItem) {
                case Explosive -> {
                    Explosive explosive = new Explosive(getOwnerArea(), DOWN, getFieldOfViewCells().getFirst());
                    if (getOwnerArea().canEnterAreaCells(explosive, getFieldOfViewCells())) {
                        getOwnerArea().registerActor(explosive);
                        inventory.removePocketItem(ICoopItem.Explosive, 1);
                        if (!inventory.contains(ICoopItem.Explosive)) {
                            switchItem();
                        }
                    }
                }
                case FireStaff -> {
                    resetItemAnimationTimer();
                    Boule boule = new Boule(getOwnerArea(), getOrientation(), getFieldOfViewCells().getFirst(), PROJECTILE_MAX_DISTANCE, Boule.AttackType.FEU);
                    getOwnerArea().registerActor(boule);
                    final Vector anchor = new Vector ( -.5f , -.20f);
                    useItemAnimation =  new OrientedAnimation ( "icoop/player.staff_fire" , STAFF_ANIMATION_DURATION , this ,  anchor , orders , 4, 2, 2, 32 , 32);
                }
                case WaterStaff -> {
                    resetItemAnimationTimer();
                    Boule boule = new Boule(getOwnerArea(), getOrientation(), getFieldOfViewCells().getFirst(), PROJECTILE_MAX_DISTANCE, Boule.AttackType.EAU);
                    getOwnerArea().registerActor(boule);
                    final Vector anchor = new Vector(-.5f, -.20f);
                    useItemAnimation = new OrientedAnimation("icoop/player2.staff_water", STAFF_ANIMATION_DURATION, this, anchor, orders, 4, 2, 2, 32, 32);
                }
                case Sword -> {
                    resetItemAnimationTimer();
                    final Vector anchor = new Vector ( -.5f , 0) ;
                    useItemAnimation =  new OrientedAnimation ( prefix + ".sword",  SWORD_ANIMATION_DURATION , this ,  anchor , orders , 4, 2, 2, 32 , 32);
                    }
                }
            }

        if (isDisplacementOccurs()) {
            sprite.update(deltaTime);
        } else {
            sprite.reset();
        }

        if (immuneTimer > 0) {
            immuneTimer--;
        }
        if (itemAnimationTimer > 0) {
            itemAnimationTimer--;
            useItemAnimation.update(deltaTime);
        }

        super.update(deltaTime);
    }

    public void resetItemAnimationTimer(){
        itemAnimationTimer=8;
    }

    public boolean inventoryContains(ICoopItem iCoopItem) {
        return inventory.contains(iCoopItem);
    }


    /**
     * @param canvas target, not null
     */
    @Override
    public void draw(Canvas canvas) {
        if (immuneTimer % 8 == 0) {
            if (itemAnimationTimer>0){
                useItemAnimation.draw(canvas);
            }
            else{sprite.draw(canvas);}
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
        if (isLeavingAreaDoor != null) {
            return new Door(isLeavingAreaDoor);
        }
        return null;
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

        @Override
        public void interactWith(Staff staff, boolean isCellInteraction) {
            switch(staff.getStaffType()) {
                case FEU -> inventory.addPocketItem(ICoopItem.FireStaff, 1);
                case EAU -> inventory.addPocketItem(ICoopItem.WaterStaff, 1);
            }
            interactWith((ElementalItem) staff, isCellInteraction);
        }
        @Override
        public void interactWith(Foe foe, boolean isCellInteraction){
            Keyboard keyboard = getOwnerArea().getKeyboard();
            if (currentItem==ICoopItem.Sword && keyboard.get(keys.useItem()).isPressed()){
                foe.decreaseHealth(1, Vulnerability.PHYSIQUE);
            }
        }

        @Override
        public void interactWith(ElementalKey elementalKey, boolean isCellInteraction) {
            switch(elementalKey.element()) {
                case FEU -> inventory.addPocketItem(ICoopItem.FireKey, 1);
                case EAU -> inventory.addPocketItem(ICoopItem.WaterKey, 1);
            }
            interactWith((ElementalItem) elementalKey, isCellInteraction);
        }

        @Override
        public void interactWith(Coin coin, boolean isCellInteraction) {
            coin.collect();
        }
    }
}
