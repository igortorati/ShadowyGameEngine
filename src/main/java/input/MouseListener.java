/**
 * Mouse listener class. It listens for all possible mouse keys on GLFW.
 * This class stores the mouse position and last position (used to calculate
 * the difference between the mouse position from one frame and the previous
 * one).
 * It also stores the values for the mouse wheel (scroll), and the button
 * status (pressed, held and released).
 * */
package input;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.system.CallbackI;
import utils.KeyAction;
import utils.Vect2D;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class MouseListener {

    /**
     * Stores the mouse current position on screen: {@link #pos}
     * */
    private Vect2D pos;

    /**
     * Stores the mouse previous position on screen: {@link #lastPos}
     * */
    private Vect2D lastPos;

    /**
     * Stores Key Actions for each mouse buttons: {@link #keys}
     * */
    private KeyAction keys[];

    /**
     * Stores if the mouse is being dragged (moved while pressed/held)
     * by any button: {@link #dragging}
     * */
    private boolean dragging;

    /**
     * Stores the mouse scroll on the X-axis: {@link #xScroll}
     * */
    private double xScroll;

    /**
     * Stores the mouse scroll on the Y-axis: {@link #yScroll}
     * */
    private double yScroll;

    /**
     * Stores the singleton instance for the MouseListener
     * class: {@link #mouseListener}
     * */
    private static MouseListener mouseListener = null;

    /**
     * Default constructor for the MouseListener.
     * */
    private MouseListener() {
        pos = new Vect2D();
        lastPos = new Vect2D();
        xScroll = 0.0;
        yScroll = 0.0;
        keys = new KeyAction[7];
        for (int i = 0; i < keys.length; i++) {
            keys[i] = new KeyAction();
        }
    }

    /**
     * Singleton static method, synchronized to avoid multiple
     * instances if multiple threads call it at the same time.
     * */
    public static synchronized MouseListener get() {
        if (MouseListener.mouseListener == null) {
            MouseListener.mouseListener = new MouseListener();
        }

        return MouseListener.mouseListener;
    }

    /**
     * Standard Callback method for updating the mouse position
     * as described on the GLFW documentation.
     */
    public static void positionCallBack(long gameWindow, double x, double y) {
        MouseListener instance = get();
        instance.lastPos.copy(instance.pos);
        instance.pos.set(x, y);
        instance.dragging = isAnyPressed();
    }

    /**
     * Standard Callback method for updating the mouse button pressed
     * as described on the GLFW documentation.
     */
    public static void buttonCallback(long gameWindow, int button, int action, int mods) {
        MouseListener instance = get();
        if (button < instance.keys.length) {
            if (action == GLFW_PRESS) {
                instance.keys[button].held = true;
                instance.keys[button].pressed = true;
            } else if (action == GLFW_RELEASE) {
                instance.keys[button].pressed = false;
                instance.keys[button].released = true; // Remember to reset!!
                instance.keys[button].held = false;
                instance.dragging = false;
            }
        }
    }

    /**
     * Standard Callback method for updating the mouse scrolling
     * as described on the GLFW documentation.
     */
    public static void scrollCallback(long gameWindow, double xOffset, double yOffset) {
        MouseListener instance = get();
        instance.xScroll = xOffset;
        instance.yScroll = yOffset;
    }

    /**
     * Set scrolling and dx and dy to zero. Called at the end of each frame.
     */
    public static void end() {
        MouseListener instance = get();
        instance.xScroll = 0.0;
        instance.yScroll = 0.0;
        instance.lastPos.set(instance.getX(), instance.getY());
        MouseListener.resetButtonRelease();
    }

    /**
     * Returns if any button is pressed.
     */
    private static boolean isAnyPressed() {
        MouseListener instance = get();
        for (int i = 0; i < instance.keys.length; i++) {
            if (instance.keys[i].pressed) return true;
        }
        return false;
    }

    /**
     * Returns the current X position.
     */
    public static double getX() {
        return (float)get().pos.x;
    }

    /**
     * Returns the current Y position.
     */
    public static double getY() {
        return (float)get().pos.y;
    }

    /**
     * Returns the current X and Y position as a Vect2D.
     */
    public static Vect2D getPos() {
        return get().pos;
    }

    /**
     * Returns dx value, the difference between current and last
     * x values.
     */
    public static double getDx() {
        return (float)(get().lastPos.x - get().pos.x);
    }

    /**
     * Returns dy value, the difference between current and last
     * y values.
     */
    public static double getDy() {
        return (float)(get().lastPos.y - get().pos.y);
    }

    /**
     * Returns scroll horizontal value.
     */
    public static double getXScroll() {
        return (float)get().xScroll;
    }

    /**
     * Returns scroll vertical value.
     */
    public static double getYScroll() {
        return (float)get().xScroll;
    }

    /**
     * Returns if mouse is dragging.
     */
    public static boolean isDragging() {
        return get().dragging;
    }

    /**
     * Returns if the specified button is being held.
     */
    public static boolean isButtonHeld(int button) {
        if (button < get().keys.length) {
            return get().keys[button].held;
        }
        return false;
    }

    /**
     * Returns if the specified button has been released.
     */
    public static boolean isButtonUp(int button) {
        if (button < get().keys.length) {
            return get().keys[button].released;
        }
        return false;
    }

    /**
     * Returns if the specified button has been pressed.
     */
    public static boolean isButtonDown(int button) {
        if (button < get().keys.length) {
            return get().keys[button].pressed;
        }
        return false;
    }

    /**
     * Reset release state for all buttons (at the end of the frame).
     */
    public static void resetButtonRelease() {
        MouseListener instance = get();
        for (int i = 0; i < instance.keys.length; i++) {
            instance.keys[i].released = false;
        }
    }
}
