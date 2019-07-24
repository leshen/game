package game.shenle.com

import android.app.Dialog
import android.content.*
import android.media.MediaPlayer
import android.os.Bundle
import android.os.IBinder
import android.view.View
import android.widget.RemoteViews
import android.widget.SeekBar
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
        return if(cb_auto.isChecked)MUSIC_AUTO_NEXT else MUSIC_NOAUTO_NEXT
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
        et_dz.setText(textMp3)
        seekbar.setOnSeekBarChangeListener(this)
        bt_submit.setOnClickListener {
            musicService?.stop()
            seekbar.progress = 0
            onPlayP(et_dz.text.toString(), 0)
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
        tv_end_time.text = TimeUtil.getStrSFMTime(size.toLong())
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
            R.id.li_play -> play(path, seekbar.progress)
            R.id.li_pre -> onPreP()
            else -> {
            }
        }
    }

    private var remoteViews: RemoteViews? = null
    private var path = ""                        //音乐文件路径
    private var isPause: Boolean = false
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
        var tagMp3 = if (path.contains(".MP3"))".MP3" else ".mp3"
        if (tv_fa1.isChecked){
            val page = MyUtils.getZZIdEnd(path.substring(path.indexOf(tagMp3)-3), tagMp3)
            val substring = path.substring(0, path.indexOf(".mp3") - 3)
            var nextPage = (page!!.toInt()+1).toString()
            if (page!!.toInt()<9&&page.contains("0")){
                nextPage = "0"+(page.toInt()+1)
            }
            path = substring+path.substring(path.indexOf(".mp3")-3).replace(page!!,nextPage)
            et_dz.setText(path)
            onPlayP(path,0)
        }
    }

    /**
     * 上一篇
     */
    override fun onPreP() {
        isPlaying = false
        onStopP()
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
