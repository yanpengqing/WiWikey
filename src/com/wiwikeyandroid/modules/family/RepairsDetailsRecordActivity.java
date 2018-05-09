package com.wiwikeyandroid.modules.family;

import java.util.HashMap;
import java.util.Map;

import org.seny.android.utils.ActivityUtils;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.InjectView;

import com.android.volley.VolleyError;
import com.wiwikeyandroid.R;
import com.wiwikeyandroid.adapter.RepairsAdapter;
import com.wiwikeyandroid.bean.GetRepairListBean;
import com.wiwikeyandroid.bean.RBResponse;
import com.wiwikeyandroid.net.HttpLoader;
import com.wiwikeyandroid.net.HttpLoader.ResponseListener;
import com.wiwikeyandroid.utils.GlobalWiwikey;
import com.wiwikeyandroid.utils.PrefUtils;

public class RepairsDetailsRecordActivity extends Activity implements
		OnClickListener, ResponseListener {

	private Button back;
	private Button add;
	private Button btnLeft;
	private Button btnRight;
	private ListView mListView;
	private LinearLayout tv_data;
	private RepairsAdapter mAdapter;
	private String type; // 类型，repair 报修，complain 投诉
	private ImageView iv_describe;
	private TextView tv_describe;

	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.repairs_record_activity);
		type = getIntent().getStringExtra("data");
		iv_describe = (ImageView) findViewById(R.id.iv_describe);
		tv_describe = (TextView) findViewById(R.id.tv_describe);
		back = (Button) findViewById(R.id.repairs_back);
		add = (Button) findViewById(R.id.repairs_record_add);
		btnLeft = (Button) findViewById(R.id.btn_left);
		btnRight = (Button) findViewById(R.id.btn_right);
		mListView = (ListView) findViewById(R.id.lv);
		tv_data = (LinearLayout) findViewById(R.id.tv_data);
		tv_data.setVisibility(View.INVISIBLE);
		btnLeft.setSelected(true);
		back.setOnClickListener(this);
		add.setOnClickListener(this);
		btnLeft.setOnClickListener(this);
		btnRight.setOnClickListener(this);
		getRepairList();

	}

	/**
	 * 报修列表查询
	 */
	private void getRepairList() {
		new Thread(new Runnable() {
			public void run() {
				Map<String, String> params = new HashMap<String, String>();
				params.put(
						"memberId",
						PrefUtils.getInt(RepairsDetailsRecordActivity.this,
								"memberId", -1) + "");
				params.put("token", PrefUtils.getString(
						RepairsDetailsRecordActivity.this, "token", ""));
				
				String url;
				if (type.equals("repair")) {
					url = GlobalWiwikey.URL_GETREPAIRLIST;
				} else {
					url = GlobalWiwikey.URL_GETCOMPLAINLIST;
				}

				HttpLoader.post(url, params,
						GetRepairListBean.class,
						GlobalWiwikey.REQUEST_GETREPAIRLIST,
						RepairsDetailsRecordActivity.this);
			}
		}).start();

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_left: // 待处理
			btnLeft.setSelected(true);
			btnRight.setSelected(false);
			if (mAdapter == null) {
				return;
			}
			mAdapter.setSolve(false);
			if (mAdapter.getListsize() == 0) {
				tv_data.setVisibility(View.VISIBLE);
				iv_describe.setImageResource(type.equals("repair")?R.drawable.home_bg2_es:R.drawable.home_bg3_es);
				tv_describe.setText(type.equals("repair")?"还木有报修记录":"还木有反馈记录");
			} else {
				tv_data.setVisibility(View.INVISIBLE);
			}
			mAdapter.notifyDataSetChanged();
			break;
		case R.id.btn_right: // 已处理
			btnLeft.setSelected(false);
			btnRight.setSelected(true);
			if (mAdapter == null) {
				return;
			}
			mAdapter.setSolve(true);
			if (mAdapter.getListsize() == 0) {
				tv_data.setVisibility(View.VISIBLE);
				iv_describe.setImageResource(type.equals("repair")?R.drawable.home_bg2_es:R.drawable.home_bg3_es);
				tv_describe.setText(type.equals("repair")?"还木有报修记录":"还木有反馈记录");
			} else {
				tv_data.setVisibility(View.INVISIBLE);
			}
			mAdapter.notifyDataSetChanged();
			break;
		case R.id.repairs_back:
			finish();
			break;
		case R.id.repairs_record_add:
			ActivityUtils.startActivity(this,
					type.equals("repair") ? RepairsCommitActivity.class
							: ComplainCommitActivity.class);

			break;
		}

	}
		@Override
		protected void onResume() {
			super.onResume();
			getRepairList() ;
		}
	@Override
	public void onGetResponseSuccess(int requestCode, RBResponse response) {
		switch (requestCode) {
		case GlobalWiwikey.REQUEST_GETREPAIRLIST:
			GetRepairListBean bean = (GetRepairListBean) response;
			if (bean.getErrmsg().equals("SUCCESS")
					&&type.equals("repair")?bean.getRepairList().size()>0:bean.getComplainList().size() > 0) {
				mAdapter = new RepairsAdapter(this, false, type,
						type.equals("repair")?bean.getRepairList():bean.getComplainList());
				mListView.setAdapter(mAdapter);
				if (mAdapter.getListsize() == 0) {
					tv_data.setVisibility(View.VISIBLE);
					iv_describe.setImageResource(type.equals("repair")?R.drawable.home_bg2_es:R.drawable.home_bg3_es);
					tv_describe.setText(type.equals("repair")?"还木有报修记录":"还木有反馈记录");
				}
			} else {
				tv_data.setVisibility(View.VISIBLE);
				iv_describe.setImageResource(type.equals("repair")?R.drawable.home_bg2_es:R.drawable.home_bg3_es);
				tv_describe.setText(type.equals("repair")?"还木有报修记录":"还木有反馈记录");
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
