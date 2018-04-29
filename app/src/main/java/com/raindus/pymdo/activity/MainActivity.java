package com.raindus.pymdo.activity;

import android.os.Bundle;
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
    }

    private void init() {
        mCoverDelegate = new CoverDelegate(mCrlCover, mSvScroll, mRlToday);

        mPcvTodayProgress.setFraction(60, 30);
    }

}
