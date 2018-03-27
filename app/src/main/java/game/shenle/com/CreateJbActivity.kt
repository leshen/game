package game.shenle.com

import android.arch.lifecycle.Observer
import android.support.design.widget.TextInputLayout
import android.view.View
import com.example.android.observability.persistence.JbHttp
import game.shenle.com.db.repository.Status
import game.shenle.com.viewmodel.CreateJbViewModel
import kotlinx.android.synthetic.main.activity_create_jb.*
import lib.shenle.com.utils.UIUtils

/**
 * 创建剧本界面
 * Created by shenle on 2017/11/14.
 */
class CreateJbActivity : BaseActivity<CreateJbViewModel>(), View.OnClickListener {
    override fun onClick(v: View?) {
        when(v){
            bt_submit->{
                bt_submit.isEnabled = false
                if(checkOk()){
                    val jbHttp = JbHttp(tl_title.editText?.getText().toString(),tl_jj.editText?.getText().toString())
                    viewModel.submitJb(jbHttp).observe(this, Observer {
                        if (it?.status==Status.SUCCESS){
                            finish()
                        }else if (it?.status==Status.ERROR){
                            bt_submit.isEnabled = true
                            UIUtils.showToastSafe(it?.message)
                        }
                    })
                }else{
                    bt_submit.isEnabled = true
                }
            }
        }
    }

    fun checkOk():Boolean {
        val title = tl_title.editText?.getText().toString()
        val jj = tl_jj.editText?.getText().toString()
        if(title.isNullOrEmpty()){
            tl_title.error = "标题不能为空"
            return false
        }
        if(jj.isNullOrEmpty()){
            tl_jj.error = "简介不能为空"
            return false
        }
        return true
    }
    override fun getTNameClass(): Class<CreateJbViewModel> {
        return CreateJbViewModel::class.java
    }

    companion object {
        fun goHere() {
//            val bundle = Bundle()
//            bundle.putLong("jbId",id)
//            UIUtils.startActivity(CreateJbActivity::class.java,bundle)
            UIUtils.startActivity(CreateJbActivity::class.java)
        }
    }

    override fun initView() {
        setContentView(R.layout.activity_create_jb)
        bt_submit.setOnClickListener(this)
    }

}