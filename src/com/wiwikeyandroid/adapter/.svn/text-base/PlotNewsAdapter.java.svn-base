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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.wiwikeyandroid.R;
import com.wiwikeyandroid.bean.PropNoticeListBean.NoticeListBean;
import com.wiwikeyandroid.net.HttpLoader;
import com.wiwikeyandroid.utils.GlobalWiwikey;

/**
 * 新闻
 * 
 */
public class PlotNewsAdapter extends BaseAdapter {

	private Context context;
	private List<NoticeListBean> datas = new ArrayList<NoticeListBean>();

	public PlotNewsAdapter(Context context, List<NoticeListBean> datas) {
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
					R.layout.plot_news_item_card, null);
			holder = new ViewHolder();
			holder.date = (TextView) convertView.findViewById(R.id.tv_date);
			holder.title1 = (TextView) convertView
					.findViewById(R.id.tv1);
			holder.title2 = (TextView) convertView
					.findViewById(R.id.tv2);
			holder.title3 = (TextView) convertView
					.findViewById(R.id.tv3);
			holder.title4 = (TextView) convertView
					.findViewById(R.id.tv4);
			
			holder.img1 = (ImageView) convertView
					.findViewById(R.id.img1);
			holder.img2 = (ImageView) convertView
					.findViewById(R.id.img2);
			holder.img3 = (ImageView) convertView
					.findViewById(R.id.img3);
			holder.img4 = (ImageView) convertView
					.findViewById(R.id.img4);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.title1.setText(datas.get(position).getTitle());
		HttpLoader.getImageLoader().get(
				GlobalWiwikey.URL_SERVER+datas.get(position).getImgUrl(),
				ImageLoader.getImageListener(holder.img2,
						R.drawable.icon_avatar, R.drawable.icon_avatar));
		holder.date.setText(DateUtils.formatDateSimple(datas.get(position).getStartDate()));
		
		return convertView;
	}

	class ViewHolder {
		private TextView date;
		private TextView title1,title2,title3,title4;
		private ImageView img1,img2,img3,img4;
		private RelativeLayout item1;
		private LinearLayout item2,item3,item4;
	}
}
