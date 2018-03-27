//Copyright (c) 2017. 章钦豪. All rights reserved.
package game.shenle.com

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Paint
import android.os.Build
import android.os.Handler
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.Toast
import com.monke.mprogressbar.OnProgressListener
import game.shenle.com.common.RxBusTag
import game.shenle.com.common.bean.DownloadChapterBean
import game.shenle.com.common.bean.DownloadChapterListBean
import game.shenle.com.db.table.BookShelfBean
import game.shenle.com.view.ChapterListView
import game.shenle.com.view.contentswitchview.BookContentView
import game.shenle.com.view.contentswitchview.ContentSwitchView
import game.shenle.com.view.modialog.MoProgressHUD
import game.shenle.com.view.popupwindow.*
import game.shenle.com.viewmodel.ReadBookViewModel
import kotlinx.android.synthetic.main.activity_bookread.*
import lib.shenle.com.base.SLBaseActivity
import lib.shenle.com.utils.ActivityLifecycleHelper
import lib.shenle.com.utils.PremissionCheck
import lib.shenle.com.utils.RxBus
import lib.shenle.com.utils.UIUtils
import java.util.*

class ReadBookActivity : BaseActivity<ReadBookViewModel>() {
    companion object {
        fun goHere(bookShelfBean: BookShelfBean){
            val intent = Intent(ActivityLifecycleHelper.getLatestActivity(), ReadBookActivity::class.java)
            intent.putExtra("from", ReadBookViewModel.OPEN_FROM_APP)
            val key = System.currentTimeMillis().toString()
            intent.putExtra("data_key", key)
            try {
                BitIntentDataManager.getInstance().putData(key, bookShelfBean.clone())
            } catch (e: CloneNotSupportedException) {
                BitIntentDataManager.getInstance().putData(key, bookShelfBean)
                e.printStackTrace()
            }
            (ActivityLifecycleHelper.getLatestActivity() as SLBaseActivity).startActivityByAnim(intent, android.R.anim.fade_in, android.R.anim.fade_out)
        }
    }
    override fun getTNameClass(): Class<ReadBookViewModel> {
        return ReadBookViewModel::class.java
    }

    override fun initView() {
        setContentView(R.layout.activity_bookread)
        initData()
        bindView()
        bindEvent()
    }

    private var flContent: FrameLayout? = null

    //主菜单动画
    private var menuTopIn: Animation? = null
    private var menuTopOut: Animation? = null
    private var menuBottomIn: Animation? = null
    private var menuBottomOut: Animation? = null

    private var checkAddShelfPop: CheckAddShelfPop? = null
    private var windowLightPop: WindowLightPop? = null
    private var readBookMenuMorePop: ReadBookMenuMorePop? = null
    private var fontPop: FontPop? = null
    private var moreSettingPop: MoreSettingPop? = null
    private var moProgressHUD: MoProgressHUD? = null

    val paint: Paint
        get() = csv_book!!.getTextPaint()

    val contentWidth: Int
        get() = csv_book!!.getContentWidth()

    private var showCheckPremission: Boolean? = false


    protected fun initData() {
        viewModel.saveProgress()
        menuTopIn = AnimationUtils.loadAnimation(this, R.anim.anim_readbook_top_in)
        menuTopIn!!.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}

            override fun onAnimationEnd(animation: Animation) {
                v_menu_bg!!.setOnClickListener {
                    ll_menu_top!!.startAnimation(menuTopOut)
                    ll_menu_bottom!!.startAnimation(menuBottomOut)
                }
            }

            override fun onAnimationRepeat(animation: Animation) {

            }
        })
        menuBottomIn = AnimationUtils.loadAnimation(this, R.anim.anim_readbook_bottom_in)

        menuTopOut = AnimationUtils.loadAnimation(this, R.anim.anim_readbook_top_out)
        menuTopOut!!.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
                v_menu_bg!!.setOnClickListener(null)
            }

            override fun onAnimationEnd(animation: Animation) {
                fl_menu!!.visibility = View.INVISIBLE
            }

            override fun onAnimationRepeat(animation: Animation) {

            }
        })
        menuBottomOut = AnimationUtils.loadAnimation(this, R.anim.anim_readbook_bottom_out)
    }

    protected fun bindView() {
        moProgressHUD = MoProgressHUD(this)
        initcsv_book()
    }

    private fun initcsv_book() {
        csv_book!!.bookReadInit(object : ContentSwitchView.OnBookReadInitListener {
            override fun success() {
                viewModel.initData(this@ReadBookActivity)
            }
        })
    }

    fun sethpb_read_progressMax(count: Float) {
        hpb_read_progress!!.setMaxProgress(count)
    }
    fun initPop() {
        checkAddShelfPop = CheckAddShelfPop(this, viewModel.getBookShelf()!!.getBookInfoBean().getName(), object : CheckAddShelfPop.OnItemClickListener {
            override fun clickExit() {
                finish()
            }

            override fun clickAddShelf() {
                viewModel.addToShelf(null)
                checkAddShelfPop!!.dismiss()
            }
        })
        clp_chapterlist!!.setData(viewModel.getBookShelf(), object : ChapterListView.OnItemClickListener {
            override fun itemClick(index: Int) {
                csv_book!!.setInitData(index, viewModel.getBookShelf()!!.getBookInfoBean().getChapterlist()!!.size, BookContentView.DURPAGEINDEXBEGIN)
            }
        })

        windowLightPop = WindowLightPop(this)
        windowLightPop!!.initLight()

        fontPop = FontPop(this, object : FontPop.OnChangeProListener {
            override fun textChange(index: Int) {
                csv_book!!.changeTextSize()
            }

            override fun bgChange(index: Int) {
                csv_book!!.changeBg()
            }
        })

        readBookMenuMorePop = ReadBookMenuMorePop(this)
        readBookMenuMorePop!!.setOnClickDownload(View.OnClickListener {
            readBookMenuMorePop!!.dismiss()
            if (fl_menu!!.visibility == View.VISIBLE) {
                ll_menu_top!!.startAnimation(menuTopOut)
                ll_menu_bottom!!.startAnimation(menuBottomOut)
            }
            //弹出离线下载界面
            var endIndex = viewModel.getBookShelf()!!.getDurChapter() + 50
            if (endIndex >= viewModel.getBookShelf()!!.getBookInfoBean().getChapterlist()!!.size) {
                endIndex = viewModel.getBookShelf()!!.getBookInfoBean().getChapterlist()!!.size - 1
            }
            moProgressHUD!!.showDownloadList(viewModel.getBookShelf()!!.getDurChapter(), endIndex, viewModel.getBookShelf()!!.getBookInfoBean().getChapterlist()!!.size, object : MoProgressHUD.OnClickDownload {
                override fun download(start: Int, end: Int) {
                    moProgressHUD!!.dismiss()
                    viewModel.addToShelf(object : ReadBookViewModel.OnAddListner {
                        override fun addSuccess() {
                            val result = ArrayList<DownloadChapterBean>()
                            for (i in start..end) {
                                val item = DownloadChapterBean()
                                item.setNoteUrl(viewModel.getBookShelf()!!.getUrl())
                                item.setDurChapterIndex(viewModel.getBookShelf()!!.getBookInfoBean().getChapterlist().get(i).getDurChapterIndex())
                                item.setDurChapterName(viewModel.getBookShelf()!!.getBookInfoBean().getChapterlist().get(i).getDurChapterName())
                                item.setDurChapterUrl(viewModel.getBookShelf()!!.getBookInfoBean().getChapterlist().get(i).getDurChapterUrl())
                                item.setTag(viewModel.getBookShelf()!!.getFrom())
                                item.setBookName(viewModel.getBookShelf()!!.getBookInfoBean().getName())
                                item.setCoverUrl(viewModel.getBookShelf()!!.getBookInfoBean().getCoverUrl())
                                result.add(item)
                            }
                            RxBus.get().post(RxBusTag.ADD_DOWNLOAD_TASK, DownloadChapterListBean(result))
                        }
                    })

                }
            })
        })

        moreSettingPop = MoreSettingPop(this)
    }

    protected fun bindEvent() {
        hpb_read_progress!!.setProgressListener(object : OnProgressListener {
            override fun moveStartProgress(dur: Float) {
            }
            override fun durProgressChange(dur: Float) {
            }
            override fun moveStopProgress(dur: Float) {
                var realDur = Math.ceil(dur.toDouble()).toInt()
                if (realDur < 1) {
                    realDur = 1
                }
                if (realDur - 1 != viewModel.getBookShelf()!!.getDurChapter()) {
                    csv_book!!.setInitData(realDur - 1, viewModel.getBookShelf()!!.getBookInfoBean().getChapterlist()!!.size, BookContentView.DURPAGEINDEXBEGIN)
                }
                if (hpb_read_progress!!.getDurProgress() != realDur.toFloat())
                    hpb_read_progress!!.setDurProgress(realDur.toFloat())
            }

            override fun setDurProgress(dur: Float) {
                if (hpb_read_progress!!.getMaxProgress() == 1f) {
                    tv_pre!!.isEnabled = false
                    tv_next!!.isEnabled = false
                } else {
                    if (dur == 1f) {
                        tv_pre!!.isEnabled = false
                        tv_next!!.isEnabled = true
                    } else if (dur == hpb_read_progress!!.getMaxProgress()) {
                        tv_pre!!.isEnabled = true
                        tv_next!!.isEnabled = false
                    } else {
                        tv_pre!!.isEnabled = true
                        tv_next!!.isEnabled = true
                    }
                }
            }
        })
        iv_return!!.setOnClickListener { finish() }
        iv_more!!.setOnClickListener { readBookMenuMorePop!!.showAsDropDown(iv_more, 0, UIUtils.dip2px( -3.5f)) }
        csv_book!!.setLoadDataListener(object : ContentSwitchView.LoadDataListener {
            override fun loaddata(bookContentView: BookContentView, qtag: Long, chapterIndex: Int, pageIndex: Int) {
                viewModel.loadContent(bookContentView, qtag, chapterIndex, pageIndex)
            }

            override fun updateProgress(chapterIndex: Int, pageIndex: Int) {
                viewModel.updateProgress(chapterIndex, pageIndex)

                if (viewModel.getBookShelf()!!.getBookInfoBean().getChapterlist()!!.size > 0)
                    atv_title!!.setText(viewModel.getBookShelf()!!.getBookInfoBean().getChapterlist()!!.get(viewModel.getBookShelf()!!.getDurChapter()).getDurChapterName())
                else
                    atv_title!!.setText("无章节")
                if (hpb_read_progress!!.getDurProgress() != chapterIndex + 1f)
                    hpb_read_progress!!.setDurProgress(chapterIndex + 1f)
            }

            override fun getChapterTitle(chapterIndex: Int): String {
                return viewModel.getChapterTitle(chapterIndex)
            }

            override fun initData(lineCount: Int) {
                viewModel.setPageLineCount(lineCount)
                viewModel.initContent()
            }

            override fun showMenu() {
                fl_menu!!.visibility = View.VISIBLE
                ll_menu_top!!.startAnimation(menuTopIn)
                ll_menu_bottom!!.startAnimation(menuBottomIn)
            }
        })

        tv_pre!!.setOnClickListener { csv_book!!.setInitData(viewModel.getBookShelf()!!.getDurChapter() - 1, viewModel.getBookShelf()!!.getBookInfoBean().getChapterlist()!!.size, BookContentView.DURPAGEINDEXBEGIN) }
        tv_next!!.setOnClickListener { csv_book!!.setInitData(viewModel.getBookShelf()!!.getDurChapter() + 1, viewModel.getBookShelf()!!.getBookInfoBean().getChapterlist()!!.size, BookContentView.DURPAGEINDEXBEGIN) }

        ll_catalog!!.setOnClickListener {
            ll_menu_top!!.startAnimation(menuTopOut)
            ll_menu_bottom!!.startAnimation(menuBottomOut)
            Handler().postDelayed({ clp_chapterlist!!.show(viewModel.getBookShelf()!!.getDurChapter()) }, menuTopOut!!.duration)
        }

        ll_light!!.setOnClickListener {
            ll_menu_top!!.startAnimation(menuTopOut)
            ll_menu_bottom!!.startAnimation(menuBottomOut)
            Handler().postDelayed({ windowLightPop!!.showAtLocation(flContent, Gravity.BOTTOM, 0, 0) }, menuTopOut!!.duration)
        }

        ll_font!!.setOnClickListener {
            ll_menu_top!!.startAnimation(menuTopOut)
            ll_menu_bottom!!.startAnimation(menuBottomOut)
            Handler().postDelayed({ fontPop!!.showAtLocation(flContent, Gravity.BOTTOM, 0, 0) }, menuTopOut!!.duration)
        }

        ll_setting!!.setOnClickListener {
            ll_menu_top!!.startAnimation(menuTopOut)
            ll_menu_bottom!!.startAnimation(menuBottomOut)
            Handler().postDelayed({ moreSettingPop!!.showAtLocation(flContent, Gravity.BOTTOM, 0, 0) }, menuTopOut!!.duration)
        }
    }

    fun initContentSuccess(durChapterIndex: Int, chapterAll: Int, durPageIndex: Int) {
        csv_book!!.setInitData(durChapterIndex, chapterAll, durPageIndex)
    }

    fun startLoadingBook() {
        csv_book!!.startLoading()
    }

    override fun onPause() {
        super.onPause()
        viewModel.saveProgress()
    }

    fun setHpbReadProgressMax(count: Int) {
        hpb_read_progress.setMaxProgress(count.toFloat())
    }
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        val mo = moProgressHUD!!.onKeyDown(keyCode, event)
        if (mo!!)
            return mo!!
        else {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (fl_menu!!.visibility == View.VISIBLE) {
                    ll_menu_top!!.startAnimation(menuTopOut)
                    ll_menu_bottom!!.startAnimation(menuBottomOut)
                    return true
                } else if (!viewModel.getAdd() && checkAddShelfPop != null && !checkAddShelfPop!!.isShowing()) {
                    checkAddShelfPop!!.showAtLocation(flContent, Gravity.CENTER, 0, 0)
                    return true
                } else {
                    val temp2 = clp_chapterlist!!.dimissChapterList()
                    if (temp2!!)
                        return true
                    else {
                        finish()
                        return true
                    }
                }
            } else {
                val temp = csv_book!!.onKeyDown(keyCode, event)
                if (temp!!)
                    return true
            }
            return super.onKeyDown(keyCode, event)
        }
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean {
        val temp = csv_book!!.onKeyUp(keyCode, event)
        return if (temp!!) true else super.onKeyUp(keyCode, event)
    }

    fun showLoadBook() {
        moProgressHUD!!.showLoading("文本导入中...")
    }

    fun dimissLoadBook() {
        moProgressHUD!!.dismiss()
    }

    fun loadLocationBookError() {
        csv_book!!.loadError()
    }

    fun showDownloadMenu() {
        iv_more!!.visibility = View.VISIBLE
    }

    @SuppressLint("NewApi")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == 0x11) {
            if (grantResults != null && grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && PremissionCheck.checkPremission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                viewModel.openBookFromOther(this@ReadBookActivity)
            } else {
                if (!this.shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    showCheckPremission = true
                    moProgressHUD!!.showTwoButton("去系统设置打开SD卡读写权限？", "取消", View.OnClickListener { finish() }, "设置", View.OnClickListener { PremissionCheck.requestPermissionSetting(this@ReadBookActivity) })
                } else {
                    Toast.makeText(this, "未获取SD卡读取权限", Toast.LENGTH_SHORT).show()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    protected override fun onResume() {
        super.onResume()
        if (showCheckPremission!! && viewModel.getOpen_from() === ReadBookViewModel.OPEN_FROM_OTHER && !(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !PremissionCheck.checkPremission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE))) {
            showCheckPremission = true
            viewModel.openBookFromOther(this)
        }
    }
}