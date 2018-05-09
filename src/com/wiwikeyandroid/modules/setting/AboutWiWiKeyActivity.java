package com.wiwikeyandroid.modules.setting;

import org.seny.android.utils.ActivityUtils;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import butterknife.InjectView;
import butterknife.OnClick;

import com.wiwikeyandroid.R;
import com.wiwikeyandroid.base.BaseActivity;
import com.wiwikeyandroid.view.AlertDialog;

/**
 * @author Joseph
 * @date 2016-8-5 关于唯家
 */
public class AboutWiWiKeyActivity extends BaseActivity {

	@InjectView(R.id.about_back)
	// 返回
	Button about_back;
	@InjectView(R.id.about_intro)
	// 简介
	RelativeLayout about_intro;
	@InjectView(R.id.about_opinion)
	// 意见反馈
	RelativeLayout about_opinion;
	@InjectView(R.id.about_phone)
	// 电话咨询
	RelativeLayout about_phone;
	@InjectView(R.id.about_grade)
	// 评分
	RelativeLayout about_grade;
	@InjectView(R.id.about_protect)
	// 协议保护
	RelativeLayout about_protect;

	@Override
	protected int initContentView() {
		return R.layout.setting_about_navigation;
	}

	@OnClick({ R.id.about_back, R.id.about_intro,R.id.about_opinion, R.id.about_phone, R.id.about_grade,
			R.id.about_protect })
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.about_back:
			finish();
			break;
		case R.id.about_intro:
			ActivityUtils
			.startActivity(this, AboutIntroActivity.class);
			break;
		case R.id.about_phone: // 客服咨询
			new AlertDialog(this).builder().setTitle("呼叫客服")
					.setMsg("0731-8627198")
					.setPositiveButton("呼叫", new OnClickListener() {
						@Override
						public void onClick(View v) {
							// 启动
							startActivity(new Intent(
									"android.intent.action.CALL", Uri
											.parse("tel:0731-8627198")));

						}
					}).setNegativeButton("取消", new OnClickListener() {
						@Override
						public void onClick(View v) {

						}
					}).show();
			break;
			case R.id.about_protect:
				ActivityUtils
				.startActivity(this, AgreementActivity.class);
				break;

		}
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
