package com.wiwikeyandroid.modules.setting;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.wiwikeyandroid.R;
import com.wiwikeyandroid.bean.GetVerifyCodeBean;
import com.wiwikeyandroid.bean.HouseholdList.MemberListBean;
import com.wiwikeyandroid.bean.RBResponse;
import com.wiwikeyandroid.net.HttpLoader;
import com.wiwikeyandroid.net.HttpLoader.ResponseListener;
import com.wiwikeyandroid.utils.GlobalWiwikey;
import com.wiwikeyandroid.utils.PrefUtils;
import com.wiwikeyandroid.utils.Tools;

public class HouseOwnerInfoAddActivity extends Activity implements
		ResponseListener {

	private Context mContext;
	private Button btnBack;
	private Button btnSave;
	private Button mSelect;
	private EditText etName;
	private EditText etPhone;
	private RadioButton rbMan;
	private RadioButton rbWoman;
	private TextView tvStatus;
	private TextView tvTitle;
	private TextView boy, girl;
	private String mSex;
	private String name;
	private String phone;
	private String mStatusId;
	private PopupWindow window;
	private View mView;
	private int householdType;
	private boolean isAdd = false;
	private MemberListBean mUser;
	private OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.houseowner_info_add_back:
				finish();
				break;
			case R.id.houseowner_info_add_status_text:
				ShowPopup();

				break;
			case R.id.item_owner:
				tvStatus.setText("业主");
				householdType = 1;
				if (window != null) {
					window.dismiss();
				}
				break;
			case R.id.item_family:
				householdType = 2;
				tvStatus.setText("家庭成员");
				if (window != null) {
					window.dismiss();
				}
				break;
			case R.id.item_rent:
				householdType = 4;
				tvStatus.setText("租客");
				if (window != null) {
					window.dismiss();
				}
				break;
			case R.id.item_else:
				householdType = 5;
				tvStatus.setText("其他");
				if (window != null) {
					window.dismiss();
				}
				break;
			case R.id.select_user:
				startActivityForResult(new Intent(Intent.ACTION_PICK,
						ContactsContract.Contacts.CONTENT_URI), 0);

				break;
			case R.id.houseowner_info_add_sex_man:
				if (rbMan.isChecked()) {
					mSex = "1";
				}
				// System.out.println("===sex+" + mSex);
				// rbWoman.setChecked(false);
				break;
			case R.id.tt_nan:
				if (rbMan.isChecked()) {

				} else {
					rbMan.setChecked(true);
				}
				mSex = "1";
				break;
			case R.id.tt_nv:
				if (rbWoman.isChecked()) {

				} else {
					rbWoman.setChecked(true);
				}

				mSex = "0";
				break;
			case R.id.houseowner_info_add_sex_woman:

				if (rbWoman.isChecked()) {
					mSex = "0";
				}
				System.out.println("===sex+" + mSex);
				// rbMan.setChecked(false);
				break;

			case R.id.houseowner_info_add_commit:
				String name = etName.getText().toString().trim();
				if (TextUtils.isEmpty(name)) {
					Toast.makeText(mContext, "请输入姓名！", Toast.LENGTH_SHORT)
							.show();
					return;
				}
				if (TextUtils.isEmpty(etPhone.getText().toString().trim())) {
					Toast.makeText(mContext, "请输入手机号！", Toast.LENGTH_SHORT)
							.show();
					return;
				}
				if (!HouseOwnerInfoAddActivity.isNumber(etPhone.getText()
						.toString().trim())) {
					Toast.makeText(mContext, "非法手机号！", Toast.LENGTH_SHORT)
							.show();
					return;
				}
				if (TextUtils.isEmpty(tvStatus.getText().toString().trim())) {
					Toast.makeText(mContext, "请选择住户类型！", Toast.LENGTH_SHORT)
							.show();
					return;
				}

				System.out.println("====sex+" + mSex);
				addHousehold();

				break;
			}
		}

	};
	// 日期对话框
	Calendar calendar = Calendar.getInstance();
	private TextView item_owner;
	private TextView item_family;
	private TextView item_rent;
	private TextView item_else;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.houseowner_add_activity);
		mContext = HouseOwnerInfoAddActivity.this;
		isAdd = this.getIntent().getBooleanExtra("isAdd", false);
		mUser = (MemberListBean) getIntent().getSerializableExtra("user");
		initView();

		if (isAdd) { //
			mSex = "1";

		} else { //
			tvTitle.setText("编辑成员资料");
			etName.setText(mUser.getRealname());
			etPhone.setText(mUser.getMobile());
			householdType = mUser.getHouseholdType();
			int sex = mUser.getSex();
			if (sex == 0) {
				mSex = "0";
				rbWoman.setChecked(true);
				rbMan.setChecked(false);
			} else {
				mSex = "1";
				rbWoman.setChecked(false);
				rbMan.setChecked(true);
			}
			switch (householdType) {
			case 1:
				tvStatus.setText("业主");
				break;

			case 2:
				tvStatus.setText("家庭成员");
				break;
			// case 3:
			// tvStatus.setText("共有人");
			// break;
			case 4:
				tvStatus.setText("租客");
				break;
			case 5:
				tvStatus.setText("其他");
				break;
			}

		}
	}

	public static boolean isic(String idNum) {

		// 定义判别用户身份证号的正则表达式（要么是15位，要么是18位，最后一位可以为字母）
		Pattern idNumPattern = Pattern
				.compile("(\\d{14}[0-9a-zA-Z])|(\\d{17}[0-9a-zA-Z])");
		// 通过Pattern获得Matcher
		Matcher idNumMatcher = idNumPattern.matcher(idNum);
		return idNumMatcher.matches();
	}

	public static boolean isNumber(String idNum) {

		Pattern idNumPattern = Pattern.compile("[1][358]\\d{9}");
		// 通过Pattern获得Matcher
		Matcher idNumMatcher = idNumPattern.matcher(idNum);
		return idNumMatcher.matches();
	}

	private void initView() {
		btnBack = (Button) this.findViewById(R.id.houseowner_info_add_back);
		btnBack.setOnClickListener(onClickListener);

		btnSave = (Button) this.findViewById(R.id.houseowner_info_add_commit);
		btnSave.setOnClickListener(onClickListener);
		tvTitle = (TextView) this.findViewById(R.id.houseowner_info_add_text);
		etName = (EditText) this
				.findViewById(R.id.houseowner_info_add_name_text);
		etPhone = (EditText) this
				.findViewById(R.id.houseowner_info_add_phone_text);
		rbMan = (RadioButton) this
				.findViewById(R.id.houseowner_info_add_sex_man);
		rbWoman = (RadioButton) this
				.findViewById(R.id.houseowner_info_add_sex_woman);
		tvStatus = (TextView) this
				.findViewById(R.id.houseowner_info_add_status_text);
		mSelect = (Button) findViewById(R.id.select_user);
		boy = (TextView) findViewById(R.id.tt_nan);
		mView = View.inflate(this, R.layout.item_household_type_layout, null);
		item_owner = (TextView) mView.findViewById(R.id.item_owner);
		item_family = (TextView) mView.findViewById(R.id.item_family);
		item_rent = (TextView) mView.findViewById(R.id.item_rent);
		item_else = (TextView) mView.findViewById(R.id.item_else);
		item_owner.setOnClickListener(onClickListener);
		item_family.setOnClickListener(onClickListener);
		item_rent.setOnClickListener(onClickListener);
		item_else.setOnClickListener(onClickListener);
		boy.setOnClickListener(onClickListener);
		girl = (TextView) findViewById(R.id.tt_nv);
		mSelect.setOnClickListener(onClickListener);
		girl.setOnClickListener(onClickListener);
		tvStatus.setOnClickListener(onClickListener);
		rbMan.setOnClickListener(onClickListener);
		rbWoman.setOnClickListener(onClickListener);
	}

	public void setStatusId(String statusId) {
		mStatusId = statusId;
	}

	private void ShowPopup() {
		if (window == null) {
			// 创建PopupWindow
			window = new PopupWindow(mView,
					RelativeLayout.LayoutParams.MATCH_PARENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT, true);
		}
		window.setFocusable(true);
		// 设置背景图片
		window.setBackgroundDrawable(new BitmapDrawable());
		// 设置外部点击消失
		window.setOutsideTouchable(true);
		window.showAsDropDown(tvStatus, 0, 0);

	}

	/**
	 * 住户登记或修改
	 */
	private void addHousehold() {
		new Thread(new Runnable() {
			public void run() {

				Map<String, String> params = new HashMap<String, String>();
				params.put("mobile", etPhone.getText().toString().trim());
				params.put("realName", etName.getText().toString().trim());
				params.put("sex", mSex);
				params.put(
						"houseId",
						PrefUtils.getInt(HouseOwnerInfoAddActivity.this,
								"houseId0", -1) + "");
				params.put("householdType", householdType + "");
				params.put(
						"memberId",
						PrefUtils.getInt(HouseOwnerInfoAddActivity.this,
								"memberId", -1) + "");
				params.put("token", PrefUtils.getString(
						HouseOwnerInfoAddActivity.this, "token", ""));
				if (!isAdd) {
					params.put("userId", mUser.getMemberId() + "");
				}
				HttpLoader.post(isAdd ? GlobalWiwikey.URL_ADDHOUSEHOLD
						: GlobalWiwikey.URL_UPDATEHOUSEHOLD, params,
						GetVerifyCodeBean.class,
						GlobalWiwikey.REQUEST_ADDHOUSEHOLD,
						HouseOwnerInfoAddActivity.this);
			}
		}).start();

	}

	@Override
	public void onActivityResult(int reqCode, int resultCode, Intent data) {
		super.onActivityResult(reqCode, resultCode, data);

		switch (reqCode) {
		case (0):
			if (resultCode == Activity.RESULT_OK) {
				Uri contactData = data.getData();
				CursorLoader cursorLoader = new CursorLoader(this, contactData,
						null, null, null, null);
				Cursor cursor = cursorLoader.loadInBackground();
				if (cursor.moveToFirst()) {
					String contactId = cursor.getString(cursor
							.getColumnIndex(ContactsContract.Contacts._ID));
					name = cursor
							.getString(cursor
									.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
					phone = "";
					Cursor phones = getContentResolver().query(
							ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
							null,
							ContactsContract.CommonDataKinds.Phone.CONTACT_ID
									+ "=" + contactId, null, null);
					if (phones.moveToFirst()) {
						phone = phones
								.getString(
										phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
								.replace(" ", "");
						;

						if (!Tools.isPhoneNum(phone)) {
							phone = "";
						}
					}

					etName.setText(name);
					etPhone.setText(phone);

					phones.close();
				}
				cursor.close();

			}
			break;
		}
	}

	@Override
	public void onGetResponseSuccess(int requestCode, RBResponse response) {
		switch (requestCode) {
		case GlobalWiwikey.REQUEST_ADDHOUSEHOLD:
			GetVerifyCodeBean bean = (GetVerifyCodeBean) response;
			if (bean.getErrno() == 0) {
				if (isAdd) {
					Toast.makeText(HouseOwnerInfoAddActivity.this, "添加成员成功", 0)
							.show();
				} else {
					Toast.makeText(HouseOwnerInfoAddActivity.this, "修改资料成功", 0)
							.show();
				}
				finish();
			}

			break;

		default:
			break;
		}
	}

	@Override
	public void onGetResponseError(int requestCode, VolleyError error) {

	}
}
