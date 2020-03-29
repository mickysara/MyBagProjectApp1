package com.example.httprequest.ui.home;

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
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.httprequest.R;
import com.example.httprequest.TestShow;
import com.google.firebase.iid.FirebaseInstanceId;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class HomeFragment extends Fragment {
    private TextView Money;

    private HomeViewModel homeViewModel;
    RecyclerView recyclerView;
    List<TestShow.Person> people;
    public String IMAGE_URL = "";
    TextView Name;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        Log.i("token hello", FirebaseInstanceId.getInstance().getToken());
        Name = root.findViewById(R.id.Name);
        Money = root.findViewById(R.id.showmoney);
        SharedPreferences sp = this.getActivity().getSharedPreferences("USER", Context.MODE_PRIVATE);


        String Fname = sp.getString("FNAME","");
        String Lname = sp.getString("LNAME","");
        Name.setText(Fname +" "+Lname);
        String M = sp.getString("Money","");



        Money.setText("คุณมียอดเงินคงเหลือ"+ M);

        recyclerView = root.findViewById(R.id.recyclerView);

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
                                    new TestShow.Person(item.getString("Transaction_Of"),
                                            item.getString("Method"),
                                            item.getString("Recived_Transaction"),
                                            item.getString("Money"),
                                            item.getString("Status"),
                                            item.getString("TimeStamp"),
                                            item.getString("chk")));

                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                            recyclerView.setAdapter(new TestShow.PersonAdapter(people));
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


        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });
        return root;
    }
    class Person {
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

    class PersonAdapter extends RecyclerView.Adapter<TestShow.PersonAdapter.PersonHolder>  {

        private List<TestShow.Person> list;

        public PersonAdapter(@NonNull List<TestShow.Person> list) {
            this.list = list;
        }

        @Override
        public TestShow.PersonAdapter.PersonHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_person, parent, false);
            return new TestShow.PersonAdapter.PersonHolder(view);
        }

        @Override
        public void onBindViewHolder(TestShow.PersonAdapter.PersonHolder holder, final int position) {
            TestShow.Person person = list.get(position);
            holder.person = person;

            //Log.d("mylog","xx" + person.imageFileName);

            holder.Method.setText(person.Method);
            holder.Date.setText(person.TimeStamp);
            holder.Amount.setText(person.Money);


            //holder.checkBox.setChecked(person.isChecked);
            //holder.textView.setText(String.format("%d %s(%d)", position, person.firstName, person.age));
            //holder.textView.setTextColor(Color.RED);



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
}