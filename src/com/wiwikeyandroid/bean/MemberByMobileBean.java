package com.wiwikeyandroid.bean;

import java.util.List;

/**
 * @author Joseph on 2016/6/12.
 *         <p/>
 *         通过手机号查询会员信息
 */
public class MemberByMobileBean extends RBResponse {

	/**
	 * memberId : 2 mobile : 18073350278 nickname : birthday : sex : -1
	 * headMiddle : /UploadImages/head_default_50.gif signature : status : 1
	 * memberType : 2 memberLevel : 1 lastLoginTime 1468807405 isModifyPass : 0
	 * isSetPass : 0
	 */

	private MemberModelBean memberModel;
	/**
	 * memberModel:{"memberId":73,"mobile":"13394637849","realname":"","nickname":"","birthday":"","sex":-1,"headMiddle":"/images/head_default_50.gif","signature":"","status":1,
	 * "memberType":1,"memberLevel":1,"lastLoginTime":1468807405,"is
	 * ModifyPass":1,"isSetPass":1,"plotIdList":[14]} 
	 * errno : 0 errmsg : SUCCESS
	 */

	private int errno;
	private String errmsg;

	public MemberModelBean getMemberModel() {
		return memberModel;
	}

	public void setMemberModel(MemberModelBean memberModel) {
		this.memberModel = memberModel;
	}

	public int getErrno() {
		return errno;
	}

	public void setErrno(int errno) {
		this.errno = errno;
	}

	public String getErrmsg() {
		return errmsg;
	}

	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}

	public static class MemberModelBean {
		private int memberId;
		private String mobile;
		private String realname;
		private String nickname;
		private String birthday;
		private int sex;
		private String headMiddle;
		private String signature;
		private int status;
		private int memberType;
		private int memberLevel;
		private int lastLoginTime;
		private int isModifyPass;
		private int isSetPass;
		private List<Integer> plotIdList;

		public int getLastLoginTime() {
			return lastLoginTime;
		}

		public void setLastLoginTime(int lastLoginTime) {
			this.lastLoginTime = lastLoginTime;
		}

		public String getRealname() {
			return realname;
		}

		public void setRealname(String realname) {
			this.realname = realname;
		}

		public int getMemberId() {
			return memberId;
		}

		public void setMemberId(int memberId) {
			this.memberId = memberId;
		}

		public String getMobile() {
			return mobile;
		}

		public void setMobile(String mobile) {
			this.mobile = mobile;
		}

		public String getNickname() {
			return nickname;
		}

		public void setNickname(String nickname) {
			this.nickname = nickname;
		}

		public String getBirthday() {
			return birthday;
		}

		public void setBirthday(String birthday) {
			this.birthday = birthday;
		}

		public int getSex() {
			return sex;
		}

		public void setSex(int sex) {
			this.sex = sex;
		}

		public String getHeadMiddle() {
			return headMiddle;
		}

		public void setHeadMiddle(String headMiddle) {
			this.headMiddle = headMiddle;
		}

		public String getSignature() {
			return signature;
		}

		public void setSignature(String signature) {
			this.signature = signature;
		}

		public int getStatus() {
			return status;
		}

		public void setStatus(int status) {
			this.status = status;
		}

		public int getMemberType() {
			return memberType;
		}

		public void setMemberType(int memberType) {
			this.memberType = memberType;
		}

		public int getMemberLevel() {
			return memberLevel;
		}

		public void setMemberLevel(int memberLevel) {
			this.memberLevel = memberLevel;
		}

		public int getIsModifyPass() {
			return isModifyPass;
		}

		public void setIsModifyPass(int isModifyPass) {
			this.isModifyPass = isModifyPass;
		}

		public int getIsSetPass() {
			return isSetPass;
		}

		public void setIsSetPass(int isSetPass) {
			this.isSetPass = isSetPass;
		}

		public List<Integer> getPlotIdList() {
			return plotIdList;
		}

		public void setPlotIdList(List<Integer> plotIdList) {
			this.plotIdList = plotIdList;
		}
	}
}
