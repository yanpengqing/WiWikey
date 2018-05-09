package com.wiwikeyandroid.modules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.seny.android.utils.ALog;
import org.seny.android.utils.ActivityUtils;
import org.seny.android.utils.MyToast;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.android.volley.VolleyError;
import com.wiwikeyandroid.R;
import com.wiwikeyandroid.adapter.AbstractWheelTextAdapter;
import com.wiwikeyandroid.adapter.ArrayWheelAdapter;
import com.wiwikeyandroid.bean.GetCityListBean;
import com.wiwikeyandroid.bean.GetCityListBean.ProvinceListBean;
import com.wiwikeyandroid.bean.GetCityListBean.ProvinceListBean.CityListBean;
import com.wiwikeyandroid.bean.GetCityListBean.ProvinceListBean.CityListBean.DistinctListBean;
import com.wiwikeyandroid.bean.GetHouseListBean;
import com.wiwikeyandroid.bean.GetHouseListBean.HouseListBean;
import com.wiwikeyandroid.bean.GetPlotListBean;
import com.wiwikeyandroid.bean.GetPlotListBean.PlotListBean;
import com.wiwikeyandroid.bean.RBResponse;
import com.wiwikeyandroid.net.HttpLoader;
import com.wiwikeyandroid.net.HttpLoader.ResponseListener;
import com.wiwikeyandroid.utils.AddressData;
import com.wiwikeyandroid.utils.GlobalWiwikey;
import com.wiwikeyandroid.utils.PrefUtils;
import com.wiwikeyandroid.view.AlertDialog;
import com.wiwikeyandroid.view.MyAlertDialog;
import com.wiwikeyandroid.view.OnWheelChangedListener;
import com.wiwikeyandroid.view.WheelView;

public class AuthenticationOneActivity extends Activity implements
		ResponseListener, OnItemClickListener {

	private static final int GET_PLOTLIST = 1;
	@InjectView(R.id.authentication_back)
	Button authenticationBack;
	@InjectView(R.id.authentication_text) 
	TextView authenticationText;
	@InjectView(R.id.authentication_next)
	Button authenticationNext;
	@InjectView(R.id.authentication_title)
	RelativeLayout authenticationTitle;
	@InjectView(R.id.et_authentication_city)
	EditText etAuthenticationCity;
	// @InjectView(R.id.et_authentication_PlotList)
	// EditText etAuthenticationCityList;
	@InjectView(R.id.et_authentication_plot)
	EditText etAuthenticationPlot;
	// @InjectView(R.id.et_authentication_unit) //单元
	// EditText etAuthenticationUnit;
	@InjectView(R.id.et_authentication_number)
	EditText etAuthenticationNumber;
	@InjectView(R.id.lv_authentication)
	ListView mLvhouseLists;

	private GetCityListBean mCityListBean; //
	List<ProvinceListBean> provinceList; // 省
	List<CityListBean> cityListBean; // 城市

	private List<PlotListBean> plotList; // 小区信息的集合
	private PopupWindow popwindow;
	private ListView lv_list = null;
	private List<String> plots  = new ArrayList<>();;// 小区名字的集合
	private List<String> mHouseListStrings;// 小区名字的集合
	private String mAddress;  //详细地址
	private String cityTxt;
	private int c_c_id;

	private boolean isShowCity = false;// 是否选择城市
	// 定义Handler
	final Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case GET_PLOTLIST: //
				// provinceList = mCityListBean.getProvinceList();
				// initListview();
				GetPlotListBean getPlotListBean = (GetPlotListBean) msg.obj;
				
				if (getPlotListBean.getErrno() != 0) {
					// 获取数据失败,账号异常走这。。
					MyToast.show(AuthenticationOneActivity.this, "网络请求数据失败");
					return;
				}
				// 小区集合初始化
				plotList = getPlotListBean.getPlotList();
				for (int i = 0; i < plotList.size(); i++) {
					plots.add(plotList.get(i).getPlotName());
				}

				break;

			default:
				break;
			}
		}
	};
	private List<DistinctListBean> distinctList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.authentication_tenement_first);
		ButterKnife.inject(this);
		// initDate();
		// initListview();
		etAuthenticationNumber.addTextChangedListener(NumberTextWatcher);

	}

	private TextWatcher NumberTextWatcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			if (s.length() >= 3) {
				ALog.w("取数据");
				Map<String, String> params = new HashMap<String, String>();
				params.put("houseNumber", s.toString());
				params.put("plotId", plotId + "");
				params.put(
						"memberId",
						PrefUtils.getInt(AuthenticationOneActivity.this,
								"memberId", -1) + "");
				params.put("token", PrefUtils.getString(
						AuthenticationOneActivity.this, "token", ""));

				HttpLoader.post(GlobalWiwikey.URL_GETHOUSELIST, params,
						GetHouseListBean.class,
						GlobalWiwikey.REQUEST_GETHOUSELIST,
						AuthenticationOneActivity.this);

			}

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {

		}

		@Override
		public void afterTextChanged(Editable s) {

		}
	};
	private int plotId; // 小区ID
	private List<HouseListBean> mHouseList; //

	private int houseId; //所选的houseId
	private String number; //所选的小区
	/**
	 * 初始化小区数据
	 */
	private void initDate() {
		new Thread() {
			public void run() {
				Map<String, String> params = new HashMap<String, String>();
				params.put("distinctCode", c_c_id + "");
				params.put(
						"memberId",
						PrefUtils.getInt(AuthenticationOneActivity.this,
								"memberId", -1) + "");
				params.put("token", PrefUtils.getString(
						AuthenticationOneActivity.this, "token", ""));
				HttpLoader.post(GlobalWiwikey.URL_GETPLOTLIST, params,
						GetPlotListBean.class,
						GlobalWiwikey.REQUEST_GETPLOTLIST,
						AuthenticationOneActivity.this);
			}

		}.start();

	}

	@OnClick({ R.id.authentication_back, R.id.authentication_next,
			R.id.et_authentication_city, R.id.et_authentication_plot,
			R.id.et_authentication_number })
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.authentication_back:
			finish();
			break;
		case R.id.authentication_next:
			showNextActivity();

			break;
		case R.id.et_authentication_city:
			// showpopwindow(etAuthenticationCity);
			View vieww = dialogm();
			final MyAlertDialog dialog = new MyAlertDialog(
					AuthenticationOneActivity.this).builder().setTitle("请选择地区")
					.setView(vieww)
					.setNegativeButton("取消", new OnClickListener() {
						@Override
						public void onClick(View v) {

						}
					});
			dialog.setPositiveButton("保存", new OnClickListener() {
				@Override
				public void onClick(View v) {
					isShowCity = true;
					// Toast.makeText(getApplicationContext(), cityTxt,
					// 1).show();
					etAuthenticationCity.setText(cityTxt);
					etAuthenticationPlot.setText("");
					initDate();

				}
			});
			dialog.show();

			break;
		/*
		 * case R.id.et_authentication_PlotList: getPlotListData(cityListBean);
		 * 
		 * showpopwindow(etAuthenticationCityList); break;
		 */
		case R.id.et_authentication_plot:
			if (!isShowCity) {
				popupWarn("请选择所在城市", "确定");
				return;
			}
			if (plots.isEmpty()) {
				popupWarn("非常抱歉，暂未查询到你所在地区的小区信息", "确定");
				return;
			}
			showpopwindow(etAuthenticationPlot);

			break;
		case R.id.et_authentication_number:

			break;
		}
	}

	/**
	 * 弹出提示框
	 * 
	 * @param string2
	 * @param string
	 */
	private void popupWarn(String string, String string2) {
		new AlertDialog(this).builder().setMsg(string)
				.setNegativeButton(string2, new OnClickListener() {
					@Override
					public void onClick(View v) {

					}
				}).show();
	}

	private void showpopwindow(EditText et) {
		initListview();
		popwindow = new PopupWindow(lv_list, et.getWidth() - 4, 500);

		// 设置点击外部可以被关闭.
		popwindow.setOutsideTouchable(true);
		popwindow.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.alert_bg));

		// 获取焦点
		popwindow.setFocusable(true);
		// 显示在输入框的左下角位置
		popwindow.showAsDropDown(et, 2, -6);
	}

	private void initListview() {
		lv_list = new ListView(this);
		lv_list.setDivider(null);
		lv_list.setDividerHeight(0);
		lv_list.setBackgroundResource(R.drawable.actionsheet_middle_normal);
		lv_list.setOnItemClickListener(this);
		// 获取城市列表数据
		// for (int i = 0; i < provinceList.size(); i++) {
		// cityListBean = provinceList.get(i).getCityList();
		// }
		// // 获取区县
		// for (int i = 0; i < cityListBean.size(); i++) {
		// String dmmc = cityListBean.get(i).getDmmc();
		// plots.add(dmmc);
		// }

		// 绑定数据

		lv_list.setAdapter(new MyCityAdapter());
	}

	class MyCityAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return plots.size();
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			NumberViewHolder mHolder = null;
			if (convertView == null) {
				convertView = View.inflate(AuthenticationOneActivity.this,
						R.layout.item_authentication, null);
				mHolder = new NumberViewHolder();
				mHolder.tvCity = (TextView) convertView
						.findViewById(R.id.tv_city);
				convertView.setTag(mHolder);
			} else {
				// 取出holder类
				mHolder = (NumberViewHolder) convertView.getTag();
			}

			// 给mHolder类中的对象赋值.
			mHolder.tvCity.setText(plots.get(position));
			return convertView;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}
	}

	class NumberViewHolder {
		public TextView tvCity;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		number = plots.get(position);
		plotId = plotList.get(position).getPlotId();
		etAuthenticationPlot.setText(number);
		popwindow.dismiss();
	}

	@Override
	public void onGetResponseSuccess(int requestCode, RBResponse response) {
		switch (requestCode) {
		case GlobalWiwikey.REQUEST_GETPLOTLIST:
			ALog.w("获取小区信息数据成功");
			Message message = Message.obtain();
			message.obj = response;
			message.what = GET_PLOTLIST;
			handler.sendMessage(message);
			// String dmmc =
			// bean.getProvinceList().get(0).getCityList().get(0).getDmmc();
			break;
		case GlobalWiwikey.REQUEST_GETHOUSELIST:
			ALog.w("获取房号信息数据成功");
			GetHouseListBean mHouseListBean = (GetHouseListBean) response;
			mHouseListStrings = new ArrayList<String>();
			if (!mHouseListBean.getHouseList().isEmpty()) { // 有数据
				mHouseList = mHouseListBean.getHouseList();
				for (int i = 0; i < mHouseList.size(); i++) {
					String buildingName = mHouseList.get(i).getBuildingName();
					String unitName = mHouseList.get(i).getUnitName();
					String name = mHouseList.get(i).getName();
					mHouseListStrings.add(buildingName + "-" + unitName + "-"
							+ name);
				}
			} else {
				mHouseListStrings.clear();

			}

			MyHouseAdapter mHouseAdapter = new MyHouseAdapter();
			mLvhouseLists.setAdapter(mHouseAdapter);
			mLvhouseLists.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					mAddress = mHouseListStrings.get(position);
					etAuthenticationNumber.setText(mAddress);
					mAddress = number+" "+mAddress;
					houseId = mHouseList.get(position).getHouseId(); 
					ALog.e("houseId:"+houseId);
				}
			});

		default:
			break;
		}

	}

	@Override
	public void onGetResponseError(int requestCode, VolleyError error) {
				
	}

	public void showNextActivity() {
		if (TextUtils.isEmpty(etAuthenticationCity.getText().toString())) {
			Toast.makeText(this, "请选择城市", 0).show();
			return;
		}
		if (TextUtils.isEmpty(etAuthenticationPlot.getText().toString())) {
			Toast.makeText(this, "请选择小区", 0).show();
			return;
		}
	
		if (TextUtils.isEmpty(etAuthenticationNumber.getText().toString())) {
			Toast.makeText(this, "请输入房号", 0).show();
			return;
		}
		Intent intent = new Intent(this, AuthenticationTwoActivity.class);
		intent.putExtra("houseId", houseId);
		intent.putExtra("mAddress", mAddress);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.trans_in, R.anim.trans_out);

	}

	private View dialogm() {
		View contentView = LayoutInflater.from(this).inflate(
				R.layout.wheelcity_cities_layout, null);
		final WheelView country = (WheelView) contentView
				.findViewById(R.id.wheelcity_country);
		country.setVisibleItems(3);
		country.setViewAdapter(new CountryAdapter(this));

		final String cities[][] = AddressData.CITIES;
		final String ccities[][][] = AddressData.COUNTIES;
		final WheelView city = (WheelView) contentView
				.findViewById(R.id.wheelcity_city);
		city.setVisibleItems(0);

		// 地区选择
		final WheelView ccity = (WheelView) contentView
				.findViewById(R.id.wheelcity_ccity);
		ccity.setVisibleItems(0);// 不限城市

		country.addChangingListener(new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				updateCities(city, cities, newValue);
				cityTxt = AddressData.PROVINCES[country.getCurrentItem()]
						+ " | "
						+ AddressData.CITIES[country.getCurrentItem()][city
								.getCurrentItem()]
						+ " | "
						+ AddressData.COUNTIES[country.getCurrentItem()][city
								.getCurrentItem()][ccity.getCurrentItem()];
			}
		});

		city.addChangingListener(new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				updatecCities(ccity, ccities, country.getCurrentItem(),
						newValue);
				cityTxt = AddressData.PROVINCES[country.getCurrentItem()]
						+ " | "
						+ AddressData.CITIES[country.getCurrentItem()][city
								.getCurrentItem()]
						+ " | "
						+ AddressData.COUNTIES[country.getCurrentItem()][city
								.getCurrentItem()][ccity.getCurrentItem()];
			}
		});

		ccity.addChangingListener(new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				cityTxt = AddressData.PROVINCES[country.getCurrentItem()]
						+ " | "
						+ AddressData.CITIES[country.getCurrentItem()][city
								.getCurrentItem()]
						+ " | "
						+ AddressData.COUNTIES[country.getCurrentItem()][city
								.getCurrentItem()][ccity.getCurrentItem()];

				c_c_id = AddressData.C_C_ID[country.getCurrentItem()][city
						.getCurrentItem()][ccity.getCurrentItem()];
			}
		});

		country.setCurrentItem(18);// 设置北京
		city.setCurrentItem(1);
		ccity.setCurrentItem(7);
		return contentView;
	}

	private void updateCities(WheelView city, String cities[][], int index) {
		ArrayWheelAdapter<String> adapter = new ArrayWheelAdapter<String>(this,
				cities[index]);
		adapter.setTextSize(18);
		city.setViewAdapter(adapter);
		city.setCurrentItem(0);
	}

	private void updatecCities(WheelView city, String ccities[][][], int index,
			int index2) {
		ArrayWheelAdapter<String> adapter = new ArrayWheelAdapter<String>(this,
				ccities[index][index2]);
		adapter.setTextSize(18);
		city.setViewAdapter(adapter);
		city.setCurrentItem(0);
	}

	private class CountryAdapter extends AbstractWheelTextAdapter {
		// Countries names
		private String countries[] = AddressData.PROVINCES;

		/**
		 * Constructor
		 */
		protected CountryAdapter(Context context) {
			super(context, R.layout.wheelcity_country_layout, NO_RESOURCE);
			setItemTextResource(R.id.wheelcity_country_name);
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			View view = super.getItem(index, cachedView, parent);
			return view;
		}

		@Override
		public int getItemsCount() {
			return countries.length;
		}

		@Override
		protected CharSequence getItemText(int index) {
			return countries[index];
		}
	}

	public class MyHouseAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return mHouseListStrings.size();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			HouseViewHolder mHolder = null;
			if (convertView == null) {
				convertView = View.inflate(AuthenticationOneActivity.this,
						R.layout.item_authentication_house_listview, null);
				mHolder = new HouseViewHolder();
				mHolder.tvCity = (TextView) convertView
						.findViewById(R.id.tv_house);
				convertView.setTag(mHolder);
			} else {
				// 取出holder类
				mHolder = (HouseViewHolder) convertView.getTag();
			}

			// 给mHolder类中的对象赋值.
			mHolder.tvCity.setText(mHouseListStrings.get(position));
			return convertView;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

	}

	static class HouseViewHolder {
		public TextView tvCity;
	}
}
