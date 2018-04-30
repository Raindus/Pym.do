package com.raindus.pymdo.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.raindus.pymdo.R;
import com.raindus.pymdo.ui.ProgressCircleView;
import com.raindus.pymdo.ui.cover.CoverDelegate;
import com.raindus.pymdo.ui.cover.CoverRelativeLayout;

public class MainActivity extends BaseActivity {

    // 封面及手势控制
    private CoverRelativeLayout mCrlCover;
    private CoverDelegate mCoverDelegate;
    private ScrollView mSvScroll;
    private RelativeLayout mRlToday;

    // 今日吸潘
    private ProgressCircleView mPcvTodayProgress;

    // 设置
    private RelativeLayout mRlSettingFocus;

    // 进入吸潘
    private FloatingActionButton mFabFocus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        init();
    }

    private void initView() {
        mCrlCover = findViewById(R.id.main_crl_cover);
        mSvScroll = findViewById(R.id.main_sv_scroll);
        mRlToday = findViewById(R.id.main_rl_today);

        mPcvTodayProgress = findViewById(R.id.main_pcv_today_progress);

        mRlSettingFocus = findViewById(R.id.main_rl_setting_focus);
        mRlSettingFocus.setOnClickListener(this);

        mFabFocus = findViewById(R.id.main_fab_focus);
        mFabFocus.setOnClickListener(this);
    }

    private void init() {
        mCoverDelegate = new CoverDelegate(mCrlCover, mSvScroll, mRlToday);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //mPcvTodayProgress.setFraction(60, 30);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_rl_setting_focus:
                overlay(FocusSettingActivity.class);
                break;
            case R.id.main_fab_focus:
                overlayFocusAnim();
                break;
        }
    }

    private void overlayFocusAnim() {
        ActivityOptions options =
                ActivityOptions.makeSceneTransitionAnimation(this, mFabFocus, mFabFocus.getTransitionName());
        startActivity(new Intent(this, FocusActivity.class), options.toBundle());
    }
}
