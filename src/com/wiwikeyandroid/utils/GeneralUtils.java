/**
 * 
 */
package com.wiwikeyandroid.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;



/**
 * 说明: 日期格式化
 * @author liquan
 * @date 2015-2-2
 */
public class GeneralUtils {
	public final static String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public final static String TIME_FORMAT_MM = "yyyy-MM-dd HH:mm";
	public final static String FILE_NAME_FORMAT = "yyyy-MM-dd-HH-mm-ss";
	public final static String DATE_FORMAT = "yyyy-MM-dd";
	public final static String TIMESTAMP_FORMAT = "yyyy-MM-dd";
	public final static String YEARMON_FORMAT = "yyyy-MM";
	public final static String CHINESE_DATE_FORMAT = "yyyy年MM月dd日";
	private final static SimpleDateFormat timestampFormat = new SimpleDateFormat(TIMESTAMP_FORMAT);
	private final static SimpleDateFormat timeFormat = new SimpleDateFormat(
			TIME_FORMAT);
	private final static SimpleDateFormat timeFormatMM = new SimpleDateFormat(
			TIME_FORMAT_MM);
	private final static SimpleDateFormat dateFormat = new SimpleDateFormat(
			DATE_FORMAT);
	private final static SimpleDateFormat yearmonFormat = new SimpleDateFormat(
			YEARMON_FORMAT);
	private final static SimpleDateFormat fileNameFormat = new SimpleDateFormat(
			FILE_NAME_FORMAT);
	private final static SimpleDateFormat chineseDateFormat = new SimpleDateFormat(
			CHINESE_DATE_FORMAT);
	public static final String SHORT_DOMAIN = "http://www.shaishaiqiang.com/u/";

	public static final int SHORT_DOMAIN_LENGTH = 25;

	public static Date getDate(String date) throws ParseException {
		return timestampFormat.parse(date);
	}

	public static Date getDateTime(String date) throws ParseException {
		return timeFormat.parse(date);
	}

	public static Date getDateTimeMM(String date) throws ParseException {
		return timeFormatMM.parse(date);
	}

	public static Timestamp getTimestamp(String date) throws ParseException {
		if (date == null || date.trim().length() == 0) {
			return null;
		}
		return new Timestamp(timeFormat.parse(date).getTime());
	}

	public static String formateFileName(Date date) {
		if (date == null) {
			return "";
		}
		return fileNameFormat.format(date);
	}

	public static String formatDate(Date date) {
		if (date == null) {
			return "";
		}
		return dateFormat.format(date);
	}
	
	//long转换为Date类型
	// currentTime要转换的long类型的时间
	// formatType要转换的时间格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
	public static Date longToDate(long currentTime, String formatType)
			throws ParseException {
		Date dateOld = new Date(currentTime); // 根据long类型的毫秒数生命一个date类型的时间
		String sDateTime = dateToString(dateOld, formatType); // 把date类型的时间转换为string
		Date date = stringToDate(sDateTime, formatType); // 把String类型转换为Date类型
		return date;
	}
	
	// long类型转换为String类型
 	// currentTime要转换的long类型的时间
 	// formatType要转换的string类型的时间格式
 	public static String longToString(long currentTime, String formatType)
 			throws ParseException {
 		Date date = longToDate(currentTime, formatType); // long类型转成Date类型
 		String strTime = dateToString(date, formatType); // date类型转成String
 		return strTime;
 	}
 	
 // date类型转换为String类型
 	// formatType格式为yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
 	// data Date类型的时间
 	public static String dateToString(Date data, String formatType) {
 		return new SimpleDateFormat(formatType).format(data);
 	}
 
 	// string类型转换为date类型
 	// strTime要转换的string类型的时间，formatType要转换的格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日
 	// HH时mm分ss秒，
 	// strTime的时间格式必须要与formatType的时间格式相同
 	public static Date stringToDate(String strTime, String formatType)
 			throws ParseException {
 		SimpleDateFormat formatter = new SimpleDateFormat(formatType);
 		Date date = null;
 		date = formatter.parse(strTime);
 		return date;
 	}
     
    /**
     * 获取当年的第一天
     * @param year
     * @return
     */
    public static Long getCurrYearFirst(){
        Calendar currCal=Calendar.getInstance();  
        int currentYear = currCal.get(Calendar.YEAR);
        return getFormatstamp(dateToString(getYearFirst(currentYear), DATE_FORMAT));
    }
    
    /**
     * 获取某年第一天日期
     * @param year 年份
     * @return Date
     */
    public static Date getYearFirst(int year){
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        Date currYearFirst = calendar.getTime();
        return currYearFirst;
    }

	public static String formatYearmon(Date date) {
		if (date == null) {
			return "";
		}
		return yearmonFormat.format(date);
	}

	public static String formatTime(Date date) {
		if (date == null) {
			return "";
		}
		return timeFormat.format(date);
	}

	public static String formatTimeMm(Date date) {
		if (date == null) {
			return "";
		}
		return timeFormatMM.format(date);
	}

	public static String formatChineseDate(Date date) {
		if (date == null) {
			return "";
		}
		return chineseDateFormat.format(date);
	}

	/**
	 * 将当前时间戳转化成数据库对应位数
	 * 
	 * @return 10位时间戳，精确到秒
	 */
	public static Long getTimestamp() {
		Long timestamp = 0L;
		Long temptime_l = System.currentTimeMillis();
		Double temptime_d = Math.floor((temptime_l.doubleValue() / 1000));
		timestamp = temptime_d.longValue();
		return timestamp;
	}
	
	/**
	 * 将当前时间戳转化成数据库对应位数
	 * 
	 * @return 10位时间戳，精确到秒
	 */
	public static Long getFormatstamp(String date) {
		Long timestamp = 0L;
		Long temptime_l;
		try {
			temptime_l = getDate(date).getTime();
			Double temptime_d = Math.floor((temptime_l.doubleValue() / 1000));
			timestamp = temptime_d.longValue();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return timestamp;
	}

	/**
	 * 将当前时间戳转化成数据库对应位数
	 * 
	 * @return 10位时间戳，精确到秒
	 */
	public static Long getFormatTimestamp(String date) {
		Long timestamp = 0L;
		Long temptime_l;
		try {
			temptime_l = getDateTime(date).getTime();
			Double temptime_d = Math.floor((temptime_l.doubleValue() / 1000));
			timestamp = temptime_d.longValue();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return timestamp;
	}

	/**
	 * 将当前时间戳转化成数据库对应位数
	 * 
	 * @return 10位时间戳，精确到秒
	 */
	public static Long getFormatTimeMMstamp(String date) {
		Long timestamp = 0L;
		Long temptime_l;
		try {
			temptime_l = getDateTimeMM(date).getTime();
			Double temptime_d = Math.floor((temptime_l.doubleValue() / 1000));
			timestamp = temptime_d.longValue();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return timestamp;
	}
	
	public static Long getFormatDatestamp(String date) {
		Long timestamp = 0L;
		Long temptime_l;
		try {
			temptime_l = getDate(date).getTime();
			Double temptime_d = Math.floor((temptime_l.doubleValue() / 1000));
			timestamp = temptime_d.longValue();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return timestamp;
	}
	
	/**
	 * 将数据库取出的时间戳转化成页面显示日期
	 * 
	 * @param timestamp
	 *            10位时间戳
	 * @return 格式为PEOPLE_T_CURDATE_FORMAT所设置的显示格式
	 */
	public static String getDisplayTime(Long timestamp) {
		if (timestamp == null || timestamp == 0L) {
			return "";
		} else {
			String timeformat = null;
			Long temptime_l = timestamp * 1000;
			Long nowtime = System.currentTimeMillis();

			Long passtime = nowtime - temptime_l;
			String strtime = "";
			if (passtime <= 60000) {
				strtime = "1分钟内";
			}
			if (passtime > 60000 && passtime < 60000 * 60) {
				strtime = (passtime / 60000) + "分钟前";
			}
			if (passtime >= 60 * 60000 && passtime < 12 * 60 * 60 * 1000) {
				strtime = (passtime / 3600000) + "小时前";
			}
			if (strtime.length() > 0) {
				return strtime;
			}

			timeformat = timeFormatMM.format(temptime_l);
			return timeformat;
		}
	}
	
	
	public static String timestampFormat(Long timestamp) {
		if (timestamp == null || timestamp == 0L) {
			return "";
		} else {
			String timeformat = null;
			Long temptime_l = timestamp * 1000;
			Long nowtime = System.currentTimeMillis();

			Long passtime = nowtime - temptime_l;
			String strtime = "";
			if (passtime <= 60000) {
				strtime = "1分钟内";
			}
			if (passtime > 60000 && passtime < 60000 * 60) {
				strtime = (passtime / 60000) + "分钟前";
			}
			if (passtime >= 60 * 60000 && passtime < 12 * 60 * 60 * 1000) {
				strtime = (passtime / 3600000) + "小时前";
			}
			if (strtime.length() > 0) {
				return strtime;
			}

			timeformat = timeFormatMM.format(temptime_l);
			return timeformat;
		}
	}

	/**
	 * 转换为管理后台使用的日期格式
	 * 
	 * @param timestamp
	 * @return
	 */
	public static String getAdminDisplayTime(Long timestamp) {
		if (timestamp == null || timestamp == 0L) {
			return "";
		}
		String timeformat = null;
		Long temptime_l = timestamp * 1000;
		timeformat = timeFormatMM.format(temptime_l);
		return timeformat;
	}

	/**
	 * YYYY-MM-DD格式
	 * @param timestamp
	 * @return
	 */
	public static String getDisplayDate(Long timestamp) {
		if (timestamp == null || timestamp == 0L) {
			return "";
		}
		String timeformat = null;
		Long temptime_l = timestamp * 1000;
		timeformat = dateFormat.format(temptime_l);
		return timeformat;
	}
	
	/**
	 * 转换为管理后台使用的日期格式
	 * 
	 * @param timestamp
	 * @return
	 */
	public static String getTime(Long timestamp) {
		if (timestamp == null || timestamp == 0L) {
			return "";
		}
		String timeformat = null;
		Long temptime_l = timestamp * 1000;
		timeformat = timeFormat.format(temptime_l);
		return timeformat;
	}

	/**
	 * 转换为管理后台使用的日期格式
	 * 
	 * @param timestamp
	 * @return
	 */
	public static String getYearmonDisplayTime(Long timestamp) {
		if (timestamp == null || timestamp == 0L) {
			return "";
		}
		String timeformat = null;
		Long temptime_l = timestamp * 1000;
		timeformat = yearmonFormat.format(temptime_l);
		return timeformat;
	}

	/**
	 * 取得两天之间相差的天数�����������
	 * 
	 * @param oldDate
	 *            Date
	 * @param newDate
	 *            Date
	 * @return int
	 */
	public static int getDaysInTwoDate(Date oldDate, Date newDate) {
		GregorianCalendar OldDate = new GregorianCalendar(1900 + oldDate
				.getYear(), oldDate.getMonth(), oldDate.getDate());
		int i = 0, j = 0;
		Date nextDate = new Date();
		if (!GeneralUtils.formatDate(oldDate).trim().equals(
				GeneralUtils.formatDate(newDate).trim())) {
			if (oldDate.before(newDate)) {
				for (;; i++) {
					OldDate.add(GregorianCalendar.DATE, 1);
					nextDate = OldDate.getTime();
					if (GeneralUtils.formatDate(nextDate).trim().equals(
							GeneralUtils.formatDate(newDate).trim())) {
						break;
					}
				}
				j = i + 1;
			} else {
				for (;; i--) {
					OldDate.add(GregorianCalendar.DATE, -1);
					nextDate = OldDate.getTime();
					if (GeneralUtils.formatDate(nextDate).trim().equals(
							GeneralUtils.formatDate(newDate).trim())) {
						break;
					}
				}
				j = i - 1;
			}
		} else {
			j = 0;
		}
		return j;
	}

	/**
	 * �õ���oldDate获取一些天以后的日期Ժ������
	 * 
	 * @param days
	 *            int
	 * @return Date
	 */
	public static Date getDateAtSomeDaysAfter(Date oldDate, int days) {
		Date theDate = new Date();
		GregorianCalendar OldDate = new GregorianCalendar(1900 + oldDate
				.getYear(), oldDate.getMonth(), oldDate.getDate());
		OldDate.add(GregorianCalendar.DATE, days);
		theDate = OldDate.getTime();
		return theDate;
	}

	/**
	 * 计算两个日期间的时间差
	 * 
	 * @param oldDate
	 * @param newDate
	 * @return
	 */
	public static long getTimeTotal(Date oldDate, Date newDate) {
		long seconds = Math.abs(newDate.getTime() - oldDate.getTime()) / 1000;

		return seconds / (60 * 60);
	}

	public static double getTimeTotal(String oldDate, String newDate)
			throws ParseException {
		Date date1 = getDateTime(oldDate);
		Date date2 = getDateTime(newDate);
		long seconds = Math.abs(date2.getTime() - date1.getTime()) / 1000;

		return seconds / (60.0 * 60.0);
	}

	public static String formatDateTime(String date, int type) {
		if (date == null || "".equals(date))
			return "";
		String year = "", month = "", day = "", hour = "", min = "", sec = "";
		year = date.substring(0, 4);
		month = date.substring(5, 7);
		day = date.substring(8, 10);
		hour = date.substring(11, 13);
		min = date.substring(14, 16);

		if (month.substring(0, 1).equals("0")) {
			month = month.replaceFirst("0", "");
		}
		if (day.substring(0, 1).equals("0")) {
			day = day.replaceFirst("0", "");
		}
		if (hour.substring(0, 1).equals("0")) {
			hour = hour.replaceFirst("0", "");
		}
		if (min.substring(0, 1).equals("0")) {
			min = min.replaceFirst("0", "");
		}
		String returnDate = year + " 年 " + month + " 月 " + day + " 日 " + hour
				+ " 时 " + min + " 分";
		if (type == 1) {
			returnDate = year + " 年 " + month + " 月 " + day + " 日 ";
		}
		if (type == 2) {
			returnDate = year + " 年 " + month + " 月 " + day + " 日 " + hour
					+ " 时 ";
			;
		}
		return returnDate;
	}
	
	   public static int daysOfTwo(Date fDate, Date oDate) {

	       Calendar aCalendar = Calendar.getInstance();

	       aCalendar.setTime(fDate);

	       int day1 = aCalendar.get(Calendar.DAY_OF_YEAR);

	       aCalendar.setTime(oDate);

	       int day2 = aCalendar.get(Calendar.DAY_OF_YEAR);

	       return day2 - day1;

	    }
	
	/**
	 * 生成一个30位的ID
	 * 
	 * @param userId,可传入一个数字
	 * @return
	 */
	public static String generateID(String userId) {
		if (userId == null)
			userId = "";
		Date d = new Date();
		StringBuffer sb = new StringBuffer();
		sb.append(toValue(1900 + d.getYear())).append(toValue(d.getMonth() + 1)).append(toValue(d.getDate()));
		sb.append(toValue(d.getHours())).append(toValue(d.getMinutes())).append(toValue(d.getSeconds()));
		sb.append(toValue(Calendar.getInstance().get(java.util.Calendar.MILLISECOND)));
		sb.append(Math.round(Math.random() * 100));
		sb.append(Math.round(Math.random() * 100));
		sb.append(Math.round(Math.random() * 100));
		sb.append(Math.round(Math.random() * 100));
		sb.append(Math.round(Math.random() * 100)).append(userId);
		return formatString(sb.toString());
	}

	public static String getPass() {
		StringBuffer sb = new StringBuffer();
		sb.append(Math.round(Math.random() * 100));
		sb.append(Math.round(Math.random() * 100));
		return sb.toString();
	}

	private static String toValue(int value) {
		String strRet = String.valueOf(value);
		if (value < 10) {
			strRet = Math.round(Math.random() * 9) + Integer.toString(value);
		}
		return strRet;
	}

	private static String formatString(String value) {
		int length = value.length();
		if (length < 30) {
			for (int i = 0; i <= 30 - length; i++) {
				value = i + value;
			}
		}
		if (value.length() > 30) {
			value = value.substring(value.length() - 30, value.length());
		}
		return value;
	}

	/**
	 * 变化类型成string类型
	 * 
	 * @param object
	 * @return
	 */
	public static String castToString(Object object) {
		String temp = "";
		if (object instanceof Integer) {
			temp = Integer.toString(((Integer) object).intValue());
		}
		if (object instanceof Date) {
			temp = GeneralUtils.formatDate((Date) object);
		}
		if (object instanceof Long) {
			temp = Long.toString(((Long) object).longValue());
		}
		if (object instanceof String) {
			temp = (String) object;
		}
		if (object instanceof Double) {
			temp = Double.toString(((Double) object).intValue());
		}

		return temp;
	}
	    public static Date stringToDate(String str) {  
	        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");  
	        Date date = null;  
	        try {  
	            // Fri Feb 24 00:00:00 CST 2012  
	            date = format.parse(str);   
	        } catch (ParseException e) {  
	            e.printStackTrace();  
	        }  
	        // 2012-02-24  
	        date = java.sql.Date.valueOf(str);  
	                                              
	        return date;  
	    }  
	/**
	 * 格式化小数位
	 * 
	 * @param d
	 * @param c
	 * @return
	 */
	public static String formatMas(double d, int c) {
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(c);
		return nf.format(d);
	}

	public static String formatMas(Object d, int c) {
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(c);
		return nf.format(d);
	}

	/**
	 * 将字符串md5加密
	 * 
	 * @param test
	 * @return
	 * @throws Exception
	 */
	public static String formatToMD5(String test) throws Exception {
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(test.getBytes());

		byte b[] = md.digest();
		int i;

		StringBuffer buf = new StringBuffer("");
		for (int offset = 0; offset < b.length; offset++) {
			i = b[offset];
			if (i < 0)
				i += 256;
			if (i < 16)
				buf.append("0");
			buf.append(Integer.toHexString(i));
		}

		return buf.toString();
	}

	/**
	 * 将字符串形式的ip地址转换为BigInteger
	 * 
	 * @param ipInString
	 *            字符串形式的ip地址
	 * @return 整数形式的ip地址
	 */

	public static BigInteger StringToBigInt(String ipInString) {
		ipInString = ipInString.replace(" ", "");
		byte[] bytes;
		if (ipInString.contains(":"))
			bytes = ipv6ToBytes(ipInString);
		else
			bytes = ipv4ToBytes(ipInString);
		return new BigInteger(bytes);
	}

	/**
	 * ipv4地址转有符号byte[5]
	 * 
	 * @param ipv4
	 *            字符串的IPV4地址
	 * @return big integer number
	 */
	private static byte[] ipv4ToBytes(String ipv4) {
		byte[] ret = new byte[5];
		ret[0] = 0;
		// 先找到IP地址字符串中.的位置
		int position1 = ipv4.indexOf(".");
		int position2 = ipv4.indexOf(".", position1 + 1);
		int position3 = ipv4.indexOf(".", position2 + 1);
		// 将每个.之间的字符串转换成整型
		ret[1] = (byte) Integer.parseInt(ipv4.substring(0, position1));
		ret[2] = (byte) Integer.parseInt(ipv4.substring(position1 + 1,
				position2));
		ret[3] = (byte) Integer.parseInt(ipv4.substring(position2 + 1,
				position3));
		ret[4] = (byte) Integer.parseInt(ipv4.substring(position3 + 1));
		return ret;
	}

	/**
	 * ipv6地址转有符号byte[17]
	 * 
	 * @param ipv6
	 *            字符串形式的IP地址
	 * @return big integer number
	 */
	private static byte[] ipv6ToBytes(String ipv6) {
		byte[] ret = new byte[17];
		ret[0] = 0;
		int ib = 16;
		boolean comFlag = false;// ipv4混合模式标记
		if (ipv6.startsWith(":"))// 去掉开头的冒号
			ipv6 = ipv6.substring(1);
		String groups[] = ipv6.split(":");
		for (int ig = groups.length - 1; ig > -1; ig--) {// 反向扫描
			if (groups[ig].contains(".")) {
				// 出现ipv4混合模式
				byte[] temp = ipv4ToBytes(groups[ig]);
				ret[ib--] = temp[4];
				ret[ib--] = temp[3];
				ret[ib--] = temp[2];
				ret[ib--] = temp[1];
				comFlag = true;
			} else if ("".equals(groups[ig])) {
				// 出现零长度压缩,计算缺少的组数
				int zlg = 9 - (groups.length + (comFlag ? 1 : 0));
				while (zlg-- > 0) {// 将这些组置0
					ret[ib--] = 0;
					ret[ib--] = 0;
				}
			} else {
				int temp = Integer.parseInt(groups[ig], 16);
				ret[ib--] = (byte) temp;
				ret[ib--] = (byte) (temp >> 8);
			}
		}
		return ret;
	}

	/**
	 * Decode html代码
	 * 
	 * @param obj
	 * @return
	 */
	public static String HTMLDecode(String str) {
		String decodestr = str;
		decodestr = decodestr.replace("&lt;", "<");
		decodestr = decodestr.replace("&gt;", ">");
		decodestr = decodestr.replace("&quot;", "\"");
		decodestr = decodestr.replace("&amp;", "&");
		return decodestr;
	}

	// 六位MD5
	public synchronized static String getMD5Upper6Bit(byte[] source) {
		String s = null;
		char hexDigits[] = { // 用来将字节转换成 16 进制表示的字符
		'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D',
				'E', 'F' };
		try {
			java.security.MessageDigest md = java.security.MessageDigest
					.getInstance("MD5");
			md.update(source);
			byte tmp[] = md.digest(); // MD5 的计算结果是一个 128 位的长整数，
			// 用字节表示就是 16 个字节
			char str[] = new char[3 * 2]; // 每个字节用 16 进制表示的话，使用两个字符，
			// 所以表示成 16 进制需要 32 个字符
			int k = 0; // 表示转换结果中对应的字符位置
			for (int i = 0; i < 3; i++) { // 从第一个字节开始，对 MD5 的每一个字节
				// 转换成 16 进制字符的转换
				byte byte0 = tmp[i]; // 取第 i 个字节
				str[k++] = hexDigits[byte0 >>> 4 & 0xf]; // 取字节中高 4 位的数字转换,
				// >>> 为逻辑右移，将符号位一起右移
				str[k++] = hexDigits[byte0 & 0xf]; // 取字节中低 4 位的数字转换
			}
			s = new String(str); // 换后的结果转换为字符串
		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}

	/**
	 * 返回第一个汉字的位置,或系统保留字
	 */
	public static int getFirstCNCharacter(String str) {
		int position = -1;
		char[] strToChar = str.toCharArray();
		if (strToChar.length > 0) {
			for (int i = 0; i < strToChar.length; i++) {
				if (isGBK(strToChar[i]) || isKeyWord(strToChar[i])) {
					position = i;
					return position;
				}
			}
		}
		return position;
	}

	public static boolean isGBK(char c) {
		Character ch = new Character(c);
		String sCh = ch.toString();
		try {
			byte[] bb = sCh.getBytes("GBK");
			if (bb.length > 1) {
				return true;
			}
		} catch (java.io.UnsupportedEncodingException ex) {
			return false;
		}
		return false;
	}
	
	/**
	 * 计算两个日期间的时间差(多少秒)
	 * @param oldDate
	 * @param newDate
	 * @return
	 */
	public static long getTimeTotalSS(Date oldDate, Date newDate) {
		long seconds = (newDate.getTime() - oldDate.getTime()) / 1000;
		return seconds;
	}

	/**
	 * 检查是否是系统保留字
	 */
	public static boolean isKeyWord(char c) {
		char[] keyWord = { '[', ']' };
		for (int i = 0; i < keyWord.length; i++) {
			if (c == keyWord[i]) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入。
	 * 
	 * @param v1
	 *            被除数
	 * @param v2
	 *            除数
	 * @param scale
	 *            表示表示需要精确到小数点以后几位。
	 * @return 两个参数的商
	 */

	public static double div(double v1, double v2, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException(
					"The   scale   must   be   a   positive   integer   or   zero");
		}
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	// 中奖算法，有概率要求，积分越高概率越高
	// userMap: 用户集合，key:userId value:积分数 grade:概率档次
	// winCount: 要抽取的中奖个数
	public static Integer[] getWinPrize(Map userMap, int winCount, int grade) {

		Set keySet = userMap.keySet();
		List uIdList = new ArrayList();
		Iterator it = keySet.iterator();
		while (it.hasNext()) {
			Integer uId = (Integer) it.next();
			Integer fraction = (Integer) userMap.get(uId);
			int count = fraction / grade + 1;
			if (count > 8)
				count = 8;
			for (int i = 0; i < count; i++) {
				uIdList.add(uId);
			}
		}
		Object[] uIds = uIdList.toArray();

		if (winCount > keySet.size()) {
			winCount = keySet.size();
		}
		Integer[] winIds = new Integer[winCount];
		List hasWindList = new ArrayList();
		for (int s = 0; s < winCount; s++) {
			Integer winId = getWindId(hasWindList, uIds);
			winIds[s] = winId;
			hasWindList.add(winId);
		}
		System.out.println("--------------=============" + winIds[0] + "---"
				+ winIds[1]);
		return winIds;
	}

	private static Integer getWindId(List list, Object[] uIds) {
		Random rnd = new Random();
		Integer startNum = rnd.nextInt(uIds.length);
		Integer winId = (Integer) uIds[startNum];
		if (!list.contains(winId)) {
			return winId;
		} else {
			Integer wId = getWindId(list, uIds);
			return wId;
		}
	}

	public static String format60To10(String math60, double subNum) {
		String result = "";
		try {
			String a = math60.split("\\.")[0];
			String b = math60.split("\\.")[1];
			String d = a.substring(0, a.length() - 2);
			String s = a.substring(a.length() - 2, a.length());
			String f = s + "." + b;
			double dd = Double.parseDouble(d);
			double fd = Double.parseDouble(f) / 60;
			result = GeneralUtils.formatMas(dd + fd + subNum, 7);
		}catch(Exception e){
			return "";
		}
		return result;
	}

	public static String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}

	// 转化十六进制编码为字符串
	public static String toStringHex(String s) {
		byte[] baKeyword = new byte[s.length() / 2];
		for (int i = 0; i < baKeyword.length; i++) {
			try {
				baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(
						i * 2, i * 2 + 2), 16));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
			s = new String(baKeyword, "utf-8");// UTF-16le:Not
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return s;
	}

	public static String stringTo16(String str) {
		String hexString = "0123456789ABCDEF"; //此处不可随意改动
		// 根据默认编码获取字节数组
		byte[] bytes = str.getBytes();
		StringBuilder sb = new StringBuilder(bytes.length * 2);
		// 将字节数组中每个字节拆解成2位16进制整数
		for (int i = 0; i < bytes.length; i++) {
			sb.append(hexString.charAt((bytes[i] & 0xf0) >> 4));
			sb.append(hexString.charAt((bytes[i] & 0x0f) >> 0));
		}
		return sb.toString();
	}

	/**
	 * 16进制转byte Convert hex string to byte[]
	 * 
	 * @param hexString
	 *            the hex string
	 * @return byte[]
	 */
	public static byte[] hexStringToBytes(String hexString) {
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
		}
		return d;
	}

	/**
	 * Convert char to byte
	 * 
	 * @param c
	 *            char
	 * @return byte
	 */
	private static byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}

	/**
	 * 转16进制
	 * 
	 * @param b
	 * @return
	 */
	public static String Bytes2HexString(byte[] b) {
		String ret = "";
		for (int i = 0; i < b.length; i++) {
			String hex = Integer.toHexString(b[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			ret += hex.toUpperCase() + " ";
		}
		return ret;
	}
	
	public static String toHexString(String s) { 
		String str=""; 
		for (int i=0;i<s.length();i++) { 
			int ch = (int)s.charAt(i); 
			String s4 = Integer.toHexString(ch); 
			str = str + s4; 
		} 
		return str; 
	} 
	/**
	 * 从list中size条记录中随机获取f*size条记录。
	 * @param list 数据集
	 * @param f 百分比
	 * @return 
	 */
	public static List<Object> hitByRandMethod(List<Object> list,Float f){
		List<Object> lists = new ArrayList<Object>();
		Set<Integer> indexList = randMethodByRatio(f, list.size());
		for (Integer i : indexList) {
			lists.add(list.get(i));
		}
        return lists;
	}
	/**
	 * 按指定比率命中返回对象。
	 * @param o
	 * @param f
	 * @return
	 */
	public static Object hitByRandMethod(Object o,Float f){
		Object d=null;
		 int hitnum=(int) Math.floor(f);
		 int randnum = (int)(Math.random()*100);
		 if(randnum<hitnum) d=o;
		return d;
	}
	/**
	 * 生成100以内的randNum个随机数
	 * @param randNum
	 * @return   
	 */
	private static Set<Integer> randMethod(Integer randNum) 
	{
	  Set<Integer> nummSet = new HashSet<Integer>();
	  Integer num = null;
	  while( nummSet.size() != randNum )
	  {
	    num = (int)(Math.random()*100);
	    nummSet.add(num);
	  }
	   return nummSet;
	}
	/**
	 * 生成total以内的百分比（ratio）个随机数(如：total为100，ratio为20f，那么就是随机生成20个随机数)  当为小数时取整
	 * @param ratio百分比
	 * @param total 总个数
	 * @return   
	 */
	private static Set<Integer> randMethodByRatio(float ratio,Integer total) 
	{
	  Set<Integer> nummSet = new HashSet<Integer>();
	  Integer num = null;
	  while( nummSet.size() != (int)Math.floor(((ratio/100)*total)))
	  {
	    num = (int)(Math.random()*total);
	    nummSet.add(num);
	  }
	   return nummSet;
	}
	
	boolean flag = false;
	/** 
	 * 删除单个文件 
	 * @param   sPath    被删除文件的文件名 
	 * @return 单个文件删除成功返回true，否则返回false 
	 */  
	public boolean deleteFile(String sPath) {  
	    flag = false;  
	    File file = new File(sPath);  
	    // 路径为文件且不为空则进行删除  
	    if (file.isFile() && file.exists()) {  
	        file.delete();  
	        flag = true;  
	    }  
	    return flag;  
	}
	
	
	/**
	 * 获取json数据
	 * @param url
	 * @return
	 */
	public static String loadJson (String url) {  
        StringBuilder json = new StringBuilder();  
        try {  
            URL urlObject = new URL(url);  
            URLConnection uc = urlObject.openConnection();  
            BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));  
            String inputLine = null;  
            while ( (inputLine = in.readLine()) != null) {  
                json.append(inputLine);  
            }  
            in.close();  
        } catch (MalformedURLException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        return json.toString();  
    } 
	
	/** 
	 * 删除目录（文件夹）以及目录下的文件 
	 * @param   sPath 被删除目录的文件路径 
	 * @return  目录删除成功返回true，否则返回false 
	 */  
	public boolean deleteDirectory(String sPath) {  
	    //如果sPath不以文件分隔符结尾，自动添加文件分隔符  
	    if (!sPath.endsWith(File.separator)) {  
	        sPath = sPath + File.separator;  
	    }  
	    File dirFile = new File(sPath);  
	    //如果dir对应的文件不存在，或者不是一个目录，则退出  
	    if (!dirFile.exists() || !dirFile.isDirectory()) {  
	        return false;  
	    }  
	    flag = true;  
	    //删除文件夹下的所有文件(包括子目录)  
	    File[] files = dirFile.listFiles();  
	    for (int i = 0; i < files.length; i++) {  
	        //删除子文件  
	        if (files[i].isFile()) {  
	            flag = deleteFile(files[i].getAbsolutePath());  
	            if (!flag) break;  
	        } //删除子目录  
	        else {  
	            flag = deleteDirectory(files[i].getAbsolutePath());  
	            if (!flag) break;  
	        }  
	    }  
	    if (!flag) return false;  
	    //删除当前目录  
	    if (dirFile.delete()) {  
	        return true;  
	    } else {  
	        return false;  
	    }  
	}
	
	// 递归
    public long getFileSize(File f)throws Exception//取得文件夹大小
    {
        long size = 0;
        File flist[] = f.listFiles();
        for (int i = 0; i < flist.length; i++)
        {
            if (flist[i].isDirectory())
            {
                size = size + getFileSize(flist[i]);
            } else
            {
                size = size + flist[i].length();
            }
        }
        return size;
    }
    
    public static String processUEditorString(String strSource)
	{
		if (GeneralUtils.nullOrBlank(strSource))
		{
			return "";
		}
		strSource = strSource.replaceAll("＜", "<");
		strSource = strSource.replaceAll("＆", "&");
		strSource = strSource.replaceAll("&quot;", "\"");
		strSource = strSource.replaceAll("\r\n", "");
		strSource = strSource.replaceAll("\r", "");
		strSource = strSource.replaceAll("\n", "");
		strSource = strSource.replaceAll("＃", "#");
		return strSource.replaceAll("＞", ">");
	}
    /**
	 * 检查一个字符串是null还是空的
	 * 
	 * @param param
	 * @return boolean
	 */
	public static boolean nullOrBlank(String param) {
		return (param == null || param.length() == 0 || param.trim().equals("") || param
				.trim().equalsIgnoreCase("null")) ? true : false;
	}
	
	private static String getClassAnnotationForType(Class clazz,String typeName,String propName) {
		for(Annotation an : clazz.getAnnotations()){
			String anStr = an.toString();
			try {
				if(anStr.indexOf(typeName) >= 0) {
					for(Method me : an.annotationType().getDeclaredMethods()) {
						 if (!me.isAccessible()) {
							 me.setAccessible(true);
						 }
						 if(me.getName().equals(propName)) {
							 Object invoke = me.invoke(an);
							 if (invoke.getClass().isArray()) {
								 Object[] temp = (Object[]) invoke;
								 for (Object o : temp) {
									 return o.toString();
								 }
							 }
						 }
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	private static String getMethodAnnotationForType(Method method,String typeName,String propName) {
		for(Annotation an : method.getAnnotations()){
			String anStr = an.toString();
			try {
				if(anStr.indexOf(typeName) >= 0) {
					for(Method me : an.annotationType().getDeclaredMethods()) {
						 if (!me.isAccessible()) {
							 me.setAccessible(true);
						 }
						 if(me.getName().equals(propName)) {
							 Object invoke = me.invoke(an);
							 if (invoke.getClass().isArray()) {
								 Object[] temp = (Object[]) invoke;
								 for (Object o : temp) {
									 return o.toString();
								 }
							 }
						 }
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	
	public static void main(String[] args) throws Exception {
		List<Integer> l = new ArrayList<Integer>();
		Integer a = 3;
		int flowType=1;
		boolean isUncertainly = false;
		List<String> ssList = new ArrayList<String>();
		int kkk = 0;
//		for(int i=0;i<1000;i++) {
//			String tmp = GeneralUtils.generateID("");
//			if(ssList.contains(tmp)){
//				kkk++;
//			}
//			ssList.add(tmp);
//			//System.out.println(tmp);
//		}
		//System.out.println("-----------" + GeneralUtils.getFormatstamp("2015-07-23"));
		int aa = -175045496;
		int cc = 734726336;
		String hex = Integer.toHexString(cc);
		BigInteger bb = new BigInteger(hex,16);
		System.out.println("转换：："+bb.intValue());
		System.out.println("10转16进制::::"+Integer.toHexString(Integer.parseInt("203968335")));//-175045496
		System.out.println("" + Integer.toHexString(-175045496));
		BigInteger b = new BigInteger("880491F5",16);
		BigInteger c = new BigInteger("0491F582D139808A",16);
		System.out.println("" + b);
		System.out.println("" + c);
		//System.out.println("--------------------" +Integer.valueOf("-175045496",16));
		//System.out.println("--------------------" +Integer.valueOf("F5910488",16));
		////GeneralUtils.getMethodInfo("com.wiwikey.webservice.appinterface.PropertyInfoInterface");
		///Class clazz = Class.forName("com.wiwikey.webservice.appinterface.PropertyInfoInterface");
		//Method method = clazz.getDeclaredMethod("getPropertyNotifyListByplotId", String.class, String.class);
		//System.out.println(Arrays.toString(getMethodParameterNamesByAsm4(clazz,method)));
	}
	
}
