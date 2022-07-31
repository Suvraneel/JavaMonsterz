import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Tiles implements VisibleObjects {
    GameCanvas canvas;
    int[][] tiles;

    public Tiles(GameCanvas canvas) {
        this.canvas = canvas;
        try {
            Scanner scanner = new Scanner(new File(getClass().getResource("resources/maps/map_1.csv").getFile()));
            String[] tokens = scanner.nextLine().split(",");
            int rows = Integer.parseInt(tokens[0]);
            int columns = Integer.parseInt(tokens[1]);
            tiles = new int[rows][columns];
            for (int i = 0; i < rows; i++) {
                tokens = scanner.nextLine().split(",");
                for (int j = 0; j < columns; j++) {
                    tiles[i][j] = Integer.parseInt(tokens[j]);
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void drawObject(Graphics2D g) {
        int x_offset = canvas.getWidth() / tiles[0].length;
        int y_offset = canvas.getHeight() / tiles.length;
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                if (tiles[i][j] == 1) {
                    g.setColor(Color.BLACK);
                    g.fillRect(j * x_offset, i * y_offset, x_offset, y_offset);
                    g.setColor(Color.RED);
                    g.drawRect(j * x_offset, i * y_offset, x_offset, y_offset);
                }
            }
        }
    }
}
