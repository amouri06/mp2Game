package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.KeyBindings;
import ch.epfl.cs107.icoop.actor.collectables.*;
import ch.epfl.cs107.icoop.actor.doors.Door;
import ch.epfl.cs107.icoop.actor.foes.Foe;
import ch.epfl.cs107.icoop.actor.miscellaneous.Health;
import ch.epfl.cs107.icoop.actor.miscellaneous.PressurePlate;
import ch.epfl.cs107.icoop.actor.projectiles.Boule;
import ch.epfl.cs107.icoop.area.ICoopArea;
import ch.epfl.cs107.icoop.audio.Sound;
import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.icoop.handler.ICoopInventory;
import ch.epfl.cs107.icoop.handler.ICoopItem;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.actor.Interactor;
import ch.epfl.cs107.play.areagame.actor.MovableAreaEntity;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.OrientedAnimation;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.math.Transform;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

import java.util.Collections;
import java.util.List;

import static ch.epfl.cs107.play.math.Orientation.*;

/**
 * A ICoopPlayer is a player for the ICoop game.
 */
public class ICoopPlayer extends MovableAreaEntity implements ElementalEntity, Interactor, Interactable {
    private final Sound sound = new Sound();

    private final static int MOVE_DURATION = 8;
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
    private int displacementTimer;
    private int coinCounter;

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

    /**
     * Restores the health of the player to the initial health
     */
    public void restoreHealth() {
        health.increase(MAX_LIFE);
    }

    /**
     * Decreases the health of the player if he is not immune and reinitialises the immuneTimer to 24.
     * @param amount (int)
     */
    public void decreaseHealth(int amount) {
        if (!isImmune()) {
            health.decrease(amount);
            immuneTimer = 24;
        }
    }

    /**
     * Returns true if the player is immune
     * @return
     */
    private boolean isImmune() {
        return (immuneTimer > 0);
    }

    /**
     * Returns true if the player is alive (Checks that by seeing id the health is active (health!=0))
     * @return
     */
    public boolean isAlive() {
        return health.isOn();
    }

    /**
     * Checks if the layer is element immune
     * @return
     */
    public boolean isElementImmune() { return isElementImmune; }

    /**
     * Returns the name of the current item
     * @return
     */
    public String getCurrentItemName() {
        if (currentItem == null) {
            return "";
        }
        return currentItem.getName();
    }

    /**
     * Switches the currentItem
     */
    public void switchItem() {
        boolean switched = false;
        for (int i = 1; i < items.length; ++i) {
            //Checks if the item was not switched (to not keep searching if the item was switched) and if the players inventory contains the next item in the ICoopItems enum
            if (!switched && inventory.contains(items[(currentItemIndex + i) % items.length])) {
                //If yes, the index goes + i % items.lenght (To not exceed the number of possible items)
                //And switches the CurrentItem to the item in the currentItem index, switched become true
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
        //Player movements:
        Keyboard keyboard = getOwnerArea().getKeyboard();
        moveIfPressed(LEFT, keyboard.get(keys.left()));
        moveIfPressed(UP, keyboard.get(keys.up()));
        moveIfPressed(RIGHT, keyboard.get(keys.right()));
        moveIfPressed(DOWN, keyboard.get(keys.down()));

        //Swtiches the item if the switchItem key is pressed
        if (keyboard.get(keys.switchItem()).isPressed()) {
            switchItem();
        }
        if (coinCounter >= 3) {
            coinCounter -= 3;
            inventory.addPocketItem(ICoopItem.Explosive, 1);
        }

        //Checks if the useItem key is pressed
        if (keyboard.get(keys.useItem()).isPressed()) {
            //Gives differents cases depending on what the type of item is
            switch (currentItem) {
                //Individual methods will be explained below in their respective locations
                case Explosive -> {
                    explosiveUse();
                }
                case FireStaff -> {
                    staffAttack("icoop/player.staff_fire", Boule.AttackType.FEU);
                }
                case WaterStaff -> {
                    staffAttack("icoop/player2.staff_water", Boule.AttackType.EAU);
                }
                case Sword -> {
                    swordUse();
                }
            }
        }
        //updates the player sprite if displacement occurs
        if (isDisplacementOccurs()) {
            sprite.update(deltaTime);
        } else {
            sprite.reset();
        }
        //Reduces the immunity Timer if it is greater than 0
        if (immuneTimer > 0) {
            immuneTimer--;
        }
        //Reduces the displacement timer is if it greater than 0
        if (displacementTimer > 0) {
            displacementTimer--;
        }
        //Reduces the itemAnimationTimer if it is greater than 0, and updates the item's respective animations
        if (itemAnimationTimer > 0) {
            itemAnimationTimer--;
            useItemAnimation.update(deltaTime);
        }
        super.update(deltaTime);
    }

    /**
     * Simulates the use of a staff
     * @param useAnimationString (String): animation's string
     * @param type (Boule.AttackType) : elemental type associated to the staff
     */
    private void staffAttack(String useAnimationString, Boule.AttackType type) {
        resetItemAnimationTimer();
        //Creates a new Boule at on the first cell in the player's field of view
        Boule boule = new Boule(getOwnerArea(), getOrientation(), getFieldOfViewCells().getFirst(), PROJECTILE_MAX_DISTANCE, type);
        //Registers it
        getOwnerArea().registerActor(boule);
        final Vector anchor = new Vector ( -.5f , -.20f);
        //Sets the useItemAnimation to the staff's attack animation
        useItemAnimation =  new OrientedAnimation (useAnimationString, STAFF_ANIMATION_DURATION , this, anchor , orders , 4, 2, 2, 32 , 32);
    }

    /**
     * simulates use of a sword
     */
    private void swordUse() {
        resetItemAnimationTimer();
        final Vector anchor = new Vector(-.5f, 0);
        //Sets the useItemAnimation to the sword's attack animation
        useItemAnimation = new OrientedAnimation(prefix + ".sword", SWORD_ANIMATION_DURATION, this, anchor, orders, 4, 2, 2, 32, 32);
    }

    /**
     * simulates use of an explosive
     */
    private void explosiveUse() {
        //Creates a new explosive
        Explosive explosive = new Explosive(getOwnerArea(), DOWN, getFieldOfViewCells().getFirst());
        //Checks if an explosive can be placed
        if (getOwnerArea().canEnterAreaCells(explosive, getFieldOfViewCells())) {
            //Places the explosive
            getOwnerArea().registerActor(explosive);
            //Removes one explosive from the player's inventory
            inventory.removePocketItem(ICoopItem.Explosive, 1);
            //Checks if the player still has any more explosives, if not, switches the item
            if (!inventory.contains(ICoopItem.Explosive)) {
                switchItem();
            }
        }
    }

    //Resets the item animation timer
    public void resetItemAnimationTimer(){
        itemAnimationTimer=8;
    }

    //Checks if a player's inventory has a certain item
    public boolean inventoryContains(ICoopItem iCoopItem) {
        return inventory.contains(iCoopItem);
    }


    /**
     *
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

    /**
     * Plays one of the imported sound effects by setting the correct file and playing it once
     * @param i (int)
     */
    public void playSoundEffect(int i){
        sound.setFile(i);
        sound.play();
    }

    public void nullifyIsLeavingAreaDoor() {
        isLeavingAreaDoor = null;
    }

    private class ICoopPlayerInteractionHandler implements ICoopInteractionVisitor {
        @Override
        public void interactWith(Door door, boolean isCellInteraction) {
            //Checks if the door is on and the arrival coordinates are not null and sets the isLeavingAreaDoor to the door the player wants to interact with
            if (door.getSignal().isOn() && door.getArrivalCoordinates() != null) {
                isLeavingAreaDoor = door;
            }
            //Checks if the player has moved, if the dialog is not null, and the displacement timer is 0
            if (door.getDialog() != null && isDisplacementOccurs() && displacementTimer == 0) {
                //If yes, it publishes the correct dialog
                ((ICoopArea) getOwnerArea()).publish(door.getDialog());
                //And sets the diasplacement timer back to it's value so as to not instantly republish the dialog
                displacementTimer = MOVE_DURATION * 4;
            }
        }

        @Override
        public void interactWith(Explosive explosive, boolean isCellInteraction) {
            //if the player walks on the explosive (cell interaction), he picks it up
            if (isCellInteraction) {
                explosive.collect();
                inventory.addPocketItem(ICoopItem.Explosive, 1);
            }
            //otherwise the player can activate it with the use key
            else {
                explosive.activate();
            }
        }

        @Override
        public void interactWith(ElementalItem elementalItem, boolean isCellInteraction) {
            //Checks if the if the player walks on the item and if the player and the ElementalItem have the same element
            if (isCellInteraction && element == elementalItem.element()) {
                //If they do, collects the item
                elementalItem.collect();
            }
        }

        @Override
        public void interactWith(Orb orb, boolean isCellInteraction) {
            //Checks if the player walks on the orb
            if (isCellInteraction) {
                //Calls the interactWith from l439 since orb is an ElementalItem
                interactWith((ElementalItem) orb, true);
                //Sets the player to elementImmune for the rest of the game
                isElementImmune = true;
                //Publishes the associated dialog
                ((ICoopArea) getOwnerArea()).publish(orb.getMessage());
                //Plays the 13th sound effect
                playSoundEffect(13);
            }
        }

        @Override
        public void interactWith(Heart heart, boolean isCellInteraction) {
            //Collects the heart
            heart.collect();
            //Increases the player's health
            health.increase(1);
        }

        @Override
        public void interactWith(PressurePlate pressurePlate, boolean isCellInteraction){
            //Checks if the pressure plate timer is zero, effectively meaning no one was on it.
            if (pressurePlate.timerIsZero()) {
                //If yes, adds 1
                pressurePlate.playerIsOn();
            }
            //Then keeps adding one while the player is on the pressure plate due to the nature of the interactWith method
            pressurePlate.playerIsOn();
            //Meaning the timer of the pressure plate goes like this when a player steps on it:
            //+1 (l472) one time and +1 (l477) every time the interactWith is called, but the update of pressurePlate is also called that does -1
            //So effectively the timer is kept at one or two because of the +1,+1,-1,+1,-1,+1,-1 and as soon as the player steps off, the timer does -1 (update method) and it's back to zero.
        }

        @Override
        public void interactWith(Staff staff, boolean isCellInteraction) {
            //Swith depending on the type of the staff
            switch(staff.getStaffType()) {
                //Adds a staff in the inventory depending on the type of the staff
                case FEU -> inventory.addPocketItem(ICoopItem.FireStaff, 1);
                case EAU -> inventory.addPocketItem(ICoopItem.WaterStaff, 1);
            }
            //calls the interactWith l438 since the staff is an elementalItem
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
            playSoundEffect(4);
            coin.collect();
            coinCounter++;
        }

        @Override
        public void interactWith(Helper helper, boolean isCellInteraction){
            System.out.println(helper.getDialog());
            ((ICoopArea)getOwnerArea()).publish(helper.getDialog());
        }

        public void interactWith(Safe safe, boolean isCellInteraction){
            boolean safeOpened = false;
            if (safe.isOrbWayComplete() && !safeOpened){
                inventory.addPocketItem(ICoopItem.Explosive, 3);
                safeOpened=true;
            }else if (!safe.isOrbWayComplete() && !safeOpened){
                ((ICoopArea)getOwnerArea()).publish("goToOrbway2"); ///ADD STRING
            }else if (safe.isOrbWayComplete() && safeOpened){
                ((ICoopArea)getOwnerArea()).publish("safeOpened"); ///ADD STRING
            }
        }
    }
}
