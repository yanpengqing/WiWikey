package com.wiwikeyandroid.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.wiwikeyandroid.R;
import com.wiwikeyandroid.agora.RecentlyCallRecordActivity;
import com.wiwikeyandroid.modules.Contacts.model.Person;
import com.wiwikeyandroid.utils.StringMatcher;

/**
 * 
 * @author Joseph on 2016/5/27.
 *         <p/>
 *         联系人
 */
public class ContactsAdapter extends BaseAdapter implements
		SectionIndexer {
	private static final String TAG = "ContactsAdapter";
	private String mSections = "#ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private Context context;
	List<Person> items;
	List<Person> mContactList;
	//private boolean isShowRecord;

	public ContactsAdapter(Context context, List<Person> items) { 
		this.context = context;
		this.items = items;
		//this.isShowRecord = isShowRecord;
		initList();
	}

	public void updateList(List<Person> lists) {
		this.items = lists;
		initList();
		notifyDataSetChanged();

	}

	private void initList() {
		mContactList = new ArrayList<Person>();
		for (int i = 0; i < items.size(); i++) {
			Person contact = items.get(i);
			mContactList.add(contact);
		}

	}

	@Override
	public int getPositionForSection(int section) {
		for (int i = section; i >= 0; i--) {
			for (int j = 0; j < getCount(); j++) {
				if (i == 0) {
					for (int k = 0; k <= 9; k++) {
						if (StringMatcher.match(String.valueOf(mContactList.get(j)
								.getSortKey().charAt(0)), String.valueOf(k)))
							return j;
					}
				} else {
					if (StringMatcher.match(
							String.valueOf(mContactList.get(j).getSortKey().charAt(0)),
							String.valueOf(mSections.charAt(i))))
						return j;
				}
			}
		}
		return 0;
	}

	@Override
	public int getSectionForPosition(int position) {
		return position;
	}

	@Override
	public Object[] getSections() {
		String[] sections = new String[mSections.length()];
		for (int i = 0; i < mSections.length(); i++)
			sections[i] = String.valueOf(mSections.charAt(i));
		return sections;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_contacts_tab, null);
			holder = new ViewHolder();
			holder.name = (TextView) convertView
					.findViewById(R.id.tv_item_contacts_name);
			holder.icon = (ImageView) convertView
					.findViewById(R.id.iv_item_contacts_icon);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.name.setText(mContactList.get(position).getName());
		// holder.icon.setImageBitmap(items.get(position).getPhoto());
		holder.icon.setImageResource(R.drawable.icon_avatar);

		convertView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// 打开选择呼叫界面
				Intent intent = new Intent(context,
						RecentlyCallRecordActivity.class);
				// intent.putExtra("acount",
				// items.get(position).getContact_name());
				// intent.putExtra("phone",
				// items.get(position).getContact_phone());
				Bundle bundle = new Bundle();
				bundle.putBoolean("isShowRecord", false);
				bundle.putSerializable("user", mContactList.get(position));
				intent.putExtras(bundle);
				context.startActivity(intent);
			}
		});
		return convertView;
	}

	class ViewHolder {
		private TextView name;
		private ImageView icon;
	}

	@Override
	public int getCount() {
		return	mContactList.size();
		
	}

	@Override
	public Object getItem(int position) {
		
		return mContactList.get(position);
	}

	@Override
	public long getItemId(int position) {
		
		return position;
	}
}
