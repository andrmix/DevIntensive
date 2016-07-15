package com.softdesign.devintensive.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.network.req.UserLoginReq;
import com.softdesign.devintensive.data.network.res.UserModelRes;
import com.softdesign.devintensive.utils.NetworkStatusChecker;

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

    /**
     * Обработка события при создании или перезапуске активности
     *
     * @param savedInstanceState сохраненные пользовательские данные
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

    private void loginSuccess(UserModelRes userModel) {
        showSnackbar(userModel.getData().getToken());
        mDataManager.getPreferencesManager().saveAuthToken(userModel.getData().getToken());
        mDataManager.getPreferencesManager().saveUserId(userModel.getData().getUser().getId());

        saveUserValues(userModel);
        Intent loginIntent = new Intent(this, UserListActivity.class);
        startActivity(loginIntent);
    }

    private void signIn() {
        if (NetworkStatusChecker.isNetworkAvailible(this)) {
            Call<UserModelRes> call = mDataManager.loginUser(new UserLoginReq(mLogin.getText().toString(), mPassword.getText().toString()));
            call.enqueue(new Callback<UserModelRes>() {
                @Override
                public void onResponse(Call<UserModelRes> call, Response<UserModelRes> response) {
                    if (response.code() == 200) {
                        showProgress();
                        loginSuccess(response.body());
                    } else if (response.code() == 404) {
                        showSnackbar("Неверный логин или пароль");
                    } else {
                        showSnackbar("Все пропало Шеф!!!");
                    }
                }

                @Override
                public void onFailure(Call<UserModelRes> call, Throwable t) {
                    // TODO: 11.07.2016 обработать ошибки ретрофита
                }
            });
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
}
