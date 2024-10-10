import bagel.*;

import java.util.Properties;

/**
 * a class representing game entities with x and y coordinates and information stored in the Game Property
 */
public abstract class GameEntity {
    private int x;
    private int y;
    private final Properties GAME_PROPS;
    private final int SCROLL_SPEED_Y;
    private static boolean driverIsInTaxi = true;

    /**
     * Creates a game entity with its general information
     *
     * @param x x-coordinates of the game entity
     * @param y y-coordinates of the game entity
     * @param gameProps Game Property where stores important information about the game entity
     */
    public GameEntity(int x, int y, Properties gameProps) {
        this.x = x;
        this.y = y;
        this.GAME_PROPS = gameProps;
        SCROLL_SPEED_Y = Integer.parseInt(gameProps.getProperty("gameObjects.taxi.speedY"));
    }

    /**
     * Gets the result of if the driver is in the taxi
     *
     * @return true if the driver is in the taxi; otherwise, false
     */
    public boolean getDriverIsInTaxi() {
        return driverIsInTaxi;
    }

    /**
     * update the driver is in taxi status by the boolean value given
     *
     * @param update the new driver is in taxi status that we want to set
     */
    public void setDriverIsInTaxi(boolean update) {
        driverIsInTaxi = update;
    }

    /**
     * Gets the Game Property of the game entity
     *
     * @return the Game Property
     */
    public Properties getGameProps() {
        return this.GAME_PROPS;
    }

    /**
     * Gets the vertical scrolling speed when the user pressed on the up arrow
     *
     * @return the defined scroll speed
     */
    public int getScrollSpeedY() { return this.SCROLL_SPEED_Y; };

    /**
     * Gets the x coordinate of the game entity
     *
     * @return x coordinate value
     */
    public int getX() {
        return x;
    }

    /**
     * Sets the x coordinate of the game entity
     *
     * @param x the x coordinate value that we want to set to the entity
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Gets the y coordinate of the game entity
     *
     * @return y coordinate value
     */
    public int getY() {
        return y;
    }

    /**
     * Sets the y coordinate of the game entity
     *
     * @param y the y coordinate value that we want to set to the entity
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * show the game entity's image
     */
    public abstract void draw();

}
