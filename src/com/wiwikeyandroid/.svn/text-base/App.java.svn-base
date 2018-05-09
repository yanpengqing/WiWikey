package com.wiwikeyandroid;

import io.agora.AgoraAPI;
import io.agora.rtc.RtcEngine;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.os.Process;

import com.ssdj.cloudroom.service.DaemonService;
import com.wiwikeyandroid.agora.BaseEngineEventHandlerActivity;
import com.wiwikeyandroid.agora.MessageHandler;
import com.wiwikeyandroid.utils.GlobalWiwikey;
import com.xiaomi.mipush.sdk.MiPushClient;

public class App extends Application {

	private static final String APP_ID = "2882303761517459789";
	private static final String APP_KEY = "5101745971789";

	/**
	 * 全局Context，方便引用
	 */
	public static App application;

	public static int systemVersion;
	private RtcEngine rtcEngine;
	public AgoraAPI m_agoraAPI;
	private MessageHandler messageHandler;

	@Override
	public void onCreate() {
		super.onCreate();
		application = this;

		if (shouldInit()) {
			MiPushClient.registerPush(this, APP_ID, APP_KEY);
		}
		removeTempFromPref();
		initSystemVersion(application);
		startDaemonService();

		// 统计数据
		// Crasheye.initWithNativeHandle(this, "06798b00");

		// Log.d("Crasheye", "0");

		messageHandler = new MessageHandler();
	}

	private boolean shouldInit() {
		ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
		List<RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
		String mainProcessName = getPackageName();
		int myPid = Process.myPid();
		for (RunningAppProcessInfo info : processInfos) {
			if (info.pid == myPid && mainProcessName.equals(info.processName)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @description 启动守护进程
	 */
	 private void startDaemonService() {
	 Intent intent = new Intent(this, DaemonService.class);
	 intent.setAction(DaemonService.START_DAEMON_ACTION);
	 this.startService(intent);
	 }

	/**
	 * @description 初始化系统版本
	 * @param context
	 */
	private void initSystemVersion(Context context) {
		java.lang.Process p = null; 
		try {
			p = Runtime.getRuntime().exec("/system/bin/am abcd");// 确保输出错误日志
			InputStreamReader isr = new InputStreamReader(p.getErrorStream());
			char[] buf = new char[1024];
			while (isr.read(buf) > 0) {
				String tmp = new String(buf);
				if (tmp.contains("--user"))
					this.systemVersion = 1;
			}
		} catch (IOException e) {
			// Do noting
		} catch (NullPointerException e) {
			// Do noting
		} finally {
			if (p != null)
				p.destroy();
		}
	}
	private void removeTempFromPref()
	{
		SharedPreferences sp = getSharedPreferences(
				GlobalWiwikey.APPLICATION_NAME, MODE_PRIVATE);
		sp.edit().remove(GlobalWiwikey.PREF_TEMP_IMAGES).commit();
	}
	public void setRtcEngine(String vendorKey) {

		if (rtcEngine == null) {
			rtcEngine = RtcEngine.create(getApplicationContext(), vendorKey,
					messageHandler);
		}
		//rtcEngine.setLogFile(Environment.getExternalStorageDirectory()+"/agoraLogs.log");
	}

	public AgoraAPI getAgoraAPI() {
		if (m_agoraAPI == null) {
			m_agoraAPI = AgoraAPI.getInstanceWithMedia(this, getRtcEngine());
			// m_agoraAPI = AgoraAPI.getInstanceWithKey(this, vid);
		}
		return m_agoraAPI;
	}

	public RtcEngine getRtcEngine() {

		return rtcEngine;
	}

	public void setEngineEventHandlerActivity(
			BaseEngineEventHandlerActivity engineEventHandlerActivity) {
		messageHandler.setActivity(engineEventHandlerActivity);
	}

}
