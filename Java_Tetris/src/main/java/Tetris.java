import com.beust.jcommander.JCommander;
import java.util.Random;

class Tetris
{
    public static void main(String[] args)
    {
        JCommander commander = new JCommander(new Constants(), args);
        commander.setProgramName("Tetris");

        if(Constants.help)
        {
            commander.usage();
        }
        else
        {
            if(Constants.seed != 0)
            {
                Constants.random = new Random(Constants.seed);
            }

            GameWindow gameWindow = new GameWindow();
            if(Constants.RUN_AI)
            {
                Experimentalist experimentalist = new Experimentalist();
                experimentalist.attach(gameWindow);
                experimentalist.run();
            }
            else
            {
                GameController humanGame = new GameController();
                gameWindow.attach(humanGame);
            }
        }
    }
}