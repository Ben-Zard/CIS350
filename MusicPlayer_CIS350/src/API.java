import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.ClientCredentials;
import se.michaelthelin.spotify.model_objects.specification.Artist;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.Recommendations;
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.model_objects.specification.TrackSimplified;
import se.michaelthelin.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;
import se.michaelthelin.spotify.requests.data.browse.GetRecommendationsRequest;
import se.michaelthelin.spotify.requests.data.browse.miscellaneous.GetAvailableGenreSeedsRequest;
import se.michaelthelin.spotify.requests.data.search.simplified.SearchArtistsRequest;
import se.michaelthelin.spotify.requests.data.search.simplified.SearchTracksRequest;

import javax.swing.*;

import org.apache.hc.core5.http.ParseException;

import java.io.IOException;

public class API {
    private GUI gui;
    static final String spotifyClientID = "88950e472b574f9fa0150e8e0c33637f";
    static final String spotifyClientSecret = "e0bf6fc402ee43c797187d9c5a5727a7";

    private static final SpotifyApi spotifyApi = new SpotifyApi.Builder()
            .setClientId(spotifyClientID)
            .setClientSecret(spotifyClientSecret)
            .build();
    private static final ClientCredentialsRequest clientCredentialsRequest = spotifyApi.clientCredentials()
            .build();

    public API() {
      try {
        this.getSpotifyToken();
      } catch (ParseException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }

    public void setGUI(GUI setgui) {
        this.gui = setgui;
    }

    public GUI getGUI() {
        return gui;
    }

     /**
     * generate token for authentication to spotify API 
     * https://github.com/spotify-web-api-java/spotify-web-api-java#Documentation
     * @throws org.apache.hc.core5.http.ParseException
     */
    public void getSpotifyToken() throws org.apache.hc.core5.http.ParseException {
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
    public void searchSong(String title) throws org.apache.hc.core5.http.ParseException {
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
     * get list of similar artists based on string input
     * @param title
     * @throws org.apache.hc.core5.http.ParseException
     */
    public Paging<Artist> searchArtist(String title) throws org.apache.hc.core5.http.ParseException {
        SearchArtistsRequest searchArtistRequest = spotifyApi.searchArtists(title).build();
        try {
            final Paging<Artist> artistPaging = searchArtistRequest.execute();
            return artistPaging;
          } catch (IOException | SpotifyWebApiException e) {
            System.out.println("Error: " + e.getMessage());
          }
          return null;
    }

    /**
     * get list of genres as listed by spotify API
     * @return
     * @throws ParseException
     */
    public String[] getGenres() throws ParseException {
        GetAvailableGenreSeedsRequest getGenresRequest = spotifyApi.getAvailableGenreSeeds().build();
        try {
            final String[] genreList = getGenresRequest.execute();
            return genreList;
          } catch (IOException | SpotifyWebApiException e) {
            System.out.println("Error: " + e.getMessage());
          }
        return null;
    }

    public TrackSimplified[] getGenreRec(String genre) throws ParseException {
      GetRecommendationsRequest getRecsRequest = spotifyApi.getRecommendations().seed_genres(genre).build();
      TrackSimplified[] tracks = getRecommendations(getRecsRequest);
      return tracks;
    }

    private TrackSimplified[] getRecommendations(GetRecommendationsRequest getRecsRequest) throws ParseException {
      try {
        final Recommendations recsList = getRecsRequest.execute();
        TrackSimplified[] tracks = recsList.getTracks();
        return tracks;
      } catch (IOException | SpotifyWebApiException e) {
        System.out.println("Error: " + e.getMessage());
      }
      return null;
    }
}
