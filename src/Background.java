import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class Background implements VisibleObjects {
    GameCanvas canvas;
    Image background;

    public Background(GameCanvas canvas, String bg) {
        this.canvas = canvas;
        String path = "resources/images/" + bg + ".png";
        background = new ImageIcon(Objects.requireNonNull(getClass().getResource(path))).getImage();
    }

    @Override
    public void drawObject(Graphics2D g) {
        g.drawImage(background, 0, 0, canvas.getWidth(), canvas.getHeight(), null);
    }
}
