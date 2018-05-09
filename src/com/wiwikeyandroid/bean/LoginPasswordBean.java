package com.wiwikeyandroid.bean;

import java.util.List;

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
 *         密码登录返回的数据
 */
public class LoginPasswordBean extends RBResponse{

    /**
     * errno : 0
     * token : 45e05f90aa5a46d00b1074cb20abfcb3
     * memberModel : {"memberId":2,"mobile":"18073350278","nickname":"hehe","birthday":"0820","sex":1,"headMiddle":"/UploadImages/head_default_50.gif","signature":"qq","status":1,"memberType":2,"memberLevel":1,"isModifyPass":0,"isSetPass":0}
     * errmsg : SUCCESS
     */

    private int errno;
    private String token;
    /**
     * memberId : 2
     * mobile : 18073350278
     * nickname : hehe
     * birthday : 0820
     * sex : 1
     * headMiddle : /UploadImages/head_default_50.gif
     * signature : qq
     * status : 1
     * memberType : 2
     * memberLevel : 1
     * isModifyPass : 0
     * isSetPass : 0
     */

    private MemberModelBean memberModel;
    private String errmsg;
    private String agoraVid;

    public String getAgoraVid() {
		return agoraVid;
	}

	public void setAgoraVid(String agoraVid) { 
		this.agoraVid = agoraVid;
	}

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
        private int memberId;   //会员ID
        private String mobile;  //手机号码
        private String realname; //名字
        private String nickname;//昵称
        private String birthday;//生日
        private int sex;//性别   -1未知 1:男  0:女
        private String headMiddle;//会员头像
        private String signature;//会员签名
        private int status;//会员状态：1:正常  0:关闭  2:冻结
        private int memberType;//会员类别： 1:住户  2:游客  3:物业员工  4:商家
        private int householdType;//住户类型 1:业主  2:家庭成员 3:共有人  4:租客 5:其他
        private int memberLevel;//会员级别
        private int isModifyPass;//是否修改过密码  0 ，没有修改过密码
        private int isSetPass;//是否设置了密码
        private int lastLoginTime;  //最后登录时间
        
        
        public int getHouseholdType() {
			return householdType;
		}

		public void setHouseholdType(int householdType) {
			this.householdType = householdType;
		}

		private List<Integer> plotIdList;// 会员绑定的小区
        

        public int getLastLoginTime() {
			return lastLoginTime;
		}

		public void setLastLoginTime(int lastLoginTime) {
			this.lastLoginTime = lastLoginTime;
		}

		public List<Integer> getPlotIdList() {
			return plotIdList;
		}

		public void setPlotIdList(List<Integer> plotIdList) {
			this.plotIdList = plotIdList;
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
    }

	@Override
	public String toString() {
		return "LoginPasswordBean [errno=" + errno + ", token=" + token
				+ ", memberModel=" + memberModel + ", errmsg=" + errmsg + "]";
	}
    
    
}
