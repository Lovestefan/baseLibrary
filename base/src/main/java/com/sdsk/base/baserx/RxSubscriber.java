package com.sdsk.base.baserx;

import android.util.Log;

import com.google.gson.JsonSyntaxException;
import com.sdsk.base.base.IView;
import com.sdsk.base.baseapp.BaseApplication;
import com.sdsk.base.commonutils.NetWorkUtils;

import rx.Subscriber;


/**
 * des:订阅封装
 * Created by xsf
 * on 2016.09.10:16
 */

public abstract class RxSubscriber<T> extends Subscriber<T> {

    private String msg;
    private IView iView;

    public RxSubscriber() {

    }

    public RxSubscriber(IView iView) {
        this(iView, "正在加载中...");
    }

    public RxSubscriber(IView iView, String msg) {
        this.iView = iView;
        this.msg = msg;
    }

    @Override
    public void onCompleted() {
        if (iView != null) {
            iView.dismissLoading();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (iView != null) {
            try {
                if (msg != null) {
                    iView.showLoading(msg);
                } else {
                    iView.showLoading("正在加载中...");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onNext(T t) {
        _onNext(t);
    }

    @Override
    public void onError(Throwable e) {

        String code = "";

        if (iView != null) {
            iView.dismissLoading();
        }
        e.printStackTrace();
        String message = "";
        //网络
        if (!NetWorkUtils.isNetConnected(BaseApplication.getAppContext())) {
            message = "网络未连接，请检查后再试";
        }
        //服务器
        else if (e instanceof ServerException) {
            message = e.getMessage();
            code = ((ServerException) e).code;
        } else if (e instanceof JsonSyntaxException) {
            Log.e("error", "未对结果进行正确封装，请检查");
            message = "结果未正确封装，请稍后重试";
        }
        //其它
        else {
            message = "请求出错，请稍后重试";
            Log.e("错误:", e.getMessage() + ":\n" + e.getStackTrace()[0]);
        }
        _onError(code, message);
    }

    protected abstract void _onNext(T t);

    protected abstract void _onError(String code, String message);

}
