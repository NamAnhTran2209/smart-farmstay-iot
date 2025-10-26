package com.example.login_app.account_page;

import static android.content.Context.MODE_PRIVATE;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.login_app.R;
import com.example.login_app.auth.LoginActivity;
import com.example.login_app.auth.VerifyAccountActivity;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;

import api.LoginResult;
import api.RetrofitInterface;
import api.RetrofitServer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountFragment extends Fragment {

    SharedPreferences sharedPreferences;

    LinearLayout layout_edit, layout_contact, layout_setting;
    TextView tvUsername,tvEmail, tvStatus;
    Button btnVerify;

    RetrofitServer retrofitServer = new RetrofitServer();
    RetrofitInterface retrofitInterface = retrofitServer.Retrofit();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AccountFragment newInstance(String param1, String param2) {
        AccountFragment fragment = new AccountFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = null;
        try {
            view = inflater.inflate(R.layout.fragment_account, container, false);
        } catch (InflateException e) {
            Log.e("MyApp", "Error inflating layout", e);
            throw e;
        }

        tvUsername = view.findViewById(R.id.tvUsername);
        tvEmail = view.findViewById(R.id.tvEmail);
        layout_edit = view.findViewById(R.id.layout_edit);
        layout_contact = view.findViewById(R.id.layout_contact);
        layout_setting = view.findViewById(R.id.layout_setting);
        tvStatus = view.findViewById(R.id.tvStatus);
        btnVerify = view.findViewById(R.id.btnVerify);


        sharedPreferences = getActivity().getSharedPreferences("SaveInfo", MODE_PRIVATE);
        String email = sharedPreferences.getString("emailUser","");

        tvUsername.setText(sharedPreferences.getString("usernameUser",""));
        tvEmail.setText(email);

        //
        String token = sharedPreferences.getString("token","");
        String verifyCode = sharedPreferences.getString("verifyCode","");

        HashMap<String, String> map = new HashMap<>();

        map.put("email", email);
        map.put("otp", verifyCode);

        Call<LoginResult> call = retrofitInterface.verifyAccount(token,map);

        call.enqueue(new Callback<LoginResult>() {
            @Override
            public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                Log.e("CODE", response.code()+"");
                if (response.code() == 410) {
                    tvStatus.setText(R.string.verified);
                    tvStatus.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_verified,0,0,0);
                } else if(response.code() == 401){
                    LoginResult result = null;
                    try {
                        result = new Gson().fromJson(response.errorBody().string(), LoginResult.class);
                        // do something with the result
                        String msg = result.getMessage();

                        Log.e("message", msg);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<LoginResult> call, Throwable t) {

            }
        });

        layout_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Url = "https://farmstays.me/";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Url));
                startActivity(intent);
            }
        });

        layout_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ContactActivity.class);
                startActivity(intent);
            }
        });

        layout_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);
            }
        });

        // xac thuc tai khoan
        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //test xac thuc
                String token = sharedPreferences.getString("token","");

                HashMap<String, String> map = new HashMap<>();
                map.put("email", email);

                Call<LoginResult> call = retrofitInterface.activeEmail(token, map);
                call.enqueue(new Callback<LoginResult>() {
                    @Override
                    public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                        Log.e("CODE", response.code()+"");
                        if(response.code() == 200) {
                            LoginResult result = response.body();

                            Intent intent = new Intent(getActivity(), VerifyAccountActivity.class);
                            startActivity(intent);
                        }
                        else if(response.code() == 401){
                            LoginResult result = null;
                            try {
                                result = new Gson().fromJson(response.errorBody().string(), LoginResult.class);
                                // do something with the result
                                String msg = result.getMessage();

                                Log.e("message", msg);

                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                        else if(response.code() == 410){
                            LoginResult result = null;
                            try {
                                result = new Gson().fromJson(response.errorBody().string(), LoginResult.class);
                                // do something with the result
                                String msg = result.getMessage();
                                Log.e("message", msg);

                                Toast toast = new Toast(getActivity());
                                View view1 = inflater.inflate(R.layout.layout_toast_success, (ViewGroup) view.findViewById(R.id.Layout_toast));
                                TextView tvMessege = view1.findViewById(R.id.tvMessege1);
                                tvMessege.setText(R.string.verified);
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
        });

        return view;
    }

    private void showLogoutDialog() {
        // Khởi tạo dialog_progressbar
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.layout_dialog_progressbar);
        ProgressBar progressBar = dialog.findViewById(R.id.progressBar);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.logout);
        builder.setMessage(R.string.sureLogout);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                dialog.show();

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
                        editor.remove("emailUser");
                        editor.remove("usernameUser");
                        editor.commit();

                        Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(loginIntent);
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
}