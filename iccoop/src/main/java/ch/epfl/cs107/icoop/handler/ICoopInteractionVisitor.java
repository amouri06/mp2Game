package ch.epfl.cs107.icoop.handler;

import ch.epfl.cs107.icoop.ICoopBehavior;
import ch.epfl.cs107.icoop.actor.*;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;

/**
 * InteractionVisitor for the ICoop entities
 */

public interface ICoopInteractionVisitor extends AreaInteractionVisitor {
    /// Add Interaction method with all non Abstract Interactable
    default void interactWith(ICoopBehavior.ICoopCell cell) {}

    default void interactWith(ICoopPlayer player) {}

    default void interactWith(Obstacle obstacle) {}

    default void interactWith(Rock rock) {}

    default void interactWith(Explosive explosive) {}

    default void interactWith(Door door) {}

}
