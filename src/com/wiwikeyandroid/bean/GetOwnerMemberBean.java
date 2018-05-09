package com.wiwikeyandroid.bean;

/**
 * @author Joseph on 2016/6/30.
 *         <p/>
 *       通过房产ID获取业主信息
 */
public class GetOwnerMemberBean extends RBResponse{

    /**
     * errno : 0
     * ownerMember : {"memberId":1,"mobile":"18618251002","realname":"陈文","nickname":"蚊子","birthday":"","sex":1,"headMiddle":"/UploadImages/head_default_50.gif","signature":"","status":1,"memberType":1,"memberLevel":1,"isModifyPass":0,"isSetPass":1}
     * errmsg : SUCCESS
     */

    private int errno;
    /**
     * memberId : 1
     * mobile : 18618251002
     * realname : 陈文
     * nickname : 蚊子
     * birthday :
     * sex : 1
     * headMiddle : /UploadImages/head_default_50.gif
     * signature :
     * status : 1
     * memberType : 1
     * memberLevel : 1
     * isModifyPass : 0
     * isSetPass : 1
     */

    private OwnerMemberBean ownerMember;
    private String errmsg;

    public int getErrno() {
        return errno;
    }

    public void setErrno(int errno) {
        this.errno = errno;
    }

    public OwnerMemberBean getOwnerMember() {
        return ownerMember;
    }

    public void setOwnerMember(OwnerMemberBean ownerMember) {
        this.ownerMember = ownerMember;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public static class OwnerMemberBean {
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
        private int isModifyPass;
        private int isSetPass;

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
    }
}
