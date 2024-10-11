import bagel.*;
import java.util.Properties;

/**
 * The class representing the taxis in the game play
 */
public class Taxi extends DamageableGameEntity {

    private final Image IMAGE;
    private final Image DAMAGEDIMAGE;

    private final int SPEEDX;

    private boolean isMovingY;
    private boolean isMovingX;

    private Driver driver;
    private final Smoke SMOKE;
    private final Fire FIRE;

    /**
     * Creates a taxi with given details
     *
     * @param x The x-coordinate of the taxi
     * @param y The y-coordinate of the taxi
     * @param healthPoints The health points of taxi
     * @param damage The damage points it can impact on other entities
     * @param timeout_move_speed The speed that it should move in the first few frames during the collision timeout
     * @param radius The radius of the taxi
     * @param gameProps The Game Property where stores essential details information
     */
    public Taxi(int x, int y, double healthPoints, double damage, int timeout_move_speed,
                double radius, Properties gameProps) {
        super(x, y, healthPoints, damage, timeout_move_speed, radius, gameProps);
        this.IMAGE = new Image(gameProps.getProperty("gameObjects.taxi.image"));
        this.DAMAGEDIMAGE = new Image(gameProps.getProperty("gameObjects.taxi.damagedImage"));

        this.SPEEDX = Integer.parseInt(gameProps.getProperty("gameObjects.taxi.speedX"));
        SMOKE = new Smoke(x, y, gameProps);
        FIRE = new Fire(x, y, gameProps);
    }

    /**
     * Gets the driver of the taxi
     *
     * @return the driver who drives the taxi
     */
    public Driver getDriver() {
        return this.driver;
    }

    /**
     * Sets the driver of the taxi
     *
     * @param Driver Driver who drives the taxi
     */
    public void setDriver(Driver Driver) {
        this.driver = Driver;
    }

    /**
     * Gets the result of whether the taxi is moving in the y direction
     *
     * @return returns true if the taxi is moving in the vertical direction; otherwise, false
     */
    public boolean isMovingY() {
        return isMovingY;
    }

    /**
     * Gets the result of whether the taxi is moving in the x direction
     *
     * @return returns true if the taxi is moving in the horizontal direction; otherwise, false
     */
    public boolean isMovingX() {
        return isMovingX;
    }

    /**
     * Show the image of the taxi depends on its status (damaged or not)
     */
    @Override
    public void draw() {
        if (!this.isDestroyed()) {
            IMAGE.draw(this.getX(), this.getY());
        } else {
            DAMAGEDIMAGE.draw(this.getX(), this.getY());
        }
    }

    /**
     * update the taxi based on its status and player's input
     *
     * @param input Input entered by the player
     */
    public void update(Input input) {
        updateCollisionTimeout();

        if (this.isDestroyed()) {
            // destroyed taxis can only respond on users' up arrow keys
            moveAway(input);
        } else if (!this.isDestroyed() && this.getDriver().getDriverIsInTaxi() && this.getCollisionTimeoutFrames() <= 0) {
            adjustToInputMovement(input);
        } else if (!this.isDestroyed() && !this.getDriver().getDriverIsInTaxi()) {
            // the taxi is a randomly generated new one without a driver driving it
            moveAway(input);
        }
        if (this.isDestroyed()) {
            FIRE.setFramesActive(FIRE.getFramesActive() + 1);
        }
        this.draw();
        SMOKE.update(this.getX(), this.getY());
        FIRE.update(this.getX(), this.getY());
    }

    /** move vertically down when the up arrow is pressed
     *
     * @param input User's input
     */
    private void moveAway(Input input) {
        if (input.isDown(Keys.UP)) {
            this.setY(this.getY() + this.getScrollSpeedY());
        }
    }

    /**
     * Updates the taxi's health points and other information when it receives damages during a collision
     *
     * @param damage The damage points received by the taxi
     * @param onTop true if the taxi is the entity on top when the collision occurred; otherwise, false
     */
    @Override
    public void takeDamage(double damage, boolean onTop) {
        updateHealthPoints(damage);
        SMOKE.setFramesActive(SMOKE.getFramesActive() + 1);
        this.setCollisionTimeoutFrame(this.getCollisionTimeoutFrames() + 1);
        this.setCollisionOnTop(onTop);
    }

    /**
     * Update the taxi based on player's input
     *
     * @param input The player's entered input
     */
    @Override
    public void adjustToInputMovement(Input input) {

        if (input.wasPressed(Keys.UP)) {
            isMovingY = true;
        }  else if(input.wasReleased(Keys.UP)) {
            isMovingY = false;
        } else if(input.isDown(Keys.LEFT)) {
            this.setX(this.getX() - SPEEDX);
            isMovingX = true;
        }  else if(input.isDown(Keys.RIGHT)) {
            this.setX(this.getX() + SPEEDX);
            isMovingX =  true;
        } else if(input.wasReleased(Keys.LEFT) || input.wasReleased(Keys.RIGHT)) {
            isMovingX = false;
        }
    }

}
