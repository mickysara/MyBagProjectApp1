package com.example.httprequest.ui.send;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.httprequest.R;
import com.example.httprequest.ShowSlip;
import com.example.httprequest.ui.ShowQr;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import cz.msebera.android.httpclient.Header;

public class SendFragment extends Fragment {
    List<Person> people;
    RecyclerView recyclerView;
    private Context mContext;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_send, container, false);
        final TextView textView = root.findViewById(R.id.text_send);
        recyclerView = root.findViewById(R.id.recyclerView);

        people = new ArrayList<Person>();

        RequestParams params = new RequestParams();
        SharedPreferences sp = this.getActivity().getSharedPreferences("USER", Context.MODE_PRIVATE);
        String Id_Users = sp.getString("Id_Users","");

        params.put("Id_Users", Id_Users);
        Log.d("nitipong","hi");

        AsyncHttpClient https = new AsyncHttpClient();
        https.post("https://www.harmonicmix.xyz/api/GetDeposit", params, new JsonHttpResponseHandler(){
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
                                    new Person(item.getString("Money"),
                                            item.getString("Slip"),
                                            item.getString("Status"),
                                            item.getString("DateTime"),
                                            item.getString("Detail")));

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
        public String Money;
        public String Slip;
        public String Status;
        public String Detail;
        public String DateTime;



        public Person(String Money, String Slip, String Status, String DateTime, String Detail) {
            this.Money = Money;
            this.Slip = Slip;
            this.Status       = Status;
            this.Detail = Detail;
            this.DateTime = DateTime;
        }
    }
}

class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.PersonHolder>  {

    private List<SendFragment.Person> list;



    public PersonAdapter(@NonNull List<SendFragment.Person> list) {
        this.list = list;

    }


    @Override
    public PersonHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_deposit, parent, false);
        return new PersonAdapter.PersonHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PersonHolder holder, int position) {
        SendFragment.Person person;
        person = list.get(position);
        holder.person = person;
        Log.d("hellooo",person.DateTime);
        StringTokenizer tokens = new StringTokenizer(person.DateTime, "-");
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
        holder.DateTime.setText("วันที่ " + day + " " + month + " " + years);
        DecimalFormat formatter = new DecimalFormat("#,###,###");
        String yourFormattedString = formatter.format(Integer.parseInt(person.Money));
        holder.Money.setText("จำนวนเงิน: " + yourFormattedString);
        holder.Status.setText("สถานะ: " + person.Status);

        if(person.equals("ไม่อนุมัติ"))
        {
            holder.Status.setTextColor(Color.parseColor("#ef4c43"));
            holder.Detail.setText("หมายเหตุ: " + person.Detail);
        }else if(person.equals("อนุมัติ"))
        {
            holder.Status.setTextColor(Color.parseColor("#3cab7a"));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent;
                    intent = new Intent(v.getContext(), ShowSlip.class);
                intent.putExtra("Image", String.valueOf(person.Slip));
                v.getContext().startActivity(intent);
            }
        });


    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    class PersonHolder extends RecyclerView.ViewHolder {

        public SendFragment.Person person;
        public TextView Money;
        public TextView Slip;
        public TextView Status;
        public TextView Detail;
        public TextView DateTime;
        public ImageView imageView;
        public ImageView imageView3;
        RelativeLayout parentLayout;

        public PersonHolder(View itemView) {
            super(itemView);

            Money = itemView.findViewById(R.id.Money);
            imageView3 = itemView.findViewById(R.id.imageView3);
            Status = itemView.findViewById(R.id.status);
            Detail = itemView.findViewById(R.id.Detail);
            DateTime = itemView.findViewById(R.id.DateTime);


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