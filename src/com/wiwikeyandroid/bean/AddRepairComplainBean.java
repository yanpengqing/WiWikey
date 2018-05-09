package com.wiwikeyandroid.bean;

/**
 * @author Joseph on 2016/5/30.
 *         <p/>
 *         
 */
public class AddRepairComplainBean extends RBResponse {

	private int errno;
	private String errmsg;
	private int repairId;
	private int complainId;

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

	public int getRepairId() {
		return repairId;
	}

	public void setRepairId(int repairId) {
		this.repairId = repairId;
	}

	public int getComplainId() {
		return complainId;
	}

	public void setComplainId(int complainId) {
		this.complainId = complainId;
	}

}
