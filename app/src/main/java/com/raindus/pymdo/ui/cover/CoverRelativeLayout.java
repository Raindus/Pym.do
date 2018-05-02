package com.raindus.pymdo.ui.cover;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import com.raindus.pymdo.R;
import com.raindus.pymdo.common.MathUtils;
import com.raindus.pymdo.common.Utils;

/**
 * Created by Raindus on 2018/4/28.
 */

public class CoverRelativeLayout extends RelativeLayout implements GestureDetector.OnGestureListener {

    private GestureDetector mGesture;

    // 最小移动距离（px）
    private static final int FLING_MIN_DISTANCE = 200;
    // 最大移动速度（px/s）
    private static final int FLING_MIN_VELOCITY = 200;

    // 封面图片资源
    private int mCoverResId;

    // 容器宽高
    private int mWidth = -1;
    private int mHeight = -1;

    // 封面图片 及宽高
    private Bitmap mCover;
    private int mCoverWidth = -1;
    private int mCoverHeight = -1;

    int mCoverRealHeight = -1;

    // 绘制完整封面
    private Rect mSrcRect;
    // 封面绘制在容器位置
    private Rect mDstRect;
    private Paint mPaint = new Paint();

    private CoverDelegate.CoverGestureListener mListener;

    public CoverRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CoverRelativeLayout);
        int coverResId = ta.getResourceId(R.styleable.CoverRelativeLayout_cover, -1);
        setCover(coverResId);

        mGesture = new GestureDetector(context, this);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        setCoverRect();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mCover != null)
            canvas.drawBitmap(mCover, mSrcRect, mDstRect, mPaint);
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mCover != null)
            mCover.recycle();
    }

    // ————————————
    // 设置封面
    void setCover(Bitmap bitmap) {
        if (bitmap != null) {
            mCoverResId = bitmap.getGenerationId();
            if (mCover != null)
                mCover.recycle();
            mCover = bitmap;
            mCoverWidth = mCover.getWidth();
            mCoverHeight = mCover.getHeight();
            setCoverRect();
        }
    }

    // 设置封面图片,并限制大小
    void setCover(int resId) {
        if (mCoverResId == resId)
            return;

        // 限制图片大小
        Bitmap temp = Utils.scaleBitmap(String.valueOf(resId), true, CoverDelegate.BITMAP_MAX_SIZE);
        if (temp != null) {
            mCoverResId = resId;
            if (mCover != null)
                mCover.recycle();
            mCover = temp;
            mCoverWidth = mCover.getWidth();
            mCoverHeight = mCover.getHeight();
            setCoverRect();
        }
    }

    // 封面宽度适应容器，局上方
    private void setCoverRect() {
        if (mCoverWidth == -1 || mCoverHeight == -1 || mWidth == -1 || mHeight == -1)
            return;

        mSrcRect = new Rect(0, 0, mCoverWidth, mCoverHeight);
        mCoverRealHeight = (int) (((float) mWidth * (float) mCoverHeight) / (float) mCoverWidth + 0.5f);
        mDstRect = new Rect(0, 0, mWidth, mCoverRealHeight);
        invalidate();

        if (mListener != null)
            mListener.coverRealHeightChanged();
    }


    // ————————————
    // 滑动手势
    void setCoverGestureListener(CoverDelegate.CoverGestureListener listener) {
        mListener = listener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGesture.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

        // 滑动速度过慢忽略
        if (Math.max(Math.abs(velocityX), Math.abs(velocityY)) < FLING_MIN_VELOCITY)
            return false;

        // 滑动距离过短忽略
        if (Math.max(Math.abs(e2.getY() - e1.getY()), Math.abs(e2.getX() - e1.getX())) < FLING_MIN_DISTANCE)
            return false;

        if (mListener != null) {
            mListener.slideDirection(MathUtils.calculateDirection(e1.getX(), e1.getY(), e2.getX(), e2.getY()));
        }

        return false;
    }

    // ————————————
    // 以下为未用到的手势接口

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
    }

}
