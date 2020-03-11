package com.example.httprequest.ui.tools;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.httprequest.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.StringTokenizer;

import cz.msebera.android.httpclient.Header;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;


public class ToolsFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public static final int RequestPermissionCode = 1;
    protected GoogleApiClient googleApiClient;
    protected TextView longitudeText;
    protected TextView latitudeText;
    protected Location lastLocation;
    private Button scan_btn;
    private FusedLocationProviderClient fusedLocationProviderClient;
    public String La;
    public  String Long;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_tools, container, false);
        longitudeText = (TextView) root.findViewById(R.id.Latitude);
        latitudeText = (TextView) root.findViewById(R.id.Longtitude);

        googleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());

        scan_btn = (Button) root.findViewById(R.id.btnQRscan);

        scan_btn.setOnClickListener(view -> {
            IntentIntegrator integrator = IntentIntegrator.forSupportFragment(ToolsFragment.this);
            integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
            integrator.setOrientationLocked(false);
            integrator.setPrompt("Scan");
            integrator.setCameraId(0);
            integrator.setBeepEnabled(false);
            integrator.setBarcodeImageEnabled(false);
            integrator.initiateScan();
        });

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    public void onStop() {
        if (googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(getContext(), ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermission();
        } else {
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener((Activity) getContext(), new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                // Logic to handle location object
                                latitudeText.setText(String.valueOf(location.getLatitude()));
                                La = String.valueOf(location.getLatitude());
                                Long = String.valueOf(location.getLongitude());
                                longitudeText.setText(String.valueOf(location.getLongitude()));
                            }
                        }
                    });
        }
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions((Activity) getContext(), new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION}, 225);
        return;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e("MainActivity", "Connection failed: " + connectionResult.getErrorCode());
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e("MainActivity", "Connection suspendedd");
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null){
            if(result.getContents()==null){
                Toast.makeText(getContext(), "You cancelled the scanning", Toast.LENGTH_LONG).show();
            }
            else {

//                Toast.makeText(getContext(), result.getContents(),Toast.LENGTH_LONG).show();

                SharedPreferences sp = getContext().getSharedPreferences("USER", Context.MODE_PRIVATE);
                String Iduser = sp.getString("Id_Users","");
                String resultString = result.getContents();
                StringTokenizer tokens = new StringTokenizer(resultString, "|");
                String first = tokens.nextToken();// this will contain "Fruit"
                String second = tokens.nextToken();

                StringTokenizer GetType = new StringTokenizer(second, "=");
                String nameType = GetType.nextToken();// this will contain "Fruit"
                String Type = GetType.nextToken();

                StringTokenizer Getid = new StringTokenizer(first, "=");
                String nameid = Getid.nextToken();// this will contain "Fruit"
                String id = Getid.nextToken();

                if(Type.equals("Activity"))
                {
                    RequestParams params = new RequestParams();
                    params.put("IdAc",id );
                    params.put("IdUser", Iduser);
                    params.put("Latitude",latitudeText.getText().toString());
                    params.put("Longtitude",longitudeText.getText().toString());
                    Log.d("hello", "onActivityResult: " + La );
                    AsyncHttpClient http = new AsyncHttpClient();
                    http.post("https://www.harmonicmix.xyz/api/JoinActivity_api", params, new JsonHttpResponseHandler(){
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response ) {
                            JSONObject obj = null;
                            try {
                                obj = new JSONObject(response.toString());


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            String status = null;
                            try {
                                status = (String) obj.get("status");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if(status.equals("NotinActivities"))
                            {
                                Toast.makeText(getContext(), "ไม่มีสิทธิ์"+Iduser, Toast.LENGTH_LONG).show();
                                Log.d("hello","Not users");
                            }else if(status.equals("NotinDate"))
                            {
                                Toast.makeText(getContext(), "ไม่ได้อยู่ในวันนั้น", Toast.LENGTH_LONG).show();
                                Log.d("hello","Success");

                            }else if(status.equals("NotinArea"))
                            {
                                Toast.makeText(getContext(), "ไม่ได้อยู่ในพื้นที่", Toast.LENGTH_LONG).show();
                                Log.d("hello","Success");
                            }
                            else
                            {
                                Toast.makeText(getContext(), "บันทึกกิจกรรม", Toast.LENGTH_LONG).show();
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
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}