package com.raindus.pymdo.activity;

import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.raindus.pymdo.R;
import com.raindus.pymdo.focus.FocusParam;

public class FocusSettingActivity extends BaseActivity implements SeekBar.OnSeekBarChangeListener,
        CompoundButton.OnCheckedChangeListener {

    // 修改次数
    private TextView mTvUpdate;
    // 今日目标
    private SeekBar mSbTarget;
    private TextView mTvTarget;
    // 吸潘时长
    private SeekBar mSbTime;
    private TextView mTvTime;
    // 短休息时长
    private SeekBar mSbShortTime;
    private TextView mTvShortTime;
    // 长休息时长
    private SeekBar mSbLongTime;
    private TextView mTvLongTime;
    // 长休息间隔
    private SeekBar mSbLongInterval;
    private TextView mTvLongInterval;
    // 常亮
    private Switch mSwitchLight;
    private TextView mTvLight;
    // 音乐
    private Switch mSwitchMusic;
    private TextView mTvMusic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_focus_setting);
        setTransitionAnimation();
        initTitleBar(getResources().getString(R.string.main_bamboo_clock), true);
        initView();
        init();
    }

    private void initView() {
        mTvUpdate = findViewById(R.id.setting_focus_tv_update);

        mTvTarget = findViewById(R.id.setting_focus_tv_target);
        mSbTarget = findViewById(R.id.setting_focus_sb_target);
        mSbTarget.setOnSeekBarChangeListener(this);
        mSbTarget.setTag(1);

        mTvTime = findViewById(R.id.setting_focus_tv_time);
        mSbTime = findViewById(R.id.setting_focus_sb_time);
        mSbTime.setOnSeekBarChangeListener(this);
        mSbTime.setTag(2);

        mTvShortTime = findViewById(R.id.setting_focus_tv_short_rest);
        mSbShortTime = findViewById(R.id.setting_focus_sb_short_rest);
        mSbShortTime.setOnSeekBarChangeListener(this);
        mSbShortTime.setTag(3);

        mTvLongTime = findViewById(R.id.setting_focus_tv_long_rest);
        mSbLongTime = findViewById(R.id.setting_focus_sb_long_rest);
        mSbLongTime.setOnSeekBarChangeListener(this);
        mSbLongTime.setTag(4);

        mTvLongInterval = findViewById(R.id.setting_focus_tv_long_interval);
        mSbLongInterval = findViewById(R.id.setting_focus_sb_long_interval);
        mSbLongInterval.setOnSeekBarChangeListener(this);
        mSbLongInterval.setTag(5);

        mTvLight = findViewById(R.id.setting_focus_tv_light);
        mSwitchLight = findViewById(R.id.setting_focus_switch_light);
        mSwitchLight.setOnCheckedChangeListener(this);
        mSwitchLight.setTag(1);

        mTvMusic = findViewById(R.id.setting_focus_tv_music);
        mSwitchMusic = findViewById(R.id.setting_focus_switch_music);
        mSwitchMusic.setOnCheckedChangeListener(this);
        mSwitchMusic.setTag(2);
    }

    private void init() {
        if (FocusParam.isTodayUpdate()) {
            mTvUpdate.setText(String.format(getResources().getString(R.string.setting_focus_update), 1));
            mSbTarget.setEnabled(false);
            mSbTime.setEnabled(false);
            mSbShortTime.setEnabled(false);
            mSbLongTime.setEnabled(false);
            mSbLongInterval.setEnabled(false);
        } else
            mTvUpdate.setText(String.format(getResources().getString(R.string.setting_focus_update), 0));

        mOriginalParam[0] = FocusParam.getTarget();
        mOriginalParam[1] = FocusParam.getTime();
        mOriginalParam[2] = FocusParam.getShortRest();
        mOriginalParam[3] = FocusParam.getLongRest();
        mOriginalParam[4] = FocusParam.getLongRestInterval();
        for (int i = 0; i < 5; i++) {
            setSeekBar(i, mOriginalParam[i], true);
        }

        setSwitch(0, FocusParam.getLight(), true);
        setSwitch(1, FocusParam.getMusic(), true);
    }

    @Override
    protected void onPositive() {
        if (!FocusParam.isTodayUpdate()) {
            boolean isUpdate = false;
            for (int i = 0; i < 5; i++) {
                if (mOriginalParam[i] != mSeekBarParam[i]) {
                    isUpdate = true;
                    break;
                }
            }
            if (isUpdate) {
                setResult(RESULT_OK);
                FocusParam.setTarget(mSeekBarParam[0]);
                FocusParam.setTime(mSeekBarParam[1]);
                FocusParam.setShortRest(mSeekBarParam[2]);
                FocusParam.setLongRest(mSeekBarParam[3]);
                FocusParam.setLongRestInterval(mSeekBarParam[4]);
                FocusParam.setUpdate();
            }
        }

        FocusParam.setLight(mSwitchParam[0]);
        FocusParam.setMusic(mSwitchParam[1]);
    }

    //————————————
    // 进度条控制
    private int[] mOriginalParam = new int[5];
    private int[] mSeekBarParam = new int[5];

    private void setSeekBar(int index, int progress, boolean seekBar) {
        switch (index) {
            case 0:
                mSeekBarParam[0] = progress;
                mTvTarget.setText(parseTime(progress));
                if (seekBar)
                    mSbTarget.setProgress(progress);
                break;
            case 1:
                mSeekBarParam[1] = progress;
                mTvTime.setText(parseTime(progress));
                if (seekBar)
                    mSbTime.setProgress(progress);
                break;
            case 2:
                mSeekBarParam[2] = progress;
                mTvShortTime.setText(parseTime(progress));
                if (seekBar)
                    mSbShortTime.setProgress(progress);
                break;
            case 3:
                mSeekBarParam[3] = progress;
                mTvLongTime.setText(parseTime(progress));
                if (seekBar)
                    mSbLongTime.setProgress(progress);
                break;
            case 4:
                mSeekBarParam[4] = progress;
                mTvLongInterval.setText(progress + "个");
                if (seekBar)
                    mSbLongInterval.setProgress(progress);
                break;
        }
    }

    private String parseTime(int min) {
        int h = min / 60;
        int m = min % 60;
        StringBuilder builder = new StringBuilder();
        if (h > 0) {
            builder.append(h).append("时");
        }
        if (m > 0)
            builder.append(m).append("分");
        return builder.toString();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (!fromUser)
            return;
        switch ((int) seekBar.getTag()) {
            case 1:
                setSeekBar(0, progress + 30, false);
                break;
            case 2:
                setSeekBar(1, progress + 5, false);
                break;
            case 3:
                setSeekBar(2, progress + 1, false);
                break;
            case 4:
                setSeekBar(3, progress + 5, false);
                break;
            case 5:
                setSeekBar(4, progress + 1, false);
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    //————————————
    // 开关控制
    private boolean[] mSwitchParam = new boolean[2];

    private void setSwitch(int index, boolean isChecked, boolean s) {
        switch (index) {
            case 0:
                mSwitchParam[0] = isChecked;
                mTvLight.setText(parseSwitch(isChecked));
                if (s)
                    mSwitchLight.setChecked(isChecked);
                break;
            case 1:
                mSwitchParam[1] = isChecked;
                mTvMusic.setText(parseSwitch(isChecked));
                if (s)
                    mSwitchMusic.setChecked(isChecked);
                break;
        }
    }

    private String parseSwitch(boolean isChecked) {
        return isChecked ? "开" : "关";
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch ((int) buttonView.getTag()) {
            case 1:
                setSwitch(0, isChecked, false);
                break;
            case 2:
                setSwitch(1, isChecked, false);
                break;
        }
    }
}
