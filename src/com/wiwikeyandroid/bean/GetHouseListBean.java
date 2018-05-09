package com.wiwikeyandroid.bean;

import java.util.List;

/**22
 * @author Joseph on 2016/6/18.
 *         <p/>
 */
public class GetHouseListBean extends RBResponse{

    /**
     * errno : 0
     * houseList : [{"houseId":1,"plotId":1,"buildingId":1,"unitId":1,"houseNumber":"101","name":"101号房","isdel":0,"unitName":"1单元","buildingName":"1栋"},{"houseId":3,"plotId":1,"buildingId":1,"unitId":2,"houseNumber":"101","name":"101号房","isdel":0,"unitName":"2单元","buildingName":"1栋"}]
     * errmsg : SUCCESS
     */

    private int errno;
    private String errmsg;
    /**
     * houseId : 1
     * plotId : 1
     * buildingId : 1
     * unitId : 1
     * houseNumber : 101
     * name : 101号房
     * isdel : 0
     * unitName : 1单元
     * buildingName : 1栋
     */

    private List<HouseListBean> houseList;

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

    public List<HouseListBean> getHouseList() {
        return houseList;
    }

    public void setHouseList(List<HouseListBean> houseList) {
        this.houseList = houseList;
    }

    public static class HouseListBean {
        private int houseId;
        private int plotId;
        private int buildingId;
        private int unitId;
        private String houseNumber;
        private String name;
        private int isdel;
        private String unitName;
        private String buildingName;

        public int getHouseId() {
            return houseId;
        }

        public void setHouseId(int houseId) {
            this.houseId = houseId;
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

        public String getHouseNumber() {
            return houseNumber;
        }

        public void setHouseNumber(String houseNumber) {
            this.houseNumber = houseNumber;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getIsdel() {
            return isdel;
        }

        public void setIsdel(int isdel) {
            this.isdel = isdel;
        }

        public String getUnitName() {
            return unitName;
        }

        public void setUnitName(String unitName) {
            this.unitName = unitName;
        }

        public String getBuildingName() {
            return buildingName;
        }

        public void setBuildingName(String buildingName) {
            this.buildingName = buildingName;
        }
    }
}
