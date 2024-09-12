import bagel.*;

import java.util.Properties;

public class Driver extends GameEntities implements Damagable{
    private Taxi taxi;
    private boolean inTaxi;

    public Driver(int x, int y, Properties gameProps) {
        super(x, y, gameProps);
    }

    @Override
    public void draw() {

    }

    public void collectTaxi(Taxi taxi) {
        this.taxi = taxi;
    }

    @Override
    public void takeDamage(double damage) {

    }

    @Override
    public boolean isDestroyed() {
        return false;
    }
}
