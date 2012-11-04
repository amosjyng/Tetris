import com.beust.jcommander.JCommander;

class Tetris
{
    public static void main(String[] args)
    {
        new JCommander(new Constants(), args);

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