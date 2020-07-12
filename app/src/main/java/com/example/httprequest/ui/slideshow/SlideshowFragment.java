package com.example.httprequest.ui.slideshow;

import android.app.Person;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.httprequest.AwesomeDialogFragment;
import com.example.httprequest.R;
import com.example.httprequest.ShowSlip;
import com.example.httprequest.ui.send.SendFragment;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

import cz.msebera.android.httpclient.Header;

public class SlideshowFragment extends Fragment {

    List<Person> people;
    RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);
        final TextView textView = root.findViewById(R.id.text_send);
        recyclerView = root.findViewById(R.id.recyclerView);

        people = new ArrayList<Person>();

        RequestParams params = new RequestParams();
        SharedPreferences sp = this.getActivity().getSharedPreferences("USER", Context.MODE_PRIVATE);
        String Id_Users = sp.getString("Id_Users","");

        params.put("Id_Users", Id_Users);
        Log.d("nitipong","hi");

        AsyncHttpClient https = new AsyncHttpClient();
        https.post("https://www.harmonicmix.xyz/api/GetJoinAc_api", params, new JsonHttpResponseHandler(){
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
                                    new Person(item.getString("Name_Activities"),
                                            item.getString("DateStart"),
                                            item.getString("DateEnd"),
                                            item.getString("TimeStart"),
                                            item.getString("TimeEnd"),
                                            item.getString("Compel"),
                                            item.getString("Id_BookActivity"),
                                            item.getString("NameLocation")));

                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                            recyclerView.setAdapter(new PersonAdapter(people));
                        }
                    }else{
                        Toast.makeText(getContext(), "ไม่สามารถแสดงข้อมูลได้", Toast.LENGTH_LONG).show();
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

        return root;
    }

    class Person {
        public String Name_Activities;
        public String DateStart;
        public String DateEnd;
        public String TimeStart;
        public String TimeEnd;
        public String Compel;
        public String Id_BookActivity;
        public String NameLocation;


        public Person(String Name_Activities, String DateStart, String DateEnd, String TimeStart, String TimeEnd,String Compel,String Id_BookActivity,String NameLocation) {
            this.Name_Activities = Name_Activities;
            this.DateStart = DateStart;
            this.DateEnd       = DateEnd;
            this.TimeStart = TimeStart;
            this.TimeEnd = TimeEnd;
            this.Compel = Compel;
            this.Id_BookActivity = Id_BookActivity;
            this.NameLocation = NameLocation;
        }
    }
    class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.PersonHolder>  {

        private List<Person> list;



        public PersonAdapter(@NonNull List<Person> list) {
            this.list = list;

        }


        @Override
        public PersonAdapter.PersonHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_activityy, parent, false);
            return new PersonAdapter.PersonHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull PersonAdapter.PersonHolder holder, int position) {
            Person person;
            person = list.get(position);
            holder.person = person;
            Log.d("hellooo",person.DateStart);

                StringTokenizer tokens = new StringTokenizer(person.DateStart, "-");
                String year = tokens.nextToken();// this will contain "Fruit"
                String month = tokens.nextToken();
                String last = tokens.nextToken();



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
                holder.DateStart.setText("วันที่เริ่ม " + last + " " + month + " " + years);

                Log.d("hellooo",person.DateEnd);
                StringTokenizer tokenss = new StringTokenizer(person.DateStart, "-");
                String y = tokenss.nextToken();// this will contain "Fruit"
                String months = tokenss.nextToken();
                String lasts = tokenss.nextToken();



                int ya = Integer.parseInt(y) + 543;

                if(months.equals("01"))
                {
                    months = "ม.ค.";
                }else if(months.equals("02"))
                {
                    months = "ก.พ.";
                }else if(months.equals("03"))
                {
                    months = "มี.ค.";
                }else if(months.equals("04"))
                {
                    months = "เม.ย.";
                }
                else if(months.equals("05"))
                {
                    months = "พ.ค.";
                }else if(months.equals("06"))
                {
                    months = "มิ.ย.";
                }else if(months.equals("07"))
                {
                    months = "ก.ค.";
                }else if(months.equals("08"))
                {
                    months = "ส.ค.";
                }else if(months.equals("09"))
                {
                    months = "ก.ย.";
                }else if(months.equals("10"))
                {
                    months = "ต.ค.";
                }else if(months.equals("11"))
                {
                    months = "พ.ย.";
                }else if(months.equals("12"))
                {
                    months = "ธ.ค.";
                }

                //Log.d("mylog","xx" + person.imageFileName);
                holder.DateEnd.setText("สิ้นสุด " + lasts + " " + months + " " + ya);
                holder.Name_Activities.setText("กิจกรรม: " + person.Name_Activities);
                holder.TimeStart.setText("เวลา: " +person.TimeStart);
                holder.TimeEnd.setText("เวลา: " +person.TimeEnd);
                holder.NameLocation.setText("สถานที่: " + person.NameLocation);
                if(person.Compel.equals(0))
                {
                    holder.com.setText("กิจกรรมบังคับ");
                }else {
                    holder.com.setText("กิจกรรมเลือก");
                }




        }
        @Override
        public int getItemCount() {
            return list.size();
        }

        class PersonHolder extends RecyclerView.ViewHolder {

            public Person person;
            public TextView Name_Activities;
            public TextView DateStart;
            public TextView DateEnd;
            public TextView TimeStart;
            public TextView TimeEnd;
            public TextView NameLocation;
            public TextView com;
            RelativeLayout parentLayout;

            public PersonHolder(View itemView) {
                super(itemView);

                Name_Activities = itemView.findViewById(R.id.Name);
                DateStart = itemView.findViewById(R.id.DateStart);
                DateEnd = itemView.findViewById(R.id.DateEnd);
                TimeStart = itemView.findViewById(R.id.TimeStart);
                TimeEnd = itemView.findViewById(R.id.TimeEnd);
                NameLocation = itemView.findViewById(R.id.Location);
                com = itemView.findViewById(R.id.com);


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