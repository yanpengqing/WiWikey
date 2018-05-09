
package com.ssdj.cloudroom.service;

import org.seny.android.utils.ALog;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.wiwikeyandroid.App;

/**
 * @description 守护进程服务 在应用程序被强制停止后，守护进程会命令开启该服务，该服务会开启需要开启的其他的服务；
 */
public class DaemonService extends Service {

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if(!isServiceRunning()){
//				Intent startServiceIntent = new Intent(DaemonService.this, MyService.class);
//				startServiceIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//				startService(startServiceIntent);
			}
			//Toast.makeText(getApplication(), "我还没死", 1).show();
		};
	};
	private String proName;
	private int cmdType;

	/**
	 * 定义本地c方法
	 * 
	 * @return
	 * 
	 */
	public native static void forkDaemon(String proName, int cmdType);

	/**
	 * 加载动态库
	 */
	static {
		System.loadLibrary("daemon");
	}
	public static final String START_DAEMON_ACTION = "com.wiwikeyandroid.ACTION_START_DAEMON";

	@Override
	public void onCreate() {
		super.onCreate();
		ALog.e("Daemon服务已启动0！");
		new Thread() {
			public void run() {
				while (true) {

					try {
						Thread.sleep(6000);
					} catch (InterruptedException e) {
						
						e.printStackTrace();
					}
					handler.sendEmptyMessage(1);
				}

			};
		}.start();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		ALog.e("Daemon服务已启动1！");
		if (intent == null) {
			stopSelf();
			return super.onStartCommand(intent, flags, startId);
		}
		String action = intent.getAction();
		proName = this.getApplicationInfo().packageName;
		cmdType = App.systemVersion;
		if (START_DAEMON_ACTION.equals(action)) {
			Log.e("守护进程", "packageName："+proName+"action:"+action);
			ALog.e("Daemon服务已重新启动！！");
			forkDaemon(proName, cmdType);// 重新开启守护进程
		}
		stopSelf();
		return super.onStartCommand(intent, flags, startId);
	}

	/**
	 * @description
	 */
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	/**
	 * 检测开门服务是否在运行
	 * @return
	 */
	private boolean isServiceRunning() {
		ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
			if ("com.ssdj.cloudroom.service.MyService".equals(service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}
}
