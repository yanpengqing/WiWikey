package com.wiwikeyandroid.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wiwikeyandroid.R;
import com.wiwikeyandroid.bean.GetHouseListBean.HouseListBean;
import com.wiwikeyandroid.bean.HouseholdList.MemberListBean;

public class HouseOwnerListAdapter extends BaseAdapter {

	private List<MemberListBean> mList;
	private LayoutInflater mInflater;
	private Context mContext;

	// 删除按钮宽度
	private int mRightWidth = 0;

	public HouseOwnerListAdapter(Context ctx, int rightWidth,
			List<MemberListBean> memberList) {

		mContext = ctx;
		mRightWidth = rightWidth;
		mList = memberList;
		mInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHold hold;
		if (convertView == null) {

			convertView = mInflater.inflate(R.layout.house_owner_list_adapter,
					null);

			hold = new ViewHold();
			// 左边显示数据View
			hold.item_left = (LinearLayout) convertView
					.findViewById(R.id.house_owner_list_adapter_left_one);
			// 右边删除按钮View
			hold.item_right = (RelativeLayout) convertView
					.findViewById(R.id.house_owner_list_adapter_right_one);
			hold.photo = (ImageView) convertView
					.findViewById(R.id.house_owner_list_adapter_photo_one);
			hold.name = (TextView) convertView
					.findViewById(R.id.house_owner_list_adapter_name_one);
			hold.status = (TextView) convertView
					.findViewById(R.id.house_owner_list_adapter_status_one);

			convertView.setTag(hold);
		}
		hold = (ViewHold) convertView.getTag();
		hold.name.setText(mList.get(position).getRealname());
		int householdType = mList.get(position).getHouseholdType();// 1:业主
																	// 2:家庭成员
																	// 4:租客 5:其他
		switch (householdType) {
		case 1:
			hold.status.setText("业主");
			break;
		case 2:
			hold.status.setText("家庭成员");
			break;
		case 4:
			hold.status.setText("租客");
			break;
		case 5:
			hold.status.setText("其他");
			break;
		default:
			hold.status.setText("其他");
			break;
		}
		int theSize = position % 5;
		switch (theSize) {
		case 0:
			hold.photo.setImageResource(R.drawable.hourse_one);
			break;
		case 1:
			hold.photo.setImageResource(R.drawable.hourse_two);
			break;
		case 2:
			hold.photo.setImageResource(R.drawable.hourse_three);
			break;
		case 3:
			hold.photo.setImageResource(R.drawable.hourse_four);
			break;
		case 4:
			hold.photo.setImageResource(R.drawable.hourse_fif);
			break;

		}
		// 加载显示布局属性
		LinearLayout.LayoutParams lp1 = new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		hold.item_left.setLayoutParams(lp1);
		// 加载删除按钮属性
		LinearLayout.LayoutParams lp2 = new LayoutParams(mRightWidth,
				LayoutParams.MATCH_PARENT);
		hold.item_right.setLayoutParams(lp2);

		// 删除按钮点击事件
		hold.item_right.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mListener != null) {
					mListener.onRightItemClick(v, position);
				}
			}
		});
		return convertView;
	}

	private class ViewHold {
		LinearLayout item_left;
		RelativeLayout item_right;
		private ImageView photo;
		private TextView name;
		private TextView status;
	}

	/**
	 * 单击事件监听器
	 */
	private onRightItemClickListener mListener = null;

	public void setOnRightItemClickListener(onRightItemClickListener listener) {
		mListener = listener;
	}

	public interface onRightItemClickListener {
		void onRightItemClick(View v, int position);

	}
}
