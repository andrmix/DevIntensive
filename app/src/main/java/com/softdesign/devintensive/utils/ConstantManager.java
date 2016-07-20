package com.softdesign.devintensive.utils;

public interface ConstantManager {
    String TAG_PREFIX = "DEV ";
    String EDIT_MODE_KEY = "EDIT_MODE_KEY";

    String USER_PHONE_KEY = "USER_1_KEY";
    String USER_MAIL_KEY = "USER_2_KEY";
    String USER_VK_KEY = "USER_3_KEY";
    String USER_GIT_KEY = "USER_4_KEY";
    String USER_BIO_KEY = "USER_5_KEY";
    String USER_PHOTO_KEY = "USER_PHOTO_KEY";
    String USER_AVATAR_KEY = "USER_AVATAR_KEY";

    String USER_FIO_KEY = "USER_FIO_KEY";
    String AUTH_TOKEN_KEY = "AUTH_TOKEN_KEY";
    String USER_ID_KEY = "USER_ID_KEY";

    String USER_SECOND_NAME = "USER_SECOND_NAME";
    String USER_FIRST_NAME = "USER_FIRST_NAME";

    String USER_RATING_VALUE = "USER_RATING_VALUE";
    String USER_CODE_LINES_VALUE = "USER_CODE_LINES_VALUE";
    String USER_PROJECT_VALUE = "USER_PROJECT_VALUE";
    String PARCELABLE_KEY = "PARCELABLE_KEY";
    String INTENT_MAIN_KEY = "INTENT_MAIN_KEY";

    int LOAD_PROFILE_PHOTO = 1;
    int REQUEST_CAMERA_PICTURE = 99;
    int REQUEST_GALLERY_PICTURE = 88;
    int PERMISSION_REQUEST_SETTINGS_CODE = 101;

    int CAMERA_REQUEST_PERMISSION_CODE = 102;
    //validate flags
    int ITS_OK = 0;
    int PHONE_FAIL = 1;
    int MAIL_FAIL = 2;
    int VK_FAIL = 3;
    int GIT_FAIL = 4;
    int SEARCH_DELAY = 3000;
    int NO_SEARCH_DELAY = 0;

}
