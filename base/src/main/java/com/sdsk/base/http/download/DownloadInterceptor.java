package com.sdsk.base.http.download;



import com.sdsk.base.http.unzip.HttpProgressListener;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * 成功回调处理
 * Created by WZG on 2016/10/20.
 */
public class DownloadInterceptor implements Interceptor {

    private HttpProgressListener listener;

    public DownloadInterceptor(HttpProgressListener listener) {
        this.listener = listener;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());
        return originalResponse.newBuilder()
                .header("Accept-Encoding", "identity")
                .body(new DownloadResponseBody(originalResponse.body(), listener))
                .build();
    }
}
