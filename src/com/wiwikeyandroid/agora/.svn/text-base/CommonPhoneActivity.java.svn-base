package com.wiwikeyandroid.agora;

import io.agora.AgoraAPI;

import java.util.Timer;
import java.util.TimerTask;

import org.seny.android.utils.ALog;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.wiwikeyandroid.App;
import com.wiwikeyandroid.R;
import com.wiwikeyandroid.db.DBManager;
import com.wiwikeyandroid.modules.Contacts.model.Person;
import com.wiwikeyandroid.utils.DesUtil;
import com.wiwikeyandroid.utils.PrefUtils;

import de.greenrobot.event.EventBus;

public class CommonPhoneActivity extends Activity {

	@InjectView(R.id.iv_icon)
	ImageView ivIcon; // 头像
	@InjectView(R.id.tv_name)
	TextView tvName; // 昵称
	@InjectView(R.id.tv_describe)
	TextView tvDescribe; // 描述
	@InjectView(R.id.ll_end)
	LinearLayout llEnd; // 取消结束
	@InjectView(R.id.tv_end)
	TextView tv_end; //
	public AgoraAPI m_agoraAPI;
	private Person mUser;
	private int time = 0;
	private Timer timer;
	
	private Long startTimeLong; //通话开始时间
	private DBManager dbManager;//数据库管理工具
	private Person mPerson; //通话记录
	private int mType = 3 ; //通话类型， 1呼出(对方接通了)， 2已接(落地电话不存在)，3未接通，4未接(落地电话不存在)  ,5 拒绝？        默认为未接通
	private boolean isConnect = false; //是否已接通
	private String vid; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.common_phone_view);
		getWindow().addFlags(
				WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
						| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
		ButterKnife.inject(this);
		EventBus.getDefault().register(this);
		dbManager = new DBManager(this);
		mPerson = new Person();
		mUser = (Person) getIntent().getSerializableExtra("user");
		tvName.setText(mUser.getName());
		startTimeLong = System.currentTimeMillis();
		try {
			vid = DesUtil.decrypt(
						PrefUtils.getString(this, "agoraVid", ""), DesUtil.DESKEY);
		} catch (Exception e) {
			e.printStackTrace();
		}
		m_agoraAPI = AgoraAPI.getInstanceWithKey(this, vid);
		initViews();
		String string = PrefUtils.getString(this, "mobile", "");
		String contact_phone = mUser.getNumber();
		ALog.d(string + ":::" + contact_phone);
		m_agoraAPI.channelInvitePhone(PrefUtils.getString(this, "mobile", ""),
				mUser.getNumber(), 0);
	}

	@OnClick(R.id.ll_end)
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.ll_end:
			m_agoraAPI.channelInviteEnd(
					PrefUtils.getString(this, "mobile", ""),
					mUser.getNumber(), 0);
			m_agoraAPI.channelLeave(PrefUtils.getString(this, "mobile", ""));
			
			mPerson.setDate(startTimeLong);
	    	mPerson.setDuration(time);
	    	mPerson.setName(mUser.getName());
	    	mPerson.setNumber(mUser.getNumber());
	    	mPerson.setType(mType);
	    	mPerson.setMold(3);
	    	dbManager.add(mPerson);
	    	
			finish();
			break;

		default:
			break;
		}
	}

	public void onEventMainThread(MsgEvent event) {
		switch (event.getMsg()) {
		case "onInviteReceivedByPeer":
			ALog.d("对方收到邀请了，本人也加入房间"); //未接通也会走这，对方不一定会振铃?
			m_agoraAPI.channelJoin(PrefUtils.getString(this, "mobile", ""));
			setupTime("等待接听 ");

			break;
		case "onInviteEndByPeer": //
			ALog.d("对方拒绝或结束通话");
			 if (!isConnect) {
				 mType = 5;
			}
				mPerson.setDate(startTimeLong);
		    	mPerson.setDuration(time);
		    	mPerson.setName(mUser.getName());
		    	mPerson.setNumber(mUser.getNumber());
		    	mPerson.setType(mType);
		    	mPerson.setMold(3);
		    	dbManager.add(mPerson);
			
			finish();
			break;
		case "onInviteAcceptedByPeer":
			ALog.d("对方接受邀请");
			isConnect = true;
			mType=1;
			timer.cancel();
			time = 0;
			setupTime("正在通话 ");
			break;

		default:
			break;
		}

	}

	/**
	 * 通话计时
	 * 
	 * @param string
	 */
	void setupTime(final String msg) {
		
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {

						time++;

						if (time >= 3600) {
							tvDescribe.setText(msg
									+ String.format("%d:%02d:%02d",
											time / 3600, (time % 3600) / 60,
											(time % 60)));
						} else {
							tvDescribe.setText(msg
									+ String.format("%02d:%02d",
											(time % 3600) / 60, (time % 60)));
						}
					}
				});
			}
		};

		timer = new Timer();
		timer.schedule(task, 1000, 1000);
	}

	void initViews() {

		// 静音
		CheckBox muter = (CheckBox) findViewById(R.id.cb_action_muter);
		muter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton,
					boolean mutes) {
				m_agoraAPI.getMedia().muteLocalAudioStream(mutes);
				// rtcEngine.muteLocalAudioStream(mutes);
				compoundButton
						.setBackgroundResource(mutes ? R.drawable.ic_room_mute_pressed
								: R.drawable.ic_room_mute);

			}
		});

		// 免提
		CheckBox speaker = (CheckBox) findViewById(R.id.cb_action_speaker);
		speaker.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton,
					boolean usesSpeaker) {
				m_agoraAPI.getMedia().setEnableSpeakerphone(usesSpeaker);
				// rtcEngine.setEnableSpeakerphone(usesSpeaker);
				compoundButton
						.setBackgroundResource(usesSpeaker ? R.drawable.ic_room_loudspeaker_pressed
								: R.drawable.ic_room_loudspeaker);
				
			}
		});
		muter.setChecked(false);
		speaker.setChecked(false);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (timer != null) {
			timer.cancel();
		}
		EventBus.getDefault().unregister(this);// 反注册EventBus
	}
	@Override
	public void onBackPressed() {
		m_agoraAPI.channelInviteEnd(
				PrefUtils.getString(this, "mobile", ""),
				mUser.getNumber(), 0);
		m_agoraAPI.channelLeave(PrefUtils.getString(this, "mobile", ""));
		
		mPerson.setDate(startTimeLong);
    	mPerson.setDuration(time);
    	mPerson.setName(mUser.getName());
    	mPerson.setNumber(mUser.getNumber());
    	mPerson.setType(mType);
    	mPerson.setMold(3);
    	dbManager.add(mPerson);
		finish();
		
	}
	
}
