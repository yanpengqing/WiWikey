package com.wiwikeyandroid.utils;

/**
 * ━━━━ Code is far away from ━━━━━━ 　　 () 　　　 () 　　 ( ) 　　　( ) 　　 ( ) 　　　( )
 * 　　┏┛┻━━━┛┻┓ 　　┃　　　━　　　┃ 　　┃　┳┛　┗┳　┃ 　　┃　　　┻　　　┃ 　　┗━┓　　　┏━┛ 　　　　┃　　　┃
 * 　　　　┃　　　┗━━━┓ 　　　　┃　　　　　　　┣┓ 　　　　┃　　　　　　　┏┛ 　　　　┗┓┓┏━┳┓┏┛ 　　　　　┃┫┫　┃┫┫
 * 　　　　　┗┻┛　┗┻┛ ━━━━ bug with the XYY protecting━━━
 * 
 * @author Joseph on 2016/5/22.
 *         <p/>
 */
public class GlobalWiwikey {
	
	public static final String SOCKET_IP = "192.168.43.1";
	public static final int SOCKET_PORT = 11008;
	
	public static final String APPLICATION_NAME = "wiwikey";
	//单次最多发送图片数
	public static final int MAX_IMAGE_SIZE = 4;
	//临时图片
	public static final String PREF_TEMP_IMAGES = "pref_temp_images";
	
	public static final String DOORWIFI = "wisdog";
	/*
	 * 服务器请求地址
	 */
	public static final String URL_SERVER = "http://112.124.103.235:8080"; // 服务器主域名
	
	
	public static final String GET_VERIFYCODE = URL_SERVER
			+ "/interface/appMember/getVerifyCode";// 获取验证码地址

	public static final String URL_LOGIN = URL_SERVER + "/interface/appMember/login"; // 登录

	public static final String URL_GETMEMBERBYMOBILE = URL_SERVER
			+ "/interface/appMember/getMemberByMobile";  //  通过手机号查询会员信息
	
	public static final String URL_INITPASSWORD = URL_SERVER+"/interface/appMember/initPassword";//设置密码
	public static final String URL_CHANGEPASSWORD = URL_SERVER+"/interface/appMember/updatePassword";//修改密码
	public static final String URL_GETCITYLIST = URL_SERVER+"/interface/baseInfo/getCityList";//获取省市县(区)
	public static final String URL_GETPLOTLIST = URL_SERVER+"/interface/baseInfo/getPlotList";//通过县(区)获取此县(区)下所有的小区
	public static final String URL_GETHOUSELIST = URL_SERVER+"/interface/baseInfo/getHouseList";//通过小区ID与房号获取此小区中所有的此房号的信息
	public static final String URL_GETOWNERINFOBYID = URL_SERVER+"/interface/baseInfo/getOwnerInfoById";//通过房产ID获取业主信息
	public static final String URL_GETHOUSEAUTHVERIFYCODE = URL_SERVER+"/interface/baseInfo/getHouseAuthVerifyCode";//APP会员向业主申请房屋认证(发验证码)
	public static final String URL_DOHOUSEAUTHVERIFYCODE = URL_SERVER+"/interface/baseInfo/doHouseAuthVerifyCode";//房子认证确认，判断会员是否认证成功
	public static final String URL_GETMEMBERHOUSELIST = URL_SERVER+"/interface/baseInfo/getMemberHouseList";//通过会员ID,获取此会员绑定的小区,房产信息
	public static final String URL_GETPROPNOTICELIST = URL_SERVER+"/interface/appProperty/getPropNoticeList";//获取物业通知信息(物业通知/办事指南/业主须知/社区公告)
	public static final String URL_GETPROPNEWSLIST = URL_SERVER+"/interface/appProperty/getPropNewsList";//获取小区新闻相关信息
	public static final String URL_GETMEMBERLISTBYTYPE = URL_SERVER+"/interface/appMember/getMemberListByType";//获取不同类型的会员列表(物业，商家)
	public static final String URL_CALLAPPPUSH = URL_SERVER+"/interface/appPush/callAppPush";//App打电话不通时向对方推送消息
	public static final String URL_ENTERAPPRECODE = URL_SERVER+"/interface/appMember/enterAppRecode";//更新会员基础信息包括登录统计信息
	public static final String URL_PHONEOPENDOOR = URL_SERVER + "/interface/doorInfo/sendOpenDoor";//发送开门信息
	public static final String URL_REPAIRREPORT = URL_SERVER + "/interface/appProperty/repairReport";//报修申请
	public static final String URL_ADDCOMPLAIN = URL_SERVER + "/interface/appProperty/addComplain";//业主投诉
	public static final String URL_GETREPAIRLIST = URL_SERVER + "/interface/appProperty/getRepairList";//报修列表查询
	public static final String URL_GETCOMPLAINLIST = URL_SERVER + "/interface/appProperty/getComplainList";//投诉列表查询
	public static final String URL_CANCELREPAIR = URL_SERVER + "/interface/appProperty/cancelRepair";//取消报修
	public static final String URL_CANCELCOMPLAIN = URL_SERVER + "/interface/appProperty//cancelComplain";//取消投诉
	public static final String URL_INSERTOPENPASS = URL_SERVER + "/interface/appMember/insertOpenPass";//生成开门密码
	public static final String URL_ADDHOUSEHOLD = URL_SERVER + "/interface/appMember/addHousehold";//住户登记
	public static final String URL_GETHOUSEHOLDLIST = URL_SERVER + "/interface/appMember/getHouseholdList";//获取某房间中所有的住户
	public static final String URL_DELETEHOUSEHOLD = URL_SERVER + "/interface/appMember/deleteHousehold";//删除某房间中某个住户
	public static final String URL_UPDATEHOUSEHOLD = URL_SERVER + "/interface/appMember/updateHousehold";//修改某个住户的信息
	public static final String URL_UPDATEMEMBERINFO = URL_SERVER + "/interface/appMember/updateMemberInfo";//登录用户修改自己的资料
	public static final String URL_UPLOADIMG = URL_SERVER + "/interface/appProperty/uploadImg";//上传
	
	/**
	 * 请求码
	 */
	public static final int REQUEST_CODE = 0; // 获取验证码
	public static final int REQUEST_LOGIN = 1; // 登录
	public static final int REQUEST_GETMEMBERBYMOBILE = 2; // 通过手机号查询会员信息
	public static final int REQUEST_INITPASSWORD = 3; // 设置密码
	public static final int REQUEST_CHANGEPASSWORD = 4; // 修改密码
	public static final int REQUEST_GETCITYLIST = 5; // 获取省市县(区)
	public static final int REQUEST_GETPLOTLIST = 6; // 通过县(区)获取此县(区)下所有的小区
	public static final int REQUEST_GETHOUSELIST = 7; //通过小区ID与房号获取此小区中所有的此房号的信息
	public static final int REQUEST_GETOWNERINFOBYID = 8;//通过房产ID获取业主信息
	public static final int REQUEST_GETHOUSEAUTHVERIFYCODE = 9;//APP会员向业主申请房屋认证(发验证码)
	public static final int REQUEST_DOHOUSEAUTHVERIFYCODE = 10;//房子认证确认，判断会员是否认证成功
	public static final int REQUEST_GETMEMBERHOUSELIST = 11;//通过会员ID,获取此会员绑定的小区,房产信息
	public static final int REQUEST_GETPROPNOTICELIST = 12;//获取物业通知信息(物业通知/办事指南/业主须知/社区公告)
	public static final int REQUEST_GETPROPNEWSLIST = 13;//获取小区新闻
	public static final int REQUEST_GETMEMBERLISTBYTYPE_TENEMENT = 14;//获取不同类型的会员列表-物业
	public static final int REQUEST_GETMEMBERLISTBYTYPE_LIFE_SERVICE = 15;//获取不同类型的会员列表-生活服务
	public static final int REQUEST_ENTERAPPRECODE = 16;//更新会员基础信息包括登录统计信息
	public static final int REQUEST_PHONEOPENDOOR = 17;//开门信息
	public static final int REQUEST_REPAIRREPORT = 18;//报修申请
	public static final int REQUEST_ADDCOMPLAIN = 19;//业主投诉
	public static final int REQUEST_GETREPAIRLIST = 20;//报修列表查询
	public static final int REQUEST_GETCOMPLAINLIST = 21;//投诉列表查询
	public static final int REQUEST_CANCELREPAIR = 22;//取消保修
	public static final int REQUEST_ADDHOUSEHOLD = 23;//住户登记
	public static final int REQUEST_GETHOUSEHOLDLIST = 24;//获取某房间中所有的住户
	public static final int REQUEST_DELETEHOUSEHOLD = 25;//删除某房间中某个住户
	public static final int REQUEST_UPDATEMEMBERINFO = 26;//登录用户修改自己的资料
	
}
