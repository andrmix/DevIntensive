package com.softdesign.devintensive.utils.operations;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.redmadrobot.chronos.ChronosOperation;
import com.redmadrobot.chronos.ChronosOperationResult;
import com.softdesign.devintensive.data.storage.models.DaoSession;
import com.softdesign.devintensive.data.storage.models.Profile;
import com.softdesign.devintensive.utils.DevintensiveApplication;

public class LoadMeFromDb extends ChronosOperation<Profile> {

    private DaoSession mDaoSession;
    private final String mId;

    public LoadMeFromDb(@NonNull final String id) {
        mId = id;
        mDaoSession = DevintensiveApplication.getDaoSession();
    }

    @Nullable
    @Override
    public Profile run() {
        Profile user;

        Log.e("run", "LoadUsersFromDb");
        user = mDaoSession.queryBuilder(Profile.class)
                .build()
                .unique();

        return user;
    }

    @NonNull
    @Override
    public Class<? extends ChronosOperationResult<Profile>> getResultClass() {
        return Result.class;
    }

    public final static class Result extends ChronosOperationResult<Profile> {

    }
}