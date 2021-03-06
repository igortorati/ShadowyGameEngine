package sge;

import input.KeyListener;
import input.MouseListener;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import renderer.Shader;
import utils.Time;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;
import test.TestScene;

public class EngineCore {
    private static EngineCore engineCore = null;
    private long gameWindow;
    private GameWindowConfig windowConfig;
    private boolean showFPS;
    private boolean vSync;
    private Scene activeScene;

    private EngineCore() {
        windowConfig = new GameWindowConfig(800, 600, 1, "Game");
        showFPS = false; // Default value for displaying FPS on window title
        vSync = true; // Default value for vSync
    }

    public static synchronized EngineCore get() {
        if (engineCore == null) {
            engineCore = new EngineCore();
        }

        return engineCore;
    }

    public void setScreenConfig(int width, int height, int scale, String title) {
        setScreenConfig(width, height, scale, title, false, false);
    }

    public void setScreenConfig(int width, int height, int scale, String title, boolean showFPS, boolean vSync) {
        windowConfig.width = width;
        windowConfig.height = height;
        windowConfig.scale = scale;
        windowConfig.title = title;
        this.showFPS = showFPS;
        this.vSync = vSync;
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

        // Set all Call backs needed
        callBacks();

        // Make OpenGL contect current
        glfwMakeContextCurrent(gameWindow);

        // Enable v-sync
        glfwSwapInterval((vSync)? 1 : 0);

        // Make window visible
        glfwShowWindow(gameWindow);

        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();


    }

    private void callBacks() {
        // Set Callbacks (using lambda functions)
        glfwSetCursorPosCallback(gameWindow, MouseListener::positionCallBack);
        glfwSetMouseButtonCallback(gameWindow, MouseListener::buttonCallback);
        glfwSetScrollCallback(gameWindow, MouseListener::scrollCallback);
        glfwSetKeyCallback(gameWindow, KeyListener::keyCallback);
    }

    public void loop() {


        // Set the clear color
        glClearColor(1.0f, 0.0f, 0.0f, 0.0f);

        float frameStart = Time.getTotalElapsedTime();
        float frameEnd = Time.getTotalElapsedTime();

        this.activeScene = new TestScene();
        activeScene.init();

        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while ( !glfwWindowShouldClose(gameWindow) ) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

            onUpdate(); // Execute custom code

            resetListeners();

            activeScene.update(Time.getDeltaTime());

            glfwSwapBuffers(gameWindow); // swap the color buffers

            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents();

            frameEnd = Time.getTotalElapsedTime();
            Time.setDeltaTime(frameEnd - frameStart); // Calculate delta time (time per frame)
            frameStart = frameEnd;

            if (showFPS) glfwSetWindowTitle(gameWindow, windowConfig.title + " " + (1/Time.getDeltaTime())); // Show FPS on window title
        }
    }

    private void resetListeners() {
        KeyListener.resetKeyRelease(); // Set key release status to false
        MouseListener.end(); // Resetting the scrolls, setting dy and dx to zero and resetting
        // mouse button release status.
    }

    public void onUpdate(){
    }
}
