import bagel.*;
import java.util.Properties;

public class InvinciblePower extends PowerUp {
    private final Image IMAGE;
    public InvinciblePower(int x, int y, double radius, int duration, Properties gameProps) {
        super(x, y, radius, duration, gameProps);
        IMAGE = new Image(gameProps.getProperty("gameObjects.invinciblePower.image"));
    }

    @Override
    public void collide(Taxi taxi) {

    }

    @Override
    public void draw() {
        IMAGE.draw(this.getX(), this.getY());
    }

    public boolean hasCollideWith(Driver driver) {
        return false;
    }
}
