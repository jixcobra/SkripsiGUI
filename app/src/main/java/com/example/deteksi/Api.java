package com.example.deteksi;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by Belal on 10/5/2017.
 */

public interface Api {

    //the base URL for our API
    //make sure you are not using localhost
    //find the ip usinc ipconfig command
    String BASE_URL = "http://192.168.1.7:5000/";

    //this is our multipart request
    //we have two parameters on is name and other one is description
    @Multipart
    @POST("upload")
    Call<MyResponse> uploadImage(@Part("file\"; filename=\"test\" ") RequestBody file, @Part("desc") RequestBody desc);

}
