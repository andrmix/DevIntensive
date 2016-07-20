package com.softdesign.devintensive.ui.activities;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.storage.models.ProfileDTO;
import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.utils.ImageHelper;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = ConstantManager.TAG_PREFIX + "Main Activity";

    private DataManager mDataManager;
    private int mCurrentEditMode = 0;

    //инициализация View через ButterKnife
    @BindView(R.id.call_img)
    ImageView mCallImg;
    @BindView(R.id.send_mail_img)
    ImageView mSendMailImg;
    @BindView(R.id.goto_vk_img)
    ImageView mGotoVkImg;
    @BindView(R.id.goto_git_img)
    ImageView mGotoGitImg;

    @BindView(R.id.main_coordinator_container)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.navigation_drawer)
    DrawerLayout mNavigationDrawer;
    @BindView(R.id.fab)
    FloatingActionButton mFab;

    @BindView(R.id.profile_placeholder)
    RelativeLayout mProfilePlaceholder;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbar;
    @BindView(R.id.appbar_layout)
    AppBarLayout mAppBarLayout;

    @BindView(R.id.user_photo_img)
    ImageView mProfileImage;
    @BindView(R.id.user_phone_et)
    EditText mUserPhone;
    @BindView(R.id.user_email_et)
    EditText mUserMail;
    @BindView(R.id.user_vk_et)
    EditText mUserVk;
    @BindView(R.id.user_git_et)
    EditText mUserGit;
    @BindView(R.id.user_bio_et)
    EditText mUserBio;

    @BindView(R.id.navigation_view)
    NavigationView navigationView;

    @BindView(R.id.user_rating_txt)
    TextView mUserValueRating;
    @BindView(R.id.user_lines_txt)
    TextView mUserValueCodeLines;
    @BindView(R.id.user_project_txt)
    TextView mUserValueProjects;

    private TextView mUserName, mUserEmail;

    List<TextView> mUserValueViews;

    private AppBarLayout.LayoutParams mAppBarParams = null;
    private ImageView mAvatar;
    List<EditText> mUserInfoViews;


    File mPhotoFile = null;
    private int validatedFieldsFlag = -1;

    private Uri mSelectedImage = null;

    private MenuItem mUserProfileItem;

    /**
     * Обработка события при создании или перезапуске активности
     *
     * @param savedInstanceState сохраненные пользовательские данные
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //присвоение разметки активности
        setContentView(R.layout.activity_main);

        //логирование
        Log.d(TAG, "onCreate");

        //инициализация View через ButterKnife
        ButterKnife.bind(this);

        //получение Инстанса DataManager'а
        mDataManager = DataManager.getInstance();

        //заполнение списка EditText'ов профиля
        mUserInfoViews = new ArrayList<>();
        mUserInfoViews.add(mUserPhone);
        mUserInfoViews.add(mUserMail);
        mUserInfoViews.add(mUserVk);
        mUserInfoViews.add(mUserGit);
        mUserInfoViews.add(mUserBio);

        //заполнение списка TextView профиля
        mUserValueViews = new ArrayList<>();
        mUserValueViews.add(mUserValueRating);
        mUserValueViews.add(mUserValueCodeLines);
        mUserValueViews.add(mUserValueProjects);

        //назначение события нажатия
        mFab.setOnClickListener(this);
        mProfilePlaceholder.setOnClickListener(this);
        mCallImg.setOnClickListener(this);
        mSendMailImg.setOnClickListener(this);
        mGotoVkImg.setOnClickListener(this);
        mGotoGitImg.setOnClickListener(this);

        //настройка Toolbar'а
        setupToolbar();

        ProfileDTO user = getIntent().getParcelableExtra(ConstantManager.INTENT_MAIN_KEY);

        //настройка Drower'а
        setupDrawer(user);

        //загрузка пользовательских данных
        initUserFields(user);

        //загрузка фото профиля
        Picasso.with(this).load(user.getPhoto())
                .placeholder(R.drawable.pens_small)
                .into(mProfileImage);

        if (savedInstanceState == null) {
            //первый запуск
        } else {
            //загрузка сохраненных данных о состоянии редактирования
            mCurrentEditMode = savedInstanceState.getInt(ConstantManager.EDIT_MODE_KEY, 0);
            changeEditMode(mCurrentEditMode);
        }
    }

    /**
     * Обработка нажатия "сэндвича"
     *
     * @param item выбранный пункт меню
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            mNavigationDrawer.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Обработка события при старте/восстановлении активности
     */
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }


    /**
     * Обработка события при отображении активности на экране
     */
    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    /**
     * Обработка события при сворачивании активности
     */
    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    /**
     * Обработка события при остановке активности
     */
    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    /**
     * Обработка события при закрытии активности
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    /**
     * Обработка события при перезапуске активности
     */
    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart");
    }

    /**
     * Обработка события при нажатии на View-элемент
     *
     * @param v обрабатываемый View-элемент
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //нажата кнопка FloatingActionButton
            case R.id.fab:
                if (mCurrentEditMode == 0) {
                    //режим просмотра
                    changeEditMode(1);
                    mCurrentEditMode = 1;
                } else {
                    //режим редактирования
                    //проверка на правильность заполнения полей
                    validatedFieldsFlag = validatedFields();
                    //проверка пройдена
                    if (validatedFieldsFlag == ConstantManager.ITS_OK) {
                        changeEditMode(0);
                        mCurrentEditMode = 0;
                        //сохранение введенных данных
                        saveUserFields();
                    } else {
                        //проверка не пройдена - сообщение об ошибке
                        showErrorSnackbar(validatedFieldsFlag);
                    }
                }
                break;
            //нажато изображение выбора фото профиля
            case R.id.profile_placeholder:
                showDialog(ConstantManager.LOAD_PROFILE_PHOTO);
                break;
            //нажато изображение звонка
            case R.id.call_img:
                doCall();
                break;
            //нажато изображение отправки почты
            case R.id.send_mail_img:
                doSendMail();
                break;
            //нажато изображение просмотра профиля VK
            case R.id.goto_vk_img:
                doOpenVk();
                break;
            //нажато изображение просмотра профиля Github
            case R.id.goto_git_img:
                doOpenGit();
                break;
        }
    }

    /**
     * Вывод сообщения об ошибке при заполнении полей
     *
     * @param validatedFieldsFlag флаг проблемного поля
     */
    private void showErrorSnackbar(int validatedFieldsFlag) {
        switch (validatedFieldsFlag) {
            //неверно введен номер телефона
            case ConstantManager.PHONE_FAIL:
                showSnackbar(getString(R.string.error_phone_length));
                break;
            //неверно введен адрес e-mail
            case ConstantManager.MAIL_FAIL:
                showSnackbar(getString(R.string.error_mail_format));
                break;
            //неверно введена ссылка на профиль VK
            case ConstantManager.VK_FAIL:
                showSnackbar(getString(R.string.error_vk_format));
                break;
            //неверно введена ссылка на профиль Github
            case ConstantManager.GIT_FAIL:
                showSnackbar(getString(R.string.error_git_format));
                break;
        }
    }

    /**
     * Проверка на правильность заполнения полей
     *
     * @return флаг ITS_OK (0) - если поля заполнены верно, valFlag > 0 если обнаружена ошибка заполнения
     */
    private int validatedFields() {
        int valFlag = ConstantManager.ITS_OK;

        //проверка поля "номер телефона"
        if (validatePhoneFail()) {
            valFlag = ConstantManager.PHONE_FAIL;
        }
        //проверка поля "адрес e-mail"
        if (validateMailFail()) {
            valFlag = ConstantManager.MAIL_FAIL;
        }
        //проверка поля "профиль VK"
        if (validateVkFail()) {
            valFlag = ConstantManager.VK_FAIL;
        }
        //проверка поля "профиль Github"
        if (validateGitFail()) {
            valFlag = ConstantManager.GIT_FAIL;
        }
        return valFlag;
    }


    /**
     * Обработка события при сохранении пользовательских данных
     *
     * @param outState
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //сохранение текущего режима работы с профилем
        outState.putInt(ConstantManager.EDIT_MODE_KEY, mCurrentEditMode);
    }

    /**
     * Вывод на экран Snackbar
     *
     * @param message сообщение в Snackbar
     */
    private void showSnackbar(String message) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    /**
     * Настройка Toolbar'а
     */
    private void setupToolbar() {
        //замена ActionBar'а созданным Toolbar'ом
        setSupportActionBar(mToolbar);
        //расстановка элементов (айтемов) в Toolbar'е
        ActionBar actionBar = getSupportActionBar();
        mAppBarParams = (AppBarLayout.LayoutParams) mCollapsingToolbar.getLayoutParams();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * Настройка Drower'а
     */
    private void setupDrawer(ProfileDTO user) {
        //при нажатии на элемент списка в Drower'е выводится Snackbar с текстом элемента
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.team_menu:
                        doGotoUserListActivity();
                    case R.id.exit_menu:
                        System.exit(0);
                }

                //закрытие Drower'а
                mNavigationDrawer.closeDrawer(GravityCompat.START);
                return false;
            }
        });

        mUserProfileItem = navigationView.getMenu().findItem(R.id.user_profile_menu);
        mUserProfileItem.setChecked(true);

        //скругление аватара в Drower'е
        mAvatar = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.avatar);

        Picasso.with(this).load(user.getAvatar())
                .transform(new ImageHelper())
                .into(mAvatar);

        mUserName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.user_name_txt);
        mUserEmail = (TextView) navigationView.getHeaderView(0).findViewById(R.id.user_email_txt);
        mUserName.setText(user.getFullName());
        mUserEmail.setText(user.getEmail());
    }

    private void doGotoUserListActivity() {
        Intent intent = new Intent(MainActivity.this, UserListActivity.class);
        startActivity(intent);
    }

    /**
     * Получение результата из другой Activity (фото из камеры или галереи)
     *
     * @param requestCode код запроса, по которому идет определение к какому интенту было обращение
     * @param resultCode  код результата обращения
     * @param data        переданные данные
     */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            //запрос фото с галлереи
            case ConstantManager.REQUEST_GALLERY_PICTURE:
                if (resultCode == RESULT_OK && data != null) {
                    mSelectedImage = data.getData();
                    insertProfileImage(mSelectedImage);
                }
                break;
            //запрос фото с камеры
            case ConstantManager.REQUEST_CAMERA_PICTURE:
                if (resultCode == RESULT_OK && mPhotoFile != null) {
                    mSelectedImage = Uri.fromFile(mPhotoFile);
                    insertProfileImage(mSelectedImage);
                }
                break;
        }
    }


    /**
     * Смена режима работы с профилем
     *
     * @param mode флаг режима
     */
    private void changeEditMode(int mode) {
        //включить режим редактирования
        if (mode == 1) {
            mFab.setImageResource(R.drawable.ic_done_black_24dp);
            //для каждого EditText'а применить:
            for (EditText editText : mUserInfoViews) {
                //включить EditText
                editText.setEnabled(true);
                //включить возможность принятия фокуса
                editText.setFocusable(true);
                editText.setFocusableInTouchMode(true);
            }
            //вывод изображения выбора фото профиля
            showProfilePlaceholder();
            //заблокировать Toolbar
            lockToolbar();
            mCollapsingToolbar.setExpandedTitleColor(Color.TRANSPARENT);
            //перевести фокус в поле номера телефона
            mUserPhone.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(0, 0);
            //выключить режим редактирования
        } else {
            mFab.setImageResource(R.drawable.ic_edit_black_24dp);
            //для каждого EditText'а применить:
            for (EditText editText : mUserInfoViews) {
                //выключить EditText
                editText.setEnabled(false);
                //выключить возможность принятия фокуса
                editText.setFocusable(false);
                editText.setFocusableInTouchMode(false);
            }
            //убрать изображение выбора фото профиля
            hideProfilePlaceholder();
            //разблокировать Toolbar
            unlockToolbar();
            //сохранить введенные данные
            mCollapsingToolbar.setExpandedTitleColor(getResources().getColor(R.color.color_white));
        }
    }


    /**
     * Загрузка пользовательских данных
     */
    private void initUserFields(ProfileDTO user) {
        mUserPhone.setText(user.getPhone());
        mUserMail.setText(user.getEmail());
        mUserVk.setText(user.getVk());
        mUserGit.setText(user.getRepo());
        mUserBio.setText(user.getBio());

        mUserValueRating.setText(user.getRating());
        mUserValueCodeLines.setText(user.getCodeLines());
        mUserValueProjects.setText(user.getProjects());
    }

    /**
     * Сохранение пользовательских данных
     */
    private void saveUserFields() {
        //сбор данных из полей
        List<String> userData = new ArrayList<>();
        for (EditText userField : mUserInfoViews) {
            userData.add(userField.getText().toString());
        }
        //Сохранение пользовательских данных в PreferencesManager
        mDataManager.getPreferencesManager().saveUserProfileData(userData);
    }

    private void initUserInfoValue() {
        List<String> userData = mDataManager.getPreferencesManager().loadUserProfileValues();
        for (int i = 0; i < userData.size(); i++) {
            mUserValueViews.get(i).setText(userData.get(i));
        }
    }

    /**
     * Обработка события нажатия системной кнопки Back телефона
     */
    @Override
    public void onBackPressed() {
        //если открыт Drawer, то закрыть его
        if (mNavigationDrawer.isDrawerOpen(GravityCompat.START)) {
            mNavigationDrawer.closeDrawer(GravityCompat.START);
            //иначе системная обработка нажатия
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Загрузка фото из галлереи
     */
    private void loadPhotoFromGallery() {
        //создание интента
        Intent takeGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //указание типа получаемых данных
        takeGalleryIntent.setType("image/*");
        //запуск интента
        startActivityForResult(Intent.createChooser(takeGalleryIntent, getString(R.string.user_profile_choose_message)), ConstantManager.REQUEST_GALLERY_PICTURE);
    }

    /**
     * Загрузка фото из камеры
     */
    private void loadPhotoFromCamera() {
        //проверка присутствия необходимых прав
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

            //если права есть - создание интента
            Intent takeCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            //создание пустого файла
            try {
                mPhotoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //получение снимка и запись в файл
            if (mPhotoFile != null) {
                takeCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mPhotoFile));
                startActivityForResult(takeCaptureIntent, ConstantManager.REQUEST_CAMERA_PICTURE);
            }
        } else {
            //если прав нет - запрос прав
            ActivityCompat.requestPermissions(this, new String[]{
                    android.Manifest.permission.CAMERA,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, ConstantManager.CAMERA_REQUEST_PERMISSION_CODE);
        }

        //вывод Snackbar для перехода в настройки приложения
        Snackbar.make(mCoordinatorLayout, R.string.msg_permissions, Snackbar.LENGTH_LONG)
                .setAction(R.string.txt_allow, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openApplicationSettings();
                    }
                }).show();
    }


    /**
     * Обработка события получения результата запроса прав
     *
     * @param requestCode  код запроса
     * @param permissions  массив разрешений
     * @param grantResults массив результатов
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == ConstantManager.CAMERA_REQUEST_PERMISSION_CODE && grantResults.length == 2) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //TODO: обработка разрешения (разрешение получено)
            }
            if (grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                //TODO: обработка разрешения (разрешение получено)
            }
        }
    }

    /**
     * Скрытие изображения выбора фото профиля
     */
    private void hideProfilePlaceholder() {
        mProfilePlaceholder.setVisibility(View.GONE);
    }

    /**
     * Вывод изображения выбора фото профиля
     */
    private void showProfilePlaceholder() {
        mProfilePlaceholder.setVisibility(View.VISIBLE);
    }

    /**
     * Блокировка Toolbar'а
     */
    private void lockToolbar() {
        mAppBarLayout.setExpanded(true, true);
        mAppBarParams.setScrollFlags(0);
        mCollapsingToolbar.setLayoutParams(mAppBarParams);
    }

    /**
     * Разблокировка Toolbar'а
     */
    private void unlockToolbar() {
        mAppBarParams.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED);
        mCollapsingToolbar.setLayoutParams(mAppBarParams);
    }

    /**
     * Обработка события создания диалогового окна
     *
     * @param id параметр, указывающий какое диалоговое окно создать
     * @return
     */
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            //создание диалогового окна выбора источника изображения для фото профиля
            case ConstantManager.LOAD_PROFILE_PHOTO:
                //заполнение массива элементов окна
                String[] selectItems = {getString(R.string.user_profile_dialog_gallery),
                        getString(R.string.user_profile_dialog_camera),
                        getString(R.string.user_profile_dialog_cancel)};
                //создание окна
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                //применение заголовка
                builder.setTitle(getString(R.string.user_profile_dialog_title));
                //обработчик нажатия по элементу
                builder.setItems(selectItems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int choiceItem) {
                        switch (choiceItem) {
                            case 0:
                                //загрузка фото из галлереи
                                loadPhotoFromGallery();
                                break;
                            case 1:
                                //загрузка фото из камеры
                                loadPhotoFromCamera();
                                break;
                            case 2:
                                //отмена
                                dialog.cancel();
                                break;
                        }
                    }
                });

                return builder.create();
            default:
                return null;
        }
    }

    /**
     * Создание пустого файла для снимка из камеры
     *
     * @return
     * @throws IOException
     */
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.MediaColumns.DATA, image.getAbsolutePath());

        this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        return image;
    }

    /**
     * Вставка нового изображения в фото профиля / отправка на сервер
     *
     * @param selectedImage uri нового изображения
     */
    private void insertProfileImage(Uri selectedImage) {
        Picasso.with(this)
                .load(selectedImage)
                .into(mProfileImage);

        Uri photoProfileOnServer = mDataManager.getPreferencesManager().loadUserPhoto();
        String uriServ = photoProfileOnServer.toString();
        String uriLocal = selectedImage.toString();

        if (uriServ != uriLocal) {
            mDataManager.getFileUploader().uploadFile(mDataManager.getPreferencesManager().getUserId(), selectedImage);
        }
    }

    /**
     * Открытие окна настроек приложения
     */
    public void openApplicationSettings() {
        Intent appSettingsIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse(getString(R.string.txt_package) + getPackageName()));
        startActivityForResult(appSettingsIntent, ConstantManager.PERMISSION_REQUEST_SETTINGS_CODE);
    }

    /**
     * Обработка вызова звонка
     */
    void doCall() {
        Intent openlinkIntentPhone = new Intent(Intent.ACTION_DIAL, Uri.parse(getString(R.string.txt_tel) + mUserPhone.getText().toString()));
        startActivity(openlinkIntentPhone);
    }

    /**
     * Обработка отправки письма
     */
    void doSendMail() {
        Intent openlinkIntentMail = new Intent(Intent.ACTION_SENDTO, Uri.parse(getString(R.string.txt_mailto) + mUserMail.getText().toString()));
        startActivity(openlinkIntentMail);
    }

    /**
     * Обработка открытия ссылки на профиль VK
     */
    void doOpenVk() {
        Intent openlinkIntentVk = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.txt_https) + mUserVk.getText().toString()));
        startActivity(openlinkIntentVk);
    }

    /**
     * Обработка открытия ссылки на профиль Github
     */
    void doOpenGit() {
        Intent openlinkIntentGit = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.txt_https) + mUserGit.getText().toString()));
        startActivity(openlinkIntentGit);
    }

    /**
     * Проверка на правильность заполнения поля "Телефон"
     *
     * @return false - проверка пройдена, true - проверка не пройдена
     */
    private boolean validatePhoneFail() {
        boolean result = false;
        //номер телефона должен содержать количество символов в диапазоне от 11 до 20
        if (mUserPhone.getText().toString().length() > 20 || mUserPhone.getText().toString().length() < 11) {
            result = true;
        }
        return result;
    }

    /**
     * Проверка на правильность заполнения поля "E-Mail"
     *
     * @return false - проверка пройдена, true - проверка не пройдена
     */
    private boolean validateMailFail() {
        boolean result = false;
        String email = mUserMail.getText().toString();
        //номер символа "@"
        int placeDog = 0;
        //количество символов перед точкой
        int countBefDot = 0;
        //количество символов после точки
        int countAfterDot = 0;
        //номер последней точки
        int placeLastDot = 0;

        //поиск символа "@"
        for (int i = 0; i < email.length(); i++) {
            if (email.charAt(i) == '@') {
                placeDog = i;
            }
        }

        //до символа "@" должно быть как минимум 3 символа
        if (placeDog < 3) {
            return true;
        }

        placeDog++;
        //поиск последней точки
        for (int i = placeDog; i < email.length(); i++) {
            if (email.charAt(i) == '.') {
                placeLastDot = i;
            }
        }

        //вычисление количества символов перед точкой
        countBefDot = placeLastDot - placeDog;
        //количество символов перед точкой должно быть как минимум 2
        if (countBefDot < 2) {
            return true;
        }

        placeLastDot++;
        //вычисление количества символов после точки
        countAfterDot = email.length() - placeLastDot;
        //количество символов после точки должно быть как минимум 2
        if (countAfterDot < 2) {
            return true;
        }

        return result;
    }

    /**
     * Проверка на правильность заполнения поля "Профиль VK"
     *
     * @return false - проверка пройдена, true - проверка не пройдена
     */
    private boolean validateVkFail() {
        boolean result = false;
        String vkLink = mUserVk.getText().toString();

        //поиск строки vk.com в тексте поля
        if (vkLink.contains(getString(R.string.vk_address))) {
            //перед vk.com не должно быть символов
            if (vkLink.indexOf(getString(R.string.vk_address)) == 0) {
                result = false;
            } else {
                return true;
            }
        } else {
            return true;
        }

        return result;
    }

    /**
     * Проверка на правильность заполнения поля "Профиль Github"
     *
     * @return false - проверка пройдена, true - проверка не пройдена
     */
    private boolean validateGitFail() {
        boolean result = false;
        String gitLink = mUserGit.getText().toString();

        //поиск строки github.com в тексте поля
        if (gitLink.contains(getString(R.string.git_address))) {
            //перед github.com не должно быть символов
            if (gitLink.indexOf(getString(R.string.git_address)) == 0) {
                result = false;
            } else {
                return true;
            }
        } else {
            return true;
        }

        return result;
    }
}
