package ch.epfl.cs107.icoop.audio;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;

public class Sound {
    Clip clip;
    URL soundURL[] = new URL[30];
    //adds all the resources to the array of URL's
    public Sound(){
        soundURL[0] = getClass().getResource("/sound/BlueBoyAdventure.wav");
        soundURL[1] = getClass().getResource("/sound/coin.wav");
        soundURL[2] = getClass().getResource("/sound/levelup.wav");


    }
    //sets the file to read out of the array of URL's
    public void setFile(int i){
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
            clip= AudioSystem.getClip();
            clip.open(ais);

        } catch (Exception e) {
        }
    }
    //Starts the clip
    public void play(){
        clip.start();
    }

    //Loops the clip
    public void loop(){
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    //Stops the clip
    public void stop(){
        if (clip!= null) {
            clip.stop();
        }
    }
}
