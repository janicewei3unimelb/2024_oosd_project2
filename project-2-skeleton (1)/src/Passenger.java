import bagel.Font;
import bagel.Image;
import bagel.Input;
import bagel.Keys;

import java.util.Properties;

/**
 * A class representing a passenger in the game with its relative essential information
 */
public class Passenger extends DamageableGameEntity {
    private final int WALK_SPEED_X;
    private final int WALK_SPEED_Y;
    private final TravelPlan TRAVEL_PLAN;

    private final int TAXI_DETECT_RADIUS;

    private final int PRIORITY_OFFSET;
    private final int EXPECTED_FEE_OFFSET;

    private int walkDirectionX;
    private int walkDirectionY;
    private boolean isGetInTaxi;
    private final boolean HAS_UMBRELLA;

    private final Image IMAGE;

    private int moveY;
    private Trip trip;
    private boolean reachedFlag;

    private static final int EJECT_X_MARGIN = 100;
    private final Blood BLOOD;

    /**
     * Creates a passenger instance for the game with its essential information
     *
     * @param x The x-coordinate of the passenger
     * @param y The y-coordinate of the passenger
     * @param healthPoints The health points of the passenger
     * @param damage The damage points it can give to other entities
     * @param move_speed The speed that the passenger moves during the first few frames in the timeout
     * @param radius The radius of the passenger
     * @param priority The initial priority of the passenger
     * @param endX The X coordinates of the destination of the passenger
     * @param distanceY The vertical distance that the passenger needs to travel for
     * @param hasUmbrella boolean result of whether the passenger has an umbrella
     * @param props The game property where stores essential information
     */
    public Passenger(int x, int y, double healthPoints, double damage, int move_speed,
                     double radius, int priority, int endX, int distanceY, boolean hasUmbrella, Properties props) {
        super(x, y, healthPoints, damage, move_speed, radius, props);
        this.WALK_SPEED_X = Integer.parseInt(props.getProperty("gameObjects.passenger.walkSpeedX"));
        this.WALK_SPEED_Y = Integer.parseInt(props.getProperty("gameObjects.passenger.walkSpeedY"));

        this.TRAVEL_PLAN = new TravelPlan(endX, distanceY, priority, props);
        this.TAXI_DETECT_RADIUS = Integer.parseInt(props.getProperty("gameObjects.passenger.taxiDetectRadius"));

        this.PRIORITY_OFFSET = 30;
        this.EXPECTED_FEE_OFFSET = 100;

        this.IMAGE = new Image(props.getProperty("gameObjects.passenger.image"));
        this.HAS_UMBRELLA = hasUmbrella;

        this.moveY = 0;
        BLOOD = new Blood(x, y, props);
    }

    /**
     * Gets the result of whether of not the passenger carries an umbrella
     *
     * @return true if the passenger has an umbrella; otherwise, false
     */
    public boolean getHasUmbrella(){
        return this.HAS_UMBRELLA;
    }

    /**
     * Gets the passenger's corresponding travel plan
     * @return the passenger's corresponding travel plan
     */
    public TravelPlan getTravelPlan() {
        return TRAVEL_PLAN;
    }

    /**
     * Gets the result of if the passenger is in the taxi
     * @return true if the passenger is in the taxi; otherwise, false
     */
    public boolean getIsGetInTaxi() {
        return this.isGetInTaxi;
    }

    /**
     * take damage from the other entities and update its information
     *
     * @param damage Damage points received
     * @param onTop true if the passenger is the entity on top when the collision occurred; otherwise, false
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
     * show the passenger's image on its current location
     */
    @Override
    public void draw() {
        IMAGE.draw(this.getX(), this.getY());
    }

    /**
     * updates the passenger's status and their behaviours
     * based on user's input and taxi's and driver's statuses
     *
     * @param input Current user's input
     * @param taxi the active taxi in the game
     * @param driver the driver in the game
     */
    public void update(Input input, Taxi taxi, Driver driver) {
        updateCollisionTimeout();

        if(!isGetInTaxi && trip == null) {
            drawPriority();
        }
        // if the passenger hasn't started the trip or the trip is completed,
        // update the passenger status based on keyboard
        // input. This means the passenger is go down when taxi moves up.
        // if the passenger is not in the taxi and there's no trip initiated, draw the priority number on the passenger.
        if(!isGetInTaxi || (trip != null && trip.isComplete())) {
            if(input != null) {
                adjustToInputMovement(input);
            }
            move();
            draw();
        }

        if (!isGetInTaxi && !driver.getDriverIsInTaxi() && trip != null && trip == driver.getTrip()
                && !trip.isComplete()) {
            // the previous taxi is damaged
            // both the driver and the passenger are ejected
            if (input != null) {
                moveWithDriver(driver);
            }
        } else if (!isGetInTaxi && driver.getDriverIsInTaxi() && trip != null && !trip.isComplete() &&
                trip == driver.getTrip()) {
            // passenger needs to gets in the taxi when the driver gets in
            this.setIsGetInTaxi();
        }

        if(adjacentToObject(taxi, driver) && !isGetInTaxi && trip == null && getDriverIsInTaxi()) {
            // if the passenger has not started the trip yet,
            // Taxi must be stopped in passenger's vicinity and not having another trip.
            setIsGetInTaxi(taxi);
            move(taxi);
        } else if(isGetInTaxi) {
            // if the passenger is in the taxi, initiate the trip and move the passenger along with the taxi.
            if(trip == null) {
                //Create new trip
                getTravelPlan().setEndY(this.getY());
                trip = new Trip(this, driver, this.getGameProps());
                driver.setTrip(trip);
            }
            move(taxi);
        } else if(!isGetInTaxi && trip != null && trip.isComplete()) {
            move(taxi);
            draw();
        }
        BLOOD.update(this.getX(), this.getY());
    }

    private void moveWithDriver(Driver driver) {
        if (driver.getIsWalking()) {
            this.setX(driver.getX());
            this.setY(driver.getY());
        }
    }

    /**
     * Draw the priority number on the passenger.
     */
    private void drawPriority() {
        Font font = new Font(this.getGameProps().getProperty("font"),
                Integer.parseInt(this.getGameProps().getProperty("gameObjects.passenger.fontSize")));
        font.drawString(String.valueOf(TRAVEL_PLAN.getPriority()), this.getX() - PRIORITY_OFFSET, this.getY());
        font.drawString(String.valueOf(TRAVEL_PLAN.getExpectedFee()), this.getX() - EXPECTED_FEE_OFFSET, this.getY());
    }

    /**
     * Adjust the movement direction in y-axis of the GameObject based on the keyboard input.
     * @param input The current mouse/keyboard input.
     */
    @Override
    public void adjustToInputMovement(Input input) {
        if (input.isDown(Keys.UP)) {
            moveY = 1;
        }  else if(input.wasReleased(Keys.UP)) {
            moveY = 0;
        }
    }

    private void move(Taxi taxi) {
        if (isGetInTaxi) {
            // if the passenger is in the taxi, move the passenger along with the taxi.
            moveWithTaxi(taxi);
        } else if(trip != null && trip.isComplete()) {
            // walk towards end flag if the trip is completed and not in the taxi.
            if(!hasReachedFlag()) {
                TripEndFlag tef = trip.getTripEndFlag();
                walkXDirectionObj(tef.getX());
                walkYDirectionObj(tef.getY());
                walk();
            }
        } else if (!isGetInTaxi && trip == null && !taxi.isMovingX() && !taxi.isMovingY()) {
            // Walk towards the taxi if other conditions are not met.
            // (That is when taxi is stopped with not having a trip and adjacent to the passenger and the passenger
            // hasn't initiated the trip yet.)
            walkXDirectionObj(taxi.getX());
            walkYDirectionObj(taxi.getY());
            walk();
        }
    }

    private void move() {
        this.setY(this.getY() + this.getScrollSpeedY() * moveY);
    }

    private void walk() {
        this.setX(this.getX() + WALK_SPEED_X * walkDirectionX);
        this.setY(this.getY() + WALK_SPEED_Y * walkDirectionY);
    }

    private void moveWithTaxi(Taxi taxi) {
        this.setX(taxi.getX());
        this.setY(taxi.getY());
    }

    /**
     * Determine the walk direction in x-axis of the passenger based on the x direction of the object.
     */
    private void walkXDirectionObj(int otherX) {
        if (otherX > this.getX()) {
            walkDirectionX = 1;
        } else if (otherX < this.getX()) {
            walkDirectionX = -1;
        } else {
            walkDirectionX = 0;
        }
    }

    /**
     * Determine the walk direction in y-axis of the passenger based on the x direction of the object.
     */
    private void walkYDirectionObj(int otherY) {
        if (otherY > this.getY()) {
            walkDirectionY = 1;
        } else if (otherY < this.getY()) {
            walkDirectionY = -1;
        } else {
            walkDirectionY = 0;
        }
    }

    /**
     * Check if the passenger has reached the end flag of the trip.
     * @return a boolean value indicating if the passenger has reached the end flag.
     */
    public boolean hasReachedFlag() {
        if(trip != null) {
            TripEndFlag tef = trip.getTripEndFlag();
            if(tef.getX() == this.getX() && tef.getY() == this.getY()) {
                reachedFlag = true;
            }
            return reachedFlag;
        }
        return false;
    }

    /**
     * Check if the taxi is adjacent to the passenger. This is evaluated based on multiple crietria.
     * @param taxi The active taxi in the game play.
     * @param driver The driver in the game play
     * @return a boolean value indicating if the taxi is adjacent to the passenger.
     */
    private boolean adjacentToObject(Taxi taxi, Driver driver) {
        // Check if Taxi is stopped and health > 0
        boolean taxiStopped = !taxi.isMovingX() && !taxi.isMovingY();
        // Check if Taxi is in the passenger's detect radius
        double currDistance = Math.sqrt(Math.pow(taxi.getX() - this.getX(), 2) +
                Math.pow(taxi.getY() - this.getY(), 2));
        // Check if Taxi is not having another trip
        boolean isHavingAnotherTrip = driver.getTrip() != null && driver.getTrip().getPassenger() != this;

        return currDistance <= TAXI_DETECT_RADIUS && taxiStopped && !isHavingAnotherTrip;
    }

    /**
     * Set the get in taxi status of the people object.
     * This is used to set an indication to check whether the people object is in the taxi or not.
     * @param taxi The taxi object to be checked. If it is null, the people object is not in a taxi at the moment in
     *             the game play.
     */
    public void setIsGetInTaxi(Taxi taxi) {
        if(taxi == null) {
            this.isGetInTaxi = false;
        } else if(taxi.getX() == this.getX() && taxi.getY() == this.getY()) {
            this.isGetInTaxi = true;
        }
    }

    /**
     * set result to the passenger is get in taxi status to be true
     */
    public void setIsGetInTaxi() {
        this.isGetInTaxi = true;
    }


    /**
     * eject the passenger from the damaged taxi by altering its location and status
     *
     * @param taxi The current active taxi in the game
     */
    public void ejectFromTaxi(Taxi taxi) {
        if (this.isGetInTaxi) {
            this.setX(taxi.getX() - EJECT_X_MARGIN);
            this.setY(taxi.getY());
        }
        setIsGetInTaxi(null);
    }

    /**
     * Gets passenger's blood
     *
     * @return Blood of the passenger
     */
    public Blood getPassengersBlood() {
        return this.BLOOD;
    }
}
