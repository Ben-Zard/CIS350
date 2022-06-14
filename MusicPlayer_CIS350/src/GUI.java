import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.model_objects.specification.ArtistSimplified;
import se.michaelthelin.spotify.model_objects.specification.TrackSimplified;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class GUI extends JFrame {
    private API api;

    public static JComboBox<String> localSongList;

    public static JTextField songInput;
    public static JTextField artistInput;

    public static JComboBox<String> searchGenreList;

    public static JComboBox<String> generatedPlaylist;

    private static String musicfolder = "./src/songs/";
    private static String backgroundImg = "./src/img/test2.jpg";
    private static AdvancedPlayer player;

    /**
     * Set API to handle data from spotify
     * 
     * @param setapi
     */
    public void setAPI(API setapi) {
        this.api = setapi;
    }

    /**
     * Get associated API for this GUI
     * 
     * @return
     */
    public API getAPI() {
        return this.api;
    }

    /**
     * Create the user interface for the spotify playlist generator
     */
    public GUI() {
        super("Background image");
        this.api = new API();
        api.setGUI(this);
        Image img = Toolkit.getDefaultToolkit().getImage(backgroundImg);
        JPanel frame = new BackgroundPanel(img);
        getContentPane().add(frame);

        setSize(500, 500);
        GridLayout layout = new GridLayout(6, 10);
        frame.setLayout(layout);

        ///// Keybindings /////

        // playbutton
        InputMap play = frame.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap player = frame.getActionMap();
        play.put(KeyStroke.getKeyStroke(KeyEvent.VK_1, 0), "1");
        player.put("1", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    playSong();
                } catch (JavaLayerException ex) {
                    ex.printStackTrace();
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        });
        // pausebutton
        InputMap pause = frame.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap pauser = frame.getActionMap();
        pause.put(KeyStroke.getKeyStroke(KeyEvent.VK_2, 0), "2");
        pauser.put("2", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pauseSong();
            }
        });
        // prevbutton
        InputMap prev = frame.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap prever = frame.getActionMap();
        prev.put(KeyStroke.getKeyStroke(KeyEvent.VK_3, 0), "3");
        prever.put("3", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // prevSong();
            }
        });
        // nextbutton
        InputMap next = frame.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap nexter = frame.getActionMap();
        next.put(KeyStroke.getKeyStroke(KeyEvent.VK_4, 0), "4");
        nexter.put("4", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // nextSong();
            }
        });

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

        // genre list
        try {
            String[] spotifyGenreList = api.getGenres();
            searchGenreList = new JComboBox<String>(spotifyGenreList);
        } catch (ParseException e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();
        }
        searchGenreList.setPreferredSize(new Dimension(85, 20));
        JLabel genreLabel2 = new JLabel("Genres");
        row2.add(genreLabel2);
        row2.add(searchGenreList);

        // song inputs
        JLabel songlabel1 = new JLabel("Select Song");
        row2.add(songlabel1);
        songInput = new JTextField();
        songInput.setPreferredSize(new Dimension(65, 15));
        row2.add(songInput);

        // artist input
        JLabel artistLabel1 = new JLabel("Select Artist");
        row2.add(artistLabel1);
        artistInput = new JTextField();
        artistInput.setPreferredSize(new Dimension(65, 15));
        row2.add(artistInput);

        ///// Row 3 /////
        JLabel generateLabel = new JLabel("Generate Playlist By:");
        row3.add(generateLabel);

        JButton genPlaylistSong = new JButton("Song");
        genPlaylistSong.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Generate playlist by song");
                // Generate playlist from song
                String song = songInput.getText().toString();
                try {
                    api.searchSong(song);
                    TrackSimplified[] matches = api.getSongRec(api.trackID);
                    generatedPlaylist.removeAllItems();
                    if (matches.length > 0) {
                        for (TrackSimplified t : matches) {
                            String tracklabel = generateTrackLabel(t);
                            generatedPlaylist.addItem(tracklabel);
                        }
                    }
                } catch (ParseException e1) {
                    e1.printStackTrace();
                }
            }
        });
        row3.add(genPlaylistSong);

        JButton genPlaylistArtist = new JButton("Artist");
        genPlaylistArtist.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Generate playlist by artist");
                System.out.println(artistInput.getText());
                // Generate playlist from artist
                String artist = artistInput.getText().toString();
                try {
                    api.searchArtist(artist);
                    TrackSimplified[] matches = api.getArtistRec(api.artistID);
                    generatedPlaylist.removeAllItems();
                    if (matches.length > 0) {
                        for (TrackSimplified t : matches) {
                            String tracklabel = generateTrackLabel(t);
                            generatedPlaylist.addItem(tracklabel);
                        }
                    }
                } catch (ParseException e1) {
                    e1.printStackTrace();
                }
            }
        });
        row3.add(genPlaylistArtist);

        JButton genPlaylistGenre = new JButton("Genre");
        genPlaylistGenre.addActionListener(new ActionListener() {
            // Generate playlist from genre
            public void actionPerformed(ActionEvent e) {
                String genre = searchGenreList.getSelectedItem().toString();
                try {
                    TrackSimplified[] matches = api.getGenreRec(genre);
                    generatedPlaylist.removeAllItems();
                    if (matches.length > 0) {
                        for (TrackSimplified t : matches) {
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
        row3.add(genPlaylistGenre);

        ///// Row 4 /////

        JLabel playlistLabel = new JLabel("Playlist:");
        row4.add(playlistLabel);
        generatedPlaylist = new JComboBox<String>(new String[0]);
        generatedPlaylist.setPreferredSize(new Dimension(350, 20));
        row4.add(generatedPlaylist);

        ///// Row 5 /////

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
                // Needs to pause song but not stop it
                // It restarts the song as of now when play is pushed again
            }
        });

        JButton prevButton = new JButton();
        prevButton.setText("Prev");
        prevButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Need to figure out how to go back the song that was just played
            }
        });

        JButton nextButton = new JButton();
        nextButton.setText("Next");
        nextButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Need to figure out how to go to next song in the list
            }
        });

        row5.add(playButton);
        row5.add(pauseButton);
        row5.add(prevButton);
        row5.add(nextButton);

        ///// Row 6 /////

        // progress bar
        JProgressBar progressBar = new JProgressBar();
        row6.add(progressBar);

        row1.setOpaque(false);
        row3.setOpaque(false);
        row2.setOpaque(false);
        row4.setOpaque(false);
        row5.setOpaque(false);
        row6.setOpaque(false);
        // Start
        setVisible(true);
    }

    /**
     * Generate a track label string from track object
     * 
     * @param track
     * @return String
     */
    private String generateTrackLabel(TrackSimplified track) {
        ArtistSimplified[] artists = track.getArtists();
        String tracklabel = track.getName() + " (" + artists[0].getName() + ")";
        return tracklabel;
    }

    /**
     * Play local audio foile
     * https://github.com/manjurulhoque/play-mp3-java
     * 
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
        // Needs to just pause the song and not stop it
    }

}