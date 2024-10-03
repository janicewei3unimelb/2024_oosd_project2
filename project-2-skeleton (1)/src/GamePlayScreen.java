import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import bagel.*;

public class GamePlayScreen extends Screen {
    private final Properties GAME_PROPS;
    private final Properties MSG_PROPS;

    private int currFrame = 0;
    private double coinFramesActive;
    private double invincibleFramesActive;
    private final int MAX_FRAMES;

    private static final int RANDOM_BOUND = 1000;
    private static final int CAR_DIVISIBILITY = 200;
    private static final int ENEMY_DIVISIBILITY = 400;
    private static final int RANDOM_Y1 = -50;
    private static final int RANDOM_Y2 = 768;

    private double minPassengerHealth;
    private static final int VEHICLE_TIMEOUT_SPEED = 1;
    private static final int PERSON_TIMEOUT_SPEED = 2;

    // Weather condition management
    private final List<Weather> WEATHERCONDITIONS;
    private Weather currentWeather;

    private GameBackground background;

    private double totalEarnings;
    private final double TARGET;

    private final String PLAYER_NAME;
    private boolean savedData;

    private Taxi taxi;
    private final List<Taxi> DAMAGED_TAXIS;
    private Driver driver;
    private final List<Passenger> PASSENGERS;
    private final List<Coin> COINS;
    private final List<InvinciblePower> INVINCIBLEPOWERS;
    private final List<OtherCar> CARS;
    private final List<EnemyCar> ENEMIES;

    // display text vars
    private final Font INFO_FONT;
    private final int EARNINGS_Y;
    private final int EARNINGS_X;
    private final int COIN_X;
    private final int COIN_Y;
    private final int TARGET_X;
    private final int TARGET_Y;
    private final int MAX_FRAMES_X;
    private final int MAX_FRAMES_Y;

    private final int TRIP_INFO_X;
    private final int TRIP_INFO_Y;
    private final int TRIP_INFO_OFFSET_1;
    private final int TRIP_INFO_OFFSET_2;
    private final int TRIP_INFO_OFFSET_3;

    public GamePlayScreen(Properties gameProps, Properties msgProps, String playerName) {
        this.GAME_PROPS = gameProps;
        this.MSG_PROPS = msgProps;

        PASSENGERS = new ArrayList<>();
        COINS = new ArrayList<>();
        INVINCIBLEPOWERS = new ArrayList<>();
        CARS = new ArrayList<>();
        DAMAGED_TAXIS = new ArrayList<>();
        ENEMIES = new ArrayList<>();

        ArrayList<String[]> lines = IOUtils.readCommaSeperatedFile(gameProps.getProperty("gamePlay.objectsFile"));
        populateGameObjects(lines);

        this.MAX_FRAMES = Integer.parseInt(gameProps.getProperty("gamePlay.maxFrames"));

        this.TARGET = Double.parseDouble(gameProps.getProperty("gamePlay.target"));

        WEATHERCONDITIONS = new ArrayList<>();
        loadWeather(gameProps.getProperty("gamePlay.weatherFile"));

        background = new GameBackground(gameProps);

        // display text vars
        INFO_FONT = new Font(gameProps.getProperty("font"),
                Integer.parseInt(gameProps.getProperty("gamePlay.info.fontSize")));
        EARNINGS_Y = Integer.parseInt(gameProps.getProperty("gamePlay.earnings.y"));
        EARNINGS_X = Integer.parseInt(gameProps.getProperty("gamePlay.earnings.x"));
        COIN_X = Integer.parseInt(gameProps.getProperty("gameplay.coin.x"));
        COIN_Y = Integer.parseInt(gameProps.getProperty("gameplay.coin.y"));
        TARGET_X = Integer.parseInt(gameProps.getProperty("gamePlay.target.x"));
        TARGET_Y = Integer.parseInt(gameProps.getProperty("gamePlay.target.y"));
        MAX_FRAMES_X = Integer.parseInt(gameProps.getProperty("gamePlay.maxFrames.x"));
        MAX_FRAMES_Y = Integer.parseInt(gameProps.getProperty("gamePlay.maxFrames.y"));

        // current trip info vars
        TRIP_INFO_X = Integer.parseInt(gameProps.getProperty("gamePlay.tripInfo.x"));
        TRIP_INFO_Y = Integer.parseInt(gameProps.getProperty("gamePlay.tripInfo.y"));
        TRIP_INFO_OFFSET_1 = 30;
        TRIP_INFO_OFFSET_2 = 60;
        TRIP_INFO_OFFSET_3 = 90;

        this.PLAYER_NAME = playerName;
    }

    public void populateGameObjects(ArrayList<String[]> lines) {
        for (String[] line : lines) {
            String type = line[0];
            int x = Integer.parseInt(line[1]);
            int y = Integer.parseInt(line[2]);
            double healthPoints;
            double damage;
            double radius;
            switch (type) {
                case "TAXI":
                    healthPoints = DamageableGameEntity.getHealthUnit() *
                            Double.parseDouble(GAME_PROPS.getProperty("gameObjects.taxi.health"));
                    damage = DamageableGameEntity.getHealthUnit() *
                            Double.parseDouble(GAME_PROPS.getProperty("gameObjects.taxi.damage"));
                    radius = Double.parseDouble(GAME_PROPS.getProperty("gameObjects.taxi.radius"));
                    taxi = new Taxi(x, y, healthPoints, damage, VEHICLE_TIMEOUT_SPEED, radius, GAME_PROPS);
                    break;
                case "DRIVER":
                    healthPoints = DamageableGameEntity.getHealthUnit() *
                            Double.parseDouble(GAME_PROPS.getProperty("gameObjects.driver.health"));
                    damage = 0;
                    radius = Double.parseDouble(GAME_PROPS.getProperty("gameObjects.driver.radius"));
                    driver = new Driver(x, y, healthPoints, damage, PERSON_TIMEOUT_SPEED, radius, GAME_PROPS);
                    driver.collectTaxi(taxi);
                    taxi.setDriver(driver);
                    break;
                case "COIN":
                    Coin newCoin = new Coin(x, y, Double.parseDouble(GAME_PROPS.getProperty("gameObjects.coin.radius")),
                            Integer.parseInt(GAME_PROPS.getProperty("gameObjects.coin.maxFrames")), GAME_PROPS);
                    COINS.add(newCoin);
                    break;
                case "PASSENGER":
                    int priority = Integer.parseInt(line[3]);
                    int endX = Integer.parseInt(line[4]);
                    int yDistance = Integer.parseInt(line[5]);
                    boolean withUmbrella;
                    healthPoints = DamageableGameEntity.getHealthUnit() *
                            Double.parseDouble(GAME_PROPS.getProperty("gameObjects.passenger.health"));
                    damage = 0;
                    radius = Double.parseDouble(GAME_PROPS.getProperty("gameObjects.passenger.radius"));
                    if (Integer.parseInt(line[6]) == 0) {
                        withUmbrella = false;
                    } else {
                        withUmbrella = true;
                    }
                    PASSENGERS.add(new Passenger(x, y, healthPoints, damage, PERSON_TIMEOUT_SPEED, radius,
                            priority, endX, yDistance, withUmbrella, GAME_PROPS));
                    break;
                case "INVINCIBLE_POWER":
                    InvinciblePower newPower = new InvinciblePower(x, y,
                            Double.parseDouble(GAME_PROPS.getProperty("gameObjects.invinciblePower.radius")),
                            Integer.parseInt(GAME_PROPS.getProperty("gameObjects.invinciblePower.maxFrames")),
                            GAME_PROPS);
                    INVINCIBLEPOWERS.add(newPower);
                    break;
            }
        }
        minPassengerHealth = PASSENGERS.get(0).getHealthPoints();
    }
    @Override
    public boolean update(Input input) {
        currFrame++;
        updateCurrentWeather();

        boolean moveUp = input.isDown(Keys.UP);
        background.updateBackground(currentWeather.getType(), moveUp);

        // update passengers
        updatePassengers(input);

        // update taxis
        updateTaxis(input);

        // update driver
        driver.update(input);
        totalEarnings = driver.calculateTotalEarnings();

        // update coin
        updateCoins(input);

        // update invincible powers
        updateInvinciblePowers(input);

        generateRandomCars();

        // update other cars & enemy cars
        updateCars(input);
        updateEnemyCars(input);

        displayInfo();

        return isGameOver() || isLevelCompleted();
    }

    private void updatePassengers(Input input) {
        boolean isRaining = currentWeather.getType().equals("RAINING");
        for(Passenger passenger: PASSENGERS) {
            TravelPlan currPlan = passenger.getTravelPlan();
            if (isRaining) {
                currPlan.setPriority(1);
            } else {
                if (!passenger.getIsGetInTaxi()) {
                    currPlan.setPriority(currPlan.getInitPriority());
                }
            }
            passenger.update(input, taxi, driver);
        }
    }

    private void updateTaxis(Input input) {
        if (DAMAGED_TAXIS.size() > 0) {
            for (Taxi damagedOne: DAMAGED_TAXIS) {
                damagedOne.update(input);
            }
        }
        taxi.update(input);
    }

    // not finished draft
    private void updateCars(Input input) {
        if (CARS.size() > 0) {
            for (OtherCar otherCar: CARS) {
                otherCar.update(input);
            }
        }
    }

    // not finished draft
    private void updateEnemyCars(Input input) {
        if (ENEMIES.size() > 0) {
            for (EnemyCar enemy: ENEMIES) {
                enemy.update(input);
                List<Fireball> fireballs = enemy.getFireBalls();
                if (fireballs.size() > 0) {
                    for (Fireball fireball: fireballs) {
                        fireball.update(input);
                    }
                }
            }
        }
    }

    private void updateInvinciblePowers(Input input) {
        if (INVINCIBLEPOWERS.size() > 0) {
            int minFramesActive = INVINCIBLEPOWERS.get(0).getDuration();
            for (InvinciblePower power: INVINCIBLEPOWERS) {
                power.update(input);
                if (driver.getIsInTaxi()) {
                    power.collide(taxi);
                } else {
                    power.collide(driver);
                }

                int framesActive = power.getFramesActive();
                if (power.getIsActive() && minFramesActive > framesActive) {
                    minFramesActive = framesActive;
                }
            }
            invincibleFramesActive = minFramesActive;
        }
    }

    private void updateCoins(Input input) {
        if (COINS.size() > 0) {
            int minFramesActive = COINS.get(0).getDuration();
            for(Coin coinPower: COINS) {
                coinPower.update(input);
                if (driver.getIsInTaxi()) {
                    coinPower.collide(taxi);
                } else {
                    coinPower.collide(driver);
                }

                // check if there's active coin and finding the coin with maximum ttl
                int framesActive = coinPower.getFramesActive();
                if(coinPower.getIsActive() && minFramesActive > framesActive) {
                    minFramesActive = framesActive;
                }
            }
            coinFramesActive = minFramesActive;
        }
    }

    private void generateRandomCars() {
        Random random = new Random();
        int randomNumCar = random.nextInt(RANDOM_BOUND);
        int randomNumEnemy = random.nextInt(RANDOM_BOUND);
        int lanesX[] = {Integer.parseInt(GAME_PROPS.getProperty("roadLaneCenter1")),
                Integer.parseInt(GAME_PROPS.getProperty("roadLaneCenter2")),
                Integer.parseInt(GAME_PROPS.getProperty("roadLaneCenter3"))};
        int x = lanesX[random.nextInt(lanesX.length)];
        int y = random.nextBoolean() ? RANDOM_Y1 : RANDOM_Y2;
        double healthPoints;
        double damage;
        double radius;

        if (randomNumCar % CAR_DIVISIBILITY == 0) {
            System.out.println("an othercar is generated");
            healthPoints = DamageableGameEntity.getHealthUnit() *
                    Double.parseDouble(GAME_PROPS.getProperty("gameObjects.otherCar.health"));
            damage = DamageableGameEntity.getHealthUnit() *
                    Double.parseDouble(GAME_PROPS.getProperty("gameObjects.otherCar.damage"));
            radius = Double.parseDouble(GAME_PROPS.getProperty("gameObjects.otherCar.radius"));
            CARS.add(new OtherCar(x, y, healthPoints, damage, VEHICLE_TIMEOUT_SPEED, radius, GAME_PROPS));
        } else if (randomNumEnemy % ENEMY_DIVISIBILITY == 0) {
            System.out.println("an enemy is generated");
            healthPoints = DamageableGameEntity.getHealthUnit() *
                    Double.parseDouble(GAME_PROPS.getProperty("gameObjects.enemyCar.health"));
            damage = DamageableGameEntity.getHealthUnit() *
                    Double.parseDouble(GAME_PROPS.getProperty("gameObjects.enemyCar.damage"));
            radius = Double.parseDouble(GAME_PROPS.getProperty("gameObjects.enemyCar.radius"));
            ENEMIES.add(new EnemyCar(x, y, healthPoints, damage, VEHICLE_TIMEOUT_SPEED, radius, GAME_PROPS));
        }
    }

    public String getTotalEarnings() {
        return String.format("%.02f", totalEarnings);
    }

    public void displayInfo() {
        INFO_FONT.drawString(MSG_PROPS.getProperty("gamePlay.earnings") + getTotalEarnings(), EARNINGS_X, EARNINGS_Y);
        INFO_FONT.drawString(MSG_PROPS.getProperty("gamePlay.target") + String.format("%.02f", TARGET), TARGET_X,
                TARGET_Y);
        INFO_FONT.drawString(MSG_PROPS.getProperty("gamePlay.remFrames") + (MAX_FRAMES - currFrame), MAX_FRAMES_X,
                MAX_FRAMES_Y);

        if(COINS.size() > 0 && COINS.get(0).getDuration() != coinFramesActive) {
            INFO_FONT.drawString(String.valueOf(Math.round(coinFramesActive)), COIN_X, COIN_Y);
        }

        Trip lastTrip = driver.getLastTrip();
        if(lastTrip != null) {
            if(lastTrip.isComplete()) {
                INFO_FONT.drawString(MSG_PROPS.getProperty("gamePlay.completedTrip.title"), TRIP_INFO_X, TRIP_INFO_Y);
            } else {
                INFO_FONT.drawString(MSG_PROPS.getProperty("gamePlay.onGoingTrip.title"), TRIP_INFO_X, TRIP_INFO_Y);
            }
            INFO_FONT.drawString(MSG_PROPS.getProperty("gamePlay.trip.expectedEarning")
                    + lastTrip.getPassenger().getTravelPlan().getExpectedFee(), TRIP_INFO_X, TRIP_INFO_Y
                    + TRIP_INFO_OFFSET_1);
            INFO_FONT.drawString(MSG_PROPS.getProperty("gamePlay.trip.priority")
                    + lastTrip.getPassenger().getTravelPlan().getPriority(), TRIP_INFO_X, TRIP_INFO_Y
                    + TRIP_INFO_OFFSET_2);
            if(lastTrip.isComplete()) {
                INFO_FONT.drawString(MSG_PROPS.getProperty("gamePlay.trip.penalty") + String.format("%.02f",
                        lastTrip.getPenalty()), TRIP_INFO_X, TRIP_INFO_Y + TRIP_INFO_OFFSET_3);
            }
        }
    }

    // Load weather conditions from CSV
    private void loadWeather(String filePath) {
        ArrayList<String[]> data = IOUtils.readCommaSeperatedFile(filePath);
        for (String[] line : data) {
            String type = line[0];
            int startFrame = Integer.parseInt(line[1]);
            int endFrame = Integer.parseInt(line[2]);
            WEATHERCONDITIONS.add(new Weather(type, startFrame, endFrame));
        }
    }

    // Update weather based on the current frame
    private void updateCurrentWeather() {
        for (Weather condition : WEATHERCONDITIONS) {
            if (currFrame >= condition.getStartFrame() && currFrame <= condition.getEndFrame()) {
                currentWeather = condition;
                break;
            }
        }
    }

    public boolean isGameOver() {
        // Game is over if any of the 4 conditions are met
        boolean isGameOver = (currFrame >= MAX_FRAMES || driver.getHealthPoints() <= 0
                || minPassengerHealth <= 0);

        if(currFrame >= MAX_FRAMES && !savedData) {
            savedData = true;
            IOUtils.writeLineToFile(GAME_PROPS.getProperty("gameEnd.scoresFile"),
                    PLAYER_NAME + "," + totalEarnings);
        }
        return isGameOver;
    }

    /**
     * Check if the level is completed. If the level is completed and not saved the score, save the score.
     * @return true if the level is completed, false otherwise.
     */
    public boolean isLevelCompleted() {
        // Level is completed if the total earnings is greater than or equal to the target earnings
        boolean isLevelCompleted = totalEarnings >= TARGET;
        if(isLevelCompleted && !savedData) {
            savedData = true;
            IOUtils.writeLineToFile(GAME_PROPS.getProperty("gameEnd.scoresFile"),
                    PLAYER_NAME + "," + totalEarnings);
        }
        return isLevelCompleted;
    }
}
