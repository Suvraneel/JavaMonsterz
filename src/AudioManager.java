import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class AudioManager {
    public static void main(String[] args) {
        AudioManager audioManager = new AudioManager();
//        audioManager.play("src/audio/background.wav", false);
    }

    public void play(String fileName, boolean loop) {
        try {
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(new File(fileName)));
            if (loop) clip.loop(Clip.LOOP_CONTINUOUSLY);
            else clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
