package com.softdesign.devintensive.data.managers;

import android.app.Application;
import android.net.Uri;
import android.util.Log;

import com.softdesign.devintensive.data.network.FileUploadService;
import com.softdesign.devintensive.data.network.ServiceGenerator;
import com.softdesign.devintensive.utils.DevintensiveApplication;
import com.softdesign.devintensive.utils.FileUtils;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FileUploader {
    public void uploadFile(String userId, Uri fileUri) {
        FileUploadService service =
                ServiceGenerator.createService(FileUploadService.class);

        File file = new File(fileUri.getPath());

        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);

        MultipartBody.Part body =
                MultipartBody.Part.createFormData("picture", file.getName(), requestFile);

        Call<ResponseBody> call = service.upload(userId, body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,
                                   Response<ResponseBody> response) {
                if(response.code() == 200) {
                    Log.v("Upload", "UPLOAD SUCCESS");
                } else {
                    Log.v("Upload", "UPLOAD FAIL");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Upload error:", t.getMessage());
            }
        });
    }
}
