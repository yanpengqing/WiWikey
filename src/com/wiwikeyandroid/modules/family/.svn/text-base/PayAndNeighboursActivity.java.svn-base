package com.wiwikeyandroid.modules.family;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.InjectView;

import com.wiwikeyandroid.R;
import com.wiwikeyandroid.base.BaseActivity;

/**
 * @author Joseph
 * @date 2016-7-31 邻里和生活缴费
 */
public class PayAndNeighboursActivity extends BaseActivity {

	@InjectView(R.id.pay_neighbours_back)
	Button mBack;
	@InjectView(R.id.sys_title_text)
	TextView title_text;
	@InjectView(R.id.iv_describe)
	ImageView iv_describe;

	@Override
	protected int initContentView() {

		return R.layout.pay_or_neighbours_activity;
	}

	@Override
	protected void initView() {
		String type = getIntent().getStringExtra("data");
		iv_describe.setImageResource(type.equals("pay")?R.drawable.pay_null:R.drawable.neighbours_null);
		title_text.setText(type.equals("pay")?"生活缴费":"邻里圈");
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

	}

}
