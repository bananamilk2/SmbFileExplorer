package com.asjm.fileexplorer.http;

import com.asjm.fileexplorer.BuildConfig;
import com.asjm.fileexplorer.utils.IOUtil;
import com.asjm.fileexplorer.utils.SmbUtils;
import com.asjm.lib.util.ALog;
import com.google.common.collect.Lists;
import com.google.common.io.ByteStreams;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import jcifs.smb.SmbFile;

public class HttpServer extends NanoHTTPD {

    public static final int PORT = 23333;
    public static final String URI_PREFIX = "/stream/";

    public HttpServer() {
        super(BuildConfig.DEBUG ? null : "127.0.0.1", PORT);
        setAsyncRunner(new ThreadPoolRunner());
    }

    @Override
    public Response serve(IHTTPSession session) {
        ALog.getInstance().d("server: " + session.getUri());
        Method method = session.getMethod();
        if (method != Method.GET)
            return getForbiddenResponse();
        return handleStream(session.getUri(), session);
    }

    private Response handleStream(String uri, IHTTPSession session) {
        ALog.getInstance().d("handleStream: " + uri);
        Response response = null;
        try {
            SmbFile smbFile = null;
            smbFile = new SmbFile(uri);
            if (smbFile.isFile()) {
                String contentType = smbFile.getContentType();
                if (contentType == null)
                    contentType = SmbUtils.getMineType(uri);
                ALog.getInstance().d("mime type = " + contentType);
                response = convertStream(session.getHeaders(), smbFile, contentType);
            }
        } catch (Exception e) {
            ALog.getInstance().e(e.toString());
        }
        return response;
    }

    private Response convertStream(Map<String, String> headers, SmbFile file, String contentType) {
        Response res;
        boolean success = false;
        InputStream fis = null;
        try {
            String etag = Integer.toHexString((file.getCanonicalPath() + file.lastModified() + "" + file.length()).hashCode());
            ALog.getInstance().d(etag);
            long startFrom = 0;
            long endAt = -1;
            Set<String> strings = headers.keySet();
            for (String key : strings)
                ALog.getInstance().d(key + " = " + headers.get(key));
            String range = headers.get("range");
            if (range != null) {
                if (range.startsWith("bytes=")) {
                    range = range.substring("bytes=".length());
                    int minus = range.indexOf('-');
                    try {
                        if (minus > 0) {
                            startFrom = Long.parseLong(range.substring(0, minus));
                            endAt = Long.parseLong(range.substring(minus + 1));
                        }
                    } catch (Exception e) {
                        ALog.getInstance().e(e.toString());
                    }
                }
            }
            String ifRange = headers.get("if-range");
            boolean headerIfRangeMissingOrMatching = (ifRange == null || etag.equals(ifRange));
            String ifNoneMatch = headers.get("if-none-match");
            boolean headerIfNoneMatchPresentAndMatching = ifNoneMatch != null && (ifNoneMatch.equals("*") || ifNoneMatch.equals(etag));
            long fileLen = file.length();
            fis = file.getInputStream();
            if (headerIfRangeMissingOrMatching && range != null && startFrom >= 0 && startFrom < fileLen) {
                if (headerIfNoneMatchPresentAndMatching) {
                    res = newFixedLengthResponse(Response.Status.NOT_MODIFIED, contentType, "");
                    res.addHeader("ETag", etag);
                } else {
                    if (endAt < 0) {
                        endAt = fileLen - 1;
                    }
                    long newLen = endAt - startFrom + 1;
                    if (newLen < 0) {
                        newLen = 0;
                    }
                    ByteStreams.skipFully(fis, startFrom);
                    res = newFixedLengthResponse(Response.Status.PARTIAL_CONTENT, contentType, fis, newLen);
                    res.addHeader("Accept-Ranges", "bytes");
                    res.addHeader("Content-Length", "" + newLen);
                    res.addHeader("Content-Range", "bytes " + startFrom + "-" + endAt + "/" + fileLen);
                    res.addHeader("ETag", etag);
                }
            } else {
                if (headerIfRangeMissingOrMatching && range != null && startFrom >= fileLen) {
                    res = newFixedLengthResponse(Response.Status.RANGE_NOT_SATISFIABLE, NanoHTTPD.MIME_PLAINTEXT, "");
                    res.addHeader("Content-Range", "byte */*" + fileLen);
                    res.addHeader("ETag", etag);
                } else if (range == null && headerIfNoneMatchPresentAndMatching) {
                    res = newFixedLengthResponse(Response.Status.NOT_MODIFIED, contentType, "");
                    res.addHeader("ETag", etag);
                } else if (!headerIfRangeMissingOrMatching && headerIfNoneMatchPresentAndMatching) {
                    res = newFixedLengthResponse(Response.Status.NOT_MODIFIED, contentType, "");
                    res.addHeader("ETag", etag);
                } else {
                    res = newFixedLengthResponse(Response.Status.OK, contentType, fis, fileLen);
                    res.addHeader("Content-Length", Long.toString(fileLen));
                    res.addHeader("ETag", etag);
                }
            }
            success = true;
        } catch (Exception e) {
            ALog.getInstance().e(e.toString());
            res = getForbiddenResponse();
        }finally {
            if(!success && fis != null){
                IOUtil.closeQuietly(fis);
            }
        }
        return res;
    }

    protected Response getForbiddenResponse() {
        return newFixedLengthResponse(Response.Status.FORBIDDEN, MIME_PLAINTEXT, "Forbidden");
    }

    public static class ThreadPoolRunner implements AsyncRunner {
        private final ExecutorService mExecutor = Executors.newCachedThreadPool();
        private final List<ClientHandler> running = Collections.synchronizedList(new ArrayList<ClientHandler>());

        @Override
        public void closeAll() {
            // copy of the list for concurrency
            mExecutor.shutdown();
            for (ClientHandler clientHandler : Lists.newArrayList(running)) {
                clientHandler.close();
            }
        }

        @Override
        public void closed(ClientHandler clientHandler) {
            running.remove(clientHandler);
        }

        @Override
        public void exec(ClientHandler clientHandler) {
            running.add(clientHandler);
            mExecutor.submit(clientHandler);
        }
    }
}
