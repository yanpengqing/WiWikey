package com.wiwikeyandroid.modules.phone;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.seny.android.utils.ALog;
import org.seny.android.utils.ActivityUtils;
import org.seny.android.utils.DensityUtil;

import android.app.Dialog;
import android.content.Context;
import android.database.ContentObserver;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Handler;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.android.volley.VolleyError;
import com.wiwikeyandroid.R;
import com.wiwikeyandroid.activity.HomeAcivity;
import com.wiwikeyandroid.adapter.CallRecordAdapter;
import com.wiwikeyandroid.adapter.HouselistAdapter;
import com.wiwikeyandroid.agora.MsgEvent;
import com.wiwikeyandroid.base.BaseFragment;
import com.wiwikeyandroid.bean.GetMemberHouseListBean;
import com.wiwikeyandroid.bean.GetMemberHouseListBean.MemberHouseListBean;
import com.wiwikeyandroid.bean.HouseInfo;
import com.wiwikeyandroid.bean.RBResponse;
import com.wiwikeyandroid.db.DBManager;
import com.wiwikeyandroid.modules.Contacts.model.Person;
import com.wiwikeyandroid.net.HttpLoader;
import com.wiwikeyandroid.net.HttpLoader.ResponseListener;
import com.wiwikeyandroid.utils.GlobalWiwikey;
import com.wiwikeyandroid.utils.PrefUtils;
import com.wiwikeyandroid.view.AlertDialog;
import com.wiwikeyandroid.view.PullToRefreshView;
import com.wiwikeyandroid.view.PullToRefreshView.OnHeaderRefreshListener;
import com.xiaomi.mipush.sdk.MiPushClient;

import de.greenrobot.event.EventBus;

/**
 * @author Joseph on 2016/5/22.
 *         <p/>
 *         电话主界面
 */
public class PhoneFragment extends BaseFragment implements ResponseListener,
		OnHeaderRefreshListener {

	@InjectView(R.id.rl_tenement_notify)
	RelativeLayout rlTenementNotify;
	@InjectView(R.id.rl_news_notify)
	RelativeLayout RelativeLayout;
	@InjectView(R.id.iv_notify_icon)
	ImageView ivNotifyIcon;
	@InjectView(R.id.tv_title_name)
	TextView tvTitleName;
	@InjectView(R.id.tv_content_name)
	TextView tvContentName;
	@InjectView(R.id.lv_call_records)
	ListView lvCallRecords;
	@InjectView(R.id.tv_plotName)
	TextView tv_plotName;
	@InjectView(R.id.rl_homebg_es)
	RelativeLayout rl_homebg_es;
	@InjectView(R.id.main_pull_refresh_view)
	PullToRefreshView pull_refresh;
	@InjectView(R.id.go_ContactsFragment)
	TextView goContacts;
	private LinearLayout lLayout_bg;
	Dialog dia;
	private Display display;
	private DBManager dbManager;// 数据库管理工具
	private List<Person> persons;
	private ContactContentObservers contactobserver;
	Uri notifyUri = Uri.parse("content://com.wiwikeyandroid");
	private CallRecordAdapter mAdapter;
	private boolean isChange = false;

	@Override
	public View CreateView() {
		ALog.d("PhoneFragment:CreateView()");
		View mPhoneView = View.inflate(mActivity, R.layout.phone_navigation,
				null);
		ButterKnife.inject(this, mPhoneView);
		EventBus.getDefault().register(this);
		pull_refresh.setOnHeaderRefreshListener(this);
		lvCallRecords.setDivider(null);
		return mPhoneView;
	}

	@Override
	public void onHeaderRefresh(PullToRefreshView view) {
		initData();
		pull_refresh.postDelayed(new Runnable() {

			@Override
			public void run() {
				// 设置更新时间
				pull_refresh.onHeaderRefreshComplete();
			}
		}, 1000);
	}

	/**
	 * 获取此会员绑定的小区,房产信息
	 */
	private void getMemberHouseList() {
		new Thread(new Runnable() {
			public void run() {
				Map<String, String> params = new HashMap<String, String>();
				params.put("memberId",
						PrefUtils.getInt(mActivity, "memberId", -1) + "");
				params.put("token", PrefUtils.getString(mActivity, "token", ""));
				HttpLoader.post(GlobalWiwikey.URL_GETMEMBERHOUSELIST, params,
						GetMemberHouseListBean.class,
						GlobalWiwikey.REQUEST_GETMEMBERHOUSELIST,
						PhoneFragment.this);
			}
		}).start();
	}

	@Override
	public void initData() {
		ALog.d(mActivity.getResources().getDisplayMetrics().density + "");
		ALog.d(DensityUtil.px2dip(mActivity, 36) + "");
		if (PrefUtils.getInt(mActivity, "memberType", -1) == 2) {
			tv_plotName.setText("游客");
		} else {
			if (!PrefUtils.getString(mActivity, "memberHouseList0", "")
					.isEmpty()) {
				ALog.e(PrefUtils.getString(mActivity, "memberHouseList0", "")
						+ "哈哈");
				tv_plotName.setText(PrefUtils.getString(mActivity,
						"memberHouseList0", "").split("#")[0]);

				String plotId = PrefUtils.getString(mActivity,
						"memberHouseList0", "").split("#")[1];
				System.out
						.println("-------------------------------------plotId："
								+ plotId);
				MiPushClient.setUserAccount(mActivity, plotId, null);
			} else {
				getMemberHouseList();
			}
		}

		dbManager = new DBManager(mActivity);
		persons = dbManager.query();
		persons = mySort(persons);
		contactobserver = new ContactContentObservers(null);
		mActivity.getContentResolver().registerContentObserver(notifyUri, true,
				contactobserver);
		//
		// dia = new Dialog(mActivity, R.style.AlertDialogStyle); // 获取Dialog布局
		// View view = LayoutInflater.from(mActivity).inflate(
		// R.layout.member_bind, null); lLayout_bg = (LinearLayout)
		// view.findViewById(R.id.lLayout_bg); ImageView iv_close = (ImageView)
		// view.findViewById(R.id.iv_close); EditText et_address_plot_ =
		// (EditText) view .findViewById(R.id.et_address_plot); final TextView
		// et_select_city = (TextView) view .findViewById(R.id.et_select_city);
		// EditText et_bind_name = (EditText)
		// view.findViewById(R.id.et_bind_name); Button bt_submit = (Button)
		// view.findViewById(R.id.bt_submit); dia.setContentView(view);
		//
		// WindowManager windowManager = (WindowManager) mActivity
		// .getSystemService(Context.WINDOW_SERVICE); display =
		// windowManager.getDefaultDisplay(); // 调整dialog背景大小 //
		// lLayout_bg.setLayoutParams(new FrameLayout.LayoutParams((int)
		// (display // .getWidth() * 0.85), LayoutParams.WRAP_CONTENT));
		//
		// bt_submit.setOnClickListener(new OnClickListener() {
		//
		// @Override public void onClick(View v) { //获取输入的数据调接口上传服务器 return;
		//
		// } }); iv_close.setOnClickListener(new OnClickListener() {
		//
		// @Override public void onClick(View v) { dia.cancel(); } });
		// dia.show(); dia.setCanceledOnTouchOutside(false);
		//

		// int memberType = PrefUtils.getInt(mActivity, "memberType", -1);
		// if (memberType == 2) {
		// new AlertDialog(mActivity).builder().setTitle("你好")
		// .setMsg("你还不是小区的认证住户")
		// .setPositiveButton("去认证", new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// // 启动认证界面
		// ActivityUtils.startActivity(mActivity,
		// AuthenticationOneActivity.class);
		//
		// }
		// }).setNegativeButton("取消", new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		//
		// }
		// }).show();
		// }

		lvCallRecords.setFocusable(false);
		if (persons.size() > 0) {
			mAdapter = new CallRecordAdapter(persons, mActivity);
			lvCallRecords.setVisibility(View.VISIBLE);
			lvCallRecords.setAdapter(mAdapter);
			setListViewHeight(lvCallRecords);
			rl_homebg_es.setVisibility(View.GONE);
		} else {
			lvCallRecords.setVisibility(View.GONE);
			rl_homebg_es.setVisibility(View.VISIBLE);
		}
	}

	public static List<Person> mySort(List<Person> list) {
		HashMap<String, Person> tempMap = new HashMap<>();
		for (Person user : list) {
			tempMap.put(user.getNumber(), user);
		}
		List<Person> tempList = new ArrayList<>();
		for (String key : tempMap.keySet()) {
			tempList.add(tempMap.get(key));
		}
		Collections.sort(tempList);
		return tempList;

	}

	@Override
	public boolean onBackPressed() {
		return false;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		ButterKnife.reset(this);
	}

	public List<HouseInfo> houseList;

	@OnClick({ R.id.tv_plotName, R.id.rl_tenement_notify, R.id.rl_news_notify,R.id.go_ContactsFragment })
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.tv_plotName: //
			int num = PrefUtils.getInt(mActivity, "HouseNumber", 1);
			if (num == 1) {
				return;
			}
			houseList = new ArrayList<HouseInfo>();
			HouseInfo houseInfo;
			for (int i = 0; i < num; i++) {
				houseInfo = new HouseInfo();
				houseInfo.setPlotName(PrefUtils.getString(mActivity,
						"memberHouseList" + i, "").split("#")[0]);
				houseInfo.setPlotId(PrefUtils.getString(mActivity,
						"memberHouseList" + i, "").split("#")[1]);
				houseInfo.setBuildingId(PrefUtils.getString(mActivity,
						"memberHouseList" + i, "").split("#")[2]);
				houseInfo.setHouseId(PrefUtils.getInt(mActivity, "houseId" + i,
						-1));
				houseInfo.setHouseholdType(PrefUtils.getInt(mActivity,
						"householdType" + i, -1));
				houseList.add(houseInfo);
			}
			showPopWindow(mActivity, tv_plotName, houseList);
			break;

		case R.id.rl_tenement_notify: // 物业公告二级界面
			if (PrefUtils.getString(mActivity, "memberHouseList0", "")
					.isEmpty()) {
				new AlertDialog(mActivity).builder()
						.setMsg("你现在还无法查看物业公告信息。请先绑定小区。")
						.setNegativeButton("确定", new OnClickListener() {
							@Override
							public void onClick(View v) {

							}
						}).show();
			} else {

				ActivityUtils.startActivity(mActivity,
						TenementNotifyListActivity.class);
			}

			break;
		case R.id.rl_news_notify: // 小区新闻二级界面
			if (PrefUtils.getString(mActivity, "memberHouseList0", "")
					.isEmpty()) {
				new AlertDialog(mActivity).builder()
						.setMsg("你现在还无法查看小区新闻信息 。请先绑定小区")
						.setNegativeButton("确定", new OnClickListener() {
							@Override
							public void onClick(View v) {

							}
						}).show();
			} else {

				ActivityUtils.startActivityForData(mActivity,
						TenementNotifyListActivity.class, "news");
			}

			break;
		case R.id.go_ContactsFragment:
			HomeAcivity mActivity = (HomeAcivity )getActivity();
			mActivity.setTabSelection(1);
			break;

		default:
			break;
		}

	}

	public void onEventMainThread(MsgEvent event) {
		switch (event.getMsg()) {
		case "auThenSucceed":
			initData();
			break;
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if (isChange) {
			initData();
			isChange = false;
		}
	}

	/**
	 * 监听通话记录变化
	 * 
	 * @author Administrator
	 * 
	 */
	public class ContactContentObservers extends ContentObserver {

		public ContactContentObservers(Handler handler) {
			super(handler);
		}

		@Override
		public void onChange(boolean selfChange) {
			super.onChange(selfChange);
			isChange = true;
		}
	}

	@Override
	public void onGetResponseSuccess(int requestCode, RBResponse response) {
		switch (requestCode) {
		case GlobalWiwikey.REQUEST_GETMEMBERHOUSELIST:
			GetMemberHouseListBean bean = (GetMemberHouseListBean) response;
			// 集合为空，查不到用户的小区信息，
			if (!bean.getMemberHouseList().isEmpty()
					&& bean.getMemberHouseList().get(0).getBuildingId() != -1) {
				List<MemberHouseListBean> memberHouseList = bean
						.getMemberHouseList();
				PrefUtils.setInt(mActivity, "HouseNumber",
						memberHouseList.size());
				for (int i = 0; i < memberHouseList.size(); i++) {
					int buildingId = memberHouseList.get(i).getBuildingId();
					String plotName = memberHouseList.get(i).getBasePlotValue()
							.getPlotName();
					int plotId = memberHouseList.get(i).getPlotId();
					int unitId = memberHouseList.get(i).getUnitId();
					PrefUtils.setString(mActivity, "memberHouseList" + i,
							plotName + "#" + plotId + "#" + buildingId + "#"
									+ unitId);
					PrefUtils.setInt(mActivity, "houseId" + i, memberHouseList
							.get(i).getHouseId());
					PrefUtils.setInt(mActivity, "householdType" + i,
							memberHouseList.get(i).getHouseholdType());
				}
				String str = PrefUtils.getString(mActivity, "memberHouseList0",
						"");
				tv_plotName.setText(str.split("#")[0]);
				EventBus.getDefault().post(new MsgEvent("auThenSucceed"));
			}
			break;
		default:
			break;

		}
	}

	private void showPopWindow(Context context, final TextView tvItem,
			List<HouseInfo> items) {

		LinearLayout layout = (LinearLayout) LayoutInflater.from(context)
				.inflate(R.layout.house_list_select, null);
		ListView lvTemp = (ListView) layout.findViewById(R.id.lv_slect_self);
		HouselistAdapter adapter = new HouselistAdapter(context, items);
		lvTemp.setAdapter(adapter);
		final PopupWindow popupWindow = new PopupWindow(tvItem);
		popupWindow.setWidth(tvItem.getWidth());
		popupWindow.setHeight(LayoutParams.WRAP_CONTENT);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setOutsideTouchable(true);
		popupWindow.setFocusable(true);
		popupWindow.setContentView(layout);
		popupWindow.showAsDropDown(tvItem, 0, 0);
		popupWindow.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {

			}
		});

		// listView的item点击事件
		lvTemp.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				tv_plotName.setText(houseList.get(position).getPlotName());
				PrefUtils.setString(mActivity, "memberHouseList0",
						houseList.get(position).getPlotName() + "#"
								+ houseList.get(position).getPlotId() + "#"
								+ houseList.get(position).getBuildingId() + "#"
								+ houseList.get(position).getUnitId());
				PrefUtils.setInt(mActivity, "houseId0", houseList.get(position)
						.getHouseId());
				PrefUtils.setInt(mActivity, "householdType0",
						houseList.get(position).getHouseholdType());
				PrefUtils.setString(mActivity, "memberHouseList" + position,
						houseList.get(0).getPlotName() + "#"
								+ houseList.get(0).getPlotId() + "#"
								+ houseList.get(0).getBuildingId() + "#"
								+ houseList.get(0).getUnitId());
				PrefUtils.setInt(mActivity, "houseId" + position, houseList
						.get(0).getHouseId());
				PrefUtils.setInt(mActivity, "householdType" + position,
						houseList.get(0).getHouseholdType());
				EventBus.getDefault().post(new MsgEvent("auThenSucceed"));
				popupWindow.dismiss();

			}
		});
	}
	  public  void setListViewHeight(ListView listView) {
          ListAdapter listAdapter = listView.getAdapter(); 
          if (listAdapter == null) {
              return;
          }

          int totalHeight = 0;
          for (int i = 0; i < listAdapter.getCount(); i++) {
              View listItem = listAdapter.getView(i, null, listView);
              listItem.measure(0, 0);
              totalHeight += listItem.getMeasuredHeight();
          }

          ViewGroup.LayoutParams params = listView.getLayoutParams();
          params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
          listView.setLayoutParams(params);
      }
	@Override
	public void onGetResponseError(int requestCode, VolleyError error) {

	}
}
