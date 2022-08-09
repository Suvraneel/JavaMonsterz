import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class MissionObjectives implements VisibleObjects {
    int x, y, x_offset, y_offset;

    GameCanvas canvas;
    Image missionImg;
    Sprite missionSprite = new Sprite("mission/treasure", 8);
    Rectangle missionBounds = new Rectangle();
    Tiles tiles;
    Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
    int screenHeight = (int) size.getHeight();
    int screenWidth = (int) size.getWidth();
    AudioManager audioManager = new AudioManager();
    public MissionObjectives(GameCanvas canvas, int i, int j, Tiles tiles) {
        this.canvas = canvas;
        this.tiles = tiles;
        x_offset = (screenWidth / tiles.tiles[0].length);
        y_offset = (screenHeight / tiles.tiles.length);
        this.x = j * x_offset;
        this.y = i * y_offset;
        missionImg = new ImageIcon(Objects.requireNonNull(getClass().getResource("resources/sprites/mission/treasure/00.png"))).getImage();
        System.out.println("==================>" + missionImg.getWidth(null) + " " + missionImg.getHeight(null));
        missionBounds.setBounds(x, y, missionImg.getWidth(null), missionImg.getHeight(null));
    }

    @Override
    public void drawObject(Graphics2D g) {
        g.drawImage(missionImg, x, y, canvas);
    }

    public void tick() {
        if (missionBounds.intersects(canvas.player.playerBounds) && canvas.screen == GameCanvas.Screen.GAME) {
            try {
                audioManager.play("src/resources/sounds/treasure.wav", false);
                while (missionSprite.spriteIndex < missionSprite.frames.length - 1) {
                    missionImg = missionSprite.getNextFrame();
                    Thread.sleep(10);
                }
                canvas.screen = GameCanvas.Screen.GAMEWON;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
