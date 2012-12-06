import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class TetrisCanvas extends JPanel
{
    private GameController game;
    private Board board;

    public TetrisCanvas()
    {
        addKeyListener(new KeyListener()
        {
            @Override
            public void keyTyped(KeyEvent keyEvent)
            {
                // nothing to do...
            }

            @Override
            public void keyPressed(KeyEvent keyEvent)
            {
                switch (keyEvent.getKeyCode())
                {
                    case KeyEvent.VK_SPACE:
                        keyAllTheWayDownPressed();
                        break;
                    case KeyEvent.VK_DOWN:
                        keyDownPressed();
                        break;
                    case KeyEvent.VK_LEFT:
                        keyLeftPressed();
                        break;
                    case KeyEvent.VK_RIGHT:
                        keyRightPressed();
                        break;
                    case KeyEvent.VK_UP:
                        keyRotatePressed();
                        break;
                    case KeyEvent.VK_P:
                        keyPausePressed();
                        break;
                    case KeyEvent.VK_U:
                        keyUndoPressed();
                        break;
                    default:
                        System.err.println("Key " + keyEvent.getKeyCode() + " not recognized");
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent keyEvent)
            {
                // nothing to do...
            }
        });
    }

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

    private void keyAllTheWayDownPressed()
    {
        if(game != null)
        {
            game.moveAllTheWay(Direction.DOWN);
        }
    }

    private void keyDownPressed()
    {
        if(game != null)
        {
            game.tryMove(Direction.DOWN);
        }
    }

    private void keyLeftPressed()
    {
        if(game != null)
        {
            game.tryMove(Direction.LEFT);
        }
    }

    private void keyRightPressed()
    {
        if(game != null)
        {
            game.tryMove(Direction.RIGHT);
        }
    }

    private void keyRotatePressed()
    {
        if(game != null)
        {
            game.tryRotate();
        }
    }

    private void keyPausePressed()
    {
        if(game != null)
        {
            game.togglePause();
        }
    }

    private void keyUndoPressed()
    {
        if(game != null)
        {
            try
            {
                game.undo();
            }
            catch (Exception e)
            {
                System.out.println("No more moves left to undo!");
            }
        }
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
            return Color.yellow.darker(); // yellow is too bright by default
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
        else if(colorName.equals("gray"))
        {
            return Color.gray;
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

    private void displayText(Graphics g, String text)
    {
        g.setColor(Color.black);
        int xPos = 0, yPos = (Constants.MAXY / 2) * Constants.SQUARE_SIZE;
        g.fillRect(xPos, yPos - 1 * Constants.SQUARE_SIZE,
                   Constants.MAXX * Constants.SQUARE_SIZE, 3 * Constants.SQUARE_SIZE);
        Font font = new Font("Helvetica", Font.BOLD, 16);
        g.setFont(font);
        g.setColor(Color.red);
        g.drawString(text, xPos + 3 * Constants.SQUARE_SIZE,
                     (int)Math.round(yPos + 0.5 * Constants.SQUARE_SIZE));
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

            if(game.gameOver())
            {
                displayText(g, "GAME OVER");
            }
            else if(game.paused())
            {
                displayText(g, "PAUSED");
            }
        }
    }
}