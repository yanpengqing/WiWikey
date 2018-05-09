package com.wiwikeyandroid.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.seny.android.utils.ActivityUtils;
import org.seny.android.utils.MD5Utils;

import com.android.volley.VolleyError;
import com.wiwikeyandroid.R;
import com.wiwikeyandroid.bean.LoginPasswordBean;
import com.wiwikeyandroid.bean.RBResponse;
import com.wiwikeyandroid.net.HttpLoader;
import com.wiwikeyandroid.net.HttpLoader.ResponseListener;
import com.wiwikeyandroid.utils.GlobalWiwikey;
import com.wiwikeyandroid.utils.PrefUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SetPasswordActivity extends Activity implements ResponseListener {

	private Button btnBack;
	private Button btnCommit;
	private EditText etPwd1;
	private EditText etPwd2;
	private EditText nickname;
	private Context mContext;

	private OnClickListener buttonClick = new OnClickListener() {

		@Override
		public void onClick(View v) {

			switch (v.getId()) {
			case R.id.empty_password_back: // 取消设置密码,直接进入主界面
				Intent intent = new Intent(mContext, HomeAcivity.class);
				startActivity(intent);
				finish();
				break;
			case R.id.empty_password_commit:// 设置密码

				String newpwd = etPwd1.getText().toString().trim();
				String newpwd1 = etPwd2.getText().toString().trim();
				String nick = nickname.getText().toString().trim();

				if (newpwd.equals("") || (newpwd == null)) {
					Toast.makeText(mContext, "请输入初始密码！", Toast.LENGTH_SHORT)
							.show();
					return;
				}
				if (newpwd.length() < 6 || newpwd.length() > 12) {

					Toast.makeText(mContext, "密码长度有误 ！", Toast.LENGTH_SHORT)
							.show();
					return;
				}
				if (newpwd1.equals("") || (newpwd1 == null)) {
					Toast.makeText(mContext, "请输入确认密码！", Toast.LENGTH_SHORT)
							.show();
					return;
				}
				if (newpwd1.length() < 6 || newpwd1.length() > 12) {

					Toast.makeText(mContext, "确认密码长度有误 ！", Toast.LENGTH_SHORT)
							.show();
					return;
				}
				if (!newpwd.equals(newpwd1)) {
					Toast.makeText(mContext, "两个密码不一致！", Toast.LENGTH_SHORT)
							.show();
					return;
				}
				if (nick.equals("") || (nick == null)) {
					Toast.makeText(mContext, "请设置昵称！", Toast.LENGTH_SHORT)
							.show();
					return;
				}

				Map<String, String> params = new HashMap<String, String>();
				params.put("password", MD5Utils.encode(newpwd1));
				params.put("nickname", nick);
				params.put("memberId",
						PrefUtils.getInt(mContext, "memberId", -1) + "");
				params.put("token", PrefUtils.getString(mContext, "token", ""));
				// 确定，调用接口
				HttpLoader.post(GlobalWiwikey.URL_INITPASSWORD, params,
						LoginPasswordBean.class,
						GlobalWiwikey.REQUEST_INITPASSWORD,
						SetPasswordActivity.this);
				break;
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_password_activity);

		mContext = SetPasswordActivity.this;

		// flag = this.getIntent().getStringExtra("flag");
		// phone = this.getIntent().getStringExtra("phone");

		initView();

	}

	private void initView() {
		btnBack = (Button) this.findViewById(R.id.empty_password_back);
		btnCommit = (Button) this.findViewById(R.id.empty_password_commit);

		btnBack.setOnClickListener(buttonClick);
		btnCommit.setOnClickListener(buttonClick);

		etPwd1 = (EditText) this.findViewById(R.id.empty_password_new_info1);
		etPwd2 = (EditText) this.findViewById(R.id.empty_password_new_info2);
		nickname = (EditText) this.findViewById(R.id.empty_nickname);

	}

	@Override
	public void onGetResponseSuccess(int requestCode, RBResponse response) {
		switch (requestCode) {
		case GlobalWiwikey.REQUEST_INITPASSWORD:
			LoginPasswordBean bean = (LoginPasswordBean) response;
			if (bean.getErrmsg().equals("SUCCESS")) { // 设置密码成功，跳主界面
				PrefUtils.setBoolean(this, "IsSetPass", true);
				Toast.makeText(this, "密码设置成功", Toast.LENGTH_SHORT).show();
				ActivityUtils.startActivityAndFinish(this, HomeAcivity.class);
			
			} else {// 设置密码失败

			}

			break;
		}
	}

	@Override
	public void onGetResponseError(int requestCode, VolleyError error) {

	}
}
