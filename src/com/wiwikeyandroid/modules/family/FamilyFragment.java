package com.wiwikeyandroid.modules.family;

import org.seny.android.utils.ALog;
import org.seny.android.utils.ActivityUtils;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.wiwikeyandroid.R;
import com.wiwikeyandroid.agora.MsgEvent;
import com.wiwikeyandroid.base.BaseFragment;
import com.wiwikeyandroid.modules.phone.TenementNotifyDetailsActivity;
import com.wiwikeyandroid.utils.PrefUtils;
import com.wiwikeyandroid.view.PullToRefreshView;
import com.wiwikeyandroid.view.PullToRefreshView.OnHeaderRefreshListener;

import de.greenrobot.event.EventBus;

/**
 * @author Joseph on 2016/5/22.
 *         <p/>
 *         唯家发现主界面布局
 */
public class FamilyFragment extends BaseFragment {

	@InjectView(R.id.tv_plotName)
	TextView tv_plotName;
	@InjectView(R.id.rl_repair)
	// 报修
	RelativeLayout rl_repair;
	@InjectView(R.id.rl_complain)
	// 投诉
	RelativeLayout rl_complain;
	// 生活缴费
	@InjectView(R.id.rl_pay)
	RelativeLayout rl_pay;
	// 邻里圈
	@InjectView(R.id.rl_neighbor)
	RelativeLayout rl_neighbor;
	// 购物
	@InjectView(R.id.rl_shoping)
	RelativeLayout rl_shoping;
	//来访预约
	@InjectView(R.id.rl_visit)
	RelativeLayout  rl_visit;
	@Override
	public View CreateView() {
		ALog.d("FamilyFragment:CreateView()");
		View mFamilyView = View.inflate(mActivity, R.layout.family_navigation,
				null);
		ButterKnife.inject(this, mFamilyView);
		EventBus.getDefault().register(this);

		return mFamilyView;
	}

	@Override
	public void initData() {
		if (PrefUtils.getInt(mActivity, "memberType", -1) == 2) {
			tv_plotName.setText("游客");
		} else {
			if (!PrefUtils.getString(mActivity, "memberHouseList0", "")
					.isEmpty()) {
				tv_plotName.setText(PrefUtils.getString(mActivity,
						"memberHouseList0", "").split("#")[0]);
			} else {

			}
		}
	}

	@OnClick({ R.id.rl_repair, R.id.rl_complain, R.id.rl_pay, R.id.rl_neighbor,R.id.rl_visit,R.id.rl_shoping })
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.rl_repair:
			ActivityUtils.startActivityForData(mActivity,
					RepairsDetailsRecordActivity.class, "repair");
			break;

		case R.id.rl_complain:
			ActivityUtils.startActivityForData(mActivity,
					RepairsDetailsRecordActivity.class, "complain");
			break;

		case R.id.rl_pay:
			ActivityUtils.startActivityForData(mActivity,
					PayAndNeighboursActivity.class, "pay");
			break;
		case R.id.rl_neighbor:
			ActivityUtils.startActivityForData(mActivity,
					PayAndNeighboursActivity.class, "neighbor");
			break;
		case R.id.rl_shoping:
			ActivityUtils.startActivity(mActivity, TenementNotifyDetailsActivity.class);
			break;
		case R.id.rl_visit:
			ActivityUtils.startActivity(mActivity,
					VisitOrderActivity.class);
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
	public boolean onBackPressed() {
		return false;
	}
}
