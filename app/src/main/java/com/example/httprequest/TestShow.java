package com.example.httprequest;

import android.app.Person;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class TestShow extends AppCompatActivity {

    RecyclerView recyclerView;
    List<Person> people;
    public String IMAGE_URL = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testshow);
        Log.d("Statuse","hi");
        //List data
        recyclerView = findViewById(R.id.recyclerView);

        people = new ArrayList<>();
        RequestParams params = new RequestParams();
        params.put("username", "025930461012-6");
        params.put("password", "1234");
        Log.d("nitipong","hi");

        AsyncHttpClient http = new AsyncHttpClient();
        http.post("https://www.harmonicmix.xyz/api/Getuser", params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {

                    Log.d("Statuse","Success");
                    JSONObject obj = new JSONObject(response.toString());
                    String status = obj.getString("status");
                    if(status.equals("true")) {
                        JSONArray data = obj.getJSONArray("data");
                        //Log.d("myCat",String.valueOf(data.length()));
                        for(int i=0; i < data.length(); i++ ) {
                            //Log.d("myCat", String.valueOf(i));
                            JSONObject item = data.getJSONObject(i);
                            people.add(
                                    new Person(item.getString("Id_Student"),
                                            item.getString("Fname"),
                                            item.getString("Lname")));

                            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            recyclerView.setAdapter(new PersonAdapter(people));
                        }
                    }else{
                        Toast.makeText(getApplicationContext(), "ไม่สามารถแสดงข้อมูลได้", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                Log.d("onFailure", Integer.toString(statusCode));
            }
        });


    }
    class Person {
        public String empID;
        public String firstName;
        public String lastName;
        public boolean isChecked;

        public Person(String empID, String firstName, String lastName) {
            this.empID = empID;
            this.firstName = firstName;
            this.lastName = lastName;

        }
    }

    class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.PersonHolder>  {

        private List<Person> list;

        public PersonAdapter(@NonNull List<Person> list) {
            this.list = list;
        }

        @Override
        public PersonHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_person, parent, false);
            return new PersonHolder(view);
        }

        @Override
        public void onBindViewHolder(PersonHolder holder, final int position) {
            Person person = list.get(position);
            holder.person = person;

            //Log.d("mylog","xx" + person.imageFileName);

            holder.textView.setText(person.empID + " " +person.firstName + " " + person.lastName);

            //holder.checkBox.setChecked(person.isChecked);
            //holder.textView.setText(String.format("%d %s(%d)", position, person.firstName, person.age));
            //holder.textView.setTextColor(Color.RED);

            final String empID = person.empID;

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class PersonHolder extends RecyclerView.ViewHolder {

            public Person person;
            public TextView textView;
            public ImageView imageView;
            public CheckBox checkBox;

            public PersonHolder(View itemView) {
                super(itemView);

                textView = itemView.findViewById(R.id.txtFullName);
                imageView = itemView.findViewById(R.id.imageView);

             /*
             checkBox = (CheckBox) itemView.findViewById(R.id.checkbox);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    person.isChecked = b;
                }
            });
            */
            }
        }

    }
}
