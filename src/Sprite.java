import javax.swing.*;
import java.awt.*;

public class Sprite {
    Image[] frames;
    int spriteIndex = 0;
    int frameDelay = 5;

    public Sprite(String folder, int count) {
        frames = new Image[count];
        for (int i = 0; i < count; i++) {
            String path = String.format("resources/sprites/%s/%02d.png", folder, i);
            frames[i] = new ImageIcon(getClass().getResource(path)).getImage();
        }
    }

    Image getNextFrame() {
        if (frameDelay == 0) {
            spriteIndex = (spriteIndex + 1) % frames.length;
            frameDelay = 10;
        } else {
            frameDelay--;
        }
        return frames[spriteIndex];
    }
}
