package com.wiwikeyandroid.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author Joseph on 2016/8/10.
 *         <p/>
 */
public class HouseholdList extends RBResponse{

    private int errno;
    private String errmsg;
    /**
     * memberId : 68
     * mobile : 18618251002
     * realname : 陈文
     * nickname : 陈文
     * birthday :
     * sex : -1
     * headMiddle :
     * signature :
     * status : 1
     * memberType : 1
     * memberLevel : 1
     * lastLoginTime : 1469080716
     * isModifyPass : 0
     * isSetPass : 1
     * householdType:1
     * plotIdList : []
     */

    private List<MemberListBean> memberList;

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

    public List<MemberListBean> getMemberList() {
        return memberList;
    }

    public void setMemberList(List<MemberListBean> memberList) {
        this.memberList = memberList;
    }

    public static class MemberListBean implements Serializable { 
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
        private int householdType;
        private List<?> plotIdList;

        public int getHouseholdType() {
			return householdType;
		}

		public void setHouseholdType(int householdType) {
			this.householdType = householdType;
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

        public String getRealname() {
            return realname;
        }

        public void setRealname(String realname) {
            this.realname = realname;
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

        public int getLastLoginTime() {
            return lastLoginTime;
        }

        public void setLastLoginTime(int lastLoginTime) {
            this.lastLoginTime = lastLoginTime;
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

        public List<?> getPlotIdList() {
            return plotIdList;
        }

        public void setPlotIdList(List<?> plotIdList) {
            this.plotIdList = plotIdList;
        }
    }
}
