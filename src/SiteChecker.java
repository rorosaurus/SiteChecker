import gui.AlertPopup;

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

// TODO: compile into a nice .jar
public class SiteChecker {

    // TODO: prompt the user for these in a GUI
    // The URL to hit
    private static final String url = "http://www.awesomedl.com/search/label/How%20I%20Met%20Your%20Mother";
    // The string to search for at the URL (if that method is executed)
    private static final String phrase = "How I Met Your Mother Season 8";
    // The milliseconds in-between every refresh
    private static final int refreshDelay = 5000;

    public static void main(String[] args){
        alertUser("This is a test!");
//        scanForString();
//        scanForChange();
    }

    public static void scanForString(){
        // Initialize variables
        String webpage = "";
        String inputLine;

        // TODO: split this stuff into some a little more OOP
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
                    Thread.sleep(refreshDelay);
                }
            }
        } catch (Exception e) {
            handleException(e);
        }
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
                        Thread.sleep(refreshDelay);
                    }
                }
            }
        } catch (Exception e) {
            handleException(e);
        }
    }

    public static void alertUser(final String alert){
        System.out.println(alert);
        // Make a popup
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new AlertPopup(alert);
            }
        });
    }

    public static void handleException(Exception e){
        // TODO: handle these exceptions better.  check for 403's and retry.
        // Bad program, bad!
        e.printStackTrace();
    }
}
