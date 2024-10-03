public class Weather {
    private final String TYPE;
    private final int STRAT_FRAME;
    private final int END_FRAME;


    public Weather(String type, int startFrame, int endFrame) {
        this.TYPE = type;
        this.STRAT_FRAME = startFrame;
        this.END_FRAME = endFrame;
    }

    public String getType() {
        return TYPE;
    }

    public int getStartFrame() {
        return STRAT_FRAME;
    }

    public int getEndFrame() {
        return END_FRAME;
    }
}
