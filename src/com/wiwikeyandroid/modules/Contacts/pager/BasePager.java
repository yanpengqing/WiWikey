package com.wiwikeyandroid.modules.Contacts.pager;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.wiwikeyandroid.R;
import com.wiwikeyandroid.view.PullToRefreshView;
import com.wiwikeyandroid.view.PullToRefreshView.OnHeaderRefreshListener;

public class BasePager {

    public Activity mActivity;
    public FrameLayout fl_content;

    public View mRootView; //当前页面的布局对象
	private View view;

    public BasePager(Activity mActivity) {
        this.mActivity = mActivity;
        this.mRootView = initView();
    }

    public View initView() { 
        view = View.inflate(mActivity, R.layout.base_pager, null);
        fl_content = (FrameLayout) view.findViewById(R.id.fl_content);
        return view;
    }

    public void initData() {
    }
}
