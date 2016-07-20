package com.softdesign.devintensive.utils.operations;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.redmadrobot.chronos.ChronosOperation;
import com.redmadrobot.chronos.ChronosOperationResult;
import com.softdesign.devintensive.data.network.RestService;
import com.softdesign.devintensive.data.network.ServiceGenerator;
import com.softdesign.devintensive.data.network.req.UserLoginReq;
import com.softdesign.devintensive.data.network.res.UserModelRes;

import retrofit2.Call;

public class CheckLogin extends ChronosOperation<Call<UserModelRes>> {

    private final UserLoginReq mUserLoginReq;
    private final RestService mRestService;

    public CheckLogin(@NonNull final UserLoginReq userLoginReq) {
        mUserLoginReq = userLoginReq;
        mRestService = ServiceGenerator.createService(RestService.class);
    }

    @Nullable
    @Override
    public Call<UserModelRes> run() {
        Log.e("run", "CheckLogin");
        return mRestService.loginUser(mUserLoginReq);
    }

    @NonNull
    @Override
    public Class<? extends ChronosOperationResult<Call<UserModelRes>>> getResultClass() {
        return Result.class;
    }

    public final static class Result extends ChronosOperationResult<Call<UserModelRes>> {

    }
}
