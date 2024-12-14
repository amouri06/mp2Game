package ch.epfl.cs107.icoop;


import ch.epfl.cs107.icoop.actor.CenterOfMass;
import ch.epfl.cs107.icoop.actor.Door;
import ch.epfl.cs107.icoop.actor.ElementalEntity;
import ch.epfl.cs107.icoop.actor.ICoopPlayer;
import ch.epfl.cs107.icoop.area.*;
import ch.epfl.cs107.icoop.handler.DialogHandler;
import ch.epfl.cs107.icoop.handler.ICoopItem;
import ch.epfl.cs107.icoop.handler.ICoopPlayerStatusGUI;
import ch.epfl.cs107.play.areagame.AreaGame;
import ch.epfl.cs107.play.engine.actor.Dialog;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.io.ResourcePath;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Keyboard;
import ch.epfl.cs107.play.window.Window;

import java.util.List;

import static ch.epfl.cs107.icoop.area.ICoopArea.DEFAULT_SCALE_FACTOR;
import static java.lang.Math.max;


public class ICoop extends AreaGame implements DialogHandler {

    private final String[] areas = {"Spawn", "OrbWay", "Maze", "Arena"};
    private int areaIndex;
    private static ICoopPlayer firePlayer;
    private ICoopPlayerStatusGUI firePlayerStatusGUI;
    private static ICoopPlayer waterPlayer;
    private ICoopPlayerStatusGUI waterPlayerStatusGUI;
    private Dialog dialog = null;
    CenterOfMass cameraCenter;


    /**
     * Add all the Tuto2 areas
     */
    private void createAreas() {
        addArea(new Spawn(this));
        addArea(new OrbWay(this));
        addArea(new Maze(this, AreaCompleteLogic.MAZE));
        addArea(new Arena(this, getWindow().getImage(ResourcePath.getBehavior("Arena"), null, false), AreaCompleteLogic.ARENA));
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
            firePlayerStatusGUI = new ICoopPlayerStatusGUI(firePlayer, true);

            DiscreteCoordinates coordsBlue = area.getBluePlayerSpawnPosition();
            waterPlayer = new ICoopPlayer(area, Orientation.DOWN, coordsBlue, ElementalEntity.Element.EAU, "icoop/player2", KeyBindings.BLUE_PLAYER_KEY_BINDINGS);
            waterPlayerStatusGUI = new ICoopPlayerStatusGUI(waterPlayer, false);

            initArea(areas[areaIndex]);

            cameraCenter = new CenterOfMass(firePlayer, waterPlayer);

            return true;
        }
        return false;
    }



    /**
     * @param deltaTime elapsed time since last update, in seconds, non-negative
     */
    @Override
    public void update(float deltaTime) {

        ((ICoopArea) getCurrentArea()).setCameraScaleFactor((float) max(DEFAULT_SCALE_FACTOR, DEFAULT_SCALE_FACTOR * 0.75 + (new Vector(firePlayer.getCurrentMainCellCoordinates().x, firePlayer.getCurrentMainCellCoordinates().y).sub(new Vector(waterPlayer.getCurrentMainCellCoordinates().x, waterPlayer.getCurrentMainCellCoordinates().y))).getLength()));
        getCurrentArea().setViewCandidate(cameraCenter);


        firePlayerStatusGUI.draw(getWindow());
        waterPlayerStatusGUI.draw(getWindow());

        Keyboard keyboard = getCurrentArea().getKeyboard();
        if (!getCurrentArea().isPaused()) {
            if (keyboard.get(KeyBindings.RESET_GAME).isDown()) {
                begin(getWindow(), getFileSystem());
            }
            if (keyboard.get(KeyBindings.RESET_AREA).isDown() || !firePlayer.isAlive() || !waterPlayer.isAlive()) {
                initArea(areas[areaIndex]);
            }
            if (firePlayer.getIsLeavingAreaDoor() != null) {
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
                getCurrentArea().requestResume();
                dialog = null;
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

    public enum AreaCompleteLogic implements Logic {
        MAZE(new ICoopItem[]{ICoopItem.FireStaff, ICoopItem. WaterStaff}, new ICoopPlayer[]{firePlayer, waterPlayer}),
        ARENA(new ICoopItem[]{ICoopItem.FireKey, ICoopItem.WaterKey}, new ICoopPlayer[]{firePlayer, waterPlayer});

        private AreaCompleteLogic(ICoopItem[] iCoopItems, ICoopPlayer[] players) {
            this.iCoopItems = iCoopItems;
            this.players = players;
        }

        private ICoopItem[] iCoopItems;
        private ICoopPlayer[] players;

        @Override
        public boolean isOn() {
//            for (ICoopPlayer player: players) {
//                boolean inventoryContains = false;
//                for (ICoopItem iCoopItem: iCoopItems) {
//                    if (player.inventoryContains(iCoopItem)) {
//                        inventoryContains = true;
//                    }
//                }
//                if (!inventoryContains) return false;
//            }
//            return true;
            return ((firePlayer.inventoryContains(iCoopItems[0]) || firePlayer.inventoryContains(iCoopItems[1])) && (waterPlayer.inventoryContains(iCoopItems[0]) || waterPlayer.inventoryContains(iCoopItems[1])));
        }

        @Override
        public boolean isOff() {
            return !isOn();
        }
    }

}
