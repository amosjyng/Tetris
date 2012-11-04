import com.beust.jcommander.JCommander;

class Tetris
{
    public static void main(String[] args)
    {
        new JCommander(new Constants(), args);

        GameWindow gameWindow = new GameWindow();
        Experimentalist experimentalist = new Experimentalist();
        experimentalist.attach(gameWindow);
        experimentalist.run();
    }
}