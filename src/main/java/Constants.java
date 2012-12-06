import com.beust.jcommander.Parameter;

public class Constants
{
    // todo: refactor non-static variables into new class "Options," because they aren't really "Constant"?

    // program related stuff
    @Parameter(names = {"--help", "-h"}, help = true, description = "Display this help message")
    public static boolean help;
    @Parameter(names = "--seed", description = "Random number generator seed")
    public static int seed = 0;
    @Parameter(names = "--exact", description = "Perform an exact search for optimal play")
    public static boolean exact;

    // actual Tetris related stuff
    public static final int MAXX = 10;
    public static final int MAXY = 22;
    @Parameter(names = "--queue-size", description = "Number of shapes in the queue")
    public static int DEFAULT_SHAPES_QUEUE_SIZE = 2;
    public static final int MAX_LEVELS = 10;
    public static final boolean LINEAR_SCORE_FUNCTION = true;
    public static final int SCORE_MULTIPLIER = 1; // make people happier, I guess

    // UI-related stuff
    public static final int SQUARE_SIZE = 20;
    public static final String REMOVED_SQUARE_COLOR = "gray";

    // AI related stuff
    @Parameter(names = "--ai", description = "Run AI, or let human user play?")
    public static boolean RUN_AI = false;
    @Parameter(names = "--genetic", description = "Use genetic algorithms to determine best weights")
    public static boolean USE_GA = false;
    @Parameter(names = "--trials", description = "Number of trials to run")
    public static int AI_TRIALS = 10;
    public static final int AI_PAUSE_INTERVAL = 0;
    public static final double CURRENT_MOVE_MULTIPLIER = 1.01; // prioritize earlier gains
    public static double DEFAULT_GAME_SCORE_WEIGHT = 10;
    public static double DEFAULT_BOARD_HEIGHT_WEIGHT = -5;
    public static double DEFAULT_BOARD_OVERHANG_WEIGHT = -10;
    @Parameter(names = {"--population", "--pop"}, description = "Population size to use for genetic algorithm")
    public static int POPULATION = 10;
    @Parameter(names = "--evolutions", description = "Maximum number of evolutions to run GA for")
    public static int MAX_EVOLUTIONS = 10;
}
