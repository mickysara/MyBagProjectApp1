package com.example.httprequest;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.Window;
import android.widget.Toast;

import com.hanks.passcodeview.PasscodeView;

public class Passcode extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passcode);




        SharedPreferences sp = getSharedPreferences("USER", Context.MODE_PRIVATE);


        String PIN = sp.getString("PIN", "");

        if (PIN.equals("")) {
            PasscodeView passcodeView = (PasscodeView) findViewById(R.id.passcodeView);

            passcodeView.setPasscodeLength(4).setListener(new PasscodeView.PasscodeViewListener() {



                @Override
                public void onFail() {

                }

                @Override
                public void onSuccess(String number) {
                    Toast.makeText(getApplication(),"finish",Toast.LENGTH_SHORT).show();
                    SharedPreferences sp = getSharedPreferences("USER", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("PIN", number);
                    editor.commit();
                    Intent intent = new Intent(getApplicationContext(), Transaction.class);
                    startActivity(intent);
                }
            });
        } else {


            PasscodeView passcodeView = (PasscodeView) findViewById(R.id.passcodeView);

            passcodeView.setPasscodeLength(4).setLocalPasscode(PIN).setWrongInputTip("รหัส PIN มือถือไม่ถูกต้อง").setListener(new PasscodeView.PasscodeViewListener() {



                @Override
                public void onFail() {
                    Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    // Vibrate for 500 milliseconds
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        //deprecated in API 26
                        v.vibrate(500);
                    }
                }

                @Override
                public void onSuccess(String number) {
                    Toast.makeText(getApplication(),"PIN ของมือถือถูกต้อง",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), Transaction.class);
                    startActivity(intent);
                }
            });
        }

    }
}
