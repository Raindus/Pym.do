package com.raindus.pymdo.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.raindus.pymdo.R;
import com.raindus.pymdo.common.Utils;

/**
 * Created by Raindus on 2018/4/14.
 */

public class ProgressCircleView extends View {

    // 圆心坐标
    private float cx, cy;
    // 半径
    private float mRadius;
    // 阴影画笔
    private Paint mShadePaint;
    // 进度画笔
    private Paint mPaint;
    // 进度
    private Path mPath;
    private PathMeasure mPathMeasure;

    // 目标画笔 和 基线
    private Paint mTextPaint;
    private float mBaseLine;

    // 完成进度 画笔 和 进度
    private Paint mCompleteTextPaint;
    private float mCompleteBaseLine;

    public ProgressCircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    private void initPaint() {
        mShadePaint = new Paint();
        mShadePaint.setAntiAlias(true);
        mShadePaint.setStyle(Paint.Style.STROKE);
        mShadePaint.setStrokeWidth(8f);
        mShadePaint.setColor(getResources().getColor(R.color.mid_transparent));

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(12f);
        mPaint.setColor(getResources().getColor(R.color.dandongshi));

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTextSize(Utils.dipToPx(12f));
        mTextPaint.setColor(getResources().getColor(R.color.mid_transparent));

        mCompleteTextPaint = new Paint();
        mCompleteTextPaint.setAntiAlias(true);
        mCompleteTextPaint.setTextAlign(Paint.Align.CENTER);
        mCompleteTextPaint.setColor(Color.WHITE);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        cx = (float) w / 2f;
        cy = (float) h / 2f;
        mRadius = Math.min(cx, cy) - getResources().getDimension(R.dimen.main_today_margin_horizontal);

        mPath = new Path();
        mPath.addArc(cx - mRadius, cy - mRadius,
                cx + mRadius, cy + mRadius,
                -90, 360);
        mPathMeasure = new PathMeasure(mPath, true);

        mBaseLine = cy + (mRadius * 0.7f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawCircle(cx, cy, mRadius, mShadePaint);

        Path tempClockPath = new Path();
        boolean success = mPathMeasure.getSegment(0,
                mPathMeasure.getLength() * mFraction, tempClockPath, true);
        if (success)
            canvas.drawPath(tempClockPath, mPaint);

        if (mIsComplete) {
            mCompleteBaseLine = cy + (Utils.dipToPx(24) * 0.372f);
            mCompleteTextPaint.setTextSize(Utils.dipToPx(24));
            canvas.drawText(mCompleteText, cx, mCompleteBaseLine, mCompleteTextPaint);
        } else {
            mCompleteBaseLine = cy + (Utils.dipToPx(32) * 0.372f);
            mCompleteTextPaint.setTextSize(Utils.dipToPx(32));
            canvas.drawText(mText, cx, mBaseLine, mTextPaint);
            canvas.drawText(mCompleteText, cx + (Utils.dipToPx(32) * 0.125f), mCompleteBaseLine, mCompleteTextPaint);
        }
    }

    private float mFraction = 0;
    private boolean mIsComplete = false;
    private final String FRACTION = getResources().getString(R.string.progress_circle_view_fraction);
    private String mCompleteText = String.format(FRACTION, 0);
    private final String TEXT = getResources().getString(R.string.progress_circle_view_target);
    private String mText = String.format(TEXT, 60);

    /**
     * @param target 目标
     * @param real   已达成
     */
    public void setFraction(int target, int real) {
        mFraction = (float) real / (float) target;
        if (mFraction >= 1) {
            mFraction = 1;
            mIsComplete = true;
            mCompleteText = getResources().getString(R.string.progress_circle_view_completed);
        } else {
            mIsComplete = false;
            mCompleteText = String.format(FRACTION, (int) ((mFraction + 0.005f) * 100));
            mText = String.format(TEXT, target);
        }
        invalidate();
    }
}
