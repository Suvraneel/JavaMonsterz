import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class Enemy implements VisibleObjects {
    private final int speed;
    int x, y, x_offset, y_offset;
    GameCanvas canvas;
    Image enemy;
    Sprite runningSprite = new Sprite("enemy/cop/running", 8);
    Sprite attackingSprite = new Sprite("enemy/cop/attacking", 10);
    Rectangle enemyBounds = new Rectangle();
    Point rangeMin, rangeMax;
    Direction direction;
    Tiles tiles;
    Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
    int screenHeight = (int) size.getHeight();
    int screenWidth = (int) size.getWidth();

    public Enemy(GameCanvas canvas, int i, int j, Tiles tiles, Point rangeMin, Point rangeMax, String dir, int speed) {
        this.canvas = canvas;
        this.speed = speed;
        enemy = new ImageIcon(Objects.requireNonNull(getClass().getResource("resources/sprites/enemy/cop/running/00.png"))).getImage();
        this.tiles = tiles;
        this.direction = Direction.valueOf(dir);
        x_offset = (screenWidth / tiles.tiles[0].length);
        y_offset = (screenHeight / tiles.tiles.length);
        this.x = j * x_offset;
        this.y = i * y_offset;
        this.rangeMin = new Point(rangeMin.y * x_offset, rangeMin.x * y_offset);
        this.rangeMax = new Point(rangeMax.y * x_offset, rangeMax.x * y_offset);
        enemyBounds.setBounds(x, y, enemy.getWidth(null), enemy.getHeight(null));
    }

    @Override
    public void drawObject(Graphics2D g) {
        enemy = (canvas.player.getState() != Player.State.DYING) ? runningSprite.getNextFrame() : attackingSprite.getNextFrame();
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
