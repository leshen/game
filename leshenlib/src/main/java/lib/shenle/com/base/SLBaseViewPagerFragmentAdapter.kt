package lib.shenle.com.base

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.PagerAdapter

import java.util.ArrayList

class SLBaseViewPagerFragmentAdapter(fm: FragmentManager, list: List<Fragment>?) : FragmentStatePagerAdapter(fm) {
    private val list = ArrayList<Fragment>()

    init {
        if (list != null) {
            this.list.clear()
            this.list.addAll(list)
        }
    }

    override fun getItem(position: Int): Fragment {
        return list[position]
    }

    override fun getCount(): Int {
        return list.size
    }

    override fun getItemPosition(`object`: Any): Int {
        return PagerAdapter.POSITION_UNCHANGED
    }
}
