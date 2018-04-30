package com.raindus.pymdo.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.raindus.pymdo.R;
import com.raindus.pymdo.focus.FocusAnim;

/**
 * Created by Raindus on 2018/4/30.
 */

public class FocusActivity extends BaseActivity implements FocusAnim.OnFocusAnimListener {

    // 进出场动画
    private FrameLayout mFlRootView;
    private FloatingActionButton mFabAnim;
    private RelativeLayout mRlContainer;
    private FocusAnim mFocusAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_focus);

        initAnim();
    }

    private void initAnim() {
        mFlRootView = findViewById(R.id.focus_fl_root);
        mFabAnim = findViewById(R.id.focus_fab_anim);
        mRlContainer = findViewById(R.id.focus_container);

        mFocusAnim = new FocusAnim(this, mFlRootView, mFabAnim, mRlContainer);
        mFocusAnim.setOnFocusAnimListener(this);
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        mFocusAnim.containerVisible(false);
    }

    @Override
    public void onEnterAnim() {

    }

    @Override
    public void onExitAnim() { // 真正返回
        super.onBackPressed();
    }
}
