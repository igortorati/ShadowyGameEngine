import sge.EngineCore;

public class Main {
    public static void main(String[] args) {
        System.out.println("Starting...");
        EngineCore engine = EngineCore.get();
        engine.setScreenConfig(800, 600, 1, "Game", true, true);
        //engine.setScreenConfig(200, 200, 4, "Game");
        engine.run();
    }
}
