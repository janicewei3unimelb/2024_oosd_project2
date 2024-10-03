import bagel.*;

import java.util.ArrayList;
import java.util.Properties;

public class Driver extends DamageableGameEntity {
    private Taxi taxi;
    private final int WALK_SPEED_X;
    private final int WALK_SPEED_Y;
    private boolean isInTaxi;
    private final Image IMAGE;

    private Coin coinPower;
    private Trip trip;
    private ArrayList<Trip> trips;

    public Driver(int x, int y, double healthPoints, double damage, int move_speed,
                  double radius, Properties gameProps) {
        super(x, y, healthPoints, damage, move_speed, radius, gameProps);
        this.WALK_SPEED_X = Integer.parseInt(gameProps.getProperty("gameObjects.driver.walkSpeedX"));
        this.WALK_SPEED_Y = Integer.parseInt(gameProps.getProperty("gameObjects.driver.walkSpeedY"));
        isInTaxi = true;
        IMAGE = new Image(gameProps.getProperty("gameObjects.driver.image"));

        trips = new ArrayList<Trip>();
    }

    @Override
    public void draw() {
        if (!this.isInTaxi) {
            IMAGE.draw(this.getX(), this.getY());
        }
    }

    @Override
    public void adjustToInputMovement(Input input) {

    }

    public void collectTaxi(Taxi taxi) {
        this.taxi = taxi;
    }

    @Override
    public void takeDamage(double damage) {

    }

    public Taxi getTaxi() { return this.taxi; }

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

        if(input != null && !this.isInTaxi) {
            adjustToInputMovement(input);
        }

        if(this.isInTaxi) {
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

    public boolean getIsInTaxi() {
        return this.isInTaxi;
    }

    public void collectCoinPower(Coin coin) {
        coinPower = coin;
    }

    public void transferTrip(Trip newTrip) {
        this.trips.add(newTrip);
        this.trip = newTrip;
    }

    public void deleteLastTrip() {
        this.trips.remove(this.trips.size() - 1);
        this.trips.trimToSize();
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
