package com.wiwikeyandroid.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.sina.weibo.SinaWeibo.ShareParams;
import cn.sharesdk.system.text.ShortMessage;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

import com.wiwikeyandroid.R;

public class ShareBottomDialog {
	private Context context;
	private Dialog dialog;
	private Display display;
	private LinearLayout ll_sinaweibo;
	private LinearLayout ll_wechat_friend;
	private LinearLayout ll_qq;
	private LinearLayout ll_sms;
	private Button bt_cancal;

	public ShareBottomDialog(Context context) {
		this.context = context;
		WindowManager windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		display = windowManager.getDefaultDisplay();
	}

	public ShareBottomDialog builder() {
		// 获取Dialog布局
		View view = LayoutInflater.from(context).inflate(R.layout.dialog_share,
				null);
		// 设置Dialog最小宽度为屏幕宽度
		view.setMinimumWidth(display.getWidth());
		ll_sinaweibo = (LinearLayout) view.findViewById(R.id.ll_sinaweibo);
		ll_wechat_friend = (LinearLayout) view
				.findViewById(R.id.ll_wechat_friend);
		ll_qq = (LinearLayout) view.findViewById(R.id.ll_qq);
		ll_sms = (LinearLayout) view.findViewById(R.id.ll_sms);
		bt_cancal = (Button) view.findViewById(R.id.bt_cancal);
		bt_cancal.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		// 定义Dialog布局和参数
		dialog = new Dialog(context, R.style.ActionSheetDialogStyle);
		dialog.setContentView(view);
		Window dialogWindow = dialog.getWindow();
		dialogWindow.setGravity(Gravity.LEFT | Gravity.BOTTOM);
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		lp.x = 0;
		lp.y = 0;
		dialogWindow.setAttributes(lp);

		return this;
	}

	public ShareBottomDialog setUiBeforShow(final String phone) {  
		
		ShareSDK.initSDK(context);

		ll_sinaweibo.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ShareParams sp = new ShareParams();
				sp.setText("嗨，朋友，送你一款免费视频电话神器…我在等你来电哦！");
				sp.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
				Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
				weibo.share(sp);
				dialog.dismiss();
			}
		});
		ll_wechat_friend.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ShareParams sp = new ShareParams();
				sp.setTitle("嗨，朋友，送你一款免费视频电话神器…");
				sp.setText("我在等你来电哦！");
				sp.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
				Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
				wechat.share(sp);
				dialog.dismiss();
			}
		});
		ll_qq.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ShareParams sp = new ShareParams();
				sp.setTitle("嗨，朋友，送你一款免费视频电话神器…");
				sp.setText("我在等你来电哦！");
				sp.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
				Platform qq = ShareSDK.getPlatform(QQ.NAME);
				qq.share(sp);
				dialog.dismiss();
			}
		});
		ll_sms.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ShareParams sp = new ShareParams();
				sp.setText("嗨，朋友。我在使用 唯家APP 与亲朋进行免费视频电话，你也一起来吧www.wiwikey.com");
				sp.setAddress(phone);
				Platform sms = ShareSDK.getPlatform(ShortMessage.NAME);
				sms.share(sp);
				dialog.dismiss();
			}
		});
		return this;
	}

	public ShareBottomDialog setCancelable(boolean cancel) {
		dialog.setCancelable(cancel);
		return this;
	}

	public ShareBottomDialog setCanceledOnTouchOutside(boolean cancel) {
		dialog.setCanceledOnTouchOutside(cancel);
		return this;
	}

	public void show() {
		dialog.show();
	}
}
