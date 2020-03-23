package com.example.httprequest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class Home extends AppCompatActivity {
    private TextView ErrorMsg;
    private EditText txtUsername;
    private EditText txtPassword;
    String Fname;
    String Lname;
    String Id_Users;
    String Id_Title;
    String Money;
    public static final String FNAME = "FNAME";
    private SharedPreferences sharedPrefer;
    public static final String APP_PREFER = "appPrefer" ;
    public static final String USERNAME_PREFER = "usernamePref";
    public static final String PASSWORD_PREFER = "passwordPref";
    public static final String EMP_TYPE_PREFER = "empTypePref";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

    }
    @Override
    protected void onResume() {
        sharedPrefer=getSharedPreferences(APP_PREFER, Context.MODE_PRIVATE);
        if ((sharedPrefer.contains(Fname)) && sharedPrefer.contains(Lname)){
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            finish();
        }
        super.onResume();
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
                    JSONArray User = obj.getJSONArray("data");

                    for (int i = 0; i < User.length(); i++) {
                        JSONObject c = User.getJSONObject(i);
                        Fname = c.getString("Fname");
                        Lname = c.getString("Lname");
                        Id_Users = c.getString("Id_Users");
                        Id_Title = c.getString("Id_Title");
                        Money = c.getString("Money");
                    }

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
                    SharedPreferences sp = getSharedPreferences("USER", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("FNAME", Fname);
                    editor.putString("Lname", Lname);
                    editor.putString("Id_Users", Id_Users);
                    editor.putString("Id_Title", Id_Title);
                    editor.putString("Money", Money);
                    editor.commit();
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
