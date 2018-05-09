package com.wiwikeyandroid.agora;

import io.agora.AgoraAPI;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.VideoCanvas;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.seny.android.utils.ALog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.Toast;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.wiwikeyandroid.App;
import com.wiwikeyandroid.R;
import com.wiwikeyandroid.activity.LoginActivity;
import com.wiwikeyandroid.bean.GetOpenInfoBean;
import com.wiwikeyandroid.bean.RBResponse;
import com.wiwikeyandroid.db.DBManager;
import com.wiwikeyandroid.modules.Contacts.model.Person;
import com.wiwikeyandroid.net.HttpLoader;
import com.wiwikeyandroid.net.HttpLoader.ResponseListener;
import com.wiwikeyandroid.utils.DesUtil;
import com.wiwikeyandroid.utils.GlobalWiwikey;
import com.wiwikeyandroid.utils.NetworkConnectivityUtils;
import com.wiwikeyandroid.utils.PrefUtils;

import de.greenrobot.event.EventBus;

public class CallOutActivity extends BaseEngineEventHandlerActivity implements
		ResponseListener {

	private final int MSG_HIDE_CONTROL = 0;// 延时隐藏控制面板

	private static final int MSG_NOT_ONLINE = 1;// 对方不在线,未收到邀请

	private boolean isShowControlLayout = false;// 是否是显示控制面板
	private boolean isAnimate = true;// 动画是否在执行过程中，默认为true，
	private boolean isLeave = true; // 是否退出房间，确保只执行一次

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MSG_HIDE_CONTROL:
				hideControlLayout();
				break;
			case MSG_NOT_ONLINE:
				// 调用推送接口，通知对方
				// appNotification.setText("无人接听…");
				new Thread(new Runnable() {
					public void run() {
						Map<String, String> params = new HashMap<String, String>();
						params.put("fromMobile", channleID);
						params.put("toMobile", toPhone);
						params.put(
								"memberId",
								PrefUtils.getInt(CallOutActivity.this,
										"memberId", -1) + "");
						params.put("token", PrefUtils.getString(
								CallOutActivity.this, "token", ""));
						HttpLoader.post(GlobalWiwikey.URL_CALLAPPPUSH, params,
								RBResponse.class,
								GlobalWiwikey.REQUEST_GETMEMBERHOUSELIST,
								CallOutActivity.this);
					}
				}).start();
				break;
			}
		};
	};

	public final static int CALLING_TYPE_VIDEO = 0x100;
	public final static int CALLING_TYPE_VOICE = 0x101;

	public final static String EXTRA_CALLING_TYPE = "EXTRA_CALLING_TYPE";
	public final static String EXTRA_VENDOR_KEY = "EXTRA_VENDOR_KEY";
	// public final static String EXTRA_CHANNEL_ID = "EXTRA_CHANNEL_ID";

	private LinearLayout ll_top_control, ll_bottom_control;
	private int mCallingType;
	private SurfaceView mLocalView;
	private String vendorKey = "";
	private TextView mDuration;
	private TextView mByteCounts;

	private View mCameraEnabler;
	private View mCameraSwitcher;
	private LinearLayout mRemoteUserContainer;
	private LinearLayout llOpenDoorLl;
	                        
	private FrameLayout mMainVideoContainer;
	private AlertDialog alertDialog;
	private int time = 0;
	private Timer timer;
	private boolean isMuted = false;
	private boolean isCorrect = true;

	private int mLastRxBytes = 0;
	private int mLastTxBytes = 0;
	private int mLastDuration = 0;

	private int mRemoteUserViewWidth = 0;
	private int mRemoteUserViewHight = 0;

	private DBManager dbManager;// 数据库管理工具
	private Person mPerson; // 通话记录
	private Long startTimeLong; // 通话开始时间
	private int mType = 3; // 通话类型， 1呼出(对方接通了)， 2已接，3未接通，4未接 ,5拒绝 默认为未接通
	RtcEngine rtcEngine;
	Dialog dia;
	private Display display;
	public AgoraAPI m_agoraAPI;

	private String toAccount; // 对方昵称
	private String toPhone; // 对方手机号码，也就是对方登录账号
	private TextView toAccountName; // 对方昵称
	protected static final String TAG = "CallOutActivity";

	private String channleID; // 频道号
	private int userId = new Random().nextInt(Math.abs((int) System
			.currentTimeMillis()));
	private TextView appNotification; // 通话状态时时报告

	@Override
	public void onCreate(Bundle savedInstance) {
		super.onCreate(savedInstance);
		EventBus.getDefault().register(this);
		setContentView(R.layout.activity_room_channel);
		getWindow().addFlags(
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
						| WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
						| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

		mRemoteUserViewWidth = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 80, getResources()
						.getDisplayMetrics());
		// 16/9
		mRemoteUserViewHight = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 63, getResources()
						.getDisplayMetrics());
		appNotification = (TextView) findViewById(R.id.app_notification);
		dbManager = new DBManager(this);

		mCallingType = getIntent().getIntExtra(EXTRA_CALLING_TYPE,
				CALLING_TYPE_VOICE);
		ll_top_control = (LinearLayout) findViewById(R.id.ll_top_control);
		ll_bottom_control = (LinearLayout) findViewById(R.id.ll_bottom_control);
		toAccount = getIntent().getStringExtra("acount");
		toAccountName = (TextView) findViewById(R.id.tv_toAccount);
		toPhone = getIntent().getStringExtra("phone");
		channleID = getIntent().getStringExtra("channleID");
		toAccountName.setText(toAccount);
		llOpenDoorLl = (LinearLayout) findViewById(R.id.ll_action_hung_up);
		if(channleID.indexOf("door")>=0) {
			llOpenDoorLl.setVisibility(View.VISIBLE);
			ll_bottom_control.setVisibility(View.GONE);
			toAccountName.setText("唯家门口机");
		}
		setupRtcEngine();
		
		if(channleID.indexOf("door")>=0) {
			rtcEngine.disableVideo();
		
		}
		m_agoraAPI = AgoraAPI.getInstanceWithMedia(this, rtcEngine);
		if (toPhone != null) {
			// 邀请好友
			m_agoraAPI.channelInviteUser(channleID, toPhone, 0);
			// m_agoraAPI.channelJoin("110");
			appNotification.setText("正在呼叫…");
			handler.sendEmptyMessageDelayed(MSG_NOT_ONLINE, 5000);
		}

		initViews();

		setupTime();

		// check network
		if (!NetworkConnectivityUtils.isConnectedToNetwork(this)) {
			onError(104);
		}
	}

	/*
	 * 显示面板
	 */
	private void showControlLayout() {
		isAnimate = true;

		ViewPropertyAnimator.animate(ll_top_control).translationY(0)
				.setDuration(200);
		ViewPropertyAnimator.animate(ll_bottom_control).translationY(0)
				.setDuration(200).setListener(new AnimatorListener() {

					@Override
					public void onAnimationStart(Animator arg0) {

					}

					@Override
					public void onAnimationRepeat(Animator arg0) {

					}

					@Override
					public void onAnimationEnd(Animator arg0) {
						isShowControlLayout = false;
						isAnimate = false;
					}

					@Override
					public void onAnimationCancel(Animator arg0) {

					}
				});
		handler.sendEmptyMessageDelayed(MSG_HIDE_CONTROL, 6000);
	}

	/*
	 * 隐藏面板
	 */
	private void hideControlLayout() {
		isAnimate = true;
		ViewPropertyAnimator.animate(ll_bottom_control)
				.translationY(ll_bottom_control.getHeight()).setDuration(200);
		ViewPropertyAnimator.animate(ll_top_control)
				.translationY(-ll_top_control.getHeight()).setDuration(200)
				.setListener(new AnimatorListener() {

					@Override
					public void onAnimationStart(Animator arg0) {

					}

					@Override
					public void onAnimationRepeat(Animator arg0) {

					}

					@Override
					public void onAnimationEnd(Animator arg0) {
						isAnimate = false;
						isShowControlLayout = true;
					}

					@Override
					public void onAnimationCancel(Animator arg0) {

					}
				});

	}

	/*
	 * 触摸事件
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			ALog.d("onTouchEvent:ACTION_DOWN 触摸屏幕了");

			if (isAnimate) {
				return super.onTouchEvent(event);
			}
			if (isShowControlLayout) {
				// 更新UI
				// 显示操作
				showControlLayout();

			} else {
				handler.removeMessages(MSG_HIDE_CONTROL);
				// 隐藏操作
				hideControlLayout();

				/*
				 * new Thread() { public void run() { // 这儿是耗时操作，完成之后更新UI；
				 * runOnUiThread(new Runnable() {
				 * 
				 * @Override public void run() {
				 * 
				 * }
				 * 
				 * }); } }.start();
				 */

			}
			break;

		default:
			break;
		}

		return super.onTouchEvent(event);
	}
	
	
	void setupChannel() {
		// String channelId = getIntent().getStringExtra(EXTRA_CHANNEL_ID);
		// this.channelId = channelId;
		rtcEngine.setVideoProfile(21); // 360P_2 360x640
		String  vid = null ; 
		try {
			vid = DesUtil.decrypt(
						PrefUtils.getString(this, "agoraVid", ""), DesUtil.DESKEY);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("-----------------------userId:"+userId);
		this.rtcEngine.joinChannel(vid, channleID, "", userId);
	}

	void setupRtcEngine() {
		rtcEngine = App.application.getRtcEngine();

		((App) getApplication()).setEngineEventHandlerActivity(this);

		rtcEngine.enableVideo();

	}

	/**
	 * 初始化view
	 */
	void initViews() {

		// 静音
		CheckBox muter = (CheckBox) findViewById(R.id.action_muter);
		muter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton,
					boolean mutes) {

				rtcEngine.muteLocalAudioStream(mutes);
				compoundButton
						.setBackgroundResource(mutes ? R.drawable.ic_room_mute_pressed
								: R.drawable.ic_room_mute);

			}
		});

		// 免提
		CheckBox speaker = (CheckBox) findViewById(R.id.action_speaker);
		speaker.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton,
					boolean usesSpeaker) {

				rtcEngine.setEnableSpeakerphone(usesSpeaker);
				compoundButton
						.setBackgroundResource(usesSpeaker ? R.drawable.ic_room_loudspeaker
								: R.drawable.ic_room_loudspeaker_pressed);

			}
		});

		// 关闭摄像头
		CheckBox cameraEnabler = (CheckBox) findViewById(R.id.action_camera_enabler);
		cameraEnabler
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton compoundButton,
							boolean disablesCamera) {

						rtcEngine.muteLocalVideoStream(disablesCamera);

						if (disablesCamera) {
							findViewById(R.id.user_local_voice_bg)
									.setVisibility(View.VISIBLE);
							rtcEngine.muteLocalVideoStream(true);

						} else {
							findViewById(R.id.user_local_voice_bg)
									.setVisibility(View.GONE);
							rtcEngine.muteLocalVideoStream(false);
						}

						compoundButton
								.setBackgroundResource(disablesCamera ? R.drawable.ic_room_button_close_pressed
										: R.drawable.ic_room_button_close);

					}
				});

		// 切换摄像头
		CheckBox cameraSwitch = (CheckBox) findViewById(R.id.action_camera_switcher);
		cameraSwitch
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton compoundButton,
							boolean switches) {

						rtcEngine.switchCamera();

						compoundButton
								.setBackgroundResource(switches ? R.drawable.ic_room_button_change_pressed
										: R.drawable.ic_room_button_change);

					}
				});

		// setup states of action buttons
		muter.setChecked(false);
		speaker.setChecked(true);
		cameraEnabler.setChecked(false);
		cameraSwitch.setChecked(false);

		findViewById(R.id.action_hung_up).setOnClickListener(getViewClickListener());
		findViewById(R.id.ll_open_door).setOnClickListener(getViewClickListener());
		findViewById(R.id.ll_door_action_hung_up).setOnClickListener(getViewClickListener());
		// findViewById(R.id.action_back).setOnClickListener(getViewClickListener());

		mDuration = (TextView) findViewById(R.id.stat_time);
		mByteCounts = (TextView) findViewById(R.id.stat_bytes);

		mCameraEnabler = findViewById(R.id.wrapper_action_camera_enabler);
		mCameraSwitcher = findViewById(R.id.wrapper_action_camera_switcher);

		mRemoteUserContainer = (LinearLayout) findViewById(R.id.user_remote_views);
		mMainVideoContainer = (FrameLayout) findViewById(R.id.user_main_view_container);
		
		View simulateClick = new View(this);
		this.onUserInteraction(simulateClick);

		mCallingType = CALLING_TYPE_VIDEO;

		mCameraEnabler.setVisibility(View.VISIBLE);
		mCameraSwitcher.setVisibility(View.VISIBLE);

		// findViewById(R.id.wrapper_action_video_calling).setBackgroundResource(R.drawable.ic_room_button_yellow_bg);
		findViewById(R.id.user_local_voice_bg).setVisibility(View.GONE);

		// enable video call
		ensureLocalViewIsCreated();

		rtcEngine.enableVideo();
		rtcEngine.muteLocalVideoStream(false);
		rtcEngine.muteLocalAudioStream(false);
		rtcEngine.muteAllRemoteVideoStreams(false);

		// join video call
		if (mRemoteUserContainer.getChildCount() == 0) {
			this.setupChannel();
			ALog.d("setupChannel");
		}

		new android.os.Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				updateAllViews();
			}
		}, 500);

		// setRemoteUserViewVisibility(false);
	}

	void setRemoteUserViewVisibility(boolean isVisible) {

		findViewById(R.id.user_remote_views).getLayoutParams().height = isVisible ? (int) TypedValue
				.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, getResources()
						.getDisplayMetrics()) : 0;

	}

	/**
	 * 本地视频
	 */

	private void ensureLocalViewIsCreated() {
		ViewGroup vg = findViewFor(0);
		if (vg != null)
			return;

		vg = setupViewFor(0);

		if (vg == null)
			return;
		SurfaceView canvasView = getVideoSurface(vg, false);
		if (canvasView == null)
			return;
		rtcEngine.enableVideo(); // If video has not been started, then start it
		int rc;
		rc = rtcEngine.setupLocalVideo(new VideoCanvas(canvasView));
		if (rc < 0) {
			Log.e("AGORA_SDK",
					"Failed to call rtcEngine.setupLocalVideo for local preview");
		}

	}

	private ViewGroup findViewFor(int uid) {
		ViewGroup gr;
		gr = (ViewGroup) mMainVideoContainer.getChildAt(0);
		if (gr != null && (int) gr.getTag() == uid)
			return gr;

		int child_count = mRemoteUserContainer.getChildCount();
		int i;
		for (i = 0; i < child_count; i++) {
			gr = (ViewGroup) mRemoteUserContainer.getChildAt(i);

			if (gr != null && (int) gr.getTag() == uid)
				return gr;
		}

		return null;
	}

	void setupTime() {
		startTimeLong = System.currentTimeMillis();
		ALog.d("通话开始时间" + startTimeLong);
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {

						time++;
						// 电话通了，对方未接听，时间超过N秒
						if (isCorrect && time >= 40) {
							mType = 3;
							appNotification.setText("无人接听…");
							m_agoraAPI.channelInviteEnd(channleID, toPhone, 0);
							rtcEngine.leaveChannel();
							m_agoraAPI.channelLeave(channleID);
							timer.cancel();
						}

						if (time >= 3600) {
							mDuration.setText(String.format("%d:%02d:%02d",
									time / 3600, (time % 3600) / 60,
									(time % 60)));
						} else {
							mDuration.setText(String.format("%02d:%02d",
									(time % 3600) / 60, (time % 60)));
						}
					}
				});
			}
		};

		timer = new Timer();
		timer.schedule(task, 1000, 1000);
	}

	// 本地
	private ViewGroup createLocalView() {
		LayoutInflater layoutInflater = getLayoutInflater();
		View top = layoutInflater.inflate(R.layout.viewlet_remote_user, null);

		return (ViewGroup) top;
	}

	// 远程
	private ViewGroup createRemoteView() {
		LayoutInflater layoutInflater = getLayoutInflater();
		View top = layoutInflater.inflate(R.layout.viewlet_remote_user, null);
		return (ViewGroup) top;
	}

	private void updateViewStatus(ViewGroup view, int uid) {

		if (view == null)
			return;

		if (uid == 0) {
//			// local
//			View mic = view.findViewById(R.id.viewlet_remote_video_voice);
//			if (mic != null) {
//				mic.setVisibility(View.GONE);
//			}

		} else {
//			View mic = view.findViewById(R.id.viewlet_remote_video_voice);
//			if (mic != null) {
//				mic.setBackgroundResource(isMuted ? R.drawable.ic_room_voice_red
//						: R.drawable.ic_room_voice_grey);
//				mic.setVisibility(View.VISIBLE);
//			}
			SurfaceView sv = getVideoSurface(view, false);
			if (mCallingType == CALLING_TYPE_VIDEO) {
				sv.setVisibility(View.VISIBLE);
			} else {
				sv.setVisibility(View.GONE);
			}
		}
	}

	private ViewGroup setupViewFor(int uid) {
		boolean videoViewOnTop = false;
		ViewGroup gr;
		FrameLayout anchor;

		if (uid == 0) {
			gr = createLocalView();
		} else {
			gr = createRemoteView();
		}

		anchor = getAnchorPoint(gr, uid == 0);
		if (anchor == null)
			return null;

		gr.setTag(uid);

		if (mMainVideoContainer.getChildCount() == 0) {
			mMainVideoContainer.addView(gr, new FrameLayout.LayoutParams(
					FrameLayout.LayoutParams.MATCH_PARENT,
					FrameLayout.LayoutParams.MATCH_PARENT));

			videoViewOnTop = false;
		} else {
			mRemoteUserContainer.addView(gr, new FrameLayout.LayoutParams(
					mRemoteUserViewWidth, mRemoteUserViewWidth));

			videoViewOnTop = true;

			// 显示
			mRemoteUserContainer.setVisibility(View.VISIBLE);
		}

		// 创建视图
		final SurfaceView canvasView = RtcEngine
				.CreateRendererView(getApplicationContext());

		canvasView.setZOrderOnTop(videoViewOnTop);
		canvasView.setZOrderMediaOverlay(videoViewOnTop);

		// 添加视图
		anchor.addView(canvasView);

		// 更新状态
		updateViewStatus(gr, uid);

		return gr;
	}

	private void moveThumbnailVideoToMain() {
		if (mRemoteUserContainer.getChildCount() == 0)
			return;

		ViewGroup gr = (ViewGroup) mRemoteUserContainer.getChildAt(0);
		mRemoteUserContainer.removeView(gr);
		mMainVideoContainer.addView(gr, new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.MATCH_PARENT,
				FrameLayout.LayoutParams.MATCH_PARENT));

		if (mRemoteUserContainer.getChildCount() == 0)
			mRemoteUserContainer.setVisibility(View.GONE);

		int uid = (int) gr.getTag();
		SurfaceView c = getVideoSurface(gr, uid == 0);
		if (c != null) {
			c.setZOrderOnTop(false);
			c.setZOrderMediaOverlay(false);
		}
	}

	private void removeViewFor(int uid) {
		ViewGroup gr = findViewFor(uid);
		if (gr == null)
			return;

		if (uid == 0)
			rtcEngine.setupLocalVideo(new VideoCanvas(null));
		else
			rtcEngine.setupRemoteVideo(new VideoCanvas(null,
					VideoCanvas.RENDER_MODE_HIDDEN, uid));

		ViewGroup parent = (ViewGroup) gr.getParent();
		if (parent == null)
			return;

		parent.removeView(gr);

		if (mRemoteUserContainer.getChildCount() == 0) {
			mRemoteUserContainer.setVisibility(View.GONE);
		}

		if (mMainVideoContainer.getChildCount() == 0)
			moveThumbnailVideoToMain();
	}

	public void onSwitchRemoteUsers(int uid) {
		if (CALLING_TYPE_VOICE == mCallingType) {
			return;
		}
		// In Video Call

		// view: on which the tap event raised
		ViewGroup v1 = findViewFor(uid);
		if (v1 == null)
			return;

		// switch with main view
		ViewGroup v2 = (ViewGroup) mMainVideoContainer.getChildAt(0);
		if (v2 == null)
			return;

		// if click on main view: no switch
		if (v2.getTag() == v1.getTag())
			return;

		// switch
		ViewGroup v1_parent = (ViewGroup) v1.getParent();
		if (v1_parent == null)
			return;

		int pos = v1_parent.indexOfChild(v1); // keep position
		v1_parent.removeView(v1);
		mMainVideoContainer.removeView(v2);
		v1_parent.addView(v2, pos, new FrameLayout.LayoutParams(
				mRemoteUserViewWidth, mRemoteUserViewWidth));
		mMainVideoContainer.addView(v1, new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.MATCH_PARENT,
				FrameLayout.LayoutParams.MATCH_PARENT));

		// bring the new main view to background and
		// thumbnail view top
		SurfaceView c;
		c = getVideoSurface(v1, false);
		if (c != null) {
			c.setZOrderOnTop(false);
			c.setZOrderMediaOverlay(false);
		}

		c = getVideoSurface(v2, true);
		if (c != null) {
			c.setZOrderOnTop(true);
			c.setZOrderMediaOverlay(true);
		}
	}

	// switch click
	public void onSwitchRemoteUsers(View view) {

		// In voice call
		if (CALLING_TYPE_VOICE == mCallingType) {
			return;
		}
		// In Video Call

		// view: on which the tap event raised
		ViewGroup v1 = (ViewGroup) view.getParent();
		if (v1 == null)
			return;

		// switch with main view
		ViewGroup v2 = (ViewGroup) mMainVideoContainer.getChildAt(0);
		if (v2 == null)
			return;

		// if click on main view: no switch
		if (v2.getTag() == v1.getTag())
			return;

		// switch
		ViewGroup v1_parent = (ViewGroup) v1.getParent();
		if (v1_parent == null)
			return;

		int pos = v1_parent.indexOfChild(v1); // keep position
		v1_parent.removeView(v1);
		mMainVideoContainer.removeView(v2);
		v1_parent.addView(v2, pos, new FrameLayout.LayoutParams(
				mRemoteUserViewWidth, mRemoteUserViewWidth));
		mMainVideoContainer.addView(v1, new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.MATCH_PARENT,
				FrameLayout.LayoutParams.MATCH_PARENT));

		// bring the new main view to background and
		// thumbnail view top
		SurfaceView c;
		c = getVideoSurface(v1, false);
		if (c != null) {
			c.setZOrderOnTop(false);
			c.setZOrderMediaOverlay(false);
		}

		c = getVideoSurface(v2, true);
		if (c != null) {
			c.setZOrderOnTop(true);
			c.setZOrderMediaOverlay(true);
		}
	}

	private void updateAllViews() {
		// check main view
		RelativeLayout vg;
		vg = (RelativeLayout) mMainVideoContainer.getChildAt(0);
		if (vg != null && (int) vg.getTag() != 0) {
			updateViewStatus(vg, (int) vg.getTag());
		}

		// check thumbnails view
		int child_count = mRemoteUserContainer.getChildCount();
		int i;
		for (i = 0; i < child_count; i++) {
			vg = (RelativeLayout) mRemoteUserContainer.getChildAt(i);
			if (vg != null && (int) vg.getTag() != 0) {
				updateViewStatus(vg, (int) vg.getTag());
			}
		}
	}

	// mute others
	public void onMuteRemoteUsers(View view) {
		isMuted = !isMuted;

		rtcEngine.muteAllRemoteAudioStreams(isMuted);

		// check main view
		RelativeLayout vg;
		vg = (RelativeLayout) mMainVideoContainer.getChildAt(0);
		if (vg != null && (int) vg.getTag() != 0) {
			updateViewStatus(vg, (int) vg.getTag());
		}

		// check thumbnails view
		int child_count = mRemoteUserContainer.getChildCount();
		int i;
		for (i = 0; i < child_count; i++) {
			vg = (RelativeLayout) mRemoteUserContainer.getChildAt(i);
			if (vg != null && (int) vg.getTag() != 0) {
				updateViewStatus(vg, (int) vg.getTag());
			}
		}
	}

	private SurfaceView getVideoSurface(ViewGroup view, boolean isLocal) {
		FrameLayout fl = getAnchorPoint(view, isLocal);
		if (fl == null)
			return null;
		if (isLocal)
			return (SurfaceView) ((ViewGroup) fl).getChildAt(0);
		else
			return (SurfaceView) ((ViewGroup) fl).getChildAt(0);
	}

	// return the innermost view where video view will be attached at
	private FrameLayout getAnchorPoint(ViewGroup view, boolean isLocal) {
		if (isLocal) {
			// depends on your view layout
			FrameLayout anchor = (FrameLayout) view
					.findViewById(R.id.viewlet_anchor);
			return anchor;
		} else {
			// depends on your view layout
			FrameLayout anchor = (FrameLayout) view
					.findViewById(R.id.viewlet_anchor);
			return anchor;
		}
	}

	/**
	 * 切换视频音频，更新 view.
	 * 
	 */
	/*
	 * void updateRemoteUserViews(int callingType) {
	 * 
	 * int visibility = View.GONE;
	 * 
	 * if (CALLING_TYPE_VIDEO == callingType) { visibility = View.GONE;
	 * 
	 * } else if (CALLING_TYPE_VOICE == callingType) { visibility =
	 * View.VISIBLE; }
	 * 
	 * for (int i = 0, size = mRemoteUserContainer.getChildCount(); i < size;
	 * i++) {
	 * 
	 * View singleRemoteView = mRemoteUserContainer.getChildAt(i);
	 * singleRemoteView.findViewById(R.id.remote_user_voice_container)
	 * .setVisibility(visibility);
	 * 
	 * if (CALLING_TYPE_VIDEO == callingType) { // re-setup remote video
	 * 
	 * FrameLayout remoteVideoUser = (FrameLayout) singleRemoteView
	 * .findViewById(R.id.viewlet_remote_video_user);
	 * 
	 * // ensure remote video view setup if (remoteVideoUser.getChildCount() >
	 * 0) { final SurfaceView remoteView = (SurfaceView) remoteVideoUser
	 * .getChildAt(0); remoteView.setBackgroundColor(Color.TRANSPARENT);
	 * 
	 * if (remoteView != null) { remoteView.setZOrderOnTop(true);
	 * remoteView.setZOrderMediaOverlay(true); int savedUid = (Integer)
	 * remoteVideoUser.getTag(); log("saved uid: " + savedUid);
	 * rtcEngine.setupRemoteVideo(new VideoCanvas(remoteView,
	 * VideoCanvas.RENDER_MODE_HIDDEN, savedUid));
	 * 
	 * } }
	 * 
	 * } } }
	 */

	@Override
	public void onUserInteraction(View view) {
		switch (view.getId()) {
	
		case R.id.action_hung_up: 
		case R.id.ll_door_action_hung_up:
		{
			m_agoraAPI.channelInviteEnd(channleID, toPhone, 0);
			// finish();
			onBackPressed();
		}
		break;
		
		case R.id.ll_open_door: 
		{
			//Toast.makeText(this, "点击开门",Toast.LENGTH_SHORT).show();
			new Thread(new Runnable() {
				@Override
				public void run() {
					Map<String, String> params = new HashMap<String, String>();
					params.put("doorCode", channleID.replaceAll("door", ""));
					params.put("memberId",PrefUtils.getInt(CallOutActivity.this,"memberId", -1) + "");
					params.put("token", PrefUtils.getString(CallOutActivity.this, "token", ""));
					HttpLoader.post(GlobalWiwikey.URL_PHONEOPENDOOR, params,GetOpenInfoBean.class, GlobalWiwikey.REQUEST_PHONEOPENDOOR, CallOutActivity.this);
				}
			}).start();
		}
		break;
		default:
			super.onUserInteraction(view);
			break;

		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {

		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void onBackPressed() {

		new Thread(new Runnable() {
			@Override
			public void run() {
				rtcEngine.leaveChannel();
				m_agoraAPI.channelInviteEnd(channleID, toPhone, 0);
				m_agoraAPI.channelLeave(channleID);
			}
		}).run();

		// keep screen on - turned off
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}

	public void onUpdateSessionStats(final IRtcEngineEventHandler.RtcStats stats) {

		runOnUiThread(new Runnable() {
			@Override
			public void run() {

				// bytes
				mByteCounts
						.setText(((stats.txBytes + stats.rxBytes - mLastTxBytes - mLastRxBytes) / 1024 / (stats.totalDuration
								- mLastDuration + 1))
								+ "KB/s");

				// remember data from this call back
				mLastRxBytes = stats.rxBytes;
				mLastTxBytes = stats.txBytes;
				mLastDuration = stats.totalDuration;

			}
		});

	}

	public synchronized void onFirstRemoteVideoDecoded(final int uid,
			int width, int height, final int elapsed) {

		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// Enable view now
				ViewGroup vg = findViewFor(uid);
				if (vg == null)
					return;
				SurfaceView canvasView = getVideoSurface(vg, false);
				if (canvasView == null)
					return;
				rtcEngine.enableVideo(); // If video has not been started, then
											// start it
				int rc;
				if (uid == 0)
					rc = rtcEngine.setupLocalVideo(new VideoCanvas(canvasView));
				else
					rc = rtcEngine.setupRemoteVideo(new VideoCanvas(canvasView,
							VideoCanvas.RENDER_MODE_HIDDEN, uid));
				if (rc < 0) {
				}

			}
		});
	}

	//
	//
	// public synchronized void onFirstRemoteVideoDecoded(final int uid,
	// int width, int height, final int elapsed) {
	//
	// log("onFirstRemoteVideoDecoded: uid: " + uid + ", width: " + width
	// + ", height: " + height);
	//
	// runOnUiThread(new Runnable() {
	// @Override
	// public void run() {
	//
	// View remoteUserView = mRemoteUserContainer.findViewById(Math
	// .abs(uid));
	//
	// // ensure container is added
	// if (remoteUserView == null) {
	//
	// LayoutInflater layoutInflater = getLayoutInflater();
	//
	// View singleRemoteUser = layoutInflater.inflate(
	// R.layout.viewlet_remote_user, null);
	// FrameLayout anchor = (FrameLayout)
	// singleRemoteUser.findViewById(R.id.viewlet_remote_video_user);
	// singleRemoteUser.setId(Math.abs(uid));
	//
	// TextView username = (TextView) singleRemoteUser
	// .findViewById(R.id.remote_user_name);
	// username.setText(toAccount);
	// singleRemoteUser.setBackgroundColor(Color.TRANSPARENT);
	// mRemoteUserContainer.setBackgroundColor(Color.TRANSPARENT);
	// mRemoteUserContainer.addView(singleRemoteUser,
	//
	// new LinearLayout.LayoutParams(mRemoteUserViewWidth,
	// mRemoteUserViewWidth));
	//
	// mRemoteUserContainer.setBackgroundColor(Color.TRANSPARENT);
	// remoteUserView = singleRemoteUser;
	// }
	//
	// remoteVideoUser = (FrameLayout) remoteUserView
	// .findViewById(R.id.viewlet_remote_video_user);
	// remoteVideoUser.removeAllViews();
	// remoteVideoUser.setTag(uid);
	//
	// // ensure remote video view setup
	// final SurfaceView remoteView = RtcEngine
	// .CreateRendererView(CallOutActivity.this);
	// remoteVideoUser.setBackgroundColor(Color.TRANSPARENT);
	// remoteView.setBackgroundColor(Color.TRANSPARENT);
	// remoteVideoUser.addView(remoteView,
	// new FrameLayout.LayoutParams(
	// FrameLayout.LayoutParams.MATCH_PARENT,
	// FrameLayout.LayoutParams.MATCH_PARENT));
	// remoteView.setZOrderOnTop(true);
	// remoteView.setZOrderMediaOverlay(true);
	//
	// rtcEngine.enableVideo();
	// int successCode = rtcEngine.setupRemoteVideo(new VideoCanvas(
	// remoteView, VideoCanvas.RENDER_MODE_HIDDEN, uid));
	//
	// if (successCode < 0) {
	// new android.os.Handler().postDelayed(new Runnable() {
	// @Override
	// public void run() {
	// rtcEngine.setupRemoteVideo(new VideoCanvas(
	// remoteView, VideoCanvas.RENDER_MODE_HIDDEN,
	// uid));
	// remoteView.invalidate();
	// }
	// }, 500);
	// }
	//
	// if (remoteUserView != null
	// && CALLING_TYPE_VIDEO == mCallingType) {
	// remoteUserView.findViewById(R.id.remote_user_voice_container)
	// .setVisibility(View.GONE);
	// } else {
	// remoteUserView.findViewById(R.id.remote_user_voice_container)
	// .setVisibility(View.VISIBLE);
	// }
	//
	// // app hints before you join
	// TextView appNotification = (TextView)
	// findViewById(R.id.app_notification);
	// appNotification.setText("正在通话中…");
	// setRemoteUserViewVisibility(true);
	// }
	// });
	// }
	/**
	 * 主叫被叫对方进频道
	 */
	public synchronized void onUserJoined(final int uid, int elapsed) {
		
		ALog.d("onUserJoined");
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Log.e("AGORA_SDK", "onUserJoined: " + uid);
				isAnimate = false;
				ImageView iv = (ImageView) findViewById(R.id.iv_icon);
				iv.setVisibility(View.INVISIBLE);
				TextView tv_hung_up = (TextView) findViewById(R.id.tv_hung_up);
				tv_hung_up.setText("结束通话");

				// a view with the UserID already there?
				// if yes, continue use that view and return
				ViewGroup vg = findViewFor(uid);
				if (vg != null) {
					Log.e("AGORA_SDK", "a previous view is used for user "
							+ uid);
					return;
				}

				vg = setupViewFor(uid);
				if (vg == null) {
					Log.e("AGORA_SDK",
							"Failed to create a anchor window for user " + uid);
					return;
				}

				// app hints before you join
				appNotification.setText("正在通话中…");
				isCorrect = false;
				onSwitchRemoteUsers(uid);
				if (toPhone != null) {
					mType = 1;
				} else {
					mType = 2;
				}

			}
		});
	}

	// public synchronized void onUserJoined(final int uid, int elapsed) {
	//
	// Log.i(TAG, "onUserJoined，对方接听了");
	// View existedUser = mRemoteUserContainer.findViewById(Math.abs(uid));
	// if (existedUser != null) {
	// // user view already added
	// return;
	// }
	//
	// runOnUiThread(new Runnable() {
	// @Override
	// public void run() {
	// ImageView iv = (ImageView) findViewById(R.id.iv_icon);
	// iv.setVisibility(View.INVISIBLE);
	// TextView tv_hung_up = (TextView) findViewById(R.id.tv_hung_up);
	// tv_hung_up.setText("结束通话");
	// // Handle the case onFirstRemoteVideoDecoded() is called before
	// // onUserJoined()
	// View singleRemoteUser = mRemoteUserContainer.findViewById(Math
	// .abs(uid));
	// if (singleRemoteUser != null) {
	// return;
	// }
	//
	// LayoutInflater layoutInflater = getLayoutInflater();
	// singleRemoteUser = layoutInflater.inflate(
	// R.layout.viewlet_remote_user, null);
	// singleRemoteUser.setId(Math.abs(uid));
	//
	// TextView username = (TextView) singleRemoteUser
	// .findViewById(R.id.remote_user_name);
	// username.setText(toAccount);
	//
	// singleRemoteUser.setBackgroundColor(Color.TRANSPARENT);
	//
	// mRemoteUserContainer.addView(singleRemoteUser,
	// new LinearLayout.LayoutParams(mRemoteUserViewWidth,
	// mRemoteUserViewWidth));
	//
	// // app hints before you join
	//
	// TextView appNotification = (TextView)
	// findViewById(R.id.app_notification);
	// appNotification.setText("正在通话中...");
	// setRemoteUserViewVisibility(true);
	//
	// }
	// });
	//
	// }

	public void onUserOffline(final int uid) {
		rtcEngine.leaveChannel();
		//finish();

		log("onUserOffline: uid: " + uid);

		if (isFinishing()) {
			return;
		}

		if (mRemoteUserContainer == null) {
			return;
		}
	}

	@Override
	public void finish() {

		if (alertDialog != null) {
			alertDialog.dismiss();
		}
		rtcEngine.leaveChannel();
		super.finish();
	}

	@Override
	public void onJoinChannelSuccess(String channel, final int uid, int elapsed) {

		userId = uid;
			ALog.d("onJoinChannelSuccess：加入频道成功");
		// ((AgoraApplication)getApplication()).setIsInChannel(true);

		// callId=rtcEngine.getCallId();

		// ((AgoraApplication)getApplication()).setCallId(callId);

		// String recordValue = new
		// SimpleDateFormat("yyyy-MM-dd/HH:mm:ss").format(Calendar.getInstance().getTime());

		// String recordUrl = rtcEngine.makeQualityReportUrl(channel, uid, 0,
		// 0);

		// ((AgoraApplication) getApplication()).setRecordDate(callId,
		// recordValue + "+" + recordUrl);
	}

	@Override
	public void onLeaveChannel(final IRtcEngineEventHandler.RtcStats stats) {
		Log.i(TAG, "媒体离开频道成功，销毁当前界面");
		if (isLeave) {
			isLeave = false;
			mPerson = new Person();
			mPerson.setDate(startTimeLong);
			mPerson.setDuration(time);
			mPerson.setName(toAccount);
			mPerson.setNumber(toPhone == null ? channleID.substring(5) : toPhone); 
			mPerson.setType(mType);
			mPerson.setMold(channleID.substring(0,4).equals("door")?4:1);
			dbManager.add(mPerson);

			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if (isCorrect) {

						// if (stats.totalDuration >= 3600) {
						// ((TextView)
						// findViewById(R.id.evaluation_time)).setText(String.format("%d:%02d:%02d",
						// time / 3600, (time % 3600) / 60, (time % 60)));
						// } else {
						// ((TextView)
						// findViewById(R.id.evaluation_time)).setText(String.format("%02d:%02d",
						// (time % 3600) / 60, (time % 60)));
						// }
						//
						// if (((stats.txBytes + stats.rxBytes) / 1024 / 1024) >
						// 0) {
						// ((TextView)
						// findViewById(R.id.evaluation_bytes)).setText(Integer.toString((stats.txBytes
						// + stats.rxBytes) / 1024 / 1024) + "MB");
						// } else {
						// ((TextView)
						// findViewById(R.id.evaluation_bytes)).setText(Integer.toString((stats.txBytes
						// + stats.rxBytes) / 1024) + "KB");
						// }
						if (!isFinishing()) {
							// showDialDialog();
							finish();
						}

					} else {

						finish();
					}
				}
			});

		}

	}

	public void onUserMuteVideo(final int uid, final boolean muted) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				ViewGroup gr = findViewFor(uid);
				if (gr == null)
					return;
				SurfaceView sv = getVideoSurface(gr, uid == 0);
				sv.setVisibility(muted ? View.GONE : View.VISIBLE);
			}
		});
	}

	@Override
	public synchronized void onError(int err) {

		if (isFinishing()) {
			return;
		}

		// incorrect vendor key
		if (101 == err) {

			runOnUiThread(new Runnable() {
				@Override
				public void run() {

					if (alertDialog != null) {
						return;
					}

					alertDialog = new AlertDialog.Builder(CallOutActivity.this)
							.setCancelable(false)
							.setMessage("无效的厂商Key")
							.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											getSharedPreferences("config", Context.MODE_PRIVATE).edit()
											.clear().commit();
											PrefUtils.setBoolean(getApplication(), "isFirst", false);
											// Go to login
											Intent toLogin = new Intent(
													CallOutActivity.this,
													LoginActivity.class);
											toLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
													| Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
											startActivity(toLogin);
											rtcEngine.leaveChannel();
											
										}
									})
							.setOnCancelListener(
									new DialogInterface.OnCancelListener() {
										@Override
										public void onCancel(
												DialogInterface dialogInterface) {
											dialogInterface.dismiss();
										}
									}).create();

					alertDialog.show();
				}
			});

		}

		// no network connection
		if (104 == err) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					appNotification.setText("网络无法连接，请检查网络");
				}
			});
		}

	}

	public static String humanReadableByteCount(long bytes, boolean si) {
		int unit = si ? 1000 : 1024;
		if (bytes < unit)
			return bytes + " B";
		int exp = (int) (Math.log(bytes) / Math.log(unit));
		String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1)
				+ (si ? "" : "i");
		return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
	}

	@Override
	protected void onDestroy() {
		Log.i(TAG, "onDestroy");
		if (timer != null) {
			timer.cancel();
		}
		rtcEngine.leaveChannel();
		EventBus.getDefault().unregister(this);// 反注册
		handler.removeMessages(MSG_HIDE_CONTROL);
		super.onDestroy();
	}

	public void onEventMainThread(MsgEvent event) {
		if (event.getMsg().equals("onInviteRefusedByPeer")) { // 对方拒绝邀请
			isCorrect = false;
			mType = 5;
			appNotification.setText("对方已拒绝");
			handler.postDelayed(new Runnable() {

				@Override
				public void run() {
					rtcEngine.leaveChannel();
					m_agoraAPI.channelLeave(channleID);
				}
			}, 2000);
			// dimBackground(1.0f,0.1f);

		} else if (event.getMsg().equals("onInviteReceivedByPeer")) { // 对方收到邀请
			handler.removeMessages(MSG_NOT_ONLINE);
			appNotification.setText("等待对方接听…");

		}
	}

	/*
	 * 弹出拨打普通电话窗口
	 */
	private void showDialDialog() {
		dia = new Dialog(this, R.style.AlertDialogStyle); // 获取Dialog布局
		View view = LayoutInflater.from(this).inflate(
				R.layout.no_answer_dialog, null);
		RelativeLayout rlLayout = (RelativeLayout) view
				.findViewById(R.id.channel_evaluation);
		Button iv_close = (Button) view.findViewById(R.id.evaluation_close);
		Button dial = (Button) view.findViewById(R.id.evaluation_evaluate);
		dia.setContentView(view);
		WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		display = windowManager.getDefaultDisplay();
		// 调整dialog背景大小
		rlLayout.setLayoutParams(new FrameLayout.LayoutParams((int) (display
				.getWidth() * 0.85), LayoutParams.WRAP_CONTENT));

		dial.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dia.cancel();
				Intent intentPhone = new Intent(CallOutActivity.this,
						CommonPhoneActivity.class);
				Bundle bundle = new Bundle();
				Person mUser = new Person();
				mUser.setName(toAccount);
				mUser.setNumber(toPhone);
				bundle.putSerializable("user", mUser);
				intentPhone.putExtras(bundle);
				CallOutActivity.this.startActivity(intentPhone);
				finish();
			}
		});

		iv_close.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dia.cancel();
				finish();

			}
		});
		dia.show();
		dia.setCancelable(false);
		dia.setCanceledOnTouchOutside(false);

	}

	private void dimBackground(final float from, final float to) {
		final Window window = getWindow();
		ValueAnimator valueAnimator = ValueAnimator.ofFloat(from, to);
		valueAnimator.setDuration(1000);
		valueAnimator.addUpdateListener(new AnimatorUpdateListener() {

			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				WindowManager.LayoutParams params = window.getAttributes();
				params.alpha = (Float) animation.getAnimatedValue();
				window.setAttributes(params);
			}
		});
		valueAnimator.addListener(new AnimatorListener() {

			@Override
			public void onAnimationStart(Animator animation) {

			}

			@Override
			public void onAnimationRepeat(Animator animation) {

			}

			@Override
			public void onAnimationEnd(Animator animation) {
				// rtcEngine.leaveChannel();
				// m_agoraAPI.channelLeave(channleID);
			}

			@Override
			public void onAnimationCancel(Animator animation) {

			}
		});
		valueAnimator.start();
	}

	@Override
	public void onGetResponseSuccess(int requestCode, RBResponse response) {
		switch (requestCode) {
		case GlobalWiwikey.REQUEST_PHONEOPENDOOR:
			GetOpenInfoBean rb = (GetOpenInfoBean) response;
			if(rb.getErrno() == 0) {
				Toast.makeText(this, "开门成功，5秒后将自动关闭通话", Toast.LENGTH_LONG).show();
				handler.postDelayed(new Runnable() {
					@Override
					public void run() {
						rtcEngine.leaveChannel();
						m_agoraAPI.channelLeave(channleID);
						finish();
					}
				}, 6000);
			}
			break;
		}
	}

	@Override
	public void onGetResponseError(int requestCode, VolleyError error) {

	}
}