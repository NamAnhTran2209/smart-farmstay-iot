package com.example.login_app.auth;

import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Dialog;
import android.content.Intent;
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

import com.chaos.view.PinView;
import com.example.login_app.R;
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

public class VerificationCodeActivity extends BaseActivity {

    PinView pinViewCode;
    Button btnVerification, btnOk;
    ImageView imBack, imback2;
    TextView tvEmail, tvErrorpass2, tvErrorpass3;
    ConstraintLayout layout1, layout2;
    EditText etNewpass, etComfirmNewpass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Thiết lập Activity fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_verification_code);

        pinViewCode = findViewById(R.id.pinviewCode);
        btnVerification = findViewById(R.id.btnVerification);
        imBack = findViewById(R.id.imBack);
        tvEmail = findViewById(R.id.tvEmail);

        imback2 = findViewById(R.id.imBack2);
        etNewpass = findViewById(R.id.etNewpass);
        etComfirmNewpass = findViewById(R.id.etComfirmNewpass);
        tvErrorpass2 = findViewById(R.id.tvErrorPass2);
        tvErrorpass3 = findViewById(R.id.tvErrorPass3);
        btnOk = findViewById(R.id.btnOk);

        layout1 = findViewById(R.id.constraintLayout1);
        layout2 = findViewById(R.id.constraintLayout2);

        String email = getIntent().getStringExtra("emailVerify");
        tvEmail.setText(email);

        layout2.setVisibility(View.GONE);
        tvErrorpass2.setVisibility(View.GONE);
        tvErrorpass3.setVisibility(View.GONE);
        imBack.setVisibility(View.VISIBLE);
        imback2.setVisibility(View.GONE);

        imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VerificationCodeActivity.this, EmailActivity.class);
                startActivity(intent);
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
                    tvErrorpass2.setVisibility(View.VISIBLE);
                    tvErrorpass2.setText("Password must be 6 characters");
                    return;
                } else {
                    tvErrorpass2.setVisibility(View.GONE);
                }
            }
        });

        etComfirmNewpass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String comfirmPass = etComfirmNewpass.getText().toString().trim();
                String newPass = etNewpass.getText().toString().trim();
                if (!comfirmPass.equals(newPass)) {
                    tvErrorpass3.setVisibility(View.VISIBLE);
                    tvErrorpass3.setText("Password not match");
                    return;
                } else {
                    tvErrorpass3.setVisibility(View.GONE);
                }
            }
        });

        Toast toast = new Toast(VerificationCodeActivity.this);
        LayoutInflater inflater = getLayoutInflater();

        RetrofitServer retrofitServer = new RetrofitServer();
        RetrofitInterface retrofitInterface = retrofitServer.Retrofit();

        btnVerification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Khởi tạo dialog_progressbar
                final Dialog dialog = new Dialog(VerificationCodeActivity.this);
                dialog.setContentView(R.layout.layout_dialog_progressbar);
                ProgressBar progressBar = dialog.findViewById(R.id.progressBar);
                dialog.setCancelable(false);

                String verifyCode = pinViewCode.getText().toString();
                String email = tvEmail.getText().toString();

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

                    HashMap<String, String> map = new HashMap<>();

                    map.put("email", email);
                    map.put("otp", verifyCode);

                    Call<LoginResult> callReset = retrofitInterface.Active(map);
                    callReset.enqueue(new Callback<LoginResult>() {
                        @Override
                        public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                            Log.e("code", String.valueOf(response.code()));
                            if (response.code() == 202) {

                                LoginResult result = response.body();
                                String msg = result.getStatus();

                                View view1 = inflater.inflate(R.layout.layout_toast_success, (ViewGroup) findViewById(R.id.Layout_toast));
                                TextView tvMessege = view1.findViewById(R.id.tvMessege1);
                                tvMessege.setText(R.string.otpVerifySuccess);
                                toast.setView(view1);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.setDuration(Toast.LENGTH_LONG);
                                toast.show();

                                layout1.setVisibility(View.GONE);
                                layout2.setVisibility(View.VISIBLE);
                                imBack.setVisibility(View.GONE);
                                imback2.setVisibility(View.VISIBLE);

                                dialog.dismiss();
//
                            }
                            else if (response.code() == 401) {
                                LoginResult result = null;
                                try {
                                    dialog.dismiss();

                                    result = new Gson().fromJson(response.errorBody().string(), LoginResult.class);
                                    // do something with the result
                                    String msg = result.getMessage();

                                    View view1 = inflater.inflate(R.layout.layout_toast_error, (ViewGroup) findViewById(R.id.Layout_toast_2));
                                    TextView tvMessege = view1.findViewById(R.id.tvMessege2);
                                    tvMessege.setText(R.string.incorectOtpCode);
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

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Khởi tạo dialog_progressbar
                final Dialog dialog = new Dialog(VerificationCodeActivity.this);
                dialog.setContentView(R.layout.layout_dialog_progressbar);
                ProgressBar progressBar = dialog.findViewById(R.id.progressBar);

                String email = tvEmail.getText().toString();
                String newPassword = etComfirmNewpass.getText().toString();
                String verifyCode = pinViewCode.getText().toString();

                if(tvErrorpass2.getVisibility() == View.VISIBLE || tvErrorpass3.getVisibility() == View.VISIBLE ||
                        (tvErrorpass2.getVisibility() == View.VISIBLE && tvErrorpass3.getVisibility() == View.VISIBLE)) {
                    View view1 = inflater.inflate(R.layout.layout_toast_error, (ViewGroup) findViewById(R.id.Layout_toast_2));
                    TextView tvMessege = view1.findViewById(R.id.tvMessege2);
                    tvMessege.setText("Check your new password");
                    toast.setView(view1);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.show();
                } else {
                    dialog.show();

                    HashMap<String, String> map = new HashMap<>();

                    map.put("email", email);
                    map.put("otp", verifyCode);
                    map.put("new_password", newPassword);

                    Call<LoginResult> callReset = retrofitInterface.resetPass(map);
                    callReset.enqueue(new Callback<LoginResult>() {
                        @Override
                        public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                            if (response.code() == 401){
                                LoginResult result = null;
                                try {
                                    dialog.dismiss();

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
                            else if (response.code() == 410){
                                LoginResult result = null;
                                try {
                                    dialog.dismiss();

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
                            else if (response.code() == 200){
                                LoginResult result = response.body();
                                String msg = result.getStatus();

                                View view1 = inflater.inflate(R.layout.layout_toast_success, (ViewGroup) findViewById(R.id.Layout_toast));
                                TextView tvMessege = view1.findViewById(R.id.tvMessege1);
                                tvMessege.setText(R.string.resetPassSuccess);
                                toast.setView(view1);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.setDuration(Toast.LENGTH_LONG);
                                toast.show();

                                Intent intent = new Intent(VerificationCodeActivity.this, LoginActivity.class);
                                startActivity(intent);

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