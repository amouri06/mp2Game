package ch.epfl.cs107.icoop;


import ch.epfl.cs107.icoop.actor.*;
import ch.epfl.cs107.icoop.actor.doors.Door;
import ch.epfl.cs107.icoop.actor.miscellaneous.CenterOfMass;
import ch.epfl.cs107.icoop.area.*;
import ch.epfl.cs107.icoop.area.maps.Arena;
import ch.epfl.cs107.icoop.area.maps.Maze;
import ch.epfl.cs107.icoop.area.maps.OrbWay;
import ch.epfl.cs107.icoop.area.maps.Spawn;
import ch.epfl.cs107.icoop.audio.Sound;
import ch.epfl.cs107.icoop.handler.DialogHandler;
import ch.epfl.cs107.icoop.handler.ICoopPlayerStatusGUI;
import ch.epfl.cs107.play.areagame.AreaGame;
import ch.epfl.cs107.play.engine.actor.Dialog;
import ch.epfl.cs107.play.engine.actor.Foreground;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Keyboard;
import ch.epfl.cs107.play.window.Window;

import static ch.epfl.cs107.icoop.area.ICoopArea.DEFAULT_SCALE_FACTOR;
import static java.lang.Math.max;


public class ICoop extends AreaGame implements DialogHandler {
    private Helper helper;
    private final String[] areas = {"Arena", "Spawn", "OrbWay", "Maze"};
    private int areaIndex;
    private static ICoopPlayer firePlayer;
    private Sound sound = new Sound();
    private ICoopPlayerStatusGUI firePlayerStatusGUI;
    private static ICoopPlayer waterPlayer;
    private static Pet firePlayerPet;
    private ICoopPlayerStatusGUI waterPlayerStatusGUI;
    private Dialog dialog = null;
    private CenterOfMass cameraCenter;
    private Foreground pauseAnimation;



    /**
     * Add all the ICoop areas
     */
    private void createAreas() {

        Arena arena = new Arena(this);
        addArea(arena);

        Spawn spawn =  new Spawn(this, arena);
        addArea(spawn);

        OrbWay orbWay= new OrbWay(this);
        addArea(orbWay);

        addArea(new Maze(this));

        //Instance of helper given the logics associated
        helper = new Helper(spawn, Orientation.DOWN, new DiscreteCoordinates(4,10), orbWay, arena);

    }

    /**
     * Publishes dialog
     * @param dialog (dialog)
     */
    @Override
    public void publish(Dialog dialog) {
        this.dialog = dialog;
    }

    /**
     * changes the active dialog
     * @param path (String) : path to dialog
     */
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
            areaIndex = 1;
            ICoopArea area = (ICoopArea) setCurrentArea(areas[areaIndex], true);

            //registers the fire player as well as his pet
            DiscreteCoordinates coordsRed = area.getRedPlayerSpawnPosition();
            firePlayer = new ICoopPlayer(area, Orientation.DOWN, coordsRed, ElementalEntity.Element.FEU, "icoop/player", KeyBindings.RED_PLAYER_KEY_BINDINGS);
            firePlayerStatusGUI = new ICoopPlayerStatusGUI(firePlayer, true);
            firePlayerPet= new Pet(area, Orientation.DOWN, coordsRed, firePlayer,"icoop/Slime3_Walk_Full");

            //registers water player as well as his pet
            DiscreteCoordinates coordsBlue = area.getBluePlayerSpawnPosition();
            waterPlayer = new ICoopPlayer(area, Orientation.DOWN, coordsBlue, ElementalEntity.Element.EAU, "icoop/player2", KeyBindings.BLUE_PLAYER_KEY_BINDINGS);
            waterPlayerStatusGUI = new ICoopPlayerStatusGUI(waterPlayer, false);

            initArea(areas[areaIndex]);

            cameraCenter = new CenterOfMass(firePlayer, waterPlayer);

            stopMusic();
            playMusic(1);


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
        //Checks if the dialog is null
        if (dialog != null) {
            //if the dialog is not competed continue
            if (!dialog.isCompleted()) {
                dialog.draw(getWindow());
                getCurrentArea().requestPause();
                if (keyboard.get(KeyBindings.NEXT_DIALOG).isPressed()) {
                    dialog.update(deltaTime);
                }
            }
            //else set the dialog to null
            else {
                getCurrentArea().requestResume();
                dialog = null;
            }
        }

        //if the current area is not paused
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
            if (keyboard.get(KeyBindings.PAUSE_GAME).isPressed()) {
                getCurrentArea().requestPause();
                pauseAnimation = new Foreground(getCurrentArea(), null, "pause");
                getCurrentArea().registerActor(pauseAnimation);
            }
            super.update(deltaTime);
            return;
        }
        //if pause game key is pressed resume the game
        if (keyboard.get(KeyBindings.PAUSE_GAME).isPressed()) {
            getCurrentArea().requestResume();
            getCurrentArea().unregisterActor(pauseAnimation);
        }
    }

    @Override
    public void end() {
    }

    /**
     *
     * @return (String): title of game
     */
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
        firePlayerPet.enterArea(area, coordsRed);
        firePlayer.restoreHealth();

        DiscreteCoordinates coordsBlue = area.getBluePlayerSpawnPosition();
        waterPlayer.enterArea(area, coordsBlue);
        waterPlayer.restoreHealth();

        if (areas[areaIndex].equals("Spawn")) {
            getCurrentArea().registerActor(helper);
        }


    }

    /**
     * switches from one area to the other
     * the player is healed when moving to a new area
     */
    private void switchArea(Door door) {
        //checks if door leads somewhere
        if (door.getArrivalCoordinates() != null) {
            //players leave the current area
            firePlayer.leaveArea(); waterPlayer.leaveArea();
            getCurrentArea().unregisterActor(firePlayerPet);
            //change the current area to the door's destination
            ICoopArea currentArea = (ICoopArea) setCurrentArea(door.getDestination(), false);
            //add both players and pet to the new area
            firePlayer.enterArea(currentArea, (door.getArrivalCoordinates().getFirst()));
            waterPlayer.enterArea(currentArea, (door.getArrivalCoordinates().get(1)));
            firePlayerPet.enterArea(currentArea, (door.getArrivalCoordinates().getFirst()));
            firePlayer.nullifyIsLeavingAreaDoor(); waterPlayer.nullifyIsLeavingAreaDoor();
            //updates the area's index
            for (int i = 0; i < areas.length; ++i) {
                if (areas[i].equals(door.getDestination())) {
                    areaIndex = i;
                }
            }
        }
        // if a dialog is assocaited -> publish it
        if (door.getDialog() != null) {
            ((ICoopArea) getCurrentArea()).publish(door.getDialog());
        }
    }

    public void playMusic(int i){
        sound.setFile(i);
        sound.play();
        sound.loop();
    }

    public void stopMusic(){
        sound.stop();
    }

    public void playSoundEffect(int i){
        sound.setFile(i);
        sound.play();
    }

}
