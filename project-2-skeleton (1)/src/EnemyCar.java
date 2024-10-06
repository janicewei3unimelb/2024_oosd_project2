import bagel.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Properties;

public class EnemyCar extends DamageableGameEntity {
    private final Image IMAGE;
    private int speed_y;
    private final List<Fireball> FIREBALLS;
    private static final int FIREBALL_DIVISIBILITY = 300;
    private static final int RANDOM_BOUND = 1000;

    public EnemyCar(int x, int y, double healthPoints, double damage, int timeout_move_speed,
                    double radius, Properties gameProps) {
        super(x, y, healthPoints, damage, timeout_move_speed, radius, gameProps);
        Random rand = new Random();
        speed_y = rand.nextInt(4) + 2;
        IMAGE = new Image(gameProps.getProperty("gameObjects.enemyCar.image"));
        FIREBALLS = new ArrayList<>();
    }

    @Override
    public void takeDamage(double damage, boolean onTop) {
        double prevHealthPoints = this.getHealthPoints();
        updateHealthPoints(damage);

        if (this.getHealthPoints() < prevHealthPoints) {
            // there is a collision effect
            this.setCollisionTimeout(this.getCollisionTimeout() + 1);
            this.setCollisionOnTop(onTop);
        }
    }

    @Override
    public void draw() {
        IMAGE.draw(this.getX(), this.getY());
    }

    @Override
    public void adjustToInputMovement(Input input) {
        if (input.wasPressed(Keys.UP)) {
            this.setY(this.getY() + this.getScrollSpeedY());
        }
    }

    public void update(Input input) {
        if (this.getCollisionTimeout() >= getMaxTimeoutframes()) {
            this.setCollisionTimeout(0);
        }
        if (!this.isDestroyed()) {
            generateRandomFireballs();
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

    public void generateRandomFireballs() {
        Random random = new Random();
        int randomNum = random.nextInt(RANDOM_BOUND);
        if (randomNum % FIREBALL_DIVISIBILITY == 0) {
            FIREBALLS.add(new Fireball(this.getX(), this.getY(), this.getGameProps()));
        }
    }

    public List<Fireball> getFireBalls() {
        return this.FIREBALLS;
    }
}
