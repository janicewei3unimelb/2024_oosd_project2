import java.util.Properties;

/**
 * A class representing a travel plan, which has all the details of priority, coin power,
 * end location and expected fee calculation.
 */
public class TravelPlan {

    private final int END_X;
    private final int DISTANCE_Y;
    private final Properties PROPS;

    private int endY;
    private int currentPriority;
    private int initPriority;
    private boolean coinPowerApplied;

    /**
     * Creates a travel plan associated with a specific passenger with its travel details
     *
     * @param endX The x-coordinate of the ending place for the trip.
     * @param distanceY The vertical distance of the travel
     * @param priority Passenger's priority
     * @param props The game property where we can fetch essential information
     */
    public TravelPlan(int endX, int distanceY, int priority, Properties props) {
        this.END_X = endX;
        this.DISTANCE_Y = distanceY;
        this.currentPriority = priority;
        this.initPriority = priority;
        this.PROPS = props;
    }

    /**
     * Gets the x-coordinate of the end of the trip
     *
     * @return The x-coordinate of the end of the trip
     */
    public int getEndX() {
        return END_X;
    }

    /**
     * Gets passenger's priority
     *
     * @return Get the passenger's priority
     */
    public int getPriority() {
        return currentPriority;
    }

    /**
     * Gets passenger's initial priority
     *
     * @return Get the passenger's initial priority
     */
    public int getInitPriority(){
        return initPriority;
    }

    /**
     * Gets the y-coordinate of the end of the trip
     *
     * @return  The y-coordinate of the end of the trip
     */
    public int getEndY() {
        return endY;
    }

    /**
     * Sets the ending y coordinate of the trip
     *
     * @param startY The staring y coordinate that we want to use to calculate the ending y coordinate
     */
    public void setEndY(int startY) {
        this.endY = startY - DISTANCE_Y;
    }

    /**
     * Sets the passenger's priority
     *
     * @param priority The priority value that we want to assign to the passenger of the travel
     */
    public void setPriority(int priority) {
        this.currentPriority = priority;
    }

    /**
     * Marks that the passenger has gained coin power's effect
     */
    public void setCoinPowerApplied() {
        this.coinPowerApplied = true;
    }

    /**
     * Return the result of whether the trip gains the coin power
     *
     * @return Return true if the travel has gained the coin power's effect; otherwise, false
     */
    public boolean getCoinPowerApplied() {
        return this.coinPowerApplied;
    }

    /**
     * Get the expected fee of the trip based on the travel distance and priority.
     * @return The expected fee of the trip.
     */
    public double getExpectedFee() {
        double ratePerY = Double.parseDouble(PROPS.getProperty("trip.rate.perY"));
        double travelPlanDistanceFee = ratePerY * DISTANCE_Y;
        double travelPlanPriorityFee = Double.parseDouble(
                PROPS.getProperty(String.format("trip.rate.priority%d", currentPriority)));

        return travelPlanDistanceFee + travelPlanPriorityFee;
    }
}
