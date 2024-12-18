package ch.epfl.cs107.icoop.audio;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;

public class Sound {
    Clip clip;
    URL soundURL[] = new URL[30];
    public Sound(){
        soundURL[0] = getClass().getResource("/sound/blocked.wav");
        soundURL[1] = getClass().getResource("/sound/BlueBoyAdventure.wav");
        soundURL[2] = getClass().getResource("/sound/burning.wav");
        soundURL[3] = getClass().getResource("/sound/chipwall.wav");
        soundURL[4] = getClass().getResource("/sound/coin.wav");
        soundURL[5] = getClass().getResource("/sound/cursor.wav");
        soundURL[6] = getClass().getResource("/sound/cuttree.wav");
        soundURL[7] = getClass().getResource("/sound/dooropen.wav");
        soundURL[8] = getClass().getResource("/sound/Dungeon.wav");
        soundURL[9] = getClass().getResource("/sound/fanfare.wav");
        soundURL[10] = getClass().getResource("/sound/FinalBatlle.wav");
        soundURL[11] = getClass().getResource("/sound/gameover.wav");
        soundURL[12] = getClass().getResource("/sound/hitmonster.wav");
        soundURL[13] = getClass().getResource("/sound/levelup.wav");
        soundURL[14] = getClass().getResource("/sound/Merchant.wav");
        soundURL[15] = getClass().getResource("/sound/parry.wav");
        soundURL[16] = getClass().getResource("/sound/powerup.wav");
        soundURL[17] = getClass().getResource("/sound/receiveddamage.wav");
        soundURL[18] = getClass().getResource("/sound/sleep.wav");
        soundURL[19] = getClass().getResource("/sound/speak.wav");
        soundURL[20] = getClass().getResource("/sound/stairs.wav");
        soundURL[21] = getClass().getResource("/sound/unlock.wav");


    }
    public void setFile(int i){
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
            clip= AudioSystem.getClip();
            clip.open(ais);

        } catch (Exception e) {
        }
    }
    public void play(){
        clip.start();
    }

    public void loop(){
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }
    public void stop(){
        if (clip!= null) {
            clip.stop();
        }
    }
}
