package com.wiwikeyandroid.modules.Contacts.pager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.seny.android.utils.ALog;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.wiwikeyandroid.R;
import com.wiwikeyandroid.adapter.ContactsAdapter;
import com.wiwikeyandroid.modules.Contacts.model.Person;
import com.wiwikeyandroid.utils.CnToSpell;
import com.wiwikeyandroid.utils.ContactUtils;
import com.wiwikeyandroid.utils.PrefUtils;
import com.wiwikeyandroid.view.IndexableListView;
import com.wiwikeyandroid.view.PullToRefreshView;
import com.wiwikeyandroid.view.PullToRefreshView.OnHeaderRefreshListener;
import com.wiwikeyandroid.view.SeekEditText;
import com.xiaomi.mipush.sdk.MiPushClient;

import android.app.Activity;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class neighboursPager extends BasePager  {

	@InjectView(R.id.lv_contacts)
	IndexableListView lvContacts;
	@InjectView(R.id.et_seek)
	SeekEditText et_seek;
	@InjectView(R.id.ll_et_seek) 
	LinearLayout ll_et_seek;
	@InjectView(R.id.ll_no_data)
	LinearLayout ll_no_data;
	@InjectView(R.id.iv_describe)
	ImageView iv_describe;
	@InjectView(R.id.tv_describe)
	TextView tv_describe;

	private List<Person> listsN = new ArrayList<Person>();
	private ContactsAdapter adapter;

	public neighboursPager(Activity mActivity) {
		super(mActivity);
	}

	@Override
	public void initData() {
		System.out.println("邻里页面数据初始化了");
		View mNeighboursTabView = View.inflate(mActivity,
				R.layout.item_contacts_listview, null);
		ButterKnife.inject(this, mNeighboursTabView);
		if (!PrefUtils.getString(mActivity, "memberHouseList0", "").isEmpty()) {
			final String plotId = PrefUtils.getString(mActivity,
					"memberHouseList0", "").split("#")[1];
			ALog.d("plotId:" + plotId);
			new Thread() {
				public void run() {
					listsN.clear();
					listsN.addAll(ContactsPager.lists);
					Iterator<Person> it = listsN.iterator();
					while (it.hasNext()) {
						if (!it.next().isNeighbour()) {
							it.remove();
						}
					}
					ALog.d("获取的邻里数量：" + listsN.size());
					adapter = new ContactsAdapter(mActivity, listsN);
					mActivity.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							if (listsN.size() == 0) {
								lvContacts.setVisibility(View.GONE);
								ll_et_seek.setVisibility(View.GONE);
								ll_no_data.setVisibility(View.VISIBLE);
								iv_describe
										.setImageResource(R.drawable.home_bg2_es);
								tv_describe.setText("阁下还没有邻里联系人\n ");
							} else {
								lvContacts.setVisibility(View.VISIBLE);
								lvContacts.setAdapter(adapter);
							}

						}
					});
				};
			}.start();
		} else {
			lvContacts.setVisibility(View.GONE);
			ll_et_seek.setVisibility(View.GONE);
			ll_no_data.setVisibility(View.VISIBLE);
			iv_describe.setImageResource(R.drawable.home_bg2_es);
			tv_describe.setText("阁下还没有邻里联系人\n ");

		}

		et_seek.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				// 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
				filterData(s.toString());
				// handler.obtainMessage(MSG_PART).sendToTarget();
			}
		});
		fl_content.addView(mNeighboursTabView);

	}
	
	/**
	 * 根据输入框中的值来过滤数据并更新ListView
	 * 
	 * @param filterStr
	 */
	private void filterData(String filterStr) {
		List<Person> filterDateList = new ArrayList<Person>();

		if (TextUtils.isEmpty(filterStr)) {
			filterDateList = listsN;
		} else {
			filterDateList.clear();
			for (Person phoneInfo : listsN) {
				String name = phoneInfo.getName();
				if (name.indexOf(filterStr.toString()) != -1
						|| CnToSpell.getFullSpell(name).startsWith(
								filterStr.toString())) {
					filterDateList.add(phoneInfo);
				}
			}
		}
		adapter.updateList(filterDateList);
	}
}
