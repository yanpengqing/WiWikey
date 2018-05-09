package com.wiwikeyandroid.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.protocol.HTTP;
import org.seny.android.utils.ALog;
import org.seny.android.utils.DateUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.wiwikeyandroid.R;
import com.wiwikeyandroid.activity.HomeAcivity;
import com.wiwikeyandroid.bean.GetRepairListBean.RepairListBean;
import com.wiwikeyandroid.bean.GetVerifyCodeBean;
import com.wiwikeyandroid.bean.RBResponse;
import com.wiwikeyandroid.net.HttpLoader;
import com.wiwikeyandroid.net.HttpLoader.ResponseListener;
import com.wiwikeyandroid.utils.GlobalWiwikey;
import com.wiwikeyandroid.utils.PrefUtils;
 
public class RepairsAdapter extends BaseAdapter implements ResponseListener { 

	private Context context;
	private boolean  isSolve ;//处理类型   true已处理   ，  默认false 待处理
	private String type ;    //类型，repair 报修，complain 投诉
	private List<RepairListBean> repairList = new ArrayList<RepairListBean>(); //总报修单
	private List<RepairListBean> completeList ;//已处理
	//private List<RepairListBean> untreatedList; //待处理
	private int delPosition;
	public RepairsAdapter(Context context, boolean b, String type, List<RepairListBean> list) { 
		this.context = context;
		this.isSolve = b;
		this.type = type;
		repairList = list;
		classify(repairList);
	}
	/**
	 * 
	 * @param list总的报修单子
	 */
	private void classify(List<RepairListBean> list) {
		completeList = new ArrayList<RepairListBean>();
		if (isSolve) {   
			for (int i = 0; i <list.size(); i++) {
				if (list.get(i).getCurrStatus()!=1) {
					completeList.add(list.get(i));
				}
			}
		}else {
			for (int i = 0; i <list.size(); i++) {
				if (list.get(i).getCurrStatus()==1) {
					completeList.add(list.get(i));
				}
			}
		}
	}

	@Override
	public int getCount() {
		return completeList.size();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate( 
					R.layout.repairs_item_card, null);
			holder = new ViewHolder();
			holder.tv_title = (TextView) convertView
					.findViewById(R.id.tv_repairs_title);
			holder.date = (TextView) convertView
					.findViewById(R.id.tv_repairs_date);
			holder.address = (TextView) convertView
					.findViewById(R.id.tv_address);
			holder.tv_name_phone = (TextView) convertView
					.findViewById(R.id.tv_name_phone);
			holder.tv_state = (TextView) convertView
					.findViewById(R.id.tv_state);
			holder.tv_update_state = (TextView) convertView
					.findViewById(R.id.tv_update_state);
			holder.tv_content = (TextView) convertView
					.findViewById(R.id.tv_content);
			holder.ll_image = (LinearLayout) convertView
					.findViewById(R.id.ll_image);
			holder.ll_address = (LinearLayout) convertView
					.findViewById(R.id.ll_address);
			holder.btn_cancel = (Button) convertView
					.findViewById(R.id.btn_cancel);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		//暂时隐藏图片
		holder.ll_image.setVisibility(View.GONE);
		holder.tv_state.setTextColor(isSolve?context.getResources().getColor(R.color.colorGreen):context.getResources().getColor(R.color.textRed));
		holder.tv_update_state.setTextColor(isSolve?context.getResources().getColor(R.color.colorGreen):context.getResources().getColor(R.color.textRed));
		holder.tv_update_state.setText(isSolve?"已处理":"待处理");
		holder.btn_cancel.setVisibility(isSolve?View.GONE:View.VISIBLE);
		holder.ll_address.setVisibility(type.equals("repair")?View.VISIBLE:View.GONE);
		holder.btn_cancel.setText(type.equals("repair")?"取消订单":"取消投诉");
		
		holder.tv_title.setText(completeList.get(position).getSubject());
		holder.date.setText(DateUtils.formatDateAndTime10(type.equals("repair")?completeList.get(position).getRepairTime():completeList.get(position).getComplainTime()));
		holder.address.setText(completeList.get(position).getPosition());
		holder.tv_name_phone.setText(completeList.get(position).getMemberName()+" "+completeList.get(position).getMobile());
		holder.tv_content.setText(completeList.get(position).getContent());
		holder.btn_cancel.setOnClickListener(new OnClickListener() {
			

			@Override
			public void onClick(View v) {
				delPosition = position;
				ALog.d("delPosition"+delPosition+"");
					Map<String, String> params = new HashMap<String, String>();
					params.put(type.equals("repair")?"repairId":"complainId", type.equals("repair")?completeList.get(position).getRepairId()+"":completeList.get(position).getComplainId()+"");
					params.put("memberId",
							PrefUtils.getInt(context, "memberId", -1) + "");
					params.put("token",
							PrefUtils.getString(context, "token", ""));
					HttpLoader.post(type.equals("repair")?GlobalWiwikey.URL_CANCELREPAIR:GlobalWiwikey.URL_CANCELCOMPLAIN, params , GetVerifyCodeBean.class, GlobalWiwikey.REQUEST_CANCELREPAIR, RepairsAdapter.this);
			}
		});
		return convertView;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	class ViewHolder {
		private TextView tv_title, date, address, tv_name_phone, tv_state,
				tv_update_state, tv_content;
		private LinearLayout ll_image,ll_address;
		private Button btn_cancel;
	}

	public void setSolve(boolean b) {
		this.isSolve = b;
		classify(repairList);
	}
	public int getListsize(){
		return completeList.size();
	}
	@Override
	public void onGetResponseSuccess(int requestCode, RBResponse response) {
		GetVerifyCodeBean bean = (GetVerifyCodeBean) response;
		if (bean.getErrmsg().equals("SUCCESS")) {
			// completeList.remove(delPosition);
			 Iterator<RepairListBean> sListIterator = repairList.iterator();  
			 while(sListIterator.hasNext()){  
			     RepairListBean e = sListIterator.next();   
			     if(e.equals(completeList.get(delPosition))){  
			     sListIterator.remove();  
			     break;
			     }  
			 }  
			// List<RepairListBean> repairLists = 
			//completeList.clear();  
			//completeList.addAll(); 
			 setSolve(isSolve);
			notifyDataSetChanged();
		}
	}
	@Override
	public void onGetResponseError(int requestCode, VolleyError error) {
		
	}
}
