package com.sdsk.base.baserx;

import com.sdsk.base.base.IView;
import com.sdsk.base.base.RxBaseActivity;
import com.sdsk.base.base.RxBaseFragment;
import com.sdsk.base.basebean.Base2Respose;
import com.trello.rxlifecycle.LifecycleTransformer;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * des:对服务器返回数据成功和失败处理
 * Created by xsf
 * on 2016.09.9:59
 */

/**************使用例子******************/
/*_apiService.login(mobile, verifyCode)
        .compose(RxSchedulersHelper.io_main())
        .compose(RxResultHelper.handleResult())
        .//省略*/

public class Rx2Helper {
    /**
     * 对服务器返回数据进行预处理
     *
     * @param <T>
     * @return
     */
    public static <T> Observable.Transformer<Base2Respose<T>, T> handleResult() {
        return tObservable -> tObservable.flatMap(new Func1<Base2Respose<T>, Observable<T>>() {
            @Override
            public Observable<T> call(Base2Respose<T> result) {
                if (result.success()) {
                    return createData(result.data);
                } else {
                    return Observable.error(new ServerException("" + result.meta.code, result.meta.message));
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 创建成功的数据
     *
     * @param data
     * @param <T>
     * @return
     */
    private static <T> Observable<T> createData(final T data) {
        return Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {
                try {
                    subscriber.onNext(data);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });

    }

    //    public static <T> Observable.Transformer<T, T> applySchedulers(final IView view) {
    //        return new Observable.Transformer<T, T>() {
    //            @Override
    //            public Observable<T> call(Observable<T> observable) {
    //                return observable.subscribeOn(Schedulers.io())
    //                        .doOnSubscribe(new Action0() {
    //                            @Override
    //                            public void call() {//显示进度条
    //                                view.showLoading("请稍后...");
    //                            }
    //                        })
    //                        .subscribeOn(AndroidSchedulers.mainThread())
    //                        .observeOn(AndroidSchedulers.mainThread())
    //                        .doAfterTerminate(new Action0() {
    //                            @Override
    //                            public void call() {
    //                                view.stopLoading();//隐藏进度条
    //                            }
    //                        }).compose(RxHelper.<T>bindToLifecycle(view));
    //            }
    //        };
    //    }


    public static <T> LifecycleTransformer<T> bindToLifecycle(IView view) {
        if (view instanceof RxBaseActivity) {
            return ((RxBaseActivity) view).<T>bindToLifecycle();
        } else if (view instanceof RxBaseFragment) {
            return ((RxBaseFragment) view).<T>bindToLifecycle();
        } else {
            throw new IllegalArgumentException("view isn't activity or fragment");
        }

    }
}
