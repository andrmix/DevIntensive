package com.softdesign.devintensive.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.facebook.stetho.Stetho;
import com.softdesign.devintensive.data.storage.models.DaoMaster;
import com.softdesign.devintensive.data.storage.models.DaoSession;

public class DevintensiveApplication extends Application {
    public static SharedPreferences sSharedPreferences;
    private static Context sContext;
    private static DaoSession sDaoSession;

    /**
     * Обработка события при создании приложения
     */
    @Override
    public void onCreate() {
        super.onCreate();

        sContext = getApplicationContext();
        //получение SharedPreferences по умолчанию
        sSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "devintensive-db");
        org.greenrobot.greendao.database.Database db = helper.getWritableDb();
        sDaoSession = new DaoMaster(db).newSession();

        Stetho.initializeWithDefaults(this);
    }

    public static DaoSession getDaoSession() {
        return sDaoSession;
    }

    /**
     * Возвращает sSharedPreferences
     *
     * @return sSharedPreferences
     */
    public static SharedPreferences getSharedPreferences() {
        return sSharedPreferences;
    }

    public static Context getContext() {
        return sContext;
    }
}
