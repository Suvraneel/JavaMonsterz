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
    ArrayList<Enemy> enemies = new ArrayList<Enemy>();
    Thread thread;

    public GameCanvas() {
        background = new Background(this);
        objects.add(background);
        Tiles tiles = new Tiles(this);
        objects.add(new Tiles(this));
        player = new Player(this, 0, 0, 5, tiles, 8);
        objects.add(player);
        enemies.add(new Enemy(this, 4, 5, tiles, new Point(4, 3), new Point(4, 8), "LEFT", 6));
        enemies.add(new Enemy(this, 1, 7, tiles, new Point(1, 7), new Point(1, 12), "RIGHT", 4));
        objects.addAll(enemies);
        addKeyListener(this);
//        setBackground(Color.BLACK);
        audioManager.play("src/resources/sounds/diablo.wav", true);
        thread = new Thread(this);
        thread.start();
    }

    public void draw() {
        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
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
        if (player.getState() == Player.State.DYING) {        // Freeze movements when player is dead ie, game over
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
                System.exit(0);
            else if (e.getKeyCode() == KeyEvent.VK_ENTER)
                reset();
            else return;
        }
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
                player.isSpacePressed = true;
            }
        }
//        System.out.println("Key pressed: " + e.getKeyCode());
        player.move(player.getDirection());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (player.getState() == Player.State.DYING) return;
        switch (e.getKeyCode()) {
            case KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_W, KeyEvent.VK_S -> {
                player.setState(Player.State.IDLE);
                player.setDirection(Player.Direction.NONE);
            }
            case KeyEvent.VK_SPACE -> {
                player.isSpacePressed = false;
                player.setState(Player.State.IDLE);
            }
        }
    }

    @Override
    public void run() {
        while (player.getState() != Player.State.DYING) {
            try {
                Thread.sleep(30);
                player.tick();
                for (Enemy enemy : enemies) {
                    enemy.move();
                }
                draw();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void gameOver() {
//        audioManager.play("src/resources/sounds/gameover.wav", false);
        objects.add(new GameOver(this));
    }

    public void reset() {
        thread.interrupt();
        objects.clear();
        enemies.clear();
        background = new Background(this);
        objects.add(background);
        Tiles tiles = new Tiles(this);
        objects.add(new Tiles(this));
        player = new Player(this, 0, 0, 8, tiles, 8);
        objects.add(player);
        enemies.add(new Enemy(this, 4, 5, tiles, new Point(4, 3), new Point(4, 8), "LEFT", 7));
        enemies.add(new Enemy(this, 2, 7, tiles, new Point(2, 7), new Point(2, 12), "RIGHT", 7));
        objects.addAll(enemies);
        Thread thread = new Thread(this);
        thread.start();
    }
}
