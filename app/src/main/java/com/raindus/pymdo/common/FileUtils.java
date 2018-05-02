package com.raindus.pymdo.common;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * Created by Raindus on 2018/5/1.
 */

public class FileUtils {

    public static final String DIR_ROOT = Environment.getExternalStorageDirectory().getPath() + "/Pymdo";
    public static final String DIR_PERSONAL = DIR_ROOT + "/personal";
    public static final String DIR_THEME = DIR_ROOT + "/theme";

    // personal
    public static final String PERSONAL_COVER_PATH = DIR_PERSONAL + "/cover";
    public static final String PERSONAL_BANNER_PATH = DIR_PERSONAL + "/banner";
    public static final String PERSONAL_REPORT_PATH = DIR_PERSONAL + "/report";

    // 创建相应文件夹并创建.nomedia文件
    public static void createDir(String path) {
        File dir = new File(path);
        if (!dir.exists())
            dir.mkdirs();
        if (path != DIR_ROOT) {
            File noMedia = new File(dir, ".nomedia");
            if (!noMedia.exists()) {
                try {
                    noMedia.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // 复制图片 src -> dst
    public static boolean copyPicture(String src, String dst) {
        File srcFile = new File(src);
        if (!srcFile.exists())
            return false;

        File dstFile = new File(dst);
        if (dstFile.exists())
            dstFile.delete();

        FileChannel inputChannel = null;
        FileChannel outputChannel = null;

        try {
            inputChannel = new FileInputStream(srcFile).getChannel();
            outputChannel = new FileOutputStream(dstFile).getChannel();
            outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            return false;
        } finally {
            try {
                if (inputChannel != null)
                    inputChannel.close();
                if (outputChannel != null)
                    outputChannel.close();
            } catch (IOException e) {
                return false;
            }
        }
        return true;
    }
}
