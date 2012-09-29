import gui.AlertPopup;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.MalformedURLException;
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
        // TODO: find a way to avoid using finals
        JFrame frame = new JFrame();

        JLabel lblUrl = new JLabel();
        lblUrl.setLocation(20, 20);
        lblUrl.setSize(30, 30);
        lblUrl.setText("URL");
        frame.add(lblUrl);

        final JTextField txtUrl = new JTextField();
        txtUrl.setLocation(80, 20);
        txtUrl.setSize(200, 30);
        txtUrl.setText(url);
        frame.add(txtUrl);

        JLabel lblPhrase = new JLabel();
        lblPhrase.setLocation(20, 60);
        lblPhrase.setSize(50, 30);
        lblPhrase.setText("Phrase");
        frame.add(lblPhrase);

        final JTextField txtPhrase = new JTextField();
        txtPhrase.setLocation(80, 60);
        txtPhrase.setSize(200, 30);
        txtPhrase.setText(phrase);
        frame.add(txtPhrase);

        JLabel lblDir = new JLabel();
        lblDir.setLocation(20, 100);
        lblDir.setSize(50, 30);
        lblDir.setText("Directory");
        frame.add(lblDir);

        final JTextField txtDir = new JTextField();
        txtDir.setLocation(80, 100);
        txtDir.setSize(200, 30);
        txtDir.setText("D:\\");
        frame.add(txtDir);

        final JButton btnCheck = new JButton();
        btnCheck.setLocation(100, 140);
        btnCheck.setSize(100, 30);
        btnCheck.setText("Check");
        btnCheck.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                btnCheck.setText("Checking...");
                String episodePage = getEpisodePage(txtUrl.getText(), txtPhrase.getText());
                String downloadPage = null;
                String actualDownloadPage = null;
                String realDownloadPage = null;
                String downloadLink = null;
                if(episodePage != null) {
                    downloadPage = getLinkFromRight(episodePage, "\">Mediafire", "href=\"");
                }
                if(downloadPage != null) {
                    actualDownloadPage = getLinkFromLeft(downloadPage, "var url = '", "';");
                }
                if(actualDownloadPage != null) {
                    realDownloadPage = getLinkFromLeft(actualDownloadPage, "URL=", "\">");
                }
                if(realDownloadPage != null) {
                    downloadLink = getLinkFromLeft(realDownloadPage, "kNO = \"", "\";");
                }
                if(downloadLink != null) {
                    try {
                        saveUrl(txtDir.getText() + "video.rar", downloadLink);
                    } catch (IOException e1) {
                        handleException(e1);
                    }
                }
                btnCheck.setText("Check");
            }
        });
        frame.add(btnCheck);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);
        frame.setSize(300, 300);
        frame.setTitle("SiteChecker");
        frame.setVisible(true);
    }

    //gets link to episode download page
    public static String getEpisodePage(String url, String phrase) {
        String oldPage = getWebPage(url);

        //while(true){
            String webpage = getWebPage(url);
            //if(webpage != oldPage) {
                if(webpage.contains(phrase)){
                    String[] content = webpage.split("'>" + phrase)[0].split("href='");
                    return content[content.length-1];
                }
                // Otherwise, try again in five seconds
                else{

                    try {
                        Thread.sleep(refreshDelay);
                    } catch (InterruptedException e) {
                        handleException(e);
                    }
                }
            //}
        //}
        return null;
    }

    //gets desired url
    //string1 = text to the right of link
    //string2 = text to the left of link
    public static String getLinkFromRight(String url, String string1, String string2){
        String webpage = getWebPage(url);
        if(webpage.contains(string1)){
            String[] content = webpage.split(string1)[0].split(string2);
            return content[content.length-1];
        }
        return null;
    }

    //gets desired url
    //string1 = text to the right of link
    //string2 = text to the left of link
    public static String getLinkFromLeft(String url, String string1, String string2){
        String webpage = getWebPage(url);
        if(webpage.contains(string1)){
            String[] content = webpage.split(string1)[1].split(string2);
            return content[0];
        }
        return null;
    }

    //get mediafire download page
    public static String getDownloadPage(String downloadPage){
        // Initialize variables
        String webpage = getWebPage(downloadPage);

        if(webpage.contains("Mediafire")){
            String[] content = webpage.split("\">Mediafire")[0].split("href=\"");
            alertUser(content[content.length-1]);
            return content[content.length-1];
        }
        return null;
    }

    public static String getWebPage(String url){
        // Initialize variables
        String webpage = "";
        String inputLine;

        // TODO: split this stuff into some a little more OOP
        try {
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
            return webpage;
        } catch (Exception e) {
            handleException(e);
        }
        return null;
    }

    public static void scanForChange(String url){
        // TODO: check for null strings and valid url

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

    public static void saveUrl(String filename, String urlString) throws MalformedURLException, IOException
    {
        BufferedInputStream in = null;
        FileOutputStream fout = null;
        try
        {
            in = new BufferedInputStream(new URL(urlString).openStream());
            fout = new FileOutputStream(filename);

            byte data[] = new byte[1024];
            int count;
            while ((count = in.read(data, 0, 1024)) != -1)
            {
                fout.write(data, 0, count);
            }
        }
        finally
        {
            if (in != null)
                in.close();
            if (fout != null)
                fout.close();
        }

        Runtime rt = Runtime.getRuntime();
        Process pr = rt.exec("C:\\Program Files (x86)\\7-Zip\\7z x D:\\video.rar -od:\\video\\");
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
