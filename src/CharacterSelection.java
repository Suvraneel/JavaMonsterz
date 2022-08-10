import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

public class CharacterSelection implements VisibleObjects {
    String msg = "----- Character Selection -----";
    ArrayList<Image> characters = new ArrayList<>();
    Font huge = new Font("Helvetica", Font.BOLD, 200);
    Font small = new Font("Helvetica", Font.BOLD, 80);
    Font tiny = new Font("Helvetica", Font.BOLD, 50);
    GameCanvas canvas;
    private int selected = 0;

    public CharacterSelection(GameCanvas canvas) {
        this.canvas = canvas;
        characters.add(new ImageIcon(Objects.requireNonNull(getClass().getResource("resources/images/player1.png"))).getImage());
        characters.add(new ImageIcon(Objects.requireNonNull(getClass().getResource("resources/images/player2.png"))).getImage());
    }

    @Override
    public void drawObject(Graphics2D g) {
        g.setColor(Color.white);
        g.setFont(huge);
        g.drawString("", (canvas.getWidth() - g.getFontMetrics().stringWidth(msg)) / 2, (canvas.getHeight() - g.getFontMetrics().getHeight()) / 2);
        g.setFont(small);
        g.drawString("Press Enter to Select", (canvas.getWidth() - g.getFontMetrics().stringWidth("Press Enter to Select")) / 2, (canvas.getHeight() - g.getFontMetrics().getHeight()) / 2 + g.getFontMetrics().getHeight());
        g.setFont(tiny);
        g.drawString(msg, (canvas.getWidth() - g.getFontMetrics().stringWidth(msg)) / 2, (canvas.getHeight() - g.getFontMetrics().getHeight()) / 2);
        for (int i = 0; i < characters.size(); i++) {
            if (i == selected) {
                g.setColor(Color.RED);
            } else {
                g.setColor(Color.WHITE);
            }
            g.drawRect((-300 + canvas.getWidth() - characters.get(i).getWidth(null)) / 2 + characters.get(i).getWidth(null)  * (i + 1), (canvas.getHeight() - characters.get(i).getHeight(null)) / 2 + characters.get(i).getHeight(null), characters.get(i).getWidth(null), characters.get(i).getHeight(null));
            g.drawImage(characters.get(i), (-300 + canvas.getWidth() - characters.get(i).getWidth(null)) / 2 + characters.get(i).getWidth(null)  * (i + 1), (canvas.getHeight() - characters.get(i).getHeight(null)) / 2 + characters.get(i).getHeight(null), null);
        }
    }

    public void moveLt() {
        if (selected == 0) {
            selected = characters.size() - 1;
        }
        else selected--;
    }

    public void moveRt() {
        selected = (selected + 1) % characters.size();
    }

    public void select() {
        canvas.SelectedCharacter = selected;
        canvas.screen = GameCanvas.Screen.MENU;
        canvas.exitToMenu();
    }
}
