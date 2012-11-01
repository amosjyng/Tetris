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

    public boolean checkValidMove(ArrayList<Coordinate> newCoordinates)
    {
        return board.areValid(newCoordinates);
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

    // Returns whether it is useful to keep moving the piece in this direction
    public boolean tryMove(Direction direction)
    {
        ArrayList<Coordinate> newCoordinates = currentShape.getTranslatedCoordinates(direction);
        if(checkValidMove(newCoordinates))
        {
            currentShape.setCoordinates(newCoordinates);
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

    public Boolean tryRotate()
    {
        ArrayList<Coordinate> newCoordinates = currentShape.getRotatedCoordinates();
        if(checkValidMove(newCoordinates))
        {
            currentShape.setCoordinates(newCoordinates);
            return true;
        }
        else
        {
            return false;
        }
    }

    public void moveAllTheWay(Direction direction)
    {
        while(tryMove(direction)); // just keep making that same move
    }
}
