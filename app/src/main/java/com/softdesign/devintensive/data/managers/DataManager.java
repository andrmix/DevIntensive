package com.softdesign.devintensive.data.managers;

import android.content.Context;

import com.softdesign.devintensive.data.network.PicassoCache;
import com.softdesign.devintensive.data.network.RestService;
import com.softdesign.devintensive.data.network.ServiceGenerator;
import com.softdesign.devintensive.data.storage.models.DaoSession;
import com.softdesign.devintensive.data.storage.models.User;
import com.softdesign.devintensive.data.storage.models.UserDao;
import com.softdesign.devintensive.utils.DevintensiveApplication;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DataManager {
    private static DataManager INSTANCE = null;
    private Picasso mPicasso;
    private Context mContext;
    private PreferencesManager mPreferencesManager;
    private RestService mRestService;
    private FileUploader mFileUploader;
    private DaoSession mDaoSession;

    /**
     * Возвращает инстанс PreferencesManager'а
     *
     * @return инстанс PreferencesManager'а
     */
    public PreferencesManager getPreferencesManager() {
        return mPreferencesManager;
    }

    public FileUploader getFileUploader() {
        return mFileUploader;
    }

    /**
     * Конструктор DataManager'а / создание инстанса PreferencesManager'а
     */
    public DataManager() {
        this.mPreferencesManager = new PreferencesManager();
        this.mContext = DevintensiveApplication.getContext();
        this.mFileUploader = new FileUploader();
        this.mRestService = ServiceGenerator.createService(RestService.class);
        this.mPicasso = new PicassoCache(mContext).getPicassoInstance();
        this.mDaoSession = DevintensiveApplication.getDaoSession();
    }

    /**
     * Возвращает инстанс DataManager'а
     *
     * @return инстанс DataManager'а
     */
    public static DataManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DataManager();
        }
        return INSTANCE;
    }

    public Context getContext() {
        return mContext;
    }

    public Picasso getPicasso() {
        return mPicasso;
    }

    //region ============= Network =============

    /*public Call<UserModelRes> loginUser(UserLoginReq userLoginReq) {
        return mRestService.loginUser(userLoginReq);
    }*/

    /*public Call<UserListRes> getUserListFromNetwork() {
        return mRestService.getUserList();
    }*/


    //endregion

    // region ============= DataBase =============


    public DaoSession getDaoSession() {
        return mDaoSession;
    }

    public List<User> getUserListByName(String query) {
        List<User> userList = new ArrayList<>();
        try {
            userList = getDaoSession().queryBuilder(User.class)
                    .where(UserDao.Properties.Rating.gt(0), UserDao.Properties.SearchName.like("%" + query.toUpperCase() + "%"))
                    .orderDesc(UserDao.Properties.CodeLines)
                    .build()
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return userList;
    }

    //endregion
}
