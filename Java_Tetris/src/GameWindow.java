import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameWindow
{
    private GameController game;
    private JFrame gameFrame = new JFrame("Java Tetris");
    private TetrisCanvas canvas = new TetrisCanvas();
    private JLabel scoreLabel = new JLabel();
    private JLabel levelLabel = new JLabel();

    static class Updater implements ActionListener
    {
        GameWindow window;

        public Updater(GameWindow gameWindow)
        {
            window = gameWindow;
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent)
        {
            window.updateDisplay();
        }
    }

    public GameWindow()
    {
        javax.swing.SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                createWindow();
            }
        });
    }

    public void updateDisplay()
    {
        canvas.repaint();
        if(game != null)
        {
            scoreLabel.setText("Score: " + game.getScore());
            levelLabel.setText("Level: " + game.getLevel());
        }
    }

    public void attach(GameController gameController)
    {
        canvas.attach(gameController);
        game = gameController;
    }

    private void createWindow()
    {
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        scoreLabel.setHorizontalTextPosition(JLabel.LEFT);
        levelLabel.setHorizontalTextPosition(JLabel.LEFT);
        scoreLabel.setText("Score: ");
        levelLabel.setText("Level: ");
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        JPanel labelsPanel = new JPanel(new GridLayout(1, 2));
        labelsPanel.add(scoreLabel);
        labelsPanel.add(levelLabel);
        topPanel.add(labelsPanel);
        topPanel.add(canvas);
        gameFrame.add(topPanel);
        canvas.setFocusable(true);
        gameFrame.pack();
        gameFrame.setVisible(true);

        new Timer(100, new Updater(this)).start();
    }
}
