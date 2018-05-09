package com.wiwikeyandroid.modules.setting;

import org.seny.android.utils.AppInfoUtil;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.wiwikeyandroid.R;
import com.wiwikeyandroid.base.BaseActivity;

/** 
 * @author Joseph 
 * @date   2016-8-5
 * 产品简介
 */
public class AboutIntroActivity extends BaseActivity {
	@Override
	protected int initContentView() {
		return R.layout.setting_intro_activity;
	}

	@Override
	protected void initView() {

	}

	@Override
	protected void initListener() {
		findViewById(R.id.about_back).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	@Override
	protected void initData() {
		((TextView)findViewById(R.id.versions)).setText("版本  "+AppInfoUtil.getVersionCode(this));
	}

}
