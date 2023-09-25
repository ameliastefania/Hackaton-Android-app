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

        Device value = (Device) getIntent().getSerializableExtra("device");

        EditText etMac =  (EditText) findViewById(R.id.etMac);
        EditText etIP =  (EditText) findViewById(R.id.etIpAddr);
        EditText etFirm =  (EditText) findViewById(R.id.etFirm);
        EditText etDevName =  (EditText) findViewById(R.id.etDeviceName);
        etMac.setText(value.getMAC());
        etIP.setText(value.getIP_addr().toString());
        etFirm.setText(value.getFirmware_version().toString());
        etDevName.setText(value.getName().toString());
    }
}