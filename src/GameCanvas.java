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
    ArrayList<MissionObjectives> missionObjectives = new ArrayList<MissionObjectives>();
    Thread thread;
    Screen screen = Screen.MENU;
    Menu menu;
    public GameCanvas() {
        addKeyListener(this);
        setBackground(Color.BLACK);
        audioManager.play("src/resources/sounds/sneaky.wav", true);
        exitToMenu();
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
        switch (screen) {
            case MENU -> {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP -> menu.moveUp();
                    case KeyEvent.VK_DOWN -> menu.moveDown();
                    case KeyEvent.VK_ENTER -> menu.select();
                    case KeyEvent.VK_ESCAPE -> System.exit(0);
                }
            }
            case GAMEOVER -> {
                objects.add(new GameOver(this));
                draw();
                // Freeze movements when player is dead ie, game over
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
                    exitToMenu();
                else if (e.getKeyCode() == KeyEvent.VK_ENTER)
                    reset();
            }
            case GAME -> {
                switch (e.getKeyCode()) {
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
                    case KeyEvent.VK_ESCAPE -> exitToMenu();
                }
//        System.out.println("Key pressed: " + e.getKeyCode());
                player.move(player.getDirection());
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (screen != Screen.GAME) return;
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
                for (MissionObjectives missionObjective : missionObjectives) {
                    missionObjective.tick();
                }
                draw();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void reset() {
        if(thread!=null)
            thread.interrupt();
        enemies.clear();
        cameras.clear();
        missionObjectives.clear();
        background = new Background(this, "Dungeon" );
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
        missionObjectives.add(new MissionObjectives(this, 4, 13, tiles));
        objects.addAll(missionObjectives);
        Thread thread = new Thread(this);
        thread.start();
    }

    public void exitToMenu() {
        if(thread!=null)
            thread.interrupt();
        objects.clear();
        enemies.clear();
        cameras.clear();
        missionObjectives.clear();
        screen = Screen.MENU;
        objects.add(new Background(this, "Jailbreak"));
        menu = new Menu(this);
        objects.add(menu);
    }

    enum Screen {
        MENU, GAME, GAMEOVER
    }
}
