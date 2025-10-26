package com.example.login_app.onboarding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.login_app.R;
import com.example.login_app.auth.LoginActivity;

import adapter.ViewPagerAdapter;
import me.relex.circleindicator.CircleIndicator;

public class OnboardingActivity extends AppCompatActivity {

    private TextView tvSkip;
    private Button btnNext;
    private ViewPager viewPager;
    private CircleIndicator circleIndicator;
    private LinearLayout layoutBottom;
    private RelativeLayout layoutNextSkip;

    private ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Thiết lập Activity fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_onboarding);

        anhXa();

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        boolean isFirstTime = sharedPreferences.getBoolean("isFirstTime", true);

        if (isFirstTime) {
            // Hiển thị phần onboarding
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isFirstTime", false);
            editor.apply();
        } else {
            // Chuyển hướng đến trang đăng nhập
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }


        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(viewPagerAdapter);

        circleIndicator.setViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position == 2){
                    layoutBottom.setVisibility(View.GONE);
                } else {
                    layoutBottom.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void anhXa(){
        tvSkip = findViewById(R.id.tvSkip);
        btnNext = findViewById(R.id.btn_next);
        viewPager = findViewById(R.id.viewPager);
        circleIndicator = findViewById(R.id.circle_Indicator);
        layoutBottom = findViewById(R.id.layout_bottom);
        layoutNextSkip = findViewById(R.id.layout_nextSkip);

        tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(2);
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(viewPager.getCurrentItem() < 2){
                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                }
            }
        });
    }
}