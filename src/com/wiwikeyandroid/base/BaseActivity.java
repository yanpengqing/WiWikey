package com.wiwikeyandroid.base;

import org.seny.android.utils.ALog;
import org.seny.android.utils.MyToast;
import org.seny.android.utils.NetworkUtils;

import android.R;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Window;
import android.view.WindowManager;
import butterknife.ButterKnife;


/**
 * @author Joseph丶 on 2016/5/22.
 */

public abstract class BaseActivity extends FragmentActivity implements
		BaseFragment.BackHandledInterface {

	private BaseFragment mBackHandedFragment;

	@Override
	public void setSelectedFragment(BaseFragment selectedFragment) {
		this.mBackHandedFragment = selectedFragment;
	}

	@Override
	public void onBackPressed() {
		if (mBackHandedFragment == null || !mBackHandedFragment.onBackPressed()) {
			if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
				super.onBackPressed();
			} else {
				getSupportFragmentManager().popBackStack();
			}
		}

	}

	/**
	 * 进度条
	 */

	protected ProgressDialog progressDialog;
	protected SharedPreferences SP;
	protected SharedPreferences.Editor EDIT;
	protected FragmentManager FM;

	protected BaseActivity() {
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.setTheme(R.style.Theme_Light_NoTitleBar);  
		super.onCreate(savedInstanceState);

		ALog.w("BaseActivity:onCreate！");

		getWindow().requestFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
		// 删除窗口背景
		getWindow().setBackgroundDrawable(null);
		// 界面中如果有EditText，默认隐藏输入法
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

		// 定义弹出进度
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("请稍候....");
		// 如果弹出进度被手动取消,则关闭当前Activity
		progressDialog
				.setOnCancelListener(new DialogInterface.OnCancelListener() {
					@Override
					public void onCancel(DialogInterface dialog) {
						finish();
					}
				});

		// 初始化通用的SP&EDIT
		// SP = App.application.SP;
		// EDIT = App.application.EDIT;

		// Fragment相关

		FM = getSupportFragmentManager();

		setContentView(initContentView());
		ButterKnife.inject(this);// 初始化ButterKnife
		initView();
		initListener();
		initData();

	}

	protected boolean checkNetworked() {
		if (!NetworkUtils.checkNetwork(this)) {
			MyToast.show(getApplicationContext(), "手机无可用网络！");
			return false;
		}

		return true;
	}

	/**
	 * 初始化contentView
	 * 
	 * @return 返回contentView的layout id
	 */
	protected abstract int initContentView();

	/**
	 * 初始化View，执行findViewById操作
	 */
	protected abstract void initView();

	/**
	 * 初始化监听器
	 */
	protected abstract void initListener();

	/**
	 * 初始化数据
	 */
	protected abstract void initData();

	/**
	 * 弹出一个进度对话框
	 */
	protected void showProgressDialog() {
		if (progressDialog != null && !progressDialog.isShowing()) {
			progressDialog.show();
		}
	}

	/**
	 * 关闭弹出的进度对话框
	 */
	protected void dismissProgressDialog() {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ButterKnife.reset(this);
	}
}