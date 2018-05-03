package com.raindus.pymdo.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.raindus.pymdo.R;
import com.raindus.pymdo.common.Utils;
import com.raindus.pymdo.focus.FocusAnim;
import com.raindus.pymdo.focus.FocusDelegate;
import com.raindus.pymdo.focus.FocusLayer;
import com.raindus.pymdo.focus.FocusLayerAdapter;
import com.raindus.pymdo.focus.FocusMusic;
import com.raindus.pymdo.focus.FocusParam;
import com.raindus.pymdo.personal.CoverParam;
import com.raindus.pymdo.ui.focus.NavBarView;
import com.raindus.pymdo.ui.focus.SlideConstraintLayout;
import com.raindus.pymdo.ui.focus.FocusClockView;

/**
 * Created by Raindus on 2018/4/30.
 */

public class FocusActivity extends BaseActivity implements FocusAnim.OnFocusAnimListener, FocusLayer.OnLayerChangerListener {

    // 进出场动画
    private FrameLayout mFlRootView;
    private FloatingActionButton mFabAnim;
    private SlideConstraintLayout mRlContainer;
    private FocusAnim mFocusAnim;

    // 蒙层
    private ViewPager mVpMaskLayer;
    private FocusLayerAdapter mLayerAdapter;
    private int mPosition = 0;
    // 音乐
    private FocusMusic mTomatoMusic;
    // control
    private FocusClockView mClock;
    private NavBarView mNavBar;

    private TextView mTvTitle;

    private ImageButton mIBtnLight;
    private boolean mLightOn = false;
    private ImageButton mIBtnMusic;
    private boolean mMusicOn = true;

    private FocusDelegate mTomatoDelegate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_focus);

        initAnim();
        initView();
    }

    private void initAnim() {
        mFlRootView = findViewById(R.id.focus_fl_root);
        mFabAnim = findViewById(R.id.focus_fab_anim);
        mRlContainer = findViewById(R.id.focus_container);
        mRlContainer.setOnLayerChangerListener(this);

        mFocusAnim = new FocusAnim(this, mFlRootView, mFabAnim, mRlContainer);
        mFocusAnim.setOnFocusAnimListener(this);
    }

    private void initView() {
        mVpMaskLayer = findViewById(R.id.focus_mask_layer);
        mLayerAdapter = new FocusLayerAdapter(this);
        mVpMaskLayer.setAdapter(mLayerAdapter);
        mVpMaskLayer.addOnPageChangeListener(mRlContainer.mOnPageChangeListener);

        mTomatoMusic = new FocusMusic(this);

        mClock = findViewById(R.id.focus_clock);
        mNavBar = findViewById(R.id.focus_nav_bar);
        mRlContainer.setOnLayerScrolledListener(mNavBar.mOnLayerScrolledListener);
        mRlContainer.setOnLayerStaticListener(mNavBar.mOnLayerStaticListener);

        mTvTitle = findViewById(R.id.focus_title);

        mIBtnLight = findViewById(R.id.focus_light);
        mIBtnLight.setOnClickListener(this);
        mIBtnMusic = findViewById(R.id.focus_music);
        mIBtnMusic.setOnClickListener(this);

        mTomatoDelegate = new FocusDelegate(getWindow());
        mTomatoDelegate.setOnTomatoListener(mOnTomatoListener);
    }

    // -----------------------------------//
    // 标题 - (蒙层切换 - 状态切换)//

    private void switchTitle(final String title, final int color, boolean sendMsg) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Animation animation = AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in);
                animation.setDuration(300);
                mTvTitle.setText(title);
                mTvTitle.setTextColor(color);
                mTvTitle.startAnimation(animation);
            }
        });
        if (sendMsg) {
            if (mHandler.hasMessages(MSG_RECOVER))
                mHandler.removeMessages(MSG_RECOVER);
            mHandler.sendEmptyMessageDelayed(MSG_RECOVER, RECOVER_TIME);
        }
    }

    private static final long RECOVER_TIME = 3_000L;
    private static final int MSG_RECOVER = 1;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MSG_RECOVER) {
                switchTitle(Utils.getStringByKey(CoverParam.getNickname(), CoverParam.isNicknameDefault()),
                        Color.WHITE, false);
            }
        }
    };

    // -------
    // 蒙层切换
    @Override
    public void onLayerSelected(int position) {
        mPosition = position;
        mTomatoMusic.playTomatoMusic(position);

        switchTitle(FocusLayer.MUSIC_DESCRIBE[position],
                getResources().getColor(R.color.mid_transparent), true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.focus_light:
                if (mLightOn) {
                    mIBtnLight.setColorFilter(getResources().getColor(R.color.mid_transparent));
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                } else {
                    mIBtnLight.setColorFilter(Color.WHITE);
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                    toast("屏幕已常亮");
                }
                mLightOn = !mLightOn;
                break;
            case R.id.focus_music:
                if (mMusicOn) {
                    mIBtnMusic.setColorFilter(getResources().getColor(R.color.mid_transparent));
                    mTomatoMusic.stopTomatoMusic();
                    mClock.stopMusic();
                } else {
                    mIBtnMusic.setColorFilter(Color.WHITE);
                    mTomatoMusic.restartTomatoMusic(mPosition);
                    mClock.playMusic();
                }
                mMusicOn = !mMusicOn;
                break;
        }
    }

    // -----------------------------------//
    // 番茄钟 //


    @Override
    protected void onResume() {
        super.onResume();
        mTomatoDelegate.onContinue();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mTomatoDelegate.onPause();
    }

    private FocusDelegate.OnTomatoListener mOnTomatoListener = new FocusDelegate.OnTomatoListener() {
        @Override
        public void onStart(String time, int status) {
            mClock.startTiming(time, status);
        }

        @Override
        public void onStatusChanged(int status) {
            mTomatoMusic.playRingtone();
            switchTitle(FocusDelegate.STATUS_DESCRIBE[status],
                    getResources().getColor(R.color.mid_transparent), true);
        }

        @Override
        public void onTiming(String time, float fraction) {
            mClock.onTiming(time, fraction);
        }

        @Override
        public void onQuit() {
            onBackPressed();
        }
    };

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        mFocusAnim.containerVisible(false);
        mTomatoMusic.onDestroy();
    }

    @Override
    public void onEnterAnim() {
        if (FocusParam.getMusic()) {
            mTomatoMusic.playTomatoMusic(0);
            mClock.playMusic();
        } else
            mMusicOn = false;

        if (FocusParam.getLight()) {
            mIBtnLight.setColorFilter(Color.WHITE);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            mLightOn = true;
        }

        mHandler.sendEmptyMessage(MSG_RECOVER);

        mTomatoDelegate.onStart();
    }

    @Override
    public void onExitAnim() { // 真正返回
        super.onBackPressed();
    }

    private Context getContext() {
        return this;
    }
}
