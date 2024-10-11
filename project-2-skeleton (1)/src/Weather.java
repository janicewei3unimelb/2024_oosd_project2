/**
 * Represents a specific weather at a period of time with its basic information.
 */

public class Weather {
    private final String TYPE;
    private final int STRAT_FRAME;
    private final int END_FRAME;

    /**
     * Creates a new instance of weather. It represents a type of weather
     * during a specified period of time in the game.
     *
     * @param type This is the weather type (sunny or rainy)
     * @param startFrame This represents the frame when the weather should start
     * @param endFrame This indicates when the weather should stop
     */
    public Weather(String type, int startFrame, int endFrame) {
        this.TYPE = type;
        this.STRAT_FRAME = startFrame;
        this.END_FRAME = endFrame;
    }

    /**
     * Gets the type of the weather.
     *
     * @return the type of weather condition
     */
    public String getType() {
        return TYPE;
    }

    /**
     * Gets the starting frame of this weather
     *
     * @return the starting frame of the weather
     */
    public int getStartFrame() {
        return STRAT_FRAME;
    }

    /**
     * Gets the ending frame of this weather
     *
     * @return the ending frame of the weather
     */
    public int getEndFrame() {
        return END_FRAME;
    }
}
