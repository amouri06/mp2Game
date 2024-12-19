package ch.epfl.cs107.icoop.actor.collectables;

import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

public class Staff  extends ElementalItem implements Interactable {

    private final static int PROJECTILE_ANIMATION_DURATION =12;
    private final static int SPRITE_ANIMATION_DURATION =32;

    private RPGSprite[] rpgSprite;
    private int currentSpriteIndex;
    private StaffType staffType;

    //Enum tracking specific values for staff types
    public enum StaffType{
        FEU("icoop/staff_fire", Element.FEU),
        EAU("icoop/staff_water", Element.EAU),;

        private Element element;
        private String spriteName;


        private StaffType(String spriteName, Element element){
            this.spriteName=spriteName;
            this.element=element;
        }

        public Element getElement() {
            return element;
        }
        public String getSpriteName(){
            return spriteName;
        }

    }

    /**
     *
     * @param area (Area)
     * @param orientation (Orientation)
     * @param position (DiscreteCoordinates)
     * @param staffType (StaffType)
     */
    public Staff(Area area, Orientation orientation, DiscreteCoordinates position,  StaffType staffType) {
        super(area, orientation, position, staffType.getElement());
        rpgSprite = new RPGSprite[8];
        for (int i =0; i<8; ++i) {
            this.rpgSprite[i] = new RPGSprite(staffType.getSpriteName(), 2, 2, this, new RegionOfInterest(i *
                    32, 0, 32, 32), new Vector(-0.5f, 0));
        }
        currentSpriteIndex = 0;
        this.staffType = staffType;

    }
    ///Implements Interactable
    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    /**
     *
     * @return (staffType) assocated to staff
     */
    public StaffType getStaffType() { return staffType; }

    ///Implements Interactable
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
        return false;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICoopInteractionVisitor)v).interactWith(this, isCellInteraction );
    }

    /**
     * Changes the sprite index at each update mod 8 as to not exceed the Sprite array's size
     * @param deltaTime elapsed time since last update, in seconds, non-negative
     */
    @Override
    public void update(float deltaTime){
        currentSpriteIndex = (currentSpriteIndex + 1) % 8;
    }

    /**
     * Draws the Staff in the game
     * @param canvas target, not null
     */
    @Override
    public void draw(Canvas canvas) {
        rpgSprite[currentSpriteIndex].draw(canvas);
    }
}
