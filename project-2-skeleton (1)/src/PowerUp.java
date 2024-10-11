import bagel.*;

import java.util.Properties;

/**
 * The parent of coins and invincible powers that can give effects on the entities of the game
 * for a period of time
 */
public abstract class PowerUp extends GameEntity {
    private final double RADIUS;
    private final int DURATION;
    private boolean isCollided;
    private int framesActive;
    private int moveY;

    /**
     * Creates a power up entity with its essential details
     *
     * @param x The x-coordinate of the power up entity
     * @param y The y-coordinate of the power up entity
     * @param radius The radius of the entity
     * @param duration The duration of the power up's active frames
     * @param gameProps The Game Property where stores essential information
     */
    public PowerUp(int x, int y, double radius, int duration, Properties gameProps) {
        super(x, y, gameProps);
        RADIUS = radius;
        DURATION = duration;
        isCollided = false;
        framesActive = 0;
        this.moveY = 0;
    }

    /**
     * Returns the result of whether the instance has collided with the driver
     *
     * @param driver Driver in the game
     * @return true if it has collided with the driver; otherwise, false
     */
    public boolean hasCollidedWith(Driver driver) {
        double collisionDistance = this.getRadius() + driver.getRadius();
        double currDistance = Math.sqrt(Math.pow(this.getX() - driver.getX(), 2) +
                Math.pow(this.getY() - driver.getY(), 2));
        return currDistance <= collisionDistance;
    }

    /**
     * Returns the result of whether the instance has collided with the taxi
     *
     * @param taxi active taxi in the game
     * @return true if it has collided with the taxi; otherwise, false
     */
    public boolean hasCollidedWith(Taxi taxi) {
        // if the distance between the two objects is less than the sum of their radius, they are collided
        double collisionDistance = this.getRadius() + taxi.getRadius();
        double currDistance = Math.sqrt(Math.pow(this.getX() - taxi.getX(), 2) +
                Math.pow(this.getY() - taxi.getY(), 2));
        return currDistance <= collisionDistance;
    }

    /**
     * update the power up based on user's input
     * @param input User's input
     */
    public abstract void update(Input input);

    /**
     * Gets the number of active frames
     *
     * @return the current number of frames being active
     */
    public int getFramesActive() {
        return this.framesActive;
    }

    /**
     * Sets the number of active frames
     *
     * @param result the new number of frames being active
     */
    public void setFramesActive(int result) {
        this.framesActive = result;
    }

    private int getSpeedY() { return this.getScrollSpeedY(); }

    /**
     * Gets the duration of the effect of the power up
     *
     * @return duration of the effect of the power up
     */
    public int getDuration() { return this.DURATION; }

    private double getRadius() {
        return this.RADIUS; }

    /**
     * Gets the result of whether the power up is collided or not
     *
     * @return true if is collided; otherwise, false
     */
    public boolean getIsCollided() {
        return this.isCollided;
    }

    /**
     * Sets the status of the power up to be 'collided'
     */
    public void setIsCollided() {
        this.isCollided = true;
    }

    /**
     * Gets the result of whether the power up is being active
     * @return true if the power up is collided with valid framesActive value; otherwise, false
     */
    public boolean getIsActive() {
        return isCollided && framesActive <= DURATION && framesActive > 0;
    }

    /**
     * move the entities on the vertical direction
     */
    public void move() {
        this.setY(this.getY() + this.getSpeedY() * moveY);
    }

    /**
     * updates the moveY value thus the vertical movement base on user's input
     * @param input User's input
     */
    public void adjustToInputMovement(Input input) {
        if (input.wasPressed(Keys.UP)) {
            moveY = 1;
        }  else if(input.wasReleased(Keys.UP)) {
            moveY = 0;
        }
    }

}
