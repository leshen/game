package game.shenle.com

import android.arch.lifecycle.Observer
import android.nfc.tech.MifareUltralight.PAGE_SIZE
import android.support.v7.widget.LinearLayoutManager
import android.view.Gravity
import android.view.View
import android.widget.Toast
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.android.observability.persistence.JbTable
import game.shenle.com.db.repository.Resource
import game.shenle.com.db.repository.Status
import game.shenle.com.viewmodel.NewUserCreateViewModel
import kotlinx.android.synthetic.main.activity_user_create.*
import kotlinx.android.synthetic.main.holder_jb_select.view.*
import lib.shenle.com.base.SlBaseAdapter
import lib.shenle.com.utils.UIUtils

/**
 * 新建游戏角色界面
 * 人物设定(暂时正派和反派)
 * Created by shenle on 2017/11/14.
 */
class NewUserCreateActivity : BaseActivity<NewUserCreateViewModel>() {
    override fun getTNameClass(): Class<NewUserCreateViewModel> {
        return NewUserCreateViewModel::class.java
    }

    companion object {
        fun goHere() {
            UIUtils.startActivity(NewUserCreateActivity::class.java)
        }
    }

    private var listData: List<JbTable>? = null
    override fun initView() {
        setContentView(R.layout.activity_user_create)
        viewModel.init()
        initRecyleView(listData)
        viewModel.getJBList()?.observe(this, Observer<Resource<List<JbTable>>> {
            if(it?.status == Status.SUCCESS) {
                it?.data?.let { baseAdapter?.replaceData(it) }
                baseAdapter?.addData(JbTable("-1"))
            }
        })
    }

    private var baseAdapter: SlBaseAdapter<JbTable>? = null

    private fun initRecyleView(listData: List<JbTable>?) {
        rv.layoutManager = LinearLayoutManager(this)
        baseAdapter = SlBaseAdapter(R.layout.holder_jb_select, listData, object : SlBaseAdapter.BaseAdapterInterface<JbTable> {
            override fun setData(v: View, item: JbTable) {
                if (item?.objectId == "-1") {
                    v.tv?.text = "小伙,发挥自己想象,去新建一个剧本吧..."
                    v.tv.gravity = Gravity.CENTER
                    v.tv_sign.visibility = View.GONE
                    v.tv.setTextColor(UIUtils.getColor(R.color.text_color_2))
                } else {
                    v.tv_sign.visibility = View.VISIBLE
                    v.tv.gravity = Gravity.LEFT
                    v.tv.text = "<<${item.jbTitle}>>,简述:${item.jbContent}"
                    v.tv_sign.text = "作者:${item.userName ?: "佚名"},最近更新:${item.updatedAt}"
                    v.tv.setTextColor(UIUtils.getColor(R.color.text_color_1))
                }
            }
        })
        rv.adapter = baseAdapter
        baseAdapter?.openLoadAnimation(BaseQuickAdapter.SLIDEIN_RIGHT)
        baseAdapter?.setOnItemClickListener { adapter: BaseQuickAdapter<Any, BaseViewHolder>?, _: View, position: Int ->
            val item = adapter?.data?.get(position) as JbTable?
            if (item?.objectId == "-1") {
                CreateJbActivity.goHere()
            } else {
                GameActivity.goHere(item?.objectId!!)
            }
        }
        baseAdapter?.setOnLoadMoreListener({
            viewModel.loadNextJBList().observe(this, Observer {
                if (it?.status == Status.ERROR) {
                    baseAdapter?.loadMoreFail()
                } else if(it?.status == Status.SUCCESS){
                    baseAdapter?.addData(it?.data!!)
                    if (it?.data?.size!! < 10) {
                        baseAdapter?.loadMoreEnd()
                    } else {
                        baseAdapter?.loadMoreComplete()
                    }
                }
            })
        }, rv)
    }
}