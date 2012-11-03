import java.util.ArrayList;
import java.util.concurrent.Executor;
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

    private void addGameToPlay()
    {
        GameController game = new GameController();
        games.add(game);
        AI ai = new AI();
        ai.attach(game);
        es.execute(ai);
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

    @Override
    public void run()
    {
        ArrayList<Integer> trialResults = new ArrayList<Integer>();
        for(int i = 0; i < Constants.AI_TRIALS; i++)
        {
            addGameToPlay();
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
}
