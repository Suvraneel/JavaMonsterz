import java.awt.*;
import java.util.ArrayList;

public class Menu implements VisibleObjects {
    String msg = "";
    ArrayList<String> options = new ArrayList<>();
    Font huge = new Font("Helvetica", Font.BOLD, 200);
    Font small = new Font("Helvetica", Font.BOLD, 100);
    GameCanvas canvas;
    private int selected = 0;
    public Menu(GameCanvas canvas) {
        this.canvas = canvas;
        options.add("New Game");
        options.add("Load Game");
        options.add("Exit");
    }

    @Override
    public void drawObject(Graphics2D g) {
        g.setColor(Color.white);
        g.setFont(huge);
        g.drawString(msg, (canvas.getWidth() - g.getFontMetrics().stringWidth(msg)) / 2, (canvas.getHeight() - g.getFontMetrics().getHeight()) / 2);
        g.setFont(small);
        for (int i = 0; i < options.size(); i++) {
            if (i == selected) {
                g.setColor(Color.RED);
            } else {
                g.setColor(Color.WHITE);
            }
            g.drawString(options.get(i), (canvas.getWidth() - g.getFontMetrics().stringWidth(options.get(i))) / 2, (canvas.getHeight() - g.getFontMetrics().getHeight()) / 2 + g.getFontMetrics().getHeight() * (i + 1));
        }
    }

    public void moveUp() {
        if (selected == 0) {
            selected = options.size() - 1;
        }
        else selected--;
    }

    public void moveDown() {
        selected = (selected + 1) % options.size();
    }

    public int getSelected() {
        return selected;
    }

    public void select() {
        if (selected == 0) {
            canvas.screen = GameCanvas.Screen.GAME;
            canvas.reset();
        } else if (selected == 1) {
            canvas.screen = GameCanvas.Screen.GAME;
            canvas.reset();
        } else if (selected == 2) {
            System.exit(0);
        }
    }
}
