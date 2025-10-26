package com.example.login_app.shared_activity;

import static android.content.Context.MODE_PRIVATE;

import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.login_app.auth.LoginActivity;
import com.example.login_app.R;

import api.RetrofitInterface;
import api.RetrofitServer;

public class BaseFragment extends Fragment {

    private BroadcastReceiver networkReceiver;

    SharedPreferences sharedPreferences;

    RetrofitServer retrofitServer = new RetrofitServer();
    RetrofitInterface retrofitInterface = retrofitServer.Retrofit();

    @Override
    public void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        requireActivity().registerReceiver(networkReceiver, intentFilter);
    }

    @Override
    public void onStop() {
        super.onStop();
        requireActivity().unregisterReceiver(networkReceiver);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    // Hàm hiển thị dialog hết phiên đăng nhập
    private void showSessionExpiredDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.loginAgain);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.logIn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Xử lý đăng nhập lại
                sharedPreferences = getActivity().getSharedPreferences("SaveInfo",MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("notify");
                editor.remove("notifySoil");
                editor.remove("notifyWater");
                editor.remove("notifyWater2");
                editor.remove("token");
                editor.commit();

                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
