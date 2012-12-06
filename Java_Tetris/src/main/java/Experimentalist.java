import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Experimentalist implements Runnable
{
    private GameWindow gameWindow;
    ArrayList<GameController> games = new ArrayList<GameController>();
    private ExecutorService es = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public void attach(GameWindow newGameWindow)
    {
        gameWindow = newGameWindow;
    }

    private AI setUpGame(Constants constants)
    {
        if(constants == null)
        {
            constants = new Constants();
            constants.SHAPES_QUEUE_SIZE = Constants.DEFAULT_SHAPES_QUEUE_SIZE;
        }
        GameController game = new GameController(constants.SHAPES_QUEUE_SIZE);
        games.add(game);
        AI ai = new AI(constants);
        ai.attach(game);

        return ai;
    }

    private void playGame(Constants constants)
    {
        setUpGame(constants).run();
    }

    private void addGameToPlay(Constants constants)
    {
        es.execute(setUpGame(constants));
    }

    private void analyzeData(ArrayList<Integer> results)
    {
        int trialSum = 0;
        for(int result : results)
        {
            trialSum += result;
        }

        System.out.println("Average score was: " + trialSum * 1.0 / results.size());
    }

    private void runNormalTrials()
    {
        ArrayList<Integer> trialResults = new ArrayList<Integer>();
        for(int i = 0; i < Constants.AI_TRIALS; i++)
        {
            addGameToPlay(null);
        }
        gameWindow.attach(games.get(0)); // this will get boring after the first game

        es.shutdown();
        try
        {
            es.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        }
        catch (InterruptedException ie)
        {
            System.err.println("Could not wait for threads to end!");
            ie.printStackTrace();
        }

        for(GameController game : games)
        {
            trialResults.add(game.getScore());
        }

        analyzeData(trialResults);
    }

    private void runExactTrials()
    {
        Constants optimalPlayConstant = new Constants();
        optimalPlayConstant.BOARD_HEIGHT_WEIGHT = 0;
        optimalPlayConstant.BOARD_OVERHANGS_WEIGHT = 0;
        optimalPlayConstant.SHAPES_QUEUE_SIZE = Constants.DEFAULT_SHAPES_QUEUE_SIZE;
        int maxMoves = Constants.DEFAULT_SHAPES_QUEUE_SIZE + 1;
        optimalPlayConstant.MAX_MOVES = maxMoves;
        optimalPlayConstant.EXACT_SEARCH = true;
        playGame(optimalPlayConstant);
        System.out.println("Optimal play gave a score of " + games.get(0).getScore() + ":");
        games.get(0).getBoard().output();

        for(int i = 0; i < Constants.DEFAULT_SHAPES_QUEUE_SIZE; i++)
        {
            Constants newGameConstant = new Constants();
            newGameConstant.SHAPES_QUEUE_SIZE = i;
            newGameConstant.MAX_MOVES = maxMoves;
            Constants.random = new Random(Constants.seed);
            playGame(newGameConstant);
        }

        for(int i = 1; i < games.size(); i++)
        {
            System.out.println("For a game with a queue size of " + games.get(i).getShapesQueueSize()
                                + ", the AI obtained a score of " + games.get(i).getScore() + ":");
            games.get(i).getBoard().output();
        }
    }

    @Override
    public void run()
    {
        if(Constants.exact)
        {
            runExactTrials();
        }
        else
        {
            runNormalTrials();
        }
    }
}
