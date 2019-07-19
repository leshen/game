package game.shenle.com.service

import android.app.Dialog
import android.app.Service
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Binder
import android.os.Bundle
import android.os.IBinder

import java.util.Timer
import java.util.TimerTask


class MusicService : Service(), MediaPlayer.OnCompletionListener {
    var mMediaPlayer: MediaPlayer? = null       //媒体播放器对象
    private var path: String? = ""                        //音乐文件路径
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
                playUpdateProgressListener!!.updateBar(mMediaPlayer!!.currentPosition)
        }
    }
    private var isChanging: Boolean = false
    private val nId: Int = 0
    private var isPrepare: Boolean = false
    var aid: String? = null
    private var next_aid: String? = null
    //    private Intent myIntent;
    //    private RemoteViews rv;
    private val paths: Array<String>? = null
    private val musicIndex: Int = 0
    //
    //    private void setOnClick() {
    //        // 设置开始监听
    //        Intent intent = new Intent();
    //        // 为Intent对象设置Action
    ////        intent.putExtra("remoteViews", rv);
    //
    //// 使用getBroadcast方法，得到一个PendingIntent对象，当该对象执行时，会发送一个广播\
    //        intent.setAction(MUSIC_GB_PRE);
    //        PendingIntent pendingIntent_pre = PendingIntent.getBroadcast(this,
    //                0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    //        rv.setOnClickPendingIntent(R.id.li_pre,
    //                pendingIntent_pre);
    //        intent.setAction(MUSIC_GB_PLAY);
    //        PendingIntent pendingIntent_play = PendingIntent.getBroadcast(this,
    //                0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    //        rv.setOnClickPendingIntent(R.id.li_play,
    //                pendingIntent_play);
    //        intent.setAction(MUSIC_GB_NEXT);
    //        PendingIntent pendingIntent_next = PendingIntent.getBroadcast(this,
    //                0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    //        rv.setOnClickPendingIntent(R.id.li_next,
    //                pendingIntent_next);
    //
    //    }


    val binder: IBinder = MyBinder()

    fun setChanging(changing: Boolean) {
        isChanging = changing
    }

    //    public void updataView() {
    //        myNm.notify(1, myNf);
    //    }

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

    //    private Notification myNf;
    //    private NotificationManager myNm;
    private fun creatNotification() {
        //        //1.创建通知对象
        //        myNf = new Notification();
        //        //2.通知的一些常用设置
        //        //图标
        //        myNf.icon = R.drawable.ic_launcher;
        //        //通知时间
        //        myNf.when = System.currentTimeMillis();
        //        myNf.tickerText = "这是自定义的通知";
        //        myNf.flags = Notification.FLAG_AUTO_CANCEL;
        //        //3.创建延时意图
        //        myIntent = new Intent(this, HomeZYGBDetailActivity.class);
        //        PendingIntent myPi = PendingIntent.getActivity(this, 0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        //        /**
        //         * 注意：这个字段是要配合nf.contentView这个字段一起使用的，如果只是设置一个会报错
        //         * nf.contentIntent = pi ;
        //         */
        //        /******************
        //         *没有设置contentIntent时:java.lang.IllegalArgumentException: contentIntent required: pkg=com.lw.remoteviewsdemo id=0 notification=Notification(vibrate=null,sound=null,defaults=0x0,flags=0x10)
        //         * ****************************/
        //        //4.创建RemoteViews
        //        rv = new RemoteViews(getPackageName(), R.layout.notification_zygb);
        ////
        ////        //给remoteviews手动设置值
        ////        rv.setTextViewText(R.id.msg, "我是自定义通知");
        ////        rv.setTextViewText(R.id.des, "我是对自定义通知的一些简单描述信息");
        //        setOnClick();
        //        myNf.contentView = rv;
        //        myNf.contentIntent = myPi;
        //        //5.创建notificationmanager对象
        //        myNm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        ////        //这里的id最好保证每次都是不一样的,否则第二次发送无效
        ////        myNm.notify(nId++ , myNf);
        //        startForeground(1, myNf);
    }

    fun setPlayUpdateOnProgressBarListener(playUpdateProgressListener: PlayUpdateProgressListener) {
        this.playUpdateProgressListener = playUpdateProgressListener
    }

    fun setUpdate(update: Boolean) {
        isUpdate = update
    }

    override fun onCompletion(mp: MediaPlayer?) {
        //TODO 播放完成,播下一首
    }
    fun setNextAid(next_aid: String) {
        this.next_aid = next_aid
    }

    inner class MyBinder : Binder() {

        fun getService(bundle: Bundle): MusicService {
            //            bundle.putBoolean("is_init",true);
            //            myIntent.putExtras(bundle);
            return this@MusicService
        }

        //        public RemoteViews getRemoteViews() {
        //            return rv;
        //        }
    }

    override fun onBind(arg0: Intent): IBinder? {
        return binder
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        //        if(mMediaPlayer.isPlaying()) {
        //            stop();
        //        }
        //        path = intent.getStringExtra("url");
        //        int msg = intent.getIntExtra("MSG", 0);
        //        if(msg == PlayerMsg.PLAY_MSG) {
        //            play(0);
        //        } else if(msg == PlayerMsg.PAUSE_MSG) {
        //            pause();
        //        } else if(msg == PlayerMsg.STOP_MSG) {
        //            stop();
        //        }else if(msg == PlayerMsg.PLAY_PRE) {
        //            pre();
        //        }else if(msg == PlayerMsg.PLAY_NEXT) {
        //            next();
        //        }
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
    fun play(path: String?, position: Int, isTouchSeek: Boolean, dialog: Dialog?) {
        try {
            if (mMediaPlayer != null && isPrepare && isPause && !isTouchSeek && this.path == path) {
                mMediaPlayer!!.start()
                isPause = false
                isPlaying = true
            } else {
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
                //                mMediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
                //                    @Override
                //                    public void onSeekComplete(MediaPlayer mp) {
                //                        if (next_aid!=null&&mp!=null&&mp.getCurrentPosition()==mp.getDuration()) {
                //                            if (UIUtils.getActivity() instanceof HomeZYGBDetailActivity) {
                //                                ((HomeZYGBDetailActivity) UIUtils.getActivity()).getHeadTitleHolder().onNext();
                //                            } else {
                //                                aid = next_aid;
                //                                next_aid= null;
                //                                AppInstance.getInstance().getAsyncHttpClient().get(UIUtils.getContext(), ConstantsUrl.ZYGB_DETAIL + aid,
                //                                        new MyAsyncHttpResponseHandler("detail"));
                //                            }
                //                        }
                //                    }
                //                });

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

    //    public void next() {
    //        if (mMediaPlayer != null
    //                && musicIndex < paths.length
    //                ) {
    //            mMediaPlayer.stop();
    //            try {
    //                mMediaPlayer.reset();
    //                mMediaPlayer.setDataSource(paths[musicIndex+1]);
    //                musicIndex++;
    //                isPause = false;
    //                mMediaPlayer.prepare();
    //                mMediaPlayer.seekTo(0);
    //                mMediaPlayer.start();
    //            } catch (Exception e) {
    //                Log.d("hint", "can't jump next music");
    //                e.printStackTrace();
    //            }
    //        }
    //    }
    //
    //    public void pre() {
    //        if (mMediaPlayer != null
    //                && musicIndex > 0
    //                ) {
    //            mMediaPlayer.stop();
    //            try {
    //                mMediaPlayer.reset();
    //                isPause = false;
    //                mMediaPlayer.setDataSource(paths[musicIndex-1]);
    //                musicIndex--;
    //                mMediaPlayer.prepare();
    //                mMediaPlayer.seekTo(0);
    //                mMediaPlayer.start();
    //            } catch (Exception e) {
    //                Log.d("hint", "can't jump pre music");
    //                e.printStackTrace();
    //            }
    //        }
    //    }

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
                playUpdateProgressListener!!.setMaxBar(mMediaPlayer!!.duration)
                playUpdateProgressListener!!.onPrepared()
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
                        playUpdateProgressListener!!.updateBar(mMediaPlayer.currentPosition)
                }
            }
            mTimer.schedule(mTimerTask, 0, 10)
        }
    }

    companion object {
        val MUSIC_GB_PLAY = "com.lty.zhuyitong.service.MusicService_play"
        val MUSIC_GB_PRE = "com.lty.zhuyitong.service.MusicService_pre"
        val MUSIC_GB_NEXT = "com.lty.zhuyitong.service.MusicService_next"
        var isPlaying = false
    }
    interface PlayUpdateProgressListener {
        fun updateBar(size: Int)
        fun onPause()
        fun onPlay(path: String, progress: Int)
        fun onStop()
        fun onNext()
        fun onPre()
        fun setMaxBar(duration: Int)
        fun onPrepared()
    }
}
