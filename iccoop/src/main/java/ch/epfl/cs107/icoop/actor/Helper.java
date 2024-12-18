package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.Entity;
import ch.epfl.cs107.play.engine.actor.RPGSprite;
import ch.epfl.cs107.play.engine.actor.Sprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Canvas;

import java.util.List;

public class Helper extends AreaEntity implements Interactable{

    private final Sprite rpgSprite;
    private Logic[] dialogOrder;
    private String[] dialogs;


    /**
     * Default AreaEntity constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity in the Area. Not null
     * @param position    (DiscreteCoordinate): Initial position of the entity in the Area. Not null
     */
    public Helper(Area area, Orientation orientation, DiscreteCoordinates position, Logic orbWay, Logic arena) {
        super(area, orientation, position);
        rpgSprite = new Sprite("icoop/logMonster", 1f, 1f, this);
        dialogOrder = new Logic[] {orbWay, arena};
        dialogs = new String[] {"gotoOrbway", "goToArena", "goToEnd"};
    }
    public String getDialog(){
        for (int i = 0; i < dialogOrder.length; ++i) {
            if (dialogOrder[i].isOff()) {
                return dialogs[i];
            }
        }
        return dialogs[dialogOrder.length];
    }

    @Override
    public void draw(Canvas canvas) {
        rpgSprite.draw(canvas);
    }

    ///Implements Interactable
    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return List.of(getCurrentMainCellCoordinates());
    }

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
        ((ICoopInteractionVisitor)v).interactWith(this, isCellInteraction );
    }

}
