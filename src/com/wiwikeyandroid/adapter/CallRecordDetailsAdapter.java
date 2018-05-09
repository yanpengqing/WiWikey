package com.wiwikeyandroid.adapter;

import java.util.ArrayList;

import org.seny.android.utils.DateUtils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wiwikeyandroid.R;
import com.wiwikeyandroid.bean.PinnedSectionBean;
import com.wiwikeyandroid.view.PinnedSectionListView.PinnedSectionListAdapter;

public class CallRecordDetailsAdapter extends BaseAdapter implements
		PinnedSectionListAdapter {
	// 时间以及相关信息
	private ArrayList<PinnedSectionBean> list; // 分好类别后的list,在所属activity进行数据分类
	private Context mContext;

	public ArrayList<PinnedSectionBean> getList() {
		return list;
	}

	public void setList(ArrayList<PinnedSectionBean> list) {
		if (list != null) {
			this.list = list;
		} else {
			list = new ArrayList<PinnedSectionBean>();
		}
	}

	public CallRecordDetailsAdapter(ArrayList<PinnedSectionBean> list,
			Context mContext) {
		super();
		this.setList(list);
		this.mContext = mContext;
	}

	final static class ViewHolder {
		TextView item_date, item_duration, item_type;
		ImageView item_mold;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public PinnedSectionBean getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_list_call_record, null);
			viewHolder.item_date = (TextView) convertView
					.findViewById(R.id.item_date);
			viewHolder.item_duration = (TextView) convertView
					.findViewById(R.id.item_duration);
			viewHolder.item_type = (TextView) convertView
					.findViewById(R.id.item_type);
			viewHolder.item_mold = (ImageView) convertView
					.findViewById(R.id.item_mold);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		PinnedSectionBean bean = getItem(position);
		// 当item属于标题的时候,就只显示分类的日期yyyy-MM-dd
		if (bean.type == PinnedSectionBean.SECTION) {
			viewHolder.item_date.setText(DateUtils.formatDateSimple(list
					.get(position).getDetail().getDate()));
			viewHolder.item_date.setGravity(Gravity.START);
			viewHolder.item_date.setTextColor(mContext.getResources().getColor(
					R.color.text_title));
			viewHolder.item_mold.setVisibility(View.GONE);
		}
		// 当item属于内容的时候,就只显示分类的日期HH:mm:ss,和其他类容
		else {
			viewHolder.item_date.setText(DateUtils.formatTime(list
					.get(position).getDetail().getDate()));
			String duration ;
			if (list.get(position).getDetail().getDuration()>60) {
				duration = list.get(position).getDetail().getDuration() / 60
						+ "分" + list.get(position).getDetail().getDuration() % 60
						+ "秒";
			}else if(list.get(position).getDetail().getDuration()==60){
				duration =60+ "秒";
			}else {
				duration =list.get(position).getDetail().getDuration() % 60
						+ "秒";
			}
			viewHolder.item_duration.setText(duration);
			viewHolder.item_mold
					.setImageResource(list.get(position).getDetail().getMold() == 1||list.get(position).getDetail().getMold() == 4 ? R.drawable.home_btn_video_blue
							: R.drawable.home_btn_call_blue);
			
			// 1呼出， 2已接，3未接通，4未接 ,5 拒绝？
			switch (list.get(position).getDetail().getType()) {
			case 1:
				viewHolder.item_type.setText("呼出");
				break;
			case 2:
				viewHolder.item_type.setText("已接");
				break;
			case 3:
				viewHolder.item_type.setText("未接通");
				break;
			case 4:
				viewHolder.item_type.setText("未接");
				break;
			case 5:
				viewHolder.item_type.setText("拒绝");
				break;

			default:
				break;
			}

		}

		return convertView;
	}

	// 判断是否是属于标题的
	@Override
	public boolean isItemViewTypePinned(int viewType) {
		return viewType == PinnedSectionBean.SECTION;
	}

	// arraylist的数据里面有2种类型,标题和内容
	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public int getItemViewType(int position) {
		return ((PinnedSectionBean) getItem(position)).type;
	}

}
