import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * User: Rory
 * Date: 8/14/12
 * Time: 7:22 PM
 */

public class SiteChecker {

    private static final String url = "http://www.awesomedl.com/search/label/How%20I%20Met%20Your%20Mother";
    private static final String phrase = "How I Met Your Mother Season 8";

    public static void main(String[] args){
        alertUser("This is a test!");
//        scanForString();
//        scanForChange();
    }

    public static void scanForString(){
        // Initialize variables
        String webpage = "";
        String inputLine;

        try {
            while(true){
                // Re-connect
                URL site = new URL(url);
                URLConnection connection = site.openConnection();
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                // Reset newHTML to read in new contents
                webpage = "";

                // Read in new contents
                while ((inputLine = in.readLine()) != null) {
                    webpage += inputLine;
                }
                in.close();

                // Compare new and old.  If there's a change, notify the user and break.
                if(webpage.contains(phrase)){
                    alertUser("The string \"" + phrase + "\" has been found at the URL \"" + url + "\"!");
                    break;
                }
                // Otherwise, try again in five seconds
                else{
                    Thread.sleep(5000);
                }
            }
        } catch (Exception e) {
            // Bad program, bad!
            e.printStackTrace();
        }
        System.out.println("YO, BITCH!");
    }

    public static void scanForChange(){
        // Initialize variables
        String originalHTML = "";
        String newHTML = "";
        String inputLine;

        try {
            // Initialize connections
            URL site = new URL(url);
            URLConnection connection = site.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            // Read in the site contents
            while ((inputLine = in.readLine()) != null) {
                originalHTML += inputLine;
            }
            in.close();

            // As long as it exists, let's continue
            if(!originalHTML.equals("")){
                // Infinite loop, until break statement is hit
                while(true){
                    // Re-connect
                    site = new URL(url);
                    connection = site.openConnection();
                    in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    // Reset newHTML to read in new contents
                    newHTML = "";

                    // Read in new contents
                    while ((inputLine = in.readLine()) != null) {
                        newHTML += inputLine;
                    }
                    in.close();

                    // Compare new and old.  If there's a change, notify the user and break.
                    if(!newHTML.equals(originalHTML)){
                        alertUser("The contents at the URL \"" + url + "\" have changed!");
                        break;
                    }
                    // Otherwise, try again in five seconds
                    else{
                        Thread.sleep(5000);
                    }
                }
            }
        } catch (Exception e) {
            // Bad program, bad!
            e.printStackTrace();
        }
    }

    public static void alertUser(final String alert){
        System.out.println(alert);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    // Make a popup
                    JFrame frame = new JFrame("SiteChecker");

                    JTextArea outputArea = new JTextArea(alert);
                    JScrollPane scrollPane = new JScrollPane(outputArea);
                    frame.add(scrollPane);

                    outputArea.setLineWrap(true);
                    outputArea.setAutoscrolls(true);
                    outputArea.setEditable(false);

                    frame.setSize(300, 200);
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.setLocationRelativeTo(null);
                    frame.setVisible(true);

                    // Play a short clip
                    Clip clip = AudioSystem.getClip();
                    clip.loop(Clip.LOOP_CONTINUOUSLY);

                    String programDirectory = System.getProperty("user.dir");
                    clip.open(AudioSystem.getAudioInputStream(new File(programDirectory + "/notify.wav")));

                    clip.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
