package com.wiwikeyandroid.modules.phone;

import java.io.File;

import org.seny.android.utils.ALog;
import org.seny.android.utils.NetworkUtils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wiwikeyandroid.R;

public class TenementNotifyDetailsActivity extends Activity implements
		OnClickListener {

	private WebView webView;
	private Button backButton;
	//private Button shareBack;
	private LinearLayout ll_loading;
	private TextView txtTitle;
	private static final String APP_CACHE_DIRNAME = "/webcache"; // web缓存目录

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.phone_notification_details);
		initView();
		initListener();
		initData();
	}

	private void initListener() {
		backButton.setOnClickListener(this);
		//shareBack.setOnClickListener(this);
	}

	private void initData() {
		initWebView();

		webView.setWebViewClient(new WebViewClient() {
			@Override
			public void onLoadResource(WebView view, String url) {
				super.onLoadResource(view, url);

			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
			}

			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				super.onReceivedError(view, errorCode, description, failingUrl);
			}

		});

		webView.setWebChromeClient(new WebChromeClient() {
			
			@Override
			public void onReceivedTitle(WebView view, String title) {
				super.onReceivedTitle(view, title);
				 txtTitle.setText(title); 
			}
			@Override
			public boolean onJsAlert(WebView view, String url, String message,
					JsResult result) {
				result.confirm();
				return super.onJsAlert(view, url, message, result);
			}

			@Override
			public void onProgressChanged(WebView view, int newProgress) {

				if (newProgress == 100) {
					// 网页加载完成
					ll_loading.setVisibility(View.INVISIBLE);
				} else {
					// 加载中

				}

			}
		});
		webView.loadUrl("http://m.xianghuo.me/");
	}

	private void initWebView() {
		webView.getSettings().setSupportZoom(true);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setRenderPriority(RenderPriority.HIGH);
		if (NetworkUtils.checkNetwork(this)) {
			webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT); // 设置缓存模式
		} else {
			webView.getSettings().setCacheMode(
					WebSettings.LOAD_CACHE_ELSE_NETWORK); // 设置缓存模式
		}

		// 开启DOM storage API 功能
		webView.getSettings().setDomStorageEnabled(true);
		// 开启database storage API功能
		webView.getSettings().setDatabaseEnabled(true);
		String cacheDirPath = getCacheDir().getAbsolutePath()
				+ APP_CACHE_DIRNAME;
		ALog.d("cacheDirPath:" + cacheDirPath);
		// 设置数据库缓存路径
		webView.getSettings().setDatabasePath(cacheDirPath);
		// 设置Application caches缓存目录
		webView.getSettings().setAppCachePath(cacheDirPath);
		webView.getSettings().setDatabaseEnabled(true);
		// 开启Application Cache功能
		webView.getSettings().setAppCacheEnabled(true);

	}

	private void initView() {
		webView = (WebView) findViewById(R.id.webview);
		// 设置WebView属性，能够执行Javascript脚本
		webView.getSettings().setJavaScriptEnabled(true);
		// 设置可以支持缩放
		webView.getSettings().setSupportZoom(true);
		backButton = (Button) findViewById(R.id.phone_notification_details_back);
		//shareBack = (Button) findViewById(R.id.info_detail_share);
		ll_loading = (LinearLayout) findViewById(R.id.ll_loading);
		txtTitle = (TextView) findViewById(R.id.sys_setting_text);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.phone_notification_details_back: // 返回
			finish();
			break;
		case R.id.info_detail_share: // 分享

			break;

		default:
			break;
		}
	}

	public void clearWebViewCache() {
		// 清理WebView缓存数据库
		try {
			deleteDatabase("webview.db");
			deleteDatabase("webviewCache.db");
		} catch (Exception e) {
			e.printStackTrace();
		}
		// WebView缓存文件
		File appCacheDir = new File(getFilesDir().getAbsolutePath()
				+ APP_CACHE_DIRNAME);
		ALog.d("appCacheDir path=" + appCacheDir.getAbsolutePath());

		File webviewCacheDir = new File(getCacheDir().getAbsolutePath()
				+ "/webviewCache");
		// 删除webView缓存目录
		if (webviewCacheDir.exists()) {
			deleteFile(webviewCacheDir);
		}
		// 删除webView缓存，缓存目录
		if (appCacheDir.exists()) {
			deleteFile(appCacheDir);
		}
	}

	public void deleteFile(File file) {
		if (file.exists()) {
			if (file.isFile()) {
				file.delete();
			} else if (file.isDirectory()) {
				File files[] = file.listFiles();
				for (int i = 0; i < files.length; i++) {
					deleteFile(files[i]);
				}
			}
			file.delete();
		} else {
		}
	}

	// 改写返回的逻辑

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
			webView.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
