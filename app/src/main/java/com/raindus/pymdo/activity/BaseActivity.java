package com.raindus.pymdo.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.transition.Explode;
import android.transition.Fade;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.raindus.pymdo.R;

/**
 * Created by Raindus on 2018/4/26.
 */

public class BaseActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    protected static final int PERMISSION_CODE_STORAGE_FROM_PERSONAL = 1;
    protected static final int PERMISSION_CODE_STORAGE_FROM_THEME = 2;
    protected static final int PERMISSION_CODE_STORAGE = 3;
    private final String[] PERMISSION_STORAGE = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,};

    protected static final int REQUEST_CODE_OF_FOCUS = 1;
    protected static final int REQUEST_CODE_OF_PERSONAL = 2;

    // 初始化标题栏
    protected void initTitleBar(String title, boolean positive) {
        findViewById(R.id.title_bar_negative).setOnClickListener(this);
        ((TextView) findViewById(R.id.title_bar_title)).setText(title);
        if (positive) {
            findViewById(R.id.title_bar_positive).setVisibility(View.VISIBLE);
            findViewById(R.id.title_bar_positive).setOnClickListener(this);
        }
    }

    // 取消加载
    protected void dismissLoading() {
        findViewById(R.id.view_loading).setVisibility(View.GONE);
    }

    // 进出场动画
    protected void setTransitionAnimation() {
        getWindow().setEnterTransition(new Fade().setDuration(getResources().getInteger(R.integer.enter_transition_duration)));
        getWindow().setReturnTransition(new Explode().setDuration(getResources().getInteger(R.integer.exit_transition_duration)));
    }

    // 在当前界面之上覆盖目标界面
    public void overlay(Class<?> classObj) {
        Intent intent = new Intent(this, classObj);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    @SuppressWarnings("all")
    // 在当前界面之上覆盖目标界面
    public void overlay(Class<?> classObj, int requestCode) {
        Intent intent = new Intent(this, classObj);
        startActivityForResult(intent, requestCode, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    @SuppressWarnings("all")
    protected void overlayFocusAnim(View view) {
        ActivityOptions options =
                ActivityOptions.makeSceneTransitionAnimation(this, view, view.getTransitionName());
        startActivityForResult(new Intent(this, FocusActivity.class), REQUEST_CODE_OF_FOCUS, options.toBundle());
    }

    @TargetApi(23)
    protected boolean requestPermission(int code) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return true;

        if (ContextCompat.checkSelfPermission(this, PERMISSION_STORAGE[0]) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, PERMISSION_STORAGE, code);
            return false;
        }
        return true;
    }

    protected void choosePhoto(int tag) {
        if (requestPermission(PERMISSION_CODE_STORAGE)) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, tag);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean result = grantResults[0] == PackageManager.PERMISSION_GRANTED;
        switch (requestCode) {
            case PERMISSION_CODE_STORAGE_FROM_PERSONAL:
                if (result)
                    mHandler.sendEmptyMessage(PERMISSION_CODE_STORAGE_FROM_PERSONAL);
                else
                    toast(getResources().getString(R.string.permission_personal));
                break;
            case PERMISSION_CODE_STORAGE_FROM_THEME:
                if (result)
                    mHandler.sendEmptyMessage(PERMISSION_CODE_STORAGE_FROM_THEME);
                else
                    toast(getResources().getString(R.string.permission_theme));
                break;
            case PERMISSION_CODE_STORAGE:
                if (!result)
                    toast(getResources().getString(R.string.permission_default));
                break;
        }
    }

    @SuppressWarnings("all")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PERMISSION_CODE_STORAGE_FROM_PERSONAL:
                    overlay(PersonalSettingActivity.class, REQUEST_CODE_OF_PERSONAL);
                    break;
                case PERMISSION_CODE_STORAGE_FROM_THEME:
                    overlay(ThemeSettingActivity.class);
                    break;
            }
        }
    };

    public void debugLog(String log) {
        Log.d(getResources().getString(R.string.app_name), log);
    }

    public void toast(String t) {
        Toast.makeText(this, t, Toast.LENGTH_SHORT).show();
    }

    protected void onPositive() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_bar_negative:
                onBackPressed();
                break;
            case R.id.title_bar_positive:
                onPositive();
                onBackPressed();
                break;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }
}
