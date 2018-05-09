package com.wiwikeyandroid.modules.phone;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.seny.android.utils.ALog;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.android.volley.VolleyError;
import com.wiwikeyandroid.R;
import com.wiwikeyandroid.adapter.NotifyAdapter;
import com.wiwikeyandroid.adapter.PlotNewsAdapter;
import com.wiwikeyandroid.bean.PropNoticeListBean;
import com.wiwikeyandroid.bean.RBResponse;
import com.wiwikeyandroid.bean.PropNoticeListBean.NoticeListBean;
import com.wiwikeyandroid.net.HttpLoader;
import com.wiwikeyandroid.net.HttpLoader.ResponseListener;
import com.wiwikeyandroid.utils.GlobalWiwikey;
import com.wiwikeyandroid.utils.PrefUtils;

public class PlotNewsListActivity extends Activity implements ResponseListener {

	private ListView lv_news;
	private PlotNewsAdapter mAdaper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.plot_news_listitem);
		lv_news = (ListView) findViewById(R.id.lv_news);
		if (!PrefUtils.getString(this, "memberHouseList0", "").isEmpty()) {
			getPropNewsList();
		}
	}

	/**
	 * 获取小区新闻
	 */

	private void getPropNewsList() {
		new Thread(new Runnable() {
			public void run() {
				Map<String, String> params = new HashMap<String, String>();
				params.put(
						"plotId",
						PrefUtils.getString(PlotNewsListActivity.this,
								"memberHouseList0", "").split("#")[1]);
				params.put(
						"buildingId",
						PrefUtils.getString(PlotNewsListActivity.this,
								"memberHouseList0", "").split("#")[2]);
				params.put(
						"unitId",
						PrefUtils.getString(PlotNewsListActivity.this,
								"memberHouseList0", "").split("#")[3]);
				params.put(
						"memberId",
						PrefUtils.getInt(PlotNewsListActivity.this, "memberId",
								-1) + "");
				params.put("token", PrefUtils.getString(
						PlotNewsListActivity.this, "token", ""));
				HttpLoader.post(GlobalWiwikey.URL_GETPROPNEWSLIST, params,
						PropNoticeListBean.class,
						GlobalWiwikey.REQUEST_GETPROPNEWSLIST,
						PlotNewsListActivity.this);
			}
		}).start();
	}

	@Override
	public void onGetResponseSuccess(int requestCode, RBResponse response) {
		switch (requestCode) {
		case GlobalWiwikey.REQUEST_GETPROPNEWSLIST:
			PropNoticeListBean bean = (PropNoticeListBean) response;
			if (bean.getErrmsg().equals("SUCCESS")) {
				List<NoticeListBean> noticeList = bean.getNoticeList();
				mAdaper = new PlotNewsAdapter(this, noticeList);
				lv_news.setAdapter(mAdaper);

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
