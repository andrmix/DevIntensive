package com.softdesign.devintensive.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.network.req.UserLoginReq;
import com.softdesign.devintensive.data.network.res.UserListRes;
import com.softdesign.devintensive.data.network.res.UserModelRes;
import com.softdesign.devintensive.data.storage.models.Profile;
import com.softdesign.devintensive.data.storage.models.Repository;
import com.softdesign.devintensive.data.storage.models.User;
import com.softdesign.devintensive.utils.NetworkStatusChecker;
import com.softdesign.devintensive.utils.operations.CheckLogin;
import com.softdesign.devintensive.utils.operations.LoadUserListFromNetwork;
import com.softdesign.devintensive.utils.operations.SaveUsersInDb;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.login_btn)
    Button mButtonLogin;
    @BindView(R.id.remember_pass_txt)
    TextView mRememberPass;
    @BindView(R.id.login_et)
    EditText mLogin;
    @BindView(R.id.pass_et)
    EditText mPassword;
    @BindView(R.id.main_coordinator_container)
    CoordinatorLayout mCoordinatorLayout;

    private DataManager mDataManager;
    List<Profile> myProfile;


    /**
     * Обработка события при создании или перезапуске активности
     *
     * @param savedInstanceState сохраненные пользовательские данные
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //присвоение разметки активности
        setContentView(R.layout.activity_login);

        mDataManager = DataManager.getInstance();

        //инициализация View через ButterKnife
        ButterKnife.bind(this);

        //назначение события нажатия
        mButtonLogin.setOnClickListener(this);
        mRememberPass.setOnClickListener(this);
    }


    /**
     * Обработка события при нажатии на View-элемент
     *
     * @param v обрабатываемый View-элемент
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_btn:
                signIn();
                break;
            case R.id.remember_pass_txt:
                rememberPassword();
                break;
        }
    }

    private void showSnackbar(String message) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    private void rememberPassword() {
        Intent rememberIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://devintensive.softdesign-apps.ru/forgotpass"));
        startActivity(rememberIntent);
    }

    private void saveTokenAndId(UserModelRes userModel) {
        mDataManager.getPreferencesManager().saveAuthToken(userModel.getData().getToken());
        mDataManager.getPreferencesManager().saveUserId(userModel.getData().getUser().getId());
    }

    private void signIn() {
        if (NetworkStatusChecker.isNetworkAvailible(this)) {
            Log.e("signIn", "showProgress()");
            showProgress();
            Log.e("signIn", "runOperation(new CheckLogin)");
            runOperation(new CheckLogin(new UserLoginReq(mLogin.getText().toString(), mPassword.getText().toString())));
        } else {
            showSnackbar("Сеть на данный момент не доступна, попробуйте позже");
        }
    }

    private void saveUserValues(UserModelRes userModel) {
        int[] userValues = {
                userModel.getData().getUser().getProfileValues().getRating(),
                userModel.getData().getUser().getProfileValues().getLinesCode(),
                userModel.getData().getUser().getProfileValues().getProjects()
        };


        List<String> userFields = new ArrayList<String>();
        userFields.add(userModel.getData().getUser().getContacts().getPhone());
        userFields.add(userModel.getData().getUser().getContacts().getEmail());
        userFields.add(userModel.getData().getUser().getContacts().getVk());

        String repos = "";
        for (int i = 0; i < userModel.getData().getUser().getRepositories().getRepo().size(); i++) {
            if (userModel.getData().getUser().getRepositories().getRepo().get(i).getGit() != null) {
                repos = repos + userModel.getData().getUser().getRepositories().getRepo().get(i).getGit() + " ";
            }
        }

        userFields.add(repos);
        userFields.add(userModel.getData().getUser().getPublicInfo().getBio());

        List<String> userFio = new ArrayList<String>();
        userFio.add(userModel.getData().getUser().getSecondName());
        userFio.add(userModel.getData().getUser().getFirstName());

        mDataManager.getPreferencesManager().saveUserProfileValues(userValues);
        mDataManager.getPreferencesManager().saveUserProfileData(userFields);
        mDataManager.getPreferencesManager().saveUserFio(userFio);
        mDataManager.getPreferencesManager().saveUserPhoto(Uri.parse(userModel.getData().getUser().getPublicInfo().getPhoto()));
        mDataManager.getPreferencesManager().saveUserAvatar(Uri.parse(userModel.getData().getUser().getPublicInfo().getAvatar()));
    }

    private List<Repository> getRepoListFromUserRes(UserListRes.UserData userData) {
        final String userId = userData.getId();

        List<Repository> repositories = new ArrayList<>();
        for (UserModelRes.Repo repositoryRes : userData.getRepositories().getRepo()) {
            repositories.add(new Repository(repositoryRes, userId));
        }

        return repositories;
    }

    public void onOperationFinished(final CheckLogin.Result result) {
        Log.e("onOperationFinished", "CheckLogin - in");
        if (result.isSuccessful()) {
            Log.e("onOperationFinished", "CheckLogin - isSuccessful");
            Call<UserModelRes> call = result.getOutput();
            call.enqueue(new Callback<UserModelRes>() {
                @Override
                public void onResponse(Call<UserModelRes> call, Response<UserModelRes> response) {
                    if (response.code() == 200) {
                        Log.e("onOperationFinished", "CheckLogin - onResponse = 200OK");
                        saveTokenAndId(response.body());
                        myProfile = new ArrayList<Profile>();
                        myProfile.add(new Profile(response.body().getUser()));
                        runOperation(new LoadUserListFromNetwork());
                    } else if (response.code() == 404) {
                        hideProgress();
                        showSnackbar("Неверный логин или пароль");
                    } else {
                        showSnackbar("Все пропало Шеф!!!");
                    }
                }

                @Override
                public void onFailure(Call<UserModelRes> call, Throwable t) {
                }
            });

        } else {
            Log.e("LoadChronos", "ErrLoadChronos");
        }
    }

    public void onOperationFinished(final LoadUserListFromNetwork.Result result) {
        Log.e("onOperationFinished", "LoadUserListFromNetwork - in");
        if (result.isSuccessful()) {
            Log.e("onOperationFinished", "LoadUserListFromNetwork - isSuccessful");
            Call<UserListRes> call = result.getOutput();
            call.enqueue(new Callback<UserListRes>() {
                @Override
                public void onResponse(Call<UserListRes> call, Response<UserListRes> response) {
                    try {
                        if (response.code() == 200) {
                            Log.e("onOperationFinished", "LoadUserListFromNetwork - onResponse = 200OK");
                            List<Repository> allRepositories = new ArrayList<Repository>();
                            List<User> allUsers = new ArrayList<User>();


                            for (UserListRes.UserData userRes : response.body().getData()) {
                                allRepositories.addAll(getRepoListFromUserRes(userRes));
                                allUsers.add(new User(userRes));
                            }

                            Log.e("onOperationFinished", "LoadUserListFromNetwork - insert BD");
                            runOperation(new SaveUsersInDb(allRepositories, allUsers, myProfile));

                        } else {
                            hideProgress();
                            showSnackbar("Список пользователей не может быть получен");
                            Log.e(TAG, "onResponse: " + String.valueOf(response.errorBody().source()));
                        }

                    } catch (NullPointerException e) {
                        e.printStackTrace();
                        showSnackbar("Что-то пошло не так");
                    }
                }

                @Override
                public void onFailure(Call<UserListRes> call, Throwable t) {
                    // TODO: 14.07.2016 Обработать ошибки
                }
            });

        } else {
            Log.e("LoadChronos", "ErrLoadChronos");
        }
    }

    public void onOperationFinished(final SaveUsersInDb.Result result) {
        Log.e("onOperationFinished", "SaveUsersInDb - in");
        if (result.isSuccessful()) {
            Log.e("onOperationFinished", "SaveUsersInDb - isSuccessful");
            Log.e("onOperationFinished", "SaveUsersInDb - Intent UserListActivity");
            Intent loginIntent = new Intent(LoginActivity.this, UserListActivity.class);
            startActivity(loginIntent);

            hideProgress();
        }
    }

}
