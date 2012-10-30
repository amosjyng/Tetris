import java.util.ArrayList;

public class GameController
{
    private int score = 0;
    private int level = 0;
    private Board board = new Board();
    private Shape currentShape;
    private ArrayList<Shape> shapesQueue = new ArrayList<Shape>(); // todo: make an actual queue...?
    private Boolean paused = false;
    private Boolean gameOver = false;

    GameController()
    {
        for(int i = 0; i < Constants.SHAPES_QUEUE_SIZE; i++)
        {
            try
            {
                shapesQueue.add(Shape.randomShape());
            }
            catch (Exception e)
            {
                System.err.println("Error adding random shape: " + e);
                System.err.println("Game will continue with " + Integer.toString(shapesQueue.size())
                                     + " random shapes in queue");
            }
            try
            {
                currentShape = Shape.randomShape();
            }
            catch (Exception e)
            {
                System.err.println("Error creating random shape on board: " + e);
                System.exit(100);
            }
        }
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
}
