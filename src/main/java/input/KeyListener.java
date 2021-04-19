/**
 * Keyboard listener class. It listens for all possible keyboard keys on GLFW.
 * This class stores the status for each keyboard key.
 * */

package input;

import utils.KeyAction;

import java.security.Key;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;


public class KeyListener {

    /**
     * Stores Key Actions for each key: {@link #keys}
     * */
    private KeyAction keys[];

    /**
     * Stores the singleton instance for the KeyListener
     * class: {@link #keyListener}
     * */
    private static KeyListener keyListener;

    /**
     * Default constructor for the KeyListener.
     * */
    private KeyListener() {
        keys = new KeyAction[350];
        for (int i = 0; i < keys.length; i++) {
            keys[i] = new KeyAction();
        }
    }

    /**
     * Singleton static method, synchronized to avoid multiple
     * instances if multiple threads call it at the same time.
     * */
    public static synchronized KeyListener get() {
        if (KeyListener.keyListener == null) {
            KeyListener.keyListener = new KeyListener();
        }

        return KeyListener.keyListener;
    }

    /**
     * Standard Callback method for updating the key on each action
     * as described on the GLFW documentation.
     */
    public static void keyCallback(long gameWindow, int key, int scancode, int action, int mods) {
        KeyListener instance = get();
        if (key < instance.keys.length) {
            if (action == GLFW_PRESS) {
                instance.keys[key].held = true;
                instance.keys[key].pressed = true;
            } else if (action == GLFW_RELEASE) {
                instance.keys[key].released = true; // Remember to reset this!
                instance.keys[key].held = false;
                instance.keys[key].pressed = false;
            }
        }
    }

    /**
     * Returns if the specified key is being held.
     */
    public static boolean isKeyHeld(int button) {
        if (button < get().keys.length) {
            return get().keys[button].held;
        }
        return false;
    }

    /**
     * Returns if the specified key has been released.
     */
    public static boolean isKeyUp(int button) {
        if (button < get().keys.length) {
            return get().keys[button].released;
        }
        return false;
    }

    /**
     * Returns if the specified key has been pressed.
     */
    public static boolean isKeyDown(int button) {
        if (button < get().keys.length) {
            return get().keys[button].pressed;
        }
        return false;
    }

    /**
     * Reset release state for all keys (at the end of the frame).
     */
    public static void resetKeyRelease() {
        KeyListener instance = get();
        for (int i = 0; i < instance.keys.length; i++) {
            instance.keys[i].released = false;
        }
    }
}
