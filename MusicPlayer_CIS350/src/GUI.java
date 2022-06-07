import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import se.michaelthelin.spotify.model_objects.specification.ArtistSimplified;
import se.michaelthelin.spotify.model_objects.specification.TrackSimplified;

import javax.swing.*;

import org.apache.hc.client5.http.impl.routing.SystemDefaultRoutePlanner;
import org.apache.hc.core5.http.ParseException;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class GUI extends JFrame{
    private API api;

    public static JComboBox<String> localSongList;

    public static JTextArea songInput;
    public static JTextArea artistInput;

    public static JComboBox<String> searchSongList;
    public static JComboBox<String> searchArtistList;
    public static JComboBox<String> searchGenreList;

    public static JComboBox<String> generatedPlaylist;

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
        this.api = new API();
        api.setGUI(this);
        Image img = Toolkit.getDefaultToolkit().getImage(backgroundImg);
        JPanel frame = new BackgroundPanel(img);
        getContentPane().add(frame);

        setSize(500, 500);
        GridLayout layout = new GridLayout(7, 10);
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
        JPanel row6 = new JPanel();
        frame.add(row6);
        JPanel row7 = new JPanel();
        frame.add(row7);

        ///// Row 1 /////

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
        localSongList = new JComboBox<String>(array);
        localSongList.setPreferredSize(new Dimension(400, 20));
        row1.add(localSongList);

        ///// Row 2 /////

        // song inputs
        JLabel songlabel1 = new JLabel("Select Song");
        row2.add(songlabel1);
        songInput = new JTextArea();
        songInput.setPreferredSize(new Dimension(100, 15));
        row2.add(songInput);
        JButton findSimilarSongButton = new JButton();
        findSimilarSongButton.setText("Go");
        findSimilarSongButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Make list of similar songs");
            }
        });
        row2.add(findSimilarSongButton);

        // artist input
        JLabel artistLabel1 = new JLabel("Select Artist");
        row2.add(artistLabel1);
        artistInput = new JTextArea();
        artistInput.setPreferredSize(new Dimension(100, 15));
        row2.add(artistInput);
        JButton findSimilarArtistButton = new JButton();
        findSimilarArtistButton.setText("Go");
        findSimilarArtistButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Make list of similar artists");
            }
        });
        row2.add(findSimilarArtistButton);
        

        ///// Row 3 /////
        
        // song list 
        JLabel songLabel2 = new JLabel("Songs");
        row3.add(songLabel2);
        String[] spotifySongList = new String[0];
        searchSongList = new JComboBox<String>(spotifySongList);
        searchSongList.setPreferredSize(new Dimension(100, 20));
        row3.add(searchSongList);

        // artist list
        JLabel artistLabel2 = new JLabel("Artists");
        row3.add(artistLabel2);
        String[] spotifyArtistList = new String[0];
        searchArtistList = new JComboBox<String>(spotifyArtistList);
        searchArtistList.setPreferredSize(new Dimension(100, 20));
        row3.add(searchArtistList);

        //genre list
        try {
            String[] spotifyGenreList = api.getGenres();
            searchGenreList = new JComboBox<String>(spotifyGenreList);
        } catch (ParseException e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();
        }
        searchGenreList.setPreferredSize(new Dimension(100, 20));
        JLabel genreLabel2 = new JLabel("Genres");
        row3.add(genreLabel2);
        row3.add(searchGenreList);


        ///// Row 4 /////
        JLabel generateLabel = new JLabel("Generate Playlist By:");
        row4.add(generateLabel);
        JButton genPlaylistSong = new JButton("Song");
        genPlaylistSong.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Generate playlist by song");
            }
        });
        row4.add(genPlaylistSong);
        JButton genPlaylistArtist = new JButton("Artist");
        genPlaylistArtist.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Generate playlist by artist");
            }
        });
        row4.add(genPlaylistArtist);
        JButton genPlaylistGenre = new JButton("Genre");
        genPlaylistGenre.addActionListener(new ActionListener() {
            // Generate playlist from genre
            public void actionPerformed(ActionEvent e) {
                String genre = searchGenreList.getSelectedItem().toString();
                try {
                    TrackSimplified[] matches = api.getGenreRec(genre);
                    generatedPlaylist.removeAllItems();
                    if (matches.length > 0) {
                        for (TrackSimplified t : matches){
                            String tracklabel = generateTrackLabel(t);
                            generatedPlaylist.addItem(tracklabel);
                        }
                    }
                } catch (ParseException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        });
        row4.add(genPlaylistGenre);

        ///// Row 5 /////

        JLabel playlistLabel = new JLabel("Playlist:");
        row5.add(playlistLabel);
        generatedPlaylist = new JComboBox<String>(new String[0]);
        generatedPlaylist.setPreferredSize(new Dimension(350, 20));
        row5.add(generatedPlaylist);

        ///// Row 6 /////

        // song nav buttons
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


        row6.add(playButton);
        row6.add(pauseButton);
        row6.add(prevButton);
        row6.add(nextButton);

        ///// Row 7 /////

        //progress bar
        JProgressBar progressBar = new JProgressBar();
        row7.add(progressBar);

        row1.setOpaque(false);
        row3.setOpaque(false);
        row2.setOpaque(false);
        row4.setOpaque(false);
        row5.setOpaque(false);
        row6.setOpaque(false);
        row7.setOpaque(false);
        // Start
        setVisible(true);
    }

    private String generateTrackLabel(TrackSimplified track) {
        ArtistSimplified[] artists = track.getArtists();
        String tracklabel = track.getName() + " (" +  artists[0].getName()+ ")";
        return tracklabel;
    }

    /**
     * https://github.com/manjurulhoque/play-mp3-java
     * @throws JavaLayerException
     * @throws FileNotFoundException
     */
    public static void playSong() throws JavaLayerException, FileNotFoundException {
        String selectedSong = musicfolder + localSongList.getSelectedItem().toString();
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