package game.shenle.com.persistence

import android.support.v4.app.FragmentManager
import game.shenle.com.MainActivity
import game.shenle.com.R
import game.shenle.com.fragment.ControllerFragment
import javax.inject.Inject

/**
 * Created by shenle on 2017/12/1.
 */
class NavigationController {
//    private lateinit var containerId: Int
    private lateinit var fragmentManager: FragmentManager
    @Inject
    fun NavigationController(mainActivity: MainActivity){
//        this.containerId = R.id.container
        this.fragmentManager = mainActivity.getSupportFragmentManager()
    }

//    fun navigateToSearch() {
//        val searchFragment = SearchFragment()
//        fragmentManager.beginTransaction()
//                .replace(R.id., searchFragment)
//                .commitAllowingStateLoss()
//    }
//
//    fun navigateToController(owner: String, name: String) {
//        val fragment = ControllerFragment.create(owner, name)
//        val tag = "repo/$owner/$name"
//        fragmentManager.beginTransaction()
//                .replace(containerId, fragment, tag)
//                .addToBackStack(null)
//                .commitAllowingStateLoss()
//    }
//
//    fun navigateToUser(login: String) {
//        val tag = "user" + "/" + login
//        val userFragment = UserFragment.create(login)
//        fragmentManager.beginTransaction()
//                .replace(containerId, userFragment, tag)
//                .addToBackStack(null)
//                .commitAllowingStateLoss()
//    }
}