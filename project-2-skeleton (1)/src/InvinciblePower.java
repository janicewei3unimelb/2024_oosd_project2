import bagel.*;
import java.util.Properties;

public class InvinciblePower extends PowerUp {
    private final Image IMAGE;

    public InvinciblePower(int x, int y, double radius, int duration, Properties gameProps) {
        super(x, y, radius, duration, gameProps);
        IMAGE = new Image(gameProps.getProperty("gameObjects.invinciblePower.image"));
    }

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

    public void collide(Taxi taxi) {
        if (hasCollidedWith(taxi)) {
            setIsCollided();
        }
    }

    public void collide(Driver driver) {
        if (hasCollidedWith(driver)) {
            setIsCollided();
        }
    }

    @Override
    public void draw() {
        IMAGE.draw(this.getX(), this.getY());
    }

}
