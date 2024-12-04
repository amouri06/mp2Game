package ch.epfl.cs107.icoop.area;

import ch.epfl.cs107.icoop.ICoopBehavior;
import ch.epfl.cs107.icoop.handler.DialogHandler;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Window;

public abstract class ICoopArea extends Area {

    public final static float DEFAULT_SCALE_FACTOR = 13.f;
    private float cameraScaleFactor = DEFAULT_SCALE_FACTOR;

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
            return true;
        }
        return false;
    }
}
