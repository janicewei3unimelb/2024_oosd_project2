import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import bagel.*;

public class GamePlayScreen {
    private final Properties GAME_PROPS;
    private final Properties MSG_PROPS;

    private int currFrame = 0;
    private double coinFramesActive;
    private double invincibleFramesActive;
    private final int MAX_FRAMES;

    // Weather condition management
    private final List<Weather> WEATHERCONDITIONS;
    private Weather currentWeather;

    private GameBackground background;

    private double totalEarnings;
    private final double TARGET;

    private final String PLAYER_NAME;
    private boolean savedData;

    private Taxi taxi;
    private Driver driver;
    private final List<Passenger> PASSENGERS;
    private final List<Coin> COINS;
    private final List<InvinciblePower> INVINCIBLEPOWERS;

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

        ArrayList<String[]> lines = IOUtils.readCommaSeperatedFile(gameProps.getProperty("gamePlay.objectsFile"));
        populateGameObjects(lines);

        this.MAX_FRAMES = Integer.parseInt(gameProps.getProperty("gamePlay.maxFrames"));

        this.TARGET = Float.parseFloat(gameProps.getProperty("gamePlay.target"));

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
            switch (type) {
                case "TAXI":
                    taxi = new Taxi(x, y, GAME_PROPS);
                    break;
                case "DRIVER":
                    driver = new Driver(x, y, GAME_PROPS);
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
                    PASSENGERS.add(new Passenger(x, y, priority, endX, yDistance, GAME_PROPS));
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
    }

    public boolean update(Input input) {
        currFrame++;
        updateCurrentWeather();

        boolean moveUp = input.isDown(Keys.UP);
        background.updateBackground(currentWeather.getType(), moveUp);

        for(Passenger passenger: PASSENGERS) {
            passenger.updateWithTaxi(input, taxi);
        }

        taxi.update(input);
        totalEarnings = taxi.calculateTotalEarnings();

        if(COINS.size() > 0) {
            int minFramesActive = COINS.get(0).getDuration();
            for(Coin coinPower: COINS) {
                coinPower.update(input);
                coinPower.collide(taxi);

                // check if there's active coin and finding the coin with maximum ttl
                int framesActive = coinPower.getFramesActive();
                if(coinPower.getIsActive() && minFramesActive > framesActive) {
                    minFramesActive = framesActive;
                }
            }
            coinFramesActive = minFramesActive;
        }

        generateRandomEntities();

        displayInfo();

        return isGameOver() || isLevelCompleted();
    }

    private void generateRandomEntities() {

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

        Trip lastTrip = taxi.getLastTrip();
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
        // Game is over if the current frame is greater than the max frames
        boolean isGameOver = currFrame >= MAX_FRAMES;

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
