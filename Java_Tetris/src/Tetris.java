class Tetris
{
    public static void main(String[] args)
    {
        GameController game = new GameController();
        GameWindow gameWindow = new GameWindow();
        gameWindow.attach(game);
        while(!game.gameOver())
        {
            game.output();
            game.moveAllTheWay(Direction.DOWN);
        }
        System.out.println("Game Over!");
        game.output();
    }
}