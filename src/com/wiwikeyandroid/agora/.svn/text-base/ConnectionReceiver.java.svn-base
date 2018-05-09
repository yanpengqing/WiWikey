package com.wiwikeyandroid.agora;

import org.seny.android.utils.ALog;
import org.seny.android.utils.MyToast;

import com.wiwikeyandroid.App;
import com.xiaomi.mipush.sdk.MiPushClient;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionReceiver extends BroadcastReceiver {
	
	private static final String APP_ID = "2882303761517459789";
	private static final String APP_KEY = "5101745971789";

	@Override
	public void onReceive(Context context, Intent intent) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mobNetInfo = connectivityManager
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		NetworkInfo wifiNetInfo = connectivityManager
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

		if (!mobNetInfo.isConnected() && !wifiNetInfo.isConnected()) {
			// 网络不可以用
			MyToast.show(context, "网络不可用");
			
			ALog.d("网络不可以用");
		} else {
			context.startService(new Intent(context, AgoraService.class));
			MiPushClient.registerPush(context, APP_ID, APP_KEY);
			ALog.d("网络来啦！");
		}
	}
}
