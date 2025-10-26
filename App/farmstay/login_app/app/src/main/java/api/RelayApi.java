package api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RelayApi {
    @GET("/api/{channel}")
    Call<ResponseBody> setRelayState(
            @Path("channel") int channel,
            @Query("state") int state
    );
}
