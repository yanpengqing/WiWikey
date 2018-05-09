package com.wiwikeyandroid.modules.setting;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.InjectView;
import butterknife.OnClick;

import com.android.volley.VolleyError;
import com.wiwikeyandroid.R;
import com.wiwikeyandroid.adapter.HouseOwnerListAdapter;
import com.wiwikeyandroid.base.BaseActivity;
import com.wiwikeyandroid.bean.GetVerifyCodeBean;
import com.wiwikeyandroid.bean.HouseholdList;
import com.wiwikeyandroid.bean.HouseholdList.MemberListBean;
import com.wiwikeyandroid.bean.RBResponse;
import com.wiwikeyandroid.net.HttpLoader;
import com.wiwikeyandroid.net.HttpLoader.ResponseListener;
import com.wiwikeyandroid.utils.GlobalWiwikey;
import com.wiwikeyandroid.utils.PrefUtils;
import com.wiwikeyandroid.view.SwipeListView;

public class HouseOwnerRegActivity extends BaseActivity implements
		ResponseListener {

	@InjectView(R.id.houseowner_reg_back)
	Button btnBack;
	@InjectView(R.id.houseowner_reg_add_layout)
	RelativeLayout rlAdd;
	@InjectView(R.id.houseowner_reg_listview)
	SwipeListView lvHouseOwners;
	@InjectView(R.id.houseowner_reg_text)
	TextView tvTitle;

	private int index;
	private HouseOwnerListAdapter adapter;
	private List<MemberListBean> memberList; // 成员列表

	@Override
	protected int initContentView() {

		return R.layout.houseowner_reg_activity;
	}

	@Override
	protected void initView() {
		// 获取屏幕宽度和密度
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		lvHouseOwners.setDensity(metric.density);
		rlAdd.setVisibility(PrefUtils.getInt(this, "householdType0", -1) == 1 ? View.VISIBLE
				: View.GONE);

		getHouseholdList();
		lvHouseOwners.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				if (!(PrefUtils.getInt(HouseOwnerRegActivity.this,
						"householdType0", -1) == 1)) {
					return;
				}

				Intent intent = new Intent(HouseOwnerRegActivity.this,
						HouseOwnerInfoAddActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("user", memberList.get(pos));
				bundle.putBoolean("isAdd", false);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});

	}

	/**
	 * 获取某房间中所有的住户
	 */
	private void getHouseholdList() {
		new Thread(new Runnable() {
			public void run() {
				Map<String, String> params = new HashMap<String, String>();
				params.put(
						"houseId",
						PrefUtils.getInt(HouseOwnerRegActivity.this,
								"houseId0", -1) + "");
				params.put(
						"memberId",
						PrefUtils.getInt(HouseOwnerRegActivity.this,
								"memberId", -1) + "");
				params.put("token", PrefUtils.getString(
						HouseOwnerRegActivity.this, "token", ""));
				HttpLoader.post(GlobalWiwikey.URL_GETHOUSEHOLDLIST, params,
						HouseholdList.class,
						GlobalWiwikey.REQUEST_GETHOUSEHOLDLIST,
						HouseOwnerRegActivity.this);
			}
		}).start();

	}

	@OnClick({ R.id.houseowner_reg_back, R.id.houseowner_reg_add_layout })
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.houseowner_reg_back:
			finish();
			break;
		case R.id.houseowner_reg_add_layout:
			Intent intent = new Intent(this, HouseOwnerInfoAddActivity.class);
			Bundle bundle = new Bundle();
			bundle.putBoolean("isAdd", true);
			intent.putExtras(bundle);
			startActivity(intent);
			break;
		}
	}

	@Override
	protected void initListener() {

	}

	@Override
	protected void initData() {

	}

	@Override
	public void onGetResponseSuccess(int requestCode, RBResponse response) {
		switch (requestCode) {
		case GlobalWiwikey.REQUEST_GETHOUSEHOLDLIST:
			HouseholdList bean = (HouseholdList) response;
			if (bean.getErrno() == 0) {
				memberList = bean.getMemberList();
				if (memberList.size() > 0) {
					adapter = new HouseOwnerListAdapter(this,
							lvHouseOwners.getRightViewWidth(), memberList);
					lvHouseOwners.setAdapter(adapter);

					// 列表中删除按钮点击事件
					adapter.setOnRightItemClickListener(new HouseOwnerListAdapter.onRightItemClickListener() {

						@Override
						public void onRightItemClick(View v, int position) {
							if (!(PrefUtils.getInt(HouseOwnerRegActivity.this,
									"householdType0", -1) == 1)) {
								Toast.makeText(HouseOwnerRegActivity.this,
										"亲,你不能删除成员!", Toast.LENGTH_LONG).show();
								lvHouseOwners.hiddenRight(lvHouseOwners
										.getmPreItemView());
								
								return;
							}
							if (memberList.get(position).getMemberId() == PrefUtils
									.getInt(HouseOwnerRegActivity.this,
											"memberId", -1)) {
								Toast.makeText(HouseOwnerRegActivity.this,
										"亲,不可以删除自己!", Toast.LENGTH_LONG).show();
								lvHouseOwners.hiddenRight(lvHouseOwners
										.getmPreItemView());
								return;
							}
							
							deleteHousehold();
							index = position;

						}

					});
				}
			}

			break;
		case GlobalWiwikey.REQUEST_DELETEHOUSEHOLD:
			GetVerifyCodeBean delbean = (GetVerifyCodeBean) response;
			if (delbean.getErrno() == 0) {
				getHouseholdList();

			}

			break;
		default:
			break;
		}
	}

	/**
	 * 删除某房间中某个住户
	 */
	protected void deleteHousehold() {
		new Thread(new Runnable() {
			public void run() {
				Map<String, String> params = new HashMap<String, String>();
				params.put(
						"houseId",
						PrefUtils.getInt(HouseOwnerRegActivity.this,
								"houseId0", -1) + "");
				params.put("userId", memberList.get(index).getMemberId() + "");
				params.put(
						"memberId",
						PrefUtils.getInt(HouseOwnerRegActivity.this,
								"memberId", -1) + "");
				params.put("token", PrefUtils.getString(
						HouseOwnerRegActivity.this, "token", ""));
				HttpLoader.post(GlobalWiwikey.URL_DELETEHOUSEHOLD, params,
						GetVerifyCodeBean.class,
						GlobalWiwikey.REQUEST_DELETEHOUSEHOLD,
						HouseOwnerRegActivity.this);
			}
		}).start();
	}

	@Override
	protected void onResume() {
		super.onResume();
		getHouseholdList();
	}

	@Override
	public void onGetResponseError(int requestCode, VolleyError error) {

	}
}
