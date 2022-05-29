import javazoom.jl.decoder.JavaLayerException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Background extends JFrame {
    private static JComboBox<String> songList;
    private static String musicfolder = "./src/songs/";
    private static String backgroundImg = "./src/img/test2.jpg";


    public Background() {
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
                    App.playSong();
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
                App.stopSong();
            }
        });
        JButton prevButton = new JButton();
        prevButton.setText("prev");
        JButton nextButton = new JButton();
        nextButton.setText("next");


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
}
/*
    public static void main( String args[] )
    {
        Background app = new Background();
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
} // end Background class*/
