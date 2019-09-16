package com.xwc.dreamkotlin.base

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.OnTitleBarListener
import com.hjq.bar.TitleBar
import com.xwc.dreamkotlin.R

/**
 * author: xuweichao
 * date: 2019/9/2 14:50
 * desc: 活动基类
 */
abstract class BaseActivity : AppCompatActivity() {

    protected var isDestroy = false
    private var loadingDialog: AlertDialog? = null
    protected var isLoading = false
    protected var mActivity: AppCompatActivity? = null
    protected var mContext: Context? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(setLayout())

        // 设置沉浸式
        ImmersionBar.with(this)
            .statusBarDarkFont(false) // 状态栏字体颜色为黑色
            .keyboardEnable(true) // 解决软键盘与底部输入框冲突问题，默认为false
            .init()

        // 标题栏id为tool_bar时，将不会被挤进状态栏
        ImmersionBar.setTitleBar(this, findViewById(R.id.tool_bar))

        initToolbar()

        mActivity = this
        mContext = this
        initViewAndData()
    }

    open fun initToolbar() {
        findViewById<TitleBar>(R.id.tool_bar).setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(v: View?) {
                finish()
            }

            override fun onTitleClick(v: View?) {
                Toast.makeText(this@BaseActivity, "你点到我了，开心吗？", Toast.LENGTH_SHORT).show()
            }

            override fun onRightClick(v: View?) {
                showLoading()
            }
        })
    }

    protected fun showLoading(cancelable: Boolean = true) {
        if (loadingDialog == null) {
            loadingDialog = AlertDialog.Builder(this, R.style.custom_dialog)
                .setView(R.layout.dialog_loading)
                .setOnDismissListener { isLoading = false }
                .setCancelable(cancelable)
                .create()
        }
        if (!loadingDialog!!.isShowing) {
            loadingDialog?.show()
            isLoading = true
        }
    }

    protected fun dismissDialog() {
        if (loadingDialog != null && loadingDialog!!.isShowing) {
            loadingDialog!!.dismiss()
            isLoading = false
        }
    }

    protected abstract fun setLayout(): Int
    protected abstract fun initViewAndData()

    override fun onDestroy() {
        super.onDestroy()
        isDestroy = true
    }
}