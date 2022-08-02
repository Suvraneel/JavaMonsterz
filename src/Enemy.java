import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class Enemy implements VisibleObjects {

    private final int speed;
    int x, y;
    GameCanvas canvas;
    Image enemy;
    Sprite runningSprite = new Sprite("enemy/pink/running", 6);
    Rectangle enemyBounds = new Rectangle();
    Point rangeMin, rangeMax;
    Direction direction = Direction.LEFT;
    Tiles tiles;

    public Enemy(GameCanvas canvas, int x, int y, Tiles tiles, Point rangeMin, Point rangeMax, int speed) {
        this.canvas = canvas;
        this.speed = speed;
        enemy = new ImageIcon(Objects.requireNonNull(getClass().getResource("resources/sprites/enemy/pink/running/00.png"))).getImage();
        this.x = x;
        this.y = y;
        this.rangeMin = rangeMin;
        this.rangeMax = rangeMax;
        this.tiles = tiles;
        enemyBounds.setBounds(x, y, enemy.getWidth(null), enemy.getHeight(null));
    }

    @Override
    public void drawObject(Graphics2D g) {
        enemy = runningSprite.getNextFrame();
        if (direction == Direction.RIGHT) {
            g.drawImage(enemy, x, y, canvas);
        } else {
            g.drawImage(enemy, x + enemy.getWidth(null), y, -enemy.getWidth(null), enemy.getHeight(null), canvas);
        }
    }

    public void move() {
        switch (direction) {
            case UP:
                if (canMove(x, y - speed))
                    y -= speed;
                break;
            case DOWN:
                if (canMove(x, y + enemy.getHeight(canvas) + speed))
                    y += speed;
                break;
            case LEFT:
                if (canMove(x - speed, y))
                    x -= speed;
                break;
            case RIGHT:
                if (canMove(x + enemy.getWidth(canvas) + speed, y))
                    x += speed;
                break;
        }
        System.out.println("enemy x: " + x);
        enemyBounds.setBounds(x, y, enemy.getWidth(null), enemy.getHeight(null));
        System.out.println(enemyBounds);
        canvas.draw();
    }

    private boolean canMove(int x, int y) {
        if (x < rangeMin.x)
            direction = Direction.RIGHT;
        else if (x > rangeMax.x)
            direction = Direction.LEFT;
        else if (y < rangeMin.y)
            direction = Direction.DOWN;
        else if (y > rangeMax.y)
            direction = Direction.UP;
        else if (enemyBounds.intersects(canvas.player.playerBounds)) {
            canvas.player.setState(Player.State.DYING);
        } else return true;
        return false;
    }

    enum Direction {
        UP, DOWN, LEFT, RIGHT
    }
}
