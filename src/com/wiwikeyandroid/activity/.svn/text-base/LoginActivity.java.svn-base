package com.wiwikeyandroid.activity;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.seny.android.utils.ALog;
import org.seny.android.utils.ActivityUtils;
import org.seny.android.utils.MD5Utils;
import org.xmlpull.v1.XmlPullParser;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.android.volley.VolleyError;
import com.wiwikeyandroid.R;
import com.wiwikeyandroid.agora.AgoraService;
import com.wiwikeyandroid.bean.GetVerifyCodeBean;
import com.wiwikeyandroid.bean.LoginPasswordBean;
import com.wiwikeyandroid.bean.RBResponse;
import com.wiwikeyandroid.modules.setting.AgreementActivity;
import com.wiwikeyandroid.net.HttpLoader;
import com.wiwikeyandroid.net.HttpLoader.ResponseListener;
import com.wiwikeyandroid.utils.DesUtil;
import com.wiwikeyandroid.utils.GlobalWiwikey;
import com.wiwikeyandroid.utils.PrefUtils;
import com.wiwikeyandroid.utils.Tools;

public class LoginActivity extends Activity implements ResponseListener {

	private static final String TAG = "LoginActivity";
	@InjectView(R.id.agreement)
	TextView agreement;
	@InjectView(R.id.login_password_iv)
	ImageView login_code;
	@InjectView(R.id.login_username_et)
	EditText etUserName;
	@InjectView(R.id.login_password_et)
	EditText etPassword;
	@InjectView(R.id.login_getcode)
	TextView tvGetCode;
	@InjectView(R.id.login_getcode_hint)
	TextView tvHint;
	@InjectView(R.id.login_forgetpassword)
	TextView loginForgetpassword;
	@InjectView(R.id.login_enter)
	Button loginEnter;
	@InjectView(R.id.login_type)
	TextView tvType;
	@InjectView(R.id.liner_top)
	LinearLayout linerTop;
	@InjectView(R.id.amin)
	RelativeLayout amin;
	private Animation my_Translate;// 位移动画
	private Animation my_Rotate;// 旋转动画
	private Timer timer;
	private int timePast;

	private boolean iscode = true; // 登录模式，默认验证码登录

	// 定义Handler
	final Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			// handler处理消息
			if (msg.what >= 1 && iscode) {
				tvHint.setText(msg.what + "s");
			} else {
				// 在handler里可以更改UI组件
				tvHint.setVisibility(View.GONE);
				tvGetCode.setVisibility(View.VISIBLE);
				timer.cancel();
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login_activity);
		ButterKnife.inject(this);
		receiver = new HomeKeyEventBroadCastReceiver();
		registerReceiver(receiver, new IntentFilter(
				Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
		etUserName.addTextChangedListener(textChange);
		tvGetCode.setTextColor(this.getResources().getColor(R.color.gray_text));
		stopService(new Intent(LoginActivity.this, AgoraService.class));
		initview();
	}

	private void initview() {
		SpannableString spStr = new SpannableString(getResources().getString(R.string.LinkText)); 
		ClickableSpan clickSpan = new NoLineClickSpan(spStr.toString()); 
		spStr.setSpan(clickSpan, 0, spStr.length(),
				Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
		agreement.append(spStr);
		agreement.setMovementMethod(LinkMovementMethod.getInstance());
	}

	private class NoLineClickSpan extends ClickableSpan {

		public NoLineClickSpan(String text) {
			super();
		}

		@Override
		public void updateDrawState(TextPaint ds) {
			ds.setColor(getResources().getColor(R.color.colorBlue));
			ds.setUnderlineText(false); 
		}

		@Override
		public void onClick(View widget) {
			// 点击进入
			ActivityUtils
			.startActivity(LoginActivity.this, AgreementActivity.class);
		}
	}

	private void anim() {
		my_Translate = AnimationUtils.loadAnimation(this, R.anim.my_translate);
		my_Rotate = AnimationUtils.loadAnimation(this, R.anim.my_rotate);
	}

	/*
	 * 登录方式改变更改UI
	 */
	private void changeLoginState() {
		if (iscode) { // 密码登录
			loginForgetpassword.setVisibility(View.VISIBLE);
			login_code.setImageResource(R.drawable.login_password);
			tvType.setText("通过验证码登录");
			etPassword.setText("");
			etPassword.setInputType(InputType.TYPE_CLASS_TEXT
					| InputType.TYPE_TEXT_VARIATION_PASSWORD);
			etPassword.setHint("请输入密码 不少于六位数");
			tvGetCode.setVisibility(View.GONE);

			tvHint.setVisibility(View.GONE);
			// tvGetCode.setVisibility(View.VISIBLE);
			if (timer != null) {
				timer.cancel();
			}

			iscode = false;

		} else {// 验证码登录
			login_code.setImageResource(R.drawable.login_code);
			loginForgetpassword.setVisibility(View.GONE);
			tvType.setText("通过密码登录");

			etPassword.setInputType(InputType.TYPE_CLASS_NUMBER
					| InputType.TYPE_NUMBER_VARIATION_NORMAL);
			etPassword.setHint("请输入验证码");
			tvGetCode.setVisibility(View.VISIBLE);

			etPassword.setText("");
			iscode = true;

			if (etUserName.getText().length() == 11) {

				try {
					XmlPullParser xrp = getResources().getXml(
							R.color.textcolor_switch);
					ColorStateList csl = ColorStateList.createFromXml(
							getResources(), xrp);
					tvGetCode.setTextColor(csl);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}

	}

	/**
	 * 监听账号输入长度变化
	 */
	private TextWatcher textChange = new TextWatcher() {

		@Override
		public void afterTextChanged(Editable s) {

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int before,
				int count) {

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {

			if (TextUtils.getTrimmedLength(s) == 11) {
				try {
					XmlPullParser xrp = getResources().getXml(
							R.color.textcolor_switch);
					ColorStateList csl = ColorStateList.createFromXml(
							getResources(), xrp);
					tvGetCode.setTextColor(csl);
				} catch (Exception e) {
				}
			}

			else {
				tvGetCode.setTextColor(LoginActivity.this.getResources()
						.getColor(R.color.gray_text));
			}
		}

	};
	private String verifyCode; // 收到的短信验证码
	private HomeKeyEventBroadCastReceiver receiver;

	@OnClick({ R.id.login_getcode, R.id.login_forgetpassword, R.id.login_enter,
			R.id.login_type })
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.login_getcode: // 获取验证码
			if (etUserName.getText().toString().length() != 11) {
				return;
			}

			if (!Tools.isPhoneNum(etUserName.getText().toString().trim())) {
				Toast.makeText(this, "非法手机号！", Toast.LENGTH_SHORT).show();
				return;
			}

			tvGetCode.setVisibility(View.GONE);
			tvHint.setVisibility(View.VISIBLE);

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

			Map<String, String> params = new HashMap<String, String>();

			params.put("mobile", etUserName.getText().toString().trim());

			HttpLoader.post(GlobalWiwikey.GET_VERIFYCODE, params,
					GetVerifyCodeBean.class, GlobalWiwikey.REQUEST_CODE, this);

			break;
		case R.id.login_forgetpassword:// 忘记密码
			ActivityUtils.startActivityForData(this,
					ForgetPasswordActivity.class, etUserName.getText()
							.toString().trim());

			break;
		case R.id.login_enter: // 登录
			doLogin();

			/*
			 * startActivity(new Intent(LoginActivity.this, HomeAcivity.class));
			 * Intent intentAgora = new Intent(this, AgoraService.class);
			 * intentAgora.putExtra("acount",
			 * etUserName.getText().toString().trim());
			 * startService(intentAgora); finish();
			 */

			break;
		case R.id.login_type:// 短信验码或密码登录
			changeLoginState();

			break;
		}
	}

	/**
	 * 登录
	 */
	private void doLogin() {

		if (etUserName.getText().toString().trim().equals("")) {
			Toast.makeText(this, "请输入手机号码！", Toast.LENGTH_SHORT).show();
			return;
		}
		if (!Tools.isPhoneNum(etUserName.getText().toString().trim())) {
			Toast.makeText(this, "非法手机号！", Toast.LENGTH_SHORT).show();
			return;
		}
		String hint = "请输入密码！";
		if (iscode) {
			hint = "请输入验证码！";
		}
		if (etPassword.getText().toString().trim().equals("")) {
			Toast.makeText(this, hint, Toast.LENGTH_SHORT).show();
			return;
		}
		// 获取保存的验证码
		verifyCode = PrefUtils.getString(this, "verifyCode", "");
		Log.i(TAG, "保存的验证码:" + verifyCode);
		if (iscode
				&& !etPassword.getText().toString().trim().equals(verifyCode)) {
			Log.i(TAG, "输入的验证码:" + etPassword.getText().toString().trim());
			Toast.makeText(this, "验证码输入不正确", Toast.LENGTH_SHORT).show();
			return;
		}

		// 1，iscode验证码登录 ; 2，!iscode，密码登录，调用登录的接口。

		Map<String, String> params = new HashMap<String, String>();
		params.put("mobile", etUserName.getText().toString().trim());
		if (iscode) {
			params.put("verifyCode", etPassword.getText().toString().trim());

		} else {
			params.put("password",
					MD5Utils.encode(etPassword.getText().toString().trim()));

		}

		HttpLoader.post(GlobalWiwikey.URL_LOGIN, params,
				LoginPasswordBean.class, GlobalWiwikey.REQUEST_LOGIN, this);

	}

	/*
	 * 取消倒计时
	 */
	@Override
	protected void onPause() {
		super.onPause();

		if (timer != null) {

			timer.cancel();
			tvHint.setVisibility(View.GONE);
			tvGetCode.setVisibility(View.VISIBLE);
			timePast = 0;
		}
	}

	@Override
	public void onGetResponseSuccess(int requestCode, RBResponse response) {
		switch (requestCode) {
		case GlobalWiwikey.REQUEST_CODE:
			GetVerifyCodeBean mCode = (GetVerifyCodeBean) response;
			verifyCode = mCode.getVerifyCode();
			Log.i(TAG, "获取验证码成功:" + verifyCode);
			PrefUtils.setString(this, "verifyCode", verifyCode);

			break;
		case GlobalWiwikey.REQUEST_LOGIN: //

			LoginPasswordBean loginbean = (LoginPasswordBean) response;

			if (!loginbean.getErrmsg().equals("SUCCESS")) {
				Log.i(TAG, "登录失败:" + loginbean.getErrmsg());
				return;
			}

			Log.i(TAG, "获取登录数据成功:" + loginbean.getMemberModel().getIsSetPass()
					+ "token:" + loginbean.getToken());

			PrefUtils.setInt(this, "memberId", loginbean.getMemberModel()
					.getMemberId());
			PrefUtils.setString(this, "mobile", loginbean.getMemberModel()
					.getMobile());
			PrefUtils.setString(this, "realname", loginbean.getMemberModel()
					.getRealname());
			PrefUtils.setString(this, "nickname", loginbean.getMemberModel()
					.getNickname());
			PrefUtils.setInt(this, "memberType", loginbean.getMemberModel()
					.getMemberType());
			PrefUtils.setInt(this, "householdType0", loginbean.getMemberModel()
					.getHouseholdType());
			PrefUtils.setInt(this, "sex", loginbean.getMemberModel().getSex());
			PrefUtils.setString(this, "signature", loginbean.getMemberModel()
					.getSignature());
			try {
				PrefUtils.setString(this, "agoraVid", DesUtil.encrypt(
						loginbean.getAgoraVid(), DesUtil.DESKEY));
			} catch (Exception e1) {
				e1.printStackTrace();
			}

			PrefUtils.setBoolean(this, "isLogin", true);
			if (!iscode) {
				PrefUtils
						.setString(
								this,
								"Password",
								MD5Utils.encode(etPassword.getText().toString()
										.trim()));
			}
			try {
				PrefUtils.setString(this, "token",
						DesUtil.encrypt(loginbean.getToken(), DesUtil.DESKEY));
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (loginbean.getMemberModel().getIsSetPass() == 0) {
				// 没有设置密码，进入设置密码界面
				Intent intent = new Intent(this, SetPasswordActivity.class);
				startActivity(intent);
				finish();

			} else if (loginbean.getMemberModel().getIsModifyPass() == 0) {
				// 没有修改密码，传个参数进入主界面，告诉主界面我没有设置密码。
				Intent intent = new Intent(this, HomeAcivity.class);
				startActivity(intent);
				finish();

			} else {
				// 直接进入主页面
				Intent intent = new Intent(this, HomeAcivity.class);
				startActivity(intent);
				finish();
			}

			break;

		default:
			break;
		}

	}

	private class HomeKeyEventBroadCastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			System.exit(0);
		}

	}

	// 1，服务器没响应，2，服务器返回数据成功，解析失败也会走此方法
	@Override
	public void onGetResponseError(int requestCode, VolleyError error) {

		Log.i(TAG, "服务器请求失败:");
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			System.exit(0);
			finish();
			return true;
		} else
			return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(receiver);
	}
}
