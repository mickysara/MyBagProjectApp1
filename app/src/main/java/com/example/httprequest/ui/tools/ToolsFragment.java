package com.example.httprequest.ui.tools;

<<<<<<< HEAD
import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
=======
>>>>>>> parent of 803b72f... add scan qr code
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.httprequest.R;
<<<<<<< HEAD
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static com.google.zxing.integration.android.IntentIntegrator.REQUEST_CODE;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;


public class ToolsFragment extends Fragment  implements ConnectionCallbacks, OnConnectionFailedListener {

    public static final int RequestPermissionCode = 1;
    protected GoogleApiClient googleApiClient;
    protected TextView longitudeText;
    protected TextView latitudeText;
    protected Location lastLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;

    private ZXingScannerView zXingScannerView;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
=======

public class ToolsFragment extends Fragment {
>>>>>>> parent of 803b72f... add scan qr code

    private ToolsViewModel toolsViewModel;

<<<<<<< HEAD

        googleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        Button button = getView().findViewById(R.id.btnQRscan);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
//                        == PackageManager.PERMISSION_DENIED)
//                {
//                    requestPermissions(new String[] {Manifest.permission.CAMERA}, REQUEST_CODE);
//                    return;
//                }

                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED )
                {
                    requestPermissions( new String[]{

                            Manifest.permission.CAMERA}, 225);
                    return;
                }


                zXingScannerView = new ZXingScannerView(getActivity());
                getActivity().setContentView(zXingScannerView);
                zXingScannerView.startCamera();

                zXingScannerView.setResultHandler(new ZXingScannerView.ResultHandler() {
                    @Override
                    public void handleResult(Result result) {

                        zXingScannerView.stopCamera();
                        getActivity().setContentView(R.layout.activity_tool);
                        String resultString = result.getText().toString();
                        Toast.makeText(getActivity(), "QR code = " + resultString,
                                Toast.LENGTH_LONG).show();
                        Log.d("12MarchV1", "QR code ==> " + resultString);
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.contentMainFragment, new ToolsFragment()).commit();
                    }
                });

=======
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        toolsViewModel =
                ViewModelProviders.of(this).get(ToolsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_tools, container, false);
        final TextView textView = root.findViewById(R.id.text_tools);
        toolsViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
>>>>>>> parent of 803b72f... add scan qr code
            }
        });
        return root;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}