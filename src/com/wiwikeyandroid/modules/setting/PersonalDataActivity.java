package com.wiwikeyandroid.modules.setting;

import java.io.File;

import org.seny.android.utils.ALog;
import org.seny.android.utils.ActivityUtils;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.InjectView;
import butterknife.OnClick;

import com.wiwikeyandroid.R;
import com.wiwikeyandroid.activity.HomeAcivity;
import com.wiwikeyandroid.agora.MsgEvent;
import com.wiwikeyandroid.base.BaseActivity;
import com.wiwikeyandroid.modules.AuthenticationOneActivity;
import com.wiwikeyandroid.utils.PrefUtils;
import com.wiwikeyandroid.view.ActionSheetDialog;
import com.wiwikeyandroid.view.ActionSheetDialog.OnSheetItemClickListener;
import com.wiwikeyandroid.view.ActionSheetDialog.SheetItemColor;

import de.greenrobot.event.EventBus;

/**
 * @author Joseph
 * @date 2016-7-30 个人资料展示
 */
public class PersonalDataActivity extends BaseActivity {

	@InjectView(R.id.self_info_back)
	Button mBack;
	@InjectView(R.id.self_info_name_text)
	TextView mName;
	@InjectView(R.id.self_info_sex_text)
	TextView mSex;
	@InjectView(R.id.self_info_phone_text)
	TextView mPhone;
	@InjectView(R.id.self_info_type_text)
	TextView mType;
	@InjectView(R.id.self_info_plot_text)
	TextView mPlot;
	@InjectView(R.id.self_info_sign_text)
	TextView mSign;
	@InjectView(R.id.iv_arrow_right2)
	ImageView img_arrow;
	@InjectView(R.id.head_img)
	ImageView img_pic;
	@InjectView(R.id.self_info_head)
	// 头像
	RelativeLayout rlHead;
	@InjectView(R.id.self_info_name)
	// 姓名
	RelativeLayout rlName;
	@InjectView(R.id.self_info_sex)
	// 性别
	RelativeLayout rlSex;
	@InjectView(R.id.self_info_plot)
	// 所在小区
	RelativeLayout rlPlot;
	@InjectView(R.id.self_info_sign)
	// 个性签名
	RelativeLayout rlSign;

	private static final int FLAG_CHOOSE_CAMERA = 1;
	private static final int FLAG_CHOOSE_IMG = 2;
	private static final int FLAG_MODIFY_FINISH = 3;
	public static final String TMP_PATH = "temp.jpg";

	@Override
	protected int initContentView() {

		return R.layout.personal_data_activity;
	};

	@Override
	protected void initView() {
		EventBus.getDefault().register(this);
	}

	@Override
	protected void initData() {
		String realname = PrefUtils.getString(this, "realname", "");
		if (!realname.isEmpty()) {
			// if (realname.length() == 2) {
			// mName.setText(PrefUtils.getString(this, "realname", "")
			// .substring(0, 1) + "*");
			// } else {
			// StringBuilder sb = new StringBuilder(realname);
			// StringBuilder newname = sb.replace(1, sb.length() - 1, "*");
			// mName.setText(newname);
			// }
			mName.setText(realname);
			mName.setTextColor(getResources().getColor(R.color.sub_head));
		} else {
			mName.setText("未填写");
			mName.setTextColor(getResources().getColor(R.color.colorHint));
		}
		mSex.setText(PrefUtils.getInt(this, "sex", -1) == 0 ? "女" : "男");
		String mobile = PrefUtils.getString(this, "mobile", "");
		// String substr = mobile.replaceAll("(\\d{3})\\d{4}(\\d{4})",
		// "$1****$2");
		mPhone.setText(mobile);
		if (!PrefUtils.getString(this, "memberHouseList0", "").isEmpty()) {
			mPlot.setText(PrefUtils.getString(this, "memberHouseList0", "")
					.split("#")[0]);
			img_arrow.setVisibility(View.GONE);
			rlPlot.setClickable(false);
		} else {
			mPlot.setText("未认证");
			mPlot.setTextColor(getResources().getColor(R.color.colorHint));
		}

		mSign.setText(PrefUtils.getString(this, "signature", "未填写").isEmpty()?"未填写":PrefUtils.getString(this, "signature", "未填写"));
		mSign.setTextColor(PrefUtils.getString(this, "signature", "").isEmpty() ? getResources()
				.getColor(R.color.colorHint) : getResources().getColor(
				R.color.sub_head));

		Bitmap b = BitmapFactory.decodeFile(PrefUtils.getString(this,
				"headPath", ""));
		if (b != null) {
			img_pic.setImageBitmap(b);
		}
		int holdType = PrefUtils.getInt(this, "householdType0", -1);
		switch (holdType) {
		case 1:
			mType.setText("业主");
			break;

		case 2:
			mType.setText("家庭成员");
			break;
		case 4:
			mType.setText("租客");
			break;
		case 5:
			mType.setText("其他");
			break;
		default:
			mType.setText("其他");
			break;
		}
	}

	@OnClick({ R.id.self_info_sign, R.id.self_info_back, R.id.self_info_head,
			R.id.self_info_plot, R.id.self_info_name, R.id.self_info_sex })
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.self_info_back: //
			finish();

			break;
		case R.id.self_info_head:
			showSelectPictureDiolag();
			break;
		case R.id.self_info_name:
			ActivityUtils.startActivity(this,
					PersonalModifyRealNameActivity.class);
			break;
		case R.id.self_info_sex:
			ActivityUtils.startActivity(this, PersonalModifySexActivity.class);

			break;
		case R.id.self_info_plot:
			ActivityUtils.startActivity(this, AuthenticationOneActivity.class);

			break;

		case R.id.self_info_sign: // 个性签名
			ActivityUtils.startActivity(this, PersonalSignatureActivity.class);

			break;

		}
	}

	/**
	 * 弹出选择图片窗口
	 */
	private void showSelectPictureDiolag() {
		new ActionSheetDialog(this)
				.builder()
				.setCancelable(false)
				.setCanceledOnTouchOutside(true)
				.addSheetItem("拍照", SheetItemColor.Black,
						new OnSheetItemClickListener() {
							@Override
							public void onClick(int which) {
								openCamera();

							}
						})
				.addSheetItem("从相册中选取", SheetItemColor.Black,
						new OnSheetItemClickListener() {
							@Override
							public void onClick(int which) {
								openPicture();

							}
						}).show();
	}

	/**
	 * 从本地图片获取
	 */
	protected void openPicture() {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_PICK);
		intent.setType("image/*");
		startActivityForResult(intent, FLAG_CHOOSE_IMG);
	}

	/**
	 * 从相机拍照获取
	 */
	protected void openCamera() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
				Environment.getExternalStorageDirectory(), TMP_PATH)));
		startActivityForResult(intent, FLAG_CHOOSE_CAMERA);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == FLAG_CHOOSE_IMG && resultCode == RESULT_OK) {
			if (data != null) {
				Uri uri = data.getData();
				if (!TextUtils.isEmpty(uri.getAuthority())) {
					Cursor cursor = getContentResolver().query(uri,
							new String[] { MediaStore.Images.Media.DATA },
							null, null, null);
					if (null == cursor) {
						Toast.makeText(getApplicationContext(), "图片没找到", 0)
								.show();
						return;
					}
					cursor.moveToFirst();
					String path = cursor.getString(cursor
							.getColumnIndex(MediaStore.Images.Media.DATA));
					cursor.close();

					Intent intent = new Intent(this, CutPictureActivity.class);
					intent.putExtra("path", path);
					startActivityForResult(intent, FLAG_MODIFY_FINISH);
				} else {
					Intent intent = new Intent(this, CutPictureActivity.class);
					intent.putExtra("path", uri.getPath());
					startActivityForResult(intent, FLAG_MODIFY_FINISH);
				}
			}
		} else if (requestCode == FLAG_MODIFY_FINISH && resultCode == RESULT_OK) {
			if (data != null) {
				final String path = data.getStringExtra("path");
				ALog.d("头像路径：" + path);
				PrefUtils.setString(this, "headPath", path);
				Bitmap b = BitmapFactory.decodeFile(path);
				img_pic.setImageBitmap(b);
				EventBus.getDefault().post(new MsgEvent("signature"));
			}
		}
		switch (requestCode) {
		case FLAG_CHOOSE_CAMERA:
			// 照相机程序返回的,再次调用图片剪辑程序去修剪图片
			startCropImageActivity(Environment.getExternalStorageDirectory()
					+ "/" + TMP_PATH);
			break;
		}
	}

	// 裁剪图片的Activity
	private void startCropImageActivity(String path) {
		Intent intent = new Intent(this, CutPictureActivity.class);
		intent.putExtra("path", path);
		startActivityForResult(intent, FLAG_MODIFY_FINISH);
	}

	public void onEventMainThread(MsgEvent event) {
		initData();
	}

	@Override
	protected void initListener() {

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}
}
