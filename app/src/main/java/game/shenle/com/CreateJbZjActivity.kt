package game.shenle.com

import android.arch.lifecycle.Observer
import android.support.design.widget.TextInputLayout
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.example.android.observability.persistence.JbContentHttp
import com.example.android.observability.persistence.JbHttp
import game.shenle.com.db.repository.Status
import game.shenle.com.viewmodel.CreateJbViewModel
import game.shenle.com.viewmodel.CreateJbZjViewModel
import kotlinx.android.synthetic.main.activity_create_jb_zj.*
import kotlinx.android.synthetic.main.edit_help_utils.*
import lib.shenle.com.utils.LogUtils
import lib.shenle.com.utils.SoftKeyBoardListener
import lib.shenle.com.utils.UIUtils

/**
 * 创建剧本界面
 * Created by shenle on 2017/11/14.
 */
class CreateJbZjActivity : BaseActivity<CreateJbZjViewModel>(), View.OnClickListener, TextWatcher, SoftKeyBoardListener.OnSoftKeyBoardChangeListener {
    override fun keyBoardShow(height: Int) {
        edit_help.visibility = View.VISIBLE
    }

    override fun keyBoardHide(height: Int) {
        edit_help.visibility = View.GONE
    }

    override fun afterTextChanged(s: Editable?) {

    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

    }

    override fun onClick(v: View?) {
        when(v){
            ib_biaoqing->{
                findViewById<View>(R.id.emojicons_fragment)?.visibility = View.VISIBLE
            }
            bt_submit->{
                bt_submit.isEnabled = false
                if(checkOk()){
                    val jbHttp = JbContentHttp()
                    //TODO 具体文本
                    viewModel.submitJbZj(jbHttp).observe(this, Observer {
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
    override fun getTNameClass(): Class<CreateJbZjViewModel> {
        return CreateJbZjViewModel::class.java
    }

    companion object {
        fun goHere() {
//            val bundle = Bundle()
//            bundle.putLong("jbId",id)
//            UIUtils.startActivity(CreateJbActivity::class.java,bundle)
            UIUtils.startActivity(CreateJbZjActivity::class.java)
        }
    }

    override fun initView() {
        setContentView(R.layout.activity_create_jb_zj)
        bt_submit.setOnClickListener(this)
        ib_biaoqing.setOnClickListener(this)
        et_jj.addTextChangedListener(this)
        findViewById<View>(R.id.emojicons_fragment)?.visibility = View.GONE
        SoftKeyBoardListener.setListener(this,this)
        edit_help.visibility = View.GONE
    }

}