package com.raindus.pymdo.focus;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.raindus.pymdo.R;
import com.raindus.pymdo.anim.RevealAnim;

/**
 * Created by Raindus on 2018/4/30.
 */

public class FocusAnim {

    private Context mContext;
    private View mRootView;
    private View mFabView;
    private View mContainerView;

    private long duration;
    private OnFocusAnimListener mListener;

    public FocusAnim(Context context, View root, View fab, View container) {
        mContext = context;
        mRootView = root;
        mFabView = fab;
        mContainerView = container;
        duration = mContext.getResources().getInteger(R.integer.focus_reveal_anim_duration);

        setEnterAnimation();
        setExitAnimation();
    }

    // 入场FAB动画
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setEnterAnimation() {
        Transition transition = TransitionInflater.from(mContext)
                .inflateTransition(R.transition.motion_focus);
        transition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
            }

            @Override
            public void onTransitionEnd(Transition transition) {
                transition.removeListener(this);
                animateRevealShow();
            }

            @Override
            public void onTransitionCancel(Transition transition) {
            }

            @Override
            public void onTransitionPause(Transition transition) {
            }

            @Override
            public void onTransitionResume(Transition transition) {
            }
        });
        ((Activity) mContext).getWindow().setSharedElementEnterTransition(transition);
    }

    // 入场扩散动画
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void animateRevealShow() {
        RevealAnim.animateRevealShow(
                mContext, mRootView, mFabView.getWidth() / 2,
                R.color.dandongshi, mRevealListener);
    }

    // 主界面淡入淡出
    public void containerVisible(final boolean show) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if (show) {
                    Animation animation = AnimationUtils.loadAnimation(mContext, android.R.anim.fade_in);
                    animation.setDuration(duration);
                    mContainerView.setVisibility(View.VISIBLE);
                    mContainerView.startAnimation(animation);
                    if (mListener != null)
                        mListener.onEnterAnim();
                } else {
                    Animation animation = AnimationUtils.loadAnimation(mContext, android.R.anim.fade_out);
                    animation.setDuration(duration);
                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            mFabView.setVisibility(View.VISIBLE);
                            animateRevealHide();
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    mContainerView.setVisibility(View.INVISIBLE);
                    mContainerView.startAnimation(animation);
                }
            }
        });
    }

    // 退出动画
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setExitAnimation() {
        Fade fade = new Fade();
        fade.setDuration(duration);
        ((Activity) mContext).getWindow().setReturnTransition(fade);
    }

    // 出场圆圈凝聚动画
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void animateRevealHide() {
        RevealAnim.animateRevealHide(
                mContext, mRootView, mFabView.getWidth() / 2,
                R.color.dandongshi, mRevealListener);
    }

    // 圆圈扩散，凝聚
    private RevealAnim.OnRevealAnimationListener mRevealListener = new RevealAnim.OnRevealAnimationListener() {
        @Override
        public void onRevealHide() {
            if (mListener != null)
                mListener.onExitAnim();
        }

        @Override
        public void onRevealShow() {
            mFabView.setVisibility(View.INVISIBLE);
            containerVisible(true);
        }
    };

    public void setOnFocusAnimListener(OnFocusAnimListener listener) {
        mListener = listener;
    }

    public interface OnFocusAnimListener {
        // 进场动画开始后
        void onEnterAnim();

        // 退出动画结束后
        void onExitAnim();
    }
}
