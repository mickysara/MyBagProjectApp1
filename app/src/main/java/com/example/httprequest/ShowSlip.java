package com.example.httprequest;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class ShowSlip extends AppCompatActivity {
    private ProgressDialog progressDialog;
    private ImageView imageView;
    String Url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_slip);
        imageView = findViewById(R.id.imageView2);
        Intent intent = getIntent();
        Url = intent.getStringExtra("Image");
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("แจ้งเตือน");
        progressDialog.setMessage("กำลังอัปโหลดรูปภาพกรุณารอสักครู่...");
        progressDialog.show();
        Log.d("helooo",Url);
        Glide.with(imageView.getContext())
                .load("https://www.harmonicmix.xyz/img/DepositUser/"+Url)
                .into(imageView);
        progressDialog.dismiss();
    }
}
