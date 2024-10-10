import bagel.*;

import java.util.Properties;

/**
 * Class representing coins in the game. Coins can be collected by either the driver or the taxi.
 * It will set one level higher priority for the passengers that are waiting to get-in or already in the taxi.
 */
public class Coin extends PowerUp {
    private final Image IMAGE;

    /**
     * Creates a coin instance with its essential information
     *
     * @param x x-coordinate of the coin
     * @param y y-coordinate of the coin
     * @param radius radius of the coin for detecting collisions
     * @param duration duration of its effect
     * @param gameProps Game property where we can fetch essential information
     */
    public Coin(int x, int y, double radius, int duration, Properties gameProps) {
        super(x, y, radius, duration, gameProps);
        this.IMAGE = new Image(gameProps.getProperty("gameObjects.coin.image"));
    }

    /**
     * Shows the image of the coin on its current location
     */
    @Override
    public void draw() {
        IMAGE.draw(this.getX(), this.getY());
    }

    /**
     * Check if the coin has collided with the taxi, and power will be collected by the driver.
     */
    public void collide(Taxi taxi) {
        if(hasCollidedWith(taxi)) {
            taxi.getDriver().collectCoinPower(this);
            setIsCollided();
        }
    }

    /**
     * Check if the coin has collided with the driver, and power will be collected by the driver.
     */
    public void collide(Driver driver) {
        if (hasCollidedWith(driver)) {
            driver.collectCoinPower(this);
            setIsCollided();
        }
    }

    /**
     * Apply the effect of the coin on the priority of the passenger.
     * @param priority The current priority of the passenger.
     * @return The new priority of the passenger.
     */
    public Integer applyEffect(Integer priority) {
        if (this.getFramesActive() <= this.getDuration() && priority > 1) {
            priority -= 1;
        }
        return priority;
    }

    /**
     * Move the object in y direction according to the keyboard input, and render the coin image, before collision happens with PowerCollectable objects.
     * Once the collision happens, the coin active time will be increased.
     * @param input The current mouse/keyboard input.
     */
    @Override
    public void update(Input input) {
        if(this.getIsCollided()) {
            this.setFramesActive(this.getFramesActive() + 1);
        } else {
            if(input != null) {
                adjustToInputMovement(input);
            }

            move();
            draw();
        }
    }

}
