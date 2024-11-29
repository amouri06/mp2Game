package ch.epfl.cs107.icoop;


import ch.epfl.cs107.icoop.actor.CenterOfMass;
import ch.epfl.cs107.icoop.actor.ElementalEntity;
import ch.epfl.cs107.icoop.actor.ICoopPlayer;
import ch.epfl.cs107.icoop.area.ICoopArea;
import ch.epfl.cs107.icoop.area.OrbWay;
import ch.epfl.cs107.icoop.area.Spawn;
import ch.epfl.cs107.play.areagame.AreaGame;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.window.Window;


public class ICoop extends AreaGame {

    private final String[] areas = {"Spawn", "OrbWay"};
    private int areaIndex;
    private ICoopPlayer firePlayer;
    private ICoopPlayer waterPlayer;

    /**
     * Add all the Tuto2 areas
     */
    private void createAreas() {
        addArea(new Spawn());
        addArea(new OrbWay());
    }

    /**
     * @param window (Window): display context. Not null
     * @param fileSystem (FileSystem): given file system. Not null
     * @return true if the game begins properly
     */
    @Override
    public boolean begin(Window window, FileSystem fileSystem) {
        if (super.begin(window, fileSystem)) {
            createAreas();
            areaIndex = 0;

            ICoopArea area = (ICoopArea) setCurrentArea(areas[areaIndex], true);

            DiscreteCoordinates coordsRed = area.getRedPlayerSpawnPosition();
            firePlayer = new ICoopPlayer(area, Orientation.DOWN, coordsRed, ElementalEntity.Element.FEU, "icoop/player", KeyBindings.RED_PLAYER_KEY_BINDINGS);
            firePlayer.enterArea(area, coordsRed);

            DiscreteCoordinates coordsBlue = area.getBluePlayerSpawnPosition();
            waterPlayer = new ICoopPlayer(area, Orientation.DOWN, coordsBlue, ElementalEntity.Element.EAU, "icoop/player2", KeyBindings.BLUE_PLAYER_KEY_BINDINGS);
            waterPlayer.enterArea(area, coordsBlue);

            CenterOfMass cameraCenter = new CenterOfMass(firePlayer, waterPlayer);
            area.setViewCandidate(cameraCenter);

            return true;
        }
        return false;
    }



    /**
     * @param deltaTime elapsed time since last update, in seconds, non-negative
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
    }

    @Override
    public void end() {
    }

    @Override
    public String getTitle() {
        return "ICoop";
    }

    /**
     * sets the area named `areaKey` as current area in the game ICoop
     * @param areaKey (String) title of an area
     */
    private void initArea(String areaKey) {


    }

    /**
     * switches from one area to the other
     * the player is healed when moving to a new area
     */
    private void switchArea() {
        /*player.leaveArea();
        areaIndex = (areaIndex == 0) ? 1 : 0;
        Tuto2Area currentArea = (Tuto2Area) setCurrentArea(areas[areaIndex], false);
        player.enterArea(currentArea, currentArea.getPlayerSpawnPosition());
        player.strengthen();*/
    }
}
