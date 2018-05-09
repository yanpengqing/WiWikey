package com.wiwikeyandroid.modules.family;

import java.util.HashMap;
import java.util.Map;

import org.seny.android.utils.DateUtils;

import android.app.Activity;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.InjectView;
import butterknife.OnClick;

import com.android.volley.VolleyError;
import com.wiwikeyandroid.R;
import com.wiwikeyandroid.adapter.RepairsAdapter;
import com.wiwikeyandroid.base.BaseActivity;
import com.wiwikeyandroid.bean.GetVerifyCodeBean;
import com.wiwikeyandroid.bean.RBResponse;
import com.wiwikeyandroid.net.HttpLoader;
import com.wiwikeyandroid.net.HttpLoader.ResponseListener;
import com.wiwikeyandroid.utils.DateTimePickDialogUtil;
import com.wiwikeyandroid.utils.GlobalWiwikey;
import com.wiwikeyandroid.utils.PrefUtils;
import com.wiwikeyandroid.utils.Tools;

/**
 * @author Joseph
 * @date 2016-8-1 来访预约
 */
public class VisitOrderActivity extends BaseActivity implements ResponseListener {
	@InjectView(R.id.visit_order_back)
	Button mBack;
	@InjectView(R.id.select_user) 
	Button mSelect;
	@InjectView(R.id.visit_order_name_text)
	EditText et_name;
	@InjectView(R.id.visit_order_phone_text)
	EditText et_phone;
	@InjectView(R.id.visit_order_start_text)
	TextView mOrderTime;
	@InjectView(R.id.visit_order_commit)
	Button mCommit;

	@Override
	protected int initContentView() {
		return R.layout.family_visit_order_activity;
	}

	@OnClick({ R.id.visit_order_back, R.id.select_user,
			R.id.visit_order_start_text, R.id.visit_order_commit})
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.visit_order_back: // 返回
			finish();
			break;
		case R.id.select_user: // 选择联系人
			startActivityForResult(new Intent(Intent.ACTION_PICK,
					ContactsContract.Contacts.CONTENT_URI), 0);

			break;
		case R.id.visit_order_start_text: // 预约时间
			String initEndDateTime = "";
			DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(
					VisitOrderActivity.this, initEndDateTime);
			dateTimePicKDialog.dateTimePicKDialog(mOrderTime);
			

			break;
		case R.id.visit_order_commit: // 提交 
			String vistorName = et_name.getText().toString().trim();
			vistorPhone = et_phone.getText().toString().trim(); 
			startTime = mOrderTime.getText().toString().trim(); 
			
			if (TextUtils.isEmpty(vistorName)) { 
				Toast.makeText(this, "请输入姓名！", Toast.LENGTH_SHORT)
						.show();
				return;
			}

			if (!Tools.isPhoneNum(vistorPhone)) {
				Toast.makeText(this, "手机号错误！", Toast.LENGTH_SHORT)
						.show();
				return;
			}

			if (TextUtils.isEmpty(startTime)) {
				Toast.makeText(this, "请选择起始时间", Toast.LENGTH_SHORT)
						.show();
				return;
			}
			long sTime = DateUtils.formatToLong(startTime,"yyyy-MM-dd HH:mm");  
			if (sTime<=System.currentTimeMillis()) {
				Toast.makeText(VisitOrderActivity.this, "开始时间不能早于当前时间",
						Toast.LENGTH_LONG).show();
				mOrderTime.setText(" ");
				return;
			}
			i = (int)((Math.random()*9+1)*100000); 
			Map<String, String> params = new HashMap<String, String>();
			params.put("password",i+"");
			params.put("memberId",
					PrefUtils.getInt(this, "memberId", -1) + "");
			params.put("token",
					PrefUtils.getString(this, "token", ""));
			HttpLoader.post(GlobalWiwikey.URL_INSERTOPENPASS, params , GetVerifyCodeBean.class, GlobalWiwikey.REQUEST_CANCELREPAIR, this);
			
			break;

		default:
			break;
		}
	}

	@Override
	protected void initView() {

	}

	@Override
	protected void initListener() {

	}

	@Override
	protected void initData() {

	}

	private String name;
	private String phone;
	private String startTime;
	private String vistorPhone;
	private int i;

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
								.getString(phones
										.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).replace(" ", "");;
							
						if (!Tools.isPhoneNum(phone)) {
							phone = "";
						}
					}
					et_name.setText(name);
					et_phone.setText(phone);
					
					phones.close();
				}
				cursor.close();

			}
			break;
		}
	}

	@Override
	public void onGetResponseSuccess(int requestCode, RBResponse response) {
		GetVerifyCodeBean bean = (GetVerifyCodeBean) response;
		if (bean.getErrmsg().equals("SUCCESS")&&bean.getErrno()==0) {
		Intent intent = new Intent(this, ShareOrderPasswordActivity.class);
		Bundle data = new Bundle();
		data.putString("startTime", startTime);
		data.putString("visitPhone", vistorPhone);
		data.putString("passWord", i+"");
		intent.putExtras(data);
		startActivity(intent);
		finish();
		}
		
	}

	@Override
	public void onGetResponseError(int requestCode, VolleyError error) {
		// TODO Auto-generated method stub
		
	}
}
