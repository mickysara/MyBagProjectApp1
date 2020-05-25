package com.example.httprequest.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.httprequest.Passcode;
import com.example.httprequest.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ShowQr extends AppCompatActivity {
    private ImageView imageView;
     String Url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_qr);
        imageView = findViewById(R.id.imageView2);

        SharedPreferences sp = this.getSharedPreferences("USER", Context.MODE_PRIVATE);
        String Iduser = sp.getString("Id_Users","");

        RequestParams params = new RequestParams();
        params.put("Iduser", Iduser);

        AsyncHttpClient http = new AsyncHttpClient();
        http.post("https://www.harmonicmix.xyz/api/GetQr_api", params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response ) {
                JSONObject obj = null;
                try {
                    obj = new JSONObject(response.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String Test = null;
                try {
                    Url = (String) obj.get("Url");
                    Log.d("hello",Url);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Glide.with(imageView.getContext())
                        .load(Url)
                        .into(imageView);



            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.d("onFailure", Integer.toString(statusCode));
            }
        });



    }
}
