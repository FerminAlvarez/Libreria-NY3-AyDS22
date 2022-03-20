package ayds.newyork.songinfo.moredetails.fulllogic;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NYTimesAPI {

  @GET("articlesearch.json?api-key=fFnIAXXz8s8aJ4dB8CVOJl0Um2P96Zyx")
  Call<String> getArtistInfo(@Query("q") String artist);

}
