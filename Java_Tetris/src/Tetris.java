class Tetris
{
    public static void main(String[] args)
    {
        GameController game = new GameController();
        GameWindow gameWindow = new GameWindow();
        gameWindow.attach(game);
    }
}