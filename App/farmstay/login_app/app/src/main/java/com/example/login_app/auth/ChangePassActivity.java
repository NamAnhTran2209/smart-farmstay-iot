package com.example.login_app.auth;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.login_app.R;
import com.example.login_app.account_page.SettingActivity;
import com.example.login_app.shared_activity.BaseActivity;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;

import api.LoginResult;
import api.RetrofitInterface;
import api.RetrofitServer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePassActivity extends BaseActivity {

    EditText etCurrentPass, etNewpass, etComfirmPass;
    TextView tvError, tvError2;
    Button btnOk;
    ImageView imBack;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Thiết lập Activity fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_change_pass);

        etCurrentPass = findViewById(R.id.etCurrentPass);
        etNewpass = findViewById(R.id.etNewpass);
        etComfirmPass = findViewById(R.id.etComfirmNewpass);
        tvError = findViewById(R.id.tvErrorPass4);
        tvError2 = findViewById(R.id.tvErrorPass3);
        btnOk = findViewById(R.id.btnOk);
        imBack = findViewById(R.id.imBack2);

        tvError.setVisibility(View.GONE);
        tvError2.setVisibility(View.GONE);

        sharedPreferences = getSharedPreferences("SaveInfo",MODE_PRIVATE);

        imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChangePassActivity.this, SettingActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        etNewpass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String newPass = etNewpass.getText().toString().trim();
                if (newPass.length() < 6) {
                    tvError.setVisibility(View.VISIBLE);
                    tvError.setText(R.string.passwordmustbe6characters);
                    return;
                } else {
                    tvError.setVisibility(View.GONE);
                }
            }
        });

        etComfirmPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String comfirmPass = etComfirmPass.getText().toString().trim();
                String newPass = etNewpass.getText().toString().trim();
                if (!comfirmPass.equals(newPass)) {
                    tvError2.setVisibility(View.VISIBLE);
                    tvError2.setText(R.string.passwordnotmatch);
                    return;
                } else {
                    tvError2.setVisibility(View.GONE);
                }
            }
        });

        RetrofitServer retrofitServer = new RetrofitServer();
        RetrofitInterface retrofitInterface = retrofitServer.Retrofit();

        Toast toast = new Toast(ChangePassActivity.this);
        LayoutInflater inflater = getLayoutInflater();

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Khởi tạo dialog_progressbar
                final Dialog dialog = new Dialog(ChangePassActivity.this);
                dialog.setContentView(R.layout.layout_dialog_progressbar);
                ProgressBar progressBar = dialog.findViewById(R.id.progressBar);
                dialog.setCancelable(false);

                String pass = sharedPreferences.getString("password2","");
                String currentPass = etCurrentPass.getText().toString().trim();
                String newPass = etNewpass.getText().toString().trim();
                String comfirmPass = etComfirmPass.getText().toString().trim();

                if(TextUtils.isEmpty(currentPass) || TextUtils.isEmpty(newPass) || TextUtils.isEmpty(comfirmPass)){
                    View view1 = inflater.inflate(R.layout.layout_toast_error, (ViewGroup) findViewById(R.id.Layout_toast_2));
                    TextView tvMessege = view1.findViewById(R.id.tvMessege2);
                    tvMessege.setText("Enter every details");
                    toast.setView(view1);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.show();
                }
                else if(tvError.getVisibility() == View.VISIBLE || tvError2.getVisibility() == View.VISIBLE ||
                        (tvError.getVisibility() == View.VISIBLE && tvError2.getVisibility() == View.VISIBLE)) {
                    View view1 = inflater.inflate(R.layout.layout_toast_error, (ViewGroup) findViewById(R.id.Layout_toast_2));
                    TextView tvMessege = view1.findViewById(R.id.tvMessege2);
                    tvMessege.setText("Check your new password");
                    toast.setView(view1);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.show();

//                } else if(!currentPass.equals(pass)){
//                    View view1 = inflater.inflate(R.layout.layout_toast_error, (ViewGroup) findViewById(R.id.Layout_toast_2));
//                    TextView tvMessege = view1.findViewById(R.id.tvMessege2);
//                    tvMessege.setText("Current password is incorrect");
//                    toast.setView(view1);
//                    toast.setGravity(Gravity.CENTER, 0, 0);
//                    toast.setDuration(Toast.LENGTH_LONG);
//                    toast.show();
                } else {
                    dialog.show();

                    String token = sharedPreferences.getString("token", "");

                    HashMap<String, String> map = new HashMap<>();

                    map.put("old_password", currentPass);
                    map.put("new_password", comfirmPass);

                    Call<LoginResult> call = retrofitInterface.ChangePass(token, map);
                    call.enqueue(new Callback<LoginResult>() {
                        @Override
                        public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                            Log.e("code", String.valueOf(response.code()));
                            if (response.code() == 400){
                                LoginResult result = null;
                                try {
                                    dialog.dismiss();

                                    result = new Gson().fromJson(response.errorBody().string(), LoginResult.class);
                                    // do something with the result
                                    String msg = result.getMessage();

                                    View view1 = inflater.inflate(R.layout.layout_toast_error, (ViewGroup) findViewById(R.id.Layout_toast_2));
                                    TextView tvMessege = view1.findViewById(R.id.tvMessege2);
                                    tvMessege.setText(R.string.curentPassIncorect);
                                    toast.setView(view1);
                                    toast.setGravity(Gravity.CENTER, 0, 0);
                                    toast.setDuration(Toast.LENGTH_LONG);
                                    toast.show();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            else if (response.code() == 200){
                                LoginResult result = response.body();
                                String msg = result.getStatus();

                                View view1 = inflater.inflate(R.layout.layout_toast_success, (ViewGroup) findViewById(R.id.Layout_toast));
                                TextView tvMessege = view1.findViewById(R.id.tvMessege1);
                                tvMessege.setText(R.string.changePassSuccess);
                                toast.setView(view1);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.setDuration(Toast.LENGTH_LONG);
                                toast.show();

                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.remove("notify");
                                editor.remove("notifySoil");
                                editor.remove("notifyWater");
                                editor.remove("notifyWater2");
                                editor.remove("token");
                                editor.commit();

                                Intent loginIntent = new Intent(ChangePassActivity.this, LoginActivity.class);
                                startActivity(loginIntent);

                                dialog.dismiss();
                            }
                        }

                        @Override
                        public void onFailure(Call<LoginResult> call, Throwable t) {

                        }
                    });
                }
            }
        });
    }
}