package com.example.httprequest.ui.tools;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.httprequest.R;

public class ToolActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.contentMainFragment, new ToolsFragment()).commit();
        }

    }
}