package com.wiwikeyandroid.modules.family;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.seny.android.utils.ALog;
import org.springframework.util.StreamUtils;

import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wiwikeyandroid.R;
import com.wiwikeyandroid.adapter.ImagePublishAdapter;
import com.wiwikeyandroid.adapter.RepairsListAdapter;
import com.wiwikeyandroid.agora.MsgEvent;
import com.wiwikeyandroid.bean.AddRepairComplainBean;
import com.wiwikeyandroid.bean.ImageItem;
import com.wiwikeyandroid.bean.RBResponse;
import com.wiwikeyandroid.net.HttpLoader;
import com.wiwikeyandroid.net.HttpLoader.ResponseListener;
import com.wiwikeyandroid.net.MultipartRequest;
import com.wiwikeyandroid.utils.DesUtil;
import com.wiwikeyandroid.utils.GlobalWiwikey;
import com.wiwikeyandroid.utils.IntentConstants;
import com.wiwikeyandroid.utils.PrefUtils;
import com.wiwikeyandroid.view.ActionSheetDialog;
import com.wiwikeyandroid.view.ActionSheetDialog.OnSheetItemClickListener;
import com.wiwikeyandroid.view.ActionSheetDialog.SheetItemColor;

import de.greenrobot.event.EventBus;

public class RepairsCommitActivity extends Activity implements ResponseListener {

	private GridView mGridView;
	private ImagePublishAdapter mAdapter;
	private Button mBtCommit;
	private Button mBtBack;
	private Gson gson;
	private RelativeLayout rlItem;
	private TextView tvItem;
	private EditText etAddress;
	private EditText etDesc;
	private EditText etSubject;
	private PopupWindow window;
	private View mView;
	private TextView areaType_private;
	private TextView areaType_public;

	private ExpandableListView mListView;
	private RepairsListAdapter adapter;

	String[][] child_text = { { "冰箱", "电视", "电脑", "电灯", "桌子" },
			{ "厕所", "长凳", "电线", "垃圾桶", "景观水池", "路灯/健身器材", "电梯" } };
	String[] list = new String[] { "私有报修", "公有报修" };

	int[] LISTVIEW_IMG = new int[] { R.drawable.repairs_private,
			R.drawable.repairs_public };
	private List<Map<String, Object>> lists;

	public static List<ImageItem> mDataList = new ArrayList<ImageItem>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.repairs_commit);
		EventBus.getDefault().postSticky(new MsgEvent("RepairsCommitActivity"));
		mDataList = new ArrayList<ImageItem>();
		gson = new Gson();
		initData();
		initView();
		// setListener();
	}

	private void setListener() {

		mListView.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				tvItem.setText(child_text[groupPosition][childPosition]);
				if (window != null) {
					window.dismiss();
				}
				return true;
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
		MsgEvent stickyEvent = EventBus.getDefault().getStickyEvent(
				MsgEvent.class);
		if (stickyEvent != null) {
			EventBus.getDefault().removeStickyEvent(stickyEvent);
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		saveTempToPref();
	}

	private void saveTempToPref() {
		SharedPreferences sp = getSharedPreferences(
				GlobalWiwikey.APPLICATION_NAME, MODE_PRIVATE);
		String prefStr = gson.toJson(mDataList);
		sp.edit().putString(GlobalWiwikey.PREF_TEMP_IMAGES, prefStr).commit();

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
			ALog.d("getTempFromPref:" + mDataList.size());
		}
	}

	private void removeTempFromPref() {
		SharedPreferences sp = getSharedPreferences(
				GlobalWiwikey.APPLICATION_NAME, MODE_PRIVATE);
		sp.edit().remove(GlobalWiwikey.PREF_TEMP_IMAGES).commit();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		EventBus.getDefault().postSticky(new MsgEvent("RepairsCommitActivity"));
		setIntent(intent);
		// String ringName = intent.getStringExtra("ringName");
		getTempFromPref();
		List<ImageItem> incomingDataList = (List<ImageItem>) intent
				.getSerializableExtra(IntentConstants.EXTRA_IMAGE_LIST);
		if (incomingDataList != null) {
			mDataList.addAll(incomingDataList);
		}
		initView();
		notifyDataChanged();

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

	@Override
	protected void onResume() {
		super.onResume();
		notifyDataChanged(); // 当在ImageZoomActivity中删除图片时，返回这里需要刷新
	}

	public void initView() {
		mView = View.inflate(this, R.layout.item_area_type_layout, null);
		areaType_private = (TextView) mView.findViewById(R.id.areaType_private);
		areaType_public = (TextView) mView.findViewById(R.id.areaType_public);
		areaType_private.setOnClickListener(onClickListener);
		areaType_public.setOnClickListener(onClickListener);

		// mListView = (ExpandableListView) mView.findViewById(R.id.list);
		// lists = new ArrayList<Map<String, Object>>(); for (int i = 0; i <
		// list.length; i++) { Map<String, Object> map = new HashMap<String,
		// Object>(); map.put("img", LISTVIEW_IMG[i]); map.put("txt", list[i]);
		// lists.add(map); } adapter = new RepairsListAdapter(this, lists,
		// child_text); mListView.setAdapter(adapter);

		rlItem = (RelativeLayout) this.findViewById(R.id.repairs_apply_item);
		rlItem.setOnClickListener(onClickListener);

		tvItem = (TextView) this.findViewById(R.id.repairs_apply_item_name);
		etAddress = (EditText) this
				.findViewById(R.id.repairs_apply_address_content);
		etDesc = (EditText) this.findViewById(R.id.repairs_apply_desc_content);
		etSubject = (EditText) findViewById(R.id.repairs_subject_content);
		mBtBack = (Button) findViewById(R.id.repairs_back);
		mBtCommit = (Button) findViewById(R.id.repairs_commit);
		mBtCommit.setOnClickListener(onClickListener);
		mGridView = (GridView) findViewById(R.id.gridview);
		mGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		mAdapter = new ImagePublishAdapter(this, mDataList);
		mGridView.setAdapter(mAdapter);
		mGridView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position == getDataSize()) {
					selectPhotoDialog();
				} else {
					Intent intent = new Intent(RepairsCommitActivity.this,
							ImageZoomActivity.class);
					intent.putExtra(IntentConstants.EXTRA_IMAGE_LIST, "");
					intent.putExtra(IntentConstants.EXTRA_CURRENT_IMG_POSITION,
							position);
					startActivity(intent);
				}
			}
		});
		mBtBack.setOnClickListener(onClickListener);
	}

	private OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {

			switch (v.getId()) {
			case R.id.repairs_back:
				finish();
				break;

			case R.id.repairs_apply_item:

				ShowPopup();

				break;

			case R.id.areaType_private:
				tvItem.setText("私有区域");
				if (window != null) {
					window.dismiss();
				}

				break;

			case R.id.areaType_public:
				tvItem.setText("公有区域");
				if (window != null) {
					window.dismiss();
				}

				break;

			case R.id.repairs_commit:

				// 这边以mDataList为来源做上传
				removeTempFromPref();
				String itemName = tvItem.getText().toString();

				if (itemName == null || itemName.equals("")) {
					Toast.makeText(RepairsCommitActivity.this, "请选择区域类别！",
							Toast.LENGTH_LONG).show();
					return;
				}

				String itemSubject = etSubject.getText().toString();

				if (itemSubject == null || itemSubject.equals("")) {
					Toast.makeText(RepairsCommitActivity.this, "请填写报修项目的主题！",
							Toast.LENGTH_LONG).show();
					return;
				}

				String address = etAddress.getText().toString();
				if (address == null || address.equals("")) {
					Toast.makeText(RepairsCommitActivity.this, "请填写具体地址！",
							Toast.LENGTH_LONG).show();
					return;
				}

				String desc = etDesc.getText().toString();

				if (desc == null || desc.equals("")) {
					Toast.makeText(RepairsCommitActivity.this, "请填写简要情况！",
							Toast.LENGTH_LONG).show();
					return;
				}

				for (int i = 0; i < mDataList.size(); i++) {
					ALog.d("sourcePath:" + mDataList.get(i).sourcePath
							+ "||thumbnailPath:"
							+ mDataList.get(i).thumbnailPath);
				}

				Map<String, String> params = new HashMap<String, String>();
				params.put("subject", itemSubject);
				params.put("position", address);
				params.put("content", desc);
				params.put("areaType", itemName.equals("私有区域") ? 1 + ""
						: 2 + "");
				params.put(
						"houseId",
						PrefUtils.getInt(RepairsCommitActivity.this,
								"houseId0", -1) + "");
				ALog.d(PrefUtils.getInt(RepairsCommitActivity.this, "houseId0",
						-1) + "");
				params.put(
						"memberId",
						PrefUtils.getInt(RepairsCommitActivity.this,
								"memberId", -1) + "");
				params.put("token", PrefUtils.getString(
						RepairsCommitActivity.this, "token", ""));
				HttpLoader.post(GlobalWiwikey.URL_REPAIRREPORT, params,
						AddRepairComplainBean.class,
						GlobalWiwikey.REQUEST_REPAIRREPORT,
						RepairsCommitActivity.this);

				break;
			}
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
			window.showAsDropDown(rlItem, 0, 0);

		}
	};

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

	public String getString(String s) {
		String path = null;
		if (s == null)
			return "";
		for (int i = s.length() - 1; i > 0; i++) {
			s.charAt(i);
		}
		return path;
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
										RepairsCommitActivity.this,
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

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case TAKE_PICTURE:
			if (mDataList.size() < GlobalWiwikey.MAX_IMAGE_SIZE
					&& resultCode == -1 && !TextUtils.isEmpty(path)) {
				ImageItem item = new ImageItem();
				item.sourcePath = path;
				mDataList.add(item);
			}
			break;
		}
	}

	private void notifyDataChanged() {
		mAdapter.notifyDataSetChanged();
	}

	@Override
	public void onGetResponseSuccess(int requestCode, RBResponse response) {
		switch (requestCode) {
		case GlobalWiwikey.REQUEST_REPAIRREPORT:
			AddRepairComplainBean bean = (AddRepairComplainBean) response;
			if (bean.getErrmsg().equals("SUCCESS")) {
				ALog.e("报修数据提交成功：" + bean.getRepairId() + "图片"
						+ mDataList.size() + "张");
				// finish();
				if (mDataList.size() > 0) {
					doUpload(bean.getRepairId());
				}

			}
			break;

		default:
			break;
		}
	}

	/**
	 * 上传文件
	 * 
	 * @param id
	 *            服务器返回的id
	 */
	private void doUpload(int id) {
		RequestQueue mSingleQueue = Volley
				.newRequestQueue(RepairsCommitActivity.this);
		Map<String, String> params = new HashMap<String, String>();
		params.put("memberId",
				PrefUtils.getInt(RepairsCommitActivity.this, "memberId", -1)
						+ "");
		params.put("token",
				PrefUtils.getString(RepairsCommitActivity.this, "token", ""));
		List<File> f = new ArrayList<File>();
		List<String> mFileName = new ArrayList<String>();
		int pos = 1;
		for (int i = 0; i < mDataList.size(); i++) {
			File oldfile = new File(mDataList.get(i).sourcePath);  
			mFileName.add("bsbx_["
					 + id
					 + "]_"
					 + System.currentTimeMillis()+pos
					 + mDataList.get(i).sourcePath.substring(mDataList
					 .get(i).sourcePath.lastIndexOf(".")));
			f.add(oldfile);
			pos+=1;
		}
		MultipartRequest request = new MultipartRequest(
				GlobalWiwikey.URL_UPLOADIMG, new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						try {
							ALog.d("图片上传返回数据："
									+ DesUtil.decrypt(response, DesUtil.DESKEY));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError response) {
						ALog.d("图片上传失败：");

					}
				}, "f_file[]",mFileName, f, params);
		mSingleQueue.add(request);
	}

	/**
	 * 文件重命名
	 * 
	 * @param path
	 *            文件目录
	 * @param newname
	 *            新文件名
	 */
//	public File renameFile(String path, String newname) {
//		File oldfile = new File(path);
//		File newfile = new File(path.substring(0, path.lastIndexOf("/")) + "/"
//				+ newname);
//		boolean renameTo = oldfile.renameTo(newfile);
//
//		ALog.d("修改" + renameTo + "||" + oldfile.getAbsolutePath() + "::"
//				+ newfile.getAbsolutePath());
//		return newfile;
//	}

	@Override
	public void onGetResponseError(int requestCode, VolleyError error) {

	}
}
