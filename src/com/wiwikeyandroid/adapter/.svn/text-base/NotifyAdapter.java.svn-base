package com.wiwikeyandroid.adapter;

import java.util.ArrayList;
import java.util.List;

import org.seny.android.utils.DateUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.wiwikeyandroid.R;
import com.wiwikeyandroid.bean.PropNoticeListBean.NoticeListBean;
import com.wiwikeyandroid.net.HttpLoader;
import com.wiwikeyandroid.utils.GlobalWiwikey;

/**
 * 公告
 * 
 */
public class NotifyAdapter extends BaseAdapter {

	private Context context;
	private List<NoticeListBean> datas = new ArrayList<NoticeListBean>();

	public NotifyAdapter(Context context, List<NoticeListBean> datas) {
		this.datas = datas;
		this.context = context;
	}

	public List<NoticeListBean> getDatas() {
		return datas;
	}

	public void setDatas(List<NoticeListBean> datas) {
		if (datas != null) {
			this.datas = datas;
			
		}
	}

	public void setDatas(int i) {

	}

	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_notify_msg_list, null);
			holder = new ViewHolder();
			holder.title = (TextView) convertView
					.findViewById(R.id.tv_title_name);
			holder.icon = (ImageView) convertView
					.findViewById(R.id.iv_notify_icon);
			holder.content = (TextView) convertView
					.findViewById(R.id.tv_content_name);
			holder.date = (TextView) convertView.findViewById(R.id.tv_date);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.title.setText(datas.get(position).getTitle());
		HttpLoader.getImageLoader().get(
				GlobalWiwikey.URL_SERVER+datas.get(position).getImgUrl(),
				ImageLoader.getImageListener(holder.icon,
						R.drawable.home_message, R.drawable.home_message));
		holder.content.setText(datas.get(position).getBrief());
		holder.date.setText(DateUtils.formatDateSimple10(datas.get(position).getStartDate()));
		return convertView;
	}

	class ViewHolder {
		private TextView title;
		private ImageView icon;
		private TextView content;
		private TextView date;
	}

}
