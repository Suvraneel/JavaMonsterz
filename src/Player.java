import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class Player implements VisibleObjects {
    private final int speed;
    private final Tiles tiles;
    GameCanvas canvas;
    Image image;
    Sprite runningSprite = new Sprite("player/running", 6);
    Sprite jumpingSprite = new Sprite("player/jumping", 8);
    Sprite idleSprite = new Sprite("player/idle", 3);
    Sprite dyingSprite = new Sprite("player/dying", 6);
    Dimension size
            = Toolkit.getDefaultToolkit().getScreenSize();
    int screenHeight = (int) size.getHeight();
    int screenWidth = (int) size.getWidth();
    Rectangle bounds = new Rectangle();
    private State state = State.IDLE;
    private Direction direction = Direction.RIGHT;
    private int x;
    private int y;

    public Player(GameCanvas canvas, int x, int y, Tiles tiles) {
        this.canvas = canvas;
        this.x = x;
        this.y = y;
        this.tiles = tiles;
        speed = 5;
        image = new ImageIcon(Objects.requireNonNull(getClass().getResource("resources/sprites/player/idle/00.png"))).getImage();
        bounds.setBounds(x, y, image.getWidth(null), image.getHeight(null));
        for (int[] tr : tiles.tiles) {
            for (int t : tr)
                System.out.print(t);
            System.out.println();
        }
        System.out.println("==================>" + screenHeight + " " + screenWidth);
    }

    @Override
    public void drawObject(Graphics2D g) {
        g.drawImage(image, x, y, canvas);
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
        bounds.setLocation(x, y);
    }

    private boolean canMove(int x, int y) {
        Rectangle rect = new Rectangle(bounds);
        rect.setLocation(x, y);
        int i = (y * tiles.tiles.length) / screenHeight;
        int j = (x * tiles.tiles[0].length) / screenWidth;
        int x_offset = (screenWidth / tiles.tiles[0].length);
        int y_offset = (screenHeight / tiles.tiles.length);
        Rectangle blk = null;
        System.out.println("i: " + i + " j: " + j + " x:" + rect.getX() + " y:" + rect.getY() + " xmax:" + rect.getMaxX() + " ymax:" + rect.getMaxY());
        if (i <= 0 || i >= tiles.tiles.length || j <= 0 || j >= tiles.tiles[0].length) {
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
            image = dyingSprite.getNextFrame();
        } else if (state == State.JUMPING) {
            image = jumpingSprite.getNextFrame();
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
        // Todo: implement
    }

    enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    enum State {
        IDLE, RUNNING, JUMPING, DYING
    }
}
