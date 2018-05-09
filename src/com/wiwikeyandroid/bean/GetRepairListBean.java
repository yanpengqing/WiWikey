package com.wiwikeyandroid.bean;

import java.util.List;

import android.R.integer;

/**
 * @author Joseph on 2016/7/28.
 *         <p/>
 */
public class GetRepairListBean extends RBResponse {

	/**
	 * errmsg : SUCCESS errno : 0 repairList :
	 * [{"areaType":1,"buildingId":8,"content"
	 * :"抽油烟机坏了","currStatus":1,"fromType"
	 * :1,"houseId":641,"isdel":0,"memberId":71
	 * ,"memberName":"小白","mobile":"15200839697"
	 * ,"orderTime":0,"plotId":14,"position"
	 * :"厨房","processResult":"","processTime"
	 * :0,"processer":"","repairId":7,"repairTime"
	 * :1469604474,"sendMobile":"","sendTime"
	 * :0,"sendUser":"","subject":"维修测试-cw","unitId":18,"urgentType":1}]
	 */

	private String errmsg;
	private int errno;
	/**
	 * areaType : 1 buildingId : 8 content : 抽油烟机坏了 currStatus : 1 fromType : 1
	 * houseId : 641 isdel : 0 memberId : 71 memberName : 小白 mobile :
	 * 15200839697 orderTime : 0 plotId : 14 position : 厨房 processResult :
	 * processTime : 0 processer : repairId : 7 repairTime : 1469604474
	 * sendMobile : sendTime : 0 sendUser : subject : 维修测试-cw unitId : 18
	 * urgentType : 1
	 */

	private List<RepairListBean> repairList;
	private List<RepairListBean> complainList;

	public List<RepairListBean> getComplainList() {
		return complainList;
	}

	public void setComplainList(List<RepairListBean> complainList) {
		this.complainList = complainList;
	}

	public String getErrmsg() {
		return errmsg;
	}

	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}

	public int getErrno() {
		return errno;
	}

	public void setErrno(int errno) {
		this.errno = errno;
	}

	public List<RepairListBean> getRepairList() {
		return repairList;
	}

	public void setRepairList(List<RepairListBean> repairList) {
		this.repairList = repairList;
	}

	public static class RepairListBean {
		private int areaType;
		private int buildingId;
		private String content;
		private int currStatus;
		private int fromType;
		private int houseId;
		private int isdel;
		private int memberId;
		private String memberName;
		private String mobile;
		private int orderTime;
		private int plotId;
		private String position;
		private String processResult;
		private int processTime;
		private String processer;
		private int repairId;
		private int repairTime;
		private String sendMobile;
		private int sendTime;
		private String sendUser;
		private String subject;
		private int unitId;
		private int urgentType;

		private int complainTime;
		private int complainId;
		

		public int getComplainId() {
			return complainId;
		}

		public void setComplainId(int complainId) {
			this.complainId = complainId;
		}

		public int getComplainTime() {
			return complainTime;
		}

		public void setComplainTime(int complainTime) {
			this.complainTime = complainTime;
		}

		public int getAreaType() {
			return areaType;
		}

		public void setAreaType(int areaType) {
			this.areaType = areaType;
		}

		public int getBuildingId() {
			return buildingId;
		}

		public void setBuildingId(int buildingId) {
			this.buildingId = buildingId;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}

		public int getCurrStatus() {
			return currStatus;
		}

		public void setCurrStatus(int currStatus) {
			this.currStatus = currStatus;
		}

		public int getFromType() {
			return fromType;
		}

		public void setFromType(int fromType) {
			this.fromType = fromType;
		}

		public int getHouseId() {
			return houseId;
		}

		public void setHouseId(int houseId) {
			this.houseId = houseId;
		}

		public int getIsdel() {
			return isdel;
		}

		public void setIsdel(int isdel) {
			this.isdel = isdel;
		}

		public int getMemberId() {
			return memberId;
		}

		public void setMemberId(int memberId) {
			this.memberId = memberId;
		}

		public String getMemberName() {
			return memberName;
		}

		public void setMemberName(String memberName) {
			this.memberName = memberName;
		}

		public String getMobile() {
			return mobile;
		}

		public void setMobile(String mobile) {
			this.mobile = mobile;
		}

		public int getOrderTime() {
			return orderTime;
		}

		public void setOrderTime(int orderTime) {
			this.orderTime = orderTime;
		}

		public int getPlotId() {
			return plotId;
		}

		public void setPlotId(int plotId) {
			this.plotId = plotId;
		}

		public String getPosition() {
			return position;
		}

		public void setPosition(String position) {
			this.position = position;
		}

		public String getProcessResult() {
			return processResult;
		}

		public void setProcessResult(String processResult) {
			this.processResult = processResult;
		}

		public int getProcessTime() {
			return processTime;
		}

		public void setProcessTime(int processTime) {
			this.processTime = processTime;
		}

		public String getProcesser() {
			return processer;
		}

		public void setProcesser(String processer) {
			this.processer = processer;
		}

		public int getRepairId() {
			return repairId;
		}

		public void setRepairId(int repairId) {
			this.repairId = repairId;
		}

		public int getRepairTime() {
			return repairTime;
		}

		public void setRepairTime(int repairTime) {
			this.repairTime = repairTime;
		}

		public String getSendMobile() {
			return sendMobile;
		}

		public void setSendMobile(String sendMobile) {
			this.sendMobile = sendMobile;
		}

		public int getSendTime() {
			return sendTime;
		}

		public void setSendTime(int sendTime) {
			this.sendTime = sendTime;
		}

		public String getSendUser() {
			return sendUser;
		}

		public void setSendUser(String sendUser) {
			this.sendUser = sendUser;
		}

		public String getSubject() {
			return subject;
		}

		public void setSubject(String subject) {
			this.subject = subject;
		}

		public int getUnitId() {
			return unitId;
		}

		public void setUnitId(int unitId) {
			this.unitId = unitId;
		}

		public int getUrgentType() {
			return urgentType;
		}

		public void setUrgentType(int urgentType) {
			this.urgentType = urgentType;
		}
	}
}
