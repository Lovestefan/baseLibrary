package com.sdsk.base.di.interfacea;

import okhttp3.HttpUrl;

public class DefaultUrlParser implements UrlParse {
    private UrlParse mDomainUrlParser;
    @Override
    public HttpUrl parseUrl(HttpUrl domainUrl, HttpUrl url) {
        return mDomainUrlParser.parseUrl(domainUrl, url);
    }
}
