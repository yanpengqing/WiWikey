package com.wiwikeyandroid.modules.phone;


import org.seny.android.utils.ALog;
import org.seny.android.utils.DateUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.wiwikeyandroid.R;
import com.wiwikeyandroid.bean.PropNoticeListBean.NoticeListBean;
import com.wiwikeyandroid.utils.PrefUtils;

public class NewsOrTenementNotifyDetailsActivity extends Activity {
	
	NoticeListBean notify;
	private TextView title_text;  //toptitle
	private Button btBack;        //返回
	private TextView notify_title;//公告标题
	private TextView tv_date;	//日期
	private TextView tv_plotName;//小区名字
	private WebView tv_content; //内容
      @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    	setContentView(R.layout.news_or_notify_detail_activiy);
    	Intent intent = this.getIntent(); 
    	notify=(NoticeListBean) intent.getSerializableExtra("notice");
    	String news = intent.getStringExtra("news");
    	
    	initView();
		initData();
    }

  	private void initView() {
  		title_text = (TextView) findViewById(R.id.sys_title_text);
  		title_text.setText(getIntent().getStringExtra("news").isEmpty()?"公告详情":"新闻详情");
  		btBack = (Button) findViewById(R.id.phone_notification_details_back);
  		btBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
  		notify_title = (TextView) findViewById(R.id.notify_title);
  		tv_date = (TextView) findViewById(R.id.tv_date);
  		tv_plotName = (TextView) findViewById(R.id.tv_plotName);
  		tv_content = (WebView) findViewById(R.id.tv_content); 
  	}
	private void initData() {
		notify_title.setText(notify.getTitle());
		ALog.e(notify.getStartDate()+"");
		tv_date.setText(DateUtils.formatDate10(notify.getStartDate()));
		tv_content.getSettings().setTextSize(WebSettings.TextSize.NORMAL);
		tv_content.getSettings().setDefaultTextEncodingName("UTF-8");
		tv_content.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		notify.setContent(notify.getContent().replaceAll("<img ", "<img width='100%' "));
  		tv_content.loadData(notify.getContent(), "text/html; charset=UTF-8", null);
		//tv_content.setText("\t\t\t"+Html.fromHtml(notify.getContent()));
		tv_plotName.setText(PrefUtils.getString(this, "memberHouseList0", "").split("#")[0]);
}
}