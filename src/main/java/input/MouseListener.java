package input;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.system.CallbackI;
import utils.KeyAction;
import utils.Vect2D;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class MouseListener {
    private Vect2D pos;
    private Vect2D lastPos;
    private KeyAction keys[];
    private boolean dragging;
    private double xScroll;
    private double yScroll;
    private static MouseListener mouseListener = null;

    private MouseListener() {
        pos = new Vect2D();
        lastPos = new Vect2D();
        xScroll = 0.0;
        yScroll = 0.0;
        keys = new KeyAction[3];
        for (int i = 0; i < keys.length; i++) {
            keys[i] = new KeyAction();
        }
    }

    public static synchronized MouseListener get() {
        if (MouseListener.mouseListener == null) {
            MouseListener.mouseListener = new MouseListener();
        }

        return MouseListener.mouseListener;
    }

    public static void positionCallBack(long gameWindow, double x, double y) {
        MouseListener instance = get();
        instance.lastPos.copy(instance.pos);
        instance.pos.set(x, y);
        instance.dragging = isAnyPressed();
    }

    private static boolean isAnyPressed() {
        MouseListener instance = get();
        for (int i = 0; i < instance.keys.length; i++) {
            if (instance.keys[i].pressed) return true;
        }
        return false;
    }

    public static void buttonCallback(long gameWindow, int button, int action, int mods) {
        MouseListener instance = get();
        if (button < instance.keys.length) {
            if (action == GLFW_PRESS) {
                if (instance.keys[button].pressed == true) {
                    instance.keys[button].held = true;
                }
                instance.keys[button].pressed = true;
            } else if (action == GLFW_RELEASE) {
                instance.keys[button].pressed = false;
                instance.keys[button].released = true; // Remember to reset!!
                instance.keys[button].held = false;
                instance.dragging = false;
            }
        }
    }

    public static void scrollCallback(long gameWindow, double xOffset, double yOffset) {
        MouseListener instance = get();
        instance.xScroll = xOffset;
        instance.yScroll = yOffset;
    }

    public static void end() {
        MouseListener instance = get();
        instance.xScroll = 0.0;
        instance.yScroll = 0.0;
        instance.lastPos.set(0.0, 0.0);
    }

    public static double getX() {
        return (float)get().pos.x;
    }

    public static double getY() {
        return (float)get().pos.y;
    }

    public static double getDx() {
        return (float)(get().lastPos.x - get().pos.x);
    }

    public static double getDy() {
        return (float)(get().lastPos.y - get().pos.y);
    }

    public static double getXScroll() {
        return (float)get().xScroll;
    }

    public static double getYScroll() {
        return (float)get().xScroll;
    }

    public static boolean isDragging() {
        return get().dragging;
    }

    public static boolean isButtonHeld(int button) {
        if (button < get().keys.length) {
            return get().keys[button].held;
        }
        return false;
    }

    public static boolean isButtonUp(int button) {
        if (button < get().keys.length) {
            return get().keys[button].released;
        }
        return false;
    }

    public static boolean isButtonDown(int button) {
        if (button < get().keys.length) {
            return get().keys[button].pressed;
        }
        return false;
    }

    public static void resetButtonRelease() {
        MouseListener instance = get();
        for (int i = 0; i < instance.keys.length; i++) {
            instance.keys[i].released = false;
        }
    }
}
