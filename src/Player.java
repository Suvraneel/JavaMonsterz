import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class Player implements VisibleObjects {
    private final Tiles tiles;
    private final int speed;
    GameCanvas canvas;
    Image image;
    Sprite runningSprite = new Sprite("player/running", 5);
    Sprite idleSprite = new Sprite("player/idle", 8);
    Sprite dyingSprite = new Sprite("player/dying", 9);
    Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
    int screenHeight = (int) size.getHeight();
    int screenWidth = (int) size.getWidth();
    Rectangle playerBounds = new Rectangle();
    int x_offset, y_offset;
    private State state = State.IDLE;
    private Direction direction = Direction.RIGHT;
    private int x, y;
    AudioManager audioManager = new AudioManager();
    public Player(GameCanvas canvas, int i, int j, int speed, Tiles tiles) {
        this.canvas = canvas;
        x_offset = (screenWidth / tiles.tiles[0].length);
        y_offset = (screenHeight / tiles.tiles.length);
        this.x = j * x_offset;
        this.y = i * y_offset;
        this.speed = speed;
        this.tiles = tiles;
        image = new ImageIcon(Objects.requireNonNull(getClass().getResource("resources/sprites/player/idle/00.png"))).getImage();
        playerBounds.setBounds(x, y, image.getWidth(null), image.getHeight(null));
        for (int[] tr : tiles.tiles) {
            for (int t : tr)
                System.out.print(t);
            System.out.println();
        }
        System.out.println("==================>" + screenHeight + " " + screenWidth);
    }

    @Override
    public void drawObject(Graphics2D g) {
        if (direction == Direction.RIGHT) {
            g.drawImage(image, x, y, canvas);
        } else {
            g.drawImage(image, x + image.getWidth(null), y, -image.getWidth(null), image.getHeight(null), canvas);
        }
    }

    public void move(Direction direction) {
        switch (direction) {
            case UP:
                if (canMove(x, y - speed))      // x, y + y_offset - speed
                    y -= speed;
                break;
            case DOWN:
                if (canMove(x, y + image.getHeight(canvas) + speed))     // x, y+imgHt+speed
                    y += speed;
                break;
            case LEFT:
                if (canMove(x - speed, y + image.getHeight(canvas)))   // x+x_offset-speed, y
                    x -= speed;
                break;
            case RIGHT:
                if (canMove(x + image.getWidth(canvas) + speed, y + image.getHeight(canvas)))   // x+imgWd+speed, y+imgHt
                    x += speed;
                break;
        }
        playerBounds.setLocation(x, y);
    }

    private boolean canMove(int x, int y) {
        Rectangle rect = new Rectangle(playerBounds);
        rect.setLocation(x, y);
        int i = (y * tiles.tiles.length) / screenHeight;
        int j = (x * tiles.tiles[0].length) / screenWidth;
        int x_offset = (screenWidth / tiles.tiles[0].length);
        int y_offset = (screenHeight / tiles.tiles.length);
        Rectangle blk;
        System.out.println("i: " + i + " j: " + j + " x:" + rect.getX() + " y:" + rect.getY() + " xmax:" + rect.getMaxX() + " ymax:" + rect.getMaxY());
        if (i < 0 || i >= tiles.tiles.length || j < 0 || j >= tiles.tiles[0].length) {
            return false;
        }
        if (tiles.tiles[i][j] == 1) {
            blk = new Rectangle(j * x_offset, i * y_offset, x_offset + speed, y_offset + speed);
            System.out.println("blk: " + blk);
            return !rect.intersects(blk);
        }
        return true;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
        if (state == State.DYING) {
            try {
                audioManager.play("src/resources/sounds/die.wav", false);
                while (dyingSprite.spriteIndex < dyingSprite.frames.length - 1) {
                    image = dyingSprite.getNextFrame();
                    Thread.sleep(10);
                }
                canvas.screen = GameCanvas.Screen.GAMEOVER;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else if (state == State.RUNNING) {
            image = runningSprite.getNextFrame();
        } else {
            image = idleSprite.getNextFrame();
        }
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void tick() {
        // Todo: Anything random left to render
    }

    enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    enum State {
        IDLE, RUNNING, DYING
    }
}
