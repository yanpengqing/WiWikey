package com.wiwikeyandroid.bean;

import java.util.List;

/**
 * @author Joseph on 2016/6/16.
 * 	获取省市县(区)
 *         <p/>
 */
public class GetCityListBean extends RBResponse{

    /**
     * provinceList : [{"cityList":[{"distinctList":[{"dm":430104,"dmmc":"岳麓区","xh":4301,"dm1":"","dmmc1":"","isuse":1},{"dm":430121,"dmmc":"长沙县","xh":4301,"dm1":"","dmmc1":"","isuse":1}],"dm":4301,"dmmc":"长沙","xh":43,"dm1":"","dmmc1":"","isuse":1}],"dm":43,"dmmc":"湖南","xh":0,"dm1":"","dmmc1":"中南","isuse":1}]
     * errno : 0
     * errmsg : SUCCESS
     */

    private int errno;
    private String errmsg;
    /**
     * cityList : [{"distinctList":[{"dm":430104,"dmmc":"岳麓区","xh":4301,"dm1":"","dmmc1":"","isuse":1},{"dm":430121,"dmmc":"长沙县","xh":4301,"dm1":"","dmmc1":"","isuse":1}],"dm":4301,"dmmc":"长沙","xh":43,"dm1":"","dmmc1":"","isuse":1}]
     * dm : 43
     * dmmc : 湖南
     * xh : 0
     * dm1 :
     * dmmc1 : 中南
     * isuse : 1
     */

    private List<ProvinceListBean> provinceList;

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

    public List<ProvinceListBean> getProvinceList() {
        return provinceList;
    }

    public void setProvinceList(List<ProvinceListBean> provinceList) {
        this.provinceList = provinceList;
    }

    public static class ProvinceListBean {
        private int dm;
        private String dmmc;
        private int xh;
        private String dm1;
        private String dmmc1;
        private int isuse;
        /**
         * distinctList : [{"dm":430104,"dmmc":"岳麓区","xh":4301,"dm1":"","dmmc1":"","isuse":1},{"dm":430121,"dmmc":"长沙县","xh":4301,"dm1":"","dmmc1":"","isuse":1}]
         * dm : 4301
         * dmmc : 长沙
         * xh : 43
         * dm1 :
         * dmmc1 :
         * isuse : 1
         */

        private List<CityListBean> cityList;

        public int getDm() {
            return dm;
        }

        public void setDm(int dm) {
            this.dm = dm;
        }

        public String getDmmc() {
            return dmmc;
        }

        public void setDmmc(String dmmc) {
            this.dmmc = dmmc;
        }

        public int getXh() {
            return xh;
        }

        public void setXh(int xh) {
            this.xh = xh;
        }

        public String getDm1() {
            return dm1;
        }

        public void setDm1(String dm1) {
            this.dm1 = dm1;
        }

        public String getDmmc1() {
            return dmmc1;
        }

        public void setDmmc1(String dmmc1) {
            this.dmmc1 = dmmc1;
        }

        public int getIsuse() {
            return isuse;
        }

        public void setIsuse(int isuse) {
            this.isuse = isuse;
        }

        public List<CityListBean> getCityList() {
            return cityList;
        }

        public void setCityList(List<CityListBean> cityList) {
            this.cityList = cityList;
        }

        public static class CityListBean {
            private int dm;
            private String dmmc;
            private int xh;
            private String dm1;
            private String dmmc1;
            private int isuse;
            /**
             * dm : 430104
             * dmmc : 岳麓区
             * xh : 4301
             * dm1 :
             * dmmc1 :
             * isuse : 1
             */

            private List<DistinctListBean> distinctList;

            public int getDm() {
                return dm;
            }

            public void setDm(int dm) {
                this.dm = dm;
            }

            public String getDmmc() {
                return dmmc;
            }

            public void setDmmc(String dmmc) {
                this.dmmc = dmmc;
            }

            public int getXh() {
                return xh;
            }

            public void setXh(int xh) {
                this.xh = xh;
            }

            public String getDm1() {
                return dm1;
            }

            public void setDm1(String dm1) {
                this.dm1 = dm1;
            }

            public String getDmmc1() {
                return dmmc1;
            }

            public void setDmmc1(String dmmc1) {
                this.dmmc1 = dmmc1;
            }

            public int getIsuse() {
                return isuse;
            }

            public void setIsuse(int isuse) {
                this.isuse = isuse;
            }

            public List<DistinctListBean> getDistinctList() {
                return distinctList;
            }

            public void setDistinctList(List<DistinctListBean> distinctList) {
                this.distinctList = distinctList;
            }

            public static class DistinctListBean {
                private int dm;
                private String dmmc;
                private int xh;
                private String dm1;
                private String dmmc1;
                private int isuse;

                public int getDm() {
                    return dm;
                }

                public void setDm(int dm) {
                    this.dm = dm;
                }

                public String getDmmc() {
                    return dmmc;
                }

                public void setDmmc(String dmmc) {
                    this.dmmc = dmmc;
                }

                public int getXh() {
                    return xh;
                }

                public void setXh(int xh) {
                    this.xh = xh;
                }

                public String getDm1() {
                    return dm1;
                }

                public void setDm1(String dm1) {
                    this.dm1 = dm1;
                }

                public String getDmmc1() {
                    return dmmc1;
                }

                public void setDmmc1(String dmmc1) {
                    this.dmmc1 = dmmc1;
                }

                public int getIsuse() {
                    return isuse;
                }

                public void setIsuse(int isuse) {
                    this.isuse = isuse;
                }
            }
        }
    }
}
