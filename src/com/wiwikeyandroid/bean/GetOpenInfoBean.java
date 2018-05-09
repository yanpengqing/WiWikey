package com.wiwikeyandroid.bean;


/**
 * @author Joseph on 2016/6/16.
 * 	获取省市县(区)
 *         <p/>
 */
public class GetOpenInfoBean extends RBResponse{

    private int errno;
    private String errmsg;
   

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

}
