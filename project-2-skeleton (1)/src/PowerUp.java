import bagel.*;

import java.util.Properties;

public abstract class PowerUp extends GameEntities{
    private final double RADIUS;
    private final int DURATION;
    private final int SPEED_Y;
    private boolean isCollided;
    private int framesActive;

    public PowerUp(int x, int y, double radius, int duration, Properties gameProps) {
        super(x, y, gameProps);
        RADIUS = radius;
        DURATION = duration;
        SPEED_Y = Integer.parseInt(gameProps.getProperty("gameObjects.taxi.speedY"));
        isCollided = false;
        framesActive = 0;
    }

    public abstract void collide(Taxi taxi);

    public boolean hasCollidedWith(Taxi taxi) {
        // if the distance between the two objects is less than the sum of their radius, they are collided
        double collisionDistance = this.getRadius() + taxi.getRadius();
        double currDistance = (double) Math.sqrt(Math.pow(this.getX() - taxi.getX(), 2) +
                Math.pow(this.getY() - taxi.getY(), 2));
        return currDistance <= collisionDistance;
    }

    public int getFramesActive() {
        return this.framesActive;
    }

    public void setFramesActive(int result) {
        this.framesActive = result;
    }

    public int getSpeedY() { return this.SPEED_Y; }

    public int getDuration() { return this.DURATION; }

    public double getRadius() { return this.RADIUS; }

    public void setIsCollided(boolean result) {
        this.isCollided = result;
    }

    public boolean getIsCollided() {
        return this.isCollided;
    }

    public void setIsCollided() {
        this.isCollided = true;
    }

    public boolean getIsActive() {
        return isCollided && framesActive <= DURATION && framesActive > 0;
    }

}
