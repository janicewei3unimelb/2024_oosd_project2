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
    public void takeDamage(double damage) {

    }

    @Override
    public void draw() {
        IMAGE.draw(this.getX(), this.getY());
    }

    @Override
    public void adjustToInputMovement(Input input) {
        if (input.wasPressed(Keys.UP)) {
            this.setY(this.getY() + this.getScollSpeedY());
        }
    }

    public void update(Input input) {
        if (this.getHealthPoints() > 0) {
            generateRandomFireballs();
            if (this.getCollisionTimeout() > 0) {
                this.setCollisionTimeout(this.getCollisionTimeout() - 1);
                // Add logic for moving away during the first 10 frames
            } else {
                adjustToInputMovement(input);
                this.setY(this.getY() - speed_y);
            }
            draw();
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
