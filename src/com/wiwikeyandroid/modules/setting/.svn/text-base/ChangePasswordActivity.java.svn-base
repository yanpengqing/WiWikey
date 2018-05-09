package com.wiwikeyandroid.modules.setting;

import java.util.HashMap;
import java.util.Map;

import org.seny.android.utils.ActivityUtils;
import org.seny.android.utils.MD5Utils;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.wiwikeyandroid.R;
import com.wiwikeyandroid.activity.HomeAcivity;
import com.wiwikeyandroid.activity.SetPasswordActivity;
import com.wiwikeyandroid.bean.LoginPasswordBean;
import com.wiwikeyandroid.bean.RBResponse;
import com.wiwikeyandroid.net.HttpLoader;
import com.wiwikeyandroid.net.HttpLoader.ResponseListener;
import com.wiwikeyandroid.utils.GlobalWiwikey;
import com.wiwikeyandroid.utils.PrefUtils;

public class ChangePasswordActivity extends Activity implements
		ResponseListener {

	private Button btnCancel;
	private EditText etOldPassword;
	private EditText etNewPassword1;
	private EditText etNewPassword2;
	private Button btnCommit;

	private Context mContext;

	public OnClickListener buttonClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.update_password_back:
				finish();
				break;
			case R.id.update_password_commit:
				savePassword();
				break;
			}
		}
	};
	private String newpwd2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.update_password_activity);

		mContext = ChangePasswordActivity.this;

		initView();
	}

	private void initView() {
		btnCancel = (Button) this.findViewById(R.id.update_password_back);
		btnCancel.setOnClickListener(buttonClick);

		etOldPassword = (EditText) this
				.findViewById(R.id.update_password_old_text);
		etNewPassword1 = (EditText) this
				.findViewById(R.id.update_password_new_text1);
		etNewPassword2 = (EditText) this
				.findViewById(R.id.update_password_new_text2);
		btnCommit = (Button) this.findViewById(R.id.update_password_commit);
		btnCommit.setOnClickListener(buttonClick);
	}

	private void savePassword() {

		String oldpwd = etOldPassword.getText().toString();
		String newpwd1 = etNewPassword1.getText().toString();
		newpwd2 = etNewPassword2.getText().toString();

		if (oldpwd.equals("") || (newpwd1 == null)) {
			Toast.makeText(mContext, "请填入原密码！", Toast.LENGTH_SHORT).show();
			return;
		}

		if (newpwd1.equals("") || (newpwd1 == null)) {
			Toast.makeText(mContext, "请填入新密码！", Toast.LENGTH_SHORT).show();
			return;
		}
		if (newpwd1.length() < 6 || newpwd1.length() > 12) {
			Toast.makeText(mContext, "密码长度不对！", Toast.LENGTH_SHORT).show();
			return;
		}
		if (!newpwd1.equals(newpwd2)) {
			Toast.makeText(mContext, "密码不一致！", Toast.LENGTH_SHORT).show();
			return;
		}
		if (newpwd2.length() < 6 || newpwd2.length() > 12) {
			Toast.makeText(mContext, "密码长度不对！", Toast.LENGTH_SHORT).show();
			return;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("oldPassword", MD5Utils.encode(oldpwd));
		params.put("newPassword", MD5Utils.encode(newpwd2));
		params.put("memberId", PrefUtils.getInt(mContext, "memberId", -1) + "");
		params.put("token", PrefUtils.getString(mContext, "token", ""));
		// 确定，调用接口
		HttpLoader.post(GlobalWiwikey.URL_CHANGEPASSWORD, params,
				LoginPasswordBean.class, GlobalWiwikey.REQUEST_CHANGEPASSWORD,
				this);
	}

	@Override
	public void onGetResponseSuccess(int requestCode, RBResponse response) {
		switch (requestCode) {
		case GlobalWiwikey.REQUEST_CHANGEPASSWORD:
			LoginPasswordBean bean = (LoginPasswordBean) response;
			if (bean.getErrmsg().equals("SUCCESS")) { // 修改密码成功
				Toast.makeText(this, "密码修改成功", Toast.LENGTH_SHORT).show();
				PrefUtils.setString(this, "Password", MD5Utils.encode(newpwd2));
				finish();

			} else {// 修改密码失败
				Toast.makeText(this, "密码修改失败", Toast.LENGTH_SHORT).show();
			}
			break;
		}
	}

	@Override
	public void onGetResponseError(int requestCode, VolleyError error) {

	}

}
