package com.raindus.pymdo.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.raindus.pymdo.R;

public class ThemeSettingActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme_setting);
        setTransitionAnimation();
        initTitleBar(getResources().getString(R.string.main_theme), false);
    }
}
