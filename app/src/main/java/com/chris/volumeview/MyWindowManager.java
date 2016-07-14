package com.chris.volumeview;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.WindowManager;

import com.chris.volumeview.view.FloatWindowSmallView;

public class MyWindowManager {

	/**
	 * 小悬浮窗View的实例
	 */
	public static FloatWindowSmallView smallWindow;


	/**
	 * 小悬浮窗View的参数
	 */
	private static WindowManager.LayoutParams smallWindowParams;


	/**
	 * 用于控制在屏幕上添加或移除悬浮窗
	 */
	private static WindowManager mWindowManager;

	/**
	 * 用于获取手机可用内存
	 */
	private static ActivityManager mActivityManager;

    private static AnimationDrawable anim;

	/**
	 * 创建一个小悬浮窗。初始位置为屏幕的右部中间位置。
	 * 
	 * @param context
	 *            必须为应用程序的Context.
	 */
	public static void createSmallWindow(final Context context) {
		WindowManager windowManager = getWindowManager(context);
		int screenWidth = windowManager.getDefaultDisplay().getWidth();
		int screenHeight = windowManager.getDefaultDisplay().getHeight();
		if (smallWindow == null) {
			smallWindow = new FloatWindowSmallView(context);
			if (smallWindowParams == null) {
				smallWindowParams = new WindowManager.LayoutParams();
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
					smallWindowParams.type = WindowManager.LayoutParams.TYPE_TOAST;
				} else {
					smallWindowParams.type = WindowManager.LayoutParams.TYPE_PHONE;
				}
				smallWindowParams.format = PixelFormat.RGBA_8888;
				smallWindowParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
						| WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
				smallWindowParams.gravity = Gravity.LEFT | Gravity.TOP;
				smallWindowParams.width = FloatWindowSmallView.viewWidth;
				smallWindowParams.height = FloatWindowSmallView.viewHeight;
				smallWindowParams.x = screenWidth-screenWidth/6;
				smallWindowParams.y = screenHeight / 8;
			}
			smallWindow.setParams(smallWindowParams);
            smallWindow.ivAudioPlayNotity.setBackgroundResource(R.drawable.audio_notify_frame);
            anim = (AnimationDrawable) smallWindow.ivAudioPlayNotity.getBackground();
            anim.start();
			windowManager.addView(smallWindow, smallWindowParams);
		}
	}

	/**
	 * 将小悬浮窗从屏幕上移除。
	 * 
	 * @param context
	 *            必须为应用程序的Context.
	 */
	public static void removeSmallWindow(Context context) {
		if (smallWindow != null) {
			WindowManager windowManager = getWindowManager(context);
            if (anim != null && anim.isRunning()) {
                anim.stop();
            }
			windowManager.removeView(smallWindow);
			smallWindow = null;
		}

	}




	/**
	 * 是否有悬浮窗(包括小悬浮窗和大悬浮窗)显示在屏幕上。
	 * 
	 * @return 有悬浮窗显示在桌面上返回true，没有的话返回false。
	 */
	public static boolean isWindowShowing() {
		return smallWindow != null;
	}

	/**
	 * 如果WindowManager还未创建，则创建一个新的WindowManager返回。否则返回当前已创建的WindowManager。
	 * 
	 * @param context
	 *            必须为应用程序的Context.
	 * @return WindowManager的实例，用于控制在屏幕上添加或移除悬浮窗。
	 */
	private static WindowManager getWindowManager(Context context) {
		if (mWindowManager == null) {
			mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		}
		return mWindowManager;
	}

	/**
	 * 如果ActivityManager还未创建，则创建一个新的ActivityManager返回。否则返回当前已创建的ActivityManager。
	 * 
	 * @param context
	 *            可传入应用程序上下文。
	 * @return ActivityManager的实例，用于获取手机可用内存。
	 */
	private static ActivityManager getActivityManager(Context context) {
		if (mActivityManager == null) {
			mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		}
		return mActivityManager;
	}


	/**
	 * 获取当前可用内存，返回数据以字节为单位。
	 * 
	 * @param context
	 *            可传入应用程序上下文。
	 * @return 当前可用内存。
	 */
	private static long getAvailableMemory(Context context) {
		ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
		getActivityManager(context).getMemoryInfo(mi);
		return mi.availMem;
	}

}