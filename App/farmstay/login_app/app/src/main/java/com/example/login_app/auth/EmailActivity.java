package com.example.login_app.auth;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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

public class EmailActivity extends BaseActivity {

    EditText etEmailSend;
    Button btnSendEmail;
    ImageView imBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Thiết lập Activity fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_email);

        etEmailSend = findViewById(R.id.etEmailsend);
        btnSendEmail = findViewById(R.id.btnSendEmail);
        imBack = findViewById(R.id.imBack);

        imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EmailActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        Toast toast = new Toast(EmailActivity.this);
        LayoutInflater inflater = getLayoutInflater();

        RetrofitServer retrofitServer = new RetrofitServer();
        RetrofitInterface retrofitInterface = retrofitServer.Retrofit();

        btnSendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Khởi tạo dialog_progressbar
                final Dialog dialog = new Dialog(EmailActivity.this);
                dialog.setContentView(R.layout.layout_dialog_progressbar);
                ProgressBar progressBar = dialog.findViewById(R.id.progressBar);
                dialog.setCancelable(false);

                String email = etEmailSend.getText().toString();
                if(TextUtils.isEmpty(email)) {
                    View view1 = inflater.inflate(R.layout.layout_toast_error, (ViewGroup) findViewById(R.id.Layout_toast_2));
                    TextView tvMessege = view1.findViewById(R.id.tvMessege2);
                    tvMessege.setText("Enter your Email Address");
                    toast.setView(view1);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.show();
                }
                else{
                    dialog.show();
                    HashMap<String, String> map = new HashMap<>();

                    map.put("email", email);

                    Call<LoginResult> callFogotMail = retrofitInterface.forgotPassword(map);
                    callFogotMail.enqueue(new Callback<LoginResult>() {
                        @Override
                        public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                            Log.e("code", String.valueOf(response.code()));
                            if (response.code() == 200) {
                                LoginResult result = response.body();
                                String msg = result.getStatus();

                                View view1 = inflater.inflate(R.layout.layout_toast_success, (ViewGroup) findViewById(R.id.Layout_toast));
                                TextView tvMessege = view1.findViewById(R.id.tvMessege1);
                                tvMessege.setText(R.string.codeSendSuccess);
                                toast.setView(view1);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.setDuration(Toast.LENGTH_LONG);
                                toast.show();

                                Intent intent = new Intent(EmailActivity.this, VerificationCodeActivity.class);
                                intent.putExtra("emailVerify", email);
                                startActivity(intent);

                                dialog.dismiss();
                            }
                            if (response.code() == 401) {
                                LoginResult result = null;
                                try {
                                    dialog.dismiss();
                                    result = new Gson().fromJson(response.errorBody().string(), LoginResult.class);
                                    // do something with the result
                                    String msg = result.getStatus();

                                    View view1 = inflater.inflate(R.layout.layout_toast_error, (ViewGroup) findViewById(R.id.Layout_toast_2));
                                    TextView tvMessege = view1.findViewById(R.id.tvMessege2);
                                    tvMessege.setText(R.string.emailNotRegister);
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
                            Toast.makeText(EmailActivity.this, t.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
//                            if (response.code() == 500) {
//                                View view1 = inflater.inflate(R.layout.layout_toast_error, (ViewGroup) findViewById(R.id.Layout_toast_2));
//                                TextView tvMessege = view1.findViewById(R.id.tvMessege2);
//                                tvMessege.setText("Error finding user");
//                                toast.setView(view1);
//                                toast.setGravity(Gravity.CENTER, 0, 0);
//                                toast.setDuration(Toast.LENGTH_LONG);
//                                toast.show();
//                            } else if(response.code() == 400){
//                                View view1 = inflater.inflate(R.layout.layout_toast_error, (ViewGroup) findViewById(R.id.Layout_toast_2));
//                                TextView tvMessege = view1.findViewById(R.id.tvMessege2);
//                                tvMessege.setText("Email not found");
//                                toast.setView(view1);
//                                toast.setGravity(Gravity.CENTER, 0, 0);
//                                toast.setDuration(Toast.LENGTH_LONG);
//                                toast.show();
//                            } else if(response.code() == 504){
//                                View view1 = inflater.inflate(R.layout.layout_toast_error, (ViewGroup) findViewById(R.id.Layout_toast_2));
//                                TextView tvMessege = view1.findViewById(R.id.tvMessege2);
//                                tvMessege.setText("Error sending email");
//                                toast.setView(view1);
//                                toast.setGravity(Gravity.CENTER, 0, 0);
//                                toast.setDuration(Toast.LENGTH_LONG);
//                                toast.show();
//                            } else if(response.code() == 200){
//                                View view1 = inflater.inflate(R.layout.layout_toast_success, (ViewGroup) findViewById(R.id.Layout_toast));
//                                TextView tvMessege = view1.findViewById(R.id.tvMessege1);
//                                tvMessege.setText("Email sent successfully");
//                                toast.setView(view1);
//                                toast.setGravity(Gravity.CENTER, 0, 0);
//                                toast.setDuration(Toast.LENGTH_LONG);
//                                toast.show();
//
//                                Intent intent = new Intent(EmailActivity.this, VerificationCodeActivity.class);
//                                intent.putExtra("emailVerify", email);
//                                startActivity(intent);
//


                }
            }
        });
    }
}