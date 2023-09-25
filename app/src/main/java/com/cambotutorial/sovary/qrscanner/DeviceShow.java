package com.cambotutorial.sovary.qrscanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

public class DeviceShow extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_show);

        Intent intent = getIntent();
        String value = intent.getStringExtra("MAC");
        Toast.makeText(this, "My value: " + value,Toast.LENGTH_LONG).show();

        EditText etMac =  (EditText) findViewById(R.id.etMac);
        etMac.setText(value);
    }
}