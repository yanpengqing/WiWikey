package com.wiwikeyandroid.modules.family;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.seny.android.utils.ALog;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wiwikeyandroid.R;
import com.wiwikeyandroid.adapter.ImagePublishAdapter;
import com.wiwikeyandroid.agora.MsgEvent;
import com.wiwikeyandroid.bean.AddRepairComplainBean;
import com.wiwikeyandroid.bean.ImageItem;
import com.wiwikeyandroid.bean.RBResponse;
import com.wiwikeyandroid.net.HttpLoader;
import com.wiwikeyandroid.net.HttpLoader.ResponseListener;
import com.wiwikeyandroid.utils.GlobalWiwikey;
import com.wiwikeyandroid.utils.IntentConstants;
import com.wiwikeyandroid.utils.PrefUtils;
import com.wiwikeyandroid.view.ActionSheetDialog;
import com.wiwikeyandroid.view.ActionSheetDialog.OnSheetItemClickListener;
import com.wiwikeyandroid.view.ActionSheetDialog.SheetItemColor;

import de.greenrobot.event.EventBus;

public class ComplainCommitActivity extends Activity implements
		OnClickListener, ResponseListener {

	private EditText itemSubject;
	private EditText address;
	private EditText content;
	private Button complaint_commit;
	private Button complain_back;
	private GridView mGridView;
	private ImagePublishAdapter mAdapter;
	private Gson gson;
	public static List<ImageItem> mDataList = new ArrayList<ImageItem>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.complain_submit_activity);
		mDataList = new ArrayList<ImageItem>();
		gson = new Gson();
		initData();
		initView();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		getTempFromPref();
		List<ImageItem> incomingDataList = (List<ImageItem>) intent
				.getSerializableExtra(IntentConstants.EXTRA_IMAGE_LIST);
		if (incomingDataList != null) {
			mDataList.addAll(incomingDataList);
		}
		initView();
		notifyDataChanged();

	}
	@Override
	protected void onResume() {
		super.onResume();
		notifyDataChanged(); // 当在ImageZoomActivity中删除图片时，返回这里需要刷新
	}
	private void notifyDataChanged() {
		mAdapter.notifyDataSetChanged();
	}
	@SuppressWarnings("unchecked")
	private void initData() {
		getTempFromPref();
		List<ImageItem> incomingDataList = (List<ImageItem>) getIntent()
				.getSerializableExtra(IntentConstants.EXTRA_IMAGE_LIST);
		if (incomingDataList != null) {
			mDataList.addAll(incomingDataList);
		}
	}

	private void getTempFromPref() {
		SharedPreferences sp = getSharedPreferences(
				GlobalWiwikey.APPLICATION_NAME, MODE_PRIVATE);
		String prefStr = sp.getString(GlobalWiwikey.PREF_TEMP_IMAGES, null);
		if (!TextUtils.isEmpty(prefStr)) {
			List<ImageItem> tempImages = gson.fromJson(prefStr,
					new TypeToken<List<ImageItem>>() {
					}.getType());
			mDataList = tempImages;
		}
	}

	private void initView() {
		itemSubject = (EditText) findViewById(R.id.complain_subject);
		address = (EditText) findViewById(R.id.complain_address);
		content = (EditText) findViewById(R.id.complaint_content);
		complaint_commit = (Button) findViewById(R.id.complaint_commit);
		complain_back = (Button) findViewById(R.id.complain_back);
		mGridView = (GridView) findViewById(R.id.gridview);
		mGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		mAdapter = new ImagePublishAdapter(this, mDataList);
		complain_back.setOnClickListener(this);
		complaint_commit.setOnClickListener(this);
		mGridView.setAdapter(mAdapter);
		mGridView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position == getDataSize()) {
					selectPhotoDialog();
				} else {
					Intent intent = new Intent(ComplainCommitActivity.this,
							ImageZoomActivity.class);
					intent.putExtra(IntentConstants.EXTRA_IMAGE_LIST,
							"Complain");
					intent.putExtra(IntentConstants.EXTRA_CURRENT_IMG_POSITION,
							position);
					startActivity(intent);
				}
			}
		});
	}
	protected void onPause() {
		super.onPause();
		saveTempToPref();
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		removeTempFromPref();
	}
	private void removeTempFromPref() {
		SharedPreferences sp = getSharedPreferences(
				GlobalWiwikey.APPLICATION_NAME, MODE_PRIVATE);
		sp.edit().remove(GlobalWiwikey.PREF_TEMP_IMAGES).commit();
	}
	
	private void saveTempToPref() {
		SharedPreferences sp = getSharedPreferences(
				GlobalWiwikey.APPLICATION_NAME, MODE_PRIVATE);
		String prefStr = gson.toJson(mDataList);
		sp.edit().putString(GlobalWiwikey.PREF_TEMP_IMAGES, prefStr).commit();

	}
	// 从底部弹出自定义dialog
	private void selectPhotoDialog() {
		new ActionSheetDialog(this)
				.builder()
				.setCancelable(false)
				.setCanceledOnTouchOutside(true)
				.addSheetItem("拍照", SheetItemColor.Black,
						new OnSheetItemClickListener() {
							@Override
							public void onClick(int which) {
								takePhoto();

							}
						})
				.addSheetItem("从相册中选取", SheetItemColor.Black,
						new OnSheetItemClickListener() {
							@Override
							public void onClick(int which) {
								Intent intent = new Intent(
										ComplainCommitActivity.this,
										ImageBucketChooseActivity.class);
								intent.putExtra(
										IntentConstants.EXTRA_CAN_ADD_IMAGE_SIZE,
										getAvailableSize());
								startActivity(intent);
							}
						}).show();
	}

	private static final int TAKE_PICTURE = 0x000000;
	private String path = "";

	public void takePhoto() {
		Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		File vFile = new File(Environment.getExternalStorageDirectory()
				+ "/myimage/", String.valueOf(System.currentTimeMillis())
				+ ".jpg");
		if (!vFile.exists()) {
			File vDirPath = vFile.getParentFile();
			vDirPath.mkdirs();
		} else {
			if (vFile.exists()) {
				vFile.delete();
			}
		}
		path = vFile.getPath();
		Uri cameraUri = Uri.fromFile(vFile);
		openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
		startActivityForResult(openCameraIntent, TAKE_PICTURE);
	}

	private int getDataSize() {
		return mDataList == null ? 0 : mDataList.size();
	}

	private int getAvailableSize() {
		int availSize = GlobalWiwikey.MAX_IMAGE_SIZE - mDataList.size();
		if (availSize >= 0) {
			return availSize;
		}
		return 0;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.complain_back:
			finish();
			break;

		case R.id.complaint_commit:
			String mSubject = itemSubject.getText().toString().trim();
			if (mSubject == null || mSubject.equals("")) {
				Toast.makeText(ComplainCommitActivity.this, "请填写投诉主题！",
						Toast.LENGTH_LONG).show();
				return;
			}
			String mAddress = address.getText().toString().trim();
			if (mAddress == null || mAddress.equals("")) {
				Toast.makeText(ComplainCommitActivity.this, "请填写投诉地点！",
						Toast.LENGTH_LONG).show();
				return;
			}
			String mContent = content.getText().toString().trim();
			if (mContent == null || mContent.equals("")) {
				Toast.makeText(ComplainCommitActivity.this, "请填写投诉说明！",
						Toast.LENGTH_LONG).show();
				return;
			}

			Map<String, String> params = new HashMap<String, String>();
			params.put("subject", mSubject);
			params.put("addr", mAddress);
			params.put("content", mContent);
			params.put(
					"houseId",
					PrefUtils.getInt(ComplainCommitActivity.this, "houseId0",
							-1) + "");
			params.put(
					"memberId",
					PrefUtils.getInt(ComplainCommitActivity.this, "memberId",
							-1) + "");
			params.put("token", PrefUtils.getString(
					ComplainCommitActivity.this, "token", ""));
			HttpLoader.post(GlobalWiwikey.URL_ADDCOMPLAIN, params,
					AddRepairComplainBean.class,
					GlobalWiwikey.REQUEST_ADDCOMPLAIN,
					ComplainCommitActivity.this);

			break;

		default:
			break;
		}
	}

	@Override
	public void onGetResponseSuccess(int requestCode, RBResponse response) {
		switch (requestCode) {
		case GlobalWiwikey.REQUEST_ADDCOMPLAIN:
			AddRepairComplainBean bean = (AddRepairComplainBean) response;
			if (bean.getErrmsg().equals("SUCCESS")) {
				ALog.e("投诉成功：" + bean.getComplainId());
				
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
