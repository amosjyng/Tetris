class Tetris
{
    public static void main(String[] args)
    {
        GameWindow gameWindow = new GameWindow();
        Experimentalist experimentalist = new Experimentalist();
        experimentalist.attach(gameWindow);
        experimentalist.run();
    }
}