package com.wiwikeyandroid.bean;

/**
 * @author Joseph on 2016/7/1.
 *         <p/>
 *      房子认证确认，判断会员是否认证成功,返回个人信息
 */
public class DoHouseAuthVerifyCodeBean extends RBResponse{

    /**
     * memberId : 33
     * mobile : 13777777777
     * realname :
     * nickname :
     * birthday :
     * sex : -1
     * headMiddle : /UploadImages/head_default_50.gif
     * signature :
     * status : 1
     * memberType : 1
     * memberLevel : 1
     * isModifyPass : 0
     * isSetPass : 0
     */

    private MemberModelBean memberModel;
    /**
     * memberModel : {"memberId":33,"mobile":"13777777777","realname":"","nickname":"","birthday":"","sex":-1,"headMiddle":"/UploadImages/head_default_50.gif","signature":"","status":1,"memberType":1,"memberLevel":1,"isModifyPass":0,"isSetPass":0}
     * errno : 0
     * errmsg : SUCCESS
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
