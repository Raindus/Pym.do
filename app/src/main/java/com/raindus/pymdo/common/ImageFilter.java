package com.raindus.pymdo.common;

import android.graphics.BitmapFactory;

/**
 * Created by Raindus on 2018/5/2.
 */

public class ImageFilter {

    private static BitmapFactory.Options getOptions(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        if (options.outHeight != -1 || options.outWidth != -1)
            return options;
        else
            return null;
    }

    public static class PersonalFilter {

        public static String cover(String path) {
            // 封面图片最小像素
            final int mMinSize = 500;
            // 封面图片最小宽高比例
            final float mMinScale = 10f / 16f;
            // 封面图片最大宽高比例
            final float mMaxScale = 3f / 4f;

            BitmapFactory.Options options = getOptions(path);
            if (options == null)
                return "图片解码失败";

            if (Math.min(options.outHeight, options.outWidth) < mMinSize)
                return "封面最小边不少于500像素";

            float scale = (float) options.outWidth / (float) options.outHeight;
            if (scale > mMaxScale)
                return "封面宽高比不超过3:4";
            if (scale < mMinScale)
                return "封面宽高比不低于10:16";

            return null;
        }

        public static String banner(String path) {
            // 封面图片最小像素
            final int mMinSize = 300;
            // 封面图片最小宽高比例
            final float mMinScale = 1f / 1f;
            // 封面图片最大宽高比例
            final float mMaxScale = 3f / 1f;

            BitmapFactory.Options options = getOptions(path);
            if (options == null)
                return "图片解码失败";

            if (Math.min(options.outHeight, options.outWidth) < mMinSize)
                return "图片最小边不少于300像素";

            float scale = (float) options.outWidth / (float) options.outHeight;
            if (scale > mMaxScale)
                return "图片宽高比不超过3:1";
            if (scale < mMinScale)
                return "图片宽高比不低于1:1";

            return null;
        }
    }

    public static class ThemeFilter {

        public static String single(String path) {
            // 封面图片最小像素
            final int mMinSize = 800;
            // 封面图片最小宽高比例
            final float mMinScale = 1f / 1f;
            // 封面图片最大宽高比例
            final float mMaxScale = 4f / 3f;

            BitmapFactory.Options options = getOptions(path);
            if (options == null)
                return "图片解码失败";

            if (Math.min(options.outHeight, options.outWidth) < mMinSize)
                return "封面最小边不少于800像素";

            float scale = (float) options.outWidth / (float) options.outHeight;
            if (scale > mMaxScale)
                return "封面宽高比不超过4:3";
            if (scale < mMinScale)
                return "封面宽高比不低于1:1";

            return null;
        }

        public static String multiple(String path) {
            // 封面图片最小像素
            final int mMinSize = 600;
            // 封面图片最小宽高比例
            final float mMinScale = 1f / 2f;
            // 封面图片最大宽高比例
            final float mMaxScale = 3f / 4f;

            BitmapFactory.Options options = getOptions(path);
            if (options == null)
                return "图片解码失败";

            if (Math.min(options.outHeight, options.outWidth) < mMinSize)
                return "图片最小边不少于600像素";

            float scale = (float) options.outWidth / (float) options.outHeight;
            if (scale > mMaxScale)
                return "图片宽高比不超过3:4";
            if (scale < mMinScale)
                return "图片宽高比不低于1:2";

            return null;
        }
    }

}
