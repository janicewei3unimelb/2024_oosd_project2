import bagel.Image;
import bagel.Input;
import bagel.Keys;

import java.util.Properties;

/**
 * A class representing the trip end flag in the game play.
 * Objects of this class will only move up and down based on the keyboard input. No other functionalities needed.
 */
public class TripEndFlag extends GameEntity {

    private final Image IMAGE;
    private final int SPEED_Y;
    private final double RADIUS;
    private int moveY;

    /**
     * Creates a trip end flag with basic information
     *
     * @param x The x-coordinate of the trip end flag
     * @param y The y-coordinate of the trip end flag
     * @param props The Game Property where we can fetch essential information
     */
    public TripEndFlag(int x, int y, Properties props) {
        super(x, y, props);
        this.SPEED_Y = Integer.parseInt(props.getProperty("gameObjects.taxi.speedY"));
        this.RADIUS = Float.parseFloat(props.getProperty("gameObjects.tripEndFlag.radius"));
        this.IMAGE = new Image(props.getProperty("gameObjects.tripEndFlag.image"));
        this.moveY = 0;
    }

    /**
     * Gets the radius of the trip end flag in order to detect if the trip should be finished.
     *
     * @return radius of the trip end flag
     */
    public double getRadius() {
        return RADIUS;
    }

    /**
     * show the image of the trip end flag on its current location
     */
    @Override
    public void draw() {
        IMAGE.draw(this.getX(), this.getY());
    }

    /**
     * update the trip end flag based on the player's input
     *
     * @param input The input entered by the player
     */
    public void update(Input input) {
        if(input != null) {
            adjustToInputMovement(input);
        }
        move();
        draw();
    }

    private void move() {
        this.setY(this.getY() + SPEED_Y * moveY);
    }

    /**
     * respond to users' input
     */
    private void adjustToInputMovement(Input input) {
        if (input.wasPressed(Keys.UP)) {
            moveY = 1;
        }  else if(input.wasReleased(Keys.UP)) {
            moveY = 0;
        }
    }
}
