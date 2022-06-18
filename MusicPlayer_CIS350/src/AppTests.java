import static org.junit.Assert.*;

import java.awt.Dimension;

import org.junit.Test;

import se.michaelthelin.spotify.model_objects.specification.TrackSimplified;

public class AppTests {

    @Test
    /*
     * Test to make sure the GUI can be created without exceptions being thrown
     */
    public void testGUI() {
        try {
            GUI gui = new GUI();
            Dimension panelsize = gui.getSize();
            assertTrue(panelsize.getWidth() == 500.0 && panelsize.getHeight() == 500.0);
        } catch (Exception e) {
            fail();
        }
    }

    /**
     * Make sure that the GUI and API are both correctly setting each other as attributes
     */
    @Test
    public void testGUIAPIIntegration() {
        try {
            GUI gui = new GUI();
            API api = gui.getAPI();
            assertTrue(api.getGUI() == gui);
        } catch (Exception e) {
            fail();
        }
    }

    /**
     * Test if API is able to create authentication token from Spotify API
     */
    @Test
    public void testConnectToken() {
        API api = new API();
        String token = api.getAPIToken();
        assertTrue(token.length() > 0);
    }

    @Test
    /**
     * Check that generating a list of similar songs creates a list of tracks
     */
    public void testSongRecs() {
        API api = new API();
        try {
            api.searchSong("Yellow Submarine");
            TrackSimplified[] matches = api.getSongRec(api.trackID);
            assertTrue(matches.length > 0);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    /**
     * Check that generating a list of similar songs creates a list of tracks
     */
    public void testArtistRecs() {
        API api = new API();
        try {
            api.searchArtist("Red Hot Chili Peppers");
            TrackSimplified[] matches = api.getArtistRec(api.artistID);
            assertTrue(matches.length > 0);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    /**
     * Check that API is able to provide list of genres
     */
    public void testGenreList() {
        API api = new API();
        try {
            String[] spotifyGenreList = api.getGenres();
            assertTrue(spotifyGenreList.length > 0);
        } catch (Exception e) {
            fail();
        }
    }
    
    @Test
    /**
     * Check that generating a list of similar songs creates a list of tracks
     */
    public void testGenreRecs() {
        API api = new API();
        try {
            String[] spotifyGenreList = api.getGenres();
            TrackSimplified[] matches = api.getGenreRec(spotifyGenreList[0]);
            assertTrue(matches.length > 0);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    /**
     * Check that generating a displayable label from a track returned by API
     */
    public void testTrackLabel() {
        API api = new API();
        try {
            String[] spotifyGenreList = api.getGenres();
            TrackSimplified[] matches = api.getGenreRec(spotifyGenreList[0]);
            String label = GUI.generateTrackLabel(matches[0]);
            assertTrue(label.length() > 0 && label.contains("(") && label.contains(")"));
        } catch (Exception e) {
            fail();
        }
    }

}
