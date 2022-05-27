import javax.swing.*;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;

import se.michaelthelin.spotify.*;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;
import se.michaelthelin.spotify.requests.data.search.simplified.SearchTracksRequest;
import se.michaelthelin.spotify.model_objects.credentials.ClientCredentials;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.Track;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

public class App {
    private static JComboBox<String> songList;
    private static String musicfolder = "./src/songs/";
    private static AdvancedPlayer player;

    static final String spotifyClientID = "88950e472b574f9fa0150e8e0c33637f";
    static final String spotifyClientSecret = "e0bf6fc402ee43c797187d9c5a5727a7";

    private static final SpotifyApi spotifyApi = new SpotifyApi.Builder()
            .setClientId(spotifyClientID)
            .setClientSecret(spotifyClientSecret)
            .build();
    private static final ClientCredentialsRequest clientCredentialsRequest = spotifyApi.clientCredentials()
            .build();

    public static void main(String[] args) throws Exception {
        showGUI();
        getSpotifyToken();
    }

    /**
     * This is a sample of javadocs
     * 
     * @return void
     * 
     * 
     */
    public static void showGUI() {
        // create frame
        JFrame frame = new JFrame();
        frame.setSize(500, 400);
        frame.setTitle("Music Player");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBackground(Color.gray);
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
            for (int i = 0; i < listOfFiles.length; i++) {
                songs.add(listOfFiles[i].getName());
            }
        }

        String[] array = songs.toArray(new String[0]);
        songList = new JComboBox<String>(array);
        songList.setPreferredSize(new Dimension(400, 20));
        row1.add(songList);

        // buttons
        JButton playButton = new javax.swing.JButton();
        playButton.setText("play");
        playButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    playSong();
                } catch (FileNotFoundException | JavaLayerException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        });
        JButton pauseButton = new javax.swing.JButton();
        pauseButton.setText("pause");
        pauseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                stopSong();
            }
        });
        JButton prevButton = new javax.swing.JButton();
        prevButton.setText("prev");
        JButton nextButton = new javax.swing.JButton();
        nextButton.setText("next");

        row4.add(playButton);
        row4.add(pauseButton);
        row4.add(prevButton);
        row4.add(nextButton);

        //progress bar
        JProgressBar progressBar = new JProgressBar();
        row5.add(progressBar);

        // display after everything is set up
        frame.setVisible(true);
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
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * Stop playing song
     */
    public static void stopSong() {
        player.close();
    }

    /**
     * https://github.com/spotify-web-api-java/spotify-web-api-java#Documentation
     * @throws org.apache.hc.core5.http.ParseException
     */
    public static void getSpotifyToken() throws org.apache.hc.core5.http.ParseException {
        try {
            final ClientCredentials clientCredentials = clientCredentialsRequest.execute();
      
            // Set access token for further "spotifyApi" object usage
            spotifyApi.setAccessToken(clientCredentials.getAccessToken());
      
            System.out.println("Token: " + clientCredentials.getAccessToken());
            System.out.println("Expires in: " + clientCredentials.getExpiresIn());
          } catch (IOException | SpotifyWebApiException e) {
            System.out.println("Error: " + e.getMessage());
          }
    }

    public static void findSimilarSong(String title) throws org.apache.hc.core5.http.ParseException {
        SearchTracksRequest searchTracksRequest = spotifyApi.searchTracks(title).build();
        try {
            final Paging<Track> trackPaging = searchTracksRequest.execute();
      
            System.out.println("Total: " + trackPaging.getTotal());
          } catch (IOException | SpotifyWebApiException e) {
            System.out.println("Error: " + e.getMessage());
          }
    }
}
