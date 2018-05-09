package com.wiwikeyandroid.modules.Contacts.model;

import java.io.Serializable;

import android.R.integer;

public class Person implements Serializable,Comparable<Person>{

	private int Contact_id;
	private String number;
	private String name;
	private long date;
	private int duration;
	private int mold; //通话类型    1视频，2语音，3落地,4门口机
	private int type; //通话状态    1呼出， 2已接，3未接通，4未接,5拒绝
	private String picUrl;
	private String sortKey;
	private boolean isWiWiUser;
	private boolean isNeighbour; //是否是邻里

	public Person() {
		super();
	}

	public Person(int contact_id, String number, String name, long date,
			int duration, int mold ,int type, String picUrl, String sortKey,
			boolean isWiWiUser,boolean isNeighbour) {
		super();
		Contact_id = contact_id;
		this.number = number;
		this.name = name;
		this.date = date;
		this.duration = duration;
		this.type = type;
		this.picUrl = picUrl;
		this.sortKey = sortKey;
		this.isWiWiUser = isWiWiUser;
		this.mold = mold;
		this.isNeighbour = isNeighbour;
	}

	public int getMold() {
		return mold;
	}

	public boolean isNeighbour() {
		return isNeighbour;
	}

	public void setNeighbour(boolean isNeighbour) {
		this.isNeighbour = isNeighbour;
	}

	public void setMold(int mold) {
		this.mold = mold;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getContact_id() {
		return Contact_id;
	}

	public void setContact_id(int contact_id) {
		Contact_id = contact_id;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public String getSortKey() {
		return sortKey;
	}

	public void setSortKey(String sortKey) {
		this.sortKey = sortKey;
	}

	public boolean isWiWiUser() {
		return isWiWiUser;
	}

	public void setWiWiUser(boolean isWiWiUser) {
		this.isWiWiUser = isWiWiUser;
	}

	 @Override
	    public int compareTo(Person o) {
	        if(o!=null){
	            if(this.getDate()>o.getDate()){

	               return -1;
	            }else if(this.getDate()==o.getDate()){

	               return 0;

	            }

	       }
	        return 1;
	    }
}
