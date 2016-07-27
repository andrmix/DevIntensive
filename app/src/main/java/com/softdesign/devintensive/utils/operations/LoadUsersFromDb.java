package com.softdesign.devintensive.utils.operations;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.redmadrobot.chronos.ChronosOperation;
import com.redmadrobot.chronos.ChronosOperationResult;
import com.softdesign.devintensive.data.storage.models.DaoSession;
import com.softdesign.devintensive.data.storage.models.User;
import com.softdesign.devintensive.data.storage.models.UserDao;
import com.softdesign.devintensive.utils.DevintensiveApplication;

import java.util.List;

public class LoadUsersFromDb extends ChronosOperation<List<User>> {

    private DaoSession mDaoSession;

    public LoadUsersFromDb() {
        this.mDaoSession = DevintensiveApplication.getDaoSession();
    }

    @Nullable
    @Override
    public List<User> run() {
        List<User> userList;

        Log.e("run", "LoadUsersFromDb");
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
