public class Weather {
    private String type;
    private int startFrame;
    private int endFrame;


    public Weather(String type, int startFrame, int endFrame) {
        this.type = type;
        this.startFrame = startFrame;
        this.endFrame = endFrame;
    }

    public String getType() {
        return type;
    }

    public int getStartFrame() {
        return startFrame;
    }

    public int getEndFrame() {
        return endFrame;
    }
}
