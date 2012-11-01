import javax.swing.*;

public class GameWindow
{
    JFrame gameFrame = new JFrame("Java Tetris");
    TetrisCanvas canvas = new TetrisCanvas();

    public GameWindow()
    {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createWindow();
            }
        });
    }

    public void attach(GameController gameController)
    {
        canvas.attach(gameController);
    }

    private void createWindow()
    {
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.add(canvas);
        canvas.setFocusable(true);
        gameFrame.pack();
        gameFrame.setVisible(true);
    }
}
