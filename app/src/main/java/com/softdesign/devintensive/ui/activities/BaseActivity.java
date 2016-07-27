package com.softdesign.devintensive.ui.activities;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.redmadrobot.chronos.ChronosConnector;
import com.redmadrobot.chronos.ChronosOperation;
import com.redmadrobot.chronos.gui.ChronosConnectorWrapper;
import com.softdesign.devintensive.R;
import com.softdesign.devintensive.utils.ConstantManager;

import org.jetbrains.annotations.Contract;

@SuppressWarnings("unused")
public class BaseActivity extends AppCompatActivity implements ChronosConnectorWrapper {
    static final String TAG = ConstantManager.TAG_PREFIX + "BaseActivity";
    protected ProgressDialog mProgressDialog;

    private final ChronosConnector mConnector = new ChronosConnector();

    /**
     * Вывод окна ProgressBar'а
     */
    public void showProgress(){
        //
        if (mProgressDialog == null){
            mProgressDialog = new ProgressDialog(this, R.style.progress_dialog);
            mProgressDialog.setCancelable(false);
            mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            mProgressDialog.show();
            mProgressDialog.setContentView(R.layout.progress_splash);
        } else {
            mProgressDialog.show();
            mProgressDialog.setContentView(R.layout.progress_splash);
        }
    }

    /**
     * Скрытие окна ProgressBar'а
     */
    public void hideProgress() {
        if (mProgressDialog != null){
            if (mProgressDialog.isShowing()){
                mProgressDialog.hide();
            }
        }
    }


    /**
     * Вывод Toast-сообщения об ошибке
     * @param message сообщение
     * @param error exception
     */
    public void showError(String message, Exception error){
        showToast(message);
        Log.e(TAG, String.valueOf(error));
    }

    /**
     * Вывод Toast-сообщения
     * @param message сообщение
     */
    public void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mConnector.onCreate(this, savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mConnector.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mConnector.onSaveInstanceState(outState);
    }

    @Override
    protected void onPause() {
        mConnector.onPause();
        super.onPause();
    }

    @Override
    public int runOperation(@NonNull ChronosOperation operation, @NonNull String tag) {
        return mConnector.runOperation(operation, tag, false);
    }

    @Override
    public int runOperation(@NonNull ChronosOperation operation) {
        return mConnector.runOperation(operation, false);
    }

    @Override
    public int runOperationBroadcast(@NonNull ChronosOperation operation, @NonNull String tag) {
        return mConnector.runOperation(operation, tag, true);
    }

    @Override
    public int runOperationBroadcast(@NonNull ChronosOperation operation) {
        return mConnector.runOperation(operation, true);
    }

    @Override
    public boolean cancelOperation(int id) {
        return mConnector.cancelOperation(id, true);
    }

    @Override
    public boolean cancelOperation(@NonNull String tag) {
        return mConnector.cancelOperation(tag, true);
    }

    @Override
    @Contract(pure = true)
    public boolean isOperationRunning(int id) {
        return mConnector.isOperationRunning(id);
    }

    @Override
    @Contract(pure = true)
    public boolean isOperationRunning(@NonNull String tag) {
        return mConnector.isOperationRunning(tag);
    }
}
