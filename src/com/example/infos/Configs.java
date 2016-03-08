package com.example.infos;

import com.baidu.location.BDLocation;

import android.location.Location;

public class Configs {
	
	/**
	 * 网络请求的编码格式
	 */
	public static final String Uid = "uid";
	
	public static final String CHARSET = "utf-8";

	public static final String USERNAME = "username";

	public static final String START_X = "start_x";

	public static final String START_Y = "start_y";

	public static final String END_X = "end_x";

	public static final String END_Y = "end_y";

	public static final String START_NAME = "start_name";

	public static final String END_NAME = "end_name";

	public static final String PNUM = "pnum";

	public static final String LEAVE_TIME = "leave_time";

	/**
	 * 当前城市
	 */
	public static String CITY="济南";

	/**
	 * 当前用户是否已经绑定手机号
	 */
	public static boolean weatherBinded = false;
	
	/**
	 * 当前的坐标
	 */
	public static BDLocation mLocation;
	
	public static final String UID = "uid";
	
	public static final String FAULT_LINE = "fault_line";
	
	public static final String PHONE = "phone";
	
	/**
	 * 故障的图片URL
	 */
	public static final String IMG_URL = "img_url";

	public static final String FAULT_DETAIL = "fault_detail";
	
	public static final String BNAMME = "Bnamme";
	
	public static final String DATE = "Date";
	
	public static final String HYGIENE = "Hygiene";
	
	public static final String CROWD = "Crowd";
	
	public static final String ATTITUDE = "Attitude";
	
	public static final String MINUTE = "Minute";
	
	public static final String SUGGESTION = "Suggestion";

	public static final String TEL = "tel";

	public static final String COO_X = "coo_x";
	
	public static final String COO_Y = "coo_y";
	
	public static final String RADIUS="raidus";
	
	public static final String NICKNAME="nickname";
	
	public static final String SEX="sex";
	
	public static final String AGE="age";
	
	public static final String CITY2="city";
	
	public static double myLatitude = 0.0;
	public static double myLongtitude=0.0;


	
	
	/**
	 * 添加一条拼车信息的URL
	 */
	public static String URL_ADD_CARPOOL = "http://onthewayapp.sinaapp.com/add_carpool.php";

	/**
	 * 移动电视报修的URL
	 */
	public static String URL_test_repair_tv = "http://onthewayapp.sinaapp.com/repair_tv.php";
	
	/**
	 * 公交车评分的URL
	 */
	public static String URL_test_grade_bus = "http://onthewayapp.sinaapp.com/grade_bus.php";
	
	/**
	 * 设置常用路线的URL
	 */
	public static String URL_SET_REGULAR_ROUTE = "http://onthewayapp.sinaapp.com/set_common_route.php";
	
	/**
	 * 获得我的拼车信息URL
	 */
	public static String URL_GET_MYCARPOOL_MEASSAGE = "http://onthewayapp.sinaapp.com/get_carpool.php";
	
	/**
	 * 获得周围拼车信息
	 */
	public static String URL_GET_AROUND = "http://onthewayapp.sinaapp.com/get_around_info.php";
	
	/**
	 * 修改用户基本信息
	 */
	public static String URL_CHANGE_BASE = "http://onthewayapp.sinaapp.com/change_base_info.php";
	
	/**
	 * 获取用户基本信息
	 */
	public static String URL_GET_BASE_INFO = "http://onthewayapp.sinaapp.com/get_base_info.php";
	
	
	public static String TEST_USER = "15165136991";
	public static String T_UID= "11";
	
	
}
