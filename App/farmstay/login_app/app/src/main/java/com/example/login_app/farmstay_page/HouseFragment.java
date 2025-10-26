package com.example.login_app.farmstay_page;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.login_app.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import api.RentedFarm;
import api.SensorResuilt;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import model.Notification_model;
import api.RelayApi;
import api.RetrofitInterface;
import api.RetrofitServer;
import api.RetrofitServer2;
import model.SocketIO;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HouseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HouseFragment extends Fragment {

    private Timer timer;
    private ImageView mImageView, imLamp1, imLamp2, imLamp3, imFan;
    private ConstraintLayout mLayout;
    private TextView warningTextView, tvTemp, tvHumi, tvMessage, tvFarmName, tvFarmAddress;
    private SwitchCompat switch1;
    private SwitchCompat switch2;
    private SwitchCompat switch3;
    private SwitchCompat switchFan;
    private Button switch4, switch5;
    private ScrollView scrollViewHouse;

    private List<Notification_model> listNotify;
    SharedPreferences sharedPreferences;

    public static final String CHANNEL_ID = "farmstay";

    SocketIO socketIO;
    Socket socket;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HouseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HouseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HouseFragment newInstance(String param1, String param2) {
        HouseFragment fragment = new HouseFragment();
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
        View view = inflater.inflate(R.layout.fragment_house, container, false);

        warningTextView = view.findViewById(R.id.warningTextView);
        mImageView = view.findViewById(R.id.ImFire);
        mLayout = view.findViewById(R.id.layout);
        imLamp1 = view.findViewById(R.id.ImLamp1);
        imLamp2 = view.findViewById(R.id.ImLamp2);
        imLamp3 = view.findViewById(R.id.ImLamp3);
        imFan = view.findViewById(R.id.imFan);
        tvTemp = view.findViewById(R.id.tvTemp);
        tvHumi = view.findViewById(R.id.tvHumi);
        switch1 = view.findViewById(R.id.switchBtn1);
        switch2 = view.findViewById(R.id.switchBtn2);
        switch3 = view.findViewById(R.id.switchBtn3);
        switch4 = view.findViewById(R.id.btnLampOFF);
        switch5 = view.findViewById(R.id.btnLampON);
        switchFan = view.findViewById(R.id.switchFan);
        tvFarmName = view.findViewById(R.id.farmName);
        tvFarmAddress = view.findViewById(R.id.farmAddress);

        socketIO = new SocketIO();
        socket = socketIO.socket(getActivity());

        RetrofitServer retrofitServer = new RetrofitServer();
        RetrofitInterface retrofitInterface = retrofitServer.Retrofit();

        sharedPreferences = getActivity().getSharedPreferences("SaveInfo",MODE_PRIVATE);
        String token = sharedPreferences.getString("token","");

        Call<RentedFarm> callFarm = retrofitInterface.RentedFarm(token);
        callFarm.enqueue(new Callback<RentedFarm>() {
            @Override
            public void onResponse(Call<RentedFarm> call, Response<RentedFarm> response) {
                RentedFarm result = response.body();
                if(result != null){
                    JsonObject data = result.getData();

                    String nameFarm = data.get("name").getAsString();
                    tvFarmName.setText(nameFarm);

                    JsonObject addressFull = (JsonObject) data.get("address");
                    String address = addressFull.get("specific_address").getAsString();
                    tvFarmAddress.setText(address);
                }
            }

            @Override
            public void onFailure(Call<RentedFarm> call, Throwable t) {

            }
        });

        Map<String, Pair<String, String>> relayValues = new HashMap<>();

        Call<SensorResuilt> call = retrofitInterface.SocketIO(token);
        call.enqueue(new Callback<SensorResuilt>() {
            @Override
            public void onResponse(Call<SensorResuilt> call, Response<SensorResuilt> response) {
                SensorResuilt result = response.body();
                if(result != null){
                    JsonArray jsondatas = result.getData();
//                Log.e("dataaa", String.valueOf(jsondatas));

                    Map<String, Pair<String, Pair<String, String>>> sensorValues = new HashMap<>();

                    for (int i = 0; i < jsondatas.size(); i++) {
                        JsonObject jsondata = (JsonObject) jsondatas.get(i);
                        String fieldName = jsondata.get("field_name").getAsString();
                        Log.e("fieldName", fieldName);
                        String danger_min = jsondata.get("danger_min").getAsString();
                        String danger_max = jsondata.get("danger_max").getAsString();

                        String farmstay_id = jsondata.get("farmstay_id").getAsString();
                        String hardware_id = jsondata.get("hardware_id").getAsString();

                        Pair<String, String> relays = new Pair<>(farmstay_id, hardware_id);
                        relayValues.put(fieldName, relays);

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
                                            //bao chay
                                            Pair<String, Pair<String, String>> fire = sensorValues.get("fire_sensor_0_data_0");
                                            if (fire != null && !fire.first.isEmpty()) {
                                                // Lấy giá trị value từ Pair
                                                String values = fire.first;
                                                // Chuyển đổi giá trị value sang kiểu float
                                                float fireValue = Float.parseFloat(values);
                                                //bao loi
                                                String danger_max = fire.second.second;
                                                if (fireValue == 1) {
                                                    mLayout.setBackgroundResource(R.drawable.bg_icon_red);
                                                } else {
                                                    mLayout.setBackgroundResource(R.drawable.bg_icon_2);
                                                }
                                            }
                                            //nhiet do
                                            Pair<String, Pair<String, String>> temp = sensorValues.get("humidity_temperature_sensor_0_data_0");
                                            if (temp != null && !temp.first.isEmpty()) {
                                                // Lấy giá trị value từ Pair
                                                String values = temp.first;
                                                int intValue = (int) Double.parseDouble(values);
                                                String formattedValue = String.format("%d", intValue);
                                                tvTemp.setText(formattedValue);
                                            } else {
                                                // handle the case where myValue is empty
                                            }
                                            //do am
                                            Pair<String, Pair<String, String>> humi = sensorValues.get("humidity_temperature_sensor_0_data_1");
                                            if (humi != null && !humi.first.isEmpty()) {
                                                // do something with myValue
                                                // Lấy giá trị value từ Pair
                                                String values = humi.first;
                                                tvHumi.setText(values);
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

                        //relay
                        //relay đèn 1
                        String farmstayId = null;
                        String hardwareId = null;
                        Pair<String, String> relay_0 = relayValues.get("relay_0_data_0");
                        if (relay_0 != null && !relay_0.first.isEmpty()) {
                            farmstayId = relay_0.first;
                            hardwareId = relay_0.second;
                        }

                        String finalFarmstayId = farmstayId;
                        String finalHardwareId = hardwareId;

                        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                                if(isChecked) {
                                    imLamp1.setImageResource(R.drawable.ic_light_on);
                                    sendControlSignal(finalFarmstayId, finalHardwareId, 1);
                                } else {
                                    imLamp1.setImageResource(R.drawable.ic_light_off);
                                    sendControlSignal(finalFarmstayId, finalHardwareId, 0);
                                }
                            }
                        });

                        //relay đèn 2
                        String farmstayId1 = null;
                        String hardwareId1 = null;
                        Pair<String, String> relay_1 = relayValues.get("relay_0_data_1");
                        if (relay_1 != null && !relay_1.first.isEmpty()) {
                            farmstayId1 = relay_1.first;
                            hardwareId1 = relay_1.second;
                        }

                        String finalFarmstayId1 = farmstayId1;
                        String finalHardwareId1 = hardwareId1;

                        switch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                                if(isChecked) {
                                    sendControlSignal(finalFarmstayId1, finalHardwareId1, 1);
                                    imLamp2.setImageResource(R.drawable.ic_light_on);
                                } else {
                                    sendControlSignal(finalFarmstayId1, finalHardwareId1, 0);
                                    imLamp2.setImageResource(R.drawable.ic_light_off);
                                }
                            }
                        });

                        //relay đèn 3
                        String farmstayId2 = null;
                        String hardwareId2 = null;
                        Pair<String, String> relay_2 = relayValues.get("relay_0_data_2");
                        if (relay_2 != null && !relay_2.first.isEmpty()) {
                            farmstayId2 = relay_2.first;
                            hardwareId2 = relay_2.second;
                        }

                        String finalFarmstayId2 = farmstayId2;
                        String finalHardwareId2 = hardwareId2;

                        switch3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                                if(isChecked) {
                                    sendControlSignal(finalFarmstayId2, finalHardwareId2, 1);
                                    imLamp3.setImageResource(R.drawable.ic_light_on);
                                } else {
                                    sendControlSignal(finalFarmstayId2, finalHardwareId2, 0);
                                    imLamp3.setImageResource(R.drawable.ic_light_off);
                                }
                            }
                        });

                        //relay quạt
                        String farmstayId3 = null;
                        String hardwareId3 = null;
                        Pair<String, String> relay_3 = relayValues.get("relay_0_data_3");
                        if (relay_3 != null && !relay_3.first.isEmpty()) {
                            farmstayId3 = relay_3.first;
                            hardwareId3 = relay_3.second;
                        }

                        String finalFarmstayId3 = farmstayId3;
                        String finalHardwareId3 = hardwareId3;

                        switchFan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                                if(isChecked) {
                                    sendControlSignal(finalFarmstayId3, finalHardwareId3, 1);
                                    imFan.setImageResource(R.drawable.fan_on);
                                } else {
                                    sendControlSignal(finalFarmstayId3, finalHardwareId3, 0);
                                    imFan.setImageResource(R.drawable.fan_off);
                                }
                            }
                        });

                        switch4.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                switch1.setChecked(false);
                                switch2.setChecked(false);
                                switch3.setChecked(false);
                            }
                        });

                        switch5.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                switch1.setChecked(true);
                                switch2.setChecked(true);
                                switch3.setChecked(true);
                            }
                        });

                    }
                }
            }

            @Override
            public void onFailure(Call<SensorResuilt> call, Throwable t) {

            }
        });

        Call<SensorResuilt> callRelay_0 = retrofitInterface.RelayValue_0(token);
        callRelay_0.enqueue(new Callback<SensorResuilt>() {
            @Override
            public void onResponse(Call<SensorResuilt> call, Response<SensorResuilt> response) {
                SensorResuilt result = response.body();
                if(result != null){
                    JsonArray jsondatas = result.getData();

                    int lastIndex = jsondatas.size() - 1;
                    JsonObject lastObject = jsondatas.get(lastIndex).getAsJsonObject();
                    String valueRelay = lastObject.get("value").getAsString();
                    Log.e("valueRelay_0", valueRelay);

                    //relay đèn 1
                    if (valueRelay != null && !valueRelay.isEmpty()) {
                        // do something with myValue
                        int ValueRelay = Integer.parseInt(valueRelay);
                        if (ValueRelay == 1) {
                            // Hiển thị đèn bật trên giao diện của ứng dụng
                            switch1.setChecked(true);
                        } else {
                            // Hiển thị đèn tắt trên giao diện của ứng dụng
                            switch1.setChecked(false);
                        }
                    } else {
                        // handle the case where myValue is empty
                    }
                    if(switch1.isChecked()) {
                        imLamp1.setImageResource(R.drawable.ic_light_on);
                    } else {
                        imLamp1.setImageResource(R.drawable.ic_light_off);
                    }
                }
            }

            @Override
            public void onFailure(Call<SensorResuilt> call, Throwable t) {

            }
        });

        Call<SensorResuilt> callRelay_1 = retrofitInterface.RelayValue_1(token);
        callRelay_1.enqueue(new Callback<SensorResuilt>() {
            @Override
            public void onResponse(Call<SensorResuilt> call, Response<SensorResuilt> response) {
                SensorResuilt result = response.body();
                if(result != null){
                    JsonArray jsondatas = result.getData();

                    int lastIndex = jsondatas.size() - 1;
                    JsonObject lastObject = jsondatas.get(lastIndex).getAsJsonObject();
                    String valueRelay = lastObject.get("value").getAsString();
                    Log.e("valueRelay_0", valueRelay);

                    //relay đèn 1
                    if (valueRelay != null && !valueRelay.isEmpty()) {
                        // do something with myValue
                        int ValueRelay = Integer.parseInt(valueRelay);
                        if (ValueRelay == 1) {
                            // Hiển thị đèn bật trên giao diện của ứng dụng
                            switch2.setChecked(true);
                        } else {
                            // Hiển thị đèn tắt trên giao diện của ứng dụng
                            switch2.setChecked(false);
                        }
                    } else {
                        // handle the case where myValue is empty
                    }
                    if(switch2.isChecked()) {
                        imLamp2.setImageResource(R.drawable.ic_light_on);
                    } else {
                        imLamp2.setImageResource(R.drawable.ic_light_off);
                    }
                }
            }

            @Override
            public void onFailure(Call<SensorResuilt> call, Throwable t) {

            }
        });

        Call<SensorResuilt> callRelay_2 = retrofitInterface.RelayValue_2(token);
        callRelay_2.enqueue(new Callback<SensorResuilt>() {
            @Override
            public void onResponse(Call<SensorResuilt> call, Response<SensorResuilt> response) {
                SensorResuilt result = response.body();
                if(result != null){
                    JsonArray jsondatas = result.getData();

                    int lastIndex = jsondatas.size() - 1;
                    JsonObject lastObject = jsondatas.get(lastIndex).getAsJsonObject();
                    String valueRelay = lastObject.get("value").getAsString();
                    Log.e("valueRelay_0", valueRelay);

                    //relay đèn 1
                    if (valueRelay != null && !valueRelay.isEmpty()) {
                        // do something with myValue
                        int ValueRelay = Integer.parseInt(valueRelay);
                        if (ValueRelay == 1) {
                            // Hiển thị đèn bật trên giao diện của ứng dụng
                            switch3.setChecked(true);
                        } else {
                            // Hiển thị đèn tắt trên giao diện của ứng dụng
                            switch3.setChecked(false);
                        }
                    } else {
                        // handle the case where myValue is empty
                    }
                    if(switch3.isChecked()) {
                        imLamp3.setImageResource(R.drawable.ic_light_on);
                    } else {
                        imLamp3.setImageResource(R.drawable.ic_light_off);
                    }
                }
            }

            @Override
            public void onFailure(Call<SensorResuilt> call, Throwable t) {

            }
        });

        Call<SensorResuilt> callRelay_3 = retrofitInterface.RelayValue_3(token);
        callRelay_3.enqueue(new Callback<SensorResuilt>() {
            @Override
            public void onResponse(Call<SensorResuilt> call, Response<SensorResuilt> response) {
                SensorResuilt result = response.body();
                if(result != null){
                    JsonArray jsondatas = result.getData();

                    int lastIndex = jsondatas.size() - 1;
                    JsonObject lastObject = jsondatas.get(lastIndex).getAsJsonObject();
                    String valueRelay = lastObject.get("value").getAsString();
                    Log.e("valueRelay_0", valueRelay);

                    //relay đèn 1
                    if (valueRelay != null && !valueRelay.isEmpty()) {
                        // do something with myValue
                        int ValueRelay = Integer.parseInt(valueRelay);
                        if (ValueRelay == 1) {
                            // Hiển thị đèn bật trên giao diện của ứng dụng
                            switchFan.setChecked(true);
                        } else {
                            // Hiển thị đèn tắt trên giao diện của ứng dụng
                            switchFan.setChecked(false);
                        }
                    } else {
                        // handle the case where myValue is empty
                    }
                    if(switchFan.isChecked()) {
                        imFan.setImageResource(R.drawable.fan_on);
                    } else {
                        imFan.setImageResource(R.drawable.fan_off);
                    }
                }
            }

            @Override
            public void onFailure(Call<SensorResuilt> call, Throwable t) {

            }
        });

        //cập nhật relay realtime
        socket.on("relay_0_data_0", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                // Lấy giá trị cảm biến từ tham số args
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(args[0].toString());
                            // Thực hiện các thao tác khác với đối tượng jsonObject
                            String sensorValue = jsonObject.getString("value");
                            int relayValue = Integer.parseInt(sensorValue);
                            //test
                            Log.e("RelayValuesRealtime", String.valueOf(relayValue));

                            // Hiển thị đèn bật trên giao diện của ứng dụng
                            if(relayValue == 1){
                                switch1.setChecked(true);
                            } else {
                                switch1.setChecked(false);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
        socket.on("relay_0_data_1", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                // Lấy giá trị cảm biến từ tham số args
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(args[0].toString());
                            // Thực hiện các thao tác khác với đối tượng jsonObject
                            String sensorValue = jsonObject.getString("value");
                            int relayValue = Integer.parseInt(sensorValue);
                            //test
                            Log.e("RelayValuesRealtime", String.valueOf(relayValue));

                            // Hiển thị đèn bật trên giao diện của ứng dụng
                            if(relayValue == 1){
                                switch2.setChecked(true);
                            } else {
                                switch2.setChecked(false);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        socket.on("relay_0_data_2", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                // Lấy giá trị cảm biến từ tham số args
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(args[0].toString());
                            // Thực hiện các thao tác khác với đối tượng jsonObject
                            String sensorValue = jsonObject.getString("value");
                            int relayValue = Integer.parseInt(sensorValue);
                            //test
                            Log.e("RelayValuesRealtime", String.valueOf(relayValue));

                            // Hiển thị đèn bật trên giao diện của ứng dụng
                            if(relayValue == 1){
                                switch3.setChecked(true);
                            } else {
                                switch3.setChecked(false);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
        socket.on("relay_0_data_3", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                // Lấy giá trị cảm biến từ tham số args
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(args[0].toString());
                            // Thực hiện các thao tác khác với đối tượng jsonObject
                            String sensorValue = jsonObject.getString("value");
                            int relayValue = Integer.parseInt(sensorValue);
                            //test
                            Log.e("RelayValuesRealtime", String.valueOf(relayValue));

                            // Hiển thị đèn bật trên giao diện của ứng dụng
                            if(relayValue == 1){
                                switchFan.setChecked(true);
                            } else {
                                switchFan.setChecked(false);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        return view;
    }
    // Gửi tín hiệu điều khiển
    private void sendControlSignal(String farmstayId, String hardwareId, int value) {
        JSONObject data = new JSONObject();
        try {
            data.put("farmstay_id", farmstayId);
            data.put("hardware_id", hardwareId);
            data.put("value", value);
            socket.emit("client_control", data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}