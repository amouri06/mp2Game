package ch.epfl.cs107.icoop;

import static ch.epfl.cs107.play.window.Keyboard.*;

/**
 * Interface KeyboardConfig
 * Definition of movement keys for both players, as well as other global actions in the game.
 */
public final class KeyBindings {

    /**
     * Keys used for the red player.
     */
    public static final PlayerKeyBindings RED_PLAYER_KEY_BINDINGS = new PlayerKeyBindings(W, A, S, D, Q, E);

    /**
     * Touches utilisées pour le joueur bleu.
     */
    public static final PlayerKeyBindings BLUE_PLAYER_KEY_BINDINGS = new PlayerKeyBindings(I, J, K, L, U, O);
    /**
     * Key to move on to the next dialogue.
     */
    public static final int NEXT_DIALOG = SPACE;
    /**
     * Key to reset the game.
     */
    public static final int RESET_GAME = R;
    /**
     * Key to reset the area
     */
    public static final int RESET_AREA = T;

    /**
     * Key to pause the game
     */
    public static final int PAUSE_GAME = P;

    private KeyBindings() {

    }

    /**
     * Keys used by the player
     *
     * @param up         Pour le déplacement vers le haut
     * @param left       Pour le déplacement vers la gauche
     * @param down       Pour le déplacement vers le bas
     * @param right      Pour le déplacement vers la droite
     * @param switchItem Pour changer d'objet en main
     * @param useItem    Pour utiliser l'objet en main
     */
    public record PlayerKeyBindings(int up, int left, int down, int right, int switchItem, int useItem) {
    }
}
