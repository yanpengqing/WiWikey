package com.wiwikeyandroid.agora;

import io.agora.AgoraAPI;
import io.agora.rtc.RtcEngine;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.wiwikeyandroid.R;
import com.wiwikeyandroid.db.DBManager;
import com.wiwikeyandroid.modules.Contacts.model.Person;
import com.wiwikeyandroid.utils.ContactUtils;
import com.wiwikeyandroid.utils.DesUtil;
import com.wiwikeyandroid.utils.PrefUtils;

import de.greenrobot.event.EventBus;

public class CallledActivity extends Activity {
	@InjectView(R.id.user_local_voice_bg)
	ImageView userLocalVoiceBg;
	@InjectView(R.id.iv_icon)
	ImageView ivIcon;
	@InjectView(R.id.tv_name)
	TextView tvName;
	@InjectView(R.id.tv_type)
	TextView tvType;
	@InjectView(R.id.ll_refuse)
	LinearLayout llRefuse;
	@InjectView(R.id.ll_accept)
	LinearLayout llAccept; // 视频接听
	@InjectView(R.id.ll_text_accept)
	LinearLayout textAccept;
	@InjectView(R.id.ll_text_refuse)
	LinearLayout textRefuse;
	@InjectView(R.id.ll_called)
	// 视频语音被叫界面
	LinearLayout llCalled;
	// @InjectView(R.id.ll_calling) //语音通话中界面
	// LinearLayout llCalling;
	boolean isFinish = true;
	private String vid;
	private DBManager dbManager;// 数据库管理工具
	private Person mPerson; // 通话记录
	private Long startTimeLong; // 通话开始时间
	private int time = 0;
	private Timer timer;
	private String displayNameByPhone; // 显示的名字

	private String channleID; // 频道号
	private String account; // 对方登录的账号,手机号码。
	private MediaPlayer mediaPlayer = new MediaPlayer();
	private Vibrator vibrator;

	private RtcEngine rtcEngine;
	public AgoraAPI m_agoraAPI;
	private MessageHandler messageHandler;
	long[] vibrates = { 0, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 10000 };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_called);
		getWindow().addFlags(
				WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
						| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
						| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		ButterKnife.inject(this);

		dbManager = new DBManager(this);
		vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		messageHandler = new MessageHandler();
		try {
			vid = DesUtil.decrypt( 
					PrefUtils.getString(this, "agoraVid", ""), DesUtil.DESKEY);
		}catch (Exception e1) {
			e1.printStackTrace();
		}
		rtcEngine = RtcEngine.create(this, vid, messageHandler);
		m_agoraAPI = AgoraAPI.getInstanceWithMedia(this, rtcEngine);
		channleID = getIntent().getStringExtra("channleID");
		tvType.setText(channleID.substring(0, 5).equals("video")
				|| channleID.substring(0, 4).equals("door") ? "邀请您视频通话…"
				: "邀请您语音通话…");
		account = getIntent().getStringExtra("account");
		setupTime();
		if (!channleID.substring(0, 4).equals("door")) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					displayNameByPhone = ContactUtils.getDisplayNameByPhone(
							CallledActivity.this, account);
					tvName.setText(displayNameByPhone);
				}
			}).start();

		} else {
			tvName.setText("门口机");
		}
		EventBus.getDefault().register(this);
		mediaPlayer.reset();// 把各项参数恢复到初始状态
		// Uri uri =
		// RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
		Uri uri = Uri.parse("android.resource://" + getPackageName() + "/"
				+ R.raw.kalong);

		// Uri soundUri = Uri.fromFile(new
		// File("/system/media/audio/ringtones/Basic_tone.ogg"));
		try {
			mediaPlayer.setDataSource(this, uri);
			mediaPlayer.prepare();
		} catch (IOException e) {
			e.printStackTrace();
		}
		mediaPlayer.setLooping(true);
		mediaPlayer.start();
		vibrator.vibrate(vibrates, 0);
	}

	@OnClick({ R.id.ll_refuse, R.id.ll_accept, R.id.ll_text_accept,
			R.id.ll_text_refuse })
	public void onClick(View view) {

		switch (view.getId()) {
		case R.id.ll_refuse:
		case R.id.ll_text_refuse:
			m_agoraAPI.channelInviteRefuse(channleID, account, 0);
			finish();
			break;
		case R.id.ll_accept:
		case R.id.ll_text_accept:
			if (channleID.substring(0, 5).equals("video")
					|| channleID.substring(0, 4).equals("door")) {
				//m_agoraAPI.channelInviteAccept(arg0, arg1, arg2);                                           
				Intent intent = new Intent(this, CallOutActivity.class);
				intent.putExtra("acount", displayNameByPhone);
				intent.putExtra("channleID", channleID);
				intent.putExtra(CallOutActivity.EXTRA_CALLING_TYPE,
						CallOutActivity.CALLING_TYPE_VIDEO);
				startActivity(intent);
			} else if (channleID.substring(0, 5).equals("voice")) {

				Intent intentVoice = new Intent(this,
						VoiceCallOutActivity.class);
				intentVoice.putExtra("acount", displayNameByPhone);
				intentVoice.putExtra("channleID", channleID);
				intentVoice.putExtra(VoiceCallOutActivity.EXTRA_CALLING_TYPE,
						VoiceCallOutActivity.CALLING_TYPE_VOICE);
				startActivity(intentVoice);

			} else if (channleID.substring(0, 4).equals("door_bak")) {
				// m_agoraAPI.channelInviteAccept(channleID, account, 0);
				m_agoraAPI.channelJoin(channleID.replaceAll("door", ""));
				Intent intent = new Intent(this, DoorCalledActivity.class);
				intent.putExtra("acount", displayNameByPhone);
				intent.putExtra("channleID", channleID);
				startActivity(intent);
			}
			finish();

			break;
		}
	}

	public void onEventMainThread(MsgEvent event) {
		if (isFinish) {
			mPerson = new Person();
			mPerson.setDate(startTimeLong);
			mPerson.setDuration(time);
			mPerson.setName(displayNameByPhone);
			mPerson.setNumber(account);
			mPerson.setType(4);
			if (channleID.substring(0, 5).equals("video")) {
				mPerson.setMold(1);
			} else if (channleID.substring(0, 5).equals("voice")) {
				mPerson.setMold(2);
			} else {
				mPerson.setMold(4);
			}
			dbManager.add(mPerson);
			isFinish = false;
			finish();
		}
	}

	void setupTime() {
		startTimeLong = System.currentTimeMillis();
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						time++;
					}
				});
			}
		};

		timer = new Timer();
		timer.schedule(task, 1000, 1000);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mediaPlayer != null) {
			mediaPlayer.stop();
			mediaPlayer.release();
		}
		if (timer != null) {
			timer.cancel();
		}
		vibrator.cancel();
		EventBus.getDefault().unregister(this);// 反注册EventBus
	}

}
