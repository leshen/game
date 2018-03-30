package game.shenle.com.fragment

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.Transformations
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.android.observability.persistence.JbContentTable
import game.shenle.com.BaseFragment
import game.shenle.com.R
import game.shenle.com.db.repository.Status
import game.shenle.com.view.contentswitchview.BookContentView
import game.shenle.com.view.contentswitchview.ContentSwitchView
import game.shenle.com.viewmodel.GameMainModel
import kotlinx.android.synthetic.main.fragment_main_game.*
import kotlinx.android.synthetic.main.fragment_main_game.view.*

/**
 * 主界面
 * Created by shenle on 2017/12/1.
 */
class MainGameFragment : BaseFragment<GameMainModel>() {
    override fun getTNameClass(): Class<GameMainModel> {
        return GameMainModel::class.java
    }

    companion object {
        fun getInstance(jbId: String?): MainGameFragment {
            val gameUserInfoFragment = Instance.controllerFragment
            jbId?.let {
                val bundle = Bundle()
                bundle.putString("jbId", jbId)
                gameUserInfoFragment.arguments = bundle
            }
            return gameUserInfoFragment
        }
    }

    private object Instance {
        val controllerFragment = MainGameFragment()
    }

    private var zjInput: LiveData<JbContentTable>? = null

    override fun initView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_main_game, container, false)
        viewModel.init(arguments!!.getString("jbId"))
//        viewModel.getStrLive()?.observe(activity as LifecycleOwner, Observer {
//            view?.tv_test?.visibility = View.VISIBLE
//            view?.tv_test?.text = it
//        })
        viewModel.getZjInput()?.observe(activity as LifecycleOwner, Observer {
            view?.tv_content?.setLoadDataListener(object : ContentSwitchView.LoadDataListener {
                override fun loaddata(bookContentView: BookContentView, qtag: Long, chapterIndex: Int, pageIndex: Int) {
                    viewModel.loadContent(bookContentView, qtag, chapterIndex, pageIndex)
                    //            view?.tv_content?.text = it
//                    it?.let {
//                        val tempCount = Math.ceil((it.bg?.lines().size
//                                ?: 0) * 1.0 / pageLineCount).toInt() - 1
//                        var pageIndex = pageIndex
//                        if (pageIndex == BookContentView.DURPAGEINDEXBEGIN) {
//                            pageIndex = 0
//                        } else if (pageIndex == BookContentView.DURPAGEINDEXEND) {
//                            pageIndex = tempCount
//                        } else {
//                            if (pageIndex >= tempCount) {
//                                pageIndex = tempCount
//                            }
//                        }
//                        val start = pageIndex * pageLineCount
//                        val end = if (pageIndex == tempCount) it.bg.length
//                                ?: 0 else start + pageLineCount
//                        if (bookContentView != null && qtag == bookContentView!!.getqTag()) {
//                            bookContentView.updateData(qtag, it.jbTitle, it?.bg?.toList().map { it.toString() }.subList(start, end), it.totalZj, chapterIndex, pageIndex, tempCount + 1)
//                        }
//                    }
                }
                override fun updateProgress(chapterIndex: Int, pageIndex: Int) {
//                    it?.let {
//                        it.zj_index = chapterIndex
//                        it.zj_self_index = pageIndex
//                    }
                    viewModel.updateProgress(chapterIndex, pageIndex)
//
//                if (viewModel.getBookShelf()!!.getBookInfoBean().getChapterlist()!!.size > 0)
//                    atv_title!!.setText(viewModel.getBookShelf()!!.getBookInfoBean().getChapterlist()!!.get(viewModel.getBookShelf()!!.getDurChapter()).getDurChapterName())
//                else
//                    atv_title!!.setText("无章节")
//                if (hpb_read_progress!!.getDurProgress() != chapterIndex + 1f)
//                    hpb_read_progress!!.setDurProgress(chapterIndex + 1f)
                }

                override fun getChapterTitle(chapterIndex: Int): String {
                    return viewModel.getChapterTitle(chapterIndex)
                }

                override fun initData(lineCount: Int) {
                    viewModel.setPageLineCount(lineCount)
                    it?.let {
                        view?.tv_content?.setInitData(it.zj_index, it.totalZj, it.zj_self_index)
                    }
//                viewModel.initContent()
                }

                override fun showMenu() {
//                fl_menu!!.visibility = View.VISIBLE
//                ll_menu_top!!.startAnimation(menuTopIn)
//                ll_menu_bottom!!.startAnimation(menuBottomIn)
                }
            })
            it?.let {
                view?.tv_content?.startLoading()
            }
        })
        return view
    }

    override fun onPause() {
        super.onPause()
        viewModel.saveProgress()
    }
}