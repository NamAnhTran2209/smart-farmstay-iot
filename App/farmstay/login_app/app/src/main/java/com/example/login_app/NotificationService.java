package com.example.login_app;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.util.Pair;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.login_app.home_page.DashboardActivity;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import api.SensorResuilt;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import model.Notification_model;
import api.RetrofitInterface;
import api.RetrofitServer;
import model.SocketIO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationService extends Service {

    private Timer timer;

    SharedPreferences sharedPreferences;

    public static final String CHANNEL_ID = "farmstay";
    public static final String CHANNEL_ID_1 = "SoilMoisture";
    public static final String CHANNEL_ID_2 = "WaterLevel";
    public static final String CHANNEL_ID_3 = "WaterLevel2";
    private List<Notification_model> listNotify;

    private Handler mHandler;
    private MediaPlayer mediaPlayer;

    @Override
    public void onCreate() {
        super.onCreate();
        mHandler = new Handler(Looper.getMainLooper());
        setNotificationAlarm();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Intent notificationServiceIntent = new Intent(this, NotificationService.class);
        startService(notificationServiceIntent);

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Hủy timer khi service bị stop
        timer.cancel();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private void setNotificationAlarm() {
        // Gọi API và đặt thông báo ở đây
        RetrofitServer retrofitServer = new RetrofitServer();
        RetrofitInterface retrofitInterface = retrofitServer.Retrofit();

        // Lấy thời gian hiện tại
        Calendar calendar = Calendar.getInstance();

        // Đặt giờ, phút cho thời gian hiện tại
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE));

        // Lấy giờ, phút của thời gian hiện tại
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // Đặt ngày, tháng và năm cho ngày hiện tại
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));

        // Lấy ngày, tháng và năm của ngày hiện tại
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        String time = String.format("%02d:%02d", hour, minute); // định dạng giờ:phút
        String date = String.format("%02d/%02d/%d", dayOfMonth, month + 1, year); // định dạng ngày/tháng/năm

        SocketIO socketIO = new SocketIO();
        Socket socket = socketIO.socket(this);

        sharedPreferences = getSharedPreferences("SaveInfo", MODE_PRIVATE);
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
                                mHandler.post(new Runnable() {
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
                                                if(fireValue == 1){
                                                    NotificationManager notificationManager = getSystemService(NotificationManager.class);
                                                    int notificationId = 1;
                                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "My Channel", NotificationManager.IMPORTANCE_HIGH);
                                                        channel.setDescription("My channel description");
                                                        notificationManager.createNotificationChannel(channel);
                                                    }
                                                    Intent notificationIntent = new Intent(getApplicationContext(), DashboardActivity.class);
                                                    notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                                                    NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                                                            .setSmallIcon(R.drawable.ic_fire_2)
                                                            .setContentTitle(getApplicationContext().getString(R.string.warning))
                                                            .setContentText(getApplicationContext().getString(R.string.fireAlarm))
                                                            .setContentIntent(pendingIntent)
                                                            .setAutoCancel(true)
                                                            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))
                                                            .setPriority(NotificationCompat.PRIORITY_MAX)
                                                            .setDefaults(Notification.DEFAULT_ALL);

                                                    notificationManager.notify(notificationId, builder.build());
                                                    mediaPlayer = MediaPlayer.create(NotificationService.this, R.raw.warning_sound);
                                                    mediaPlayer.start();

                                                    //test
                                                    listNotify = new ArrayList<>();
                                                    listNotify.add(new Notification_model(2,NotificationService.this.getString(R.string.warning2),NotificationService.this.getString(R.string.fireAlarm),time,date));

                                                    sharedPreferences = getSharedPreferences("SaveInfo",MODE_PRIVATE);

                                                    // Chuyển đổi danh sách thành chuỗi JSON
                                                    Gson gson = new Gson();
                                                    String json = gson.toJson(listNotify);

                                                    // Lưu chuỗi JSON vào SharedPreferences
                                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                                    editor.putString("notify", json);
                                                    editor.commit();
                                                }
                                            } else {
                                                // handle the case where myValue is empty
                                            }
                                            //nhiet do
                                            Pair<String, Pair<String, String>> temp = sensorValues.get("humidity_temperature_sensor_0_data_0");
                                            if (temp != null && !temp.first.isEmpty()) {
                                                // Lấy giá trị value từ Pair
                                                String values = temp.first;
                                                // Chuyển đổi giá trị value sang kiểu float
                                                float tempValue = Float.parseFloat(values);
                                                //bao loi
                                                String danger_max = temp.second.second;
                                                if(tempValue > Float.parseFloat(danger_max)){
//                                                donutProgressTemp.setFinishedStrokeColor(Color.RED);
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

                                                String danger_min = soilMoisture.second.first;
                                                if(soilValue < Float.parseFloat(danger_min)){
                                                    Handler handler = new Handler();
                                                    NotificationManager notificationManager = getSystemService(NotificationManager.class);
                                                    int notificationId = 2;
                                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                        NotificationChannel channel = new NotificationChannel(CHANNEL_ID_1, "My Channel 1", NotificationManager.IMPORTANCE_HIGH);
                                                        channel.setDescription("My channel description");
                                                        notificationManager.createNotificationChannel(channel);
                                                    }
                                                    Intent notificationIntent = new Intent(getApplicationContext(), DashboardActivity.class);
                                                    notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                                                    NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID_1)
                                                            .setSmallIcon(R.drawable.ic_notifications)
                                                            .setContentTitle(getApplicationContext().getString(R.string.warning2))
                                                            .setContentText(getApplicationContext().getString(R.string.lowSoil))
                                                            .setContentIntent(pendingIntent)
                                                            .setAutoCancel(true)
                                                            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))
                                                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                                                            .setDefaults(Notification.DEFAULT_ALL);

                                                    // Delay 3 seconds before showing the notification
                                                    handler.postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            notificationManager.notify(notificationId, builder.build());
                                                        }
                                                    }, 5000);

                                                    //test
                                                    listNotify = new ArrayList<>();
                                                    listNotify.add(new Notification_model(2,NotificationService.this.getString(R.string.warning2),NotificationService.this.getString(R.string.lowSoil),time,date));

                                                    sharedPreferences = getSharedPreferences("SaveInfo",MODE_PRIVATE);

                                                    // Chuyển đổi danh sách thành chuỗi JSON
                                                    Gson gson = new Gson();
                                                    String json = gson.toJson(listNotify);

                                                    // Lưu chuỗi JSON vào SharedPreferences
                                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                                    editor.putString("notifySoil", json);
                                                    editor.commit();
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

                                                String danger_min = waterLevel.second.first;
                                                String danger_max = waterLevel.second.second;

                                                Handler handler = new Handler();
                                                if(waterValue < Float.parseFloat(danger_min)){
                                                    NotificationManager notificationManager = getSystemService(NotificationManager.class);
                                                    int notificationId = 3;
                                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                        NotificationChannel channel = new NotificationChannel(CHANNEL_ID_2, "My Channel 2", NotificationManager.IMPORTANCE_HIGH);
                                                        channel.setDescription("My channel description");
                                                        notificationManager.createNotificationChannel(channel);
                                                    }
                                                    Intent notificationIntent = new Intent(getApplicationContext(), DashboardActivity.class);
                                                    notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                                                    NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID_2)
                                                            .setSmallIcon(R.drawable.ic_notifications)
                                                            .setContentTitle(getApplicationContext().getString(R.string.warning2))
                                                            .setContentText(getApplicationContext().getString(R.string.lowWater))
                                                            .setContentIntent(pendingIntent)
                                                            .setAutoCancel(true)
                                                            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))
                                                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                                                            .setDefaults(Notification.DEFAULT_ALL);

                                                    // Delay 3 seconds before showing the notification
                                                    handler.postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            notificationManager.notify(notificationId, builder.build());
                                                        }
                                                    }, 5000);

                                                    //test
                                                    listNotify = new ArrayList<>();
                                                    listNotify.add(new Notification_model(2,NotificationService.this.getString(R.string.warning2),NotificationService.this.getString(R.string.lowWater),time,date));

                                                    sharedPreferences = getSharedPreferences("SaveInfo",MODE_PRIVATE);

                                                    // Chuyển đổi danh sách thành chuỗi JSON
                                                    Gson gson = new Gson();
                                                    String json = gson.toJson(listNotify);

                                                    // Lưu chuỗi JSON vào SharedPreferences
                                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                                    editor.putString("notifyWater", json);
                                                    editor.commit();
                                                } else if(waterValue > Float.parseFloat(danger_max)){
                                                    NotificationManager notificationManager = getSystemService(NotificationManager.class);
                                                    int notificationId = 4;
                                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                        NotificationChannel channel = new NotificationChannel(CHANNEL_ID_3, "My Channel 3", NotificationManager.IMPORTANCE_HIGH);
                                                        channel.setDescription("My channel description");
                                                        notificationManager.createNotificationChannel(channel);
                                                    }
                                                    Intent notificationIntent = new Intent(getApplicationContext(), DashboardActivity.class);
                                                    notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                                                    NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID_3)
                                                            .setSmallIcon(R.drawable.ic_notifications)
                                                            .setContentTitle(getApplicationContext().getString(R.string.warning2))
                                                            .setContentText(getApplicationContext().getString(R.string.highWater))
                                                            .setContentIntent(pendingIntent)
                                                            .setAutoCancel(true)
                                                            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))
                                                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                                                            .setDefaults(Notification.DEFAULT_ALL);

                                                    // Delay 3 seconds before showing the notification
                                                    handler.postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            notificationManager.notify(notificationId, builder.build());
                                                        }
                                                    }, 5000);

                                                    //test
                                                    listNotify = new ArrayList<>();
                                                    listNotify.add(new Notification_model(2,NotificationService.this.getString(R.string.warning2),NotificationService.this.getString(R.string.highWater),time,date));

                                                    sharedPreferences = getSharedPreferences("SaveInfo",MODE_PRIVATE);

                                                    // Chuyển đổi danh sách thành chuỗi JSON
                                                    Gson gson = new Gson();
                                                    String json = gson.toJson(listNotify);

                                                    // Lưu chuỗi JSON vào SharedPreferences
                                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                                    editor.putString("notifyWater2", json);
                                                    editor.commit();
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
