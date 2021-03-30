package com.sdsk.base.di.interfacea;

import android.text.TextUtils;
import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.HttpUrl;
import okhttp3.Request;

public class Util {
    private static final String DOMAIN_NAME = "Domain-Name";
    private static final String GLOBAL_DOMAIN_NAME = "retrofitUrlManager.globalDomainName";
    //用来存储BaseUrl
    private static final Map<String, HttpUrl> mDomainNameHub = new HashMap<>();
    private static UrlParse mUrlParser;
    /**
     * 对Request进行处理，执行切换BaseUrl的相关逻辑
     * @param request
     * @return
     */
    public static Request processRequest(Request request){
        if (request==null) {
            return request;
        }
        Request.Builder newBuilder = request.newBuilder();
        Log.e("request",request.url().toString());
        HttpUrl httpUrl;
        String domainName = obtainDomainNameFromHeaders(request);
        if (!TextUtils.isEmpty(domainName)){
            httpUrl = fetchDomain(domainName);
            newBuilder.removeHeader(DOMAIN_NAME);
        }else {
            httpUrl = getGlobalDimain();
        }
        if (httpUrl!=null){
            HttpUrl newUrl = mUrlParser.parseUrl(httpUrl,request.url());
            Log.e("newUrl","The new url is"+newUrl.toString()+" old url is "+request.url().toString());
            return newBuilder.url(newUrl).build();
        }
        return newBuilder.build();
    }


    /**
     * 存放 Domain(BaseUrl) 的映射关系
     *
     * @param domainName
     * @param domainUrl
     */
    public static void putDomain(String domainName, String domainUrl) {

        synchronized (mDomainNameHub) {
            mDomainNameHub.put(domainName,checkUrl(domainUrl));
        }
    }
    static HttpUrl checkUrl(String url) {
        HttpUrl parseUrl = HttpUrl.parse(url);
        if (null == parseUrl) {
            throw new NullPointerException(url);
        } else {
            return parseUrl;
        }
    }
    /**
     * 取出domainName所对应的Url(BaseUrl)
     * @param domainName
     * @return
     */
    public static synchronized  HttpUrl fetchDomain(@NotNull String domainName){
        return mDomainNameHub.get(domainName);
    }
    /**
     * 获取全局BaseUrl
     * @return
     */
    public static synchronized HttpUrl getGlobalDimain(){
        return mDomainNameHub.get(GLOBAL_DOMAIN_NAME);
    }
    /**
     * 从 {@link Request#header(String)} 中取出 DomainName
     *
     * @param request {@link Request}
     * @return DomainName
     */
    private static String obtainDomainNameFromHeaders(Request request) {
        List<String> headers = request.headers(DOMAIN_NAME);
        if (headers == null || headers.size() == 0)
            return null;
        if (headers.size() > 1)
            throw new IllegalArgumentException("Only one Domain-Name in the headers");
        return request.header(DOMAIN_NAME);
    }
}
