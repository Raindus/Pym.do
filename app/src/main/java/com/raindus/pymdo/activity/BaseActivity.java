package com.raindus.pymdo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.raindus.pymdo.R;

/**
 * Created by Raindus on 2018/4/26.
 */

public class BaseActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    // 在当前界面之上覆盖目标界面
    public void overlay(Class<?> classObj) {
        Intent intent = new Intent(this, classObj);
        startActivity(intent);
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

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }
}
