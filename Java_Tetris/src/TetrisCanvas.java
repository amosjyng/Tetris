import javax.swing.*;
import java.awt.*;

public class TetrisCanvas extends JPanel
{
    private GameController game;
    private Board board;

    public void attach(GameController gameController)
    {
        game = gameController;
        board = game.getBoard();
    }

    @Override
    public Dimension getPreferredSize()
    {
        return new Dimension(Constants.MAXX * Constants.SQUARE_SIZE, Constants.MAXY * Constants.SQUARE_SIZE);
    }

    private Color getColor(String colorName) throws Exception
    {
        if(colorName.isEmpty())
        {
            return Color.white;
        }
        else if(colorName.equals("red"))
        {
            return Color.red;
        }
        else if(colorName.equals("yellow"))
        {
            return Color.yellow;
        }
        else if(colorName.equals("orange"))
        {
            return Color.orange;
        }
        else if(colorName.equals("green"))
        {
            return Color.green;
        }
        else if(colorName.equals("blue"))
        {
            return Color.blue;
        }
        else if(colorName.equals("purple"))
        {
            return Color.pink; // eh...
        }
        else if(colorName.equals("magenta"))
        {
            return Color.magenta;
        }
        else
        {
            throw new Exception("Unrecognized color: " + colorName);
        }
    }

    private void displaySquare(Graphics g, int x, int y, String square)
    {
        Color color;
        try
        {
            color = getColor(square);
        }
        catch (Exception e)
        {
            System.err.println(e.getMessage());
            color = Color.gray;
        }
        g.setColor(color);
        int xPos = x * Constants.SQUARE_SIZE, yPos = y * Constants.SQUARE_SIZE;
        g.fillRect(xPos, yPos, Constants.SQUARE_SIZE, Constants.SQUARE_SIZE);
        if(!color.equals(Color.white))
        {
            g.setColor(Color.white);
            g.drawRect(xPos, yPos, Constants.SQUARE_SIZE, Constants.SQUARE_SIZE);
        }
    }

    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        if(game != null)
        {
            for(int y = 0; y < Constants.MAXY; y++)
            {
                for(int x = 0; x < Constants.MAXX; x++)
                {
                    displaySquare(g, x, y, board.getSquare(x, y));
                }
            }

            Shape currentShape = game.getCurrentShape();
            for(Coordinate coordinate : currentShape.getCoordinates())
            {
                displaySquare(g, coordinate.x, coordinate.y, currentShape.getColor());
            }
        }
    }
}