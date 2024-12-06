package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.actor.Interactor;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.Animation;
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
    ;
    private RPGSprite[] rpgSprite;
    private Animation projectileAnimation;
    private int currentSpriteIndex;


    public enum StaffType{
        FEU("icoop/staff_fire","icoop/magicFireProjectile", Element.FEU),
        EAU("icoop/staff_water","icoop/magicWaterProjectile", Element.EAU),;

        private Element element;
        private String spriteName;
        private String projectileAnimationName;

        private StaffType(String spriteName, String projectileSpriteName, Element element){
            this.spriteName=spriteName;
            this.element=element;
            this.projectileAnimationName=projectileSpriteName;
        }

        public Element getElement() {
            return element;
        }
        public String getSpriteName(){
            return spriteName;
        }
        public String getProjectileAnimationName(){
            return projectileAnimationName;
        }
    }


    public Staff(Area area, Orientation orientation, DiscreteCoordinates position,  StaffType staffType) {
        super(area, orientation, position, staffType.getElement());
        rpgSprite = new RPGSprite[8];
        for (int i =0; i<8; ++i) {
            this.rpgSprite[i] = new RPGSprite(staffType.getSpriteName(), 2, 2, this, new RegionOfInterest(i *
                    32, 0, 32, 32), new Vector(-0.5f, 0));
        }
        this.projectileAnimation=new Animation ( staffType.getProjectileAnimationName() , 4, 1, 1, this , 32 , 32 ,
                PROJECTILE_ANIMATION_DURATION /4 , true );
        currentSpriteIndex = 0;

    }

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
        return false;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICoopInteractionVisitor)v).interactWith(this, isCellInteraction );
    }

    @Override
    public void draw(Canvas canvas) {
        rpgSprite[currentSpriteIndex].draw(canvas);
    }

    @Override
    public void update(float deltaTime){
        currentSpriteIndex = (currentSpriteIndex + 1) % 8;
    }
}
