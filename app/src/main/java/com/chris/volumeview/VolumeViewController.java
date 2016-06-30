package com.chris.volumeview;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import java.util.List;

/**
 * 作者：Chris on 2016/6/29 17:03
 * 邮箱：katechris@126.com
 */
public class VolumeViewController {
    Context context;

    public VolumeViewController(Context context) {
        this.context = context;
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
//                case PLAY_NET_ERROR:
//                    ToastUtils.toastShow(
//                            MusicPlayerService.this,
//                            getResources().getString(
//                                    R.string.network_error));
//                    break;

                default:
                    break;
            }
        }
    };

    /**
     * 判断是否需要并显示悬浮窗
     */
    public void showAudioView() {
        if (!isMyself() && MyWindowManager.isWindowShowing()) {//非本应用内且已显示则移除
            handler.post(new Runnable() {
                @Override
                public void run() {
                    MyWindowManager.removeSmallWindow(context);
                }
            });
        } else if (isMyself() && !MyWindowManager.isWindowShowing()) {//本应用内且未显示则创建
            handler.post(new Runnable() {
                @Override
                public void run() {
                    MyWindowManager.createSmallWindow(context);
                }
            });
        }
    }

    /**
     * 判断是否需要并显示悬浮窗
     */
    public void showAudioView(boolean isplaying) {
        if (!isMyself() && MyWindowManager.isWindowShowing() && isplaying) {//非本应用内且已显示则移除
            handler.post(new Runnable() {
                @Override
                public void run() {
                    MyWindowManager.removeSmallWindow(context);
                }
            });
        } else if (isMyself() && !MyWindowManager.isWindowShowing() && isplaying) {//本应用内且未显示则创建
            handler.post(new Runnable() {
                @Override
                public void run() {
                    MyWindowManager.createSmallWindow(context);
                }
            });
        }
    }

    /**
     * 隐藏悬浮窗
     */
    public void hideAudioView() {
        MyWindowManager.removeSmallWindow(context);
    }

    /**
     * 当前页是否为本应用内
     */
    private boolean isMyself() {
        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> rti = mActivityManager.getRunningTasks(1);
        return context.getPackageName().equals(rti.get(0).topActivity.getPackageName());
    }
}
