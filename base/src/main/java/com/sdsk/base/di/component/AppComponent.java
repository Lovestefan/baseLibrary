package com.sdsk.base.di.component;

import android.app.Application;

import com.google.gson.Gson;
import com.sdsk.base.baseapp.AppManager;
import com.sdsk.base.di.module.AppModule;
import com.sdsk.base.di.module.ClientModule;
import com.sdsk.base.di.module.GlobalConfigModule;
import com.sdsk.base.http.AppDelegate;
import com.sdsk.base.integration.IRepositoryManager;

import java.io.File;

import javax.inject.Singleton;

import dagger.Component;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import okhttp3.OkHttpClient;

/**
 * Created by jess on 8/4/16.
 */
@Singleton
@Component(modules = {AppModule.class, ClientModule.class, GlobalConfigModule.class})
public interface AppComponent {
    Application Application();

    //用于管理网络请求层,以及数据缓存层
    IRepositoryManager repositoryManager();

    //Rxjava错误处理管理类
    RxErrorHandler rxErrorHandler();

    OkHttpClient okHttpClient();

    //gson
    Gson gson();

    //缓存文件根目录(RxCache和Glide的的缓存都已经作为子文件夹在这个目录里),应该将所有缓存放到这个根目录里,便于管理和清理,可在GlobeConfigModule里配置
    File cacheFile();

    //用于管理所有activity
    AppManager appManager();

    void inject(AppDelegate delegate);
}
