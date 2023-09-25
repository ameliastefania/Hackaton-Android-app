 package com.cambotutorial.sovary.qrscanner;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import com.cambotutorial.sovary.qrscanner.Interface;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.component.milo.client.MiloClientComponent;

/*
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
*/



 public class MainActivity extends AppCompatActivity {
    Button btn_scan;
    //private Interface apiInterface;
    private CamelContext camelContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

/*
        // Initialize Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.114:80/") // Replace with your server URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Create an instance of the API interface
        apiInterface = retrofit.create(Interface.class);
*/

        btn_scan = findViewById(R.id.btn_scan);
        btn_scan.setOnClickListener(v -> {
            scanCode();
        });

        // Initialize the Camel context
        camelContext = new DefaultCamelContext();

        try {
            // Add a Camel route for OPC UA communication
            // Create an instance of MiloClientComponent
            MiloClientComponent miloComponent = new MiloClientComponent();

            // Add MiloClientComponent to the Camel context
            camelContext.addComponent("opc", miloComponent);


            camelContext.addRoutes(new RouteBuilder() {
                @Override
                public void configure() {
                    from("direct:opcua") // Define an entry point for OPC UA communication
                            .to("opc:tcp://10.20.30.200:11210/OPCUA/HiVisionUaServer")
                            .to("log:opcua"); // Log received data
                }
            });

            // Start the Camel context
            camelContext.start();
        } catch (Exception e) {
            e.printStackTrace();
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

    private void scanCode()
    {
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

            // Make the API call to your backend to fetch information based on the scanned data
            //apiInterface.getBarcodeInfo(scannedData).enqueue(new Callback<ResponseModel>() {



/*
                @Override
                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                    if (response.isSuccessful()) {
                        // Handle the successful response from the server
                        ResponseModel responseData = response.body();

                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("Result");
                        builder.setMessage("Name: " + responseData.getName() + "\nDescription: " + responseData.getDescription());
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).show();
                    } else {
                        // Handle the case when the response from the server is not successful
                        // Show an error message or take appropriate action.
                        Toast.makeText(MainActivity.this, "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseModel> call, Throwable t) {
                    // Handle the failure of the API call
                    // Show an error message or take appropriate action.
                    Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
*/

            // Trigger the OPC UA communication route in Camel
            try {
                ProducerTemplate producerTemplate = camelContext.createProducerTemplate();
                producerTemplate.sendBody("direct:opcua", scannedData);
            } catch (Exception e) {
                e.printStackTrace();
                // Handle exceptions, if any
            }

            // Display a message to the user (you can replace this with your UI logic)
            // Toast.makeText(MainActivity.this, "Scanned Data: " + scannedData, Toast.LENGTH_SHORT).show();

            // Handle the successful response from the server
            //ResponseModel responseData = response.body();

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Result");
                    builder.setMessage("test scannedData " + scannedData);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            }).show();

/*
            } else {
                // Handle the case when the response from the server is not successful
                // Show an error message or take appropriate action.
                Toast.makeText(MainActivity.this, "Error: " + response.message(), Toast.LENGTH_SHORT).show();
            }
*/
        }
    });

}