package com.asjm.fileexplorer.utils;

import android.webkit.MimeTypeMap;

import com.asjm.lib.util.ALog;
import com.google.common.io.Files;

import java.io.Closeable;

public class IOUtil {

    public static boolean isVideoFile(String fileName) {
        final String mimeType = MimeTypeMap.getSingleton()
                .getMimeTypeFromExtension(Files.getFileExtension(fileName));

        return mimeType != null && mimeType.startsWith("video/");
    }

    public static void closeQuietly(Closeable io) {
        try {
            io.close();
        } catch (Exception e) {
            ALog.getInstance().e(e);
        }
    }
}
