package com.raindus.pymdo.dao;

import com.raindus.pymdo.PymdoApplication;
import com.raindus.pymdo.common.DateUtils;
import com.raindus.pymdo.focus.entity.FocusEntity;
import com.raindus.pymdo.focus.entity.FocusEntity_;
import com.raindus.pymdo.focus.entity.FocusReportEntity;
import com.raindus.pymdo.focus.entity.FocusReportEntity_;
import com.raindus.pymdo.focus.entity.ReportType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.objectbox.Box;
import io.objectbox.query.QueryBuilder;

/**
 * Created by Raindus on 2018/4/30.
 */

public class ObjectBox {

    public static class FocusEntityBox {

        public static Box<FocusEntity> getBox() {
            return PymdoApplication.getBoxStore().boxFor(FocusEntity.class);
        }

        // 添加 or 更新
        public static long put(FocusEntity entity) {
            return getBox().put(entity);
        }

        // 查询今天的竹子钟
        public static List<FocusEntity> queryToday() {
            QueryBuilder<FocusEntity> query = getBox().query();

            long startTime = DateUtils.getTodayTime(true);
            long endTime = DateUtils.getTodayTime(false);

            return query.between(FocusEntity_.startTime, startTime, endTime).build().find();
        }
    }

    public static class FocusReportEntityBox {

        public static Box<FocusReportEntity> getBox() {
            return PymdoApplication.getBoxStore().boxFor(FocusReportEntity.class);
        }

        // 添加 or 更新
        public static long put(FocusReportEntity entity) {
            return getBox().put(entity);
        }

        // 查询 天 - 周 - 月 - 日 需要升级的表
        public static List<FocusReportEntity> queryNeedUpdateNow() {
            Date cur = new Date();
            List<FocusReportEntity> list = new ArrayList<>();
            List<FocusReportEntity> temp;

            // 当天
            temp = getBox().query()
                    .equal(FocusReportEntity_.type, ReportType.DAY.getType())
                    .between(FocusReportEntity_.date, ReportType.DAY.getNowAgo(cur), cur.getTime())
                    .build().find();
            if (temp == null || temp.size() == 0)
                list.add(new FocusReportEntity(ReportType.DAY, cur.getTime()));
            else
                list.add(temp.get(0));

            // 当周
            temp = getBox().query()
                    .equal(FocusReportEntity_.type, ReportType.WEEK.getType())
                    .between(FocusReportEntity_.date, ReportType.WEEK.getNowAgo(cur), cur.getTime())
                    .build().find();
            if (temp == null || temp.size() == 0)
                list.add(new FocusReportEntity(ReportType.WEEK, cur.getTime()));
            else
                list.add(temp.get(0));

            // 当月
            temp = getBox().query()
                    .equal(FocusReportEntity_.type, ReportType.MONTH.getType())
                    .between(FocusReportEntity_.date, ReportType.MONTH.getNowAgo(cur), cur.getTime())
                    .build().find();
            if (temp == null || temp.size() == 0)
                list.add(new FocusReportEntity(ReportType.MONTH, cur.getTime()));
            else
                list.add(temp.get(0));

            // 总
            list.add(queryAll());
            return list;
        }

        // type = DAY 只有一个
        public static FocusReportEntity queryToday() {
            long startTime = DateUtils.getTodayTime(true);
            long endTime = DateUtils.getTodayTime(false);

            List<FocusReportEntity> temp = getBox().query()
                    .equal(FocusReportEntity_.type, ReportType.DAY.getType())
                    .between(FocusReportEntity_.date, startTime, endTime)
                    .build().find();

            if (temp == null || temp.size() == 0)
                return new FocusReportEntity(ReportType.DAY, System.currentTimeMillis());
            else
                return temp.get(0);
        }

        // type = all 只有一个
        public static FocusReportEntity queryAll() {
            List<FocusReportEntity> list =
                    getBox().query()
                            .equal(FocusReportEntity_.type, ReportType.ALL.getType())
                            .build().find();

            if (list == null || list.size() == 0)
                return new FocusReportEntity(ReportType.ALL, new Date().getTime());
            else
                return list.get(0);
        }

        // 查询最近7天，已排序,最近一天在上面
        public static List<FocusReportEntity> queryLatelySevenDate() {
            Date cur = new Date();

            return getBox().query()
                    .equal(FocusReportEntity_.type, ReportType.DAY.getType())
                    .between(FocusReportEntity_.date, ReportType.DAY.getSevenAgo(cur), cur.getTime())
                    .orderDesc(FocusReportEntity_.date)
                    .build().find();
        }

        // 最近7周，已排序,最近一周在上面
        public static List<FocusReportEntity> queryLatelySevenWeek() {
            Date cur = new Date();

            return getBox().query()
                    .equal(FocusReportEntity_.type, ReportType.WEEK.getType())
                    .between(FocusReportEntity_.date, ReportType.WEEK.getSevenAgo(cur), cur.getTime())
                    .orderDesc(FocusReportEntity_.date)
                    .build().find();
        }

        // 最近7个月，已排序,最近一月在上面
        public static List<FocusReportEntity> queryLatelySevenMonth() {
            Date cur = new Date();

            return getBox().query()
                    .equal(FocusReportEntity_.type, ReportType.MONTH.getType())
                    .between(FocusReportEntity_.date, ReportType.MONTH.getSevenAgo(cur), cur.getTime())
                    .orderDesc(FocusReportEntity_.date)
                    .build().find();
        }
    }
}
