package com.wiwikeyandroid.modules.setting;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.InjectView;

import com.wiwikeyandroid.R;
import com.wiwikeyandroid.base.BaseActivity;

/**
 * @author Joseph
 * @date 2016-8-6 常见问题详情页
 */
public class AboutQuestionDetailsActivity extends BaseActivity {
	@InjectView(R.id.about_how_back)
	Button mBack; 
	@InjectView(R.id.q_title)
	TextView q_title;
	@InjectView(R.id.q_text)
	TextView q_text;
	@InjectView(R.id.q_text_first)
	TextView q_text_first;
	@InjectView(R.id.q_text_second)
	TextView q_text_second;
	@InjectView(R.id.ll_text_second)
	LinearLayout ll_text_second;
	@InjectView(R.id.q_line)
	View q_line;
	@InjectView(R.id.q_img)
	ImageView q_img;
	@InjectView(R.id.q_img_second)
	ImageView q_img_second;

	@Override
	protected int initContentView() {
		return R.layout.about_question_details_activity;
	}

	@Override
	protected void initView() {
		String data = getIntent().getStringExtra("data");
		int title=0;
		int text=0;
		int drawable=0;
		switch (data) {
		case "login":
			q_img_second.setVisibility(View.VISIBLE);
			ll_text_second.setVisibility(View.VISIBLE);
			return;
		case "cut":
			title = R.string.q_title_cut;
			text = R.string.q_text_cut;
			drawable = R.drawable.q_cut_2;
			break;
		case "sign":
			title = R.string.q_title_sign;
			text = R.string.q_text_sign;
			drawable = R.drawable.q_sign_3;
			break;
		case "video":
			title = R.string.q_title_video;
			text = R.string.q_text_video;
			drawable = R.drawable.q_video_4;
			q_img_second.setVisibility(View.VISIBLE);
			ll_text_second.setVisibility(View.VISIBLE);
			q_line.setVisibility(View.GONE);
			q_text_second.setVisibility(View.GONE);
			q_text_first.setText(getResources().getText(R.string.q_text_voice));
			q_img_second.setImageResource(R.drawable.q_vioce_4);
			break;
		case "get":
			title = R.string.q_title_get;
			text = R.string.q_text_get;
			drawable = R.drawable.q_get_5;
			break;
		case "add":
			title = R.string.q_title_add;
			text = R.string.q_text_add;
			drawable = R.drawable.q_add_6;
			break;
		case "order":
			title = R.string.q_title_order;
			text = R.string.q_text_order;
			drawable = R.drawable.q_order_7;
			q_img_second.setVisibility(View.VISIBLE);
			q_img_second.setImageResource(R.drawable.q_order_7_2);
			break;
		case "message":
			title = R.string.q_title_message;
			text = R.string.q_text_message;
			drawable = R.drawable.q_information_8;
			break;
		}
		q_title.setText(getResources().getText(title));
		q_text.setText(getResources().getText(text));
		q_img.setImageResource(drawable);
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
