package api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitServer2 {
    private Retrofit retrofit;
    private RelayApi relayApi;
    private String BASE_URL = "http://farmstays.me:8888/api/"; //ipv4 address

    public RelayApi Retrofit(){
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        relayApi = retrofit.create(RelayApi.class);
        return relayApi;
    }
}
