package com.sdsk.base.base.view

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import com.sdsk.base.R
import com.sdsk.base.base.BaseModel
import com.sdsk.base.base.BasePresenter
import kotlinx.android.synthetic.main.activity_lt_actionbar.view.*
import kotlinx.android.synthetic.main.layout_title_bar.*
import kotlinx.android.synthetic.main.layout_title_bar.view.*

/**
 * create by 96212 on 2020/6/22.
 * Email 962123525@qq.com
 * desc
 */
abstract class ActionBarActivity<T: BasePresenter<*, *>,E: BaseModel>: BaseActivity<T, E>(){
    private var rootView: View? = null
    private var toolbar: Toolbar? = null
    private var barCenterTitle: String = ""
    private var tvBarCenterTitle: TextView? = null
    private var mLeftMenuListener: (MenuItem) -> Unit = {}
    private var mRightMenuListener: (MenuItem) -> Unit = {}
    private var mRightMenuListener2: (MenuItem) -> Unit = {}

    private var rightActionMenu: Menu? = null
    private var rightResId = 0
    private var leftRestId = 0
    private var resTitleId = 0
    private var rightResId2 = 0
    private lateinit var menuItem: MenuItem
    private lateinit var rightMenuItem: MenuItem
    private lateinit var rightMenuItem2: MenuItem

    override fun setContentView(layoutResID: Int) {
        setTheme(R.style.TransTheme)
        rootView = layoutInflater.inflate(R.layout.activity_lt_actionbar, null)
        val contentView = layoutInflater.inflate(layoutResID, null)
        rootView!!.content.addView(contentView, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))

        super.setContentView(rootView)
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        toolbar = rootView!!.actionBar.toolBar
        tvBarCenterTitle = rootView!!.actionBar.tvBarCenterTitle
    }

    /**
     * 初始化ToolBar
     */
    private fun initActionBar() {
        rootView!!.actionBar.visibility = ViewGroup.VISIBLE
        toolbar!!.apply {
            setTitle("")
        }
        setSupportActionBar(toolbar)
        setBarLayoutParams()
        toolbar!!.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.rightMenu -> {
                    mRightMenuListener(it)
                    return@setOnMenuItemClickListener true
                }
                R.id.rightMenu2 -> {
                    mRightMenuListener2(it)
                    return@setOnMenuItemClickListener true
                }
                else -> {
                    return@setOnMenuItemClickListener false
                }
            }
        }
        initLeftMenu()
    }

    /**
     * 初始化左边menu
     */
    private fun initLeftMenu() {
        menuInflater.inflate(R.menu.menu_toolbar_left, actionMenu.menu)
        //actionMenu点击事件
        actionMenu.setOnMenuItemClickListener {
            if (leftRestId != 0) {
                mLeftMenuListener(it)
            } else {
                if (it.itemId == R.id.leftMenu) {
                    if (mContext is Activity) this.finish()
                    return@setOnMenuItemClickListener true
                }
            }
            return@setOnMenuItemClickListener false
        }
        menuItem = actionMenu.menu.findItem(R.id.actionView)
        menuItem.setActionView(R.layout.layout_menu_text)
        menuItem.actionView.findViewById<TextView>(R.id.tvMenu).setOnClickListener {
            if (mContext is Activity) this.finish()
        }
        menuItem.isVisible = false
    }

    /**
     * 设置Toolbar 标题
     */
    fun setToolBarTitle(toolbarTitle: String) {
        toolbar!!.setTitle(toolbarTitle)
    }

    fun setToolBarTextAppearance(resId: Int) {
        toolbar!!.setTitleTextAppearance(this, resId)
    }

    /**
     * 设置ToolBar 标题颜色
     */
    fun setToolBarTitleColor(color: Int) {
        toolbar!!.setTitleTextColor(color)
    }

    /**
     * 是否显示自定义actionView
     */
    fun isVisible(isActionView: Boolean) {
        menuItem.isVisible = isActionView
    }

    /**
     * 设置自定义控件布局
     */
    private fun setBarLayoutParams() {
        //设置自定义Title 居中
        val rlBar = toolbar!!.tvBarCenterTitle
        val relativeLayout = rlBar.layoutParams as Toolbar.LayoutParams
        relativeLayout.gravity = Gravity.CENTER
        rlBar.layoutParams = relativeLayout
    }

    /**
     * 设置标题
     */
    fun setCenterTitle(barCenterTitle: String) {
        this.barCenterTitle = barCenterTitle
        tvBarCenterTitle!!.setText(this.barCenterTitle)
        initActionBar()
    }

    /**
     * 设置标题颜色
     */
    fun setCenterTitleColor(color: Int) {
        tvBarCenterTitle!!.setTextColor(color)
    }

    /**
     * 设置标题字体大小
     */
    fun setCenterTitleSize(textSize: Float) {
        tvBarCenterTitle!!.setTextSize(textSize)
    }

    /**
     * 设置标题相关属性
     * @param resId 属性文件id
     */
    @RequiresApi(Build.VERSION_CODES.M)
    fun setCenterTitleAppearance(resId: Int) {
        tvBarCenterTitle!!.setTextAppearance(resId)
    }

    /**
     * 设置右侧菜单资源
     */
    fun addRightMenu(resId: Int, resTitleId: Int) {
        this.rightResId = resId
        this.resTitleId = resTitleId
    }

    fun addRightMenuText(resTitleId: Int) {
        rightMenuItem.setTitle(resTitleId)
    }

    fun addRightMenu2(resId: Int) {
        this.rightResId2 = resId
    }

    fun addRightMenuListener(mRightMenuListener: (MenuItem) -> Unit) {
        this.mRightMenuListener = mRightMenuListener
    }

    fun addRightMenu2Listener(mRightMenuListener2: (MenuItem) -> Unit = {}) {
        this.mRightMenuListener2 = mRightMenuListener2
    }

    /**
     * 设置左侧菜单资源
     */
    fun addLeftMenu(resId: Int, mLeftMenuListener: (MenuItem) -> Unit) {
        actionMenu.menu.findItem(R.id.leftMenu).apply {
            if (resId==0){
                isVisible = false
            }else{
                setIcon(resId)
            }
        }
        this.mLeftMenuListener = mLeftMenuListener
        this.leftRestId = resId
    }

    /**
     * 创建右侧menu菜单
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar_right, menu)
        menu?.apply {
            rightMenuItem = findItem(R.id.rightMenu).apply {
                if (resTitleId!=0){
                    setTitle(resTitleId)
                }
                if (rightResId!=0){
                    setIcon(rightResId)
                }
            }
            rightMenuItem2 = findItem(R.id.rightMenu2).apply {
                if (rightResId2 != 0) {
                    setTitle(rightResId2)
                }else{
                    isVisible = false
                }
            }
        }
        return super.onCreateOptionsMenu(menu)
    }
}