/**
 * This class simply stores three boolean values, used for key
 * and button status.
 * */

package utils;

public class KeyAction {
    public boolean pressed;
    public boolean released;
    public boolean held;

    public KeyAction() {
        pressed = false;
        released = false;
        held = false;
    }
}
