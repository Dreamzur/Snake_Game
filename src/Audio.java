import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.io.File;

public class Audio {

    private Clip clip;

    public void playSound() {
        //loads in sound file
        try {
            File file = new File("src/resources/backgroundAudio.wav");
            clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(file));
            //lowers the volume of sound file
            FloatControl gainControl =
                    (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(-10.0f);
            clip.start();
            clip.loop(-1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void stopSound(){
        //stops the WAV file from playing
            clip.stop();
        }
}
