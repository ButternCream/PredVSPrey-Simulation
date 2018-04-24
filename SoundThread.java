import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;

public class SoundThread extends Thread {

    private final String resource;
    private static Thread t;
    public static void loop(String resource) {
        t = new SoundThread(resource);
        t.setDaemon(true);
        t.start();
    }
    public static void end()
    {
        t.interrupt();
    }

    public SoundThread(String resource) {
        this.resource = resource;
    }

    @Override
    public void run() {
        Clip clip = null;
        try {
            InputStream in = SoundThread.class.getClassLoader().getResourceAsStream(resource);
            if(in != null) {
                AudioInputStream stream = AudioSystem.getAudioInputStream(in);
                AudioFormat format = stream.getFormat();
                DataLine.Info info = new DataLine.Info(Clip.class, format);
                clip = (Clip) AudioSystem.getLine(info);
                clip.open(stream);
                clip.loop(Clip.LOOP_CONTINUOUSLY);
                do  {
                    try {
                        Thread.sleep(100);
                    } catch(InterruptedException iex) {
                        clip.stop();
                        Thread.currentThread().interrupt();
                    }
                } while(clip.isRunning());
            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
        } finally {
            try {
                if(clip != null) {
                    clip.close();
                }
            } catch(Exception x) {
                x.printStackTrace(System.out);
            }
        }
    }
}