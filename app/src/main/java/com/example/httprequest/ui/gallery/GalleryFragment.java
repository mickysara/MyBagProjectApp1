package com.example.httprequest.ui.gallery;

import android.content.Context;
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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.httprequest.R;
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

public class GalleryFragment extends Fragment {
    List<Person> people;
    RecyclerView recyclerView;
    private GalleryViewModel galleryViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        recyclerView = root.findViewById(R.id.recyclerView);

        people = new ArrayList<Person>();

        RequestParams params = new RequestParams();
        SharedPreferences sp = this.getActivity().getSharedPreferences("USER", Context.MODE_PRIVATE);
        String Id_Users = sp.getString("Id_Users","");

        params.put("Id_Users", Id_Users);
        Log.d("nitipong","hi");

        AsyncHttpClient https = new AsyncHttpClient();
        https.post("https://www.harmonicmix.xyz/api/GetJoinActivity", params, new JsonHttpResponseHandler(){
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
                                            item.getString("Name_TypeActivity"),
                                            item.getString("NameLocation"),
                                            item.getString("TimeStamp")));

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
        public String name_activities;
        public String name_typeActivity;
        public String nameLocation;

        public String TimeStamp;



        public Person(String name_activities, String name_typeActivity, String nameLocation, String timeStamp) {
            this.name_activities = name_activities;
            this.name_typeActivity = name_typeActivity;
            this.nameLocation       = nameLocation;
            this.TimeStamp = timeStamp;
        }
        }
    }

    class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.PersonHolder>  {

        private List<GalleryFragment.Person> list;

        public PersonAdapter(@NonNull List<GalleryFragment.Person> list) {
            this.list = list;
        }


        @Override
        public PersonHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_joinac, parent, false);
            return new PersonHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull PersonHolder holder, int position) {
            GalleryFragment.Person person;
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
            holder.Date.setText("วันที่" + day + " " + month + " " + years);
            holder.NameAc.setText("ชื่อ: " + person.name_activities);
            holder.Type.setText("ประเภท: " + person.name_typeActivity);
            holder.Location.setText("สถานที่: " + person.nameLocation);

        }
        @Override
        public int getItemCount() {
            return list.size();
        }

        class PersonHolder extends RecyclerView.ViewHolder {

            public GalleryFragment.Person person;
            public TextView NameAc;
            public TextView Location;
            public TextView Date;
            public TextView Type;
            public ImageView imageView;
            public CheckBox checkBox;

            public PersonHolder(View itemView) {
                super(itemView);

                NameAc = itemView.findViewById(R.id.NameAc);
                Location = itemView.findViewById(R.id.Location);
                Type = itemView.findViewById(R.id.status);
                Date = itemView.findViewById(R.id.Detail);


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