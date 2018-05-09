package com.wiwikeyandroid.agora;

import io.agora.AgoraAPI;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.VideoCanvas;

import java.io.IOException;
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
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
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
import com.wiwikeyandroid.bean.RBResponse;
import com.wiwikeyandroid.db.DBManager;
import com.wiwikeyandroid.modules.Contacts.model.Person;
import com.wiwikeyandroid.net.HttpLoader.ResponseListener;
import com.wiwikeyandroid.utils.DesUtil;
import com.wiwikeyandroid.utils.NetworkConnectivityUtils;
import com.wiwikeyandroid.utils.PrefUtils;

import de.greenrobot.event.EventBus;

public class DoorCalledActivity extends BaseEngineEventHandlerActivity
		implements ResponseListener {

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
			}
		};
	};

	public final static int CALLING_TYPE_VIDEO = 0x100;

	public final static String EXTRA_CALLING_TYPE = "EXTRA_CALLING_TYPE";
	public final static String EXTRA_VENDOR_KEY = "EXTRA_VENDOR_KEY";
	// public final static String EXTRA_CHANNEL_ID = "EXTRA_CHANNEL_ID";

	private LinearLayout ll_bottom_control;
	private int mCallingType;
	private SurfaceView mLocalView;
	private String vendorKey = "";
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
	private AgoraAPI m_agoraAPI;
	Dialog dia;
	private Display display;

	private String toAccount; // 对方昵称
	private String toPhone; // 对方手机号码，也就是对方登录账号
	private TextView toAccountName; // 对方昵称
	protected static final String TAG = "DoorCalledActivity";

	private String channleID; // 频道号
	private int userId = new Random().nextInt(Math.abs((int) System
			.currentTimeMillis()));

	@Override
	public void onCreate(Bundle savedInstance) {
		super.onCreate(savedInstance);
		EventBus.getDefault().register(this);
		setContentView(R.layout.activity_room_door_channel);
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
		dbManager = new DBManager(this);
		ll_bottom_control = (LinearLayout) findViewById(R.id.ll_bottom_control);
		toAccount = getIntent().getStringExtra("acount");
		channleID = getIntent().getStringExtra("channleID");
		setupRtcEngine();
		m_agoraAPI = AgoraAPI.getInstanceWithMedia(this, rtcEngine);
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
				.translationY(ll_bottom_control.getHeight()).setDuration(200)
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
		
		rtcEngine.setVideoProfile(21); // 360P_2 360x640
		startTimeLong = System.currentTimeMillis();
		System.out.println("------------------channleID:"+channleID);
	    try {
			this.rtcEngine.joinChannel(DesUtil.decrypt(
					PrefUtils.getString(this, "agoraVid", ""), DesUtil.DESKEY), channleID, "", userId);
		} catch (Exception e) {
			e.printStackTrace();
		}
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

		// CheckBox muter = (CheckBox) findViewById(R.id.action_muter);
		// muter.setOnCheckedChangeListener(new
		// CompoundButton.OnCheckedChangeListener() {
		//
		// @Override public void onCheckedChanged(CompoundButton compoundButton,
		// boolean mutes) {
		//
		// rtcEngine.muteLocalAudioStream(mutes); compoundButton
		// .setBackgroundResource(mutes ? R.drawable.ic_room_mute_pressed :
		// R.drawable.ic_room_mute);
		//
		// } });

		// 免提
		// CheckBox speaker = (CheckBox) findViewById(R.id.action_speaker);
		// speaker.setOnCheckedChangeListener(new
		// CompoundButton.OnCheckedChangeListener() {
		// @Override
		// public void onCheckedChanged(CompoundButton compoundButton,
		// boolean usesSpeaker) {
		//
		// rtcEngine.setEnableSpeakerphone(usesSpeaker);
		// compoundButton
		// .setBackgroundResource(usesSpeaker ? R.drawable.ic_room_loudspeaker
		// : R.drawable.ic_room_loudspeaker_pressed);
		//
		// }
		// });

		// 关闭摄像头
		// CheckBox cameraEnabler = (CheckBox)
		// findViewById(R.id.action_camera_enabler);
		// cameraEnabler
		// .setOnCheckedChangeListener(new
		// CompoundButton.OnCheckedChangeListener() {
		// @Override
		// public void onCheckedChanged(CompoundButton compoundButton,
		// boolean disablesCamera) {
		//
		// rtcEngine.muteLocalVideoStream(disablesCamera);
		//
		// if (disablesCamera) {
		// findViewById(R.id.user_local_voice_bg)
		// .setVisibility(View.VISIBLE);
		// rtcEngine.muteLocalVideoStream(true);
		//
		// } else {
		// findViewById(R.id.user_local_voice_bg)
		// .setVisibility(View.GONE);
		// rtcEngine.muteLocalVideoStream(false);
		// }
		//
		// compoundButton
		// .setBackgroundResource(disablesCamera ?
		// R.drawable.ic_room_button_close_pressed
		// : R.drawable.ic_room_button_close);
		//
		// }
		// });

		// 切换摄像头
		// CheckBox cameraSwitch = (CheckBox)
		// findViewById(R.id.action_camera_switcher);
		// cameraSwitch
		// .setOnCheckedChangeListener(new
		// CompoundButton.OnCheckedChangeListener() {
		// @Override
		// public void onCheckedChanged(CompoundButton compoundButton,
		// boolean switches) {
		//
		// rtcEngine.switchCamera();
		//
		// compoundButton
		// .setBackgroundResource(switches ?
		// R.drawable.ic_room_button_change_pressed
		// : R.drawable.ic_room_button_change);
		//
		// }
		// });

		findViewById(R.id.action_hung_up).setOnClickListener(
				getViewClickListener());

		findViewById(R.id.ll_open_door).setOnClickListener(
				getViewClickListener());
		// findViewById(R.id.action_back).setOnClickListener(getViewClickListener());
		mMainVideoContainer = (FrameLayout) findViewById(R.id.user_main_view_container);

		View simulateClick = new View(this);
		this.onUserInteraction(simulateClick);

		mCallingType = CALLING_TYPE_VIDEO;
		rtcEngine.enableVideo();
		rtcEngine.muteLocalVideoStream(false);
		rtcEngine.muteLocalAudioStream(false);
		rtcEngine.muteAllRemoteVideoStreams(false);
		this.setupChannel();

	}

	/**
	 * 远端视频
	 * 
	 * @param callingType
	 */
	void updateRemoteUserViews(int uid) {
		findViewById(R.id.user_local_voice_bg).setVisibility(View.INVISIBLE);
		FrameLayout remoteVideoUser = (FrameLayout) findViewById(R.id.user_main_view_container);
		SurfaceView remoteView = rtcEngine.CreateRendererView(this);
		remoteVideoUser.addView(remoteView, new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.MATCH_PARENT,
				FrameLayout.LayoutParams.MATCH_PARENT));
		rtcEngine.setupRemoteVideo(new VideoCanvas(remoteView,
				VideoCanvas.RENDER_MODE_HIDDEN, uid));

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
					}
				});
			}
		};

		timer = new Timer();
		timer.schedule(task, 1000, 1000);
	}

	@Override
	public void onUserInteraction(View view) {
		switch (view.getId()) {

		case R.id.action_hung_up:
			// finish();
			onBackPressed();

			break;
		case R.id.ll_open_door:
			new Thread(new Runnable() {
				@Override
				public void run() {
					
					m_agoraAPI.messageChannelSend(channleID.replaceAll("door", ""), "开门", "");
				}
			}).start();
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
				m_agoraAPI.channelLeave(channleID.replaceAll("door", ""));
				rtcEngine.leaveChannel();
			}
		}).run();

		// keep screen on - turned off
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}

	public synchronized void onFirstRemoteVideoDecoded(final int uid,
			int width, int height, final int elapsed) {

		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// findViewById(R.id.user_local_voice_bg).setVisibility(View.GONE);
				updateRemoteUserViews(uid);
			}
		});
	}

	/**
	 * 主叫被叫对方进频道
	 */
	public synchronized void onUserJoined(final int uid, int elapsed) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				updateRemoteUserViews(uid);
				Log.e("AGORA_SDK", "onUserJoined: " + uid);
				isAnimate = false;
				// app hints before you join
				isCorrect = false;
				if (toPhone != null) {
					mType = 1;
				} else {
					mType = 2;
				}

			}
		});
	}

	public void onUserOffline(final int uid) {

		// finish();

		log("onUserOffline: uid: " + uid);

		if (isFinishing()) {
			return;
		}
	}

	@Override
	public void finish() {

		if (alertDialog != null) {
			alertDialog.dismiss();
		}

		super.finish();
	}

	@Override
	public void onJoinChannelSuccess(String channel, final int uid, int elapsed) {

		userId = uid;
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
			mPerson.setNumber(toPhone == null ? channleID.substring(4)
					: toPhone);
			mPerson.setType(mType);
			mPerson.setMold(1);
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

					alertDialog = new AlertDialog.Builder(
							DoorCalledActivity.this)
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
													DoorCalledActivity.this,
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
					// appNotification.setText("网络无法连接，请检查网络");
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
		EventBus.getDefault().unregister(this);// 反注册
		m_agoraAPI.channelLeave(channleID.replaceAll("door", ""));
		rtcEngine.leaveChannel();
		handler.removeMessages(MSG_HIDE_CONTROL);
		super.onDestroy();
	}
	
	public void onEventMainThread(MsgEvent event) {
		if (event.getMsg().equals("onInviteRefusedByPeer")) { // 对方拒绝邀请
			isCorrect = false;
			mType = 5;
			handler.postDelayed(new Runnable() {

				@Override
				public void run() {
					rtcEngine.leaveChannel();
					m_agoraAPI.channelLeave(channleID.replaceAll("door", ""));
				}
			}, 2000);

		} else if (event.getMsg().equals("onInviteReceivedByPeer")) { // 对方收到邀请
			handler.removeMessages(MSG_NOT_ONLINE);
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
				Intent intentPhone = new Intent(DoorCalledActivity.this,
						CommonPhoneActivity.class);
				Bundle bundle = new Bundle();
				Person mUser = new Person();
				mUser.setName(toAccount);
				mUser.setNumber(toPhone);
				bundle.putSerializable("user", mUser);
				intentPhone.putExtras(bundle);
				DoorCalledActivity.this.startActivity(intentPhone);
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

	}

	@Override
	public void onGetResponseError(int requestCode, VolleyError error) {

	}
}