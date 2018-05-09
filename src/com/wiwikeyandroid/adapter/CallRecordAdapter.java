package com.wiwikeyandroid.adapter;

import java.util.List;

import org.seny.android.utils.DateUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.android.volley.toolbox.ImageLoader;
import com.wiwikeyandroid.R;
import com.wiwikeyandroid.agora.RecentlyCallRecordActivity;
import com.wiwikeyandroid.modules.Contacts.model.Person;
import com.wiwikeyandroid.net.HttpLoader;
import com.wiwikeyandroid.utils.GlobalWiwikey;

/**
 * 
 * @author Joseph on 2016/5/27.
 *         <p/>
 *         最近通话记录信息
 */
public class CallRecordAdapter extends BaseAdapter {
	private Context context;
	private List<Person> persons;

	public CallRecordAdapter(List<Person> persons, Activity mActivity) {
		this.context = mActivity;
		this.persons = persons;
	}

	@Override
	public int getCount() {
		return persons.size();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parRent) {
		ViewHolder holder;
		View view;
		if (convertView != null) {
			view = convertView;
			holder = (ViewHolder) view.getTag();
		} else {
			view = View.inflate(context, R.layout.item_recently_call_record,
					null);
			holder = new ViewHolder(view);
			view.setTag(holder);
		}
		// 加载图片
		if (persons.get(position).getPicUrl() != null) {
			HttpLoader.getImageLoader().get(
					GlobalWiwikey.URL_SERVER+persons.get(position).getPicUrl(),
					ImageLoader.getImageListener(holder.ivIconRecentlyCall,
							R.drawable.icon_avatar, R.drawable.icon_avatar));
		} else {
			holder.ivIconRecentlyCall.setImageResource(R.drawable.icon_avatar);
		}
		
		if (persons.get(position).getName() == null) {
			holder.tvRecentlyCallName
					.setText(persons.get(position).getNumber());
		} else {
			holder.tvRecentlyCallName.setText(persons.get(position).getName());
		}
		
		holder.tvRecentlyCallTime.setText(DateUtils.formatDateSimple(persons
				.get(position).getDate()));
		holder.tvRecentlyCallMobile.setText(persons.get(position).getNumber());
		holder.tvRecentlyCallMobile.setTextColor(persons.get(position).getType()==4?
				context.getResources().getColor(R.color.colorRed):context.getResources().getColor(R.color.sub_head));
		//通话记录最后一条下划线是否显示?
		//holder.bottom_line.setVisibility(position==persons.size()-1?View.INVISIBLE:View.VISIBLE);
		holder.tvRecentlyCallMold.setImageResource(persons.get(position).getMold()==1?R.drawable.home_btn_video_gray:
			R.drawable.home_btn_call_gray);
		if (persons.get(position).getMold()==4) {
			holder.tvRecentlyCallMold.setImageResource(R.drawable.icon_door_record);
			holder.ivIconRecentlyCall.setImageResource(R.drawable.icon_door_img);
			holder.tvRecentlyCallName.setText("门口机");
			holder.tvRecentlyCallMobile.setVisibility(View.GONE);
		}
		
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 打开选择呼叫界面
				Intent intent = new Intent(context,
						RecentlyCallRecordActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("user", persons.get(position));
				bundle.putBoolean("isShowRecord", true);
				intent.putExtras(bundle);
				context.startActivity(intent);

			}
		});
		return view;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	static class ViewHolder {
		@InjectView(R.id.iv_icon_recently_call)
		ImageView ivIconRecentlyCall;
		@InjectView(R.id.tv_recently_call_name)
		TextView tvRecentlyCallName;
		@InjectView(R.id.tv_recently_call_time)
		TextView tvRecentlyCallTime;
		@InjectView(R.id.tv_recently_call_mobile)
		TextView tvRecentlyCallMobile;
		@InjectView(R.id.iv_recently_call_mold)
		ImageView tvRecentlyCallMold;
		@InjectView(R.id.bottom_line)
		View bottom_line;

		ViewHolder(View view) {
			ButterKnife.inject(this, view);
		}
	}

}
