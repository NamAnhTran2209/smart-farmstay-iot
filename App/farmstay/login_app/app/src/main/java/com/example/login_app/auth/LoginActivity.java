package com.example.login_app.auth;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.login_app.home_page.DashboardActivity;
import com.example.login_app.MyApplication;
import com.example.login_app.R;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;

import api.LoginResult;
import api.RetrofitInterface;
import api.RetrofitServer;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText emailText, passwordText;
    Button loginBtn, signupBtn;
    TextView forgotPassword;
    CheckBox cbSave;
    LinearLayout layoutLanguage;
    ImageView imLanguage;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Thiết lập Activity fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        String lang = getLanguage();
        setLocale(lang);

        setContentView(R.layout.activity_login);

        ((MyApplication) getApplicationContext()).addActivity(this);

        emailText = findViewById(R.id.email);
        passwordText = findViewById(R.id.password);
        loginBtn = findViewById(R.id.loginBtn);
        signupBtn = findViewById(R.id.signupBtn);
        forgotPassword = findViewById(R.id.forgotPassword);
        cbSave = findViewById(R.id.cbSave);
        imLanguage = findViewById(R.id.imLanguage);
        layoutLanguage = findViewById(R.id.layoutLanguage);

        RetrofitServer retrofitServer = new RetrofitServer();
        RetrofitInterface retrofitInterface = retrofitServer.Retrofit();

        Toast toast = new Toast(LoginActivity.this);
        LayoutInflater inflater = getLayoutInflater();

        //luu thong tin
        sharedPreferences = getSharedPreferences("SaveInfo",MODE_PRIVATE);

        //lay thong tin
        emailText.setText(sharedPreferences.getString("email",""));
        passwordText.setText(sharedPreferences.getString("password",""));
        cbSave.setChecked(sharedPreferences.getBoolean("check", false));

        String langCreen = sharedPreferences.getString("lang", "");
        switch (langCreen){
            case "English":
                imLanguage.setImageResource(R.drawable.anh);
                break;
            case "Vietnamese":
                imLanguage.setImageResource(R.drawable.vietnam);
                break;
            default:
                imLanguage.setImageResource(R.drawable.anh);
                break;
        }

        layoutLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogLanguage();
            }
        });

        DrawableClickListener clickListener = new DrawableClickListener() {
            public void onClick(DrawablePosition target) {
                if (target == DrawablePosition.RIGHT) {
                    Drawable[] drawables = passwordText.getCompoundDrawablesRelative();
                    Drawable drawable = getResources().getDrawable(R.drawable.ic_visibility);
                    Drawable drawable2 = getResources().getDrawable(R.drawable.ic_visibility_off);
                    if (passwordText.getTransformationMethod() instanceof PasswordTransformationMethod) {
                        passwordText.setTransformationMethod(null);
                        drawables[2] = drawable;
                    } else {
                        passwordText.setTransformationMethod(new PasswordTransformationMethod());
                        drawables[2] = drawable2;
                    }
                    passwordText.setCompoundDrawablesRelativeWithIntrinsicBounds(drawables[0], drawables[1], drawables[2], drawables[3]);

                }
            }
        };

        passwordText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (passwordText.getCompoundDrawables()[2] != null) {
                    boolean tapped = event.getX() > (passwordText.getWidth() - passwordText.getPaddingRight() - passwordText.getCompoundDrawables()[2].getIntrinsicWidth());
                    if (tapped) {
                        clickListener.onClick(DrawableClickListener.DrawablePosition.RIGHT);
                        return true;
                    }
                }
                return false;
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, EmailActivity.class);
                startActivity(intent);
            }
        });

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signupIntent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(signupIntent);
            }
        });

//        //kiem tra thoi han token va cho phep dang nhap
//        String token = sharedPreferences.getString("token","");
//        Call<LoginResult> call = retrofitInterface.Logout(token);
//
//        if (token != null){
//            call.enqueue(new Callback<LoginResult>() {
//                @Override
//                public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
//                    if(response.code() == 200){
//                        Intent intent = new Intent(LoginActivity.this,DashboardActivity.class);
//                        startActivity(intent);
//                        finish();
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<LoginResult> call, Throwable t) {
//                    Toast.makeText(LoginActivity.this, t.getMessage(),
//                            Toast.LENGTH_LONG).show();
//                }
//            });
//        }

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailText.getText().toString();
                String password = passwordText.getText().toString();

                // Khởi tạo dialog_progressbar
                final Dialog dialog = new Dialog(LoginActivity.this);
                dialog.setContentView(R.layout.layout_dialog_progressbar);
                ProgressBar progressBar = dialog.findViewById(R.id.progressBar);
                dialog.setCancelable(false);

                if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
                    View view1 = inflater.inflate(R.layout.layout_toast_error, (ViewGroup) findViewById(R.id.Layout_toast_2));
                    TextView tvMessege = view1.findViewById(R.id.tvMessege2);
                    tvMessege.setText("Enter every details");
                    toast.setView(view1);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.show();
                }
                else {
                    if(cbSave.isChecked()){
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("check", true);
                        editor.putString("email", email);
                        editor.putString("password", password);
                        editor.commit();
                    } else {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.remove("check");
                        editor.remove("email");
                        editor.remove("password");
                        editor.commit();
                    }
                    dialog.show();

                    HashMap<String, String> map = new HashMap<>();

                    map.put("login", email);
                    map.put("password", password);

                    Call<JsonObject> call = retrofitInterface.executeLogin(map);

                    call.enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            Log.e("code", String.valueOf(response.code()));
                            if (response.code() == 200) {

                                JsonObject result = response.body();
                                String token = result.get("authenticate-jwt").getAsString();

//                                String email = result.get("email").getAsString();
//                                String username = result.get("username").getAsString();

                                View view = inflater.inflate(R.layout.layout_toast_success, (ViewGroup) findViewById(R.id.Layout_toast));
                                TextView tvMessege = view.findViewById(R.id.tvMessege1);
                                tvMessege.setText(R.string.loginSuccessfully);
                                toast.setView(view);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.setDuration(Toast.LENGTH_LONG);
                                toast.show();

                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("token", token);
//                                editor.putString("emails", email);
//                                editor.putString("usernames", username);
                                editor.commit();

                                Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                                startActivity(intent);
//                                dialog.dismiss();

                                //lay thông tin user
                                Call<LoginResult> callUser = retrofitInterface.getUser(token);
                                callUser.enqueue(new Callback<LoginResult>() {
                                    @Override
                                    public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                                        Log.e("codeUser", response.code()+"");
                                        if(response.code() == 200){
                                            LoginResult result = response.body();
                                            JsonObject data = result.getData();
                                            String username = data.get("username").getAsString();
                                            String email = data.get("email").getAsString();

                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.putString("emailUser", email);
                                            editor.putString("usernameUser", username);
                                            editor.commit();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<LoginResult> call, Throwable t) {

                                    }
                                });

                            } else if (response.code() == 400) {
                                dialog.dismiss();
                                try {
                                    LoginResult result = new Gson().fromJson(response.errorBody().string(), LoginResult.class);
                                    // do something with the result
                                    String mes = result.getMsg();

                                    View view1 = inflater.inflate(R.layout.layout_toast_error, (ViewGroup) findViewById(R.id.Layout_toast_2));
                                    TextView tvMessege = view1.findViewById(R.id.tvMessege2);
                                    tvMessege.setText(R.string.passwordLenght);
                                    toast.setView(view1);
                                    toast.setGravity(Gravity.CENTER, 0, 0);
                                    toast.setDuration(Toast.LENGTH_LONG);
                                    toast.show();

                                } catch (IOException e) {
                                    // handle the exception, such as logging or displaying an error message
                                }

                            } else if (response.code() == 401) {
                                dialog.dismiss();
                                try {
                                    LoginResult result = new Gson().fromJson(response.errorBody().string(), LoginResult.class);
                                    // do something with the result
                                    String mes = result.getMessage();
//                                    String msg_vi = result.getMsg_vi();

                                    View view1 = inflater.inflate(R.layout.layout_toast_error, (ViewGroup) findViewById(R.id.Layout_toast_2));
                                    TextView tvMessege = view1.findViewById(R.id.tvMessege2);
                                    tvMessege.setText(mes);
                                    toast.setView(view1);
                                    toast.setGravity(Gravity.CENTER, 0, 0);
                                    toast.setDuration(Toast.LENGTH_LONG);
                                    toast.show();

                                } catch (IOException e) {
                                    // handle the exception, such as logging or displaying an error message
                                }
                            } else if (response.code() == 500) {
                                dialog.dismiss();
                                try {
                                    LoginResult result = new Gson().fromJson(response.errorBody().string(), LoginResult.class);
                                    // do something with the result
                                    String mes = result.getMsg();

                                    View view1 = inflater.inflate(R.layout.layout_toast_error, (ViewGroup) findViewById(R.id.Layout_toast_2));
                                    TextView tvMessege = view1.findViewById(R.id.tvMessege2);
                                    tvMessege.setText(mes);
                                    toast.setView(view1);
                                    toast.setGravity(Gravity.CENTER, 0, 0);
                                    toast.setDuration(Toast.LENGTH_LONG);
                                    toast.show();

                                } catch (IOException e) {
                                    // handle the exception, such as logging or displaying an error message
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {

                        }
                    });
                }
            }
        });
    }

    public interface DrawableClickListener {
        enum DrawablePosition { TOP, BOTTOM, LEFT, RIGHT };
        void onClick(DrawablePosition target);
    }

    private void showDialogLanguage() {
        final String[] listItem = {"English","Vietnamese"};
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(LoginActivity.this);
        mBuilder.setTitle("Chose Language");
        mBuilder.setSingleChoiceItems(listItem, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(i == 0){
                    setLocale("en");
                    SharedPreferences.Editor editor = getSharedPreferences("SaveInfo", MODE_PRIVATE).edit();
                    editor.putString("lang","English");
                    editor.apply();
                    ((MyApplication) getApplicationContext()).recreateAll();
                } else {
                    setLocale("vi");
                    SharedPreferences.Editor editor = getSharedPreferences("SaveInfo", MODE_PRIVATE).edit();
                    editor.putString("lang","Vietnamese");
                    editor.apply();
                    ((MyApplication) getApplicationContext()).recreateAll();
                }
                dialogInterface.dismiss();
            }
        });

        AlertDialog mDialog = mBuilder.create();
        mDialog.show();

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