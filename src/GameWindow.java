import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameWindow extends JFrame implements ActionListener {
    GameCanvas canvas = new GameCanvas();

    public GameWindow() {
        setTitle("JavaJailbreak");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setUndecorated(true);
        add(canvas);
        Timer t = new Timer(60, this);
        t.start();
    }

    public static void main(String[] args) {
        GameWindow game = new GameWindow();
        game.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        canvas.draw();
    }
}