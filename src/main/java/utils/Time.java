package utils;

public class Time {
    public static float timeStarted = System.nanoTime();
    private static float deltaTime = 0;

    public static float getTotalElapsedTime() {
        return (float)((System.nanoTime() - timeStarted) * 1e-9);
    }

    public static void setDeltaTime(float deltaTime) {
        Time.deltaTime = deltaTime;
    }

    public static float getDeltaTime() {
        return deltaTime;
    }
}
