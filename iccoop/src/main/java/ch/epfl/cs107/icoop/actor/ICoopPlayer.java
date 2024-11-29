package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.KeyBindings;
import ch.epfl.cs107.play.areagame.actor.MovableAreaEntity;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.OrientedAnimation;
import ch.epfl.cs107.play.engine.actor.Sprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Keyboard;

import java.util.Collections;
import java.util.List;

import static ch.epfl.cs107.play.math.Orientation.*;

/**
 * A ICoopPlayer is a player for the ICoop game.
 */
public class ICoopPlayer extends MovableAreaEntity implements ElementalEntity {

    private final static int MOVE_DURATION = 8;
    private final static int ANIMATION_DURATION = 4;
    private final Sprite sprite;
    private final Element element;
    private final String name;
    private final KeyBindings.PlayerKeyBindings keys;

    /**
     * Default ICoopPlayer constructor
     *
     * @param owner (Area) area to which the player belong
     * @param orientation (Orientation) the initial orientation of the player
     * @param coordinates (DiscreteCoordinates) the initial position in the grid
     * @param element    (String): Element of the entity. Not null
     * @param spriteName (String) name of the sprite used as graphical representation
     */
    public ICoopPlayer(Area owner, Orientation orientation, DiscreteCoordinates coordinates, Element element, String spriteName, String name, KeyBindings.PlayerKeyBindings keys) {
        super(owner, orientation, coordinates);
        this.element = element;
        this.name = name;
        this.keys = keys;
        sprite = new Sprite(spriteName, 1.f, 1.f, this);
        resetMotion();
    }

    /**
     * returns the element of the ICoopPlayer
     */
    public Element element() {
        return element;
    }

    /**
     * @param deltaTime elapsed time since last update, in seconds, non-negative
     */
    @Override
    public void update(float deltaTime) {
        Keyboard keyboard = getOwnerArea().getKeyboard();
        moveIfPressed(LEFT, keyboard.get(Keyboard.LEFT));
        moveIfPressed(UP, keyboard.get(keys.up()));
        moveIfPressed(RIGHT, keyboard.get(Keyboard.RIGHT));
        moveIfPressed(Orientation.DOWN, keyboard.get(Keyboard.DOWN));
        super.update(deltaTime);
    }

    /**
     * @param canvas target, not null
     */
    @Override
    public void draw(ch.epfl.cs107.play.window.Canvas canvas) {

        final Vector anchor = new Vector(0, 0);
        final Orientation[] orders = {DOWN, RIGHT, UP, LEFT};
        new OrientedAnimation(name, ANIMATION_DURATION, this, anchor, orders, 4, 1, 2, 16, 32, true);
        sprite.draw(canvas);
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
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {

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


}
