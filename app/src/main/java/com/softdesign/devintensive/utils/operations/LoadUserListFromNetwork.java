package com.softdesign.devintensive.utils.operations;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.redmadrobot.chronos.ChronosOperation;
import com.redmadrobot.chronos.ChronosOperationResult;
import com.softdesign.devintensive.data.network.RestService;
import com.softdesign.devintensive.data.network.ServiceGenerator;
import com.softdesign.devintensive.data.network.res.UserListRes;

import retrofit2.Call;

public class LoadUserListFromNetwork extends ChronosOperation<Call<UserListRes>> {

    private final RestService mRestService;

    public LoadUserListFromNetwork(){
        mRestService = ServiceGenerator.createService(RestService.class);
    }

    @Nullable
    @Override
    public Call<UserListRes> run() {
        Log.e("run", "LoadUserListFromNetwork");
        return mRestService.getUserList();
    }

    @NonNull
    @Override
    public Class<? extends ChronosOperationResult<Call<UserListRes>>> getResultClass() {
        return Result.class;
    }

    public final static class Result extends ChronosOperationResult<Call<UserListRes>> {

    }
}
