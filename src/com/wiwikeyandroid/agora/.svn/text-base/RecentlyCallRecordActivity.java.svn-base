package com.wiwikeyandroid.agora;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.seny.android.utils.ALog;
import org.seny.android.utils.MyToast;

import android.app.Dialog;
import android.content.ContentUris;
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.InjectView;
import butterknife.OnClick;

import com.wiwikeyandroid.R;
import com.wiwikeyandroid.adapter.CallRecordDetailsAdapter;
import com.wiwikeyandroid.base.BaseActivity;
import com.wiwikeyandroid.bean.PinnedSectionBean;
import com.wiwikeyandroid.db.DBManager;
import com.wiwikeyandroid.modules.Contacts.model.Person;
import com.wiwikeyandroid.utils.ContactUtils;
import com.wiwikeyandroid.utils.PrefUtils;
import com.wiwikeyandroid.view.ActionSheetDialog;
import com.wiwikeyandroid.view.ActionSheetDialog.OnSheetItemClickListener;
import com.wiwikeyandroid.view.ActionSheetDialog.SheetItemColor;
import com.wiwikeyandroid.view.AlertDialog;
import com.wiwikeyandroid.view.InviteAlertDialog;
import com.wiwikeyandroid.view.ShareBottomDialog;

public class RecentlyCallRecordActivity extends BaseActivity {

	@InjectView(R.id.authentication_text)
	TextView tvTitle;
	@InjectView(R.id.Call_details_name)
	// 联系人名字
	TextView tv_name;
	@InjectView(R.id.Call_details_moblie)
	// 电话
	TextView tv_phone;
	@InjectView(R.id.Call_details_icon)
	// 头像
	ImageView iv_icon;
	@InjectView(R.id.tv_content_type)
	TextView tv_type;
	@InjectView(R.id.Call_details_back)
	// 返回
	Button btn_back;
	@InjectView(R.id.Call_details_edit)
	// 编辑
	LinearLayout btn_edit;
	@InjectView(R.id.ll_comment)
	LinearLayout ll_comment; // 备注

	@InjectView(R.id.lv_callRecords)
	ListView lv_callRecords; // 通话记录
	@InjectView(R.id.ll_button_callout)
	// 底部语音视频按钮
	LinearLayout ll_button_callout;
	@InjectView(R.id.ll_item_door)
	LinearLayout ll_item_door;
	@InjectView(R.id.iv_btn_video)
	ImageView iv_btn_video;
	@InjectView(R.id.iv_btn_voice)
	ImageView iv_btn_voice;
	@InjectView(R.id.tv_video)
	TextView tv_video;
	@InjectView(R.id.tv_voice)
	TextView tv_voice;
	@InjectView(R.id.bt_video)
	LinearLayout bt_video;
	@InjectView(R.id.bt_voice)
	LinearLayout bt_voice;
	private Display display;
	private String acount;// 这是手机通讯录里面的名字
	private String phone;
	private Dialog dialog;
	private LinearLayout lLayout_bg;
	private Person mUser;
	private DBManager db;
	private List<Person> mData;
	private ArrayList<PinnedSectionBean> real_data;
	private CallRecordDetailsAdapter mAdapter;
	private boolean isShowRecord;// 是否显示通话记录

	@Override
	protected int initContentView() {

		return R.layout.recently_call_record;
	}

	@Override
	protected void initView() {
		mUser = (Person) getIntent().getSerializableExtra("user");
		isShowRecord = getIntent().getBooleanExtra("isShowRecord", false);
		ALog.d("isShowRecord" + isShowRecord);
		iv_icon.setImageResource(mUser.getMold() == 4 ? R.drawable.icon_door_img
				: R.drawable.icon_avatar);
		acount = mUser.getName();
		phone = mUser.getNumber();

		// if (isShowRecord) {
		// new Thread(
		// new Runnable() {
		// public void run() {
		// Map<String, String> params = new HashMap<String, String>();
		// params.put("mobile", phone);
		// params.put("memberId",
		// PrefUtils.getInt(RecentlyCallRecordActivity.this, "memberId", -1) +
		// "");
		//
		// params.put("token",
		// PrefUtils.getString(RecentlyCallRecordActivity.this, "token", ""));
		// HttpLoader.post(GlobalWiwikey.URL_GETMEMBERBYMOBILE,
		// params,
		// MemberByMobileBean.class,GlobalWiwikey.REQUEST_GETMEMBERBYMOBILE,
		// new ResponseListener() {
		//
		// @Override
		// public void onGetResponseSuccess(
		// int requestCode, RBResponse response) {
		// MemberByMobileBean bean = (MemberByMobileBean) response;
		// if (bean.getErrmsg().equals("SUCCESS")) { // 是唯家用户
		// mUser.setWiWiUser(true);
		// tv_type.setText("唯家资深用户");
		// } else {
		// tv_type.setText("邀请免费通话");
		// mUser.setWiWiUser(false);
		// }
		//
		// }
		//
		// @Override
		// public void onGetResponseError(int requestCode,
		// VolleyError error) {
		//
		// }
		// });
		// }
		// }
		// ).start();
		// }

	}

	@Override
	protected void initListener() {

	}

	@Override
	protected void initData() {
		// ||mUser.getMold()==1
		if (!isShowRecord && mUser.getContact_id() != 0) {
			tvTitle.setText("联系人");
		}
		// else if (!isShowRecord && mUser.getContact_id() == 0) {
		// btn_edit.setVisibility(View.GONE);
		// } else {
		// btn_edit.setVisibility(View.GONE);
		// }

		if (mUser.isWiWiUser() || mUser.getMold() == 1 || mUser.getMold() == 2) { // 是唯家用户
			tv_type.setText("唯家资深用户");
			tv_type.setTextColor(getResources().getColor(R.color.sub_head));
			tv_phone.setTextColor(getResources().getColor(R.color.colorBlue));
			tv_type.setEnabled(false);
		} else {
			tv_type.setEnabled(true);
			tv_type.setText("邀请免费通话");
			tv_type.setTextColor(getResources().getColor(R.color.colorBlue));
			tv_phone.setTextColor(getResources().getColor(R.color.sub_head));
			iv_btn_video.setImageResource(R.drawable.home_btn_video_gray);
			iv_btn_voice.setImageResource(R.drawable.home_btn_call_gray);
			tv_video.setTextColor(getResources().getColor(R.color.sub_head));
			tv_voice.setTextColor(getResources().getColor(R.color.sub_head));

		}
		tv_name.setText(mUser.getMold() == 4 ? "唯家门口机" : acount);
		StringBuffer phoneStr = new StringBuffer(phone);
		phoneStr.insert(3, "-").insert(8, "-");
		tv_phone.setText(phoneStr);
		// 获取全部通话记录
		if (isShowRecord) {
			getCallRecords(phone);
			ll_comment.setVisibility(View.GONE);
			lv_callRecords.setVisibility(View.VISIBLE);

			if (mUser.getMold() == 4) {
				ll_button_callout.setVisibility(View.GONE);
				ll_item_door.setVisibility(View.GONE);
				tv_type.setVisibility(View.GONE);
				tv_name.setGravity(Gravity.CENTER_VERTICAL);
			}

		} else {

		}
	}

	/**
	 * 获取通话记录
	 * 
	 * @param number
	 *            手机号码
	 */

	private void getCallRecords(String number) {
		db = new DBManager(this);
		mData = db.queryByNumber(number);
		Collections.sort(mData);
		real_data = PinnedSectionBean.getData(mData);
		// Collections.sort(real_data); 无效果
		for (int i = 0; i < real_data.size(); i++) {
			if (real_data.get(i).type == PinnedSectionBean.SECTION) {
				ALog.d(real_data.get(i).getDetail().getDate() + "");
			}
		}

		mAdapter = new CallRecordDetailsAdapter(real_data, this);
		lv_callRecords.setAdapter(mAdapter);
	}

	@OnClick({ R.id.bt_video, R.id.bt_voice, R.id.Call_details_edit,
			R.id.Call_details_back ,R.id.tv_content_type})
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.Call_details_back:// 顶部返回按钮
			finish();
			break;
		case R.id.tv_content_type://邀请
			ShareBottomDialog();
			break;
		case R.id.bt_video:// 跳转到通话界面
			// &&mUser.getMold()!=1
			if (!mUser.isWiWiUser() && mUser.getMold() != 1
					&& mUser.getMold() != 2) {
				inviteDialog();
				return;
			}

			Intent intent = new Intent(this, CallOutActivity.class);
			intent.putExtra("acount", acount);
			intent.putExtra("phone", phone);
			intent.putExtra("channleID",
					"video" + PrefUtils.getString(this, "mobile", ""));
			intent.putExtra(CallOutActivity.EXTRA_CALLING_TYPE,
					CallOutActivity.CALLING_TYPE_VIDEO);
			startActivity(intent);
			finish();
			break;

		case R.id.bt_voice:
			// &&mUser.getMold()!=1
			if (!mUser.isWiWiUser() && mUser.getMold() != 1
					&& mUser.getMold() != 2) {
				inviteDialog();
				return;
			}
			Intent intentVoice = new Intent(this, VoiceCallOutActivity.class);
			intentVoice.putExtra("acount", acount);
			intentVoice.putExtra("phone", phone);
			intentVoice.putExtra("channleID",
					"voice" + PrefUtils.getString(this, "mobile", ""));
			intentVoice.putExtra(CallOutActivity.EXTRA_CALLING_TYPE,
					CallOutActivity.CALLING_TYPE_VOICE);
			startActivity(intentVoice);
			finish();

			break;
		case R.id.Call_details_edit:
			if (!isShowRecord && mUser.getContact_id() != 0) {
				editContactsDialog();
			} else {
				deleteCallRecordDialog();
			}

			break;

		}
	}

	// 弹出邀请对方窗口
	private void inviteDialog() {

		new InviteAlertDialog(this).builder()
				.setTitle("邀请 " + "‘" + mUser.getName() + "’" + " 使用免费电话")
				.setMsg("邀请对方安装唯家APP获得免费" + "通话或拨打电话功能")
				.setPositiveButton("邀请", new OnClickListener() {

					@Override
					public void onClick(View v) {
						ShareBottomDialog();
					}
				}).setCustomButton("普通电话", new OnClickListener() {

					@Override
					public void onClick(View v) {
						// Intent intentPhone = new Intent(
						// RecentlyCallRecordActivity.this,
						// CommonPhoneActivity.class);
						// Bundle bundle = new Bundle();
						// bundle.putSerializable("user", mUser);
						// intentPhone.putExtras(bundle);
						// startActivity(intentPhone);
						// finish();
						Intent intent = new Intent(Intent.ACTION_CALL, Uri
								.parse("tel:" + phone));
						startActivity(intent);

					}
				}).setNegativeButton("取消", new OnClickListener() {

					@Override
					public void onClick(View v) {

					}
				}).show();

	}

	protected void ShareBottomDialog() {
		new ShareBottomDialog(this).builder().setCancelable(false)
				.setCanceledOnTouchOutside(true).setUiBeforShow(phone).show();
	}

	// 从底部弹出编辑联系人的自定义dialog
	private void editContactsDialog() {
		new ActionSheetDialog(this)
				.builder()
				.setCancelable(false)
				.setCanceledOnTouchOutside(true)
				.addSheetItem("编辑联系人", SheetItemColor.Black,
						new OnSheetItemClickListener() {
							@Override
							public void onClick(int which) {
								Uri content_lookup_uri = ContentUris
										.withAppendedId(Contacts.CONTENT_URI,
												mUser.getContact_id());
								Intent editIntent = new Intent(
										Intent.ACTION_EDIT);
								editIntent
										.setDataAndType(
												content_lookup_uri,
												ContactsContract.Contacts.CONTENT_ITEM_TYPE);
								startActivity(editIntent);

							}
						})
				.addSheetItem("删除联系人", SheetItemColor.Black,
						new OnSheetItemClickListener() {
							@Override
							public void onClick(int which) {

								new AlertDialog(RecentlyCallRecordActivity.this)
										.builder()
										.setTitle("删除联系人")
										.setMsg("删除联系人会把本地系统联系人也删除，确定删除 ？")
										.setPositiveButton("确认删除",
												new OnClickListener() {
													@Override
													public void onClick(View v) {
														// 删除联系人
														ContactUtils
																.deleteContact(
																		RecentlyCallRecordActivity.this,
																		mUser);

													}
												})
										.setNegativeButton("取消",
												new OnClickListener() {
													@Override
													public void onClick(View v) {

													}
												}).show();
							}
						}).show();
	}

	// 删除通话记录自定义dialog
	private void deleteCallRecordDialog() {
		new ActionSheetDialog(this)
				.builder()
				.setCancelable(false)
				.setCanceledOnTouchOutside(true)
				.addSheetItem("删除通话记录", SheetItemColor.Black,
						new OnSheetItemClickListener() {
							@Override
							public void onClick(int which) {
								if (db.delete(mUser)) {
									finish();
								} else {
									MyToast.show(
											RecentlyCallRecordActivity.this,
											"删除通话记录失败");
								}
							}
						}).show();
	}
}
