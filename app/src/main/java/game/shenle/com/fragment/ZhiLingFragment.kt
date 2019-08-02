package game.shenle.com.fragment

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import game.shenle.com.BaseFragment
import game.shenle.com.R
import game.shenle.com.common.data.ZhiLingData
import game.shenle.com.viewmodel.CreateJbZjViewModel
import kotlinx.android.synthetic.main.fragment_ziling.view.*
import kotlinx.android.synthetic.main.item_zhiling.view.*
import lib.shenle.com.base.SlBaseAdapter

/**
 * Created by shenle on 2019/8/1.
 */
class ZhiLingFragment : BaseFragment<CreateJbZjViewModel>(), SlBaseAdapter.BaseAdapterInterface<String>, BaseQuickAdapter.OnItemClickListener {
    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        viewModel.onInputZhiLing(adapter!!.data[position] as String)
    }

    override fun setData(v: View, item: String) {
        v.tv_name.text = item
    }

    companion object {
        fun getInstance(): ZhiLingFragment {
            return ZhiLingFragment()
        }
    }

    override fun initView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_ziling, container, false)
        view?.rv?.layoutManager = GridLayoutManager(activity, 4)
        val adapter = SlBaseAdapter<String>(R.layout.item_zhiling, ZhiLingData.list, this)
        view?.rv?.adapter = adapter
        adapter.setOnItemClickListener(this)
        return view
    }

    override fun getTNameClass(): Class<CreateJbZjViewModel> {
        return CreateJbZjViewModel::class.java
    }
}