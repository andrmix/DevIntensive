package com.softdesign.devintensive.data.network;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface FileUploadService {
    @Multipart
    @POST("user/{userId}/publicValues/profilePhoto")
    Call<ResponseBody> upload(@Part("userId") String userId, @Part MultipartBody.Part file);
}
