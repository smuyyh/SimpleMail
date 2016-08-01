package com.yuyh.library.simplemail.bean;

import java.io.File;
import java.io.Serializable;

/**
 * @author yuyh.
 * @date 2016/8/1.
 */
public class Attachment implements Serializable {

    public String filePath;
    public String fileName;
    public long fileSize;

    public Attachment() {
        super();
    }

    public Attachment(String filePath, String fileName, long fileSize) {
        super();
        this.filePath = filePath;
        this.fileName = fileName;
        this.fileSize = fileSize;
    }

    public static String convertStorage(long size) {
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;

        if (size >= gb) {
            return String.format("%.1f GB", (float) size / gb);
        } else if (size >= mb) {
            float f = (float) size / mb;
            return String.format(f > 100 ? "%.0f MB" : "%.1f MB", f);
        } else if (size >= kb) {
            float f = (float) size / kb;
            return String.format(f > 100 ? "%.0f KB" : "%.1f KB", f);
        } else
            return String.format("%d B", size);
    }

    /**
     * 获取完整的目录名字
     *
     * @param filepath
     * @return
     */
    public static String getNameFromFilepath(String filepath) {
        int pos = filepath.lastIndexOf('/');
        if (pos != -1) {
            return filepath.substring(pos + 1);
        }
        return "";
    }


    /**
     * 获取附件
     *
     * @param filePath
     * @return
     */
    public static Attachment GetFileInfo(String filePath) {
        File file = new File(filePath);
        if (!file.exists())
            return null;
        Attachment fileInfo = new Attachment();
        fileInfo.fileName = getNameFromFilepath(filePath);
        fileInfo.filePath = filePath;
        fileInfo.fileSize = file.length();
        return fileInfo;
    }
}
