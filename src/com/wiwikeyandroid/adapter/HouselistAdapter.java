package com.wiwikeyandroid.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wiwikeyandroid.R;
import com.wiwikeyandroid.bean.HouseInfo;

/**
 * @author Joseph
 * @date 2016-8-10 类说明
 */
public class HouselistAdapter extends BaseAdapter {
	LayoutInflater inflater;
	Context context;
	List<HouseInfo> items;

	public HouselistAdapter(Context context, List<HouseInfo> items) {
		super();
		this.context = context;
		this.items = items;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		HouseInfo item = items.get(position);
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.house_list_item, null);
			viewHolder = new ViewHolder();
			viewHolder.layout = (RelativeLayout) convertView
					.findViewById(R.id.spinner_dropdown_layout_self);
			viewHolder.textView = (TextView) convertView
					.findViewById(R.id.tvSpinner_dropdown_self);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.textView.setText(item.getPlotName());
		return convertView;
	}

	public class ViewHolder {
		RelativeLayout layout;
		TextView textView;
	}

}
