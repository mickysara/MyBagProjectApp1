package com.example.httprequest;

import android.graphics.Color;
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
import java.util.StringTokenizer;

import cz.msebera.android.httpclient.Header;

public class TestShow extends AppCompatActivity {

    RecyclerView recyclerView;
    List<Person> people;
    public String IMAGE_URL = "";
    TextView Hello;


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
                                    new Person(item.getString("Transaction_Of"),
                                            item.getString("Method"),
                                            item.getString("Recived_Transaction"),
                                            item.getString("Money"),
                                            item.getString("Status"),
                                            item.getString("TimeStamp"),
                                            item.getString("chk")));

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
    public static class Person {
        public String Transaction_Of;
        public String Method;
        public String Recived_Transaction;
        public String Money;
        public String Status;
        public String TimeStamp;
        public String chk;
        public boolean isChecked;

        public Person(String Transaction_Of, String Method, String Recived_Transaction,String Money,String Status,String TimeStamp,String chk) {
            this.Transaction_Of = Transaction_Of;
            this.Method = Method;
            this.Recived_Transaction = Recived_Transaction;
            this.Money = Money;
            this.Status = Status;
            this.TimeStamp = TimeStamp;
            this.chk = chk;


        }
    }

    public static class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.PersonHolder>  {

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
            Person person;
            person = list.get(position);
            holder.person = person;
            StringTokenizer tokens = new StringTokenizer(person.TimeStamp, "-");
            String year = tokens.nextToken();// this will contain "Fruit"
            String month = tokens.nextToken();
            String last = tokens.nextToken();

            StringTokenizer getday = new StringTokenizer(last," ");
            String day = getday.nextToken();
            String Time = getday.nextToken();

            int years = Integer.parseInt(year) + 543;

            if(month.equals("01"))
            {
                month = "ม.ค.";
            }else if(month.equals("02"))
            {
                month = "ก.พ.";
            }else if(month.equals("03"))
            {
                month = "มี.ค.";
            }else if(month.equals("04"))
            {
                month = "เม.ย.";
            }
            else if(month.equals("05"))
            {
                month = "พ.ค.";
            }else if(month.equals("06"))
            {
                month = "มิ.ย.";
            }else if(month.equals("07"))
            {
                month = "ก.ค.";
            }else if(month.equals("08"))
            {
                month = "ส.ค.";
            }else if(month.equals("09"))
            {
                month = "ก.ย.";
            }else if(month.equals("10"))
            {
                month = "ต.ค.";
            }else if(month.equals("11"))
            {
                month = "พ.ย.";
            }else if(month.equals("12"))
            {
                month = "ธ.ค.";
            }

            //Log.d("mylog","xx" + person.imageFileName);
            holder.Date.setText(day + " " + month + " " + years + " เวลา " +Time);
            holder.Method.setText(person.Method);

            if(person.Method.equals("ฝากเงิน"))
            {
                holder.Amount.setText("+ " + person.Money);
                holder.Amount.setTextColor(Color.parseColor("#3cab7a"));

            }else if(person.Method.equals("โอนเงิน"))
            {
                holder.Transac.setText("เลขที่บัญชีที่ปลายทาง: " + person.Recived_Transaction);
                holder.Transac.setTextColor(Color.parseColor("#000000"));
                holder.Amount.setText("- " + person.Money);
                holder.Amount.setTextColor(Color.parseColor("#ef4c43"));
            }



            //holder.checkBox.setChecked(person.isChecked);
            //holder.textView.setText(String.format("%d %s(%d)", position, person.firstName, person.age));
            //holder.textView.setTextColor(Color.RED);



        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public static class PersonHolder extends RecyclerView.ViewHolder {

            public Person person;
            public TextView Amount;
            public TextView Method;
            public TextView Date;
            public TextView Transac;
            public ImageView imageView;
            public CheckBox checkBox;

            public PersonHolder(View itemView) {
                super(itemView);

                Amount = itemView.findViewById(R.id.amount);
                Method = itemView.findViewById(R.id.Method);
                Date = itemView.findViewById(R.id.Date);
                Transac = itemView.findViewById(R.id.Transac);


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
