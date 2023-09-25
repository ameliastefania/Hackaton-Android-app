package com.cambotutorial.sovary.qrscanner;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.milo.client.MiloClientComponent;
import org.apache.camel.component.milo.client.MiloClientEndpoint;

public class MainActivity extends AppCompatActivity {
    Button btn_scan;
    private CamelContext camelContext;
    private MiloClientEndpoint miloEndpoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_scan = findViewById(R.id.btn_scan);
        btn_scan.setOnClickListener(v -> {
            scanCode();
        });

        // Initialize the Camel context using a custom thread
        CustomCamelThread customCamelThread = new CustomCamelThread();
        customCamelThread.start();
    }

    private class CustomCamelThread extends Thread {
        @Override
        public void run() {
            // Create a Camel context within the custom thread
//            camelContext = new CustomCamelContext();

            try {
                // Create an instance of MiloClientComponent
                MiloClientComponent miloComponent = new MiloClientComponent();

                // Add MiloClientComponent to the Camel context
                camelContext.addComponent("opc", miloComponent);

                // Create a MiloClientEndpoint directly
                miloEndpoint = camelContext.getEndpoint("opc:tcp://10.20.30.200:11210/OPCUA/HiVisionUaServer", MiloClientEndpoint.class);

                // Start the Camel context within the custom thread
                camelContext.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Stop the Camel context when the activity is destroyed
        if (camelContext != null) {
            try {
                camelContext.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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

            // Trigger the OPC UA communication route in Camel
            try {
                ProducerTemplate producerTemplate = camelContext.createProducerTemplate();
                producerTemplate.sendBody(miloEndpoint, scannedData);
            } catch (Exception e) {
                e.printStackTrace();
                // Handle exceptions, if any
            }

            // Display a message to the user (you can replace this with your UI logic)
            Toast.makeText(MainActivity.this, "Scanned Data: " + scannedData, Toast.LENGTH_SHORT).show();
        }
    });
}
