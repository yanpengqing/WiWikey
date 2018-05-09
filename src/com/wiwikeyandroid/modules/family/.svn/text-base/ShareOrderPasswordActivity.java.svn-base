package com.wiwikeyandroid.modules.family;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.InjectView;
import butterknife.OnClick;

import com.wiwikeyandroid.R;
import com.wiwikeyandroid.base.BaseActivity;
import com.wiwikeyandroid.utils.PrefUtils;

/**
 * @author Joseph
 * @date 2016-8-1 分享来访预约密码
 */
public class ShareOrderPasswordActivity extends BaseActivity {

	@InjectView(R.id.password_share_back)
	Button mBack;
	@InjectView(R.id.password_share_plotname)
	TextView mPlotname;
	@InjectView(R.id.password_share_send)
	Button mShare;
	@InjectView(R.id.imgone)
	ImageView imgOne;
	@InjectView(R.id.imgtwo)
	ImageView imgTwo;
	@InjectView(R.id.imgthree)
	ImageView imgThree;
	@InjectView(R.id.imgfour)
	ImageView imgFour;
	@InjectView(R.id.imgfive)
	ImageView imgFive;
	@InjectView(R.id.imgsix)
	ImageView imgSix;
	
	@InjectView(R.id.password_share_period)
	TextView mIndate;

	private String startTime;
	private String mIndateTime;
	private String visitPhone;
	private String passWord;
	@Override
	protected int initContentView() {
		return R.layout.family_share_password_activity;
	}

	@OnClick({ R.id.password_share_back, R.id.password_share_send })
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.password_share_back:
			finish();

			break;
		case R.id.password_share_send:
			// 短信形式发送给来访预约者
			Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"+visitPhone));
			intent.putExtra("sms_body", "嗨，朋友。我已用唯家小秘书为你预约了门口机开门密码："+passWord+", 有效时间为:"+mIndateTime+",(一个密码只能开门一次，为了安全不要告诉陌生人哦)，了解唯家小秘书 www.wiwikey.com");
			startActivity(intent);
			break;
		}
	}

	@Override
	protected void initView() {
		mPlotname.setText(PrefUtils.getString(this, "memberHouseList0", "")
				.split("#")[0]);
		Bundle data = getIntent().getExtras();
		startTime = data.getString("startTime");
		visitPhone = data.getString("visitPhone");
		passWord = data.getString("passWord"); 

	}

	@Override
	protected void initListener() {

	}

	@Override
	protected void initData() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date date = null;
		try {
			date = sdf.parse(startTime);
		} catch (ParseException e) {
			e.printStackTrace(); 
		}
		Calendar Cal = Calendar.getInstance();
		Cal.setTime(date);
		Cal.add(Calendar.HOUR_OF_DAY, 2);
		Date dt2 = Cal.getTime();
		String endTime = sdf.format(dt2);
		mIndateTime = startTime+"-"+endTime.substring(11); 
		mIndate.setText("有效时间："+mIndateTime);
		imgOne.setImageResource(toImage(passWord.charAt(0)));
		imgTwo.setImageResource(toImage(passWord.charAt(1)));
		imgThree.setImageResource(toImage(passWord.charAt(2)));
		imgFour.setImageResource(toImage(passWord.charAt(3)));
		imgFive.setImageResource(toImage(passWord.charAt(4)));
		imgSix.setImageResource(toImage(passWord.charAt(5)));
		
	}
	

	public int toImage(char a) {
		int x = 0;
		switch (a) {
		case '0':
			x = R.drawable.ww_key_02x;
			break;
		case '1':
			x = R.drawable.ww_key_12x;
			break;
		case '2':
			x = R.drawable.ww_key_22x;
			break;
		case '3':
			x = R.drawable.ww_key_32x;
			break;
		case '4':
			x = R.drawable.ww_key_42x;
			break;
		case '5':
			x = R.drawable.ww_key_52x;
			break;
		case '6':
			x = R.drawable.ww_key_62x;
			break;
		case '7':
			x = R.drawable.ww_key_72x;
			break;
		case '8':
			x = R.drawable.ww_key_82x;
			break;
		case '9':
			x = R.drawable.ww_key_92x;
			break;
		}
		return x;
	}
}
