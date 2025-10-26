package com.example.login_app.farmstay_page;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.login_app.R;
import com.github.lzyzsd.circleprogress.CircleProgress;
import com.github.lzyzsd.circleprogress.DonutProgress;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import api.FarmModel;
import api.RentedFarm;
import api.SensorResuilt;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import model.Notification_model;
import api.RetrofitInterface;
import api.RetrofitServer;
import model.SensorData;
import model.SocketIO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FarmFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FarmFragment extends Fragment {

    private TextView tvCreenSoil, tvCreenWater, tvValueSoil, tvValueWater;
    private Timer timer;
    private DonutProgress donutProgressTemp, donutProgressHumi, donutProgressSoil;
    private CircleProgress circleProgressWater;

    private List<Notification_model> listNotify;
    SharedPreferences sharedPreferences;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FarmFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FarmFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FarmFragment newInstance(String param1, String param2) {
        FarmFragment fragment = new FarmFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_farm, container, false);

        donutProgressTemp = view.findViewById(R.id.circleProgressTemp);
        donutProgressHumi = view.findViewById(R.id.circleProgressHumi);
        tvCreenSoil = view.findViewById(R.id.tvCreenSoil);
        tvCreenWater = view.findViewById(R.id.tvCreenWater);
        tvValueSoil = view.findViewById(R.id.tvValueSoil);
        tvValueWater = view.findViewById(R.id.tvValueWater);
        circleProgressWater = view.findViewById(R.id.circleProgressWater);
        donutProgressSoil = view.findViewById(R.id.circleProgressSoil);

        getLatestSensorData();

        return view;
    }

    private void getLatestSensorData() {

        RetrofitServer retrofitServer = new RetrofitServer();
        RetrofitInterface retrofitInterface = retrofitServer.Retrofit();

        SocketIO socketIO = new SocketIO();
        Socket socket = socketIO.socket(getActivity());

        sharedPreferences = getActivity().getSharedPreferences("SaveInfo", MODE_PRIVATE);
        String token = sharedPreferences.getString("token","");

        Call<SensorResuilt> call = retrofitInterface.SocketIO(token);
        call.enqueue(new Callback<SensorResuilt>() {
            @Override
            public void onResponse(Call<SensorResuilt> call, Response<SensorResuilt> response) {
                SensorResuilt result = response.body();
                if(result != null){
                    JsonArray jsondatas = result.getData();
                    Log.e("dataaa", String.valueOf(jsondatas));

                    Map<String, Pair<String, Pair<String, String>>> sensorValues = new HashMap<>();

                    for (int i = 0; i < jsondatas.size(); i++) {
                        JsonObject jsondata = (JsonObject) jsondatas.get(i);
                        String fieldName = jsondata.get("field_name").getAsString();
                        Log.e("fieldName", fieldName);
                        String danger_min = jsondata.get("danger_min").getAsString();
                        String danger_max = jsondata.get("danger_max").getAsString();

                        socket.on(fieldName, new Emitter.Listener() {
                            @Override
                            public void call(Object... args) {
                                // Lấy giá trị cảm biến từ tham số args
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            JSONObject jsonObject = new JSONObject(args[0].toString());
                                            // Thực hiện các thao tác khác với đối tượng jsonObject
                                            String sensorValue = jsonObject.getString("value");
                                            Pair<String, Pair<String, String>> value = new Pair<>(sensorValue, new Pair<>(danger_min, danger_max));
                                            sensorValues.put(fieldName, value);
                                            //test
                                            Log.e("sensorValues", String.valueOf(sensorValues));
                                            //lay gia tri theo tung cam bien
                                            //nhiet do
                                            Pair<String, Pair<String, String>> temp = sensorValues.get("humidity_temperature_sensor_0_data_0");
                                            if (temp != null && !temp.first.isEmpty()) {
                                                // Lấy giá trị value từ Pair
                                                String values = temp.first;
                                                // Chuyển đổi giá trị value sang kiểu float
                                                float tempValue = Float.parseFloat(values);
                                                donutProgressTemp.setProgress(tempValue);
                                                donutProgressTemp.setSuffixText("°C");
                                                //bao loi
                                                String danger_max = temp.second.second;
                                                if(tempValue > Float.parseFloat(danger_max)){
                                                    donutProgressTemp.setFinishedStrokeColor(Color.RED);
                                                } else {
                                                    donutProgressTemp.setFinishedStrokeColor(Color.parseColor("#CDDC39"));
                                                }
                                            } else {
                                                // handle the case where myValue is empty
                                            }
                                            //do am
                                            Pair<String, Pair<String, String>> humi = sensorValues.get("humidity_temperature_sensor_0_data_1");
                                            if (humi != null && !humi.first.isEmpty()) {
                                                // do something with myValue
                                                // Lấy giá trị value từ Pair
                                                String values = humi.first;
                                                // Chuyển đổi giá trị value sang kiểu float
                                                float humiValue = Float.parseFloat(values);
                                                donutProgressHumi.setProgress(humiValue);
                                                donutProgressHumi.setSuffixText("%");
                                            } else {
                                                // handle the case where myValue is empty
                                            }
                                            //do am dat
                                            Pair<String, Pair<String, String>> soilMoisture = sensorValues.get("soil_moisture_sensor_0_data_0");
                                            if (soilMoisture != null && !soilMoisture.first.isEmpty()) {
                                                // do something with myValue
                                                // Lấy giá trị value từ Pair
                                                String values = soilMoisture.first;
                                                // Chuyển đổi giá trị value sang kiểu float
                                                float soilValue = Float.parseFloat(values);
                                                tvValueSoil.setText(values);
                                                donutProgressSoil.setProgress(soilValue);
                                                donutProgressSoil.setSuffixText("%");

                                                String danger_min = soilMoisture.second.first;
                                                if(soilValue < Float.parseFloat(danger_min)){
                                                    tvCreenSoil.setText(R.string.lowSoil);
                                                    tvCreenSoil.setTextColor(Color.parseColor("#F44336"));
                                                    donutProgressSoil.setFinishedStrokeColor(Color.RED);
                                                } else {
                                                    tvCreenSoil.setText(R.string.soilIsNormal);
                                                    tvCreenSoil.setTextColor(Color.WHITE);
                                                    donutProgressSoil.setFinishedStrokeColor(Color.BLUE);
                                                }
                                            } else {
                                                // handle the case where myValue is empty
                                            }
                                            //muc nuoc
                                            Pair<String, Pair<String, String>> waterLevel = sensorValues.get("water_level_sensor_0_data_0");
                                            if (waterLevel != null && !waterLevel.first.isEmpty()) {
                                                // do something with myValue
                                                // Lấy giá trị value từ Pair
                                                String values = waterLevel.first;
                                                // Chuyển đổi giá trị value sang kiểu float
                                                float waterValue = Float.parseFloat(values);
                                                tvValueWater.setText(values);
                                                circleProgressWater.setProgress((int) waterValue);
                                                circleProgressWater.setSuffixText("mm");

                                                String danger_min = waterLevel.second.first;
                                                String danger_max = waterLevel.second.second;
                                                if(waterValue < Float.parseFloat(danger_min)){
                                                    tvCreenWater.setText(R.string.lowWater);
                                                    tvCreenWater.setTextColor(Color.parseColor("#F44336"));
                                                    circleProgressWater.setFinishedColor(Color.parseColor("#F44336"));
                                                } else if(waterValue > Float.parseFloat(danger_max)){
                                                    tvCreenWater.setText(R.string.highWater);
                                                    tvCreenWater.setTextColor(Color.parseColor("#F44336"));
                                                    circleProgressWater.setFinishedColor(Color.parseColor("#F44336"));
                                                } else {
                                                    tvCreenWater.setText(R.string.waterIsNormal);
                                                    tvCreenWater.setTextColor(Color.WHITE);
                                                    circleProgressWater.setFinishedColor(Color.BLUE);
                                                }
                                            } else {
                                                // handle the case where myValue is empty
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<SensorResuilt> call, Throwable t) {

            }
        });

    }
}