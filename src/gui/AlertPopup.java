package gui;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.swing.*;
import java.io.File;

/**
 * User: Rory
 * Date: 9/28/12
 * Time: 4:37 PM
 */

public class AlertPopup extends JFrame {
    public AlertPopup(String alert){
        super("SiteChecker");
        try {
            JTextArea outputArea = new JTextArea(alert);
            JScrollPane scrollPane = new JScrollPane(outputArea);
            add(scrollPane);

            outputArea.setLineWrap(true);
            outputArea.setAutoscrolls(true);
            outputArea.setEditable(false);

            setSize(300, 200);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLocationRelativeTo(null);
            setVisible(true);

            // Play a short clip
            Clip clip = null;
            clip = AudioSystem.getClip();

            clip.loop(Clip.LOOP_CONTINUOUSLY);

            String programDirectory = System.getProperty("user.dir");
            clip.open(AudioSystem.getAudioInputStream(new File(programDirectory + "/notify.wav")));

            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
