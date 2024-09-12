import bagel.*;

import java.util.Properties;

public class Coin extends PowerUp {
    private final Image IMAGE;
    private int moveY;

    public Coin(int x, int y, double radius, int duration, Properties gameProps) {
        super(x, y, radius, duration, gameProps);
        this.moveY = 0;
        this.IMAGE = new Image(gameProps.getProperty("gameObjects.coin.image"));
    }

    @Override
    public void draw() {
        IMAGE.draw(this.getX(), this.getY());
    }

    @Override
    /**
     * Check if the coin has collided with any PowerCollectable objects, and power will be collected by PowerCollectable
     * object that is collided with.
     */
    public void collide(Taxi taxi) {
        if(hasCollidedWith(taxi)) {
            taxi.collectCoinPower(this);
            setIsCollided();
        }
    }

    /**
     * Apply the effect of the coin on the priority of the passenger.
     * @param priority The current priority of the passenger.
     * @return The new priority of the passenger.
     */
    public Integer applyEffect(Integer priority) {
        if (this.getFramesActive() <= this.getDuration() && priority > 1) {
            priority -= 1;
        }
        return priority;
    }

    /**
     * Move the object in y direction according to the keyboard input, and render the coin image, before collision happens with PowerCollectable objects.
     * Once the collision happens, the coin active time will be increased.
     * @param input The current mouse/keyboard input.
     */
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

    /**
     * Move the GameObject object in the y-direction based on the speedY attribute.
     */
    public void move() {
        this.setY(this.getY() + this.getSpeedY() * moveY);
    }

    public void adjustToInputMovement(Input input) {
        if (input.wasPressed(Keys.UP)) {
            moveY = 1;
        }  else if(input.wasReleased(Keys.UP)) {
            moveY = 0;
        }
    }

}
