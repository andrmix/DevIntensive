package com.softdesign.devintensive.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.redmadrobot.chronos.ChronosOperation;
import com.redmadrobot.chronos.ChronosOperationResult;
import com.softdesign.devintensive.data.storage.models.DaoSession;
import com.softdesign.devintensive.data.storage.models.User;
import com.softdesign.devintensive.data.storage.models.UserDao;

import java.util.List;

public class LoadUsersFromDb extends ChronosOperation<List<User>> {

    private DaoSession mDaoSession;

//    public LoadUsersFromDb() {
//        this.mDaoSession = DevintensiveApplication.getDaoSession();
//    }

    @Nullable
    @Override
    public List<User> run() {
        List<User> userList;

        this.mDaoSession = DevintensiveApplication.getDaoSession();
        Log.e("ERR_FLAG", " mDaoSessionChronos");
        userList = mDaoSession.queryBuilder(User.class)
                .where(UserDao.Properties.CodeLines.gt(0))
                .orderDesc(UserDao.Properties.CodeLines)
                .build()
                .list();

        return userList;
    }

    @NonNull
    @Override
    public Class<? extends ChronosOperationResult<List<User>>> getResultClass() {
        return Result.class;
    }

    public final static class Result extends ChronosOperationResult<List<User>> {

    }
}
