package com.raindus.pymdo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.raindus.pymdo.R;

/**
 * Created by Raindus on 2018/4/26.
 */

public class BaseActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    protected static final int REQUEST_CODE_OF_FOCUS = 1;

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

    // 在当前界面之上覆盖目标界面
    public void overlay(Class<?> classObj) {
        Intent intent = new Intent(this, classObj);
        startActivity(intent);
    }

    // 在当前界面之上覆盖目标界面
    public void overlay(Class<?> classObj, int requestCode) {
        Intent intent = new Intent(this, classObj);
        startActivityForResult(intent, requestCode);
    }

    // 带数据
    public void overlay(Class<?> classObj, Bundle params) {
        Intent intent = new Intent(this, classObj);
        intent.putExtras(params);
        startActivity(intent);
    }

    // 切换当前界面至目标界面
    public void forward(Class<?> classObj) {
        Intent intent = new Intent(this, classObj);
        this.startActivity(intent);
        this.finish();
    }

    // 带数据
    public void forward(Class<?> classObj, Bundle params) {
        Intent intent = new Intent(this, classObj);
        intent.putExtras(params);
        this.startActivity(intent);
        this.finish();
    }

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
                finish();
                break;
            case R.id.title_bar_positive:
                onPositive();
                finish();
                break;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }
}
