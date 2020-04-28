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

import com.example.httprequest.AwesomeDialogFragment;
import com.example.httprequest.R;
import com.example.httprequest.TranferQR;
import com.example.httprequest.Transaction;
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
    private static String TAG_DIALOG = "dialog";
    private Button scan_btn;
    private FusedLocationProviderClient fusedLocationProviderClient;
    public String La="100";
    public  String Long="200";
    private ToolsFragment ToolsFragmentListener;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_tools, container, false);
        latitudeText = (TextView) root.findViewById(R.id.Latitude);
        longitudeText = (TextView) root.findViewById(R.id.Longtitude);

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
                                Log.d("Latitude",La);
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
                fusedLocationProviderClient.getLastLocation()
                        .addOnSuccessListener((Activity) getContext(), new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                // Got last known location. In some rare situations this can be null.
                                if (location != null) {
                                    // Logic to handle location object
                                    latitudeText.setText(String.valueOf(location.getLatitude()));
                                    La = String.valueOf(location.getLatitude());
                                    Log.d("Latitude",La);
                                    Long = String.valueOf(location.getLongitude());
                                    longitudeText.setText(String.valueOf(location.getLongitude()));
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
                                        params.put("Latitude",La);
                                        params.put("Longtitude",Long);
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
                                                    AwesomeDialogFragment fragment = new AwesomeDialogFragment.Builder()
                                                            .setMessage(R.string.Not_In_Activities)
                                                            .setNegative(R.string.cancel)
                                                            .setPosition(R.string.ok)
                                                            .build();;
                                                    fragment.show(getChildFragmentManager(), TAG_DIALOG);
                                                }else if(status.equals("NotinDate"))
                                                {
                                                    AwesomeDialogFragment fragment = new AwesomeDialogFragment.Builder()
                                                            .setMessage(R.string.Not_In_Date)
                                                            .setNegative(R.string.cancel)
                                                            .setPosition(R.string.ok)
                                                            .build();
                                                    fragment.show(getChildFragmentManager(), TAG_DIALOG);
                                                }else if(status.equals("NotinArea"))
                                                {
                                                    AwesomeDialogFragment fragment = new AwesomeDialogFragment.Builder()
                                                            .setMessage(R.string.Not_In_Area)
                                                            .setNegative(R.string.cancel)
                                                            .setPosition(R.string.ok)
                                                            .build();
                                                    fragment.show(getChildFragmentManager(), TAG_DIALOG);
                                                    Log.d("hello","Success");
                                                }
                                                else
                                                {
                                                    Toast.makeText(getContext(), "บันทึกกิจกรรม", Toast.LENGTH_LONG).show();
                                                    Log.d("hello","Success");
                                                    /*
                                                    Fragment newFragment = new HomeFragment();
                                                    // consider using Java coding conventions (upper first char class names!!!)
                                                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                                    // Replace whatever is in the fragment_container view with this fragment,
                                                    // and add the transaction to the back stack
                                                    transaction.replace(R.id.child_fragment, newFragment);
                                                    transaction.addToBackStack(null);
                                                    // Commit the transaction
                                                    transaction.commit();*/
                                                    Intent i = new Intent(getContext(), Transaction.class);
                                                    i.putExtra("test",1);
                                                    startActivity(i);
                                                }
                                            }

                                            @Override
                                            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                                super.onFailure(statusCode, headers, responseString, throwable);
                                                Log.d("onFailure", Integer.toString(statusCode));
                                            }
                                        });

                                    }else if(Type.equals("Users"))
                                    {
                                        Toast.makeText(getContext(), "Users", Toast.LENGTH_LONG).show();
                                        RequestParams params = new RequestParams();
                                        params.put("id",id );
                                        AsyncHttpClient http = new AsyncHttpClient();
                                        http.post("https://www.harmonicmix.xyz/api/GetUserTranfer_api", params, new JsonHttpResponseHandler(){
                                            @Override
                                            public void onSuccess(int statusCode, Header[] headers, JSONObject response ) {
                                                JSONObject obj = null;
                                                try {
                                                    obj = new JSONObject(response.toString());


                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                                String status = null;
                                                String id     = null;
                                                String Name   = null;
                                                String IdUser   = null;
                                                try {
                                                    status = (String) obj.get("status");
                                                    id = (String) obj.get("Id");
                                                    Name = (String) obj.get("Name");
                                                    IdUser = (String) obj.get("Id_Users");
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                                if(status.equals("Success"))
                                                {
                                                    Intent i = new Intent(getContext(), TranferQR.class);
                                                    i.putExtra("id",id);
                                                    i.putExtra("Name",Name);
                                                    i.putExtra("IdUser",IdUser);
                                                    startActivity(i);
                                                }
                                                else
                                                {
                                                    Toast.makeText(getContext(), "ไม่พบบัญชี", Toast.LENGTH_LONG).show();
                                                }
                                            }

                                            @Override
                                            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                                super.onFailure(statusCode, headers, responseString, throwable);
                                                Log.d("onFailure", Integer.toString(statusCode));
                                            }
                                        });
                                    }else
                                    {
                                        Toast.makeText(getContext(), Type, Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                        });

            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}