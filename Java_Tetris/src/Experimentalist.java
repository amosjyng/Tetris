import java.util.ArrayList;

public class Experimentalist implements Runnable
{
    private static GameWindow gameWindow;
    private static AI ai = new AI();

    public void attach(GameWindow newGameWindow)
    {
        gameWindow = newGameWindow;
    }

    private int playGame()
    {
        GameController game = new GameController();
        if(gameWindow != null)
        {
            gameWindow.attach(game);
        }
        ai.attach(game);
        ai.run();

        return game.getScore();
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
            trialResults.add(playGame());
        }

        analyzeData(trialResults);
    }
}
