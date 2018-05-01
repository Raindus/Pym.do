package com.raindus.pymdo.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.raindus.pymdo.PymdoApplication;
import com.raindus.pymdo.R;
import com.raindus.pymdo.common.DateUtils;
import com.raindus.pymdo.common.MathUtils;
import com.raindus.pymdo.dao.ObjectBox;
import com.raindus.pymdo.focus.entity.FocusReportEntity;
import com.raindus.pymdo.ui.chart.BarChartView;
import com.raindus.pymdo.ui.chart.LineChartView;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@SuppressWarnings("all")
public class ReportActivity extends BaseActivity {

    private static final String TEXT_OVER = PymdoApplication.get().getResources().getString(R.string.report_best_focus_day_over);
    private static final String TEXT_PERIOD = PymdoApplication.get().getResources().getString(R.string.report_best_focus_period);

    private Date mCur;
    // 基本数据
    private String[] mBaseData = new String[13];
    // 时段分布
    private float[] mPeriodSpread;
    // 吸潘日分布
    private float[] mFocusDaySpread;
    // 专注次数 - 番茄收成 - 专注时长
    // 七天前 - > 当天/周/月
    private float[][] mDay;
    private String[] mKeyDay;
    private float[][] mWeek;
    private String[] mKeyWeek;
    private float[][] mMonth;
    private String[] mKeyMonth;

    // 基本报告(0-8)
    private TextView mTvTodayFocusTimes;
    private TextView mTvTodayBambooNum;
    private TextView mTvTodayFocusTotalTime;

    private TextView mTvWeekFocusTimes;
    private TextView mTvWeekBambooNum;
    private TextView mTvWeekFocusTotalTime;

    private TextView mTvAllFocusTimes;
    private TextView mTvAllBambooNum;
    private TextView mTvAllFocusTotalTime;

    // 本周最佳工作日
    private TextView mTvBestFocusDay; //9
    private TextView mTvBestFocusDayOfTime; //10
    private TextView mTvBestFocusDayOfOver; //11
    private BarChartView mBarChartWeek;

    // 本周最佳工作时段
    private TextView mTvBestPeriodOfTime; //12
    private BarChartView mBarChartTime;

    // 最近专注次数
    private int mCurFocusTimes = 0;
    private Button mBtnLatelyFocusTimesOfDay;
    private Button mBtnLatelyFocusTimesOfWeek;
    private Button mBtnLatelyFocusTimesOfMonth;
    private LineChartView mLineCharFocusTimes;

    // 最近番茄收成
    private int mCurBambooNum = 0;
    private Button mBtnLatelyBambooNumOfDay;
    private Button mBtnLatelyBambooNumOfWeek;
    private Button mBtnLatelyBambooNumOfMonth;
    private LineChartView mLineCharBambooNum;

    // 最近专注时长
    private int mCurFocusTime = 0;
    private Button mBtnLatelyFocusTimeOfDay;
    private Button mBtnLatelyFocusTimeOfWeek;
    private Button mBtnLatelyFocusTimeOfMonth;
    private LineChartView mLineCharFocusTime;

    // 线程中处理
    private HandlerThread mHandlerThread;
    private Handler mThreadHandler;
    private Handler mUIHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        setTransitionAnimation();
        initTitleBar(getResources().getString(R.string.main_report), false);
        initView();
        initData();
    }

    private void initView() {
        // 基本报告
        mTvTodayFocusTimes = findViewById(R.id.report_tv_today_focus);
        mTvTodayBambooNum = findViewById(R.id.report_tv_today_bamboo);
        mTvTodayFocusTotalTime = findViewById(R.id.report_tv_today_time);

        mTvWeekFocusTimes = findViewById(R.id.report_tv_week_focus);
        mTvWeekBambooNum = findViewById(R.id.report_tv_week_bamboo);
        mTvWeekFocusTotalTime = findViewById(R.id.report_tv_week_time);

        mTvAllFocusTimes = findViewById(R.id.report_tv_all_focus);
        mTvAllBambooNum = findViewById(R.id.report_tv_all_bamboo);
        mTvAllFocusTotalTime = findViewById(R.id.report_tv_all_time);

        // 本周最佳工作日
        mTvBestFocusDay = findViewById(R.id.report_tv_best_focus_day);
        mTvBestFocusDayOfTime = findViewById(R.id.report_tv_best_focus_day_time);
        mTvBestFocusDayOfOver = findViewById(R.id.report_tv_best_focus_day_over);
        mBarChartWeek = findViewById(R.id.report_bc_week);

        // 本周最佳工作时段
        mTvBestPeriodOfTime = findViewById(R.id.report_tv_best_time);
        mBarChartTime = findViewById(R.id.report_bc_time);

        // 最近专注次数
        mBtnLatelyFocusTimesOfDay = findViewById(R.id.report_btn_focus_day);
        mBtnLatelyFocusTimesOfDay.setOnClickListener(this);
        mBtnLatelyFocusTimesOfWeek = findViewById(R.id.report_btn_focus_week);
        mBtnLatelyFocusTimesOfWeek.setOnClickListener(this);
        mBtnLatelyFocusTimesOfMonth = findViewById(R.id.report_btn_focus_month);
        mBtnLatelyFocusTimesOfMonth.setOnClickListener(this);
        mLineCharFocusTimes = findViewById(R.id.report_lc_focus);

        // 最近番茄收成
        mBtnLatelyBambooNumOfDay = findViewById(R.id.report_btn_bamboo_day);
        mBtnLatelyBambooNumOfDay.setOnClickListener(this);
        mBtnLatelyBambooNumOfWeek = findViewById(R.id.report_btn_bamboo_week);
        mBtnLatelyBambooNumOfWeek.setOnClickListener(this);
        mBtnLatelyBambooNumOfMonth = findViewById(R.id.report_btn_bamboo_month);
        mBtnLatelyBambooNumOfMonth.setOnClickListener(this);
        mLineCharBambooNum = findViewById(R.id.report_lc_bamboo);

        // 最近专注时长
        mBtnLatelyFocusTimeOfDay = findViewById(R.id.report_btn_time_day);
        mBtnLatelyFocusTimeOfDay.setOnClickListener(this);
        mBtnLatelyFocusTimeOfWeek = findViewById(R.id.report_btn_time_week);
        mBtnLatelyFocusTimeOfWeek.setOnClickListener(this);
        mBtnLatelyFocusTimeOfMonth = findViewById(R.id.report_btn_time_month);
        mBtnLatelyFocusTimeOfMonth.setOnClickListener(this);
        mLineCharFocusTime = findViewById(R.id.report_lc_time);
    }

    private void initData() {
        mHandlerThread = new HandlerThread("report_data");
        mHandlerThread.start();
        mThreadHandler = new Handler(mHandlerThread.getLooper());
        mThreadHandler.post(mLoadDataRunnable);
    }

    // 只加载数据
    private Runnable mLoadDataRunnable = new Runnable() {
        @Override
        public void run() {
            mCur = new Date();

            // 总 记录
            FocusReportEntity all = ObjectBox.FocusReportEntityBox.queryAll();
            mBaseData[6] = String.valueOf(all.focusTimes);
            mBaseData[7] = String.valueOf(all.bambooNum);
            mBaseData[8] = MathUtils.getFloatOnePoint(all.focusTime, true);

            // 最近七天/周/月数据
            List<FocusReportEntity> dayList = ObjectBox.FocusReportEntityBox.queryLatelySevenDate();
            List<FocusReportEntity> weekList = ObjectBox.FocusReportEntityBox.queryLatelySevenWeek();
            List<FocusReportEntity> monthList = ObjectBox.FocusReportEntityBox.queryLatelySevenMonth();

            // 最佳时段
            mPeriodSpread = calculatePeriodSpread(dayList);
            mBaseData[12] = calculateBestPeriod(mPeriodSpread);

            // 最佳吸潘日
            mFocusDaySpread = calculateFocusDay(dayList);
            int index = 0;
            float max = mFocusDaySpread[0];
            float avg = max;
            for (int i = 1; i < mFocusDaySpread.length; i++) {
                if (max < mFocusDaySpread[i]) {
                    index = i;
                    max = mFocusDaySpread[i];
                }
                avg += max;
            }
            avg /= 7;
            if (max == 0)
                mBaseData[9] = getResources().getString(R.string.report_best_non_data);
            else
                mBaseData[9] = "星期" + BarChartView.TEXT_WEEK[index];
            mBaseData[10] = MathUtils.getFloatOnePoint(max, true) + "h";
            mBaseData[11] = String.format(TEXT_OVER, (int) ((max - avg) / avg * 100));

            // 折线图横坐标
            calculateKey();
            // 折线图纵坐标
            calculateDay(dayList);
            calculateWeek(weekList);
            calculateMonth(monthList);

            // 今日 记录
            mBaseData[0] = MathUtils.getFloatNoPoint(mDay[0][6]);
            mBaseData[1] = MathUtils.getFloatNoPoint(mDay[1][6]);
            mBaseData[2] = MathUtils.getFloatOnePoint(mDay[2][6], false);
            // 本周 记录
            mBaseData[3] = MathUtils.getFloatNoPoint(mWeek[0][6]);
            mBaseData[4] = MathUtils.getFloatNoPoint(mWeek[1][6]);
            mBaseData[5] = MathUtils.getFloatOnePoint(mWeek[2][6], false);

            mUIHandler.post(mShowDataRunnable);
        }
    };

    // 只展示数据
    private Runnable mShowDataRunnable = new Runnable() {
        @Override
        public void run() {
            mHandlerThread.quit();
            // 基本数据
            mTvTodayFocusTimes.setText(mBaseData[0]);
            mTvTodayBambooNum.setText(mBaseData[1]);
            mTvTodayFocusTotalTime.setText(mBaseData[2]);
            mTvWeekFocusTimes.setText(mBaseData[3]);
            mTvWeekBambooNum.setText(mBaseData[4]);
            mTvWeekFocusTotalTime.setText(mBaseData[5]);
            mTvAllFocusTimes.setText(mBaseData[6]);
            mTvAllBambooNum.setText(mBaseData[7]);
            mTvAllFocusTotalTime.setText(mBaseData[8]);
            // 最佳吸潘日
            mTvBestFocusDay.setText(mBaseData[9]);
            mTvBestFocusDayOfTime.setText(mBaseData[10]);
            mTvBestFocusDayOfOver.setText(mBaseData[11]);
            mBarChartWeek.setBarChart(mFocusDaySpread, BarChartView.MODE_WEEK);
            // 最佳时段
            mTvBestPeriodOfTime.setText(mBaseData[12]);
            mBarChartTime.setBarChart(mPeriodSpread, BarChartView.MODE_TIME);
            // 折线图
            setFocusTimesData(mCurFocusTimes);
            setBambooNumData(mCurBambooNum);
            setFocusTimeData(mCurFocusTime);

            dismissLoading();
        }
    };

    //——————————————————
    //  折线图切换
    //——————————————————
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.report_btn_focus_day:
                setFocusTimes(0);
                break;
            case R.id.report_btn_focus_week:
                setFocusTimes(1);
                break;
            case R.id.report_btn_focus_month:
                setFocusTimes(2);
                break;
            case R.id.report_btn_bamboo_day:
                setBambooNum(0);
                break;
            case R.id.report_btn_bamboo_week:
                setBambooNum(1);
                break;
            case R.id.report_btn_bamboo_month:
                setBambooNum(2);
                break;
            case R.id.report_btn_time_day:
                setFocusTime(0);
                break;
            case R.id.report_btn_time_week:
                setFocusTime(1);
                break;
            case R.id.report_btn_time_month:
                setFocusTime(2);
                break;
        }
    }

    //////////
    /// 最近吸潘次数
    private void setFocusTimes(int type) {
        if (mCurFocusTimes == type)
            return;

        switchFocusTimes(mCurFocusTimes, false);
        mCurFocusTimes = type;
        switchFocusTimes(mCurFocusTimes, true);
    }

    private void switchFocusTimes(int type, boolean flag) {
        switch (type) {
            case 0:
                mBtnLatelyFocusTimesOfDay.setTextColor(getColor(flag));
                break;
            case 1:
                mBtnLatelyFocusTimesOfWeek.setTextColor(getColor(flag));
                break;
            case 2:
                mBtnLatelyFocusTimesOfMonth.setTextColor(getColor(flag));
                break;
        }
        if (flag)
            setFocusTimesData(type);
    }

    private void setFocusTimesData(int type) {
        switch (type) {
            case 0:
                mLineCharFocusTimes.setLineChart(mKeyDay, mDay[0], false);
                break;
            case 1:
                mLineCharFocusTimes.setLineChart(mKeyWeek, mWeek[0], false);
                break;
            case 2:
                mLineCharFocusTimes.setLineChart(mKeyMonth, mMonth[0], false);
                break;
        }
    }

    //////////
    /// 最近竹子收成
    private void setBambooNum(int type) {
        if (mCurBambooNum == type)
            return;

        switchBambooNum(mCurBambooNum, false);
        mCurBambooNum = type;
        switchBambooNum(mCurBambooNum, true);
    }

    private void switchBambooNum(int type, boolean flag) {
        switch (type) {
            case 0:
                mBtnLatelyBambooNumOfDay.setTextColor(getColor(flag));
                break;
            case 1:
                mBtnLatelyBambooNumOfWeek.setTextColor(getColor(flag));
                break;
            case 2:
                mBtnLatelyBambooNumOfMonth.setTextColor(getColor(flag));
                break;
        }
        if (flag)
            setBambooNumData(type);
    }

    private void setBambooNumData(int type) {
        switch (type) {
            case 0:
                mLineCharBambooNum.setLineChart(mKeyDay, mDay[1], false);
                break;
            case 1:
                mLineCharBambooNum.setLineChart(mKeyWeek, mWeek[1], false);
                break;
            case 2:
                mLineCharBambooNum.setLineChart(mKeyMonth, mMonth[1], false);
                break;
        }
    }

    //////////
    ///最近吸潘时长
    private void setFocusTime(int type) {
        if (mCurFocusTime == type)
            return;

        switchFocusTime(mCurFocusTime, false);
        mCurFocusTime = type;
        switchFocusTime(mCurFocusTime, true);
    }

    private void switchFocusTime(int type, boolean flag) {
        switch (type) {
            case 0:
                mBtnLatelyFocusTimeOfDay.setTextColor(getColor(flag));
                break;
            case 1:
                mBtnLatelyFocusTimeOfWeek.setTextColor(getColor(flag));
                break;
            case 2:
                mBtnLatelyFocusTimeOfMonth.setTextColor(getColor(flag));
                break;
        }
        if (flag)
            setFocusTimeData(type);
    }

    private void setFocusTimeData(int type) {
        switch (type) {
            case 0:
                mLineCharFocusTime.setLineChart(mKeyDay, mDay[2], true);
                break;
            case 1:
                mLineCharFocusTime.setLineChart(mKeyWeek, mWeek[2], true);
                break;
            case 2:
                mLineCharFocusTime.setLineChart(mKeyMonth, mMonth[2], true);
                break;
        }
    }

    private int getColor(boolean flag) {
        if (flag)
            return getResources().getColor(R.color.dandongshi);
        else
            return getResources().getColor(R.color.text_color);
    }

    //——————————————————
    //  计算
    //——————————————————
    // 计算分布时段
    private float[] calculatePeriodSpread(List<FocusReportEntity> list) {
        float[] period = new float[24];
        Arrays.fill(period, 0);
        for (FocusReportEntity entity : list) {
            float[] temp = entity.getPeriodSpread();
            for (int i = 0; i < 24; i++) {
                period[i] += temp[i];
            }
        }
        return period;
    }

    // 计算最佳时段
    private String calculateBestPeriod(float[] period) {
        float maxValue = period[0];
        int maxIndex = 0;
        for (int i = 1; i < period.length; i++) {
            if (period[i] > maxValue) {
                maxValue = period[i];
                maxIndex = i;
            }
        }
        int left = maxIndex;
        int right = maxIndex;

        if (maxValue == 0)
            return getResources().getString(R.string.report_best_non_data);

        int l = maxIndex - 1;
        while (l >= 0 && period[l] > maxValue / 3) {
            left = l;
            l--;
        }
        int r = maxIndex + 1;
        while (r <= 23 && period[r] > maxValue / 3) {
            right = l;
            l++;
        }
        return String.format(TEXT_PERIOD, left, right + 1);
    }

    // 计算最佳吸潘日
    private float[] calculateFocusDay(List<FocusReportEntity> list) {
        float[] w = new float[7];
        Arrays.fill(w, 0);
        for (FocusReportEntity entity : list) {
            // 本周
            if (mCur.getDay() - entity.getReportType().intervalToNow(mCur) >= 0) {
                w[new Date(entity.date).getDay()] = entity.focusTime;
            }
        }
        return w;
    }

    // 计算折线图横坐标-最近7天/周/月日期
    private void calculateKey() {
        mKeyDay = new String[7];
        mKeyWeek = new String[7];
        mKeyMonth = new String[7];

        int lastMonth = mCur.getMonth();

        for (int i = 6; i >= 0; i--) {
            // day
            int date = mCur.getDate() - (6 - i);
            if (date > 0) {
                mKeyDay[i] = String.valueOf(date);
            } else if (mCur.getMonth() == 0) {
                if (date == 0)
                    mKeyDay[i] = String.valueOf("12.31");
                else
                    mKeyDay[i] = String.valueOf(31 - date);
            } else {
                if (date == 0)
                    mKeyDay[i] = String.valueOf(mCur.getMonth() + "." + DateUtils.getDaysOfMonth(mCur.getYear() + 1900, mCur.getMonth()));
                else
                    mKeyDay[i] = String.valueOf(DateUtils.getDaysOfMonth(mCur.getYear() + 1900, mCur.getMonth()) - date);
            }

            // week
            long week = mCur.getTime() - (mCur.getDay() * DateUtils.ONE_DAY) - ((6 - i) * DateUtils.ONE_WEEK);
            Date weekDate = new Date(week);
            if (weekDate.getMonth() == lastMonth) {
                mKeyWeek[i] = String.valueOf(weekDate.getDate());
            } else {
                lastMonth = weekDate.getMonth();
                mKeyWeek[i] = String.valueOf((weekDate.getMonth() + 1) + "." + weekDate.getDate());
            }

            // month
            int month = mCur.getMonth() + 1 - (6 - i);
            if (month > 0) {
                mKeyMonth[i] = String.valueOf(month);
            } else if (month == 0)
                mKeyMonth[i] = String.valueOf(mCur.getYear() + 1899 + "." + 12);
            else
                mKeyMonth[i] = String.valueOf(month + 12);
        }
    }

    // day 专注次数 - 番茄收成 - 专注时长
    private void calculateDay(List<FocusReportEntity> list) {
        mDay = new float[3][7];
        Arrays.fill(mDay[0], 0);
        Arrays.fill(mDay[1], 0);
        Arrays.fill(mDay[2], 0);
        for (FocusReportEntity entity : list) {
            // 间隔几天？
            int day = entity.getReportType().intervalToNow(mCur);
            int index = 6 - day;
            mDay[0][index] = entity.focusTimes;
            mDay[1][index] = entity.bambooNum;
            mDay[2][index] = (float) entity.focusTime / 60f;
        }
    }

    // week 专注次数 - 番茄收成 - 专注时长
    private void calculateWeek(List<FocusReportEntity> list) {
        mWeek = new float[3][7];
        Arrays.fill(mWeek[0], 0);
        Arrays.fill(mWeek[1], 0);
        Arrays.fill(mWeek[2], 0);
        for (FocusReportEntity entity : list) {
            // 间隔几天？
            int week = entity.getReportType().intervalToNow(mCur);
            int index = 6 - week;
            mWeek[0][index] = entity.focusTimes;
            mWeek[1][index] = entity.bambooNum;
            mWeek[2][index] = (float) entity.focusTime / 60f;
        }
    }

    // month 专注次数 - 番茄收成 - 专注时长
    private void calculateMonth(List<FocusReportEntity> list) {
        mMonth = new float[3][7];
        Arrays.fill(mMonth[0], 0);
        Arrays.fill(mMonth[1], 0);
        Arrays.fill(mMonth[2], 0);
        for (FocusReportEntity entity : list) {
            // 间隔几天？
            int month = entity.getReportType().intervalToNow(mCur);
            int index = 6 - month;
            mMonth[0][index] = entity.focusTimes;
            mMonth[1][index] = entity.bambooNum;
            mMonth[2][index] = (float) entity.focusTime / 60f;
        }
    }

}
