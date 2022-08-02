import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.List;

public class GameCanvas extends Canvas implements KeyListener, Runnable {
    List<VisibleObjects> objects = new ArrayList<VisibleObjects>();
    Background background;
    AudioManager audioManager = new AudioManager();
    Tiles tiles;
    Player player;
    Enemy enemy;

    public GameCanvas() {
        background = new Background(this);
        objects.add(background);
        Tiles tiles = new Tiles(this);
        objects.add(new Tiles(this));
        player = new Player(this, 0, 0, 5, tiles);
        objects.add(player);
        enemy = new Enemy(this, 100, 0, tiles, new Point(0, 0), new Point(200, 0));
        objects.add(enemy);
        addKeyListener(this);
//        setBackground(Color.BLACK);
        audioManager.play("src/resources/sounds/diablo.wav", true);
//        Thread thread = new Thread(this);
//        thread.start();
    }

    public void draw() {
        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(2);
            return;
        }
        Graphics2D g = (Graphics2D) bs.getDrawGraphics();
        for (VisibleObjects object : objects) {
            object.drawObject(g);
//            System.out.println("Drawing object: " + object.getClass().getSimpleName());
        }
        g.dispose();
        bs.show();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ESCAPE -> System.exit(0);
            case KeyEvent.VK_A -> {
                player.setState(Player.State.RUNNING);
                player.setDirection(Player.Direction.LEFT);
            }
            case KeyEvent.VK_D -> {
                player.setState(Player.State.RUNNING);
                player.setDirection(Player.Direction.RIGHT);
            }
            case KeyEvent.VK_W -> {
                player.setState(Player.State.RUNNING);
                player.setDirection(Player.Direction.UP);
            }
            case KeyEvent.VK_S -> {
                player.setState(Player.State.RUNNING);
                player.setDirection(Player.Direction.DOWN);
            }
            case KeyEvent.VK_SPACE -> {
                player.setState(Player.State.JUMPING);
                player.setDirection(Player.Direction.UP);
            }
        }
//        System.out.println("Key pressed: " + e.getKeyCode());
        player.move(player.getDirection());
        player.tick();
        draw();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void run() {
        while (true) {
            enemy.move();
            draw();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }
}
