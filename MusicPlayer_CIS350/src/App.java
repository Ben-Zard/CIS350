import javax.swing.*;
import java.awt.*;

public class App {
    public static void main(String[] args) throws Exception {
        showGUI();
    }

    public static void showGUI() {
        // create frame
        JFrame frame = new JFrame();
        frame.setSize(500,400);
        frame.setTitle("Music Player");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBackground(Color.gray);
        GridLayout layout = new GridLayout(5,10);
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

        //text inputs
        JTextArea usernameInput = new JTextArea("Username");
        JTextArea passwordInput = new JTextArea("Password");
        row2.add(usernameInput);
        row2.add(passwordInput);

        // buttons
        JButton playButton = new javax.swing.JButton();
        playButton.setText("play");
        JButton pauseButton = new javax.swing.JButton();
        pauseButton.setText("pause");
        JButton prevButton = new javax.swing.JButton();
        prevButton.setText("prev");
        JButton nextButton = new javax.swing.JButton();
        nextButton.setText("next");

        row4.add(playButton);
        row4.add(pauseButton);
        row4.add(prevButton);
        row4.add(nextButton);

        //display after everything is set up
        frame.setVisible(true);
    }
}
