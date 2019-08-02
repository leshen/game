package game.shenle.com.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import game.shenle.com.BaseFragment
import game.shenle.com.R
import game.shenle.com.viewmodel.CreateJbZjViewModel
import lib.shenle.com.base.SLBaseViewPagerFragmentAdapter
import kotlinx.android.synthetic.main.fragment_edit_help.view.*

/**
 * Created by shenle on 2019/8/1.
 */
class EditHelpFragment:BaseFragment<CreateJbZjViewModel>() {
    private var listFragments = ArrayList<Fragment>()

    override fun initView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_edit_help, container, false)
        listFragments.clear()
        listFragments.add(ZhiLingFragment.getInstance())
        // 底部导航的 ViewPager
        val viewPagerAdapter = SLBaseViewPagerFragmentAdapter(
                fragmentManager!!, listFragments)
        view?.vp?.setAdapter(viewPagerAdapter)
        return view
    }

    override fun getTNameClass(): Class<CreateJbZjViewModel> {
        return CreateJbZjViewModel::class.java
    }
}