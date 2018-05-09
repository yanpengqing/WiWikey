package org.seny.android.utils;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
/**
 * 日期工具类
 ** @author Joseph on 2016/6/12.
 *        <p/>
 */
public class DateUtils {
	/**
	 * 将long得到-- 小时:分
	 * @param lefttime
	 * @return 小时:分
	 */
	public static String formatTimeSimple(long lefttime) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm",Locale.CHINA);
		String sDateTime = sdf.format(lefttime); 
		return sDateTime;
	}
	public static String formatTime(long lefttime) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss",Locale.CHINA);
		String sDateTime = sdf.format(lefttime); 
		return sDateTime;
	}
	/**
	 * 得到: 年-月-日 小时:分钟
	 * @param lefttime
	 * @return 年-月-日 小时:分钟
	 */
	public static String formatDateAndTime(long lefttime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm",Locale.CHINA);
		String sDateTime = sdf.format(lefttime); 
		return sDateTime;
	}
	
	/**
	 * 得到: 年-月-日 小时:分钟:秒
	 * @param lefttime
	 * @return 年-月-日 小时:分钟
	 */
	public static String formatDateAndTimess(long lefttime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.CHINA);
		String sDateTime = sdf.format(lefttime); 
		return sDateTime;
	}
	/**
	 * 得到: 年-月-日
	 * @param lefttime 
	 * @return 年-月-日 
	 */
	public static String formatDate10(long lefttime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",Locale.CHINA);
		 String date = sdf.format(lefttime * 1000L);
		return date;
	}
	/**
	 * 得到: 月-日 小时:分钟
	 * @param lefttime
	 * @return 月-日 小时:分钟
	 */
	public static String formatDateAndTime10(long lefttime) {
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm",Locale.CHINA);
		 String date = sdf.format(lefttime * 1000L);
		return date;
	}
	
	/**
	 * 得到: 月-日
	 * @param lefttime
	 * @return 月-日 
	 */
	public static String formatDateSimple10(long lefttime) {
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd",Locale.CHINA);
		 String date = sdf.format(lefttime * 1000L);
		return date;
	}
	
	
	/**
	 * 得到: 年-月-日
	 * @param lefttime
	 * @return 年-月-日
	 */
	public static String formatDate(long lefttime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",Locale.CHINA);
		
		String sDateTime = sdf.format(lefttime); 
		return sDateTime;
	}
	/**
	 * 获取当前时间
	 * 
	 * @param fts
	 *            要转换成的格式
	 * @return
	 */
	public static String getNowTime(String fts) {
		SimpleDateFormat ft = new SimpleDateFormat(fts);
		return ft.format(new Date());
	}
	/**
	 * 得到: 月-日
	 * @param lefttime
	 * @return 月-日
	 */
	public static String formatDateSimple(long lefttime) {
		SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日",Locale.CHINA);
		String sDateTime = sdf.format(lefttime);  // 07月02日
		if (sDateTime.substring(3,4).equals("0")) { 
			sDateTime = sDateTime.substring(0, 3)+sDateTime.substring(4);
		}
		if(sDateTime.startsWith("0")){
			sDateTime =sDateTime.substring(1);
		}
		return sDateTime;
	}
	
	/**
	 * 字符串转为long
	 * @param time 字符串时间,注意:格式要与template定义的一样
	 * @param template 要格式化的格式:如time为09:21:12那么template为"HH:mm:ss"
	 * @return long
	 */
	public static long formatToLong(String time,String template) {
		SimpleDateFormat sdf = new SimpleDateFormat(template, Locale.CHINA);
		try {
			Date d = sdf.parse(time);
			Calendar c = Calendar.getInstance();
			c.setTime(d);
			long l = c.getTimeInMillis();
			return l;
		} catch (ParseException e) {
			e.printStackTrace();
			return 0;
		}
	}
	/**
	 * 得到年份
	 * @param lefttime
	 * @return 得到年份
	 */
	public static int formatYear(long lefttime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy",Locale.CHINA);
		String sDateTime = sdf.format(lefttime); 
		int i = Integer.parseInt(sDateTime);
		return i;
	}
	/**
	 * 得到月份
	 * @param lefttime
	 * @return 得到月份
	 */
	public static int formatMonth(long lefttime) {
		SimpleDateFormat sdf = new SimpleDateFormat("MM",Locale.CHINA);
		String sDateTime = sdf.format(lefttime); 
		int i = Integer.parseInt(sDateTime);
		return i;
	}
	
	/**
	 * 返回 yyyy-MM-dd
	 * 
	 * @param String 参数为String类型yyyy-MM-dd HH:mm:ss
	 * @return 返回 yyyy-MM-dd
	 * @throws ParseException 
	 */
	public static String exchangeStringDate(String date) throws ParseException {
		if (date != null && date.length() > 10) {
			String result = date.substring(0, 10);
			return result;
		}else{
			return null;
		}

	}
	/**
	 * 返回HH:mm:ss
	 * 
	 * @param String 参数为String类型
	 * @return 返回HH:mm:ss
	 * @throws ParseException 
	 */
	public static String exchangeStringTime(String date) throws ParseException {
		if (date != null && date.length() > 10) {
			String result = date.substring(10, date.length());
			return result;
		}else{
			return null;
		}
	}
}
