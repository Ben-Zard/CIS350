import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;

import javax.swing.*;

import org.apache.hc.client5.http.impl.routing.SystemDefaultRoutePlanner;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class GUI extends JFrame{
    private API api;

    private static JComboBox<String> songList;
    private static String musicfolder = "./src/songs/";
    private static String backgroundImg = "./src/img/test2.jpg";
    private static AdvancedPlayer player;

    public void setAPI(API setapi) {
        this.api = setapi;
    }

    public API getAPI() {
        return this.api;
    }

    /**
     * Creates the user interface for the spotify playlist generator
     */
    public GUI() {
        super("Background image");
        Image img = Toolkit.getDefaultToolkit().getImage(backgroundImg);
        JPanel frame = new BackgroundPanel(img);
        getContentPane().add(frame);

        setSize(500, 500);
        GridLayout layout = new GridLayout(5, 10);
        frame.setLayout(layout);


        // add rows
        JPanel row1 = new JPanel();
        frame.add(row1);
        JPanel row2 = new JPanel();
        frame.add(row2);
        JPanel row3 = new JPanel();
        frame.add(row3);
        JPanel row4 = new JPanel();
        frame.add(row4);
        JPanel row5 = new JPanel();
        frame.add(row5);

        // text inputs
        JTextArea songInput = new JTextArea("Select Song");
        JTextArea artistInput = new JTextArea("Select Artist");
        JTextArea genreInput = new JTextArea("Select Genre");
        row2.add(songInput);
        row2.add(artistInput);
        row2.add(genreInput);

        // list songs
        ArrayList<String> songs = new ArrayList<String>();
        File folder = new File(musicfolder);
        File[] listOfFiles = folder.listFiles();
        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                songs.add(file.getName());
            }
        }

        String[] array = songs.toArray(new String[0]);
        songList = new JComboBox<String>(array);
        songList.setPreferredSize(new Dimension(400, 20));
        row1.add(songList);

        // buttons
        JButton playButton = new JButton();
        playButton.setText("Play");
        playButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    playSong();
                } catch (FileNotFoundException | JavaLayerException e1) {
                    e1.printStackTrace();
                }
            }
        });

        JButton pauseButton = new JButton();
        pauseButton.setText("Pause");
        pauseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                pauseSong();
                //Needs to pause song but not stop it
                //It restarts the song as of now when play is pushed again  
            }
        });

        JButton prevButton = new JButton();
        prevButton.setText("Prev");
        prevButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //Need to figure out how to go back the song that was just played
            }
        });

        JButton nextButton = new JButton();
        nextButton.setText("Next");
        nextButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Need to figure out how to go to next song in the list  
            }
        });


        row4.add(playButton);
        row4.add(pauseButton);
        row4.add(prevButton);
        row4.add(nextButton);

        //progress bar
        JProgressBar progressBar = new JProgressBar();
        row5.add(progressBar);

        row1.setOpaque(false);
        row3.setOpaque(false);
        row2.setOpaque(false);
        row4.setOpaque(false);
        row5.setOpaque(false);
        //
        setVisible(true);
    }

    /**
     * https://github.com/manjurulhoque/play-mp3-java
     * @throws JavaLayerException
     * @throws FileNotFoundException
     */
    public static void playSong() throws JavaLayerException, FileNotFoundException {
        String selectedSong = musicfolder + songList.getSelectedItem().toString();
        FileInputStream fis = new FileInputStream(selectedSong);

        player = new AdvancedPlayer(fis);
        new Thread() {
            public void run() {
                try {
                    player.play();
                } catch (JavaLayerException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * Stop playing song
     */
    public static void pauseSong() {
        player.close();
        //Needs to just pause the song and not stop it 
        //Changed method name to pauseSong and replaced line 86
    }

   
}