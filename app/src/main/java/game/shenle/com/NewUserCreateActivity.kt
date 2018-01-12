package game.shenle.com

import android.arch.lifecycle.Observer
import android.support.v7.widget.LinearLayoutManager
import android.text.Html
import android.view.Gravity
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.android.observability.persistence.JbTable
import game.shenle.com.db.repository.Resource
import game.shenle.com.db.repository.Status
import game.shenle.com.utils.BombHelper
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
            if (it?.status != Status.ERROR) {
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
                    v.em.isCanLeftSwipe = false
                    v.em.isCanRightSwipe = false
                    v.tv.setTextColor(UIUtils.getColor(R.color.text_color_2))
                    v.rl_content.setOnClickListener{ CreateJbActivity.goHere()}
                } else {
                    v.tv_sign.visibility = View.VISIBLE
                    v.tv.gravity = Gravity.LEFT
                    v.em.isCanLeftSwipe = true
                    v.em.isCanRightSwipe = true
                    var stateStr= if (item?.jbStatus == 0) {
                        "(审核中)"
                    } else if (item?.jbStatus == 1) {
                        "(更新中)"
                    } else {
                        "(已完结)"
                    }
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        v.tv.text = Html.fromHtml("<font color=" + UIUtils.getColor(R.color.text_color_2) + ">"
                                + stateStr + "</font><font color=" + UIUtils.getColor(R.color.text_color_1) + ">"
                                + "《${item.jbTitle}》,简述:${item.jbContent}" + "</font>", Html.FROM_HTML_MODE_LEGACY);
                    } else {
                        v.tv.text = Html.fromHtml("<font color=" + UIUtils.getColor(R.color.text_color_2) + ">"
                                + stateStr + "</font><font color=" + UIUtils.getColor(R.color.text_color_1) + ">"
                                + "《${item.jbTitle}》,简述:${item.jbContent}" + "</font>")
                    }
                    v.tv_sign.text = "作者:${item.userName ?: "佚名"},最近更新:${item.updatedAt}"
                    v.tv.setTextColor(UIUtils.getColor(R.color.text_color_1))
                    if (item.userName==""){//TODO 编辑权限判断
                        v.em.isCanRightSwipe = true
                        v.tv_edit.setOnClickListener{
                            //编辑
                            EditJbActivity.goHere(item.objectId)
                        }
                    }
                    v.em.isCanLeftSwipe = true
                    v.tv_del.setOnClickListener{
                        //TODO 删除
                            UIUtils.showToastSafe("删除")
                         }
                    v.tv_share.setOnClickListener{
                        //TODO 分享
                        UIUtils.showToastSafe("分享")
                    }
                    v.rl_content.setOnClickListener{ GameActivity.goHere(item?.objectId!!)}
                }
            }
        })
        rv.adapter = baseAdapter
        baseAdapter?.isFirstOnly(false);
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
            BombHelper.setOnLoadMoreListener(viewModel.loadNextJBList(),this,baseAdapter)
        }, rv)
    }
}