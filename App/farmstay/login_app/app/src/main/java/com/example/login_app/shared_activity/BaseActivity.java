package com.example.login_app.shared_activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.login_app.auth.LoginActivity;
import com.example.login_app.NotificationService;
import com.example.login_app.R;

import java.util.Locale;

import api.RetrofitInterface;
import api.RetrofitServer;

public class BaseActivity extends AppCompatActivity {

    RetrofitServer retrofitServer = new RetrofitServer();
    RetrofitInterface retrofitInterface = retrofitServer.Retrofit();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String lang = getLanguage();
        setLocale(lang);

        startService(new Intent(BaseActivity.this, NotificationService.class));
    }

    // Hàm hiển thị dialog hết phiên đăng nhập
    private void showSessionExpiredDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(BaseActivity.this);
        builder.setMessage(R.string.loginAgain);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.logIn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Xử lý đăng nhập lại
                SharedPreferences sharedPreferences = getSharedPreferences("SaveInfo",MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("notify");
                editor.remove("notifySoil");
                editor.remove("notifyWater");
                editor.remove("notifyWater2");
                editor.remove("token");
                editor.commit();

                Intent intent = new Intent(BaseActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        SharedPreferences.Editor editor = getSharedPreferences("SaveInfo", MODE_PRIVATE).edit();
        editor.putString("language",lang);
        editor.apply();
    }

    public String getLanguage() {
        SharedPreferences preferences = getSharedPreferences("SaveInfo", Activity.MODE_PRIVATE);
        return preferences.getString("language", "");
    }
}
