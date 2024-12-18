package ch.epfl.cs107.icoop.actor.doors;

import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.engine.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

public class Teleporter extends Door {

    private RPGSprite rpgSprite;

    /**
     *
     * @param destination (String)
     * @param signal (Logic)
     * @param arrivalCoordinates (List<DiscreteCoordinates>)
     * @param owner (Area)
     * @param mainCellPosition (DiscreteCoordinates)
     */
    public Teleporter(String destination, Logic signal, List<DiscreteCoordinates> arrivalCoordinates, Area owner, DiscreteCoordinates mainCellPosition) {
        super(destination, signal, arrivalCoordinates, owner, mainCellPosition, Collections.singletonList(mainCellPosition));
        rpgSprite = new RPGSprite("shadow", 1, 1, this, new RegionOfInterest(0, 0, 32, 32));
    }

    /**
     * Draws the door in game if it is activated
     * @param canvas target, not null
     */
    @Override
    public void draw(Canvas canvas) {
        if (getSignal().isOn()) {
            rpgSprite.draw(canvas);
        }
    }
}
