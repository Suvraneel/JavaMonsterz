import java.awt.*;

public class GameOver implements VisibleObjects {
    String msg = "Game Over";
    String msg2 = "New Game?";
    Font small = new Font("Helvetica", Font.BOLD, 200);
    GameCanvas canvas;

    public GameOver(GameCanvas canvas) {
        this.canvas = canvas;
    }

    @Override
    public void drawObject(Graphics2D g) {
        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg, (canvas.getWidth() - g.getFontMetrics().stringWidth(msg)) / 2, (canvas.getHeight() - g.getFontMetrics().getHeight()) / 2);
        g.drawString(msg2, (canvas.getWidth() - g.getFontMetrics().stringWidth(msg2)) / 2, (canvas.getHeight() - g.getFontMetrics().getHeight()) / 2 + g.getFontMetrics().getHeight());
    }
}
