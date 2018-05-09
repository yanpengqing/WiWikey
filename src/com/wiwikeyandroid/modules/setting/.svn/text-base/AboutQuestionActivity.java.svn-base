package com.wiwikeyandroid.modules.setting;

import org.seny.android.utils.ActivityUtils;

import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import butterknife.InjectView;
import butterknife.OnClick;

import com.wiwikeyandroid.R;
import com.wiwikeyandroid.base.BaseActivity;

/**
 * @author Joseph
 * @date 2016-8-6 
 * 常见问题列表
 */
public class AboutQuestionActivity extends BaseActivity {

	@InjectView(R.id.about_how_back)
	Button selfInfoBack;
	@InjectView(R.id.about_how_login)
	RelativeLayout aboutHowLogin;
	@InjectView(R.id.about_how_cut)
	RelativeLayout aboutHowCut;
	@InjectView(R.id.about_how_sign)
	RelativeLayout aboutHowSign;
	@InjectView(R.id.about_how_video)
	RelativeLayout aboutHowVideo;
	@InjectView(R.id.about_how_get)
	RelativeLayout aboutHowGet;
	@InjectView(R.id.about_how_add)
	RelativeLayout aboutHowAdd;
	@InjectView(R.id.about_how_order)
	RelativeLayout aboutHowOrder;
	@InjectView(R.id.about_how_message)
	RelativeLayout aboutHowMessage;

	@Override
	protected int initContentView() {
		
		return R.layout.about_question_activity;
	}

	@OnClick({ R.id.about_how_back, R.id.about_how_login, R.id.about_how_cut,
			R.id.about_how_sign, R.id.about_how_video, R.id.about_how_get,
			R.id.about_how_add, R.id.about_how_order,
			R.id.about_how_message })
	public void onClick(View view) {
		String  data ="";
		switch (view.getId()) {
		case R.id.about_how_back:
			finish();
			return;
		case R.id.about_how_login:
			data = "login";
			break;
		case R.id.about_how_cut:
			data = "cut";
			break;
		case R.id.about_how_sign:
			data = "sign";
			break;
		case R.id.about_how_video:
			data = "video";
			break;
		case R.id.about_how_get:
			data = "get";
			break;
		case R.id.about_how_add:
			data = "add";
			break;
		case R.id.about_how_order:
			data = "order";
			break;
		case R.id.about_how_message:
			data = "message";
			break;
		}
		ActivityUtils
		.startActivityForData(this, AboutQuestionDetailsActivity.class,data);
	}

	@Override
	protected void initView() {

	}

	@Override
	protected void initListener() {

	}

	@Override
	protected void initData() {

	}

}
