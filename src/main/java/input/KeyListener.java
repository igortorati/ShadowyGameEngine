package input;

import utils.KeyAction;

import java.security.Key;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;


public class KeyListener {
    private KeyAction keys[];
    private static KeyListener keyListener;

    private KeyListener() {
        keys = new KeyAction[350];
        for (int i = 0; i < keys.length; i++) {
            keys[i] = new KeyAction();
        }
    }

    public static synchronized KeyListener get() {
        if (KeyListener.keyListener == null) {
            KeyListener.keyListener = new KeyListener();
        }

        return KeyListener.keyListener;
    }

    public static void keyCallback(long gameWindow, int key, int scancode, int action, int mods) {
        KeyListener instance = get();
        if (key < instance.keys.length) {
            System.out.println("Key: " + key + ".");
            if (action == GLFW_PRESS) {
                System.out.println("Key: " + key + " pressed.");
                if (instance.keys[key].pressed) {
                    instance.keys[key].held = true;
                }
                instance.keys[key].pressed = true;
            } else if (action == GLFW_RELEASE) {
                instance.keys[key].released = true; // Remember to reset this!
                instance.keys[key].held = false;
                instance.keys[key].pressed = false;
            }
        }
    }

    public static boolean isKeyHeld(int button) {
        if (button < get().keys.length) {
            return get().keys[button].held;
        }
        return false;
    }

    public static boolean isKeyUp(int button) {
        if (button < get().keys.length) {
            return get().keys[button].released;
        }
        return false;
    }

    public static boolean isKeyDown(int button) {
        if (button < get().keys.length) {
            return get().keys[button].pressed;
        }
        return false;
    }

    public static void resetKeyRelease() {
        KeyListener instance = get();
        for (int i = 0; i < instance.keys.length; i++) {
            instance.keys[i].released = false;
        }
    }
}
