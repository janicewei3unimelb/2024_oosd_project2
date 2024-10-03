import bagel.*;
import java.util.Properties;
import java.util.Random;

public class OtherCar extends DamageableGameEntity {
    private int speed_y;
    private final Image IMAGE;


    public OtherCar(int x, int y, double healthPoints, double damage, int move_speed,
                    double radius, Properties gameProps) {
        super(x, y, healthPoints, damage, move_speed, radius, gameProps);
        Random rand = new Random();
        speed_y = rand.nextInt(4) + 2;
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
        if (input.wasPressed(Keys.UP)) {
            this.setY(this.getY() + this.getScollSpeedY());
        }
    }

    public void update(Input input) {
        if (this.getHealthPoints() > 0) {
            if (this.getCollisionTimeout() > 0) {
                this.setCollisionTimeout(this.getCollisionTimeout() - 1);
                // Add logic for moving away during the first 10 frames
            } else {
                adjustToInputMovement(input);
                this.setY(this.getY() - speed_y);
            }
        }
        draw();
    }

    @Override
    public void takeDamage(double damage) {

    }


}
