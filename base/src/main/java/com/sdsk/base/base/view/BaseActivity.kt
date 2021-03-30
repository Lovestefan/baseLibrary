package com.sdsk.base.base.view

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.os.Bundle
import android.os.Handler
import androidx.core.content.ContextCompat
import com.sdsk.base.BuildConfig
import com.sdsk.base.R
import com.sdsk.base.app.CommonApplication
import com.sdsk.base.base.BaseModel
import com.sdsk.base.base.BasePresenter
import com.sdsk.base.base.RxBaseActivity
import com.sdsk.base.widget.dialog.QMUIProgressBar
import com.sdsk.base.widget.dialog.QMUITipDialog
import com.zx.zxutils.util.*
import com.zx.zxutils.views.SwipeBack.ZXSwipeBackHelper
import com.zx.zxutils.views.ZXStatusBarCompat
import kotlinx.android.synthetic.main.layout_progress_bar.*

/**
 * create by 96212 on 2020/6/22.
 * Email 962123525@qq.com
 * desc
 */
abstract class BaseActivity<T : BasePresenter<*, *>, E : BaseModel> : RxBaseActivity<T, E>() {
    val mSharedPrefUtil = ZXSharedPrefUtil()
    val handler = Handler()
    var sIsLoginClear = false
    open var canSwipeBack = false
    private var defaultFontScale = 1.0f
    private lateinit var permessionBack: () -> Unit
    private lateinit var permissionArray: Array<String>
    private var tipDialog: QMUITipDialog? = null
    private var tipProgress: QMUIProgressBar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (canSwipeBack) ZXSwipeBackHelper.onCreate(this)
            .setSwipeBackEnable(true)
            .setSwipeRelateEnable(true)
        CommonApplication.instance.addActivity(this)
        ZXStatusBarCompat.setStatusBarDarkMode(this)
        ZXCrashUtil.init(BuildConfig.RELEASE) { t, e ->
            //showToast("出现未知问题，请稍后再试")
            //CrashReport.postCatchedException(e)
        }
    }

    /**
     * 判断当前activity是否在栈顶，避免重复处理
     */
    @SuppressLint("NewApi")
    private fun isTopActivity(): Boolean {
        var isTop = false
        val am = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val cn = am.getRunningTasks(1)[0].topActivity
        if (cn?.className?.contains(this.localClassName) == true) {
            isTop = true
        }
        return isTop
    }

    override fun showToast(message: String) {
        ZXToastUtil.showToast(message)
    }

    override fun showLoading(message: String) {
        if (tipDialog == null) {
            tipDialog = QMUITipDialog.Builder(this)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord(message)
                .create()
        }
        tipDialog?.apply {
            if (!isShowing){
                show()
            }
        }
    }

    override fun dismissLoading() {
        if (tipDialog != null) {
            if (tipDialog!!.isShowing) {
                try {
                    tipDialog!!.dismiss()

                } catch (e: Exception) {

                }
            }
        }
        tipDialog = null
    }

    override fun showLoading(message: String, progress: Int) {
        if (tipDialog == null) {
            tipDialog = QMUITipDialog.CustomBuilder(mContext)
                .setContent(R.layout.layout_progress_bar)
                .create()
        }
        if (!tipDialog!!.isShowing) {
            tipDialog!!.show()
        }
        tipDialog?.circleProgressBar?.apply {
            setTextSize(resources.getDimensionPixelOffset(R.dimen.sp_14))
            setTextColor(ContextCompat.getColor(mContext, R.color.white))
            setProgress(progress+1,false)
            setQMUIProgressBarTextGenerator { progressBar, value, maxValue ->
                return@setQMUIProgressBarTextGenerator "$value%"
            }
        }
    }

    override fun handleError(code: String?, message: String) {
        showToast(message)

    }

    override fun initView(savedInstanceState: Bundle?) {
        onViewListener()
    }

    abstract fun onViewListener()

    fun getPermission(permissionArray: Array<String>, permessionBack: () -> Unit) {
        this.permessionBack = permessionBack
        this.permissionArray = permissionArray
        if (!ZXPermissionUtil.checkPermissionsByArray(permissionArray)) {
            ZXPermissionUtil.requestPermissionsByArray(this)
        } else {
            this.permessionBack()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (ZXPermissionUtil.checkPermissionsByArray(permissionArray)) {
            permessionBack()
        } else {
            showToast("未获取到系统权限，请先在设置中开启相应权限！")
        }
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        if (canSwipeBack) ZXSwipeBackHelper.onPostCreate(this)
    }

    override fun onDestroy() {
        CommonApplication.instance.remove(this.javaClass)
        super.onDestroy()
//        if (canSwipeBack) ZXSwipeBackHelper.onDestroy(this)
    }

}