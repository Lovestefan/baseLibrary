package com.sdsk.base.app

import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.StrictMode
import com.alibaba.android.arouter.launcher.ARouter
import com.sdsk.base.BuildConfig
import com.sdsk.base.baseapp.BaseApplication
import com.sdsk.base.di.component.AppComponent
import com.zx.zxutils.ZXApp
import com.zx.zxutils.util.ZXSharedPrefUtil
import me.jessyan.autosize.AutoSizeConfig
import rx.Subscription
import java.util.ArrayList

/**
 * create by 96212 on 2021/3/29.
 * Email 962123525@qq.com
 * desc
 */
open class CommonApplication : BaseApplication(){
    companion object {
        lateinit var instance: CommonApplication

//        fun getInstance(): CommonApplication {
//            return
//        }
    }


    lateinit var mSharedPrefUtil: ZXSharedPrefUtil

    lateinit var mContest: Context

    lateinit var component: AppComponent
    private var fontScale: Float = 1.0f
    override fun onCreate() {
        super.onCreate()
        ZXApp.init(this, !BuildConfig.RELEASE)
        //配置Bugly
        if (BuildConfig.RELEASE) {
            // CrashReport.initCrashReport(this, Constants.BUGLEID, false)

        } else {
            ARouter.openLog()//打印日志
            ARouter.openDebug()//开启调试模式
        }
        ARouter.init(this)
        mSharedPrefUtil = ZXSharedPrefUtil()
        instance = this
        mContest = this
        initAppDelegate(this)
        component = appComponent

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val builder = StrictMode.VmPolicy.Builder()
            StrictMode.setVmPolicy(builder.build())
        }
        configUnits()
    }

    var observable: Subscription? = null


    private val activityList = ArrayList<Activity>()


    fun recreateView() {
        activityList.forEach {
            it.recreate()
        }
    }
    /**
     * 屏幕适配
     */
    private fun configUnits() {
        AutoSizeConfig.getInstance()
    }

    // 添加Activity到容器中
    override fun addActivity(activity: Activity) {
        if (activityList.size > 0 && activityList[activityList.size - 1].javaClass == activity.javaClass) {
            return
        }
        activityList.add(activity)
    }

    // 遍历所有Activity并finish
    override fun exit() {
        for (activity in activityList) {
            activity.finish()
        }
        //		App.getInstance().destroyMap();
//        System.exit(0)
    }


    //移出栈
    override fun remove(t: Class<out Activity>) {
        for (i in activityList.indices.reversed()) {
            if (activityList[i].javaClass == t) {
                activityList.removeAt(i)
                return
            }
        }
    }

    // 遍历所有Activity并finish

    fun finishAll() {
        for (activity in activityList) {
            activity.finish()
        }
        //		App.getInstance().destroyMap();
    }

    override fun haveActivity(t: Class<out Activity>): Boolean {
        for (activity in activityList) {
            if (activity.javaClass == t) {
                return true
            }
        }
        return false
    }

    override fun clearActivityList() {
        for (activity in activityList) {
            activity.finish()
        }
        activityList.clear()
    }


    override fun getActivityList(): ArrayList<Activity> {
        return activityList
    }

    fun setFontScale(fontScale: Float) {
        this.fontScale = fontScale
    }

    fun getFontScale(): Float {
        return this.fontScale
    }
}