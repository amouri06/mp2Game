package ch.epfl.cs107.icoop;


import ch.epfl.cs107.icoop.actor.CenterOfMass;
import ch.epfl.cs107.icoop.actor.Door;
import ch.epfl.cs107.icoop.actor.ElementalEntity;
import ch.epfl.cs107.icoop.actor.ICoopPlayer;
import ch.epfl.cs107.icoop.area.ICoopArea;
import ch.epfl.cs107.icoop.area.Maze;
import ch.epfl.cs107.icoop.area.OrbWay;
import ch.epfl.cs107.icoop.area.Spawn;
import ch.epfl.cs107.icoop.handler.DialogHandler;
import ch.epfl.cs107.play.areagame.AreaGame;
import ch.epfl.cs107.play.engine.actor.Dialog;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.window.Keyboard;
import ch.epfl.cs107.play.window.Window;


public class ICoop extends AreaGame implements DialogHandler {

    private final String[] areas = {"Spawn", "OrbWay", "Maze"};
    private int areaIndex;
    private ICoopPlayer firePlayer;
    private ICoopPlayer waterPlayer;
    private Dialog dialog = null;

    /**
     * Add all the Tuto2 areas
     */
    private void createAreas() {
        addArea(new Spawn(this));
        addArea(new OrbWay());
        addArea(new Maze());
    }

    @Override
    public void publish(Dialog dialog) {
        this.dialog = dialog;
    }

    public void setActiveDialog(String path) {
        this.dialog = new Dialog(path);
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

            DiscreteCoordinates coordsBlue = area.getBluePlayerSpawnPosition();
            waterPlayer = new ICoopPlayer(area, Orientation.DOWN, coordsBlue, ElementalEntity.Element.EAU, "icoop/player2", KeyBindings.BLUE_PLAYER_KEY_BINDINGS);

            initArea(areas[areaIndex]);

            return true;
        }
        return false;
    }



    /**
     * @param deltaTime elapsed time since last update, in seconds, non-negative
     */
    @Override
    public void update(float deltaTime) {
        Keyboard keyboard = getCurrentArea().getKeyboard();
        if (!getCurrentArea().isPaused()) {
            if (keyboard.get(KeyBindings.RESET_GAME).isDown()) {
                begin(getWindow(), getFileSystem());
            }
            if (keyboard.get(KeyBindings.RESET_AREA).isDown() || !firePlayer.isAlive() || !waterPlayer.isAlive()) {
                initArea(areas[areaIndex]);
            }
            if (firePlayer.getIsLeavingAreaDoor() != null) {
                System.out.println("maybe");
                switchArea(firePlayer.getIsLeavingAreaDoor());
            }
            if (waterPlayer.getIsLeavingAreaDoor() != null) {
                switchArea(waterPlayer.getIsLeavingAreaDoor());
            }
            super.update(deltaTime);
        }
        if (dialog != null) {
            if (!dialog.isCompleted()) {
                dialog.draw(getWindow());
                getCurrentArea().requestPause();
                if (keyboard.get(KeyBindings.NEXT_DIALOG).isPressed()) {
                    dialog.update(deltaTime);
                }
            }
            else {
                ((ICoopArea) getCurrentArea()).dialogCompleted();
                getCurrentArea().requestResume();
            }
        }
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
        ICoopArea area = (ICoopArea) setCurrentArea(areas[areaIndex], true);

        DiscreteCoordinates coordsRed = area.getRedPlayerSpawnPosition();
        firePlayer.enterArea(area, coordsRed);
        firePlayer.restoreHealth();

        DiscreteCoordinates coordsBlue = area.getBluePlayerSpawnPosition();
        waterPlayer.enterArea(area, coordsBlue);
        waterPlayer.restoreHealth();


        CenterOfMass cameraCenter = new CenterOfMass(firePlayer, waterPlayer);

    }

    /**
     * switches from one area to the other
     * the player is healed when moving to a new area
     */
    private void switchArea(Door door) {
        firePlayer.leaveArea(); waterPlayer.leaveArea();
        ICoopArea currentArea = (ICoopArea) setCurrentArea(door.getDestination(), false);
        firePlayer.enterArea(currentArea, (door.getPlayer1ArrivalCoordinates()));
        waterPlayer.enterArea(currentArea, (door.getPlayer2ArrivalCoordinates()));
        firePlayer.nullifyIsLeavingAreaDoor(); waterPlayer.nullifyIsLeavingAreaDoor();

        for (int i = 0; i < areas.length; ++i) {
            if (areas[i].equals(door.getDestination())) {
                areaIndex = i;
            }
        }
    }
}
