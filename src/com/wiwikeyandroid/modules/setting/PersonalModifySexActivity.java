package com.wiwikeyandroid.modules.setting;

import java.util.HashMap;
import java.util.Map;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
 * @date 2016-8-16 修改性别
 */
public class PersonalModifySexActivity extends BaseActivity implements
		ResponseListener {

	@InjectView(R.id.tv_woman)
	RelativeLayout sexWoman;
	@InjectView(R.id.tv_man)
	RelativeLayout sexMan;
	@InjectView(R.id.img_woman)
	ImageView img_woman;
	@InjectView(R.id.img_man)
	ImageView img_man;

	@Override
	protected int initContentView() {

		return R.layout.persona_sex_actiity;
	}

	@Override
	protected void initView() {
		int sex = PrefUtils.getInt(this, "sex", 1);
		if (sex == 1) {
			img_man.setVisibility(View.VISIBLE);
		} else if (sex == 0) {
			img_woman.setVisibility(View.VISIBLE);
		} else {
			img_man.setVisibility(View.VISIBLE);
		}
	}

	String sex = "1";

	@OnClick({ R.id.tv_woman, R.id.tv_man })
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.tv_woman:
			sex = "0";
			img_man.setVisibility(View.INVISIBLE);
			img_woman.setVisibility(View.VISIBLE);
			break;

		case R.id.tv_man:
			sex = "1";
			img_man.setVisibility(View.VISIBLE);
			img_woman.setVisibility(View.INVISIBLE);
			break;
		}

		if ((PrefUtils.getInt(this, "sex", 1) + "").equals(sex)) {
			finish();
			return;
		}

		Map<String, String> params = new HashMap<String, String>();
		params.put("sex", sex);
		params.put("memberId", PrefUtils.getInt(this, "memberId", -1) + "");
		params.put("token", PrefUtils.getString(this, "token", ""));
		// 确定，调用接口
		HttpLoader.post(GlobalWiwikey.URL_UPDATEMEMBERINFO, params,
				GetVerifyCodeBean.class,
				GlobalWiwikey.REQUEST_UPDATEMEMBERINFO, this);
	}

	@Override
	protected void initListener() {

	}

	@Override
	protected void initData() {

	}

	@Override
	public void onGetResponseSuccess(int requestCode, RBResponse response) {
		GetVerifyCodeBean bean = (GetVerifyCodeBean) response;
		if (bean.getErrno() == 0) {// 成功
			PrefUtils.setInt(this, "sex", Integer.parseInt(sex));
			EventBus.getDefault().post(new MsgEvent("signature"));
			finish();
		}
	}

	@Override
	public void onGetResponseError(int requestCode, VolleyError error) {

	}
}
