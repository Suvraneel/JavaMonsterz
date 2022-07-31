import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class Background implements VisibleObjects {
    GameCanvas canvas;
    Image background;

    public Background(GameCanvas canvas) {
        this.canvas = canvas;
        background = new ImageIcon(Objects.requireNonNull(getClass().getResource("resources/images/background.png"))).getImage();
    }

    @Override
    public void drawObject(Graphics2D g) {
        g.drawImage(background, 0, 0, canvas.getWidth(), canvas.getHeight(), null);
    }
}
