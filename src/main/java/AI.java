import java.util.concurrent.Callable;

public class AI implements Callable
{
    private GameController actualGame;
    private Options options;

    public AI(Options newOptions)
    {
        options = newOptions;
    }

    public void attach(GameController gameController)
    {
        actualGame = gameController;
    }

    private double heuristic(GameController game)
    {
        return game.getScore() * options.gameScoreWeight
                + game.getBoard().getAverageHeight() * options.boardHeightWeight
                + game.getBoard().getOverhangCount() * options.boardOverhangsWeight;
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
    public Integer call()
    {
        if(actualGame == null)
        {
            System.err.println("Can't run AI; actual game not set yet!");
            System.exit(300);
            return -1;
        }
        else
        {
            int movesMade = 0;
            while(movesMade < options.maxMoves && !actualGame.gameOver())
            {
                Move bestMove;
                int movesLeft = options.maxMoves - movesMade;
                if(!options.exactSearch || movesLeft > options.shapesQueueSize)
                {
                    bestMove = findBestMove(actualGame.clone(), options.shapesQueueSize);
                }
                else
                {
                    // subtract 1 from movesLeft because findBestMove only stops when iterationLevel is < 0
                    bestMove = findBestMove(actualGame.clone(), movesLeft - 1);
                }
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

                movesMade++;
            }

            return actualGame.getScore();
        }
    }
}
