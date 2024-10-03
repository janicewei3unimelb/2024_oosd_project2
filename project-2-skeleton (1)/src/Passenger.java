import bagel.Font;
import bagel.Image;
import bagel.Input;
import bagel.Keys;

import java.util.Properties;

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
    }

    public boolean getHasUmbrella(){
        return this.HAS_UMBRELLA;
    }

    public TravelPlan getTravelPlan() {
        return TRAVEL_PLAN;
    }

    public boolean getIsGetInTaxi() {
        return this.isGetInTaxi;
    }


    @Override
    public void takeDamage(double damage) {

    }


    @Override
    public void draw() {
        IMAGE.draw(this.getX(), this.getY());
    }

    public void update(Input input, Taxi taxi, Driver driver) {
        // if the passenger is not in the taxi or the trip is completed,
        // update the passenger status based on keyboard
        // input. This means the passenger is go down when taxi moves up.
        if(!isGetInTaxi || (trip != null && trip.isComplete())) {
            if(input != null) {
                adjustToInputMovement(input);
            }
            move();
            draw();
        }

        // if the passenger is not in the taxi and there's no trip initiated, draw the priority number on the passenger.
        if(!isGetInTaxi && trip == null) {
            drawPriority();
        }

        if(adjacentToObject(taxi, driver) && !isGetInTaxi && trip == null) {
            // if the passenger has not started the trip yet,
            // Taxi must be stopped in passenger's vicinity and not having another trip.
            setIsGetInTaxi(taxi);
            move(taxi);
        } else if(isGetInTaxi) {
            // if the passenger is in the taxi, initiate the trip and move the passenger along with the taxi.
            if(trip == null) {
                //Create new trip
                getTravelPlan().setStartY(this.getY());
                trip = new Trip(this, driver, this.getGameProps());
                driver.setTrip(trip);
            }

            move(taxi);
            draw();

        } else if(!isGetInTaxi && trip != null && trip.isComplete()) {
            move(taxi);
            draw();
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
        if (input.wasPressed(Keys.UP)) {
            moveY = 1;
        }  else if(input.wasReleased(Keys.UP)) {
            moveY = 0;
        }
    }

    private void move(Taxi taxi) {
        if (isGetInTaxi && !this.reachedFlag) {
            // if the passenger is in the taxi, move the passenger along with the taxi.
            moveWithTaxi(taxi);
        } else if(!isGetInTaxi && trip != null && trip.isComplete()) {
            // walk towards end flag if the trip is completed and not in the taxi.
            if(!hasReachedFlag()) {
                TripEndFlag tef = trip.getTripEndFlag();
                walkXDirectionObj(tef.getX());
                walkYDirectionObj(tef.getY());
                walk();
            }
        } else {
            // Walk towards the taxi if other conditions are not met.
            // (That is when taxi is stopped with not having a trip and adjacent to the passenger and the passenger
            // hasn't initiated the trip yet.)
            walkXDirectionObj(taxi.getX());
            System.out.println("walk Direction X: " + walkDirectionX);
            walkYDirectionObj(taxi.getY());
            System.out.println("walk Direction Y: " + walkDirectionY);
            walk();
        }
    }

    private void move() {

        this.setY(this.getY() + this.getScollSpeedY() * moveY);
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
            isGetInTaxi = false;
        } else if(taxi.getX() == this.getX() && taxi.getY() == this.getY()) {
            isGetInTaxi = true;
        }
    }
}
