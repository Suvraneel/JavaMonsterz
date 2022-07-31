import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class Player implements VisibleObjects {
    private final int speed;
    GameCanvas canvas;
    Image image;
    Sprite runningSprite = new Sprite("player/running", 6);
    Sprite jumpingSprite = new Sprite("player/jumping", 8);
    Sprite idleSprite = new Sprite("player/idle", 3);
    Sprite dyingSprite = new Sprite("player/dying", 6);
    private State state = State.IDLE;
    private Direction direction = Direction.RIGHT;
    private int x;
    private int y;

    public Player(GameCanvas canvas, int x, int y) {
        this.canvas = canvas;
        this.x = x;
        this.y = y;
        speed = 5;
        image = new ImageIcon(Objects.requireNonNull(getClass().getResource("resources/sprites/player/idle/00.png"))).getImage();
    }

    @Override
    public void drawObject(Graphics2D g) {
        g.drawImage(image, x, y, canvas);

    }

    public void move(Direction direction) {
        switch (direction) {
            case UP:
                y -= speed;
                break;
            case DOWN:
                y += speed;
                break;
            case LEFT:
                x -= speed;
                break;
            case RIGHT:
                x += speed;
                break;
        }
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
        if (this.x < 0) {
            this.x = 0;
        } else if (this.x > canvas.getWidth() - image.getWidth(null)) {
            this.x = canvas.getWidth() - image.getWidth(null);
        } else if (this.y < 0) {
            this.y = 0;
        } else if (this.y > canvas.getHeight() - image.getHeight(null)) {
            this.y = canvas.getHeight() - image.getHeight(null);
        }
    }

    enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    enum State {
        IDLE, RUNNING, JUMPING, DYING
    }
}
