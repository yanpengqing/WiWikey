package com.wiwikeyandroid.modules.setting;

import java.util.HashMap;
import java.util.Map;

import org.seny.android.utils.ALog;
import org.seny.android.utils.ActivityUtils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.android.volley.VolleyError;
import com.wiwikeyandroid.R;
import com.wiwikeyandroid.agora.MsgEvent;
import com.wiwikeyandroid.base.BaseFragment;
import com.wiwikeyandroid.bean.MemberByMobileBean;
import com.wiwikeyandroid.bean.RBResponse;
import com.wiwikeyandroid.net.HttpLoader;
import com.wiwikeyandroid.net.HttpLoader.ResponseListener;
import com.wiwikeyandroid.utils.GlobalWiwikey;
import com.wiwikeyandroid.utils.PrefUtils;
import com.wiwikeyandroid.view.PullScrollView.OnTurnListener;

import de.greenrobot.event.EventBus;

/**
 * @author Joseph on 2016/5/22.
 *         <p/>
 */
public class SettingFragment extends BaseFragment implements OnTurnListener {
	@InjectView(R.id.rl_tenement)
	// 住户登记
	RelativeLayout rl_tenement;
	@InjectView(R.id.tv_setting_remain_time)
	TextView tvSettingRemainTime;
	@InjectView(R.id.tv_setting_name)
	TextView tvSettingName;
	@InjectView(R.id.tv_setting_signa)
	TextView tv_setting_signa;
	@InjectView(R.id.tv_setting_score_exchange)
	TextView tvSettingScoreExchange;
	@InjectView(R.id.tv_personal_data_name)
	TextView tvPersonalDataName;
	@InjectView(R.id.rl_personal_data)
	RelativeLayout rlPersonalData;
	@InjectView(R.id.rl_about_question)
	RelativeLayout rlAboutQuestion;
	@InjectView(R.id.iv_icon_avatar)
	ImageView img_pic;
	@InjectView(R.id.rl_about)
	RelativeLayout rlAbout;
	@InjectView(R.id.rl_system_setting)
	RelativeLayout rlSystemSetting;

	// @InjectView(R.id.rl_avatar)
	// ImageView mAvatar;
	// @InjectView(R.id.layout)
	// PullPushLayout mLayout;

	@Override
	public View CreateView() {
		ALog.d("SettingFragment:CreateView()");
		View mSettingView = View.inflate(mActivity,
				R.layout.setting_navigation, null);
		ButterKnife.inject(this, mSettingView);
		EventBus.getDefault().register(this);
		return mSettingView;

	}

	@Override
	public void initData() {
		tv_setting_signa.setText(PrefUtils.getString(mActivity, "signature",
				"个性签名").equals("")?"个性签名":PrefUtils.getString(mActivity, "signature",
						"个性签名"));
		Bitmap b = BitmapFactory.decodeFile(PrefUtils.getString(mActivity,
				"headPath", ""));
		if (b != null) {
			img_pic.setImageBitmap(getRoundedCornerBitmap(b));
		}

		if (!PrefUtils.getString(mActivity, "realname", "").isEmpty()) {
			tvSettingName.setText(PrefUtils
					.getString(mActivity, "realname", ""));
		}else if (!PrefUtils.getString(mActivity, "nickname", "").isEmpty()) {
			tvSettingName.setText(PrefUtils
					.getString(mActivity, "nickname", ""));
			
		} else if (PrefUtils.getBoolean(mActivity, "IsSetPass", false)) {

			new Thread(new Runnable() {
				public void run() {
					Map<String, String> params = new HashMap<String, String>();
					params.put("mobile",
							PrefUtils.getString(mActivity, "mobile", ""));
					params.put("memberId",
							PrefUtils.getInt(mActivity, "memberId", -1) + "");

					params.put("token",
							PrefUtils.getString(mActivity, "token", ""));
					HttpLoader.post(GlobalWiwikey.URL_GETMEMBERBYMOBILE,
							params, MemberByMobileBean.class,
							GlobalWiwikey.REQUEST_GETMEMBERBYMOBILE,
							new ResponseListener() {

								@Override
								public void onGetResponseSuccess(
										int requestCode, RBResponse response) {
									MemberByMobileBean bean = (MemberByMobileBean) response;
									if (bean.getErrmsg().equals("SUCCESS")) { //
										PrefUtils.setString(mActivity,
												"nickname", bean
														.getMemberModel()
														.getNickname());
										tvSettingName.setText(bean.getMemberModel().getNickname());
									}

								}

								@Override
								public void onGetResponseError(int requestCode,
										VolleyError error) {

								}
							});
				}
			}).start();
		}else {
			if (PrefUtils.getInt(mActivity, "memberType", -1) == 2) {
				tvSettingName.setText("游客");
			}else {
				tvSettingName.setText(PrefUtils.getString(mActivity, "mobile", "").replaceAll("(\\d{3})\\d{4}(\\d{4})",
						 "$1****$2"));
			}
			
		}
	}

	// 圓角
	private Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
		Bitmap roundBitmap = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(roundBitmap);
		int color = 0xff424242;
		Paint paint = new Paint();
		Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		RectF rectF = new RectF(rect);
		float roundPx = 50;
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return roundBitmap;
	}

	@OnClick({ R.id.rl_tenement, R.id.rl_personal_data,
			R.id.tv_setting_score_exchange, R.id.rl_about_question,
			R.id.rl_about, R.id.rl_system_setting })
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.rl_personal_data:// 个人资料
			ActivityUtils.startActivity(mActivity, PersonalDataActivity.class);
			break;
		case R.id.rl_tenement: // 住户登记
			ActivityUtils.startActivity(mActivity, HouseOwnerRegActivity.class);
			break;
		case R.id.tv_setting_score_exchange:// 积分兑换

			break;
		case R.id.rl_about_question:// 常见问题
			ActivityUtils.startActivity(mActivity, AboutQuestionActivity.class);

			break;
		case R.id.rl_about: // 关于唯家
			ActivityUtils.startActivity(mActivity, AboutWiWiKeyActivity.class);
			break;
		case R.id.rl_system_setting:// 系统设置
			ActivityUtils.startActivity(mActivity, SystemSettingActivity.class);

			break;
		}
	}

	public void onEventMainThread(MsgEvent event) {
		initData();
	}

	@Override
	public boolean onBackPressed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		ButterKnife.reset(this);
		EventBus.getDefault().unregister(this);
	}

	@Override
	public void onTurn() {

	}
}
