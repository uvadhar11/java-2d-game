package main;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.IOException;
import java.net.URL;

// class to manage Sound
public class Sound {
    Clip clip;
    // URL stores file path of these sound files we are using
    // made a URL array with a size of 30 so we can hold up to 30 sounds in this array
    URL soundURL[] = new URL[30];

    public Sound() {
        // have to make sounds in wav since java can only do wav and not mp3
        // getting the file paths of each of the sounds and storing them in the soundURL array
        soundURL[0] = getClass().getResource("/sound/BlueBoyAdventure.wav");
        soundURL[1] = getClass().getResource("/sound/coin.wav");
        soundURL[2] = getClass().getResource("/sound/powerup.wav");
        soundURL[3] = getClass().getResource("/sound/unlock.wav");
        soundURL[4] = getClass().getResource("/sound/fanfare.wav");
    }

    // opening an audio file method, populates clip instance variable with the audio file
    public void setFile(int i) {
        try {
            // use audio input stream and give it the url of the audio file
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
            // get the audio clip and store it in clip
            clip = AudioSystem.getClip();
            // open the audio clip
            clip.open(ais);
        } catch(Exception e) {
            System.out.println(e);
        }
    }

    // plays the actual audio file/clip
    public void play() {
        clip.start();
    }

    // loops the audio clip
    public void loop() {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    // stops the audio clip
    public void stop() {
        clip.stop();
    }
}
