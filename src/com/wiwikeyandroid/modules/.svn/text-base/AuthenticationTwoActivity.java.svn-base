package com.wiwikeyandroid.modules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.seny.android.utils.ALog;
import org.seny.android.utils.ActivityUtils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.android.volley.VolleyError;
import com.wiwikeyandroid.R;
import com.wiwikeyandroid.activity.HomeAcivity;
import com.wiwikeyandroid.agora.MsgEvent;
import com.wiwikeyandroid.bean.DoHouseAuthVerifyCodeBean;
import com.wiwikeyandroid.bean.GetOwnerMemberBean;
import com.wiwikeyandroid.bean.GetVerifyCodeBean;
import com.wiwikeyandroid.bean.RBResponse;
import com.wiwikeyandroid.net.HttpLoader;
import com.wiwikeyandroid.net.HttpLoader.ResponseListener;
import com.wiwikeyandroid.utils.GlobalWiwikey;
import com.wiwikeyandroid.utils.PrefUtils;

import de.greenrobot.event.EventBus;

public class AuthenticationTwoActivity extends Activity implements ResponseListener {

	@InjectView(R.id.authentication_back)
	Button authenticationBack; // 返回
	@InjectView(R.id.login_code_et)
	EditText loginCodeEt; // 验证码输入框
	@InjectView(R.id.authen_getcode)
	TextView loginGetcode; // 获取验证码按钮
	@InjectView(R.id.login_getcode_hint)
	TextView loginGetcodeHint;
	@InjectView(R.id.tv_address)
	TextView tv_address;
	@InjectView(R.id.btn_commit)
	// 确定进行认证
	Button btnCommit;
	@InjectView(R.id.mobile_previous)
	TextView mobilePrevious;
	@InjectView(R.id.mobile_behind)
	TextView mobileBehind;

	@InjectView(R.id.pwd_et_4_1)
	EditText pwd1;
	@InjectView(R.id.pwd_et_4_2)
	EditText pwd2;
	@InjectView(R.id.pwd_et_4_3)
	EditText pwd3;
	@InjectView(R.id.pwd_et_4_4)
	EditText pwd4;
	private List<EditText> list = new ArrayList<EditText>();
	private int type = 0;
	private StringBuffer line;
	// 标志位,如果删除的是最后一位号码,只删除此位置数字
	private Boolean flag = false;
	private Timer timer;
	private int timePast;
	Dialog dia;
	private Display display;
	private LinearLayout lLayout_bg;
	private int mhouseId;//
	// 定义Handler
	final Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			// handler处理消息
			if (msg.what >= 1) {
				loginGetcodeHint.setText(msg.what + "s");
			} else {
				// 在handler里可以更改UI组件
				loginGetcodeHint.setVisibility(View.GONE);
				loginGetcode.setVisibility(View.VISIBLE);
				timer.cancel();
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.authentication_tenement_second);
		ButterKnife.inject(this);
		mhouseId = getIntent().getIntExtra("houseId", -1);
		mAddress = getIntent().getStringExtra("mAddress");  
		tv_address.setText(mAddress);
		ALog.d("mhouseId:"+mhouseId);
		initData();
		
		list.add(pwd1);
		list.add(pwd2);
		list.add(pwd3);
		list.add(pwd4);
		for (int i = 0; i < list.size(); i++) {
			list.get(i).addTextChangedListener(watcher);// 监听输入，焦点设置
			list.get(i).setOnKeyListener(delete);
		}

	}

	private void initData() {
			new Thread(
					new Runnable() {
						public void run() {
							Map<String, String> params = new HashMap<String, String>();
							params.put("houseId",mhouseId+"");
							params.put(
									"memberId",
									PrefUtils.getInt(AuthenticationTwoActivity.this,
											"memberId", -1) + "");
							params.put("token", PrefUtils.getString(
									AuthenticationTwoActivity.this, "token", ""));
							HttpLoader.post(GlobalWiwikey.URL_GETOWNERINFOBYID, params,
									GetOwnerMemberBean.class,
									GlobalWiwikey.REQUEST_GETOWNERINFOBYID,
									AuthenticationTwoActivity.this);
						}
					}
					).start();
		
	}

	@OnClick({ R.id.authentication_back, R.id.authen_getcode, R.id.btn_commit })
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.authentication_back: // 返回
			showPreActivity();
			break;
		case R.id.authen_getcode: // 获取验证码
			if(TextUtils.isEmpty(pwd4.getText().toString())){
				Toast.makeText(this, "请输入业主手机号码", 0).show();
				return;
			}
			String mobilePre = mobilePrevious.getText().toString();  
			String mobileBeh = mobileBehind.getText().toString(); 
			mobile = mobilePre+pwd1.getText().toString()+pwd2.getText().toString()+pwd3.getText().toString()+pwd4.getText().toString()+mobileBeh;
			
			getHouseAuthVerifyCode();
			
			loginGetcode.setVisibility(View.GONE);
			loginGetcodeHint.setVisibility(View.VISIBLE);

			timer = new Timer();
			timePast = 60;
			timer.schedule(new TimerTask() {

				@Override
				public void run() {

					Message msg = new Message();
					msg.what = timePast--;
					handler.sendMessage(msg);
				}

			}, 100, 1000);

			break;
		case R.id.btn_commit: // 提交认证,调用接口
			if(TextUtils.isEmpty(pwd4.getText().toString())){
				Toast.makeText(this, "请输入业主手机号码", 0).show();
				return;
			}
			if(TextUtils.isEmpty(loginCodeEt.getText().toString())){
				Toast.makeText(this, "请输入验证码", 0).show();
				return;
			}
			
			doHouseAuthVerifyCode(); 

			break;
		}
	}
	
	/**
	 * 房子认证确认，判断会员是否认证成功
	 */
	private void doHouseAuthVerifyCode() {
		new Thread(
				new Runnable() {
					public void run() {
						Map<String, String> params = new HashMap<String, String>();
						params.put("verifyCode",loginCodeEt.getText().toString());
						params.put("houseId",mhouseId+"");
						params.put("ownerMobile",mobile);
						params.put(
								"memberId",
								PrefUtils.getInt(AuthenticationTwoActivity.this,
										"memberId", -1) + "");
						params.put("token", PrefUtils.getString(
								AuthenticationTwoActivity.this, "token", ""));
						HttpLoader.post(GlobalWiwikey.URL_DOHOUSEAUTHVERIFYCODE, params,
								DoHouseAuthVerifyCodeBean.class,
								GlobalWiwikey.REQUEST_DOHOUSEAUTHVERIFYCODE,
								AuthenticationTwoActivity.this);
					}
				}
				).start();
	}

	/*
	 *获取验证码 
	 */
	private void getHouseAuthVerifyCode() {
		new Thread(
				new Runnable() {
					public void run() {
						Map<String, String> params = new HashMap<String, String>();
						params.put("ownerMobile",mobile);
						params.put(
								"memberId",
								PrefUtils.getInt(AuthenticationTwoActivity.this,
										"memberId", -1) + "");
						params.put("token", PrefUtils.getString(
								AuthenticationTwoActivity.this, "token", ""));
						HttpLoader.post(GlobalWiwikey.URL_GETHOUSEAUTHVERIFYCODE, params,
								GetVerifyCodeBean.class,
								GlobalWiwikey.REQUEST_GETHOUSEAUTHVERIFYCODE,
								AuthenticationTwoActivity.this);
					}
				}
				).start();
	}

	OnKeyListener delete = new OnKeyListener() {

		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			// TODO Auto-generated method stub
			if (type == 1) {
				type = 0;
				if (keyCode == KeyEvent.KEYCODE_DEL) {
					int done_delete_id = getCurrentFocus().getId();
					for (int i = list.size() - 1; i > 0; i--) {
						if (list.get(i).getId() == done_delete_id) {
							int before_index = i - 1;
							if (i == 3 && flag == true) {
								list.get(i).setText(null);
								list.get(i).setFocusableInTouchMode(true);
								list.get(i).setFocusable(true);
								list.get(i).requestFocus();
								i = i - 1;
								flag = false;
								break;
							} else {
								try {
									EditText before_edit = list
											.get(before_index);
									before_edit.setFocusableInTouchMode(true);
									before_edit.setFocusable(true);
									before_edit.requestFocus();
									before_edit.setText(null);
									list.get(i).setFocusable(false);
								} catch (Exception e) {

								}
							}
						}
					}
				}
			} else {
				type = 1;
			}

			return false;
		}
	};

	TextWatcher watcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub
			if (s.length() == 1) {

				int done_input_id = getCurrentFocus().getId();
				for (int i = 0; i < list.size(); i++) {
					if (list.get(i).getId() == done_input_id) {
						int next_index = i + 1;
						if (i == 3) {
							//
							flag = true;
							InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
							imm.hideSoftInputFromWindow(pwd4.getWindowToken(),
									0);
							list.get(i).setCursorVisible(false);
							return;

						}
						try {

							EditText next_edit = list.get(next_index);
							next_edit.setFocusableInTouchMode(true);
							next_edit.setFocusable(true);
							next_edit.requestFocus();
							list.get(i).setFocusable(false);
						} catch (Exception e) {

						}
					}
				}
			} else {
				pwd4.setCursorVisible(true);
			}

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {

		}

		@Override
		public void afterTextChanged(Editable s) {

		}
	};
	private String mobile;//输入的手机号码
	private String verifyCode;//收到的验证码
	private String mAddress; //所选的具体详细地址

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (timer != null) {
			timer.cancel();
		}
	}
	
	public void showPreActivity() {
		ActivityUtils.startActivityAndFinish(this,
				AuthenticationOneActivity.class);
		overridePendingTransition(R.anim.trans_in_pre, R.anim.trans_out_pre);
	}

	@Override
	public void onGetResponseSuccess(int requestCode, RBResponse response) {
		switch (requestCode) {
		case GlobalWiwikey.REQUEST_GETOWNERINFOBYID:
			GetOwnerMemberBean  mMemberBean= (GetOwnerMemberBean) response;
			if (mMemberBean.getErrmsg().equals("SUCCESS")) {
				String ownerMobile = mMemberBean.getOwnerMember().getMobile(); 
				String mobile_previous = ownerMobile.substring(0, 3);
				String mobile_behind = ownerMobile.substring(7);
				mobilePrevious.setText(mobile_previous);
				mobileBehind.setText(mobile_behind);
			}
			break;
		case  GlobalWiwikey.REQUEST_GETHOUSEAUTHVERIFYCODE:
			GetVerifyCodeBean codeBean = (GetVerifyCodeBean) response;
			if (codeBean.getErrmsg().equals("SUCCESS")) {
				verifyCode = codeBean.getVerifyCode();
				
			}
			break;
		case GlobalWiwikey.REQUEST_DOHOUSEAUTHVERIFYCODE:
			DoHouseAuthVerifyCodeBean authVerifyCodeBean = (DoHouseAuthVerifyCodeBean) response;	
			if (authVerifyCodeBean.getErrmsg().equals("SUCCESS")) {
				int memberType = authVerifyCodeBean.getMemberModel().getMemberType();
				PrefUtils.setInt(this, "memberType", memberType);
				PrefUtils.setInt(this, "houseId0", mhouseId);
				EventBus.getDefault().post(
						new MsgEvent("auThenSucceed"));
				//认证成功
				auThenSucceedDialog();
				
				// Uri notifyUri = Uri.parse("content://com.wiwikeyandroid");
				 //getContentResolver().notifyChange(notifyUri, null);
			}else {
				//认证失败
				auThenFailDialog();
			}
			
			break;
		default:
			break;
		}
		
		
	}
	
	/**
	 *  认证结果
	 */
	  
	private void auThenSucceedDialog() {

		dia = new Dialog(this, R.style.AlertDialogStyle); // 获取Dialog布局
		View viewDialog = LayoutInflater.from(this).inflate(
				R.layout.authentication_bind, null);
		
		lLayout_bg = (LinearLayout) viewDialog.findViewById(R.id.lLayout_bg);
		ImageView iv_close = (ImageView) viewDialog.findViewById(R.id.iv_close);
		EditText et_address_plot_ = (EditText) viewDialog
				.findViewById(R.id.et_address_plot);
		
		 TextView tv_title = (TextView) viewDialog
				.findViewById(R.id.tv_title);
		 tv_title.setText("恭喜你，认证成功");
		 tv_title.setTextColor(getResources().getColor(R.color.colorBlue));
		 
		 ImageView iv_image = (ImageView) viewDialog
				.findViewById(R.id.iv_image);
		 iv_image.setBackgroundResource(R.drawable.home_bg2);
		 
		 TextView tv_content1 = (TextView) viewDialog
					.findViewById(R.id.tv_content1);
		 TextView tv_content2 = (TextView) viewDialog
					.findViewById(R.id.tv_content2);
		 tv_content1.setText("您的权限已解开");
		 tv_content2.setText("可以使用本应用其它功能啦~");
		 
		Button bt_submit = (Button) viewDialog.findViewById(R.id.bt_submit);
		bt_submit.setText("去使用");
		dia.setContentView(viewDialog);

		WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		display = windowManager.getDefaultDisplay(); // 调整dialog背景大小 //

		lLayout_bg.setLayoutParams(new FrameLayout.LayoutParams(
				(int) (display.getWidth() * 0.85),
				LayoutParams.WRAP_CONTENT));

		bt_submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dia.cancel();
				ActivityUtils.startActivityAndFinish(AuthenticationTwoActivity.this, HomeAcivity.class);
				
			}
		});
		iv_close.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dia.cancel();
				ActivityUtils.startActivityAndFinish(AuthenticationTwoActivity.this, HomeAcivity.class);
			}
		});
		dia.show();
		dia.setCanceledOnTouchOutside(false);
	}

	
	private void auThenFailDialog() {

		dia = new Dialog(this, R.style.AlertDialogStyle); // 获取Dialog布局
		View viewDialog = LayoutInflater.from(this).inflate(
				R.layout.authentication_bind, null);
		
		lLayout_bg = (LinearLayout) viewDialog.findViewById(R.id.lLayout_bg);
		ImageView iv_close = (ImageView) viewDialog.findViewById(R.id.iv_close);
		EditText et_address_plot_ = (EditText) viewDialog
				.findViewById(R.id.et_address_plot);
		
		 TextView tv_title = (TextView) viewDialog
				.findViewById(R.id.tv_title);
		 tv_title.setText("对不起，认证失败!");
		 tv_title.setTextColor(getResources().getColor(R.color.sub_head));
		 
		 ImageView iv_image = (ImageView) viewDialog
				.findViewById(R.id.iv_image);
		 iv_image.setBackgroundResource(R.drawable.home_bg3);
		 
		 TextView tv_content1 = (TextView) viewDialog
					.findViewById(R.id.tv_content1);
		 TextView tv_content2 = (TextView) viewDialog
					.findViewById(R.id.tv_content2);
		 tv_content1.setText("手机号码匹配不成功");
		 tv_content2.setText("你可以联系业主补充成员信息");
		 tv_content2.setTextSize(18);
		 tv_content2.setTextColor(getResources().getColor(R.color.sub_head));
		Button bt_submit = (Button) viewDialog.findViewById(R.id.bt_submit);
		bt_submit.setText("再次认证");
		dia.setContentView(viewDialog);

		WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		display = windowManager.getDefaultDisplay(); // 调整dialog背景大小 //

		lLayout_bg.setLayoutParams(new FrameLayout.LayoutParams(
				(int) (display.getWidth() * 0.85),
				LayoutParams.WRAP_CONTENT));

		bt_submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dia.cancel();
				//ActivityUtils.startActivityAndFinish(AuthenticationTwoActivity.this, HomeAcivity.class);

			}
		});
		iv_close.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dia.cancel();
			}
		});
		dia.show();
		dia.setCanceledOnTouchOutside(false);
	}
	
	@Override
	public void onGetResponseError(int requestCode, VolleyError error) {
			
		
	}
	

}
