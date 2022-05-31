import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.ClientCredentials;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;
import se.michaelthelin.spotify.requests.data.search.simplified.SearchTracksRequest;

import javax.swing.*;
import java.io.IOException;

public class App {
    static final String spotifyClientID = "88950e472b574f9fa0150e8e0c33637f";
    static final String spotifyClientSecret = "e0bf6fc402ee43c797187d9c5a5727a7";

    private static final SpotifyApi spotifyApi = new SpotifyApi.Builder()
            .setClientId(spotifyClientID)
            .setClientSecret(spotifyClientSecret)
            .build();
    private static final ClientCredentialsRequest clientCredentialsRequest = spotifyApi.clientCredentials()
            .build();

     /**
     * generate token for authentication to spotify API 
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

    /**
     * get list of similar songs based on string input
     * @param title
     * @throws org.apache.hc.core5.http.ParseException
     */
    public static void findSimilarSong(String title) throws org.apache.hc.core5.http.ParseException {
        SearchTracksRequest searchTracksRequest = spotifyApi.searchTracks(title).build();
        try {
            final Paging<Track> trackPaging = searchTracksRequest.execute();
            //System.out.println(trackPaging.getItems()[0].toString());
            System.out.println("Total: " + trackPaging.getTotal());
          } catch (IOException | SpotifyWebApiException e) {
            System.out.println("Error: " + e.getMessage());
          }
    }

    /**
     * initialize program functions
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        GUI gui = new GUI();
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getSpotifyToken();
        //findSimilarSong("yo");
    }
}
