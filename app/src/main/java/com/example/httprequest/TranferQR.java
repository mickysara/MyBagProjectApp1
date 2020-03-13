package com.example.httprequest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class TranferQR extends AppCompatActivity {


    TextView id,users,Money;
    String IdUser;
    String ii;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tranfer_qr);
        users = findViewById(R.id.User);
        id = findViewById(R.id.id);
        Money = findViewById(R.id.Money);

        Intent intent = getIntent();

        // Get the extras (if there are any)
        Bundle extras = intent.getExtras();
        if (extras != null) {
            if (extras.containsKey("Name")) {
                boolean isNew = extras.getBoolean("Name", false);

                // TODO: Do something with the value of isNew.
                users.setText(intent.getStringExtra("Name"));
            }
            if (extras.containsKey("id")) {
                boolean isNew = extras.getBoolean("id", false);
                String test = intent.getStringExtra("id");
                id.setText(intent.getStringExtra("id"));
                ii = intent.getStringExtra("id");
                Log.d("test","hello"+test);
                // TODO: Do something with the value of isNew.
            }
            if (extras.containsKey("idUser")) {
                boolean isNew = extras.getBoolean("idUser", false);
                String test = intent.getStringExtra("idUser");
                IdUser = intent.getStringExtra("idUser");
                Log.d("test","hello"+test);
                // TODO: Do something with the value of isNew.
            }
        }
    }

    public void OnSubmit(View v)
    {
        SharedPreferences sp = getSharedPreferences("USER", Context.MODE_PRIVATE);
        String IdLogin = sp.getString("Username","");
        RequestParams params = new RequestParams();
        params.put("Id", ii);
        params.put("Money", Money.getText().toString());
        params.put("Owner",IdLogin);
        Log.d("hee", IdLogin);
        AsyncHttpClient http = new AsyncHttpClient();
        http.post("https://www.harmonicmix.xyz/api/Deposit_api", params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response ) {
                JSONObject obj = null;
                try {
                    obj = new JSONObject(response.toString());


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String status = null;
                String Test = null;
                try {
                    status = (String) obj.get("status");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(status.equals("DontHave"))
                {
                    Toast.makeText(getApplicationContext(), "ไม่เจอคน", Toast.LENGTH_LONG).show();
                    Log.d("hello","Not users");
                }else if(status.equals("NotMoney"))
                {
                    Toast.makeText(getApplicationContext(), "โอนเงินเรียบร้อย่", Toast.LENGTH_LONG).show();
                    Log.d("hello","Success");
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "โอนเงินเรียบร้อย่", Toast.LENGTH_LONG).show();
                    Log.d("hello","Success");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.d("onFailure", Integer.toString(statusCode));
            }
        });
    }
}
