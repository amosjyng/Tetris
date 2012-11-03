public class AI implements Runnable
{
    private GameController actualGame;

    public void attach(GameController gameController)
    {
        actualGame = gameController;
    }

    private Move findBestMove(GameController game, int iterationLevel)
    {
        Move bestMove = new Move(0, 0);
        return bestMove;
    }

    @Override
    public void run()
    {
        if(actualGame == null)
        {
            System.err.println("Can't run AI; actual game not set yet!");
        }
        else
        {
            while(!actualGame.gameOver())
            {
                Move bestMove = findBestMove(actualGame.clone(), Constants.SHAPES_QUEUE_SIZE);
                actualGame.moveAllTheWay(Direction.LEFT);
                for(int i = 0; i < bestMove.orientation; i++)
                {
                    actualGame.tryRotate();
                    actualGame.moveAllTheWay(Direction.LEFT);
                }
                for(int i = 0; i < bestMove.position; i++)
                {
                    actualGame.tryMove(Direction.RIGHT);
                }

                actualGame.moveAllTheWay(Direction.DOWN);
            }
        }
    }
}
