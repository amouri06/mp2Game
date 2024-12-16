package ch.epfl.cs107.icoop.area;

import ch.epfl.cs107.icoop.ICoopBehavior;
import ch.epfl.cs107.icoop.actor.Coin;
import ch.epfl.cs107.icoop.actor.Obstacle;
import ch.epfl.cs107.icoop.actor.Rock;
import ch.epfl.cs107.icoop.handler.DialogHandler;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.engine.actor.Dialog;
import ch.epfl.cs107.play.engine.actor.Foreground;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.io.ResourcePath;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.random.RandomGenerator;
import ch.epfl.cs107.play.window.Image;
import ch.epfl.cs107.play.window.Window;

public abstract class ICoopArea extends Area {

    public final static float DEFAULT_SCALE_FACTOR = 3.f;
    private float cameraScaleFactor = DEFAULT_SCALE_FACTOR;
    protected DialogHandler dialogHandler;

    protected ICoopArea(DialogHandler dialogHandler) {
        this.dialogHandler = dialogHandler;
    }

    public void dialogCompleted() {
        dialogHandler.publish(null);
    }

    public void publish(String path) {
        dialogHandler.publish(new Dialog(path));
    }

    /**
     * @return the red player's spawn position in the area
     */
    public abstract DiscreteCoordinates getRedPlayerSpawnPosition();

    /**
     * @return the blue player's spawn position in the area
     */
    public abstract DiscreteCoordinates getBluePlayerSpawnPosition();

    /**
     * Centers view to the whole window
     * @return true
     */
    @Override
    public boolean isViewCentered() { return true; }

    /**
     * Getter for ICoop's camera scale factor
     * @return Scale factor in both the x-direction and the y-direction
     */
    @Override
    public final float getCameraScaleFactor() {
        return cameraScaleFactor;
    }

    /**
     * Setter for ICoop's camera scale factor
     * @param cameraScaleFactor (float): new camera scale factor
     */
    public final void setCameraScaleFactor(float cameraScaleFactor) {
        this.cameraScaleFactor = cameraScaleFactor;
    }

    /**
     * Area specific callback to initialise the instance
     */
    protected abstract void createArea();

    /**
     * Callback to initialise the instance of the area
     * @param window (Window): display context. Not null
     * @param fileSystem (FileSystem): given file system. Not null
     * @return true if the area is instantiated correctly, false otherwise
     */
    @Override
    public boolean begin(Window window, FileSystem fileSystem) {
        if (super.begin(window, fileSystem)) {
            setBehavior(new ICoopBehavior(window, getTitle()));
            createArea();
            mapInitialize(window.getImage(ResourcePath.getBehavior(getTitle()), null, false));
            return true;
        }
        return false;
    }

    private void mapInitialize(Image behaviorMap) {
        double randDouble = RandomGenerator.getInstance().nextDouble();
        int height = getHeight();
        int width = getWidth();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                ICoopBehavior.ICoopCellType color = ICoopBehavior.ICoopCellType.toType(behaviorMap.getRGB(height - 1 - y, x));
                switch (color) {
                    case OBSTACLE -> registerActor(new Obstacle(this, Orientation.DOWN, new DiscreteCoordinates(x,y)));
                    case ROCK -> {
                        if (randDouble > 0.05) {
                            registerActor(new Rock(this, Orientation.DOWN, new DiscreteCoordinates(x,y)));
                        } else {
                            registerActor(new Coin(this, Orientation.DOWN, new DiscreteCoordinates(x,y)));
                        }
                    }
                }
            }
        }
    }

}
