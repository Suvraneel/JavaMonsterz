import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class Enemy implements VisibleObjects {

    int x, y;
    GameCanvas canvas;
    Image enemy;
    Sprite runningSprite = new Sprite("enemy/pink/running", 6);
    Rectangle enemyBounds = new Rectangle();
    Point rangeMin, rangeMax;

    public Enemy(GameCanvas canvas, int x, int y, Tiles tiles, Point rangeMin, Point rangeMax) {
        this.canvas = canvas;
        enemy = new ImageIcon(Objects.requireNonNull(getClass().getResource("resources/sprites/enemy/pink/running/00.png"))).getImage();
        this.x = x;
        this.y = y;
        this.rangeMin = rangeMin;
        this.rangeMax = rangeMax;
        enemyBounds.setBounds(x, y, enemy.getWidth(null), enemy.getHeight(null));
    }

    @Override
    public void drawObject(Graphics2D g) {
        g.drawImage(enemy, x, y, canvas);
    }

    public void move() {
        if (x < rangeMax.x) {
            x += 5;
        } else if (x > rangeMin.x) {
            x -= 5;
        }
        System.out.println("enemy x: " + x);
        enemyBounds.setBounds(x, y, enemy.getWidth(null), enemy.getHeight(null));
    }
}
