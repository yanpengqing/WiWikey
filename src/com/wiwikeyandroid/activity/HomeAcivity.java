package com.wiwikeyandroid.activity;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import org.seny.android.utils.ALog;
import org.seny.android.utils.ActivityUtils;
import org.seny.android.utils.AppInfoUtil;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.wiwikeyandroid.R;
import com.wiwikeyandroid.agora.AgoraService;
import com.wiwikeyandroid.agora.ConnectionReceiver;
import com.wiwikeyandroid.agora.MsgEvent;
import com.wiwikeyandroid.base.BaseActivity;
import com.wiwikeyandroid.bean.GetVerifyCodeBean;
import com.wiwikeyandroid.bean.RBResponse;
import com.wiwikeyandroid.modules.AuthenticationOneActivity;
import com.wiwikeyandroid.modules.Contacts.ContactsFragment;
import com.wiwikeyandroid.modules.family.FamilyFragment;
import com.wiwikeyandroid.modules.phone.PhoneFragment;
import com.wiwikeyandroid.modules.setting.SettingFragment;
import com.wiwikeyandroid.net.HttpLoader;
import com.wiwikeyandroid.net.HttpLoader.ResponseListener;
import com.wiwikeyandroid.socket.SocketTransceiver;
import com.wiwikeyandroid.socket.TcpClient;
import com.wiwikeyandroid.utils.GeneralUtils;
import com.wiwikeyandroid.utils.GlobalWiwikey;
import com.wiwikeyandroid.utils.PrefUtils;
import com.wiwikeyandroid.utils.WifiOperator;
import com.wiwikeyandroid.view.AlertDialog;
import com.xiaomi.mipush.sdk.MiPushClient;

import de.greenrobot.event.EventBus;

/**
 * 
 * @author Joseph丶 on 2016/5/25.
 * 
 *         主界面UI
 * 
 */

public class HomeAcivity extends BaseActivity implements OnClickListener,
		ResponseListener {

	private FragmentManager fragmentManager; // Fragment管理
	private PhoneFragment mPhoneFragment;
	private ContactsFragment mContactsFragment;
	private FamilyFragment mFamilyFragment;
	private SettingFragment mSettingFragment;

	private ImageView phoneIv;
	private ImageView contactsIv;
	private ImageView doorIv;
	private ImageView familyIv;
	private ImageView settingIv;

	private TextView phoneTv;
	private TextView contactsTv;
	private TextView familyTv;
	private TextView settingTv;
	private String tag = "0";
	private boolean isAppExit; // app退出标志位
	public static final int APPEXIT = -1;//
	private ConnectionReceiver myReceiver;
	Dialog dia;
	private Display display;
	private LinearLayout lLayout_bg;
	private Context mContext;

	private TcpClient client = new TcpClient() {
		TcpClient _this = this;

		@Override
		public void onConnect(SocketTransceiver transceiver) {
			handler.post(new Runnable() {
				@Override
				public void run() {
					System.out.println("--------------------------开始发送开门数据");
					// sendOpenInfo();
					new Thread(new WriteHandlerThread(
							_this.getTransceiver().socket, mContext)).start();
				}
			});
		}

		@Override
		public void onDisconnect(SocketTransceiver transceiver) {
			System.out.println("--------------------------断开连接");
		}

		@Override
		public void onConnectFailed() {
			handler.post(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(HomeAcivity.this, "连接失败,请确定是否已连上门口机的Wifi",
							Toast.LENGTH_SHORT).show();
				}
			});
		}

		@Override
		public void onReceive(SocketTransceiver transceiver, final String s) {
			handler.post(new Runnable() {
				@Override
				public void run() {
					if ("sucess".equals(s)) {
						Toast.makeText(HomeAcivity.this, "开门成功",
								Toast.LENGTH_SHORT).show();
						oPenDoorDialog();
					} else {
						if ("failed1".equals(s)) {
							Toast.makeText(HomeAcivity.this, "开门失败,您不是本单元业主",
									Toast.LENGTH_SHORT).show();
						} else if ("failed2".equals(s)) {
							Toast.makeText(HomeAcivity.this, "开门失败,您不是本小区业主",
									Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(HomeAcivity.this, "开门失败",
									Toast.LENGTH_SHORT).show();
						}

					}
					// socketConnect();
					_this.disconnect();
				}
			});
		}
	};

	@Override
	protected int initContentView() {
		getWindow().addFlags(
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
						| WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
						| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

		return R.layout.activity_home;
	}

	@Override
	protected void initView() {
		phoneIv = (ImageView) findViewById(R.id.id_phone_iv);
		contactsIv = (ImageView) findViewById(R.id.id_contacts_iv);
		doorIv = (ImageView) findViewById(R.id.id_opendoor_iv);
		familyIv = (ImageView) findViewById(R.id.id_family_iv);
		settingIv = (ImageView) findViewById(R.id.id_setting_iv);

		phoneTv = (TextView) findViewById(R.id.id_phone_tv);
		contactsTv = (TextView) findViewById(R.id.id_contacts_tv);
		// doorTv = (TextView) findViewById(R.id.id_door_tv);
		familyTv = (TextView) findViewById(R.id.id_family_tv);
		settingTv = (TextView) findViewById(R.id.id_setting_tv);
	}

	@Override
	protected void initListener() {
		findViewById(R.id.id_phone_ll).setOnClickListener(this);
		findViewById(R.id.id_contacts_ll).setOnClickListener(this);
		findViewById(R.id.id_door_ll).setOnClickListener(this);
		findViewById(R.id.id_family_ll).setOnClickListener(this);
		findViewById(R.id.id_setting_ll).setOnClickListener(this);

		phoneIv.setOnClickListener(this);
		contactsIv.setOnClickListener(this);
		doorIv.setOnClickListener(this);
		familyIv.setOnClickListener(this);
		settingIv.setOnClickListener(this);
	}

	@Override
	protected void initData() {
		ALog.w("initData");
		enterAppRecode();
		
		IntentFilter filter = new IntentFilter(
				ConnectivityManager.CONNECTIVITY_ACTION);
		myReceiver = new ConnectionReceiver();
		registerReceiver(myReceiver, filter);
		startService(new Intent(this, AgoraService.class));
		MiPushClient.setAlias(this, PrefUtils.getString(this, "mobile", ""),
				null);
		if (!PrefUtils.getString(this, "memberHouseList0", "").isEmpty()) {
			String plotId = PrefUtils.getString(this, "memberHouseList0", "")
					.split("#")[1];
			System.out.println("------------plotId：" + plotId);
			MiPushClient.setUserAccount(this, plotId, null);
		}

		fragmentManager = getSupportFragmentManager();
		setTabSelection(0);
	}

	/**
	 * 更新会员基础信息包括登录统计信息
	 */
	private void enterAppRecode() {
		new Thread(new Runnable() {
			public void run() {
				Map<String, String> params = new HashMap<String, String>();
				params.put("appType", "1");
				params.put("mobileImei", AppInfoUtil.getIMEI(HomeAcivity.this));
				params.put("memberId",
						PrefUtils.getInt(HomeAcivity.this, "memberId", -1) + "");
				params.put("token",
						PrefUtils.getString(HomeAcivity.this, "token", ""));
				HttpLoader.post(GlobalWiwikey.URL_ENTERAPPRECODE, params,
						GetVerifyCodeBean.class,
						GlobalWiwikey.REQUEST_ENTERAPPRECODE, HomeAcivity.this);
			}
		}).start();

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.id_phone_ll:
		case R.id.id_phone_iv:
			setTabSelection(0);
			break;
		case R.id.id_contacts_ll:
		case R.id.id_contacts_iv:
			setTabSelection(1);
			break;
		case R.id.id_door_ll:
		case R.id.id_opendoor_iv:
			setTabSelection(2);
			break;
		case R.id.id_family_ll:
		case R.id.id_family_iv:
			setTabSelection(3);
			break;
		case R.id.id_setting_ll:
		case R.id.id_setting_iv:
			setTabSelection(4);
			break;
		default:
			break;
		}
	}

	/**
	 * @param index
	 */
	public void setTabSelection(int index) {
		// 开启一个Fragment事务
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		if (index != 2) {
			// 每次选中之前先清除掉上次的选中状态
			clearSelection();
			// 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
			hideFragments(transaction);
		}
		switch (index) {
		case 0:
			tag = "0";
			phoneIv.setImageResource(R.drawable.phone1);
			phoneTv.setTextColor(getResources().getColor(R.color.colorBlue));
			if (mPhoneFragment == null) {
				// 如果mPhoneFragment为空，则创建一个并添加到界面上
				mPhoneFragment = new PhoneFragment();
				// mallFragment.setArguments(bundle);
				transaction.add(R.id.home_fl, mPhoneFragment);

			} else {
				// 如果mPhoneFragment不为空，则直接将它显示出来
				transaction.show(mPhoneFragment);
			}
			break;
		case 1:
			tag = "1";
			contactsIv.setImageResource(R.drawable.contacts1);
			contactsTv.setTextColor(getResources().getColor(R.color.colorBlue));
			if (mContactsFragment == null) {
				// 如果mContactsFragment为空，则创建一个并添加到界面上
				mContactsFragment = new ContactsFragment();
				// mallFragment.setArguments(bundle);
				transaction.add(R.id.home_fl, mContactsFragment,
						"ContactsFragment");
			} else {
				// 如果mContactsFragment不为空，则直接将它显示出来
				transaction.show(mContactsFragment);
			}
			break;
		case 2:
			tag = "2";
			// doorIv.setBackgroundResource(R.drawable.biz_navigation_tab_va_selected);
			int memberType = PrefUtils.getInt(this, "memberType", -1);
			if (memberType == 2) {

				new AlertDialog(this).builder().setTitle("你好")
						.setMsg("你还不是小区的认证住户")
						.setPositiveButton("去认证", new OnClickListener() {
							@Override
							public void onClick(View v) {
								// 启动认证界面
								ActivityUtils.startActivity(HomeAcivity.this,
										AuthenticationOneActivity.class);

							}
						}).setNegativeButton("取消", new OnClickListener() {
							@Override
							public void onClick(View v) {

							}
						}).show();
			} else if (memberType == 1) {// 住户 开门动作
				// 1 首先与当前门口机socket连接，成功后发送一条指令过去（当前会员ID，小区ID，单元ID等）
				// 2 门口机收到信息后判断此会员是否是本小区本单元的住户，是的话执行开门指令。
				// 完成以后断开socket连接
				WifiOperator wifiOperator = new WifiOperator(this);
				if (wifiOperator.isOpenWifi() == false) {
					Toast.makeText(this, "正在连接门口机，请等待", Toast.LENGTH_LONG)
							.show();
					boolean openWifi = wifiOperator.openWifi();
					if (openWifi) {
						try {
							Thread.sleep(5000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}

				wifiOperator.access2Wifi(GlobalWiwikey.DOORWIFI);
				boolean isConnWifi = false;

				int startTime = GeneralUtils.getTimestamp().intValue();
				while (true) {
					isConnWifi = wifiOperator.isConnMyWifi();

					if (isConnWifi == true) {
						boolean isCon = wifiOperator.isWifiConnect(this);
						if (isCon == true) {
							break;
						}
					}

					int currTime = GeneralUtils.getTimestamp().intValue();
					if (currTime - startTime > 10) {
						break;
					}
				}
				if (isConnWifi == true) {
					if (client.isConnected() == false) {
						client.connect(GlobalWiwikey.SOCKET_IP,
								GlobalWiwikey.SOCKET_PORT);
					} else {
						Toast.makeText(this, "不要频繁点击", Toast.LENGTH_SHORT)
								.show();
						client.disconnect();
					}
				} else {
					Toast.makeText(this,
							"请先连接上门口机的WIFI：" + GlobalWiwikey.DOORWIFI,
							Toast.LENGTH_LONG).show();
				}
			}

			/*
			 * if (vaFragment == null) { // 如果BrandSaleFragment为空，则创建一个并添加到界面上
			 * vaFragment = new VaFragment(); //
			 * mallFragment.setArguments(bundle); transaction.add(R.id.home_fl,
			 * vaFragment); } else { // 如果MessageFragment不为空，则直接将它显示出来
			 * transaction.show(vaFragment); }
			 */
			break;
		case 3:
			tag = "3";
			familyIv.setImageResource(R.drawable.family1);
			familyTv.setTextColor(getResources().getColor(R.color.colorBlue));
			if (mFamilyFragment == null) {
				// 如果mFamilyFragment为空，则创建一个并添加到界面上
				mFamilyFragment = new FamilyFragment();
				// mallFragment.setArguments(bundle);
				transaction.add(R.id.home_fl, mFamilyFragment);
			} else {
				// 如果mFamilyFragment不为空，则直接将它显示出来
				transaction.show(mFamilyFragment);
			}
			break;
		case 4:
			tag = "4";
			settingIv.setImageResource(R.drawable.setting1);
			settingTv.setTextColor(getResources().getColor(R.color.colorBlue));
			if (mSettingFragment == null) {
				// 如果BrandSaleFragment为空，则创建一个并添加到界面上
				mSettingFragment = new SettingFragment();
				// mallFragment.setArguments(bundle);
				transaction.add(R.id.home_fl, mSettingFragment);
			} else {
				// 如果mSettingFragment不为空，则直接将它显示出来
				transaction.show(mSettingFragment);
			}
			break;
		}
		transaction.commit();
	}

	/**
	 * 清除掉所有的选中状态。
	 */

	private void clearSelection() {
		phoneIv.setImageResource(R.drawable.phone);
		contactsIv.setImageResource(R.drawable.contacts);
		familyIv.setImageResource(R.drawable.family);
		settingIv.setImageResource(R.drawable.setting);

		phoneTv.setTextColor(getResources().getColor(R.color.colorBlack));
		contactsTv.setTextColor(getResources().getColor(R.color.colorBlack));
		familyTv.setTextColor(getResources().getColor(R.color.colorBlack));
		settingTv.setTextColor(getResources().getColor(R.color.colorBlack));
	}

	/**
	 * 清空fragment任务栈
	 */
	private void clearFragment() {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		/**
		 * 每次点击主页面下面五个分类，清空前面所有的fragment任务栈
		 */
		int n = getSupportFragmentManager().getBackStackEntryCount();
		for (int i = 0; i < n; i++) {
			FragmentManager.BackStackEntry backstatck = getSupportFragmentManager()
					.getBackStackEntryAt(i);
			getSupportFragmentManager().popBackStack();
		}
	}

	/**
	 * 将所有的Fragment都置为隐藏状态。
	 * 
	 * @param transaction
	 *            用于对Fragment执行操作的事务
	 */
	private void hideFragments(FragmentTransaction transaction) {

		if (mPhoneFragment != null) {
			transaction.hide(mPhoneFragment);
		}
		if (mContactsFragment != null) {
			transaction.hide(mContactsFragment);
		}
		// if (mFamilyFragment != null) {
		// transaction.hide(mFamilyFragment);
		// }
		if (mFamilyFragment != null) {
			transaction.hide(mFamilyFragment);
		}
		if (mSettingFragment != null) {
			transaction.hide(mSettingFragment);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (tag.equals("1") || tag.equals("2") || tag.equals("3")
					|| tag.equals("4")) {
				setTabSelection(0);
			} else if (tag.equals("0")) {
				appExit();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 退出app
	 */
	public void appExit() {

		if (!isAppExit) {
			isAppExit = true;
			Toast.makeText(this, "再按一次,返回桌面", Toast.LENGTH_SHORT).show();
			handler.sendEmptyMessageDelayed(APPEXIT, 2000);
		} else {
			// 2s内再次按back时,isExit= true，执行以下操作，app退出
			// System.exit(0);
			moveTaskToBack(true);
		}
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case APPEXIT:
				isAppExit = false;
				break;
			default:
				break;
			}
		};
	};

	/**
	 * 开门成功
	 */

	private void oPenDoorDialog() {

		dia = new Dialog(this, R.style.AlertDialogStyle); // 获取Dialog布局
		View viewDialog = LayoutInflater.from(this).inflate(
				R.layout.open_door_dialog, null);

		lLayout_bg = (LinearLayout) viewDialog.findViewById(R.id.lLayout_bg);

		dia.setContentView(viewDialog);

		WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		display = windowManager.getDefaultDisplay();
		// 调整dialog背景大小
		lLayout_bg.setLayoutParams(new FrameLayout.LayoutParams((int) (display
				.getWidth() * 0.85), LayoutParams.WRAP_CONTENT));
		dia.show();
		dia.setCanceledOnTouchOutside(false);
		new Handler().postDelayed(new Runnable() {

			public void run() {
				dia.cancel();
			}

		}, 1500);

	}

	public void onEventMainThread(MsgEvent event) {
		switch (event.getMsg()) {
		case "token":
			getSharedPreferences("config", Context.MODE_PRIVATE).edit().clear()
					.commit();
			PrefUtils.setBoolean(getApplication(), "isFirst", false);
			MiPushClient.unsetAlias(this,
					PrefUtils.getString(this, "mobile", ""), null);
			new AlertDialog(this).builder()
					.setMsg("检测到您的账号已在别的设备登录，您已被迫下线，请重新登录")
					.setNegativeButton("确定", new OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = new Intent(HomeAcivity.this,
									LoginActivity.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
									| Intent.FLAG_ACTIVITY_NEW_TASK);
							startActivity(intent);
							finish();
						}
					}).setCancelable(false).show();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		ALog.w("HomeAcivity:onCreate！");
		mContext = this;
		EventBus.getDefault().register(this);
		super.onCreate(savedInstanceState);
	}

	protected void onStart() {
		ALog.w("onStart！");
		super.onStart();
	};

	@Override
	protected void onResume() {
		ALog.w("onResume！");
		// if (mContactsFragment!=null) {
		// mContactsFragment.initData();
		// }
		super.onResume();
	}

	@Override
	protected void onRestart() {
		ALog.w("onRestart！");
		super.onRestart();
	}

	@Override
	protected void onPause() {
		ALog.w("onPause！");
		super.onPause();
	}

	@Override
	protected void onStop() {
		ALog.w("onStop！");
		super.onStop();
	}

	@Override
	public void onGetResponseSuccess(int requestCode, RBResponse response) {
		switch (requestCode) {
		case GlobalWiwikey.REQUEST_ENTERAPPRECODE:
			// GetVerifyCodeBean bean = (GetVerifyCodeBean) response;

			break;

		default:
			break;
		}

	}

	@Override
	public void onGetResponseError(int requestCode, VolleyError error) {

	}

	/**
	 * 设置IP和端口地址,连接或断开
	 */
	private void socketConnect() {
		if (client.isConnected()) {
			// 断开连接
			System.out.println("----------------------------disconnect");
			client.disconnect();
		} else {
			try {
				System.out.println("----------------------------connect");
				client.connect(GlobalWiwikey.SOCKET_IP,
						GlobalWiwikey.SOCKET_PORT);
			} catch (NumberFormatException e) {
				Toast.makeText(this, "端口错误", Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			}
		}
	}

	/**
	 * 发送数据
	 */
	private void sendOpenInfo() {
		try {
			String str = PrefUtils.getString(this, "memberHouseList0", "");
			int memberId = PrefUtils.getInt(this, "memberId", 0);
			if (str != null) {
				String[] pstrs = str.split("#");
				if (pstrs.length == 4) {
					String data = memberId + "#" + pstrs[1] + "#" + pstrs[3];
					System.out.println("------------------data:" + data);
					// client.getTransceiver().send(data);
					client.getTransceiver().sendInfo(data);
				} else {
					Toast.makeText(this, "您未绑定房屋", Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(this, "您未绑定房屋", Toast.LENGTH_SHORT).show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

/*
 * 处理写操作的线程
 */
class WriteHandlerThread implements Runnable {
	private Socket client;
	private Context mContext;

	public WriteHandlerThread(Socket client, Context mContext) {
		this.client = client;
		this.mContext = mContext;
	}

	@Override
	public void run() {
		DataOutputStream dos = null;
		try {
			String str = PrefUtils.getString(mContext, "memberHouseList0", "");
			int memberId = PrefUtils.getInt(mContext, "memberId", 0);
			if (str != null) {
				String[] pstrs = str.split("#");
				if (pstrs.length == 4) {
					String data = memberId + "#" + pstrs[1] + "#" + pstrs[3]
							+ "\n";
					System.out.println("------------------data:" + data);
					dos = new DataOutputStream(client.getOutputStream());
					// dos.writeUTF(data);
					dos.write(data.getBytes());
				} else {
					Toast.makeText(mContext, "您未绑定房屋", Toast.LENGTH_SHORT)
							.show();
				}
			} else {
				Toast.makeText(mContext, "您未绑定房屋", Toast.LENGTH_SHORT).show();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}