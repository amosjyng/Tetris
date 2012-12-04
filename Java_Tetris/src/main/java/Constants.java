import com.beust.jcommander.Parameter;

import java.util.Random;

public class Constants
{
    public static final int MAXX = 10;
    public static final int MAXY = 22;
    @Parameter(names = "--queue-size", description = "Number of shapes in the queue")
    public static int SHAPES_QUEUE_SIZE = 2;
    public static final int MAX_LEVELS = 10;
    public static final boolean LINEAR_SCORE_FUNCTION = true;
    public static final int SCORE_MULTIPLIER = 1; // make people happier, I guess
    @Parameter(names = "--seed", description = "Random number generator seed")
    public static int seed = 0;
    public static Random random = new Random();

    // UI-related stuff
    public static final int SQUARE_SIZE = 20;
    public static final String REMOVED_SQUARE_COLOR = "gray";

    // AI related stuff
    @Parameter(names = "--ai", description = "Run AI, or let human user play?")
    public static boolean RUN_AI = false;
    @Parameter(names = "--trials", description = "Number of trials to run")
    public static int AI_TRIALS = 20;
    public static final int AI_PAUSE_INTERVAL = 0;
    public static final double CURRENT_MOVE_MULTIPLIER = 1.01; // prioritize earlier gains
    public static final double GAME_SCORE_WEIGHT = 10;
    public static final double BOARD_HEIGHT_WEIGHT = -5;
    public static final double BOARD_OVERHANGS_WEIGHT = -10;
}
