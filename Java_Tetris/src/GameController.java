import java.util.ArrayList;

public class GameController
{
    private int score = 0;
    private int level = 0;
    // threshold scores to get to the next level from the current level
    private ArrayList<Integer> levelThresholds = new ArrayList<Integer>();
    private Board board = new Board();
    private Shape currentShape;
    private ArrayList<Shape> shapesQueue = new ArrayList<Shape>(); // todo: make an actual queue...?
    private boolean paused = false;
    private boolean gameOver = false;

    public GameController()
    {
        for(int i = 1; i < Constants.MAX_LEVELS; i++)
        {
            levelThresholds.add((int)Math.pow(2, (double)i) * 100);
        }
        for(int i = 0; i < Constants.SHAPES_QUEUE_SIZE; i++)
        {
            addShapeToQueue();
        }
        currentShape = popNextShapeFromQueue(); // popNextShapeFromQueue will add another shape to the queue
    }

    public void output() // for debugging purposes
    {
        System.out.println("Current shape is a(n) " + currentShape.getName());
        System.out.print("Next shapes in queue are: ");
        for(int i = 0; i < shapesQueue.size(); i++)
        {
            System.out.print("a " + shapesQueue.get(i).getName());
        }
        System.out.println();
        board.output();
    }

    public boolean gameOver()
    {
        return gameOver;
    }

    public Board getBoard()
    {
        return board;
    }

    public Shape getCurrentShape()
    {
        return currentShape;
    }

    private void addShapeToQueue()
    {
        try
        {
            shapesQueue.add(Shape.randomShape());
        }
        catch (Exception e)
        {
            System.err.println("Error adding random shape: " + e);
            System.exit(100);
        }
    }

    public boolean checkMove(Direction direction)
    {
        try
        {
            return board.areValid(currentShape.getTranslatedCoordinates(direction));
        }
        catch (Exception e)
        {
            System.err.println("Error checking translated shape!");
            e.printStackTrace();
            System.exit(101);
        }
        // I am DAMN sure that either it returns whether it was valid, or it exists the program
        // I mean, that's what System.exit does, right? But it still wants a return value
        // Okay, fine
        return false;
    }

    private void updateLevel()
    {
        if(level < Constants.MAX_LEVELS - 1 && score >= levelThresholds.get(level))
        {
            level++;
        }
    }

    private void updateScore(int completedRows)
    {
        score += 100 * completedRows * completedRows;
        updateLevel();
    }

    private Shape popNextShapeFromQueue()
    {
        Shape shape = shapesQueue.get(0);
        shapesQueue.remove(0);
        addShapeToQueue();
        return shape;
    }

    private boolean checkGameOver()
    {
        return !board.areEmpty(currentShape.getCoordinates());
    }

    private void landShape()
    {
        board.addBlocksAt(currentShape.getCoordinates(), currentShape.getColor());
        ArrayList<Integer> completedRows = board.clearCompletedRows();
        updateScore(completedRows.size());
        currentShape = popNextShapeFromQueue();
        gameOver = checkGameOver();
    }

    private void move(Direction direction)
    {
        try
        {
            currentShape.translate(direction);
        }
        catch (Exception e)
        {
            System.err.println("Error translating current shape!");
            e.printStackTrace();
            System.exit(102);
        }
    }

    // Returns whether it is useful to keep moving the piece in this direction
    public boolean tryMove(Direction direction)
    {
        if(checkMove(direction))
        {
            /*
            Possible performance issue: it has already checked whether the coordinates to move to are already occupied,
            so we don't need to generate the new coordinates AGAIN
             */
            move(direction);
            return true;
        }
        else
        {
            if(direction == Direction.DOWN)
            {
                landShape();
            }
            return false;
        }
    }

    public void moveAllTheWay(Direction direction)
    {
        while(tryMove(direction)); // just keep making that same move
    }
}
