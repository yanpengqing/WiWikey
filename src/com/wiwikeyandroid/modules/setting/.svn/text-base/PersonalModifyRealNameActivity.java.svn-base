package com.wiwikeyandroid.modules.setting;

import java.util.HashMap;
import java.util.Map;

import org.seny.android.utils.ALog;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import butterknife.InjectView;
import butterknife.OnClick;

import com.android.volley.VolleyError;
import com.wiwikeyandroid.R;
import com.wiwikeyandroid.agora.MsgEvent;
import com.wiwikeyandroid.base.BaseActivity;
import com.wiwikeyandroid.bean.GetVerifyCodeBean;
import com.wiwikeyandroid.bean.RBResponse;
import com.wiwikeyandroid.net.HttpLoader;
import com.wiwikeyandroid.net.HttpLoader.ResponseListener;
import com.wiwikeyandroid.utils.GlobalWiwikey;
import com.wiwikeyandroid.utils.PrefUtils;

import de.greenrobot.event.EventBus;

/**
 * @author Joseph
 * @date 2016-8-16 修改姓名
 */
public class PersonalModifyRealNameActivity extends BaseActivity implements
		TextWatcher, ResponseListener {

	@InjectView(R.id.set_signa)
	EditText EditText;
	@InjectView(R.id.phone_notification_details_back)
	Button mBack;
	@InjectView(R.id.info_signa_finish)
	Button finish;

	@Override
	protected int initContentView() {
		return R.layout.persona_realname_actiity;
	}

	@OnClick({ R.id.phone_notification_details_back, R.id.info_signa_finish })
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.phone_notification_details_back:
			finish();
			break;
		case R.id.info_signa_finish:
			
			if (PrefUtils.getString(this, "realname", "").equals(EditText.getText().toString().trim())) {
				finish();
				return;
			}
			Map<String, String> params = new HashMap<String, String>();
			params.put("realName", EditText.getText().toString().trim());
			params.put("memberId", PrefUtils.getInt(this, "memberId", -1) + "");
			params.put("token", PrefUtils.getString(this, "token", ""));
			// 确定，调用接口
			HttpLoader.post(GlobalWiwikey.URL_UPDATEMEMBERINFO, params,
					GetVerifyCodeBean.class,
					GlobalWiwikey.REQUEST_UPDATEMEMBERINFO, this);
			break;

		default:
			break;
		}
	}

	@Override
	protected void initView() {
		EditText.setText(PrefUtils.getString(this, "realname", ""));
	}

	@Override
	protected void initData() {
		
	}

	@Override
	protected void initListener() {
		EditText.addTextChangedListener(this);
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {

	}

	@Override
	public void afterTextChanged(Editable s) {
		if (EditText.getText().toString().trim().length() > 0) {
			finish.setEnabled(true);
		} else {
			finish.setEnabled(false);
		}
	}

	@Override
	public void onGetResponseSuccess(int requestCode, RBResponse response) {
		switch (requestCode) {
		case GlobalWiwikey.REQUEST_UPDATEMEMBERINFO:
			GetVerifyCodeBean bean = (GetVerifyCodeBean) response;
			if (bean.getErrno()==0) {//成功
				PrefUtils.setString(this, "realname",EditText.getText().toString());
				EventBus.getDefault().post(new MsgEvent("signature"));
				finish();
			}
			break;

		default:
			break;
		}
	}

	@Override
	public void onGetResponseError(int requestCode, VolleyError error) {

	}
}
