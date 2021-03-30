package com.sdsk.base.base.view

import android.os.Bundle
import android.os.Handler
import com.sdsk.base.base.BaseModel
import com.sdsk.base.base.BasePresenter
import com.sdsk.base.base.RxBaseFragment
import com.zx.zxutils.util.ZXSharedPrefUtil

/**
 * create by 96212 on 2020/6/22.
 * Email 962123525@qq.com
 * desc
 */
abstract class BaseFragment<T : BasePresenter<*, *>, E : BaseModel> : RxBaseFragment<T, E>() {
    var mSharedPrefUtil = ZXSharedPrefUtil()
    var mHandler = Handler()

    override fun handleError(code: String?, message: String) {
        (activity as BaseActivity<*, *>).handleError(code, message)
    }

    override fun showLoading(message: String) {
        (activity as BaseActivity<*, *>).showLoading(message)
    }

    override fun dismissLoading() {
        (activity as BaseActivity<*, *>).dismissLoading()
        //        ZXDialogUtil.dismissLoadingDialog();
    }

    override fun showLoading(message: String, progress: Int) {
        (activity as BaseActivity<*, *>).showLoading(message, progress)
    }

    override fun showToast(message: String) {
        (activity as BaseActivity<*, *>).showToast(message)
    }

    fun getPermission(permissionArray: Array<String>, permessionBack: () -> Unit) {
        (activity as BaseActivity<*, *>).getPermission(permissionArray, permessionBack)
    }

    override fun initView(savedInstanceState: Bundle?) {
        onViewListener()
    }

    abstract fun onViewListener()
}