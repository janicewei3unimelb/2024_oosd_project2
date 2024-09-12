import bagel.*;

import java.util.Properties;

public abstract class GameEntities {
    private int x;
    private int y;
    private final Properties GAME_PROPS;

    public GameEntities(int x, int y, Properties gameProps) {
        this.x = x;
        this.y = y;
        this.GAME_PROPS = gameProps;
    }

    public Properties getGameProps() {
        return this.GAME_PROPS;
    }

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

}
