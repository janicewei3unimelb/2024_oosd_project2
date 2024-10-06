import bagel.*;

import java.util.ArrayList;
import java.util.Properties;

public class Driver extends DamageableGameEntity {
    private Taxi taxi;
    private final int WALK_SPEED_X;
    private final int WALK_SPEED_Y;
    private final Image IMAGE;

    private Coin coinPower;
    private Trip trip;
    private ArrayList<Trip> trips;

    private static final int EJECT_X_MARGIN = 50;

    public Driver(int x, int y, double healthPoints, double damage, int move_speed,
                  double radius, Properties gameProps) {
        super(x, y, healthPoints, damage, move_speed, radius, gameProps);
        this.WALK_SPEED_X = Integer.parseInt(gameProps.getProperty("gameObjects.driver.walkSpeedX"));
        this.WALK_SPEED_Y = Integer.parseInt(gameProps.getProperty("gameObjects.driver.walkSpeedY"));
        IMAGE = new Image(gameProps.getProperty("gameObjects.driver.image"));

        trips = new ArrayList<Trip>();
    }

    @Override
    public void draw() {
        if (!this.getDriverIsInTaxi()) {
            IMAGE.draw(this.getX(), this.getY());
        }
    }

    @Override
    public void adjustToInputMovement(Input input) {

        if (input.isDown(Keys.RIGHT)) {
            this.setX(this.getX() + WALK_SPEED_X);
        } else if (input.isDown(Keys.LEFT)) {
            this.setX(this.getX() - WALK_SPEED_X);
        } else if (input.isDown(Keys.DOWN)) {
            this.setY(this.getY() + WALK_SPEED_Y);
        }

    }

    public void collectTaxi(Taxi taxi) {
        this.taxi = taxi;
    }

    @Override
    public void takeDamage(double damage, boolean onTop) {
        this.updateHealthPoints(damage);
        this.setCollisionTimeout(this.getCollisionTimeout() + 1);
        this.setCollisionOnTop(onTop);
    }

    public Taxi getTaxi() { return this.taxi; }

    public boolean movedToNewTaxi() {
        int x1 = this.getX();
        int x2 = this.getTaxi().getX();
        int y1 = this.getY();
        int y2 = this.getTaxi().getY();
        double distance = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
        return distance < this.getRadius();
    }

    public void update(Input input) {

        if (this.getCollisionTimeout() >= this.getMaxTimeoutframes()) {
            this.setCollisionTimeout(0);
        }
        if (this.getCollisionTimeout() > 0) {
            this.setCollisionTimeout(this.getCollisionTimeout() + 1);
            if (this.getCollisionTimeout() < this.getMoveAwayTimeoutframes()) {
                showCollisionEffect(this.getCollisionOnTop());
            }
        }

        setDriverIsInTaxi(this.movedToNewTaxi());

        if(input != null && !this.getDriverIsInTaxi() && this.getCollisionTimeout() <= 0) {
            adjustToInputMovement(input);
        }

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

        if(this.getDriverIsInTaxi()) {
            this.setX(this.taxi.getX());
            this.setY(this.taxi.getY());
        }

        if(trip != null && trip.hasReachedEnd()) {
            getTrip().end();
        }

        this.draw();

        // the flag of the current trip renders to the screen
        if(trips.size() > 0) {
            Trip lastTrip = trips.get(trips.size() - 1);
            if(!lastTrip.getPassenger().hasReachedFlag()) {
                lastTrip.getTripEndFlag().update(input);
            }
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

    public void collectCoinPower(Coin coin) {
        coinPower = coin;
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

    public void ejectFromTaxi() {
        if (this.getDriverIsInTaxi()) {
            System.out.println("ejected!");
            this.setX(this.taxi.getX() - EJECT_X_MARGIN);
            this.setY(this.taxi.getY());
        }
        this.setDriverIsInTaxi(false);

    }

}
