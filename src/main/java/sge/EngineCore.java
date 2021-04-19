package sge;

import input.KeyListener;
import input.MouseListener;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class EngineCore {
    private static EngineCore engineCore = null;
    private long gameWindow;
    private GameWindowConfig windowConfig;

    private EngineCore() {
        windowConfig = new GameWindowConfig(800, 600, 1, "Game");
    }

    public static synchronized EngineCore get() {
        if (engineCore == null) {
            engineCore = new EngineCore();
        }

        return engineCore;
    }

    public void setScreenConfig(int width, int height, int scale, String title) {
        windowConfig.width = width;
        windowConfig.height = height;
        windowConfig.scale = scale;
        windowConfig.title = title;
    }

    public void run() {
        System.out.println("Initializing LWJGL " + Version.getVersion() + "...");

        init();
        loop();

        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(gameWindow);
        glfwDestroyWindow(gameWindow);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public void init() {
        // Setup error callback
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW
        if (!glfwInit()) {
            throw new IllegalStateException("Not able to initialize GLFW!");
        }

        // Configure GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // Make not visible until finish configuring.
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_FALSE);

        // Create window (long is a memory position for the screen data)
        gameWindow = glfwCreateWindow(windowConfig.width * windowConfig.scale, windowConfig.height * windowConfig.scale, windowConfig.title, NULL, NULL);
        if (gameWindow == NULL) {
            throw new IllegalStateException("Failed to create the GLFW window!");
        }

        // Set Callbacks (using lambda functions)
        glfwSetCursorPosCallback(gameWindow, MouseListener::positionCallBack);
        glfwSetMouseButtonCallback(gameWindow, MouseListener::buttonCallback);
        glfwSetScrollCallback(gameWindow, MouseListener::scrollCallback);
        glfwSetKeyCallback(gameWindow, KeyListener::keyCallback);

        // Make OpenGL contect current
        glfwMakeContextCurrent(gameWindow);

        // Enable v-sync
        glfwSwapInterval(1);

        // Make window visible
        glfwShowWindow(gameWindow);
    }

    public void loop() {
        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

        // Set the clear color
        glClearColor(1.0f, 0.0f, 0.0f, 0.0f);

        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while ( !glfwWindowShouldClose(gameWindow) ) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

            onUpdate(); // Execute custom code
            KeyListener.resetKeyRelease(); // Set key release to false
            MouseListener.resetButtonRelease(); // Set button release to false

            glfwSwapBuffers(gameWindow); // swap the color buffers

            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents();
        }
    }

    public void onUpdate(){
        if (KeyListener.isKeyDown(GLFW_KEY_SPACE)) {
            System.out.println("Space Key Pressed");
        }
    }
}
