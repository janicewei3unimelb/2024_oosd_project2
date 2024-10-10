import bagel.*;
import java.util.Properties;

/**
 * Class representing invincible powers in the game. They can be collected by either the driver or the taxi.
 * It will avoid the driver and its taxi from damages for 1000 frames
 */
public class InvinciblePower extends PowerUp {
    private final Image IMAGE;

    /**
     * Creates a invincible power instance with its essential information
     *
     * @param x x-coordinate of the power
     * @param y y-coordinate of the power
     * @param radius radius of the power for detecting collisions
     * @param duration duration of its effect
     * @param gameProps Game property where we can fetch essential information
     */
    public InvinciblePower(int x, int y, double radius, int duration, Properties gameProps) {
        super(x, y, radius, duration, gameProps);
        IMAGE = new Image(gameProps.getProperty("gameObjects.invinciblePower.image"));
    }

    /**
     * update the invincible power's status based on the user input
     *
     * @param input User's current input
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

    /**
     * Check if the power has collided with the taxi.
     */
    public void collide(Taxi taxi) {
        if (hasCollidedWith(taxi)) {
            setIsCollided();
        }
    }

    /**
     * Check if the coin has collided with the driver.
     */
    public void collide(Driver driver) {
        if (hasCollidedWith(driver)) {
            setIsCollided();
        }
    }

    /**
     * show the power's image on its current location
     */
    @Override
    public void draw() {
        IMAGE.draw(this.getX(), this.getY());
    }

}
