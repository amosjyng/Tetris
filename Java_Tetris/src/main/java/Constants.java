public class Constants
{
    public static final int MAXX = 10;
    public static final int MAXY = 22;
    public static final int SHAPES_QUEUE_SIZE = 2;
    public static final int MAX_LEVELS = 10;
    public static final boolean LINEAR_SCORE_FUNCTION = true;
    public static final int SCORE_MULTIPLIER = 1; // make people happier, I guess

    // UI-related stuff
    public static final int SQUARE_SIZE = 20;
    public static final String REMOVED_SQUARE_COLOR = "gray";

    // AI related stuff
    public static final int AI_TRIALS = 20;
    public static final int AI_PAUSE_INTERVAL = 0;
    public static final double CURRENT_MOVE_MULTIPLIER = 1.01; // prioritize earlier gains
    public static final double GAME_SCORE_WEIGHT = 10;
    public static final double BOARD_HEIGHT_WEIGHT = -5;
    public static final double BOARD_OVERHANGS_WEIGHT = -10;
}