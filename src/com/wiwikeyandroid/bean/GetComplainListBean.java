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
 * @author Joseph on 2016/7/28.
 *         <p/>
 */
public class GetComplainListBean {

    /**
     * errno : 0
     * errmsg : SUCCESS
     * complainList : [{"complainId":3,"plotId":14,"buildingId":8,"unitId":18,"houseId":641,"memberId":71,"memberName":"小白","mobile":"","complainTime":1469605431,"addr":"2栋1单元大厅","subject":"投诉测试-cw","content":"灯泡坏了长时间未换","currStatus":1,"fromType":1,"sendUser":"","sendMobile":"","sendTime":0,"processTime":0,"processResult":"","processer":"","isdel":0}]
     */

    private int errno;
    private String errmsg;
    /**
     * complainId : 3
     * plotId : 14
     * buildingId : 8
     * unitId : 18
     * houseId : 641
     * memberId : 71
     * memberName : 小白
     * mobile :
     * complainTime : 1469605431
     * addr : 2栋1单元大厅
     * subject : 投诉测试-cw
     * content : 灯泡坏了长时间未换
     * currStatus : 1
     * fromType : 1
     * sendUser :
     * sendMobile :
     * sendTime : 0
     * processTime : 0
     * processResult :
     * processer :
     * isdel : 0
     */

    private List<ComplainListBean> complainList;

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

    public List<ComplainListBean> getComplainList() {
        return complainList;
    }

    public void setComplainList(List<ComplainListBean> complainList) {
        this.complainList = complainList;
    }

    public static class ComplainListBean {
        private int complainId;
        private int plotId;
        private int buildingId;
        private int unitId;
        private int houseId;
        private int memberId;
        private String memberName;
        private String mobile;
        private int complainTime;
        private String addr;
        private String subject;
        private String content;
        private int currStatus;
        private int fromType;
        private String sendUser;
        private String sendMobile;
        private int sendTime;
        private int processTime;
        private String processResult;
        private String processer;
        private int isdel;

        public int getComplainId() {
            return complainId;
        }

        public void setComplainId(int complainId) {
            this.complainId = complainId;
        }

        public int getPlotId() {
            return plotId;
        }

        public void setPlotId(int plotId) {
            this.plotId = plotId;
        }

        public int getBuildingId() {
            return buildingId;
        }

        public void setBuildingId(int buildingId) {
            this.buildingId = buildingId;
        }

        public int getUnitId() {
            return unitId;
        }

        public void setUnitId(int unitId) {
            this.unitId = unitId;
        }

        public int getHouseId() {
            return houseId;
        }

        public void setHouseId(int houseId) {
            this.houseId = houseId;
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

        public int getComplainTime() {
            return complainTime;
        }

        public void setComplainTime(int complainTime) {
            this.complainTime = complainTime;
        }

        public String getAddr() {
            return addr;
        }

        public void setAddr(String addr) {
            this.addr = addr;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
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

        public String getSendUser() {
            return sendUser;
        }

        public void setSendUser(String sendUser) {
            this.sendUser = sendUser;
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

        public int getProcessTime() {
            return processTime;
        }

        public void setProcessTime(int processTime) {
            this.processTime = processTime;
        }

        public String getProcessResult() {
            return processResult;
        }

        public void setProcessResult(String processResult) {
            this.processResult = processResult;
        }

        public String getProcesser() {
            return processer;
        }

        public void setProcesser(String processer) {
            this.processer = processer;
        }

        public int getIsdel() {
            return isdel;
        }

        public void setIsdel(int isdel) {
            this.isdel = isdel;
        }
    }
}
