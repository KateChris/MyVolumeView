package com.chris.volumeview;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 音频播放service
 */
public class MusicPlayerService extends Service {
    /**
     * 是否正在播放中
     */
    boolean isplaying=true;
    /**
     * 定时器，定时进行检测当前应该创建还是移除悬浮窗。
     */
    private Timer timer;
    Context context;
    VolumeViewController volumeViewController;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        volumeViewController = new VolumeViewController(this);
        EventBus.getDefault().register(this);
    }


    @Override
    public void onDestroy() {
        if (timer!=null){
            timer.cancel();
            timer = null;
        }
        EventBus.getDefault().unregister(this);//反注册EventBus
        super.onDestroy();
        stopSelf();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 开启定时器，每隔0.5秒刷新一次
        if (timer == null) {
            timer = new Timer();
            timer.scheduleAtFixedRate(new RefreshTask(), 0, 500);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    class RefreshTask extends TimerTask {

        @Override
        public void run() {
                volumeViewController.showAudioView(isplaying);
        }
    }

    @Subscribe
    public void onEventMainThread(FirstEvent event) {

        isplaying = event.getMsg();
        if (event.getMsg()==true){
            volumeViewController.showAudioView();
        }else{
            volumeViewController.hideAudioView();
        }
    }


}
