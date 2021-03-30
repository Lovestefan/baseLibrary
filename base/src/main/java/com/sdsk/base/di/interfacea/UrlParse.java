package com.sdsk.base.di.interfacea;

import okhttp3.HttpUrl;
import okhttp3.Request;

public interface UrlParse {
    /**
     * 用来替换 @{@link Request#url} 达到动态切换 URL
     *
     * @param domainUrl 用于替换的 URL 地址
     * @param url       旧 URL 地址
     * @return
     */
    HttpUrl parseUrl(HttpUrl domainUrl, HttpUrl url);
}
