package game.shenle.com

import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import game.shenle.com.viewmodel.OtherViewModel
import kotlinx.android.synthetic.main.activity_new_game_begin.*
import kotlinx.android.synthetic.main.holder_game_begin_select.view.*
import lib.shenle.com.base.SlBaseAdapter
import lib.shenle.com.utils.UIUtils

class OtherActivity:BaseActivity<OtherViewModel>() {
    companion object{
        fun goHere(){
            UIUtils.startActivity(OtherActivity::class.java)
        }
    }
    override fun getTNameClass(): Class<OtherViewModel> {
        return OtherViewModel::class.java
    }

    override fun initView() {
        setContentView(R.layout.activity_new_game_begin)
        initRecyleView(listData)
    }

    private lateinit var baseAdapter: SlBaseAdapter<String>
    private val listData = arrayListOf("mp3")

    private fun initRecyleView(listData:List<String>) {
        rv.layoutManager = GridLayoutManager(this,4)
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
                "mp3"->{

                }
            }
        }
    }

}
