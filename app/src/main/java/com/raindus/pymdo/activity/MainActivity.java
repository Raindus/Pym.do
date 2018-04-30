package com.raindus.pymdo.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.RestrictTo;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.raindus.pymdo.R;
import com.raindus.pymdo.dao.ObjectBox;
import com.raindus.pymdo.focus.FocusParam;
import com.raindus.pymdo.focus.entity.FocusEntity;
import com.raindus.pymdo.focus.entity.FocusReportEntity;
import com.raindus.pymdo.ui.ProgressCircleView;
import com.raindus.pymdo.ui.cover.CoverDelegate;
import com.raindus.pymdo.ui.cover.CoverRelativeLayout;

import java.util.List;

public class MainActivity extends BaseActivity {

    // 封面及手势控制
    private CoverRelativeLayout mCrlCover;
    private CoverDelegate mCoverDelegate;
    private ScrollView mSvScroll;
    private RelativeLayout mRlToday;

    // 今日吸潘
    private TextView mTvTodayFocus;
    private TextView mTvTodayBamboo;
    private TextView mTvTodayFocusTime;
    private ProgressCircleView mPcvTodayProgress;

    // 吸潘报告
    private RelativeLayout mRlReport;

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

        mTvTodayFocus = findViewById(R.id.main_tv_today_focus);
        mTvTodayBamboo = findViewById(R.id.main_tv_today_bamboo);
        mTvTodayFocusTime = findViewById(R.id.main_tv_today_focus_time);
        mPcvTodayProgress = findViewById(R.id.main_pcv_today_progress);

        mRlReport = findViewById(R.id.main_rl_report);
        mRlReport.setOnClickListener(this);

        mRlSettingFocus = findViewById(R.id.main_rl_setting_focus);
        mRlSettingFocus.setOnClickListener(this);

        mFabFocus = findViewById(R.id.main_fab_focus);
        mFabFocus.setOnClickListener(this);
    }

    private void init() {
        mCoverDelegate = new CoverDelegate(mCrlCover, mSvScroll, mRlToday);
        updateTodayFocus();
    }

    // 更新封面页今日专注数据
    private void updateTodayFocus() {
        FocusReportEntity entity = ObjectBox.FocusReportEntityBox.queryToday();
        mPcvTodayProgress.setFraction(FocusParam.getTarget(), entity.focusTime);
        mTvTodayFocus.setText(String.valueOf(entity.focusTimes));
        mTvTodayBamboo.setText(String.valueOf(entity.bambooNum));
        StringBuilder builder = new StringBuilder();
        int hour = entity.focusTime / 60;
        int min = entity.focusTime % 60;
        if (hour != 0) {
            builder.append(hour).append("h");
        }
        builder.append(min);
        if (min != 0)
            builder.append("min");
        mTvTodayFocusTime.setText(builder.toString());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_rl_report:
                overlay(ReportActivity.class);
                break;
            case R.id.main_rl_setting_focus:
                overlay(FocusSettingActivity.class, REQUEST_CODE_OF_FOCUS);
                break;
            case R.id.main_fab_focus:
                overlayFocusAnim();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_OF_FOCUS) {
                updateTodayFocus();
            }
        }
    }

    @SuppressWarnings("all")
    private void overlayFocusAnim() {
        ActivityOptions options =
                ActivityOptions.makeSceneTransitionAnimation(this, mFabFocus, mFabFocus.getTransitionName());
        startActivityForResult(new Intent(this, FocusActivity.class), REQUEST_CODE_OF_FOCUS, options.toBundle());
    }
}
