package com.example.login_app.account_page;

import androidx.appcompat.app.AlertDialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.login_app.MyApplication;
import com.example.login_app.R;
import com.example.login_app.auth.ChangePassActivity;
import com.example.login_app.auth.LoginActivity;
import com.example.login_app.shared_activity.BaseActivity;

import api.LoginResult;
import api.RetrofitInterface;
import api.RetrofitServer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingActivity extends BaseActivity {

    ImageView imBack;
    TextView tvChangePass, tvSetting, tvLogout;

    int REQUEST_CODE = 1234;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Thiết lập Activity fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_setting);

        ((MyApplication) getApplicationContext()).addActivity(this);

        imBack = findViewById(R.id.imBack2);
        tvChangePass = findViewById(R.id.tvChangePass);
        tvSetting = findViewById(R.id.textViewSetting);
        tvLogout = findViewById(R.id.tvLogout);

        imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getSupportFragmentManager().popBackStack();
                } else {
                    finish();
                }
            }
        });

        tvChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingActivity.this, ChangePassActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLogoutDialog();
            }
        });
    }

    private void showLogoutDialog() {

        RetrofitServer retrofitServer = new RetrofitServer();
        RetrofitInterface retrofitInterface = retrofitServer.Retrofit();

        // Khởi tạo dialog_progressbar
        final Dialog dialog = new Dialog(SettingActivity.this);
        dialog.setContentView(R.layout.layout_dialog_progressbar);
        ProgressBar progressBar = dialog.findViewById(R.id.progressBar);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.logout);
        builder.setMessage(R.string.sureLogout);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                dialog.show();

                sharedPreferences = getSharedPreferences("SaveInfo",MODE_PRIVATE);
                String auth = sharedPreferences.getString("token","");
                Call<LoginResult> call = retrofitInterface.Logout(auth);

                call.enqueue(new Callback<LoginResult>() {
                    @Override
                    public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                        Log.e("CODE", response.code()+"");
                        dialog.dismiss();

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.remove("notify");
                        editor.remove("notifySoil");
                        editor.remove("notifyWater");
                        editor.remove("notifyWater2");
                        editor.remove("token");
                        editor.commit();

                        Intent loginIntent = new Intent(SettingActivity.this, LoginActivity.class);
                        startActivity(loginIntent);
                        finish();
                    }

                    @Override
                    public void onFailure(Call<LoginResult> call, Throwable t) {
                        Log.e("ssss", "err");

                    }
                });
            }
        });
        builder.setNegativeButton(R.string.no,null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ((MyApplication) getApplicationContext()).removeActivity(this);
    }
}