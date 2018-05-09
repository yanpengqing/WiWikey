package com.wiwikeyandroid.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.ContextWrapper;
import android.telephony.TelephonyManager;

public class Tools {

	public static String getDeviceID(Context context)
    {
		final TelephonyManager tm = (TelephonyManager) ((ContextWrapper) context).getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
		String id = tm.getDeviceId();
		return id;
	}
	public static boolean isPhoneNum(String mobiles){
		Pattern p = Pattern.compile("^((13[0-9])|(17[0-9])|(15[^4,\\D])|(18[0,1,2,3,5-9]))\\d{8}$");	
		Matcher m = p.matcher(mobiles);
		
		boolean b = m.matches();
		return b;
	} 


}
