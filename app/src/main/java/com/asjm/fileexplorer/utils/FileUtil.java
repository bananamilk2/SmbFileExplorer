package com.asjm.fileexplorer.utils;

public class FileUtil {
    public static String formatFileSize(long data) {
        if (data > 0) {
            double size = (double) data;

            double kiloByte = size / 1024;
            if (kiloByte < 1 && kiloByte > 0) {
                //不足1K
                return String.format("%.2fByte", size);
            }
            double megaByte = kiloByte / 1024;
            if (megaByte < 1) {
                //不足1M
                return String.format("%.2fK", kiloByte);
            }

            double gigaByte = megaByte / 1024;
            if (gigaByte < 1) {
                //不足1G
                return String.format("%.2fM", megaByte);
            }

            double teraByte = gigaByte / 1024;
            if (teraByte < 1) {
                //不足1T
                return String.format("%.2fG", gigaByte);
            }

            return String.format("%.2fT", teraByte);
        }

        return "0K";
    }
}