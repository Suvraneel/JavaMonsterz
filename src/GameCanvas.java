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
    ArrayList<Camera> cameras = new ArrayList<Camera>();
    Thread thread;

    public GameCanvas() {
        background = new Background(this);
        objects.add(background);
        Tiles tiles = new Tiles(this);
        objects.add(new Tiles(this));
        player = new Player(this, 0, 0, 8, tiles);
        objects.add(player);
        enemies.add(new Enemy(this, 0, 10, tiles, new Point(0, 0), new Point(0, tiles.tiles[0].length), "LEFT", 5));
        enemies.add(new Enemy(this, 6, 10, tiles, new Point(6, 0), new Point(6, 12), "RIGHT", 7));
        enemies.add(new Enemy(this, 2, 9, tiles, new Point(0, 9), new Point(7, 9), "UP", 5));
        objects.addAll(enemies);
        cameras.add(new Camera(this, 3, 2, tiles, 20));
        cameras.add(new Camera(this, 8, 2, tiles, 20));
        objects.addAll(cameras);
        addKeyListener(this);
//        setBackground(Color.BLACK);
        audioManager.play("src/resources/sounds/sneaky.wav", true);
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
        if (player.getState() == Player.State.DYING) {      // Freeze movements when player is dead ie, game over
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
        }
//        System.out.println("Key pressed: " + e.getKeyCode());
        player.move(player.getDirection());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (player.getState() == Player.State.DYING) return;
        switch (e.getKeyCode()) {
            case KeyEvent.VK_A -> {
                player.setState(Player.State.IDLE);
                player.setDirection(Player.Direction.LEFT);
            }
            case KeyEvent.VK_D -> {
                player.setState(Player.State.IDLE);
                player.setDirection(Player.Direction.RIGHT);
            }
            case KeyEvent.VK_W -> {
                player.setState(Player.State.IDLE);
                player.setDirection(Player.Direction.UP);
            }
            case KeyEvent.VK_S -> {
                player.setState(Player.State.IDLE);
                player.setDirection(Player.Direction.DOWN);
            }
            case KeyEvent.VK_SPACE -> {
                player.setState(Player.State.IDLE);
            }
        }
    }

    @Override
    public void run() {
        while (player.getState() != Player.State.DYING) {
            try {
                Thread.sleep(16);
                player.tick();
                for (Enemy enemy : enemies) {
                    enemy.move();
                }
                for (Camera camera : cameras) {
                    camera.tick();
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
        player = new Player(this, 0, 0, 8, tiles);
        objects.add(player);
        enemies.add(new Enemy(this, 0, 10, tiles, new Point(0, 0), new Point(0, tiles.tiles[0].length), "LEFT", 5));
        enemies.add(new Enemy(this, 6, 10, tiles, new Point(6, 0), new Point(6, 12), "RIGHT", 7));
        enemies.add(new Enemy(this, 2, 9, tiles, new Point(0, 9), new Point(7, 9), "UP", 5));
        objects.addAll(enemies);
        cameras.add(new Camera(this, 3, 2, tiles, 20));
        cameras.add(new Camera(this, 8, 2, tiles, 20));
        objects.addAll(cameras);
        Thread thread = new Thread(this);
        thread.start();
    }
}
