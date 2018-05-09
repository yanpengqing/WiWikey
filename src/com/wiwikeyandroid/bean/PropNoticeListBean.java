package com.wiwikeyandroid.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author Joseph on 2016/7/20.
 *         <p/>
 *        	获取物业通知信息，小区新闻
 */
public class PropNoticeListBean extends RBResponse{

    /**
     * noticeList : [{"noticeId":5,"plotId":14,"title":"停电通知","content":"<p>经电力部门通知，7月19号一天停电，请大家准备一下<\/p><p>嘿嘿<\/p>","noticeType":1,"urgentType":2,"startDate":1468771200,"endDate":1468972800,"imgUrl":"/pictureLib/C/notice/1468809052285_notice.png","creater":9,"createTime":1468809123,"applyRange":1},{"noticeId":9,"plotId":14,"title":"停水通知","content":"<p>停水通知停水通知停水通知<\/p>","noticeType":1,"urgentType":1,"startDate":1468944000,"endDate":1469548800,"imgUrl":"/pictureLib/C/notice/1469009820762_notice.jpg","creater":9,"createTime":1469009824,"applyRange":1}]
     * errno : 0
     * errmsg : SUCCESS
     */

    private int errno;
    private String errmsg;
    /**
     * noticeId : 5
     * plotId : 14
     * title : 停电通知
     * content : <p>经电力部门通知，7月19号一天停电，请大家准备一下</p><p>嘿嘿</p>
     * noticeType : 1
     * urgentType : 2
     * startDate : 1468771200
     * endDate : 1468972800
     * imgUrl : /pictureLib/C/notice/1468809052285_notice.png
     * creater : 9
     * createTime : 1468809123
     * applyRange : 1
     */

    private List<NoticeListBean> noticeList;

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

    public List<NoticeListBean> getNoticeList() {
        return noticeList;
    }

    public void setNoticeList(List<NoticeListBean> noticeList) {
        this.noticeList = noticeList;
    }

    public static class NoticeListBean implements Serializable { 
        private int noticeId;
        private int plotId;
        private String title;
        private String content;
        private int noticeType;
        private int urgentType;
        private int startDate;
        private int endDate;
        private String imgUrl;
        private int creater;
        private int createTime;
        private int applyRange;
        private String brief;

        public String getBrief() {
			return brief;
		}

		public void setBrief(String brief) {
			this.brief = brief;
		}

		public int getNoticeId() {
            return noticeId;
        }

        public void setNoticeId(int noticeId) {
            this.noticeId = noticeId;
        }

        public int getPlotId() {
            return plotId;
        }

        public void setPlotId(int plotId) {
            this.plotId = plotId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getNoticeType() {
            return noticeType;
        }

        public void setNoticeType(int noticeType) {
            this.noticeType = noticeType;
        }

        public int getUrgentType() {
            return urgentType;
        }

        public void setUrgentType(int urgentType) {
            this.urgentType = urgentType;
        }

        public int getStartDate() {
            return startDate;
        }

        public void setStartDate(int startDate) {
            this.startDate = startDate;
        }

        public int getEndDate() {
            return endDate;
        }

        public void setEndDate(int endDate) {
            this.endDate = endDate;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public int getCreater() {
            return creater;
        }

        public void setCreater(int creater) {
            this.creater = creater;
        }

        public int getCreateTime() {
            return createTime;
        }

        public void setCreateTime(int createTime) {
            this.createTime = createTime;
        }

        public int getApplyRange() {
            return applyRange;
        }

        public void setApplyRange(int applyRange) {
            this.applyRange = applyRange;
        }
    }
}
