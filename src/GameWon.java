import java.awt.*;

public class GameWon implements VisibleObjects {
    String msg = "Game Won !";
    String msg2 = "Congrats, you made it!";
    String msg3 = "Press Enter to Restart / Escape to Exit to Menu";
    Font huge = new Font("Helvetica", Font.BOLD, 200);
    Font small = new Font("Helvetica", Font.BOLD, 120);
    Font tiny = new Font("Helvetica", Font.BOLD, 50);
    GameCanvas canvas;

    public GameWon(GameCanvas canvas) {
        this.canvas = canvas;
    }

    @Override
    public void drawObject(Graphics2D g) {
        g.setColor(Color.red);
        g.setFont(huge);
        g.drawString(msg, (canvas.getWidth() - g.getFontMetrics().stringWidth(msg)) / 2, (canvas.getHeight() - g.getFontMetrics().getHeight()) / 2);
        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg2, (canvas.getWidth() - g.getFontMetrics().stringWidth(msg2)) / 2, (canvas.getHeight() - g.getFontMetrics().getHeight()) / 2 + g.getFontMetrics().getHeight());
        g.setColor(Color.blue);
        g.setFont(tiny);
        g.drawString(msg3, (canvas.getWidth() - g.getFontMetrics().stringWidth(msg3)) / 2, (canvas.getHeight() - g.getFontMetrics().getHeight()) / 2 + g.getFontMetrics().getHeight() * 5);
    }
}
