import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class Camera implements VisibleObjects {
    private final int delay;
    int x, y, x_offset, y_offset, timeElapsed = 0;

    GameCanvas canvas;
    Image camera;
    Sprite camSprite = new Sprite("enemy/cam", 2);
    Image alarm = new ImageIcon(Objects.requireNonNull(getClass().getResource("resources/sprites/enemy/cam/radar_alarm.png"))).getImage();
    Rectangle camBounds = new Rectangle();
    Tiles tiles;
    Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
    int screenHeight = (int) size.getHeight();
    int screenWidth = (int) size.getWidth();

    public Camera(GameCanvas canvas, int i, int j, Tiles tiles, int delay) {
        this.canvas = canvas;
        this.delay = delay;
        this.tiles = tiles;
        x_offset = (screenWidth / tiles.tiles[0].length);
        y_offset = (screenHeight / tiles.tiles.length);
        this.x = j * x_offset;
        this.y = i * y_offset;
        camera = new ImageIcon(Objects.requireNonNull(getClass().getResource("resources/sprites/enemy/cam/00.png"))).getImage();
        System.out.println("==================>" + camera.getWidth(null) + " " + camera.getHeight(null));
        camBounds.setBounds(x, y, camera.getWidth(null), camera.getHeight(null));
    }

    @Override
    public void drawObject(Graphics2D g) {
        camera = timeElapsed == 0 ? camSprite.getNextFrame() : camera;
        g.drawImage(camera, x, y, canvas);
    }

    public void tick() {
        timeElapsed++;
        if (timeElapsed == delay) {
            timeElapsed = 0;
            camBounds.setBounds(x, y, camera.getWidth(null), camera.getHeight(null));
        }
        if (camBounds.intersects(canvas.player.playerBounds)) {
            camera = alarm;
            canvas.player.setState(Player.State.DYING);
        }
    }

    enum State {
        ON, OFF
    }
}
