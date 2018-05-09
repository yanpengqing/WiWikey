package com.wiwikeyandroid.modules.setting;

import android.annotation.SuppressLint;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.widget.Button;
import butterknife.InjectView;

import com.wiwikeyandroid.R;
import com.wiwikeyandroid.base.BaseActivity;

/**
 * @author Joseph
 * @date 2016-8-17 使用协议
 */
public class AgreementActivity extends BaseActivity {
	@InjectView(R.id.word_view)
	WebView word_view;

	@InjectView(R.id.self_info_back)
	Button mBack;

	@Override
	protected int initContentView() {
		return R.layout.agreement_view_word;
	}

	@Override
	protected void initView() {

	}

	@Override
	protected void initListener() {
		mBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	@Override
	protected void initData() {

		WebSettings view = word_view.getSettings();
		view.setBuiltInZoomControls(true);
		view.setSupportZoom(true);
		// view.setUseWideViewPort(true);
		 view.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		 view.setLoadWithOverviewMode(true);
		word_view.loadUrl("file:///android_asset/agreement.html");
	}

}
