package ch.epfl.cs107.icoop.actor.miscellaneous;

import ch.epfl.cs107.play.engine.actor.Actor;
import ch.epfl.cs107.play.math.Transform;
import ch.epfl.cs107.play.math.Vector;

public class CenterOfMass implements Actor {

    private final Actor[] actors;

    /**
     * CenterOfMass constructor
     * @param actor (Actor): first actor associated to the center of Masse
     * @param restOfActors (List<Actor>) : rest of the actors
     */
    public CenterOfMass(Actor actor, Actor... restOfActors) {
        this.actors = new Actor[restOfActors.length + 1];
        actors[0] = actor;
        System.arraycopy(restOfActors, 0, this.actors, 1, restOfActors.length);
    }

    /**
     *
     * @return (Position) associated with CenterOfMass
     */
    @Override
    public Vector getPosition() {
        Vector position = Vector.ZERO;
        for (Actor actor : actors) {
            position = position.add(actor.getPosition());
        }
        return position.mul(1f / actors.length);
    }

    /** @return (Transform): affine transform, not null */
    @Override
    public Transform getTransform() {
        return Transform.I.translated(getPosition());
    }

    /** @return (Vector): linear velocity, not null */
    @Override
    public Vector getVelocity() {
        Vector velocity = Vector.ZERO;
        for (Actor actor : actors) {
            velocity = velocity.add(actor.getVelocity());
        }
        return velocity.mul(1f / actors.length);
    }

}
