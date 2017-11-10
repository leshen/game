package game.shenle.com

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

/**
 * Created by shenle on 2017/11/10.
 */
abstract class BaseActivity :AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    abstract fun initView()
}