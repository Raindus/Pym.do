package com.raindus.pymdo.ui.cover;

import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;

import com.raindus.pymdo.R;
import com.raindus.pymdo.common.MathUtils;
import com.raindus.pymdo.common.Utils;
import com.raindus.pymdo.personal.CoverParam;

/**
 * Created by Raindus on 2018/4/28.
 */

public class CoverDelegate {

    // 动画时间 ms
    private static final long ANIMATOR_DURATION = 200L;

    // 不计算图片密度。封面图片宽高乘积不超过5M
    static final int BITMAP_MAX_SIZE = 5 * 1024 * 1024;

    private CoverRelativeLayout mLayout;

    private View mVertical;
    // 垂直方向动画
    private ValueAnimator mVerticalAnimator;
    // 垂直动画移动的距离
    private int mVerticalDistance = -1;
    // scroll 默认上边距
    private int mVerticalMarginTop = -1;
    // scroll 参数
    private ViewGroup.MarginLayoutParams mVerticalMarginLayoutParams;

    private View mHorizontal;

    public CoverDelegate(CoverRelativeLayout layout, View vertical, View horizontal) {
        mLayout = layout;
        mLayout.setCoverGestureListener(mListener);
        mVertical = vertical;
        mHorizontal = horizontal;

        initVerticalAnimator();
        initHorizontalAnimator();
    }

    public void setCover(String path, boolean isResId) {
        Bitmap bitmap = Utils.scaleBitmap(path, isResId, BITMAP_MAX_SIZE);
        if (bitmap != null)
            mLayout.setCover(bitmap);
        else {
            CoverParam.setCover(CoverParam.DEFAULT_COVER);
            mLayout.setCover(Integer.parseInt(CoverParam.getCover()));
        }
    }

    // ————————————
    // 上下滑动 scroll 展示与隐藏
    private boolean mInDown = CoverParam.DEFAULT_VERTICAL_IN_DOWN;

    private void initVerticalAnimator() {
        mVerticalMarginTop = (int) mLayout.getResources().getDimension(R.dimen.main_scroll_margin_top);
        mVerticalMarginLayoutParams = (ViewGroup.MarginLayoutParams) mVertical.getLayoutParams();

        mVerticalAnimator = ValueAnimator.ofFloat(0f, 1f);
        mVerticalAnimator.setInterpolator(new LinearInterpolator());
        mVerticalAnimator.setDuration(ANIMATOR_DURATION);
        mVerticalAnimator.addUpdateListener(mScrollListener);

        if (CoverParam.getVerticalInDown()) {
            mListener.slideDirection(MathUtils.DIRECTION_DOWN);
        }
    }

    // Fixme 动画出现卡顿 可能是数值初始数值大幅跳跃
    private ValueAnimator.AnimatorUpdateListener mScrollListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            float pos = (float) animation.getAnimatedValue();
            if (mInDown) {// 向下动画 0 -> dis
                mVerticalMarginLayoutParams.setMargins(0,
                        mVerticalMarginTop + (int) (pos * mVerticalDistance), 0, 0);
                mVertical.requestLayout();
            } else {//向上动画 dis - > 0
                mVerticalMarginLayoutParams.setMargins(0,
                        mVerticalMarginTop + (int) ((1f - pos) * mVerticalDistance), 0, 0);
                mVertical.requestLayout();
            }
        }
    };

    private void startVerticalAnimator() {
        if (mVerticalDistance == -1) {
            calculateVerticalDistance();
        }
        mVerticalAnimator.start();
    }

    private void calculateVerticalDistance() {
        mVerticalDistance = mLayout.mCoverRealHeight - mVerticalMarginTop
                - (int) mLayout.getResources().getDimension(R.dimen.main_scroll_distance);
    }

    // ————————————
    // 左右滑动 今日目标 展示与隐藏
    private boolean mInRight = CoverParam.DEFAULT_HORIZONTAL_IN_RIGHT;

    private void initHorizontalAnimator() {
        if (CoverParam.getHorizontalInRight()) {
            mListener.slideDirection(MathUtils.DIRECTION_LEFT);
        }
    }

    private void showHorizontal() {
        Animation animation = AnimationUtils.loadAnimation(mHorizontal.getContext(), android.R.anim.fade_in);
        animation.setDuration(ANIMATOR_DURATION);
        mHorizontal.setVisibility(View.VISIBLE);
        mHorizontal.startAnimation(animation);
    }

    private void hideHorizontal() {
        Animation animation = AnimationUtils.loadAnimation(mHorizontal.getContext(), android.R.anim.fade_out);
        animation.setDuration(ANIMATOR_DURATION);
        mHorizontal.setVisibility(View.INVISIBLE);
        mHorizontal.startAnimation(animation);
    }

    // ————————————
    // 滑动方向监听
    private CoverGestureListener mListener = new CoverGestureListener() {
        @Override
        public void slideDirection(int direction) {
            if (direction == MathUtils.DIRECTION_LEFT) {
                if (mHorizontal.getVisibility() == View.VISIBLE)
                    return;
            } else if (direction == MathUtils.DIRECTION_RIGHT) {
                if (mHorizontal.getVisibility() == View.INVISIBLE)
                    return;
            } else if (mVerticalAnimator.isRunning()) // 垂直动画
                return;

            switch (direction) {
                case MathUtils.DIRECTION_UP:// 向上
                    if (!mInDown)
                        break;
                    mInDown = false;
                    CoverParam.setVerticalInDown(mInDown);
                    startVerticalAnimator();
                    break;
                case MathUtils.DIRECTION_DOWN:// 向下
                    if (mInDown)
                        break;
                    mInDown = true;
                    CoverParam.setVerticalInDown(mInDown);
                    startVerticalAnimator();
                    break;
                case MathUtils.DIRECTION_LEFT:// 向左
                    if (mInRight)
                        break;
                    mInRight = true;
                    CoverParam.setHorizontalInRight(mInRight);
                    showHorizontal();
                    break;
                case MathUtils.DIRECTION_RIGHT:// 向右
                    if (!mInRight)
                        break;
                    mInRight = false;
                    CoverParam.setHorizontalInRight(mInRight);
                    hideHorizontal();
                    break;
            }
        }

        @Override
        public void coverRealHeightChanged() {
            calculateVerticalDistance();
        }
    };

    public interface CoverGestureListener {
        void slideDirection(int director);

        void coverRealHeightChanged();
    }
}
