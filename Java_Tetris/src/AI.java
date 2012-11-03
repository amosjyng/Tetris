public class AI implements Runnable
{
    private GameController actualGame;

    public void attach(GameController gameController)
    {
        actualGame = gameController;
    }

    private double heuristic(GameController game)
    {
        return game.getScore() * Constants.GAME_SCORE_WEIGHT
                + game.getBoard().getAverageHeight() * Constants.BOARD_HEIGHT_WEIGHT;
    }

    private Move findBestMove(GameController game, int iterationLevel)
    {
        Move bestMove = new Move(0, 0);
        for(int orientation = 0; orientation < game.getCurrentShape().getOrientations(); orientation++)
        {
            game.moveAllTheWay(Direction.LEFT);
            if(orientation != 0) // no need to rotate from default orientation if already default
            {
                game.tryRotate();
                game.moveAllTheWay(Direction.LEFT);
            }
            for(int position = 0; position < Constants.MAXX; position++)
            {
                if(position != 0) // same reasoning, no need to translate from default position
                {
                    game.tryMove(Direction.RIGHT);
                }
                game.moveAllTheWay(Direction.DOWN);
                if(!game.gameOver())
                {
                    double thisHeuristic;
                    if(iterationLevel > 0)
                    {
                        thisHeuristic = findBestMove(game, iterationLevel - 1).heuristicScore
                                            * Constants.CURRENT_MOVE_MULTIPLIER;
                    }
                    else
                    {
                        thisHeuristic = heuristic(game);
                    }

                    if(thisHeuristic > bestMove.heuristicScore)
                    {
                        bestMove.heuristicScore = thisHeuristic;
                        bestMove.orientation = orientation;
                        bestMove.position = position;
                    }
                }

                try
                {
                    game.undo();
                }
                catch (Exception e)
                {
                    System.err.println("AI couldn't undo last move!");
                    e.printStackTrace();
                    game.output();
                    System.err.println("Current orientation: " + orientation + ", current position: " + position);
                }
            }
        }
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
                try
                {
                    Thread.sleep(Constants.AI_PAUSE_INTERVAL);
                }
                catch (InterruptedException ie)
                {
                    ie.printStackTrace();
                }
            }
        }
    }
}
