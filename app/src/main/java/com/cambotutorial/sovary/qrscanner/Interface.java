package com.cambotutorial.sovary.qrscanner;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

class ResponseModel {
    // Define the fields based on the response data from your server
    // For example, if the server returns "name" and "description" for a barcode, you can define them here.
    private String name;
    private String description;

    // Create getters and setters for the fields
    // Getters and setters for name and description
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

public interface Interface {
    @GET("api/barcode_info")
    Call<ResponseModel> getBarcodeInfo(@Query("barcode") String barcode);
}