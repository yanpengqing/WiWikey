package com.wiwikeyandroid.bean;

/**
 * ━━━━ Code is far away from ━━━━━━
 * 　　  () 　　　  ()
 * 　　  ( ) 　　　( )
 * 　　  ( ) 　　　( )
 * 　　┏┛┻━━━┛┻┓
 * 　　┃　　　━　　　┃
 * 　　┃　┳┛　┗┳　┃
 * 　　┃　　　┻　　　┃
 * 　　┗━┓　　　┏━┛
 * 　　　　┃　　　┃
 * 　　　　┃　　　┗━━━┓
 * 　　　　┃　　　　　　　┣┓
 * 　　　　┃　　　　　　　┏┛
 * 　　　　┗┓┓┏━┳┓┏┛
 * 　　　　　┃┫┫　┃┫┫
 * 　　　　　┗┻┛　┗┻┛
 * ━━━━ bug with the XYY protecting━━━
 *
 * @author Joseph on 2016/6/8.
 *         <p/>
 *         验证码登录返回信息
 */
public class LoginCodeBean {

    /**
     * errno : 0
     * token : 84584a3918d95ab47998215e78dbfdbf
     * memberModel : {"memberId":2,"mobile":"18073350278","headMiddle":"/UploadImages/head_default_50.gif","status":1,"memberType":2,"memberLevel":1,"isModifyPass":0,"isSetPass":0}
     * errmsg : SUCCESS
     */

    private int errno;
    private String token;
    /**
     * memberId : 2
     * mobile : 18073350278
     * headMiddle : /UploadImages/head_default_50.gif
     * status : 1
     * memberType : 2
     * memberLevel : 1
     * isModifyPass : 0
     * isSetPass : 0
     */

    private MemberModelBean memberModel;
    private String errmsg;

    public int getErrno() {
        return errno;
    }

    public void setErrno(int errno) {
        this.errno = errno;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public MemberModelBean getMemberModel() {
        return memberModel;
    }

    public void setMemberModel(MemberModelBean memberModel) {
        this.memberModel = memberModel;
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
        private String headMiddle;
        private int status;
        private int memberType;
        private int householdType;
        private int memberLevel;
        private int isModifyPass;
        private int isSetPass;
        private int lastLoginTime;
        
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

        public String getHeadMiddle() {
            return headMiddle;
        }

        public void setHeadMiddle(String headMiddle) {
            this.headMiddle = headMiddle;
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
        public int getLastLoginTime() {
            return lastLoginTime;
        }

        public void setLastLoginTime(int lastLoginTime) {
            this.lastLoginTime = lastLoginTime;
        }
    }
}
