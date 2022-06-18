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

import org.apache.hc.core5.http.ParseException;

import java.io.IOException;

public class API {
  private GUI gui;

  private static final String spotifyClientID = "88950e472b574f9fa0150e8e0c33637f";
  private static final String spotifyClientSecret = "e0bf6fc402ee43c797187d9c5a5727a7";

  private String apiToken = "";

  static public String trackID = "";
  static public String artistID = "";

  private static final SpotifyApi spotifyApi = new SpotifyApi.Builder()
      .setClientId(spotifyClientID)
      .setClientSecret(spotifyClientSecret)
      .build();
  private static final ClientCredentialsRequest clientCredentialsRequest = spotifyApi.clientCredentials()
      .build();

  public API() {
    try {
      this.authSpotifyToken();
    } catch (ParseException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  /**
   * Set the GUI
   * @param setgui
   */
  public void setGUI(GUI setgui) {
    this.gui = setgui;
  }

  /**
   * Get the GUI
   * @return
   */
  public GUI getGUI() {
    return gui;
  }

  /**
   * generate token for authentication to spotify API
   * https://github.com/spotify-web-api-java/spotify-web-api-java#Documentation
   * 
   * @throws org.apache.hc.core5.http.ParseException
   */
  public void authSpotifyToken() throws org.apache.hc.core5.http.ParseException {
    try {
      final ClientCredentials clientCredentials = clientCredentialsRequest.execute();

      // Set access token for further "spotifyApi" object usage
      spotifyApi.setAccessToken(clientCredentials.getAccessToken());

      System.out.println("Token: " + clientCredentials.getAccessToken());
      System.out.println("Expires in: " + clientCredentials.getExpiresIn());
      this.apiToken = clientCredentials.getAccessToken();
    } catch (IOException | SpotifyWebApiException e) {
      System.out.println("Error: " + e.getMessage());
    }
  }

  /**
   * get the Spotify API auth token
   */
  public String getAPIToken() {
    return this.apiToken;
  }

  /**
   * get list of similar songs based on string input
   * 
   * @param title
   * @throws org.apache.hc.core5.http.ParseException
   */
  public void searchSong(String title) throws org.apache.hc.core5.http.ParseException {
    SearchTracksRequest searchTracksRequest = spotifyApi.searchTracks(title).build();
    try {
      final Paging<Track> trackPaging = searchTracksRequest.execute();
      System.out.println("Total: " + trackPaging.getTotal());
      trackID = trackPaging.getItems()[0].getId();
    } catch (IOException | SpotifyWebApiException e) {
      System.out.println("Error: " + e.getMessage());
    }
  }

  /**
   * get list of similar artists based on string input
   * 
   * @param title
   * @throws org.apache.hc.core5.http.ParseException
   */
  public void searchArtist(String title) throws org.apache.hc.core5.http.ParseException {
    SearchArtistsRequest searchArtistRequest = spotifyApi.searchArtists(title).build();
    try {
      final Paging<Artist> artistPaging = searchArtistRequest.execute();
      System.out.println("Total: " + artistPaging.getTotal());
      artistID = artistPaging.getItems()[0].getId();
    } catch (IOException | SpotifyWebApiException e) {
      System.out.println("Error: " + e.getMessage());
    }
  }

  /**
   * get list of genres as listed by spotify API
   * 
   * @return list of genre strings
   * @throws ParseException
   */
  public String[] getGenres() throws ParseException {
    GetAvailableGenreSeedsRequest getGenresRequest = spotifyApi.getAvailableGenreSeeds().build();
    try {
      return getGenresRequest.execute();
    } catch (IOException | SpotifyWebApiException e) {
      System.out.println("Error: " + e.getMessage());
    }
    return null;
  }

  /**
   * Generate spotifty API request based on genre
   * @param genre - string
   * @return list of tracks
   * @throws ParseException
   */
  public TrackSimplified[] getGenreRec(String genre) throws ParseException {
    GetRecommendationsRequest getRecsRequest = spotifyApi.getRecommendations().seed_genres(genre).build();
    TrackSimplified[] tracks = getRecommendations(getRecsRequest);
    return tracks;
  }

  /**
   * Generate spotifty API request based on song
   * @param song - string
   * @return list of tracks
   * @throws ParseException
   */
  public TrackSimplified[] getSongRec(String song) throws ParseException {
    GetRecommendationsRequest getRecsRequest = spotifyApi.getRecommendations().seed_tracks(song).build();
    TrackSimplified[] tracks = getRecommendations(getRecsRequest);
    return tracks;
  }

  /**
   * Generate spotifty API request based on artist
   * @param artist - string
   * @return list of tracks
   * @throws ParseException
   */
  public TrackSimplified[] getArtistRec(String artist) throws ParseException {
    GetRecommendationsRequest getRecsRequest = spotifyApi.getRecommendations().seed_artists(artist).build();
    TrackSimplified[] tracks = getRecommendations(getRecsRequest);
    return tracks;
  }

  /**
   * Sends request to spotify API and returns list of tracks
   * @param getRecsRequest
   * @return list of tracks
   * @throws ParseException
   */
  public TrackSimplified[] getRecommendations(GetRecommendationsRequest getRecsRequest) throws ParseException {
    try {
      final Recommendations recsList = getRecsRequest.execute();
      return recsList.getTracks();
    } catch (IOException | SpotifyWebApiException e) {
      System.out.println("Error: " + e.getMessage());
    }
    return null;
  }
}
