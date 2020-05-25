package com.example.httprequest.ui.gallery;

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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.httprequest.R;
import com.example.httprequest.TestShow;
import com.example.httprequest.ui.ShowQr;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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
                                            item.getString("TimeStamp ")));

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

        final View button = root.findViewById(R.id.Showqr);
        button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), ShowQr.class);
                        startActivity(intent);
//                        SharedPreferences.Editor editor = sp.edit();
//                        editor.clear();
//                        editor.apply();
//
//                        Intent intent = new Intent(getContext(), MainActivity.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(intent);
//                        getActivity().finish();
                    }
                }
        );
        return root;
    }

    class Person {
        public String name_activities;
        public String name_typeActivity;
        public String nameLocation;
        public String timeStamp_;
        public String Status;
        public String TimeStamp;
        public String chk;
        public boolean isChecked;


        public Person(String name_activities, String name_typeActivity, String nameLocation, String timeStamp_) {
            this.name_activities = name_activities;
            this.name_typeActivity = name_typeActivity;
            this.nameLocation       = nameLocation;
            this.TimeStamp = timeStamp_;
        }
        }
    }

    class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.PersonHolder>  {

        private List<Person> list;

        public PersonAdapter(List<GalleryFragment.Person> people) {
            this.list = list;
        }


        @Override
        public PersonHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_person, parent, false);
            return new PersonHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull PersonHolder holder, int position) {

        }
        @Override
        public int getItemCount() {
            return list.size();
        }

        class PersonHolder extends RecyclerView.ViewHolder {

            public TestShow.Person person;
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