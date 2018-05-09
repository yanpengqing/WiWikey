/**
 * 
 */
package com.wiwikeyandroid.utils;

import java.util.List;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

/**
 * @author chenwen
 * 
 */
public class WifiOperator {
	/**
	 * wifiManager
	 */
	private WifiManager wm;

	/**
	 * 上下文
	 */
	private Context mContext;

	private String sSSID = "";
	/**
	 * <默认构造函数>
	 * 
	 * @param context
	 *            上下文
	 */
	public WifiOperator(Context context) {
		mContext = context;
		wm = (WifiManager) mContext.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
	}
	
	public boolean isOpenWifi(){
		if (!wm.isWifiEnabled()&& WifiManager.WIFI_STATE_ENABLING != wm.getWifiState()) {
			return true;
		}else{
			return false;
		}
	}
	
	public boolean openWifi(){
		// 无线未打开时，开启无线
		if (!wm.isWifiEnabled()&& WifiManager.WIFI_STATE_ENABLING != wm.getWifiState()) {
			return wm.setWifiEnabled(true);
		}else{
			return false;
		}
	}

	/**
	 * 切换网络
	 * 
	 * @param type
	 *            网络类型(1为中心网络 2 为车载网络)
	 * @return 结果码 -1-本地设置为空；0-连接成功；1-已经为当前连接
	 * @see [类、类#方法、类#成员]
	 */
	public int access2Wifi(String ssid) {
		// 获取本地的配置信息
		sSSID = "\"" + ssid + "\"";

		List<WifiConfiguration> configurations = wm.getConfiguredNetworks();
		WifiConfiguration config = null;
		boolean isExisted = false;
		int networkId = -1;
		for (int i = configurations.size() - 1; i >= 0; i--) {
			config = configurations.get(i);
			if (config.SSID.equals(sSSID)) {
				networkId = config.networkId;
				isExisted = true;
				break;
			}
		}
		if (!isExisted) {
			// 安全类型 无、WEP(128)、WPA(TKIP)、WPA2(AES)
			config = new WifiConfiguration();
			// 名称
			config.SSID = sSSID;
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
			config.hiddenSSID = false;

			config.priority = 30;
			config.status = WifiConfiguration.Status.ENABLED;
			config.allowedAuthAlgorithms
					.set(WifiConfiguration.AuthAlgorithm.OPEN);

			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
			config.allowedGroupCiphers
					.set(WifiConfiguration.GroupCipher.WEP104);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);

			config.allowedPairwiseCiphers
					.set(WifiConfiguration.PairwiseCipher.TKIP);
			config.allowedPairwiseCiphers
					.set(WifiConfiguration.PairwiseCipher.CCMP);
			config.allowedPairwiseCiphers
					.set(WifiConfiguration.PairwiseCipher.NONE);

			config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
			// 必须添加，否则无线路由无法连接
			config.allowedProtocols.set(WifiConfiguration.Protocol.RSN);

			networkId = wm.addNetwork(config);
			if (networkId != -1) {
				wm.saveConfiguration();
			}
		} else {
			// 获取当前的wifi连接
			WifiInfo curConnection = wm.getConnectionInfo();
			if (curConnection != null && sSSID.equals(curConnection.getSSID())) {
				// 已经是当前连接
				return 1;
			}

			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
			wm.updateNetwork(config);
		}

		if (networkId != -1) {
			wm.disconnect();
			wm.enableNetwork(networkId, true);
		}
		return 0;
	}
	
	public boolean isConnMyWifi() {
		WifiInfo curConnection = wm.getConnectionInfo();
		if (curConnection != null && sSSID.equals(curConnection.getSSID())) {
			// 已经是当前连接
			return true;
		}else{
			return false;
		}
	}
	
	public boolean isWifiConnect(Context context) {
		ConnectivityManager connManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
	    return mWifi.isConnected();
	}
}
