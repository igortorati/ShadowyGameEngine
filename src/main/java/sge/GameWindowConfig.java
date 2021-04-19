package sge;

public class GameWindowConfig {
    public int width;
    public int height;
    public int scale;
    public String title;

    GameWindowConfig(int width, int height, int scale, String title) {
        this.width = width;
        this.height = height;
        this.scale = scale;
        this.title = title;
    }
}
