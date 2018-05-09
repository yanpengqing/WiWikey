package com.wiwikeyandroid.modules.Contacts.pager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.seny.android.utils.ALog;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.android.volley.VolleyError;
import com.wiwikeyandroid.R;
import com.wiwikeyandroid.adapter.ContactsAdapter;
import com.wiwikeyandroid.bean.MemberListByTypeBean;
import com.wiwikeyandroid.bean.MemberListByTypeBean.MemberListBean;
import com.wiwikeyandroid.bean.RBResponse;
import com.wiwikeyandroid.modules.Contacts.model.Person;
import com.wiwikeyandroid.net.HttpLoader;
import com.wiwikeyandroid.net.HttpLoader.ResponseListener;
import com.wiwikeyandroid.utils.GlobalWiwikey;
import com.wiwikeyandroid.utils.PinYin;
import com.wiwikeyandroid.utils.PrefUtils;
import com.wiwikeyandroid.view.IndexableListView;
import com.wiwikeyandroid.view.PullToRefreshView;
import com.wiwikeyandroid.view.PullToRefreshView.OnHeaderRefreshListener;
import com.wiwikeyandroid.view.SeekEditText;

public class TenementPager extends BasePager implements ResponseListener{
	
	@InjectView(R.id.lv_contacts)
	IndexableListView lvContacts;
	@InjectView(R.id.et_seek)
	SeekEditText et_seek;
	@InjectView(R.id.ll_et_seek)
	LinearLayout ll_et_seek;
	@InjectView(R.id.ll_no_data)
	LinearLayout ll_no_data;
	@InjectView(R.id.iv_describe)
	ImageView iv_describe;
	@InjectView(R.id.tv_describe)
	TextView tv_describe;
	private List<Person> lists;
	private ContactsAdapter adapter;
	
    public TenementPager(Activity mActivity) {
        super(mActivity);
    }

    @Override
    public void initData() {
        System.out.println("物业页面数据初始化了");

		View mTenementTabView = View.inflate(mActivity,
				R.layout.item_contacts_listview, null);
		ButterKnife.inject(this, mTenementTabView); 
		if (!PrefUtils.getString(mActivity, "memberHouseList0", "").isEmpty()) {
			ALog.d("物业页面数据"+PrefUtils.getString(mActivity, "memberHouseList0", "").split("#")[1]);
			getMemberListByType();
		}else {
			lvContacts.setVisibility(View.GONE);
			ll_et_seek.setVisibility(View.GONE);
			ll_no_data.setVisibility(View.VISIBLE);
			iv_describe.setImageResource(R.drawable.home_bg2_es);
			tv_describe.setText("阁下身份尚未认证\n管家信息已被封藏");
		}
		
        fl_content.addView(mTenementTabView);

    }
    /**
     * 获取物业信息
     */
	private void getMemberListByType() {
		new Thread(new Runnable() {
			public void run() {
				Map<String, String> params = new HashMap<String, String>();
				params.put("plotId", PrefUtils.getString(mActivity, "memberHouseList0", "").split("#")[1]);
				params.put("memberType","3");
				params.put("memberId",
						PrefUtils.getInt(mActivity, "memberId", -1) + "");
				params.put("token", PrefUtils.getString(mActivity, "token", ""));
				HttpLoader.post(GlobalWiwikey.URL_GETMEMBERLISTBYTYPE, params,
						MemberListByTypeBean.class,  
						GlobalWiwikey.REQUEST_GETMEMBERLISTBYTYPE_TENEMENT,
						TenementPager.this);
			}
		}).start();
		
	}

	@Override
	public void onGetResponseSuccess(int requestCode, RBResponse response) {
					switch (requestCode) {
					case GlobalWiwikey.REQUEST_GETMEMBERLISTBYTYPE_TENEMENT: 
						MemberListByTypeBean memberListBean = (MemberListByTypeBean) response;
						if (memberListBean.getErrmsg().equals("SUCCESS")&&memberListBean.getErrno()==0&&memberListBean.getMemberList().size()>0) {
							//获取物业信息成功
							lists = new ArrayList<Person>();
							Person person ;
							List<MemberListBean> memberList = memberListBean.getMemberList();
							for (int i = 0; i < memberList.size(); i++) {
								person = new Person();
								person.setName(memberList.get(i).getRealname());
								person.setSortKey(getSortKey(PinYin.getPinYin(memberList.get(i).getRealname().substring(0, 1))));
								person.setPicUrl(GlobalWiwikey.URL_SERVER+memberList.get(i).getHeadMiddle());
								person.setNumber(memberList.get(i).getMobile());
								person.setWiWiUser(memberList.get(i).getLastLoginTime()>0?true:false);
								lists.add(person);
							}
						}
						lvContacts.setVisibility(View.VISIBLE);
						ll_et_seek.setVisibility(View.VISIBLE);
						ll_no_data.setVisibility(View.GONE);
						adapter = new ContactsAdapter(mActivity, lists);
						lvContacts.setAdapter(adapter);
						
						break;

					default:
						break;
					}
	}

	@Override
	public void onGetResponseError(int requestCode, VolleyError error) {
		
		
	}

	private static String getSortKey(String sortKeyString) {
		String key = sortKeyString.substring(0, 1).toUpperCase();
		if (key.matches("[A-Z]")) {
			return key;
		}
		return "#";
	}
}
