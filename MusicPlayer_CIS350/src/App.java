import javax.swing.*;
public class App {

    /**
     * initialize program functions
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        GUI gui = new GUI();
        API api = new API();
        gui.setAPI(api);
        api.setGUI(gui);
        api.getSpotifyToken();
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
