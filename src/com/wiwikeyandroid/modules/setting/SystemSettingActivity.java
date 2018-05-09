package com.wiwikeyandroid.modules.setting;

import org.seny.android.utils.ActivityUtils;
import org.seny.android.utils.AppInfoUtil;
import org.seny.android.utils.DataCleanManager;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.InjectView;
import butterknife.OnClick;

import com.wiwikeyandroid.R;
import com.wiwikeyandroid.activity.LoginActivity;
import com.wiwikeyandroid.base.BaseActivity;
import com.wiwikeyandroid.utils.PrefUtils;
import com.wiwikeyandroid.view.AlertDialog;
import com.xiaomi.mipush.sdk.MiPushClient;

/**
 * @author Joseph
 * @date 2016-8-1 类说明
 */
public class SystemSettingActivity extends BaseActivity {

	@InjectView(R.id.sys_setting_pasword)
	RelativeLayout changePsw;
	@InjectView(R.id.self_info_back)
	Button mBack;
	@InjectView(R.id.sys_setting_logout_button)
	Button mLogout;
	@InjectView(R.id.newmsg_notify_setting)
	ImageView notify; // 公告
	@InjectView(R.id.new_msg_setting) 
	ImageView newmsg; // 新闻 
	@InjectView(R.id.sys_setting_cache_arrow)
	TextView cacheSize ;
	@InjectView(R.id.sys_setting_cache)
	RelativeLayout cleanCache;
	
	@Override
	protected int initContentView() {
		return R.layout.system_setting_activity;
	}

	@OnClick({ R.id.sys_setting_pasword, R.id.self_info_back,
			R.id.sys_setting_logout_button, R.id.newmsg_notify_setting,
			R.id.new_msg_setting,R.id.sys_setting_cache })
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.newmsg_notify_setting:
			if (PrefUtils.getBoolean(this, "notice", true)) {
				PrefUtils.setBoolean(this, "notice", false);
				notify.setImageResource(R.drawable.model_off);
			}else {
				PrefUtils.setBoolean(this, "notice", true);
				notify.setImageResource(R.drawable.model_on);
			}
			
			break;
		case R.id.new_msg_setting:
			if (PrefUtils.getBoolean(this, "newmsg", true)) {
				PrefUtils.setBoolean(this, "newmsg", false);
				newmsg.setImageResource(R.drawable.model_off);
			}else {
				PrefUtils.setBoolean(this, "newmsg", true);
				newmsg.setImageResource(R.drawable.model_on);
			}

			break;
		case R.id.sys_setting_pasword:
			ActivityUtils.startActivity(this, ChangePasswordActivity.class);

			break;
		case R.id.sys_setting_cache:
			DataCleanManager.clearAllCache(this);
			AppInfoUtil.showProgress(this, "请稍等", "正在清除缓存……" , true, false);
			new Handler().postDelayed(new Runnable(){   

			    public void run() {   
			    	AppInfoUtil.closeProgress();
			    	try {
						cacheSize.setText(DataCleanManager.getTotalCacheSize(SystemSettingActivity.this));
					} catch (Exception e) {
						e.printStackTrace();
					}
			    }   

			 }, 600);  
			
			break;
		case R.id.self_info_back:
			
			finish();
			break;
		case R.id.sys_setting_logout_button:
			new AlertDialog(this).builder().setTitle("退出当前账号").setMsg("退出账号会导致您无法收到语音视频呼，出门踩到狗屎，确定退出？")
					.setPositiveButton("确认退出", new OnClickListener() {
						@Override
						public void onClick(View v) {
							getSharedPreferences("config", Context.MODE_PRIVATE)
									.edit().clear().commit();
							PrefUtils.setBoolean(getApplication(), "isFirst",
									false);
							MiPushClient.unsetAlias(getApplication(),
									PrefUtils.getString(SystemSettingActivity.this, "mobile", ""), null);
							Intent i = new Intent(SystemSettingActivity.this,
									LoginActivity.class);
							i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
									| Intent.FLAG_ACTIVITY_NEW_TASK);
							startActivity(i);
							finish();
						}
					}).setNegativeButton("取消", new OnClickListener() {
						@Override
						public void onClick(View v) {

						}
					}).show();

			break;

		}
	}

	@Override
	protected void initView() {
		notify.setImageResource(PrefUtils.getBoolean(this, "notice", true)?R.drawable.model_on:R.drawable.model_off);
		newmsg.setImageResource(PrefUtils.getBoolean(this, "newmsg", true)?R.drawable.model_on:R.drawable.model_off);
		try {
			cacheSize.setText(DataCleanManager.getTotalCacheSize(this));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void initListener() {

	}

	@Override
	protected void initData() {

	}

}
