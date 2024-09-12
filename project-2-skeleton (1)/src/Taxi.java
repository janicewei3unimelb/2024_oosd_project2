import bagel.*;

import java.util.ArrayList;
import java.util.Properties;

public class Taxi extends GameEntities implements Damagable {

    private double healthPoints;
    private final double DAMAGE;
    private boolean isInvincible;

    private final double RADIUS;
    private final Image IMAGE;
    private final Image DAMAGEDIMAGE;

    private final int SPEEDX;

    private Trip trip;
    private ArrayList<Trip> trips;

    private boolean isMovingY;
    private boolean isMovingX;

    private Coin coinPower;
    private InvinciblePower invinciblePower;

    public Taxi(int x, int y, Properties gameProps) {
        super(x, y, gameProps);
        this.RADIUS = Double.parseDouble(gameProps.getProperty("gameObjects.taxi.radius"));
        this.IMAGE = new Image(gameProps.getProperty("gameObjects.taxi.image"));
        this.DAMAGEDIMAGE = new Image(gameProps.getProperty("gameObjects.taxi.damagedImage"));

        this.healthPoints = this.HEALTH_UNIT * Double.parseDouble(gameProps.getProperty("gameObjects.taxi.health"));
        this.DAMAGE = this.HEALTH_UNIT * Double.parseDouble(gameProps.getProperty("gameObjects.taxi.damage"));
        this.isInvincible = false;

        this.SPEEDX = Integer.parseInt(gameProps.getProperty("gameObjects.taxi.speedX"));

        trips = new ArrayList<Trip>();

    }

    public boolean isMovingY() {
        return isMovingY;
    }

    public boolean isMovingX() {
        return isMovingX;
    }

    public double getRadius() {return this.RADIUS; }

    @Override
    public void draw() {
        if (!this.isDestroyed()) {
            IMAGE.draw(this.getX(), this.getY());
        } else {
            DAMAGEDIMAGE.draw(this.getX(), this.getY());
        }
    }

    public void update(Input input) {
        if (trip != null && coinPower != null) {
            TravelPlan tp = trip.getPassenger().getTravelPlan();
            int newPriority = tp.getPriority();
            if(!tp.getCoinPowerApplied()) {
                newPriority = coinPower.applyEffect(tp.getPriority());
            }
            if(newPriority < tp.getPriority()) {
                tp.setCoinPowerApplied();
            }
            tp.setPriority(newPriority);
        }

        if(input != null) {
            adjustToInputMovement(input);
        }

        if(trip != null && trip.hasReachedEnd()) {
            getTrip().end();
        }

        this.draw();

        // the flag of the current trip renders to the screen
        if(trips.size() > 0) {
            Trip lastTrip = trips.get(trips.size() - 1);
            if(!lastTrip.isComplete()) {
                lastTrip.getTripEndFlag().update(input);
            }
        }
    }

    @Override
    public void takeDamage(double damage) {
        this.healthPoints -= damage;
    }

    @Override
    public boolean isDestroyed() {
        return (healthPoints <= 0);
    }

    public void collectCoinPower(Coin coin) {
        coinPower = coin;
    }

    public void collectInvinciblePower(InvinciblePower invinciblePower) {
        this.invinciblePower = invinciblePower;
    }

    public void adjustToInputMovement(Input input) {
        if (input.wasPressed(Keys.UP)) {
            isMovingY = true;
        }  else if(input.wasReleased(Keys.UP)) {
            isMovingY = false;
        } else if(input.isDown(Keys.LEFT)) {
            this.setX(this.getX() - SPEEDX);
            isMovingX = true;
        }  else if(input.isDown(Keys.RIGHT)) {
            this.setX(this.getX() + SPEEDX);
            isMovingX =  true;
        } else if(input.wasReleased(Keys.LEFT) || input.wasReleased(Keys.RIGHT)) {
            isMovingX = false;
        }
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
        if(trip != null) {
            trips.add(trip);
        }
    }

    public Trip getTrip() {
        return this.trip;
    }

    public Trip getLastTrip() {
        if(trips.size() <= 0) {
            return null;
        }
        return trips.get(trips.size() - 1);
    }

    public double calculateTotalEarnings() {
        double totalEarnings = 0;
        for(Trip trip : trips) {
            if (trip != null) {
                totalEarnings += trip.getFee();
            }
        }
        return totalEarnings;
    }
}
