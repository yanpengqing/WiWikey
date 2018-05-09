package com.wiwikeyandroid.activity;

import java.util.Timer;
import java.util.TimerTask;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.wiwikeyandroid.R;
import com.wiwikeyandroid.utils.Tools;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class ForgetPasswordActivity extends Activity {

	private Button btnBack;
	private Button btnCommit;
	private EditText etPhone;
	private EditText etPwd1;
	private EditText etPwd2;
	private EditText etCode;
	private TextView tvGetCode;
	private Context mContext;
	private int timePast;
	private TextView tvHint;
	private Timer timer;

	// 定义Handler
	final Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			// handler处理消息
			if (msg.what >= 1) {
				tvHint.setText(msg.what + "s");
			} else {
				// 在handler里可以更改UI组件
				tvHint.setVisibility(View.GONE);
				tvGetCode.setVisibility(View.VISIBLE);
				timer.cancel();
			}
		}
	};

	private OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			
			switch (v.getId()) {
			case R.id.forget_password_back:
				finish();
				break;
			case R.id.forget_password_commit:

				if (TextUtils.isEmpty(etPhone.getText().toString().trim())) {
					Toast.makeText(mContext, "请输入手机号码！", Toast.LENGTH_SHORT)
							.show();
					return;
				}
				if (!Tools.isPhoneNum(etPhone.getText().toString().trim())) {
					Toast.makeText(mContext, "非法手机号！", Toast.LENGTH_SHORT)
							.show();
					return;
				}
				if (TextUtils.isEmpty(etCode.getText().toString().trim())) {
					Toast.makeText(mContext, "请输入验证码！", Toast.LENGTH_SHORT)
							.show();
					return;
				}

				String newpwd = etPwd1.getText().toString();
				String newpwd1 = etPwd2.getText().toString();

				if (newpwd.equals("") || (newpwd == null)) {
					Toast.makeText(mContext, "请输入新密码！", Toast.LENGTH_SHORT)
							.show();
					return;
				}
				if (newpwd.length() < 6 || newpwd.length() > 12) {
					Toast.makeText(mContext, "密码长度不匹配！", Toast.LENGTH_SHORT)
							.show();
				}
				if (newpwd1.equals("") || (newpwd1 == null)) {
					Toast.makeText(mContext, "请输入确认密码！", Toast.LENGTH_SHORT)
							.show();
					return;
				}
				if (newpwd1.length() < 6 || newpwd1.length() > 12) {
					Toast.makeText(mContext, "密码长度不匹配！", Toast.LENGTH_SHORT)
							.show();
				}
				if (!newpwd.equals(newpwd1)) {
					Toast.makeText(mContext, "两个密码不一致！", Toast.LENGTH_SHORT)
							.show();
					return;
				}
				//调用设置密码登录接口
//
//				getNetData.forgetPwd(etPhone.getText().toString().trim(),
//						newpwd, etCode.getText().toString().trim());

				break;
			case R.id.forget_password_code_getcode:
				/*if (etPhone.getText().toString().length() != 11) {
					return;
				}*/

				if (!Tools.isPhoneNum(etPhone.getText().toString().trim())) {
					Toast.makeText(mContext, "非法手机号！", Toast.LENGTH_SHORT)
							.show();
					return;
				}

				if (timePast > 0) {
					Toast.makeText(mContext, "60秒内不能重新获取验证码！",
							Toast.LENGTH_LONG).show();
					return;
				}
				//调用验证码接口

//				getNetData.getVerifyCode(etPhone.getText().toString().trim());

				break;
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.forget_password_activity);

		mContext = ForgetPasswordActivity.this;

		timePast = 0;

		initView();

		etPhone.setText(this.getIntent().getStringExtra("data"));
	}

	private void initView() {
		btnBack = (Button) this.findViewById(R.id.forget_password_back);
		btnCommit = (Button) this.findViewById(R.id.forget_password_commit);
		btnBack.setOnClickListener(onClickListener);
		btnCommit.setOnClickListener(onClickListener);
		etPhone = (EditText) this.findViewById(R.id.forget_password_phone_info);
		etPwd1 = (EditText) this.findViewById(R.id.forget_password_new_info1);
		etPwd2 = (EditText) this.findViewById(R.id.forget_password_new_info2);
		etCode = (EditText) this.findViewById(R.id.forget_password_code_info);
		tvHint = (TextView) findViewById(R.id.forget_password_code_hint);

		tvGetCode = (TextView) this
				.findViewById(R.id.forget_password_code_getcode);
		etPhone.setOnClickListener(onClickListener);
		tvGetCode.setOnClickListener(onClickListener);
	}


	/* @Override
	public void getdata_complete(Object list, String s) {
		// TODO Auto-generated method stub
		if (null != list && !list.equals("")) {
			handlerWaiting.getdataok(HandlerGetData.GETDATA_OK);
			if (s.equals(BeanName.BeanGetVerifyCode)) {
				tvGetCode.setVisibility(View.GONE);
				tvHint.setVisibility(View.VISIBLE);

				timer = new Timer();
				timePast = 60;
				timer.schedule(new TimerTask() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						Message msg = new Message();
						msg.what = timePast--;
						handler.sendMessage(msg);
					}

				}, 100, 1000);
			} else if (s.equals(BeanName.BeanForgetPassword)) {
				JSONObject jsonObj;
				JSONArray array;
				try {
					jsonObj = new JSONObject(list.toString());
					JSONObject result = jsonObj.getJSONObject("result");
					String state = result.getString("state");
					if (state.equals("1")) {
						finish();
					} else {
						String msg = result.getString("msg");
						Toast.makeText(mContext, msg, Toast.LENGTH_SHORT)
								.show();
					}
				} catch (JSONException e) {

				}
			}
		}
		else {
			Toast.makeText(mContext, "网络异常，请稍后重试！", Toast.LENGTH_SHORT).show();
		}
	}*/

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
}
