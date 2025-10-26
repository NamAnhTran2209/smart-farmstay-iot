package com.example.login_app.account_page;

import androidx.core.content.FileProvider;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.login_app.R;
import com.example.login_app.shared_activity.BaseActivity;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EditProfileActivity extends BaseActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    private Uri mImageUri;

    Spinner spinnerGender;
    EditText etPhone, etAddress;
    TextView tvSave, tvEmail, tvBirthday;
    ImageView imback, imAvatar;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Thiết lập Activity fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_edit_profile);

        spinnerGender = findViewById(R.id.spinner_gender);
        tvEmail = findViewById(R.id.tvEmail);
        etPhone = findViewById(R.id.etPhone);
        etAddress = findViewById(R.id.etAddress);
        tvBirthday = findViewById(R.id.etBirthday);
        imback = findViewById(R.id.imBack5);
        imAvatar = findViewById(R.id.imAvatar);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.gender, com.chaos.view.R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        sharedPreferences = getSharedPreferences("SaveInfo", MODE_PRIVATE);
        tvEmail.setText(sharedPreferences.getString("email2",""));

        tvBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chonNgay();
            }
        });

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

        imAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogChoseAvatar();
            }
        });

    }

    public void dialogChoseAvatar(){
        final Dialog dialog = new Dialog(EditProfileActivity.this);
        dialog.setContentView(R.layout.layout_dialog_choseavatar);
        TextView tvChoseImageLibary = dialog.findViewById(R.id.tvChoseImageLibary);
        TextView tvChoseCamera = dialog.findViewById(R.id.tvChoseCamera);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);

        tvChoseImageLibary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });

        tvChoseCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        // Error occurred while creating the File
                        ex.printStackTrace();
                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        mImageUri = FileProvider.getUriForFile(EditProfileActivity.this,
                                "com.example.android.fileprovider",
                                photoFile);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
                        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                    }
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            mImageUri = data.getData();
            imAvatar.setImageURI(mImageUri);
        }
    }

    // Create a file for the image captured by the camera
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        String currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void chonNgay(){

        final Calendar calendar = Calendar.getInstance();
        int ngay = calendar.get(Calendar.DATE);
        int thang = calendar.get(Calendar.MONTH);
        int nam = calendar.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                calendar.set(i, i1, i2);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                tvBirthday.setText(simpleDateFormat.format(calendar.getTime()));
            }
        }, nam, thang, ngay);
        datePickerDialog.show();
    }
}