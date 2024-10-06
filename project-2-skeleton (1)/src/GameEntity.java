import bagel.*;

import java.util.Properties;

public abstract class GameEntity {
    private int x;
    private int y;
    private final Properties GAME_PROPS;
    private final int SCROLL_SPEED_Y;
    private boolean driverIsInTaxi = true;

    public GameEntity(int x, int y, Properties gameProps) {
        this.x = x;
        this.y = y;
        this.GAME_PROPS = gameProps;
        SCROLL_SPEED_Y = Integer.parseInt(gameProps.getProperty("gameObjects.taxi.speedY"));
    }

    public boolean getDriverIsInTaxi() {
        return this.driverIsInTaxi;
    }

    public void setDriverIsInTaxi(boolean update) {
        this.driverIsInTaxi = update;
    }

    public Properties getGameProps() {
        return this.GAME_PROPS;
    }

    public int getScrollSpeedY() { return this.SCROLL_SPEED_Y; };

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public abstract void draw();

    public abstract void adjustToInputMovement(Input input);

}
