package game.shenle.com.service

import android.app.Dialog
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Binder
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.RemoteViews

import java.util.Timer
import java.util.TimerTask

import game.shenle.com.MyMusicActivity
import lib.shenle.com.utils.UIUtils
import android.app.NotificationChannel
import android.graphics.Color
import android.os.Build


class MusicService : Service(), MediaPlayer.OnCompletionListener {
    var mMediaPlayer: MediaPlayer? = null       //媒体播放器对象
    private var path = ""                        //音乐文件路径
    private var isPause: Boolean = false                    //暂停状态
    private var playUpdateProgressListener: PlayUpdateProgressListener? = null
    private var isUpdate = false
    private val mTimer = Timer()
    private var mTimerTask: TimerTask? = object : TimerTask() {
        override fun run() {
            if (isChanging == true) {
                return
            }
            if (playUpdateProgressListener != null && mMediaPlayer != null)
                playUpdateProgressListener!!.updateBarP(mMediaPlayer!!.currentPosition)
        }
    }
    private var isChanging: Boolean = false
    private var isPrepare: Boolean = false
    private var myIntent: Intent? = null
    private var rv: RemoteViews? = null
    private val paths: Array<String>? = null
    private var musicIndex: Int = 0

    private var myNf: Notification? = null
    private var myNm: NotificationManager? = null


    val binder: IBinder = MyBinder()

    fun setChanging(changing: Boolean) {
        isChanging = changing
    }

    fun updataView() {
        myNm!!.notify(1, myNf)
    }

    override fun onCreate() {
        super.onCreate()
        if (mMediaPlayer != null) {
            mMediaPlayer!!.reset()
            mMediaPlayer!!.release()
            mMediaPlayer = null
        }
        mMediaPlayer = MediaPlayer()
        mMediaPlayer!!.setOnCompletionListener(this)
        creatNotification()
    }

    var CHANNEL_ONE_ID = "CHANNEL_ONE_ID"
    var CHANNEL_ONE_NAME = "CHANNEL_ONE_ID"
    private fun creatNotification() {
        var notificationChannel: NotificationChannel? = null
        //进行8.0的判断
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = NotificationChannel(CHANNEL_ONE_ID,
                    CHANNEL_ONE_NAME, NotificationManager.IMPORTANCE_LOW)
            notificationChannel.enableLights(true)
            notificationChannel.setLightColor(Color.RED)
            notificationChannel.setShowBadge(true)
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC)
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(notificationChannel)
        }
        //创建延时意图
        myIntent = Intent(this, MyMusicActivity::class.java)
        val myPi = PendingIntent.getActivity(this, 0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        //创建RemoteViews
        rv = RemoteViews(packageName, game.shenle.com.R.layout.notification_zygb)
        setOnClick()
        val bundle = Notification.Builder(this)
                .setTicker("Nature")
                .setSmallIcon(game.shenle.com.R.mipmap.ic_launcher)
                .setContentIntent(myPi)
//                .setContentTitle("这是一个测试标题")
//                .setContentText("这是一个测试内容")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            bundle.setChannelId(CHANNEL_ONE_ID)
        }
        //创建通知对象
        myNf = bundle.build()
        myNf!!.contentView = rv
        myNf!!.flags = Notification.FLAG_NO_CLEAR
        //创建notificationmanager对象
        myNm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager//        //这里的id最好保证每次都是不一样的,否则第二次发送无效
        //        myNm.notify(nId++ , myNf);
        startForeground(1, myNf)
    }

    private fun setOnClick() {
        // 设置开始监听
        val intent = Intent()
        // 为Intent对象设置Action
        //        intent.putExtra("remoteViews", rv);

        // 使用getBroadcast方法，得到一个PendingIntent对象，当该对象执行时，会发送一个广播\
        intent.action = MUSIC_GB_PRE
        val pendingIntent_pre = PendingIntent.getBroadcast(this,
                0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        rv!!.setOnClickPendingIntent(game.shenle.com.R.id.li_pre,
                pendingIntent_pre)
        intent.action = MUSIC_GB_PLAY
        val pendingIntent_play = PendingIntent.getBroadcast(this,
                0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        rv!!.setOnClickPendingIntent(game.shenle.com.R.id.li_play,
                pendingIntent_play)
        intent.action = MUSIC_GB_NEXT
        val pendingIntent_next = PendingIntent.getBroadcast(this,
                0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        rv!!.setOnClickPendingIntent(game.shenle.com.R.id.li_next,
                pendingIntent_next)

    }

    fun setPlayUpdateOnProgressBarListener(playUpdateProgressListener: PlayUpdateProgressListener) {
        this.playUpdateProgressListener = playUpdateProgressListener
    }

    fun setUpdate(update: Boolean) {
        isUpdate = update
    }

    override fun onCompletion(mp: MediaPlayer?) {
        //播放完成,播下一首
        if (mp != null && isPlaying == true && isChanging == false && mp.currentPosition != 0) {
            isPlaying = false
        }
    }

    inner class MyBinder : Binder() {

        fun getService(bundle: Bundle): MusicService {
            //            bundle.putBoolean("is_init",true);
            //            myIntent.putExtras(bundle);
            return this@MusicService
        }

        fun getRemoteViews(): RemoteViews? {
            return rv
        }

    }

    override fun onBind(arg0: Intent): IBinder? {
        return binder
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    fun onReStart() {
        if (mMediaPlayer != null && isPrepare && isPause) {
            mMediaPlayer!!.start()
        }
    }

    /**
     * 播放音乐
     *
     * @param path
     * @param position
     */
    fun play(path: String, position: Int, isTouchSeek: Boolean, dialog: Dialog?) {
        try {
            if (mMediaPlayer != null && isPrepare && isPause && !isTouchSeek && this.path == path) {
                mMediaPlayer!!.start()
                isPause = false
                isPlaying = true
            } else {
                if (dialog != null && UIUtils.getActivity() is MyMusicActivity)
                    dialog.show()
                isPrepare = false
                /* 监听播放是否完成 */
                //                if (mMediaPlayer != null) {
                //                    mMediaPlayer.stop();
                //                    mMediaPlayer.seekTo(0);
                //                    mMediaPlayer.reset();
                //                    mMediaPlayer.release();
                //                    mMediaPlayer = null;
                //                }
                //                if (mMediaPlayer == null) {
                //                    mMediaPlayer = new MediaPlayer();
                //                    mMediaPlayer.setOnCompletionListener(this);
                //                }
                mMediaPlayer!!.stop()
                mMediaPlayer!!.seekTo(position)
                mMediaPlayer!!.reset()//把各项参数恢复到初始状态
                mMediaPlayer!!.setAudioStreamType(AudioManager.STREAM_MUSIC)
                this.path = path
                mMediaPlayer!!.setDataSource(path)
                mMediaPlayer!!.prepareAsync()//进行缓冲
                mMediaPlayer!!.setOnPreparedListener(PreparedListener(position))//注册一个监听器
                mMediaPlayer!!.setOnSeekCompleteListener { mp ->
                    if (mp != null && mp.currentPosition == mp.duration) {
                        if (UIUtils.getActivity() is PlayUpdateProgressListener) {
                            if ((UIUtils.getActivity() as PlayUpdateProgressListener).getPlayStyle() == MUSIC_AUTO_NEXT)
                                (UIUtils.getActivity() as PlayUpdateProgressListener).onNextP()
                        }
                    }
                }

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     * 暂停音乐
     */
    fun pause() {
        if (mMediaPlayer != null && isPrepare && mMediaPlayer!!.isPlaying) {
            mMediaPlayer!!.pause()
            isPause = true
            isPlaying = false
            isChanging = true
        }
    }

    /**
     * 停止音乐
     */
    fun stop() {
        if (mMediaPlayer != null) {
            mMediaPlayer!!.seekTo(0)
            mMediaPlayer!!.stop()
            isPause = false
            isPlaying = false
            isChanging = false
            if (mTimerTask != null) {
                mTimerTask!!.cancel()  //将原任务从队列中移除
            }
            //            try {
            //                mMediaPlayer.prepare(); // 在调用stop后如果需要再次通过start进行播放,需要之前调用prepare函数
            //            } catch (Exception e) {
            //                e.printStackTrace();
            //            }
        }
    }

    operator fun next() {
        if (mMediaPlayer != null && musicIndex < paths!!.size) {
            mMediaPlayer!!.stop()
            try {
                mMediaPlayer!!.reset()
                mMediaPlayer!!.setDataSource(paths[musicIndex + 1])
                musicIndex++
                isPause = false
                mMediaPlayer!!.prepare()
                mMediaPlayer!!.seekTo(0)
                mMediaPlayer!!.start()
            } catch (e: Exception) {
                Log.d("hint", "can't jump next music")
                e.printStackTrace()
            }

        }
    }

    fun pre() {
        if (mMediaPlayer != null && musicIndex > 0) {
            mMediaPlayer!!.stop()
            try {
                mMediaPlayer!!.reset()
                isPause = false
                mMediaPlayer!!.setDataSource(paths!![musicIndex - 1])
                musicIndex--
                mMediaPlayer!!.prepare()
                mMediaPlayer!!.seekTo(0)
                mMediaPlayer!!.start()
            } catch (e: Exception) {
                Log.d("hint", "can't jump pre music")
                e.printStackTrace()
            }

        }
    }

    override fun onDestroy() {
        mTimer.cancel()
        isUpdate = false
        if (mMediaPlayer != null) {
            mMediaPlayer!!.stop()
            mMediaPlayer!!.release()
            mMediaPlayer = null
            path = ""
        }
    }

    /**
     * 实现一个OnPrepareLister接口,当音乐准备好的时候开始播放
     */
    private inner class PreparedListener(private val positon: Int) : MediaPlayer.OnPreparedListener {

        override fun onPrepared(mMediaPlayer: MediaPlayer?) {
            isPrepare = true
            if (playUpdateProgressListener != null) {
                playUpdateProgressListener!!.setMaxBarP(mMediaPlayer!!.duration)
                playUpdateProgressListener!!.onPreparedP()
            }
            mMediaPlayer!!.start()    //开始播放
            //            if(positon > 0) {    //如果音乐不是从头播放
            mMediaPlayer.seekTo(positon)
            isChanging = false
            isPause = false
            isPlaying = true
            //----------定时器记录播放进度---------//
            if (mTimerTask != null) {
                mTimerTask!!.cancel()  //将原任务从队列中移除
            }
            mTimerTask = object : TimerTask() {
                override fun run() {
                    if (isChanging == true) {
                        return
                    }
                    if (playUpdateProgressListener != null && mMediaPlayer != null)
                        playUpdateProgressListener!!.updateBarP(mMediaPlayer.currentPosition)
                }
            }
            mTimer.schedule(mTimerTask, 0, 10)
        }
    }

    var isPlaying = false

    companion object {
        val MUSIC_GB_PLAY = "com.lty.zhuyitong.service.MusicService_play"
        val MUSIC_GB_PRE = "com.lty.zhuyitong.service.MusicService_pre"
        val MUSIC_GB_NEXT = "com.lty.zhuyitong.service.MusicService_next"
        val MUSIC_AUTO_NEXT = 0
        val MUSIC_NOAUTO_NEXT = 1
    }

    interface PlayUpdateProgressListener {
        fun updateBarP(size: Int)
        fun onPauseP()
        fun onPlayP(path: String, progress: Int)
        fun onStopP()
        fun onNextP()
        fun onPreP()
        fun getPlayStyle(): Int
        fun setMaxBarP(duration: Int)
        fun onPreparedP()
    }
}
