package com.example.login_app.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.example.login_app.R;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;

import api.LoginResult;
import api.RetrofitInterface;
import api.RetrofitServer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyAccountActivity extends AppCompatActivity {

    private TextView tvEmail;
    private PinView pinviewCode;
    private Button btnVerify;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_account);

        tvEmail = findViewById(R.id.tvEmail);
        pinviewCode = findViewById(R.id.pinviewCode);
        btnVerify = findViewById(R.id.btnVerification);

        RetrofitServer retrofitServer = new RetrofitServer();
        RetrofitInterface retrofitInterface = retrofitServer.Retrofit();

        Toast toast = new Toast(VerifyAccountActivity.this);
        LayoutInflater inflater = getLayoutInflater();

        sharedPreferences = getSharedPreferences("SaveInfo",MODE_PRIVATE);

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Khởi tạo dialog_progressbar
                final Dialog dialog = new Dialog(VerifyAccountActivity.this);
                dialog.setContentView(R.layout.layout_dialog_progressbar);
                ProgressBar progressBar = dialog.findViewById(R.id.progressBar);
                dialog.setCancelable(false);

                //email
                String email = sharedPreferences.getString("emailUser","");
                String verifyCode = pinviewCode.getText().toString();

                if(TextUtils.isEmpty(verifyCode)){
                    View view1 = inflater.inflate(R.layout.layout_toast_error, (ViewGroup) findViewById(R.id.Layout_toast_2));
                    TextView tvMessege = view1.findViewById(R.id.tvMessege2);
                    tvMessege.setText("Enter every details");
                    toast.setView(view1);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.show();
                }
                else {
                    dialog.show();

                    String token = sharedPreferences.getString("token","");
                    HashMap<String, String> map = new HashMap<>();

                    map.put("email", email);
                    map.put("otp", verifyCode);

                    Call<LoginResult> call = retrofitInterface.verifyAccount(token,map);
                    call.enqueue(new Callback<LoginResult>() {
                        @Override
                        public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                            Log.e("CODE", response.code()+"");
                            if(response.code() == 202){
                                LoginResult result = response.body();
                                String msg = result.getStatus();

                                View view1 = inflater.inflate(R.layout.layout_toast_success, (ViewGroup) findViewById(R.id.Layout_toast));
                                TextView tvMessege = view1.findViewById(R.id.tvMessege1);
                                tvMessege.setText(msg);
                                toast.setView(view1);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.setDuration(Toast.LENGTH_LONG);
                                toast.show();

                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("verifyCode", verifyCode);
                                editor.commit();

                                //quay ve trang account
                                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                                    getSupportFragmentManager().popBackStack();
                                } else {
                                    finish();
                                }
                            }
                            else if (response.code() == 401) {
                                dialog.dismiss();
                                LoginResult result = null;
                                try {
                                    result = new Gson().fromJson(response.errorBody().string(), LoginResult.class);
                                    // do something with the result
                                    String msg = result.getMessage();

                                    View view1 = inflater.inflate(R.layout.layout_toast_error, (ViewGroup) findViewById(R.id.Layout_toast_2));
                                    TextView tvMessege = view1.findViewById(R.id.tvMessege2);
                                    tvMessege.setText(msg);
                                    toast.setView(view1);
                                    toast.setGravity(Gravity.CENTER, 0, 0);
                                    toast.setDuration(Toast.LENGTH_LONG);
                                    toast.show();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                            else if (response.code() == 410) {
                                dialog.dismiss();
                                LoginResult result = null;
                                try {
                                    result = new Gson().fromJson(response.errorBody().string(), LoginResult.class);
                                    // do something with the result
                                    String msg = result.getMessage();

                                    View view1 = inflater.inflate(R.layout.layout_toast_error, (ViewGroup) findViewById(R.id.Layout_toast_2));
                                    TextView tvMessege = view1.findViewById(R.id.tvMessege2);
                                    tvMessege.setText(msg);
                                    toast.setView(view1);
                                    toast.setGravity(Gravity.CENTER, 0, 0);
                                    toast.setDuration(Toast.LENGTH_LONG);
                                    toast.show();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

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