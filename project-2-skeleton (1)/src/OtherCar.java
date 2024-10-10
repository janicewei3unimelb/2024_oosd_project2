import bagel.*;
import java.util.Properties;
import java.util.Random;

/**
 * A class representing an other car that can have collisions with some entities in the game
 */
public class OtherCar extends DamageableGameEntity {
    private int speed_y;
    private final Image IMAGE;
    private static final int RANDOM_RANGE = 3;
    private static final int RANDOM_SHIFT = 2;
    private final Smoke SMOKE;
    private final Fire FIRE;

    /**
     * Creates a car with given details
     *
     * @param x The x-coordinate of the car
     * @param y The y-coordinate of the car
     * @param healthPoints The health points of the car
     * @param damage The damage points it can impact on other entities
     * @param move_speed The speed that it should move in the first 5 frames during the collision timeout
     * @param radius The radius of the car
     * @param gameProps The Game Property where we can fetch essential details
     */
    public OtherCar(int x, int y, double healthPoints, double damage, int move_speed,
                    double radius, Properties gameProps) {
        super(x, y, healthPoints, damage, move_speed, radius, gameProps);
        Random rand = new Random();
        speed_y = rand.nextInt(RANDOM_RANGE + 1) + RANDOM_SHIFT;
        String imagePath = String.format(gameProps.getProperty("gameObjects.otherCar.image"),
                rand.nextInt(2) + 1);
        IMAGE = new Image(imagePath);
        SMOKE = new Smoke(x, y, gameProps);
        FIRE = new Fire(x, y, gameProps);
    }

    /**
     * Show the image of the car on its current location
     */
    @Override
    public void draw() {
        IMAGE.draw(this.getX(), this.getY());
    }

    /**
     * Updates the car's location based on user's input
     *
     * @param input User's current input
     */
    @Override
    public void adjustToInputMovement(Input input) {
        if (input.isDown(Keys.UP)) {
            this.setY(this.getY() + this.getScrollSpeedY());
        }
    }

    /**
     * update the car based on the user's input and other statuses
     *
     * @param input User's current input
     */
    public void update(Input input) {
        updateCollisionTimeout();
        adjustToInputMovement(input);
        if (!this.isDestroyed()) {
            if (this.getCollisionTimeoutFrames() <=0 ) {
                this.setY(this.getY() - speed_y);
            }
            draw();
        } else {
            FIRE.setFramesActive(FIRE.getFramesActive() + 1);
        }
        SMOKE.update(this.getX(), this.getY());
        FIRE.update(this.getX(), this.getY());
    }

    /**
     * Updates the car's health points and other information when it receives damages during a collision
     *
     * @param damage The damage points it receives from other objects
     * @param onTop true if the car is the entity on top when the collision occurred; otherwise, false
     */
    @Override
    public void takeDamage(double damage, boolean onTop) {
        double prevHealthPoints = this.getHealthPoints();
        updateHealthPoints(damage);
        SMOKE.setFramesActive(SMOKE.getFramesActive() + 1);

        if (this.getHealthPoints() < prevHealthPoints) {
            // there is a collision effect & reset speed
            this.setCollisionTimeoutFrame(this.getCollisionTimeoutFrames() + 1);
            Random rand = new Random();
            this.speed_y = rand.nextInt(RANDOM_RANGE) + RANDOM_SHIFT;
            this.setCollisionOnTop(onTop);
        }
    }


}
