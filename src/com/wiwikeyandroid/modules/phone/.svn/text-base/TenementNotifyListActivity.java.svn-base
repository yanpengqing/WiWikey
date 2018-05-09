package com.wiwikeyandroid.modules.phone;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.ButterKnife;

import com.android.volley.VolleyError;
import com.wiwikeyandroid.R;
import com.wiwikeyandroid.activity.HomeAcivity;
import com.wiwikeyandroid.activity.LoginActivity;
import com.wiwikeyandroid.adapter.NotifyAdapter;
import com.wiwikeyandroid.bean.PropNoticeListBean;
import com.wiwikeyandroid.bean.PropNoticeListBean.NoticeListBean;
import com.wiwikeyandroid.bean.RBResponse;
import com.wiwikeyandroid.net.HttpLoader;
import com.wiwikeyandroid.net.HttpLoader.ResponseListener;
import com.wiwikeyandroid.utils.GlobalWiwikey;
import com.wiwikeyandroid.utils.NetworkConnectivityUtils;
import com.wiwikeyandroid.utils.PrefUtils;
import com.wiwikeyandroid.view.AlertDialog;
import com.wiwikeyandroid.view.SeekEditText;
import com.xiaomi.mipush.sdk.MiPushClient;

public class TenementNotifyListActivity extends Activity implements OnRefreshListener, OnClickListener, ResponseListener {
	
	private Button back;
	
	private SwipeRefreshLayout mSwipeRefreshLayout;
	
	private ListView mCircleLv;
    private NotifyAdapter mAdapter;
    private SeekEditText mSearch; //搜索

	private List<NoticeListBean> noticeList; //公告数据
	private TextView top_title;
	private String news;  //小区新闻标识
	private LinearLayout tv_data;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.phone_item_notification);
		news = getIntent().getStringExtra("data"); 
		mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.mRefreshLayout);
		mCircleLv = (ListView) findViewById(R.id.circleLv);	
		back = (Button) findViewById(R.id.phone_notification_back);
		top_title = (TextView) findViewById(R.id.top_title);
		tv_data = (LinearLayout) findViewById(R.id.tv_data);
		tv_data.setVisibility(View.GONE);
		if ("news".equals(news)) {
			top_title.setText("小区新闻");
		}
		mSearch = (SeekEditText) findViewById(R.id.info_notification_search);
		
		mCircleLv.setOnScrollListener(new SwpipeListViewOnScrollListener(
				mSwipeRefreshLayout));
		mSwipeRefreshLayout.setOnRefreshListener(this);
		mSwipeRefreshLayout.setColorSchemeResources(
				android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);
		back.setOnClickListener(this);
		
		if (!PrefUtils.getString(this, "memberHouseList0", "").isEmpty()) {
			getPropNoticeList();
		} 
	
		mCircleLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(TenementNotifyListActivity.this, NewsOrTenementNotifyDetailsActivity.class);
				Bundle bundle = new Bundle(); 
				bundle.putSerializable("notice", noticeList.get(position));
				bundle.putString("news", "news".equals(news)?"news":"");
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
	}

	@Override
	public void onRefresh() {
		if (mAdapter==null) {
			mSwipeRefreshLayout.setRefreshing(false);
			return;
		}
		loadData();
		new Handler().postDelayed(new Runnable(){
			@Override
			public void run() {
				mAdapter.notifyDataSetChanged();
				mSwipeRefreshLayout.setRefreshing(false);
			}
		}, 1500);

	}
	/**
	 *   	获取物业通知信息
	 */

	private void getPropNoticeList() {
		new Thread(new Runnable() {
			public void run() {
				Map<String, String> params = new HashMap<String, String>();
				params.put("plotId", PrefUtils.getString(TenementNotifyListActivity.this, "memberHouseList0", "").split("#")[1]);
				params.put("buildingId", PrefUtils.getString(TenementNotifyListActivity.this, "memberHouseList0", "").split("#")[2]);
				params.put("unitId", PrefUtils.getString(TenementNotifyListActivity.this, "memberHouseList0", "").split("#")[3]);
				params.put("memberId",
						PrefUtils.getInt(TenementNotifyListActivity.this, "memberId", -1) + "");
				params.put("token", PrefUtils.getString(TenementNotifyListActivity.this, "token", ""));
				String url ;
				if ("news".equals(news)) {
					url = GlobalWiwikey.URL_GETPROPNEWSLIST;
				}else {
					url =GlobalWiwikey.URL_GETPROPNOTICELIST;
				}
				HttpLoader.post(url, params,
						PropNoticeListBean.class,
						GlobalWiwikey.REQUEST_GETPROPNOTICELIST,
						TenementNotifyListActivity.this);
			}
		}).start();
	}
	/**
	 * 刷新数据
	 */
	private void loadData() {
		//获取新数据，更新界面。
		//mAdapter.setDatas(2);
		getPropNoticeList();
		
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		ButterKnife.reset(this);

	}

	@Override
	public void onClick(View v) {
			switch (v.getId()) {
			case R.id.phone_notification_back:
				finish();
				break;
			}
	}

	@Override
	public void onGetResponseSuccess(int requestCode, RBResponse response) {
		switch (requestCode) {
		case GlobalWiwikey.REQUEST_GETPROPNOTICELIST:
			PropNoticeListBean bean = (PropNoticeListBean) response;
			if (bean.getErrmsg().equals("token错误")) {
				getSharedPreferences("config", Context.MODE_PRIVATE)
						.edit().clear().commit();
				PrefUtils.setBoolean(getApplication(), "isFirst", false);
				MiPushClient.unsetAlias(this,
						PrefUtils.getString(this, "mobile", ""), null);
				new AlertDialog(this).builder()
						.setMsg("检测到您的账号已在别的设备登录，您已被迫下线，请重新登录")
						.setNegativeButton("确定", new OnClickListener() {
							@Override
							public void onClick(View v) {
								Intent intent = new Intent(TenementNotifyListActivity.this, LoginActivity.class); 
								intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
								startActivity(intent);
							}
						}).setCancelable(false).show();
				return;
			}
			if (bean.getErrmsg().equals("SUCCESS")) {
				noticeList = bean.getNoticeList();  
				mAdapter = new NotifyAdapter(this,noticeList);
				mCircleLv.setAdapter(mAdapter);
				if (noticeList.size()==0) {
					tv_data.setVisibility(View.VISIBLE);
				}
				
			}
			break;

		default:
			break;
		}
	}

	@Override
	public void onGetResponseError(int requestCode, VolleyError error) {
		
		
	}

}
