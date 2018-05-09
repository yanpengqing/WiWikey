/**
 * Created by harry on Nov 24, 2011 Copyright : FOCUSONE Inc. All Rights
 * Reserved.
 */

package com.wiwikeyandroid.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;

/**
 * This utils helps to do the following: 1) If device is connected to mobile
 * network 2) If device is connected to wifi 3) If device is connected, either
 * to mobile network or wifi.
 * 
 * @author harry
 */
public class NetworkConnectivityUtils {

	/**
	 * Log
	 */
	private static final String TAG = NetworkConnectivityUtils.class
			.getSimpleName();

	public static boolean isConnectedToMobile(Context context) {

		ConnectivityManager conMan = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		// mobile
		State mobile = conMan.getNetworkInfo(0).getState();

		return mobile == State.CONNECTED;

	}

	public static boolean isConnectedToWifi(Context context) {

		ConnectivityManager conMan = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		// wifi
		State wifi = conMan.getNetworkInfo(1).getState();

		return wifi == State.CONNECTED;
	}

	/**
	 * This is a simple way to check if you are CONNECTED or is CONNECTING to
	 * network. NOTE: you need to set <uses-permission
	 * android:name="android.permission.ACCESS_NETWORK_STATE"></uses-permission>
	 * in your AndroidManifest.xml
	 * 
	 * @param context
	 *            a context used to getSystemInfo
	 * @return
	 */
	public static boolean isConnectedToNetwork(Context context) {

		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		boolean isConnected = netInfo != null && netInfo.isConnected();
		return isConnected;
	}

	/**
	 * 检查当前网络是否可用
	 * 
	 * @param context
	 * @return
	 */

	public static boolean isNetworkAvailable(Context context) {
		// 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (connectivityManager == null) {
			return false;
		} else {
			// 获取NetworkInfo对象
			NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

			if (networkInfo != null && networkInfo.length > 0) {
				for (int i = 0; i < networkInfo.length; i++) {
					// 判断当前网络状态是否为连接状态
					if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static final boolean ping() {
		try {
			String ip = "www.baidu.com";//
			Process p = Runtime.getRuntime().exec("ping -c 3 -w 100 " + ip);// ping网址3次
			// 读取ping的内容，可以不加
			InputStream input = p.getInputStream();
			BufferedReader in = new BufferedReader(new InputStreamReader(input));
			StringBuffer stringBuffer = new StringBuffer();
			String content = "";
			while ((content = in.readLine()) != null) {
				stringBuffer.append(content);
			}
			// ping的状态
			int status = p.waitFor();
			if (status == 0) {
				return true;
			} else {
			}
		} catch (IOException e) {
		} catch (InterruptedException e) {
		} finally {
		}
		return false;
	}

	private NetworkConnectivityUtils() {

	}
}
