package com.sdsk.base.base;

/**
 * des:baseview
 * Created by xsf
 * on 2016.07.11:53
 */
public interface IView {
    //    /*******内嵌加载*******/
    void showLoading(String message);

    void showLoading(String message, int progress);

    void dismissLoading();

    void showToast(String message);

    void handleError(String code, String message);

}
