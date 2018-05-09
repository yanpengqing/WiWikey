package com.wiwikeyandroid.activity;

import java.util.ArrayList;

import org.seny.android.utils.DensityUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.wiwikeyandroid.R;
import com.wiwikeyandroid.utils.PrefUtils;

public class GuideActivity extends Activity {

	@InjectView(R.id.vp_guide)
	ViewPager vpGuide;
	@InjectView(R.id.ll_container)
	LinearLayout llContainer;
	@InjectView(R.id.iv_red_point)
	ImageView ivRedPoint;
	private GestureDetector gestureDetector; // 用户滑动
	private int currentItem = 0; // 当前图片的索引号 
	private int flaggingWidth;// 互动翻页所需滚动的长度是当前屏幕宽度的1/3
	
	private int[] imageID = new int[] { R.drawable.guide_1, R.drawable.guide_2,
			R.drawable.guide_3, R.drawable.guide_4 };

	private ArrayList<ImageView> imageViews = new ArrayList<>();
	private int dleft;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.guide_activity);
		ButterKnife.inject(this);
		gestureDetector = new GestureDetector(new GuideViewTouch());
		// 获取分辨率 
		DisplayMetrics dm = new DisplayMetrics(); 
		getWindowManager().getDefaultDisplay().getMetrics(dm); 
		flaggingWidth = dm.widthPixels / 3; 
		// 初始化数据
		initdate();
		// ViewPager设置适配器
		vpGuide.setAdapter(adapter);
		vpGuide.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
				RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
						RelativeLayout.LayoutParams.WRAP_CONTENT,
						RelativeLayout.LayoutParams.WRAP_CONTENT);

				params.leftMargin = (int) (dleft * positionOffset + position
						* dleft);
				ivRedPoint.setLayoutParams(params);
			}

			@Override
			public void onPageSelected(int position) {
//				if (position == imageViews.size() - 1) {
//					btnStart.setVisibility(View.VISIBLE);
//				} else {
//					btnStart.setVisibility(View.INVISIBLE);
//				}
				currentItem = position; 
			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});
		// 渲染完毕测量
		Point.getViewTreeObserver().addOnGlobalLayoutListener(
				new ViewTreeObserver.OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						dleft = llContainer.getChildAt(1).getLeft()
								- llContainer.getChildAt(0).getLeft();
						Point.getViewTreeObserver()
								.removeGlobalOnLayoutListener(this);
					}
				});
	}

	ImageView view;
	ImageView Point;

	private void initdate() {
		for (int i = 0; i < imageID.length; i++) {
			view = new ImageView(this);
			view.setBackgroundResource(imageID[i]);
			imageViews.add(view);
			// 添加小圆点
			Point = new ImageView(this);
			Point.setImageResource(R.drawable.shape_gray);
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			if (i != 0) {
				layoutParams.leftMargin = DensityUtil.dip2px(this, 7f);
			}
			Point.setLayoutParams(layoutParams);
			llContainer.addView(Point);
		}
	}

	private PagerAdapter adapter = new PagerAdapter() {
		@Override
		public int getCount() {
			return imageID.length;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			ImageView view = imageViews.get(position);
			container.addView(view);
			return view;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(imageViews.get(position));
		}
	};

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		if (gestureDetector.onTouchEvent(event)) {
			event.setAction(MotionEvent.ACTION_CANCEL);
		}
		return super.dispatchTouchEvent(event);
	}

	private class GuideViewTouch extends SimpleOnGestureListener {
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			if (currentItem == 3) {
				if (Math.abs(e1.getX() - e2.getX()) > Math.abs(e1.getY()
						- e2.getY())
						&& (e1.getX() - e2.getX() <= (-flaggingWidth) || e1
								.getX() - e2.getX() >= flaggingWidth)) {
					if (e1.getX() - e2.getX() >= flaggingWidth) {
						
						startActivity(new Intent(GuideActivity.this,
								LoginActivity.class));
						PrefUtils.setBoolean(getApplication(), "isFirst", false);
						finish();
						return true;
					}
				}
			}
			return false;
		}
	}
}
