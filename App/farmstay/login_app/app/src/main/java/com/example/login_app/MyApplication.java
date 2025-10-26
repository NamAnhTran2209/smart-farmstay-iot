package com.example.login_app;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;

import androidx.fragment.app.Fragment;

import com.example.login_app.auth.LoginActivity;

import java.util.LinkedList;
import java.util.List;

import io.socket.client.Socket;
import model.SocketIO;

public class MyApplication extends Application {

    private List<Activity> mActivityList = new LinkedList<>();
    private List<Fragment> mFragmentList = new LinkedList<>();

    public void addActivity(Activity activity) {
        mActivityList.add(activity);
    }

    public void removeActivity(Activity activity) {
        mActivityList.remove(activity);
    }

    public void addFragment(Fragment fragment) {
        mFragmentList.add(fragment);
    }

    public void removeFragment(Fragment fragment) {
        mFragmentList.remove(fragment);
    }

    public void recreateAll() {
        for (Activity activity : mActivityList) {
            if (activity instanceof LoginActivity) {
                activity.recreate();
            } else {
                activity.finish(); // Đóng hoạt động hiện tại nếu không phải là LoginActivity
            }
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
