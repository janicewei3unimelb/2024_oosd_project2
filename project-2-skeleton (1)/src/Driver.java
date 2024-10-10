import bagel.*;

import java.util.ArrayList;
import java.util.Properties;

/**
 * Class representing the driver of the game play
 */
public class Driver extends DamageableGameEntity {
    private Taxi taxi;
    private final int WALK_SPEED_X;
    private final int WALK_SPEED_Y;
    private final Image IMAGE;

    private Coin coinPower;
    private Trip trip;
    private ArrayList<Trip> trips;
    private boolean isWalking = false;

    private static final int EJECT_X_MARGIN = 50;
    private final Blood BLOOD;

    /**
     * Creates a driver instance with relative information
     *
     * @param x x-coordinate of the driver
     * @param y y-coordinate of the driver
     * @param healthPoints The health points of driver
     * @param damage The damage points it can impact on other entities
     * @param move_speed The speed that it should move in the first few frames during the collision timeout
     * @param radius The radius of the driver
     * @param gameProps The Game Property where stores essential details information
     */
    public Driver(int x, int y, double healthPoints, double damage, int move_speed,
                  double radius, Properties gameProps) {
        super(x, y, healthPoints, damage, move_speed, radius, gameProps);
        this.WALK_SPEED_X = Integer.parseInt(gameProps.getProperty("gameObjects.driver.walkSpeedX"));
        this.WALK_SPEED_Y = Integer.parseInt(gameProps.getProperty("gameObjects.driver.walkSpeedY"));
        IMAGE = new Image(gameProps.getProperty("gameObjects.driver.image"));

        trips = new ArrayList<Trip>();
        BLOOD = new Blood(x, y, gameProps);
    }

    /**
     * show the image of the driver on its current location
     */
    @Override
    public void draw() {
        if (!this.getDriverIsInTaxi()) {
            IMAGE.draw(this.getX(), this.getY());
        }
    }

    /**
     * update on the driver's walking status and location based on user's current input
     * @param input User's current input
     */
    @Override
    public void adjustToInputMovement(Input input) {

        if (input.isDown(Keys.RIGHT)) {
            this.setX(this.getX() + WALK_SPEED_X);
            this.isWalking = true;
        } else if (input.isDown(Keys.LEFT)) {
            this.setX(this.getX() - WALK_SPEED_X);
            this.isWalking = true;
        } else if (input.isDown(Keys.DOWN)) {
            this.setY(this.getY() + WALK_SPEED_Y);
            this.isWalking = true;
        } else if (input.isDown(Keys.UP)) {
            this.isWalking = true;
        }
        if (!input.isDown(Keys.RIGHT) && !input.isDown(Keys.LEFT) &&
                !input.isDown(Keys.DOWN) && !input.isDown(Keys.UP)) {
            this.isWalking = false;
        }

    }

    /**
     * Gets the driver's is walking status
     * @return true if the driver is walking out of the taxi; otherwise, false
     */
    public boolean getIsWalking() {
        return this.isWalking;
    }

    /**
     * Allocates the driver to its active taxi
     *
     * @param taxi the active taxi that belongs to the driver
     */
    public void collectTaxi(Taxi taxi) {
        this.taxi = taxi;
    }

    /**
     * Updates the driver's health points and other information when it receives damages during a collision
     *
     * @param damage The damage points received by the driver
     * @param onTop true if the enemy is the driver on top when the collision occurred; otherwise, false
     */
    @Override
    public void takeDamage(double damage, boolean onTop) {
        this.updateHealthPoints(damage);
        this.setCollisionTimeoutFrame(this.getCollisionTimeoutFrames() + 1);
        if (this.isDestroyed()) {
            BLOOD.setFramesActive(BLOOD.getFramesActive() + 1);
        }
        this.setCollisionOnTop(onTop);
    }

    /**
     * Gets the taxi that the driver is currently assigned to
     *
     * @return driver's current taxi
     */
    public Taxi getTaxi() { return this.taxi; }

    /**
     * returns the result of if the driver is moved closed enough to the taxi to get moved into it
     *
     * @return true if the driver is considered to be in the new taxi; otherwise, false
     */
    private boolean movedToNewTaxi() {
        int x1 = this.getX();
        int x2 = this.getTaxi().getX();
        int y1 = this.getY();
        int y2 = this.getTaxi().getY();
        double distance = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
        return distance < this.getRadius();
    }

    /**
     * update the driver based on the user's input and its other information
     *
     * @param input User's current input
     */
    public void update(Input input) {

        updateCollisionTimeout();
        setDriverIsInTaxi(this.movedToNewTaxi());

        // driver is not in the taxi and should move to the new one
        if(input != null && !this.getDriverIsInTaxi() && this.getCollisionTimeoutFrames() <= 0) {
            adjustToInputMovement(input);
        }

        if (trip != null && coinPower != null) {
            TravelPlan tp = trip.getPassenger().getTravelPlan();
            int newPriority = tp.getPriority();
            if(!tp.getCoinPowerApplied()) {
                newPriority = coinPower.applyEffect(tp.getPriority());
            }
            if(newPriority < tp.getPriority()) {
                tp.setCoinPowerApplied();
            }
            tp.setPriority(newPriority);
        }

        // move with the taxi when the driver is in the taxi
        if(this.getDriverIsInTaxi()) {
            this.setX(this.taxi.getX());
            this.setY(this.taxi.getY());
        }

        if(trip != null && trip.hasReachedEnd()) {
            getTrip().end();
        }
        BLOOD.update(this.getX(), this.getY());
        draw();

        // the flag of the current trip renders to the screen
        if(trips.size() > 0) {
            Trip lastTrip = trips.get(trips.size() - 1);
            if(!lastTrip.getPassenger().hasReachedFlag()) {
                lastTrip.getTripEndFlag().update(input);
            }
        }
    }

    /**
     * Sets a trip to the driver
     *
     * @param trip The trip assigned to the driver
     */
    public void setTrip(Trip trip) {
        this.trip = trip;
        if(trip != null) {
            trips.add(trip);
        }
    }

    /**
     * Gets the current trip of the driver
     *
     * @return driver's current trip
     */
    public Trip getTrip() {
        return this.trip;
    }

    /**
     * collect coin power for later uses
     * @param coin The coin being collected
     */
    public void collectCoinPower(Coin coin) {
        coinPower = coin;
    }

    /**
     * Gets driver's last trip
     *
     * @return driver's last trip
     */
    public Trip getLastTrip() {
        if(trips.size() <= 0) {
            return null;
        }
        return trips.get(trips.size() - 1);
    }

    /**
     * Gets driver's total earnings so far
     *
     * @return total earnings so far
     */
    public double calculateTotalEarnings() {
        double totalEarnings = 0;
        for(Trip trip : trips) {
            if (trip != null) {
                totalEarnings += trip.getFee();
            }
        }
        return totalEarnings;
    }

    /**
     * eject the driver from the taxi by changing its location and altering its is in taxi status
     */
    public void ejectFromTaxi() {
        if (this.getDriverIsInTaxi()) {
            this.setX(this.taxi.getX() - EJECT_X_MARGIN);
            this.setY(this.taxi.getY());
        }
        this.setDriverIsInTaxi(false);
    }

    /**
     * Gets the blood of the driver
     * @return the blood of the driver
     */
    public Blood getDriversBlood() {
        return this.BLOOD;
    }

}
