import bagel.*;
import java.util.Properties;
import java.util.Random;

public class OtherCar extends DamageableGameEntity {
    private int speed_y;
    private final Image IMAGE;
    private static final int RANDOM_RANGE = 3;
    private static final int RANDOM_SHIFT = 2;


    public OtherCar(int x, int y, double healthPoints, double damage, int move_speed,
                    double radius, Properties gameProps) {
        super(x, y, healthPoints, damage, move_speed, radius, gameProps);
        Random rand = new Random();
        speed_y = rand.nextInt(RANDOM_RANGE + 1) + RANDOM_SHIFT;
        String imagePath = String.format(gameProps.getProperty("gameObjects.otherCar.image"),
                rand.nextInt(2) + 1);
        IMAGE = new Image(imagePath);
    }

    @Override
    public void draw() {
        IMAGE.draw(this.getX(), this.getY());
    }

    @Override
    public void adjustToInputMovement(Input input) {
        if (input.isDown(Keys.UP)) {
            this.setY(this.getY() + this.getScrollSpeedY());
        }
    }

    public void update(Input input) {
        if (this.getCollisionTimeout() >= getMaxTimeoutframes()) {
            this.setCollisionTimeout(0);
        }
        if (!this.isDestroyed()) {
            if (this.getCollisionTimeout() > 0) {
                this.setCollisionTimeout(this.getCollisionTimeout() + 1);
                if (this.getCollisionTimeout() < this.getMoveAwayTimeoutframes()) {
                    showCollisionEffect(this.getCollisionOnTop());
                }
            } else {
                this.setY(this.getY() - speed_y);
            }
            adjustToInputMovement(input);
            draw();
        } else {
            return;
        }
    }

    @Override
    public void takeDamage(double damage, boolean onTop) {
        double prevHealthPoints = this.getHealthPoints();
        updateHealthPoints(damage);

        if (this.getHealthPoints() < prevHealthPoints) {
            // there is a collision effect & reset speed
            this.setCollisionTimeout(this.getCollisionTimeout() + 1);
            Random rand = new Random();
            this.speed_y = rand.nextInt(RANDOM_RANGE) + RANDOM_SHIFT;
            this.setCollisionOnTop(onTop);
        }
    }


}
