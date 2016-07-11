package com.softdesign.devintensive.utils;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class DevintensiveApplication extends Application{
    public static SharedPreferences sSharedPreferences;

    /**
     * Обработка события при создании приложения
     */
    @Override
    public void onCreate() {
        super.onCreate();

        //получение SharedPreferences по умолчанию
        sSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    /**
     * Возвращает sSharedPreferences
     * @return sSharedPreferences
     */
    public static SharedPreferences getSharedPreferences() {
        return sSharedPreferences;
    }
}
