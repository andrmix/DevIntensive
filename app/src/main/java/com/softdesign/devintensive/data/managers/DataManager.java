package com.softdesign.devintensive.data.managers;

import android.content.Context;

import com.softdesign.devintensive.data.network.FileUploadService;
import com.softdesign.devintensive.data.network.RestService;
import com.softdesign.devintensive.data.network.ServiceGenerator;
import com.softdesign.devintensive.data.network.req.UserLoginReq;
import com.softdesign.devintensive.data.network.res.UserListRes;
import com.softdesign.devintensive.data.network.res.UserModelRes;

import retrofit2.Call;

public class DataManager {
    private static DataManager INSTANCE = null;
    private Context mContext;
    private PreferencesManager mPreferencesManager;
    private RestService mRestService;
    private FileUploader mFileUploader;

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
        this.mFileUploader = new FileUploader();
        this.mRestService = ServiceGenerator.createService(RestService.class);
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

    //region ============= Network =============

    public Call<UserModelRes> loginUser(UserLoginReq userLoginReq) {
        return mRestService.loginUser(userLoginReq);
    }

    public Call<UserListRes> getUserList(){
        return mRestService.getUserList();
    }


    //endregion

    // region ============= DataBase =============


    //endregion
}
