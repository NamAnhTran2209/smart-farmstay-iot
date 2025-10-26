package com.example.login_app.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.login_app.R;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import api.Response;
import api.RetrofitInterface;
import api.RetrofitServer;
import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity {

    EditText nameText, emailText, passwordText, password_2Text;
    Button signupBtn, loginBtn;
    TextView tvErrorEmail, tvErrorPass, tvErrorComfirmPass;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Thiết lập Activity fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        nameText = findViewById(R.id.username);
        emailText = findViewById(R.id.email);
        passwordText = findViewById(R.id.password);
        password_2Text = findViewById(R.id.password_2);
        signupBtn = findViewById(R.id.signupBtn);
        loginBtn = findViewById(R.id.loginBtn);
        tvErrorEmail = findViewById(R.id.tvErrorEmail);
        tvErrorPass = findViewById(R.id.tvErrorPass);
        tvErrorComfirmPass = findViewById(R.id.tvErrorPassComfirm);

        RetrofitServer retrofitServer = new RetrofitServer();
        RetrofitInterface retrofitInterface = retrofitServer.Retrofit();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(loginIntent);
            }
        });

        Toast toast = new Toast(MainActivity.this);
        LayoutInflater inflater = getLayoutInflater();

        tvErrorEmail.setVisibility(View.GONE);
        tvErrorPass.setVisibility(View.GONE);
        tvErrorComfirmPass.setVisibility(View.GONE);

        sharedPreferences = getSharedPreferences("SaveInfo",MODE_PRIVATE);

        emailText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String email = emailText.getText().toString().trim();
                if (!isValidEmail(email)) {
                    tvErrorEmail.setVisibility(View.VISIBLE);
                    tvErrorEmail.setText(R.string.invalidEmail);
                    return;
                } else {
                    tvErrorEmail.setVisibility(View.GONE);
                }
            }
        });

        password_2Text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String password2 = password_2Text.getText().toString().trim();
                if (password2.length() < 6) {
                    tvErrorPass.setVisibility(View.VISIBLE);
                    tvErrorPass.setText(R.string.passwordmustbe6characters);
                    return;
                } else {
                    tvErrorPass.setVisibility(View.GONE);
                }
            }
        });

        passwordText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String password = passwordText.getText().toString().trim();
                String password2 = password_2Text.getText().toString().trim();
                if (!password.equals(password2)) {
                    tvErrorComfirmPass.setVisibility(View.VISIBLE);
                    tvErrorComfirmPass.setText(R.string.passwordnotmatch);
                    return;
                } else {
                    tvErrorComfirmPass.setVisibility(View.GONE);
                }
            }
        });

        DrawableClickListener clickListener = new DrawableClickListener() {
            public void onClick(DrawablePosition target) {
                if (target == DrawablePosition.RIGHT) {
                    Drawable[] drawables = password_2Text.getCompoundDrawablesRelative();
                    Drawable drawable = getResources().getDrawable(R.drawable.ic_visibility);
                    Drawable drawable2 = getResources().getDrawable(R.drawable.ic_visibility_off);
                    if (password_2Text.getTransformationMethod() instanceof PasswordTransformationMethod) {
                        password_2Text.setTransformationMethod(null);
                        drawables[2] = drawable;
                    } else {
                        password_2Text.setTransformationMethod(new PasswordTransformationMethod());
                        drawables[2] = drawable2;
                    }
                    password_2Text.setCompoundDrawablesRelativeWithIntrinsicBounds(drawables[0], drawables[1], drawables[2], drawables[3]);

                }
            }
        };

        password_2Text.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (password_2Text.getCompoundDrawables()[2] != null) {
                    boolean tapped = event.getX() > (password_2Text.getWidth() - password_2Text.getPaddingRight() - password_2Text.getCompoundDrawables()[2].getIntrinsicWidth());
                    if (tapped) {
                        clickListener.onClick(DrawableClickListener.DrawablePosition.RIGHT);
                        return true;
                    }
                }
                return false;
            }
        });

        DrawableClickListener clickListener2 = new DrawableClickListener() {
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
                        clickListener2.onClick(DrawableClickListener.DrawablePosition.RIGHT);
                        return true;
                    }
                }
                return false;
            }
        });

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameText.getText().toString();
                String email = emailText.getText().toString();
                String password = passwordText.getText().toString();
                String password_2 = password_2Text.getText().toString();

                // Khởi tạo dialog_progressbar
                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.layout_dialog_progressbar);
                ProgressBar progressBar = dialog.findViewById(R.id.progressBar);
                dialog.setCancelable(false);

                if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(name) || TextUtils.isEmpty(password_2)){
                    View view1 = inflater.inflate(R.layout.layout_toast_error, (ViewGroup) findViewById(R.id.Layout_toast_2));
                    TextView tvMessege = view1.findViewById(R.id.tvMessege2);
                    tvMessege.setText("Enter every details");
                    toast.setView(view1);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.show();
                }
                else if(tvErrorEmail.getVisibility() == View.VISIBLE || tvErrorPass.getVisibility() == View.VISIBLE || tvErrorComfirmPass.getVisibility() == View.VISIBLE
                        || (tvErrorEmail.getVisibility() == View.VISIBLE && tvErrorPass.getVisibility() == View.VISIBLE)
                        || (tvErrorPass.getVisibility() == View.VISIBLE && tvErrorComfirmPass.getVisibility() == View.VISIBLE)
                        || (tvErrorEmail.getVisibility() == View.VISIBLE && tvErrorComfirmPass.getVisibility() == View.VISIBLE)
                        || (tvErrorEmail.getVisibility() == View.VISIBLE && tvErrorPass.getVisibility() == View.VISIBLE && tvErrorComfirmPass.getVisibility() == View.VISIBLE)){
                    View view1 = inflater.inflate(R.layout.layout_toast_error, (ViewGroup) findViewById(R.id.Layout_toast_2));
                    TextView tvMessege = view1.findViewById(R.id.tvMessege2);
                    tvMessege.setText("check registration information");
                    toast.setView(view1);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.show();
                }

                else{
                    dialog.show();

                    HashMap<String, String> map = new HashMap<>();

                    map.put("username", name);
                    map.put("email", email);
                    map.put("password", password);

                    Call<Response> callSignup = retrofitInterface.executeSignup(map);

                    callSignup.enqueue(new Callback<Response>() {
                        @Override
                        public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                            if (response.code() == 200) {

                                View view = inflater.inflate(R.layout.layout_toast_success, (ViewGroup) findViewById(R.id.Layout_toast));
                                TextView tvMessege = view.findViewById(R.id.tvMessege1);
                                tvMessege.setText(R.string.signupSuccessfully);
                                toast.setView(view);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.setDuration(Toast.LENGTH_LONG);
                                toast.show();

                                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                startActivity(intent);
                                dialog.dismiss();

                            } else if (response.code() == 422) {
                                dialog.dismiss();
                                try {
                                    Response result = new Gson().fromJson(response.errorBody().string(), Response.class);
                                    // do something with the result
                                    String mes = result.getMsg();

                                    View view = inflater.inflate(R.layout.layout_toast_error, (ViewGroup) findViewById(R.id.Layout_toast_2));
                                    TextView tvMessege = view.findViewById(R.id.tvMessege2);
                                    tvMessege.setText(R.string.canNotCreateAccount);
                                    toast.setView(view);
                                    toast.setGravity(Gravity.CENTER, 0, 0);
                                    toast.setDuration(Toast.LENGTH_LONG);
                                    toast.show();
                                } catch (IOException e){

                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Response> call, Throwable t) {
                            Toast.makeText(MainActivity.this, t.getMessage(),
                                    Toast.LENGTH_LONG).show();
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


    private boolean isValidEmail(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}