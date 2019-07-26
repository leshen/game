package game.shenle.com

import android.app.Dialog
import android.content.*
import android.media.MediaPlayer
import android.os.Bundle
import android.os.IBinder
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.RemoteViews
import android.widget.SeekBar
import com.raizlabs.android.dbflow.sql.language.SQLite
import game.shenle.com.locatedb.bean.Mp3History
import game.shenle.com.locatedb.bean.Mp3History_Table
import game.shenle.com.service.MusicService
import game.shenle.com.service.MusicService.Companion.MUSIC_AUTO_NEXT
import game.shenle.com.service.MusicService.Companion.MUSIC_GB_NEXT
import game.shenle.com.service.MusicService.Companion.MUSIC_GB_PLAY
import game.shenle.com.service.MusicService.Companion.MUSIC_GB_PRE
import game.shenle.com.service.MusicService.Companion.MUSIC_NOAUTO_NEXT
import game.shenle.com.viewmodel.MyMusicViewModel
import kotlinx.android.synthetic.main.activity_my_music.*
import lib.shenle.com.utils.MyUtils
import lib.shenle.com.utils.TimeUtil
import lib.shenle.com.utils.UIUtils

class MyMusicActivity : BaseActivity<MyMusicViewModel>(), SeekBar.OnSeekBarChangeListener, MusicService.PlayUpdateProgressListener, View.OnClickListener {
    override fun getPlayStyle(): Int {
        return if (cb_auto.isChecked) MUSIC_AUTO_NEXT else MUSIC_NOAUTO_NEXT
    }

    companion object {
        var connectionService: ConnectionService? = null
        var musicService: MusicService? = null
        fun goHere() {
            UIUtils.startActivity(MyMusicActivity::class.java)
        }
    }

    override fun getTNameClass(): Class<MyMusicViewModel> {
        return MyMusicViewModel::class.java
    }
    val textMp3 = "http://bbs.zhue.com.cn/data/attachment/portal/201907/22/234114dsqwv8yk8vh6tziz.mp3"
    override fun initView() {
        setContentView(R.layout.activity_my_music)
        ll_list.setOnClickListener(this)
        li_play.setOnClickListener(this)
        li_pre.setOnClickListener(this)
        li_next.setOnClickListener(this)
        ib_cancel.setOnClickListener{
            et_dz.setText("")
        }
        et_dz.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int,
                                       count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int,
                                           after: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                ib_cancel.visibility = if(s.toString().isEmpty())View.GONE else View.VISIBLE
            }
        })
        et_dz.setText(textMp3)
        viewModel.initMusicService()
        val initHistory = initHistory()
        if (initHistory!=null&&initHistory.size>0){
            val mp3History = initHistory[0]
            et_dz.setText(mp3History.url)
            cb_auto.isChecked = mp3History.gz_auto==1
            cb_tiao.isChecked = mp3History.gz_tiao_begin_time>0
            tv_tiao_time.text = mp3History.gz_tiao_begin_time.toString()
            tv_fa1.isChecked = mp3History.gz_type==2||mp3History.gz_type==1
            tv_fa2.isChecked = mp3History.gz_type==2
        }
        seekbar.setOnSeekBarChangeListener(this)
        bt_submit.setOnClickListener { startPlay() }
        tv_tiao_time_add.setOnClickListener {
            cb_tiao.isChecked = true
            tv_tiao_time.text = (tv_tiao_time.text.toString().toInt() + 1).toString()
        }
        tv_tiao_time_less.setOnClickListener {
            cb_tiao.isChecked = true
            var i = tv_tiao_time.text.toString().toInt() - 1
            if (i < 0) i = 0
            tv_tiao_time.text = i.toString()
        }
        val filter = IntentFilter();
        filter.addAction(MUSIC_GB_PRE);
        filter.addAction(MUSIC_GB_PLAY);
        filter.addAction(MUSIC_GB_NEXT);
        registerReceiver(mRemoteViewsReceiver, filter)
        if (connectionService == null) {
            connectionService = ConnectionService()
        }
        val bindIntent = Intent(UIUtils.context, MusicService::class.java)
        bindService(bindIntent, connectionService, BIND_AUTO_CREATE)
        if (musicService?.mMediaPlayer != null) {
            setMaxBarP(musicService!!.mMediaPlayer!!.getDuration())
            if (musicService!!.isPlaying) {
                isPlaying = true
                iv_play.setImageResource(R.mipmap.btn_pause_text)
            }
        }
        musicService?.setPlayUpdateOnProgressBarListener(this)
    }

    override fun updateBarP(size: Int) {
        seekbar.progress = size
        tv_start_time.text = TimeUtil.getStrSFMTime(size.toLong())
    }

    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {
        musicService?.setChanging(true)
    }

    fun setPlaying(playing: Boolean) {
        isPlaying = playing
        if (!isPlaying) {
            onPauseP()
            seekbar.progress = 0
            iv_play.setImageResource(R.mipmap.btn_play_text)
            remoteViews?.setImageViewResource(R.id.iv_play, R.mipmap.btn_play_text)
            musicService?.updataView()
        }
    }

    fun startPlay() {
        musicService?.stop()
        var progress = 0
        if (cb_tiao.isChecked) {
            progress = tv_tiao_time.text.toString().toInt() * 60 * 1000
        }
        onPlayP(et_dz.text.toString(), progress)
    }

    fun play(path: String, progress: Int) {
        if (musicService?.isPlaying ?: false) {
            onPauseP()
        } else {
            onPlayP(path, progress)
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.ll_list -> {
            }
            R.id.li_next -> onNextP()
            R.id.li_play -> {
                if (UIUtils.isEmpty(path)) {
                    startPlay()
                } else {
                    play(path, seekbar.progress)
                }
            }
            R.id.li_pre -> onPreP()
            else -> {
            }
        }
    }

    private var remoteViews: RemoteViews? = null
    private var path = ""                        //音乐文件路径
    var mMediaPlayer: MediaPlayer? = null
    private var isPlaying: Boolean = false
    private var isTouchSeek: Boolean = false
    override fun onStopTrackingTouch(seekBar: SeekBar) {
        isPlaying = true
        isTouchSeek = true
        iv_play.setImageResource(R.mipmap.btn_pause_text)
        musicService?.play(path, seekbar.progress, isTouchSeek, getDialog())
    }

    private fun getDialog(): Dialog? {
        return null
    }

    override fun onPlayP(path: String, progress: Int) {
        if (UIUtils.isEmpty(path)) {
            UIUtils.showToastSafe("音频文件缺失,请联系管理员")
            return
        }
        this.path = path
        isPlaying = true
        iv_play.setImageResource(R.mipmap.btn_pause_text)
        remoteViews?.setImageViewResource(R.id.iv_play, R.mipmap.btn_pause_text)
        musicService?.updataView()
        seekbar.progress = progress
        musicService?.play(path, progress, isTouchSeek, getDialog())
    }

    /**
     * 下一篇
     */
    override fun onNextP() {
        isPlaying = false
        onStopP()
        var tagMp3 = if (path.contains(".MP3")) ".MP3" else ".mp3"
        if (tv_fa1.isChecked) {
            var pageStr = MyUtils.getZZIdEnd(path.substring(path.indexOf(tagMp3) - 3), tagMp3)
            val substring = path.substring(0, path.indexOf(".mp3") - 3)
            var nextPage = ""
            var page = pageStr
            if (pageStr?.startsWith("0") ?: return) {
                page = pageStr.substring(1)
            }
            nextPage = (page!!.toInt() + 1).toString()
            if (page!!.toInt() < 9 && pageStr.startsWith("0")) {
                nextPage = "0" + (page.toInt() + 1)
            }
            path = substring + path.substring(path.indexOf(".mp3") - 3).replace(pageStr, nextPage)
            et_dz.setText(path)
            startPlay()
        }
    }

    /**
     * 上一篇
     */
    override fun onPreP() {
        isPlaying = false
        onStopP()
        var tagMp3 = if (path.contains(".MP3")) ".MP3" else ".mp3"
        if (tv_fa1.isChecked) {
            var pageStr = MyUtils.getZZIdEnd(path.substring(path.indexOf(tagMp3) - 3), tagMp3)
            val substring = path.substring(0, path.indexOf(".mp3") - 3)
            var perPage = ""
            var page = pageStr
            if (pageStr?.startsWith("0") ?: return) {
                page = pageStr.substring(1)
            }
            if (page!!.toInt() > 0) {
                perPage = (page!!.toInt() - 1).toString()
            } else {
                UIUtils.showToastSafe("没有上一篇了")
                return
            }
            if (page!!.toInt() < 11 && !tv_fa2.isChecked) {
                perPage = "0" + (page.toInt() - 1)
            }
            path = substring + path.substring(path.indexOf(".mp3") - 3).replace(pageStr, perPage)
            et_dz.setText(path)
            startPlay()
        }
    }

    override fun onStopP() {
        musicService?.stop()
    }

    override fun onPauseP() {
        isPlaying = false
        iv_play.setImageResource(R.mipmap.btn_play_text)
        remoteViews?.setImageViewResource(R.id.iv_play, R.mipmap.btn_play_text);
        musicService?.updataView();
        musicService?.pause()
    }

    override fun setMaxBarP(duration: Int) {
        seekbar.max = duration
        tv_end_time.text = TimeUtil.getStrSFMTime(duration.toLong())
    }

    override fun onPreparedP() {
        getDialog()?.show()
    }

    override fun onDestroy() {
        if (!UIUtils.isEmpty(path))
        saveHistory(path)
        if (mRemoteViewsReceiver != null) {
            unregisterReceiver(mRemoteViewsReceiver)
        }
        if (musicService != null && connectionService != null) {
            unbindService(connectionService)
        }
        mMediaPlayer?.stop()
        mMediaPlayer?.release()
        mMediaPlayer = null
        path = ""

        super.onDestroy()
    }


    private fun initHistory(): MutableList<Mp3History>? {
        return SQLite.select().from(Mp3History::class.java!!).orderBy(Mp3History_Table.id, false).queryList()
    }
    private fun saveHistory(url: String){
        // 将搜索记录写入缓存
        var baseModel: Mp3History? = SQLite.select().from(Mp3History::class.java).where(Mp3History_Table.url.eq(url)).querySingle()
        if (baseModel != null) {
            baseModel.delete()
        } else {
            baseModel = Mp3History()
            baseModel.url = url
            baseModel.progress = seekbar.progress.toLong()
            baseModel.gz_tiao_begin_time = if(cb_tiao.isChecked)tv_tiao_time.text.toString().toInt() else 0
            baseModel.gz_auto = if(cb_auto.isChecked)1 else 0
            baseModel.gz_type = if(tv_fa2.isChecked)2 else if (tv_fa1.isChecked)1 else 0
        }
        baseModel.save()
    }
    private val mRemoteViewsReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val state = intent.action
            when (state) {
                MUSIC_GB_PLAY -> play(path, seekbar.progress)
                MUSIC_GB_PRE -> onPreP()
                MUSIC_GB_NEXT -> onNextP()
            }
        }
    }

    inner class ConnectionService : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val bundle = Bundle()
//            bundle.putString("title", title)
//            bundle.putString("aid", aid)
            musicService = (service as MusicService.MyBinder).getService(bundle)
            remoteViews = (service as MusicService.MyBinder).getRemoteViews()
            remoteViews!!.setTextViewText(R.id.tv_name, title)
            remoteViews!!.setImageViewResource(R.id.iv_icon, R.mipmap.ic_launcher)
            musicService!!.updataView()
            musicService!!.setPlayUpdateOnProgressBarListener(this@MyMusicActivity)
            musicService!!.play(path, seekbar.progress, isTouchSeek, getDialog())
        }

        override fun onServiceDisconnected(name: ComponentName) {

        }
    }
}
