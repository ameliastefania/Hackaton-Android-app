package com.cambotutorial.sovary.qrscanner;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.google.zxing.integration.android.IntentIntegrator;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import com.cambotutorial.sovary.qrscanner.Interface;

import java.io.File;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    Button btn_scan;
    Button btn_manual_input;
    private Interface apiInterface;
    HashMap<String,Device> deviceMap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        deviceMap = new HashMap<String,Device>();
        ImageView myImage = (ImageView) findViewById(R.id.imgView);
        myImage.setImageResource(R.drawable.logo);


        Device dev1 = new Device("EC74BA505D2C", "10.20.32.8", "HiOS-09.5.00-tst", "RSP35");
        Device dev2 = new Device("ECE555C845C4", "10.20.32.7", "HiOS-09.5.00-BETA32", "RSP30");

        deviceMap.put(dev1.getMAC(), dev1);
        deviceMap.put(dev2.getMAC(), dev2);

        btn_scan = findViewById(R.id.btn_scan);
        btn_manual_input = findViewById(R.id.btn_manual_input);
        btn_scan.setOnClickListener(v -> {
            scanCode();
        });
        btn_manual_input.setOnClickListener(v -> {
            showManualInputDialog();
        });
    }

    private void scanCode() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up to flash on");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLauncher.launch(options);
    }

    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result -> {
        if (result.getContents() != null) {
            // Get the scanned barcode or QR code content
            String scannedData = result.getContents();
            String foundMac = null;
            for (String MAC : deviceMap.keySet()) {
                if (scannedData.equals(MAC)) {
                    foundMac = MAC;
                }
            }

            if (foundMac == null) {
                Toast.makeText(this, "Device doesn't exist. Please, scan again", Toast.LENGTH_LONG).show();
            } else {
                Device foundDevice = deviceMap.get(foundMac);
                Intent myIntent = new Intent(this, DeviceShow.class);
                myIntent.putExtra("device", foundDevice);
                this.startActivity(myIntent);
            }
        }
    });
    private void showManualInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Manual Input");

        // Inflate the layout for the dialog
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_manual_input, null);
        builder.setView(view);

        EditText editText = view.findViewById(R.id.editTextManualInput);

        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String manuallyEnteredCode = editText.getText().toString();
                handleManualInput(manuallyEnteredCode);
                dialogInterface.dismiss();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.show();
    }
    private void handleManualInput(String code) {
        // Handle the manually entered code here
        Device foundDevice = null;
        if (code!=null) {
            for (String MAC : deviceMap.keySet()) {
                if (code != null) {
                    if (code.equals(MAC)) {
                        foundDevice = deviceMap.get(MAC);
                    }
                }
            }
        } else {
            Toast.makeText(this, "No input", Toast.LENGTH_LONG).show();
        }
        if (foundDevice != null) {
            Intent myIntent = new Intent(this, DeviceShow.class);
            myIntent.putExtra("device", foundDevice);
            this.startActivity(myIntent);
        } else {
            Toast.makeText(this, "The device is not recognized", Toast.LENGTH_LONG).show();
        }

    }

}
