package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.engine.actor.OrientedAnimation;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.math.random.RandomGenerator;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

public class HellSkull extends Foe {

    private final static int MAX_LIFE = 1;
    private final static int ANIMATION_DURATION = 12;
    private final Orientation[] orders = new Orientation[]{Orientation.UP, Orientation.LEFT, Orientation.DOWN, Orientation.RIGHT};
    private float tempsLanceFlamme;
    private OrientedAnimation orientedAnimation;
    private int projectileMaxDistance;

    /**
     * Default HellSkull constructor
     *
     * @param area              (Area): Owner area. Not null
     * @param orientation       (Orientation): Initial orientation of the entity. Not null
     * @param position          (Coordinate): Initial position of the entity. Not null
     */
    public HellSkull(Area area, Orientation orientation, DiscreteCoordinates position, int projectileMaxDistance) {
        super(area, orientation, position, MAX_LIFE, new Vulnerability[]{Vulnerability.PHYSIQUE, Vulnerability.WATER});
        tempsLanceFlamme = 0;
        this.projectileMaxDistance = projectileMaxDistance;
        orientedAnimation = new OrientedAnimation("icoop/flameskull", ANIMATION_DURATION/3, this, new Vector(-0.5f, -0.5f), orders, 3, 2, 2, 32, 32, true);
    }

    @Override
    public void update(float deltaTime) {
        if (tempsLanceFlamme <= 0) {
            tempsLanceFlamme = RandomGenerator.getInstance().nextFloat(.5f, 2.f);
            getOwnerArea().registerActor(new Fire(getOwnerArea(), getOrientation(), getCurrentMainCellCoordinates().jump(getOrientation().toVector()), projectileMaxDistance));
        }
        tempsLanceFlamme -= .05f;
        if (isAlive()) {
            orientedAnimation.update(deltaTime);
        }
        super.update(deltaTime);
    }

    @Override
    public void draw(Canvas canvas) {
        if (this.isAlive()) {
            orientedAnimation.draw(canvas);
        }
        super.draw(canvas);
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
        return true;
    }

    @Override
    public boolean isViewInteractable() {
        return true;
    }

    ///Implements Interactor
    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public boolean wantsCellInteraction() {
        return true;
    }

    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {
        other.acceptInteraction(new HellSkullInteractionHandler(), isCellInteraction);
    }

    private class HellSkullInteractionHandler implements ICoopInteractionVisitor {

        @Override
        public void interactWith(ICoopPlayer player, boolean isCellInteraction) {
            player.decreaseHealth(1);
        }
    }


}
