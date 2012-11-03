public class Constants
{
    public static final int MAXX = 10;
    public static final int MAXY = 22;
    public static final int SHAPES_QUEUE_SIZE = 1;
    public static final int MAX_LEVELS = 10;
    public static final int SQUARE_SIZE = 20;
    public static final String REMOVED_SQUARE_COLOR = "gray";
    public static final int SCORE_MULTIPLIER = 100; // make people happier, I guess

    // AI related stuff
    public static final int AI_TRIALS = 20;
    public static final int AI_PAUSE_INTERVAL = 0;
    public static final double CURRENT_MOVE_MULTIPLIER = 1.01; // prioritize earlier gains
    public static final double GAME_SCORE_WEIGHT = 10;
    public static final double BOARD_HEIGHT_WEIGHT = -5;
}
