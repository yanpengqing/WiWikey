package com.wiwikeyandroid.agora;

import io.agora.AgoraAPI;

import org.seny.android.utils.ALog;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.wiwikeyandroid.App;
import com.wiwikeyandroid.utils.DesUtil;
import com.wiwikeyandroid.utils.PrefUtils;

import de.greenrobot.event.EventBus;

/**
 * 声网Service 服务
 * 
 */

public class AgoraService extends Service {

	public AgoraAPI m_agoraAPI;
	boolean callNum = false;
	protected static final String TAG = "AgoraService";
	private String vid;

	@Override
	public void onCreate() {

		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// String acount = intent.getStringExtra("acount");
		try {
			vid = DesUtil.decrypt(PrefUtils.getString(this, "agoraVid", ""),
					DesUtil.DESKEY);
		} catch (Exception e) {
			e.printStackTrace();
		}
		ALog.d("agoraVid:" + vid);
		App.application.setRtcEngine(vid);

		m_agoraAPI = ((App) getApplication()).getAgoraAPI();
	
		m_agoraAPI.login(vid, PrefUtils.getString(this, "mobile", ""),
				"token_reserved", 0, "");

		new Thread(new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < 1; i++) {
					final int fi = i;

					m_agoraAPI.callbackSet(new AgoraAPI.CallBack() {
						// int buf[] = new int[1000*1000*1];
						// 成功登录后,如果断线，单次重连最大10s，会调用
						@Override
						public void onReconnecting(int nretry) {
							System.out.println(fi);
							Log.i(TAG, "成功登录后,如果断线，单次重连最大10s，会调用");

						}
					});

					m_agoraAPI.callbackSet(new AgoraAPI.CallBack() {

						@Override
						public void onLoginSuccess(int uid, int fd) {
							Log.i(TAG, "登录成功");
						}

						/*
						 * // int buf[] = new int[1000*1000*1]; //
						 * 成功登录后,如果断线，单次重连最大10s，会调用
						 * 
						 * 
						 * @Override public void onReconnecting(int nretry) {
						 * System.out.println(fi); Log.i(TAG,
						 * "成功登录后,如果断线，单次重连最大10s"); }
						 */

						@Override
						public void onReconnected(int fd) {
							Log.i(TAG, "成功登录后,断线重练成功");

						};
						
						
						@Override
						public void onLoginFailed(int ecode) {
							Log.i(TAG, "登录失败 " + ecode);
							// m_agoraAPI.login(vid, PrefUtils.getString(
							// AgoraService.this, "mobile", ""),
							// "token_reserved", 0, "");
						}

						@Override
						public void onLogout(int ecode) {
							// 账号退出了，重新登录
							Log.i(TAG, "退出了" + ecode);
							m_agoraAPI.login(vid, PrefUtils.getString(
									AgoraService.this, "mobile", ""),
									"token_reserved", 0, "");
						}

						@Override
						public void onLog(String txt) {
							// ActivityLogin.this.log(txt);
							Log.e("sdk2", txt);
						}

						@Override
						public void onInviteFailed(String channelID,
								String account, int uid, int ecode) {
							// ecode==700),对方还没收到邀请,不代表对方不在线，只是邀请信号还没传过去

							Log.i(TAG, "对方还没收到邀请" + channelID + " account "
									+ account + "uid" + uid);
							// m_agoraAPI.channelLeave(channelID);

						}

						@Override
						public void onChannelJoined(String chanID) {

							Log.i(TAG, "本人加入房间成功" + chanID);

						}

						// 收到邀请,此处打开新界面
						@Override
						public void onInviteReceived(final String channleID,
								final String account, final int uid) {

							Log.i(TAG, "收到邀请来自 " + account + ":" + uid + "加入 "
									+ channleID);

							Intent callledIntent = new Intent(getBaseContext(),
									CallledActivity.class);
							callledIntent.putExtra("channleID", channleID);
							callledIntent.putExtra("account", account);
							callledIntent
									.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							AgoraService.this.startActivity(callledIntent);

						}

						@Override
						public void onChannelUserJoined(String account, int uid) {
							Log.i(TAG, account + ":" + uid + " 进来了");
						}

						@Override
						public void onChannelJoinFailed(String chanID, int ecode) {
							Log.i(TAG, "加入失败  " + chanID + " : ecode " + ecode);
						}

						@Override
						public void onChannelUserLeaved(String account, int uid) {
							Log.i(TAG, account + ":" + uid + " leaved");
						}

						@Override
						public void onChannelUserList(String[] accounts,
								int[] uids) {
							Log.i(TAG, "Channel user list:");
							for (int i = 0; i < accounts.length; i++) {
								Log.i(TAG, accounts[i] + ":" + uids[i]);
							}
						}

						// 对方收到邀请, 相当于对方振铃，落地电话不一定振铃？？
						@Override
						public void onInviteReceivedByPeer(String channleID,
								String account, int uid) {
							Log.i(TAG, "对方收到邀请,对方振铃" + account + ":" + uid);
							EventBus.getDefault().post(
									new MsgEvent("onInviteReceivedByPeer"));

						}

						// 对方拒绝邀请
						@Override
						public void onInviteRefusedByPeer(String channelID,
								String account, int uid) {
							Log.i(TAG, "对方拒绝邀请" + account + ":" + uid);

							EventBus.getDefault().post(
									new MsgEvent("onInviteRefusedByPeer"));
							// m_agoraAPI.channelLeave(channelID);
						}

						// 离开频道
						public void onChannelLeaved(String channelID, int ecode) {
							Log.i(TAG, "离开频道成功,channelID:" + channelID);
						};

						// 对方结束邀请, 对方开始离开Channel,邀请落地电话对方拒绝或者结束通话也会走这
						@Override
						public void onInviteEndByPeer(String channelID,
								String account, int uid) {
							Log.i(TAG, "对方结束邀请, 对方开始离开Channel,account:"
									+ account + ":" + uid);
							EventBus.getDefault().post(
									new MsgEvent("onInviteEndByPeer"));
							m_agoraAPI.channelLeave(channelID);

						}

						@Override
						public void onInviteEndByMyself(String channelID,
								String account, int uid) {
							Log.i(TAG, "Invitation end bymyself " + account
									+ ":" + uid);
							m_agoraAPI.channelLeave(channelID);
						}

						// 对方接受邀请, 对方开始加入Channel
						@Override
						public void onInviteAcceptedByPeer(String channleID,
								String account, int uid) {
							Log.i(TAG, "对方接受邀请,对方开始加入" + channleID + account
									+ ":" + uid);
							EventBus.getDefault().post(
									new MsgEvent("onInviteAcceptedByPeer"));
						}

						@Override
						public void onMessageChannelReceive(String channelID,
								String account, int uid, String msg) {
							Log.i(TAG, "recv channel msg " + channelID + " "
									+ account + " : " + msg);
							Log.i(TAG, "收到消息: " + msg + "channelID" + channelID);
						}
					});
				}

			}
		}).start();

		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		// 服务停止手动退出
		m_agoraAPI.logout();
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent intent) {

		return null;
	}

}
