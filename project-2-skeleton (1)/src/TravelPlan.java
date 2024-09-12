import java.util.Properties;

public class TravelPlan {

    private final int END_X;
    private final int DISTANCE_Y;
    private final Properties PROPS;

    private int endY;
    private int currentPriority;
    private int initPriority;
    private boolean coinPowerApplied;

    public TravelPlan(int endX, int distanceY, int priority, Properties props) {
        this.END_X = endX;
        this.DISTANCE_Y = distanceY;
        this.currentPriority = priority;
        this.initPriority = priority;
        this.PROPS = props;
    }

    public int getEndX() {
        return END_X;
    }

    public int getPriority() {
        return currentPriority;
    }

    public int getInitPriority(){
        return initPriority;
    }

    public int getDistanceY() {
        return DISTANCE_Y;
    }

    public int getEndY() {
        return endY;
    }

    public void setStartY(int startY) {
        this.endY = startY - DISTANCE_Y;
    }

    public void setPriority(int priority) {
        this.currentPriority = priority;
    }

    public void setInitPriority(int priority) {
        this.initPriority = priority;
    }

    public void setCoinPowerApplied() {
        this.coinPowerApplied = true;
    }

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
        double travelPlanPriorityFee = currentPriority * Double.parseDouble(
                PROPS.getProperty(String.format("trip.rate.priority%d", currentPriority)));

        return travelPlanDistanceFee + travelPlanPriorityFee;
    }
}
