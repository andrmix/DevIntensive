package com.softdesign.devintensive.utils.operations;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.redmadrobot.chronos.ChronosOperation;
import com.redmadrobot.chronos.ChronosOperationResult;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.storage.models.Profile;
import com.softdesign.devintensive.data.storage.models.ProfileDao;
import com.softdesign.devintensive.data.storage.models.Repository;
import com.softdesign.devintensive.data.storage.models.RepositoryDao;
import com.softdesign.devintensive.data.storage.models.User;
import com.softdesign.devintensive.data.storage.models.UserDao;

import java.util.List;

public class SaveUsersInDb extends ChronosOperation<String> {

    private final List<Repository> allRepositories;
    private final List<User> allUsers;
    private final List<Profile> myProfile;
    private final RepositoryDao mRepositoryDao;
    private final UserDao mUserDao;
    private final ProfileDao mProfileDao;


    public SaveUsersInDb(@NonNull final List<Repository> repositories, @NonNull final List<User> users, @NonNull final List<Profile> profile){
        allRepositories = repositories;
        allUsers = users;
        myProfile = profile;
        mUserDao = DataManager.getInstance().getDaoSession().getUserDao();
        mRepositoryDao = DataManager.getInstance().getDaoSession().getRepositoryDao();
        mProfileDao = DataManager.getInstance().getDaoSession().getProfileDao();
    }

    @Nullable
    @Override
    public String run() {
        Log.e("run", "SaveUsersInDb");
        mRepositoryDao.insertOrReplaceInTx(allRepositories);
        mUserDao.insertOrReplaceInTx(allUsers);
        mProfileDao.insertOrReplaceInTx(myProfile);
        return "OK";
    }

    @NonNull
    @Override
    public Class<? extends ChronosOperationResult<String>> getResultClass() {
        return Result.class;
    }

    public final static class Result extends ChronosOperationResult<String> {

    }
}
