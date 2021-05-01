/**
 * Scene class. It will be the class extended by every Scene in the game.
 * */

package sge;

public abstract class Scene {
    private boolean isActive = false;

    /**
     * Default constructor for the Scene.
     * */
    public Scene() {

    };

    /**
     * Init method to be implemented in every scene.
     * */
    public abstract void init();

    /**
     * Update method to be implemented in every scene.
     * @param deltaTime    the Delta Time, needed for controlling the speed
     *              of the animation independently of the Hardware used.
     * */
    public abstract void update(float deltaTime);

    /**
     * Set the scene as the active scene.
     * */
    public void start() { // When game objects are implemented remember to render
        // then and update the JavaDoc for this method
        isActive = true;
    }

    /**
     * Set the scene as inactive scene.
     * */
    public void stop() {// When game objects are implemented remember to delete
        // then from the screen and update the JavaDoc for this method
        isActive = false;
    }
}
