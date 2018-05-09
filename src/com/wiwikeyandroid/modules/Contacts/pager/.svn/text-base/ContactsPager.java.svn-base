package com.wiwikeyandroid.modules.Contacts.pager;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.v4.app.NotificationCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.wiwikeyandroid.R;
import com.wiwikeyandroid.activity.HomeAcivity;
import com.wiwikeyandroid.adapter.ContactsAdapter;
import com.wiwikeyandroid.modules.Contacts.model.Person;
import com.wiwikeyandroid.utils.CnToSpell;
import com.wiwikeyandroid.utils.ContactUtils;
import com.wiwikeyandroid.utils.PrefUtils;
import com.wiwikeyandroid.view.IndexableListView;
import com.wiwikeyandroid.view.PullToRefreshView;
import com.wiwikeyandroid.view.PullToRefreshView.OnHeaderRefreshListener;
import com.wiwikeyandroid.view.SeekEditText;

public class ContactsPager extends BasePager {

	@InjectView(R.id.lv_contacts)
	IndexableListView lvContacts;
	@InjectView(R.id.et_seek)
	SeekEditText et_seek;
	@InjectView(R.id.ll_loading)
	LinearLayout ll_loading;
	public static List<Person> lists;
	private ContactsAdapter adapter;

	private ContactContentObservers contactobserver;

	public ContactsPager(Activity mActivity) {

		super(mActivity);
		this.mActivity = mActivity;
	}

	public static final int MSG_ALL = 0x1;
	public static final int MSG_PART = 0x2;
	public static final int MSG_CONTACT = 0x3;

	private static final String TAG = "ContentObserver";

	private static final String[] PHONES_PROJECTION = new String[] {
			ContactsContract.RawContacts._ID,
			ContactsContract.RawContacts.VERSION };
	ArrayList<String> changedContacts = new ArrayList<String>(); // 改变列表

	private Handler mhandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_CONTACT:
				bChange();
				break;
			}
		}
	};

	@Override
	public void initData() {
		View mContactsTabView = View.inflate(mActivity,
				R.layout.item_contacts_listview, null);
		ButterKnife.inject(this, mContactsTabView);
		lvContacts.setVisibility(View.GONE);
		ll_loading.setVisibility(View.VISIBLE);
		new Thread() {
			public void run() {
				lists = ContactUtils.getContact(mActivity);
				adapter = new ContactsAdapter(mActivity, lists);
				mActivity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						lvContacts.setVisibility(View.VISIBLE);
						ll_loading.setVisibility(View.GONE);
						lvContacts.setAdapter(adapter);
					}
				});
			};
		}.start();
		lvContacts.setFastScrollEnabled(true);

		et_seek.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				// 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
				filterData(s.toString());
				// handler.obtainMessage(MSG_PART).sendToTarget();
			}
		});

		fl_content.addView(mContactsTabView);

		contactobserver = new ContactContentObservers(mhandler);
		queryIdAndVersion();
		registerContentObservers();

	}

	/**
	 * 注册ContentObservers
	 */
	@SuppressLint("NewApi")
	private void registerContentObservers() {
		// 注册一个监听数据库的监听器
		mActivity.getContentResolver()
				.registerContentObserver(
						ContactsContract.RawContacts.CONTENT_URI, true,
						contactobserver);

	}

	/**
	 * 保存联系人信息
	 */
	private void queryIdAndVersion() {
		String id = "";
		String version = "";
		ContentResolver resolver = mActivity.getContentResolver();
		Cursor phoneCursor = resolver.query(
				ContactsContract.RawContacts.CONTENT_URI, PHONES_PROJECTION,
				ContactsContract.RawContacts.DELETED + "==0 and 1=="
						+ ContactsContract.RawContacts.DIRTY, null, null);
		if (phoneCursor != null) {
			while (phoneCursor.moveToNext()) {

				id += phoneCursor.getString(0) + "#";
				version += phoneCursor.getString(1) + "#";

				Log.v(TAG, "ID: " + phoneCursor.getString(0));
				Log.v(TAG, "Version: " + phoneCursor.getString(1));
			}
			PrefUtils.setString(mActivity, "id", id);
			PrefUtils.setString(mActivity, "version", version);
		}
		phoneCursor.close();
	}

	// 判断是否修改通讯录。
	@SuppressLint("NewApi")
	private void bChange() {
		changedContacts.clear();
		String idStr;
		String versionStr;
		ArrayList<String> newid = new ArrayList<String>();
		ArrayList<String> newversion = new ArrayList<String>();
		idStr = PrefUtils.getString(mActivity, "id", "");
		versionStr = PrefUtils.getString(mActivity, "version", "");
		String[] mid = idStr.split("#");
		String[] mversion = versionStr.split("#");
		ContentResolver resolver = mActivity.getContentResolver();
		Cursor phoneCursor = resolver.query(
				ContactsContract.RawContacts.CONTENT_URI, PHONES_PROJECTION,
				ContactsContract.RawContacts.DELETED + "==0 and 1=="
						+ ContactsContract.RawContacts.DIRTY, null, null);
		while (phoneCursor.moveToNext()) {
			newid.add(phoneCursor.getString(0));
			newversion.add(phoneCursor.getString(1));
		}
		phoneCursor.close();
		for (int i = 0; i < mid.length; i++) {
			int k = newid.size();
			int j;
			for (j = 0; j < k; j++) {
				// 找到了，但是版本不一样，说明更新了此联系人的信息
				if (mid[i].equals(newid.get(j))) {
					if (!(mversion[i].equals(newversion.get(j)))) {
						changedContacts.add(newid.get(j) + "#"
								+ newversion.get(j));
						newid.remove(j);
						newversion.remove(j);
						break;

					}
					if (mversion[i].equals(newversion.get(j))) {
						newid.remove(j);
						newversion.remove(j);
						break;
					}
				}
			}
			// 如果没有在新的链表中找到联系人
			if (j >= k) {
				changedContacts.add(mid[i] + "#" + mversion[i]);
				Log.v("DEL", mid[i] + " " + mversion[i]);
			}
		}
		// 查找新增加的人员
		int n = newid.size();
		for (int m = 0; m < n; m++) {
			changedContacts.add(newid.get(m) + "#" + newversion.get(m));
		}

		if (changedContacts.size() > 0) {
			// notifyMessage();
			updateContacts();
		}
	}

	private void updateContacts() {
		lists = ContactUtils.getContact(mActivity);
		adapter.updateList(lists);

	}

	// 通知栏消息
	private void notifyMessage() {
		Log.d("TAG", "修改" + changedContacts.size());
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				mActivity).setSmallIcon(R.drawable.head).setContentTitle("通知")
				.setAutoCancel(true)// 设置可以清除
				.setContentText("通讯录有变化,点击更新");
		changedContacts.clear();
		Intent resultIntent = new Intent(mActivity, HomeAcivity.class);
		resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent resultPendingIntent = PendingIntent.getActivity(
				mActivity, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(resultPendingIntent);
		NotificationManager mNotificationManager = (NotificationManager) mActivity
				.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(0, mBuilder.build());
	}

	// 根据输入框输入值的改变来过滤搜索
	private void getQueryByName(String key) {

		if (key.equals("")) {
			lists.clear();
			ArrayList<Person> listMember = ContactUtils.getContact(mActivity);
			;
			for (Person l : listMember) {
				lists.add(l);
			}
		} else {
			ContentResolver cr = mActivity.getContentResolver();
			String[] projection = { ContactsContract.PhoneLookup.DISPLAY_NAME,
					ContactsContract.CommonDataKinds.Phone.NUMBER,
					ContactsContract.CommonDataKinds.Phone.CONTACT_ID };
			Cursor cursor = cr.query(
					ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
					projection, ContactsContract.Contacts.DISPLAY_NAME
							+ " like " + "'%" + key + "%'", null, null);
			if (lists != null) {
				lists.clear();
			}
			lvContacts.setFastScrollEnabled(false);

			if (cursor.getCount() <= 0) {
				// ContactMember cm = new ContactMember();
				// cm.setContact_name("无搜索结果");
				// lists.add(cm);
			} else {
				while (cursor.moveToNext()) {
					Person cm = new Person();
					cm.setName(cursor.getString(cursor
							.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
					cm.setNumber(cursor.getString(cursor
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
					cm.setContact_id(cursor.getInt(cursor
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)));
					lists.add(cm);
				}
			}
			cursor.close();
		}
	}

	/**
	 * 根据输入框中的值来过滤数据并更新ListView
	 * 
	 * @param filterStr
	 */
	private void filterData(String filterStr) {
		List<Person> filterDateList = new ArrayList<Person>();

		if (TextUtils.isEmpty(filterStr)) {
			filterDateList = lists;
		} else {
			filterDateList.clear();
			for (Person phoneInfo : lists) {
				String name = phoneInfo.getName();
				if (name.indexOf(filterStr.toString()) != -1
						|| CnToSpell.getFullSpell(name).startsWith(
								filterStr.toString())) {
					filterDateList.add(phoneInfo);
				}
			}
		}

		// 根据a-z进行排序
		// Collections.sort(filterDateList, new Comparator<ContactMember>() {
		// @Override
		// public int compare(ContactMember o1, ContactMember o2) {
		// String fLetter = o1.getSortKey();
		// String sLetter = o2.getSortKey();
		// return fLetter.compareTo(sLetter);
		// }
		// });
		// lists.clear();
		// lists.addAll(filterDateList);
		// mContactsAdapter.notifyDataSetChanged();
		adapter.updateList(filterDateList);
	}

	public class ContactContentObservers extends ContentObserver {

		public ContactContentObservers(Handler handler) {
			super(handler);
		}

		@Override
		public boolean deliverSelfNotifications() {
			return super.deliverSelfNotifications();
		}

		@Override
		public void onChange(boolean selfChange) {
			super.onChange(selfChange);

			Log.v(TAG, "联系人发生变化");
			mhandler.obtainMessage(MSG_CONTACT, "ContactsChanged")
					.sendToTarget();
		}

		@SuppressLint("NewApi")
		@Override
		public void onChange(boolean selfChange, Uri uri) {
			super.onChange(selfChange, uri);
		}
	}
}