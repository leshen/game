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


    override fun initView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_main_game, container, false)
        viewModel.init(arguments!!.getString("jbId"))
        viewModel.getZjInput()?.observe(activity as LifecycleOwner, Observer {
            view?.tv_content?.setLoadDataListener(object : ContentSwitchView.LoadDataListener {
                override fun loaddata(bookContentView: BookContentView, qtag: Long, chapterIndex: Int, pageIndex: Int) {
                    viewModel.loadContent(bookContentView, qtag, chapterIndex, pageIndex)
                }
                override fun updateProgress(chapterIndex: Int, pageIndex: Int) {
                    viewModel.updateProgress(chapterIndex, pageIndex)
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
//            it?.let {
//                view?.tv_content?.startLoading()
//            }
        })
        viewModel.doZl()
        viewModel.getState()?.observe(activity as LifecycleOwner, Observer {
            when(it){
                "开始"->view?.tv_content?.startLoading()
            }
        })
        return view
    }

    override fun onPause() {
        super.onPause()
        viewModel.saveProgress()
    }
}