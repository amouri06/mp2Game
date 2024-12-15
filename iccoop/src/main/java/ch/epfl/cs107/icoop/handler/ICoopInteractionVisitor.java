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
    default void interactWith(ICoopBehavior.ICoopCell cell, boolean isCellInteraction) {}

    default void interactWith(ICoopPlayer player, boolean isCellInteraction) {}

    default void interactWith(Obstacle obstacle, boolean isCellInteraction) {}

    default void interactWith(Rock rock, boolean isCellInteraction) {}

    default void interactWith(Explosive explosive, boolean isCellInteraction) {}

    default void interactWith(Door door, boolean isCellInteraction) {}

    default void interactWith(ICoopCellCollectable collectable, boolean isCellInteraction) {}

    default void interactWith(ElementalItem elementalItem, boolean isCellInteraction) {}

    default void interactWith(Orb orb, boolean isCellInteraction) {}

    default void interactWith(Heart heart, boolean isCellInteraction) {}

    default void interactWith(ElementalWall elementalWall, boolean isCellInteraction) {}

    default void interactWith(PressurePlate pressurePlate, boolean isCellInteraction) {}

    default void interactWith(Projectile projectile, boolean isCellInteraction) {}

    default void interactWith(Foe foe, boolean isCellInteraction) {}

    default void interactWith(Fire fire, boolean isCellInteraction) {}

    default void interactWith(Staff staff, boolean isCellInteraction) {}

    default void interactWith(HellSkull hellSkull, boolean isCellInteraction) {}

    default void interactWith(ElementalKey elementalKey, boolean isCellInteraction) {}

    default void interactWith(Coin coin, boolean isCellInteraction) {}

}
