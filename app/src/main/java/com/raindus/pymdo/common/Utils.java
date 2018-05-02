package com.raindus.pymdo.common;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import com.raindus.pymdo.PymdoApplication;

/**
 * Created by Raindus on 2018/4/29.
 */

public class Utils {

    /**
     * dp转px
     *
     * @param dpValue dp
     * @return px
     */
    public static int dipToPx(float dpValue) {
        final float scale = PymdoApplication.get().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static String getStringByKey(String s, boolean isResId) {
        if (isResId)
            return PymdoApplication.get().getResources().getString(Integer.parseInt(s));
        else
            return s;
    }

    // 将图片压缩至一定大小
    public static Bitmap scaleBitmap(String path, boolean isResId, int maxSize) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        if (isResId)
            BitmapFactory.decodeResource(PymdoApplication.get().getResources(), Integer.parseInt(path), options);
        else
            BitmapFactory.decodeFile(path, options);
        if (options.outWidth != -1 && options.outHeight != -1) {
            int size = options.outHeight * options.outWidth;
            if (size > maxSize) {
                options.inSampleSize = size / maxSize + 1;
            }
            options.inJustDecodeBounds = false;
            if (isResId)
                return BitmapFactory.decodeResource(PymdoApplication.get().getResources(), Integer.parseInt(path), options);
            else
                return BitmapFactory.decodeFile(path, options);
        }
        return null;
    }

    @TargetApi(19)
    public static String handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(PymdoApplication.get(), uri)) {
            // 如果是document类型uri，则通过document id 处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];// 解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // 如果不是document类型uri，则通过普通方式处理
            imagePath = getImagePath(uri, null);
        } else {
            imagePath = uri.getPath();
        }
        return imagePath;
    }

    private static String getImagePath(Uri uri, String selection) {
        String path = null;
        // 通过uri和selection获取真实的图片路径
        Cursor cursor = PymdoApplication.get().getContentResolver().query(uri,
                null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }
}
