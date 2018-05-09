package com.wiwikeyandroid.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.seny.android.utils.ALog;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Data;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.wiwikeyandroid.bean.MemberByMobileBean;
import com.wiwikeyandroid.bean.RBResponse;
import com.wiwikeyandroid.modules.Contacts.model.Person;
import com.wiwikeyandroid.net.HttpLoader;
import com.wiwikeyandroid.net.HttpLoader.ResponseListener;

public class ContactUtils {

	public static final String SORT_KEY_PRIMARY = "sort_key";
	private static final String TAG = "ContactUtils";
	static String[] projection = { Phone.DISPLAY_NAME, Phone.NUMBER };
	private static String plotId="";

	private ContactUtils() {

	}

	public static ArrayList<Person> getContact(final Context context) { 

		System.out.println("获取通讯录联系人");
		ArrayList<Person> listMembers = new ArrayList<Person>();
		if (!PrefUtils.getString(context, "memberHouseList0", "").isEmpty()) {
		 plotId = PrefUtils.getString(context, 
					"memberHouseList0", "").split("#")[1];
		}
		Cursor cursor = null;
		try {

			Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
			// 这里是获取联系人表的电话里的信息 包括：名字，名字拼音，联系人id,电话号码,email,；
			// 然后在根据"sort-key"排序
			cursor = context.getContentResolver().query(
					uri,
					new String[] { "display_name", "sort_key", "contact_id",
							"data1" }, null, null, "sort_key");
			int a = 100;
			if (cursor.moveToFirst()) {
				do {
					final Person contact = new Person();
					final String contact_phone = removeAllSpace(cursor
							.getString(cursor
									.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
					if (!Tools.isPhoneNum(contact_phone)||contact_phone.equals(PrefUtils.getString(context, "mobile", ""))) {
						continue;
					}
					String name = cursor.getString(0);
					String sortKey = getSortKey(PinYin.getPinYin(cursor
							.getString(1).substring(0, 1)));
//					String email = cursor
//							.getString(cursor
//									.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS));
					int contact_id = cursor
							.getInt(cursor
									.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
					//
					// Long id = cursor.getLong(4);
					// Long photoid = cursor.getLong(2);
					// Bitmap contactPhoto = null;
					//
					// if (photoid > 0) {
					// Uri uri2 = ContentUris.withAppendedId(
					// ContactsContract.Contacts.CONTENT_URI, id);
					// InputStream input = ContactsContract.Contacts
					// .openContactPhotoInputStream(context.getContentResolver(),
					// uri2);
					// contactPhoto = BitmapFactory.decodeStream(input);
					// } else {
					// Resources r = context.getResources();
					// // 以数据流的方式读取资源
					// InputStream is = r.openRawResource(R.drawable.head);
					// BitmapDrawable bmpDraw = new BitmapDrawable(is);
					// contactPhoto = bmpDraw.getBitmap();
					// }

					// contact.setPhoto(contactPhoto);
					// contact.setPhotoId(photoid);
					Map<String, String> params = new HashMap<String, String>();
					params.put("mobile", contact_phone);
					params.put("memberId",
							PrefUtils.getInt(context, "memberId", -1) + "");

					params.put("token",
							PrefUtils.getString(context, "token", ""));
					HttpLoader.post(GlobalWiwikey.URL_GETMEMBERBYMOBILE,
							params, MemberByMobileBean.class, a += 1,
							new ResponseListener() {

								@Override
								public void onGetResponseSuccess(
										int requestCode, RBResponse response) {
									// case
									// GlobalWiwikey.REQUEST_GETMEMBERBYMOBILE:
									MemberByMobileBean bean = (MemberByMobileBean) response;
									Log.i(TAG, contact_phone + "通过手机号码获取信息成功:"
											+ bean.getErrno());
									
									if (bean.getErrmsg().equals("SUCCESS")&&bean.getMemberModel().getLastLoginTime()>0) { // 是唯家用户
										contact.setWiWiUser(true);
										contact.setPicUrl(GlobalWiwikey.URL_SERVER+bean.getMemberModel().getHeadMiddle());
									} else {
										contact.setWiWiUser(false);
									}
										if (!plotId.isEmpty()) {
											if (bean.getErrmsg().equals("SUCCESS")&&bean.getMemberModel().getPlotIdList().contains(Integer.valueOf(plotId))) {//是邻里关系 
												ALog.d("邻里为："+bean.getMemberModel().getMobile());
												contact.setNeighbour(true);
											
											}else {
												contact.setNeighbour(false);
											}
										}
									
								}

								@Override
								public void onGetResponseError(int requestCode,
										VolleyError error) {

								}
							});

					contact.setName(name);
					contact.setSortKey(sortKey);
					contact.setNumber(contact_phone);
					contact.setContact_id(contact_id);
					//contact.setEmail(email);

					if (name != null)
						listMembers.add(contact);
				} while (cursor.moveToNext());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return listMembers;
	}

	// 删除联系人
	public static void deleteContact(Context context, Person contact) {
		ContentResolver resolver = context.getContentResolver();
		resolver.delete(Data.CONTENT_URI,
				ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?",
				new String[] { String.valueOf(contact.getContact_id()) });

	}

	// 去除手机号码中间空格
	public static String removeAllSpace(String str) {
		String tmpstr = str.replace(" ", "");
		return tmpstr;
	}

	private static String getSortKey(String sortKeyString) {
		String key = sortKeyString.substring(0, 1).toUpperCase();
		if (key.matches("[A-Z]")) {
			return key;
		}
		return "#";
	}

	/**
	 * 根据手机号码查询联系人姓名
	 * 
	 * @param context
	 * @param phoneNum
	 *            (传入纯数字手机号码)
	 * @return
	 */

	public synchronized static String getDisplayNameByPhone(Context context,
			String phoneNum) {
		String displayName = phoneNum;
		String phone1 = new StringBuffer(phoneNum.subSequence(0, 3))
				.append(" ").append(phoneNum.substring(3, 7)).append(" ")
				.append(phoneNum.substring(7, 11)).toString();
		String phone2 = new StringBuffer(phoneNum.subSequence(0, 3))
				.append("-").append(phoneNum.substring(3, 7)).append("-")
				.append(phoneNum.substring(7, 11)).toString();
		ContentResolver resolver = context.getContentResolver();
		Cursor cursor = resolver.query(Phone.CONTENT_URI, projection,
				Phone.NUMBER + " in(?,?,?)", new String[] { phoneNum, phone1,
						phone2 }, null);

		if (cursor != null) {

			while (cursor.moveToNext()) {
				displayName = cursor.getString(cursor
						.getColumnIndex(Phone.DISPLAY_NAME));
				if (!TextUtils.isEmpty(displayName)) {
					break;
				}
				cursor.close();
			}
		}
		return displayName;
	}

}
