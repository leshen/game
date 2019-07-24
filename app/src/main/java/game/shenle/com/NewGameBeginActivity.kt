package game.shenle.com

import android.arch.lifecycle.Observer
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.android.observability.persistence.UserTable
import game.shenle.com.db.repository.Resource
import game.shenle.com.db.repository.Status
import game.shenle.com.viewmodel.NewGameBeginViewModel
import kotlinx.android.synthetic.main.activity_new_game_begin.*
import kotlinx.android.synthetic.main.holder_game_begin_select.view.*
import lib.shenle.com.base.SlBaseAdapter
import lib.shenle.com.utils.UIUtils
import java.util.*

/**
 * 游戏开始界面
 * Created by shenle on 2017/11/14.
 */
class NewGameBeginActivity : BaseActivity<NewGameBeginViewModel>() {
    override fun getTNameClass(): Class<NewGameBeginViewModel> {
        return NewGameBeginViewModel::class.java
    }
    companion object {
        fun goHere() {
            UIUtils.startActivity(NewGameBeginActivity::class.java)
        }
    }

    private val listData = arrayListOf("新建游戏","单人旅程","双人旅程","龙套体验","上帝视角旁观","关于我们","上帝视角管理","阅读","扩展")
    override fun initView() {
        setContentView(R.layout.activity_new_game_begin)
        viewModel.init("w9rxDDDi")
        initRecyleView(listData)
        checkProgress()
    }

    private lateinit var baseAdapter: SlBaseAdapter<String>

    private fun initRecyleView(listData:List<String>) {
        rv.layoutManager = LinearLayoutManager(this)
        baseAdapter = SlBaseAdapter(R.layout.holder_game_begin_select, listData, object : SlBaseAdapter.BaseAdapterInterface<String> {
            override fun setData(v: View, item: String) {
                v.tv.text = item
            }
        })
        rv.adapter = baseAdapter
        baseAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_RIGHT)
        baseAdapter.setOnItemClickListener{ adapter: BaseQuickAdapter<Any, BaseViewHolder>?, _: View, position: Int ->
                val item = adapter?.data?.get(position) as String?
                when(item){
                    "新建游戏"->{
                        NewUserCreateActivity.goHere()}
                    "继续游戏"->{
                        GameActivity.goHere(user?.data?.jbId!!)
                    }
                    "单人旅程"->{UIUtils.showToastSafe("紧张研发中...")}
                    "双人旅程"->{UIUtils.showToastSafe("紧张研发中...")}
                    "龙套体验"->{UIUtils.showToastSafe("紧张研发中...")}
                    "上帝视角旁观"->{UIUtils.showToastSafe("紧张研发中...")}
                    "关于我们"->{UIUtils.showToastSafe("紧张研发中...")}
                    "上帝视角管理"->{MyJbListActivity.goHere()}
                    "阅读"->{
//                        ReadBookActivity.goHere()
                        UIUtils.showToastSafe("紧张研发中...")
                    }
                    "扩展"->{
                        OtherActivity.goHere()
                    }
                }
        }
    }

    private var user: Resource<UserTable>?=null

    private fun checkProgress() {
        // 检查进度(新游戏和档案)
        viewModel.getUser()?.observe(this, Observer<Resource<UserTable>> {
            if (it?.status == Status.SUCCESS) {
                this.user = it
                val userName = it?.data?.username
                if (userName.isNullOrEmpty()) {
                    //新游戏
                    if (listData.contains("继续游戏")) {
                        listData.remove("继续游戏")
                        baseAdapter.replaceData(listData)
                    }
                } else {
                    // 读取档案
                    if (!listData.contains("继续游戏")) {
                        listData.add(0, "继续游戏")
                        baseAdapter.replaceData(listData)
                    }
                }
            }else if (it?.status == Status.ERROR){
                UIUtils.showToastSafe(it?.message)
            }
        })

    }
}