package com.wiwikeyandroid.modules.Contacts;

import java.util.ArrayList;

import org.seny.android.utils.ALog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.nineoldandroids.view.ViewPropertyAnimator;
import com.wiwikeyandroid.R;
import com.wiwikeyandroid.agora.MsgEvent;
import com.wiwikeyandroid.base.BaseFragment;
import com.wiwikeyandroid.modules.Contacts.pager.BasePager;
import com.wiwikeyandroid.modules.Contacts.pager.ContactsPager;
import com.wiwikeyandroid.modules.Contacts.pager.Life_service_Pager;
import com.wiwikeyandroid.modules.Contacts.pager.TenementPager;
import com.wiwikeyandroid.modules.Contacts.pager.neighboursPager;

import de.greenrobot.event.EventBus;

/**
 * @author Joseph on 2016/5/22.
 *         <p/>
 *         联系人主界面
 */
public class ContactsFragment extends BaseFragment {

	@InjectView(R.id.rb_callrecorder)
	RadioButton callrecorder;

	@InjectView(R.id.rb_neighbours)
	RadioButton neighbours;

	@InjectView(R.id.rb_tenement)
	RadioButton tenement;

	@InjectView(R.id.rb_life_service)
	RadioButton life_service;

	@InjectView(R.id.rg_group)
	RadioGroup rgGroup;

	@InjectView(R.id.ll_add)
	LinearLayout addButton;

	int mAddButtonViewHeight;
	@InjectView(R.id.indicate_line)
	View indicate_line; // 下滑指示线

	@InjectView(R.id.vpager_contacts)
	ViewPager vpagerContacts;
	// @InjectView(R.id.tab_text)
	// TextView tab_text;

	private ArrayList<BasePager> mPagers;
	private ContentAdapter adapter;
	private ContactsPager mContactsPager;
	private int lineWidth;
	boolean ininit = true;

	@Override
	public View CreateView() {
		ALog.d("ContactsFragment:CreateView()");
		View mContactsView = View.inflate(mActivity,
				R.layout.contacts_navigation, null);
		ButterKnife.inject(this, mContactsView);
		EventBus.getDefault().register(this);
		rgGroup.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {

						mAddButtonViewHeight = rgGroup.getWidth();
						ALog.d(mAddButtonViewHeight + "");
						rgGroup.getViewTreeObserver()
								.removeGlobalOnLayoutListener(this);
						calculateIndicateLineWidth();
					}
				});
		return mContactsView;
	}

	@Override
	public void initData() {
		mPagers = new ArrayList<BasePager>();
		mPagers.add(new ContactsPager(mActivity));
		mPagers.add(new neighboursPager(mActivity));
		mPagers.add(new TenementPager(mActivity));
		mPagers.add(new Life_service_Pager(mActivity));

		/*
		 * addButton.getViewTreeObserver().addOnGlobalLayoutListener( new
		 * OnGlobalLayoutListener() {
		 * 
		 * @Override public void onGlobalLayout() {
		 * 
		 * mAddButtonViewHeight = addButton.getWidth();
		 * ALog.d(mAddButtonViewHeight+""); addButton.getViewTreeObserver()
		 * .removeGlobalOnLayoutListener(this); } });
		 */
		// calculateIndicateLineWidth();
		adapter = new ContentAdapter();
		vpagerContacts.setAdapter(adapter);

		// 设置点击事件

		rgGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.rb_callrecorder:
					vpagerContacts.setCurrentItem(0, false);// 参2，是否具有滑动动画
					break;
				case R.id.rb_neighbours:
					vpagerContacts.setCurrentItem(1, false);
					break;
				case R.id.rb_tenement:
					vpagerContacts.setCurrentItem(2, false);
					break;
				case R.id.rb_life_service:
					vpagerContacts.setCurrentItem(3, false);
					break;
				}
			}
		});

		vpagerContacts.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
				int targetPosition = position * lineWidth
						+ positionOffsetPixels / mPagers.size();
				ViewPropertyAnimator.animate(indicate_line)
						.translationX(targetPosition).setDuration(0);

			}

			@Override
			public void onPageSelected(int position) {
				// 设置监听，页面被选中时初始化数据
				// mPagers.get(position).initData();
				ScaleTabTitle(position);
				isSelected(position);
				if (position == 1) {
					if (ininit) {
						mPagers.get(position).initData();
						ininit = false;
					}
				}

			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});
		// 初始化数据
		mPagers.get(0).initData();
		// mPagers.get(1).initData();
		mPagers.get(2).initData();
		mPagers.get(3).initData();
	}

	@OnClick(R.id.ll_add)
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.ll_add: // 打开系统自带添加联系人界面
			Intent addIntent = new Intent(Intent.ACTION_INSERT);
			addIntent.setType("vnd.android.cursor.dir/person");
			addIntent.setType("vnd.android.cursor.dir/contact");
			addIntent.setType("vnd.android.cursor.dir/raw_contact");
			mActivity.startActivity(addIntent);
			break;

		default:
			break;
		}
	}

	protected void ScaleTabTitle(int position) {

		ViewPropertyAnimator.animate(callrecorder)
				.scaleX(position == 0 ? 1.2f : 1).setDuration(200);
		ViewPropertyAnimator.animate(callrecorder)
				.scaleY(position == 0 ? 1.2f : 1).setDuration(200);

		ViewPropertyAnimator.animate(neighbours)
				.scaleX(position == 1 ? 1.2f : 1).setDuration(200);
		ViewPropertyAnimator.animate(neighbours)
				.scaleY(position == 1 ? 1.2f : 1).setDuration(200);

		ViewPropertyAnimator.animate(tenement).scaleX(position == 2 ? 1.2f : 1)
				.setDuration(200);
		ViewPropertyAnimator.animate(tenement).scaleY(position == 2 ? 1.2f : 1)
				.setDuration(200);

		ViewPropertyAnimator.animate(life_service)
				.scaleX(position == 3 ? 1.2f : 1).setDuration(200);
		ViewPropertyAnimator.animate(life_service)
				.scaleY(position == 3 ? 1.2f : 1).setDuration(200);
	}

	/**
	 * 随着viewpage条目的改变而改变图片选中状态
	 * 
	 * @param position
	 */

	private void isSelected(int position) {
		switch (position) {
		case 0:
			rgGroup.check(callrecorder.getId()); // tab_text.setText("通讯录");
			break;
		case 1:
			rgGroup.check(neighbours.getId()); // tab_text.setText("邻里");
			break;
		case 2:
			rgGroup.check(tenement.getId()); // tab_text.setText("物业");
			break;
		case 3:
			rgGroup.check(life_service.getId()); // tab_text.setText("生活服务");
			break;
		}
	}

	/**
	 * 动态计算指示线的宽度
	 */
	private void calculateIndicateLineWidth() {
		int screenWidth = mActivity.getWindowManager().getDefaultDisplay()
				.getWidth();
		ALog.d(screenWidth + "");
		screenWidth = screenWidth - mAddButtonViewHeight;
		lineWidth = mAddButtonViewHeight / mPagers.size();
		ALog.d(lineWidth + "");
		indicate_line.getLayoutParams().width = lineWidth;
		indicate_line.requestLayout();
	}

	// 获取屏幕宽高
	private int[] getscrren() {
		int[] w_h = new int[2];
		WindowManager wm = (WindowManager) mActivity
				.getSystemService(Context.WINDOW_SERVICE);
		int width = wm.getDefaultDisplay().getWidth();
		int height = wm.getDefaultDisplay().getHeight();
		w_h[0] = width;
		w_h[1] = height;
		return w_h;
	}

	class ContentAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return mPagers.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			BasePager pager = mPagers.get(position);
			View view = pager.mRootView;
			// pager.initData();//初始化数据
			container.addView(view);
			return view;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
	}

	@Override
	public boolean onBackPressed() {
		return false;
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);

		/*
		 * if(hidden){ //界面隐藏，禁用所有点击事件 for (int i = 0; i <
		 * ll_more.getChildCount(); i++) {
		 * ll_more.getChildAt(i).setEnabled(false); // 置为不可用 }
		 * 
		 * }else {//界面显示，相应所有点击事件 for (int i = 0; i < ll_more.getChildCount();
		 * i++) { ll_more.getChildAt(i).setEnabled(true); // 置为可用 } }
		 */
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO: inflate a fragment view
		View rootView = super.onCreateView(inflater, container,
				savedInstanceState);
		ButterKnife.inject(this, rootView);
		return rootView;
	}

	public void onEventMainThread(MsgEvent event) {
		switch (event.getMsg()) {
		case "auThenSucceed":
			initData();
			break;
		}
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		ButterKnife.reset(this);
	}
}
