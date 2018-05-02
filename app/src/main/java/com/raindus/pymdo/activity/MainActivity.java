package com.raindus.pymdo.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.raindus.pymdo.R;
import com.raindus.pymdo.common.DateUtils;
import com.raindus.pymdo.common.Utils;
import com.raindus.pymdo.dao.ObjectBox;
import com.raindus.pymdo.focus.FocusParam;
import com.raindus.pymdo.focus.entity.FocusReportEntity;
import com.raindus.pymdo.personal.CoverParam;
import com.raindus.pymdo.ui.ProgressCircleView;
import com.raindus.pymdo.ui.cover.CoverDelegate;
import com.raindus.pymdo.ui.cover.CoverRelativeLayout;

public class MainActivity extends BaseActivity {

    static final int BANNER_MAX_SIZE = 1 * 1024 * 1024;

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

    // banner 个性化
    private TextView mTvNickname;
    private ImageView mIvBanner;
    private ImageView mIvReport;

    // 吸潘报告
    private RelativeLayout mRlReport;

    // 设置
    private RelativeLayout mRlSettingFocus;
    private RelativeLayout mRlSettingPersonal;
    private RelativeLayout mRlSettingTheme;

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

        mTvNickname = findViewById(R.id.main_tv_title);
        mIvBanner = findViewById(R.id.main_iv_banner);
        mIvReport = findViewById(R.id.main_iv_report);

        mRlSettingFocus = findViewById(R.id.main_rl_setting_focus);
        mRlSettingFocus.setOnClickListener(this);
        mRlSettingPersonal = findViewById(R.id.main_rl_setting_personal);
        mRlSettingPersonal.setOnClickListener(this);
        mRlSettingTheme = findViewById(R.id.main_rl_setting_theme);
        mRlSettingTheme.setOnClickListener(this);

        mFabFocus = findViewById(R.id.main_fab_focus);
        mFabFocus.setOnClickListener(this);
    }

    private void init() {
        mCoverDelegate = new CoverDelegate(mCrlCover, mSvScroll, mRlToday);
        updatePersonal();
        updateTodayFocus();
    }

    // 更新封面、昵称、banner
    private void updatePersonal() {
        mTvNickname.setText(Utils.getStringByKey(CoverParam.getNickname(), CoverParam.isNicknameDefault()));
        mCoverDelegate.setCover(CoverParam.getCover(), CoverParam.isCoverDefault());
        Bitmap temp = Utils.scaleBitmap(CoverParam.getBanner(), CoverParam.isBannerDefault(), BANNER_MAX_SIZE);
        if (temp != null) {
            mIvBanner.setImageBitmap(temp);
        } else { // 恢复默认
            CoverParam.setBanner(CoverParam.DEFAULT_BANNER);
            temp = Utils.scaleBitmap(CoverParam.DEFAULT_BANNER, true, BANNER_MAX_SIZE);
            mIvBanner.setImageBitmap(temp);
        }
        temp = Utils.scaleBitmap(CoverParam.getReport(), CoverParam.isReportDefault(), BANNER_MAX_SIZE);
        if (temp != null) {
            mIvReport.setImageBitmap(temp);
        } else { // 恢复默认
            CoverParam.setReport(CoverParam.DEFAULT_REPORT);
            temp = Utils.scaleBitmap(CoverParam.DEFAULT_REPORT, true, BANNER_MAX_SIZE);
            mIvReport.setImageBitmap(temp);
        }
    }

    // 更新封面页今日专注数据
    private void updateTodayFocus() {
        FocusReportEntity entity = ObjectBox.FocusReportEntityBox.queryToday();
        mPcvTodayProgress.setFraction(FocusParam.getTarget(), entity.focusTime);
        mTvTodayFocus.setText(String.valueOf(entity.focusTimes));
        mTvTodayBamboo.setText(String.valueOf(entity.bambooNum));
        mTvTodayFocusTime.setText(DateUtils.parseTime(entity.focusTime));
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
            case R.id.main_rl_setting_personal:
                if (requestPermission(PERMISSION_CODE_STORAGE_FROM_PERSONAL))
                    overlay(PersonalSettingActivity.class, REQUEST_CODE_OF_PERSONAL);
                break;
            case R.id.main_rl_setting_theme:
                if (requestPermission(PERMISSION_CODE_STORAGE_FROM_THEME))
                    overlay(ThemeSettingActivity.class);
                break;
            case R.id.main_fab_focus:
                overlayFocusAnim(mFabFocus);
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
            if (requestCode == REQUEST_CODE_OF_PERSONAL) {
                updatePersonal();
            }
        }
    }
}
