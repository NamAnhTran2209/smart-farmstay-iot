package model;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


public class SocketIO {

    SharedPreferences sharedPreferences;

    Socket socket;
    public Socket socket(Context context) {

        // Khởi tạo kết nối với options đã đặt header
        try {
            IO.Options opts = new IO.Options();
            List<String> authHeader = new ArrayList<>();
            authHeader.add(getToken(context));
            opts.extraHeaders = Collections.singletonMap("authenticate-jwt", authHeader);
            socket = IO.socket("https://farmstays.me/farmstay", opts);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        // Đăng ký sự kiện connect
        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                // Kết nối thành công
                Log.e(TAG, "Connected to server");
            }
        });

        // Đăng ký sự kiện connect_error
        socket.on(Socket.EVENT_CONNECT_ERROR, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                // Kết nối thất bại
                Log.e(TAG, "Connection error: " + args[0].toString());
            }
        });

        socket.connect();
        return socket;
    }

    public String getToken(Context context){
        sharedPreferences = context.getSharedPreferences("SaveInfo",Context.MODE_PRIVATE);
        String auth = sharedPreferences.getString("token","");
        return auth;
    }

}
