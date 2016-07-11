package com.softdesign.devintensive.ui.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.softdesign.devintensive.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.login_btn)
    Button mButtonLogin;

    /**
     * Обработка события при создании или перезапуске активности
     * @param savedInstanceState сохраненные пользовательские данные
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //присвоение разметки активности
        setContentView(R.layout.activity_login);
        //инициализация View через ButterKnife
        ButterKnife.bind(this);

        //назначение события нажатия
        mButtonLogin.setOnClickListener(this);
    }


    /**
     * Обработка события при нажатии на View-элемент
     * @param v обрабатываемый View-элемент
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_btn:
                doGotoMainActivity();
                break;
        }
    }

    /**
     * Переход на главную Activity
     */
    private void doGotoMainActivity() {
        Intent intentGotoMainActivity = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intentGotoMainActivity);
    }
}
