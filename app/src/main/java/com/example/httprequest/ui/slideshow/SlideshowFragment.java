package com.example.httprequest.ui.slideshow;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.httprequest.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class SlideshowFragment extends Fragment {

    private SlideshowViewModel slideshowViewModel;
    private EditText Money;
    private EditText Id_Users;
    String IdLogin;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                ViewModelProviders.of(this).get(SlideshowViewModel.class);
        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);
        final TextView textView = root.findViewById(R.id.text_slideshow);
        Id_Users = root.findViewById(R.id.Usertxt);
        Money = root.findViewById(R.id.moneytxt);
        SharedPreferences sp = this.getActivity().getSharedPreferences("USER", Context.MODE_PRIVATE);
        IdLogin = sp.getString("Username","");

        final View button = root.findViewById(R.id.button);
        button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String Id = Id_Users.getText().toString();
                        String money = Money.getText().toString();

                        RequestParams params = new RequestParams();
                        params.put("Id", Id);
                        params.put("Money", money);
                        params.put("Owner",IdLogin);

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
                                    Toast.makeText(getActivity(), "ไม่เจอคน", Toast.LENGTH_LONG).show();
                                    Log.d("hello","Not users");
                                }else if(status.equals("NotMoney"))
                                {
                                    Toast.makeText(getActivity(), "โอนเงินเรียบร้อย่", Toast.LENGTH_LONG).show();
                                    Log.d("hello","Success");
                                }
                                else
                                {
                                    Toast.makeText(getActivity(), "โอนเงินเรียบร้อย่", Toast.LENGTH_LONG).show();
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
        );
        return root;
    }
}