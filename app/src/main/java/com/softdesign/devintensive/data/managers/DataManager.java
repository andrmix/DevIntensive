package com.softdesign.devintensive.data.managers;

public class DataManager {
    private static DataManager INSTANCE = null;
    private PreferencesManager mPreferencesManager;

    /**
     * Возвращает инстанс PreferencesManager'а
     * @return инстанс PreferencesManager'а
     */
    public PreferencesManager getPreferencesManager() {
        return mPreferencesManager;
    }

    /**
     * Конструктор DataManager'а / создание инстанса PreferencesManager'а
     */
    public DataManager() {
        this.mPreferencesManager = new PreferencesManager();
    }

    /**
     * Возвращает инстанс DataManager'а
     * @return инстанс DataManager'а
     */
    public static DataManager getInstance(){
        if (INSTANCE == null){
            INSTANCE = new DataManager();
        }
        return INSTANCE;
    }
}
