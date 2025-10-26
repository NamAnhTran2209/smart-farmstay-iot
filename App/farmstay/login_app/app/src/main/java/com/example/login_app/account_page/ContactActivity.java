package com.example.login_app.account_page;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.login_app.R;
import com.example.login_app.shared_activity.BaseActivity;

public class ContactActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Thiết lập Activity fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_contact);

        TextView textViewphone = findViewById(R.id.textviewPhone);
        textViewphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNumber = textViewphone.getText().toString();
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null));
                startActivity(intent);
            }
        });
        TextView textViewemail = findViewById(R.id.textViewemail);
        textViewemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = textViewemail.getText().toString();
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", email, null));
                startActivity(Intent.createChooser(intent, "Send email..."));
            }
        });
        TextView textViewquery = findViewById(R.id.textViewquery);
        textViewquery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = textViewquery.getText().toString();
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + phoneNumber));
                startActivity(intent);
            }
        });
        ImageView imageViewfb = findViewById(R.id.imageViewfb);
        imageViewfb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String facebookUrl = "https://www.facebook.com/hoangca09/";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(facebookUrl));
                startActivity(intent);
            }
        });
        ImageView imageViewins = findViewById(R.id.imageViewins);
        imageViewins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String facebookUrl = "https://www.instagram.com/n_anh229/";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(facebookUrl));
                startActivity(intent);
            }
        });
        ImageView imageViewyt = findViewById(R.id.imageViewyt);
        imageViewyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String facebookUrl = "https://www.youtube.com/watch?v=PR_yVho1Txc&list=RDGMEM6CZm14o9sc-Q22TIneLI8gVMHehotFZ8BGo&index=9";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(facebookUrl));
                startActivity(intent);
            }
        });
        ImageView imageViewweb = findViewById(R.id.imageViewweb);
        imageViewweb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String facebookUrl = "https://farmstays.me/";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(facebookUrl));
                startActivity(intent);
            }
        });

        ImageView imback = findViewById(R.id.imBack3);

        imback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getSupportFragmentManager().popBackStack();
                } else {
                    finish();
                }
            }
        });
    }
}