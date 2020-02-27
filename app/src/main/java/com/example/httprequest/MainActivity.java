package com.example.httprequest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {
    private TextView ErrorMsg;
    private EditText txtUsername;
    private EditText txtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtUsername = findViewById(R.id.username);
        txtPassword = findViewById(R.id.password);
        ErrorMsg = findViewById(R.id.ErrorMsg);
        ErrorMsg.setVisibility(View.INVISIBLE);
    }

    public void onSignin(View v)
    {
        String username = txtUsername.getText().toString();
        String password = txtPassword.getText().toString();

        RequestParams params = new RequestParams();
        params.put("username", username);
        params.put("password", password);

        AsyncHttpClient http = new AsyncHttpClient();
        http.post("https://www.harmonicmix.xyz/api/Login_api", params, new JsonHttpResponseHandler(){
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
                    Test = (String) obj.get("Test");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(status.equals("true"))
                {

                    Toast.makeText(getApplicationContext(), "สวัสดีคุณ"+Test, Toast.LENGTH_LONG).show();
                    Log.d("onSuccess", Integer.toString(statusCode));
                    Log.d("onSuccess", response.toString());
                    ErrorMsg.setText("username และ password ถูกต้อง");
                    ErrorMsg.setVisibility(View.VISIBLE);
                    Intent intent = new Intent(getApplicationContext(), Transaction.class);
                    startActivity(intent);

                }else
                {
                    Toast.makeText(getApplicationContext(), "username และ password ไม่ถูกต้องกรุณากรอกใหม่", Toast.LENGTH_LONG).show();
                    ErrorMsg.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.d("onFailure", Integer.toString(statusCode));
            }
        });
        Toast.makeText(this, "Clicked on Button", Toast.LENGTH_LONG).show();
    }
}
