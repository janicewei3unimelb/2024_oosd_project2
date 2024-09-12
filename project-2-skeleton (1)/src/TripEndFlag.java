import bagel.Image;
import bagel.Input;
import bagel.Keys;

import java.util.Properties;

public class TripEndFlag extends GameEntities{

    private final Image IMAGE;
    private final int SPEED_Y;
    private final float RADIUS;
    private int moveY;

    public TripEndFlag(int x, int y, Properties props) {
        super(x, y, props);
        this.SPEED_Y = Integer.parseInt(props.getProperty("gameObjects.taxi.speedY"));
        this.RADIUS = Float.parseFloat(props.getProperty("gameObjects.tripEndFlag.radius"));
        this.IMAGE = new Image(props.getProperty("gameObjects.tripEndFlag.image"));
        this.moveY = 0;
    }

    public float getRadius() {
        return RADIUS;
    }

    @Override
    public void draw() {
        IMAGE.draw(this.getX(), this.getY());
    }

    public void update(Input input) {
        if(input != null) {
            adjustToInputMovement(input);
        }

        move();
        draw();
    }

    public void move() {
        this.setY(this.getY() + SPEED_Y * moveY);
    }

    public void adjustToInputMovement(Input input) {
        if (input.wasPressed(Keys.UP)) {
            moveY = 1;
        }  else if(input.wasReleased(Keys.UP)) {
            moveY = 0;
        }
    }
}
