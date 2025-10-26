package api;

import com.google.gson.JsonObject;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface RetrofitInterface {

    @POST("auth/login")
    Call<JsonObject> executeLogin(@Body HashMap<String, String> map);

    @POST("auth/register")
    Call<Response> executeSignup (@Body HashMap<String, String> map);

    @GET("auth/logout")
    Call<LoginResult> Logout(@Header("authenticate-jwt") String token);

    @POST("user/active")
    Call<LoginResult> activeEmail (@Header("authenticate-jwt") String token, @Body HashMap<String, String> map);

    @PUT("user/active")
    Call<LoginResult> verifyAccount (@Header("authenticate-jwt") String token, @Body HashMap<String, String> map);

    @POST("user/forgot-password")
    Call<LoginResult> forgotPassword (@Body HashMap<String, String> map);

    @PUT("user/forgot-password")
    Call<LoginResult> resetPass (@Body HashMap<String, String> map);

    @POST("user/otp")
    Call<LoginResult> Active (@Body HashMap<String, String> map);

    @PUT("user/change-password")
    Call<LoginResult> ChangePass (@Header("authenticate-jwt") String token, @Body HashMap<String, String> map);

    @GET("user")
    Call<LoginResult> getUser(@Header("authenticate-jwt") String token);

    @GET("farmstays")
    Call<FarmModel> getFarm(@Header("authenticate-jwt") String token);

    //du lieu cam bien
    @GET("customer/farmstay/equipments/fields")
    Call<SensorResuilt> SocketIO (@Header("authenticate-jwt") String token);

    @GET("customer/farmstay/equipments/fields/latest_data?field=relay_0_data_0")
    Call<SensorResuilt> RelayValue_0 (@Header("authenticate-jwt") String token);

    @GET("customer/farmstay/equipments/fields/latest_data?field=relay_0_data_1")
    Call<SensorResuilt> RelayValue_1 (@Header("authenticate-jwt") String token);

    @GET("customer/farmstay/equipments/fields/latest_data?field=relay_0_data_2")
    Call<SensorResuilt> RelayValue_2 (@Header("authenticate-jwt") String token);

    @GET("customer/farmstay/equipments/fields/latest_data?field=relay_0_data_3")
    Call<SensorResuilt> RelayValue_3 (@Header("authenticate-jwt") String token);

    @GET("customer/farmstay")
    Call<RentedFarm> RentedFarm (@Header("authenticate-jwt") String token);

}
