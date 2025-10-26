package com.example.login_app.home_page;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.login_app.R;
import com.example.login_app.shared_activity.BaseActivity;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class Farm_1Activity extends BaseActivity {

    ImageView imback, imFarm;
    TextView tvNameFarm, tvDescription, tvAddress, tvPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Thiết lập Activity fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_farm1);

        imback = findViewById(R.id.imBack4);
        imFarm = findViewById(R.id.imageView2);
        tvNameFarm = findViewById(R.id.nameFarm);
        tvDescription = findViewById(R.id.descriptionFarm);
        tvAddress = findViewById(R.id.addressFarm);
        tvPrice = findViewById(R.id.priceFarm);

        imback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Farm_1Activity.this, DashboardActivity.class);
                startActivity(intent);
            }
        });

        // Lấy dữ liệu từ Intent
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String imageUrl = intent.getStringExtra("imageUrl");
        String name = intent.getStringExtra("name");
        String description = intent.getStringExtra("description");
        String address = intent.getStringExtra("address");
        String price = intent.getStringExtra("price");

        int priceInt = Integer.parseInt(price);

        NumberFormat numberFormat = NumberFormat.getInstance(Locale.getDefault());
        DecimalFormat decimalFormat = (DecimalFormat) numberFormat;
        decimalFormat.applyPattern("#,### VND");

        String formattedPrice = decimalFormat.format(priceInt);

        Glide.with(this)
                .load(imageUrl)
                .into(imFarm);

        tvNameFarm.setText(name);
        tvDescription.setText(description);
        tvAddress.setText(address);
        tvPrice.setText(formattedPrice);
    }
}