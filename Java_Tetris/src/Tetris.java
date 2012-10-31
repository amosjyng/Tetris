class Tetris
{
    public static void main(String[] args)
    {
        GameController game = new GameController();
        while(!game.gameOver())
        {
            game.output();
            game.moveAllTheWay(Direction.DOWN);
        }
        System.out.println("Game Over!");
        game.output();
    }
}