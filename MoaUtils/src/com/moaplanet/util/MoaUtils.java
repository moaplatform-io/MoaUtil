package com.moaplanet.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MoaUtils {

	static public String getCurrentTimeStr() {
		return getCurrentTimeStr("yyyy-MM-dd HH:mm:ss");
	}
	
	static public String getCurrentTimeStr(String strDateFormat) {
		return new SimpleDateFormat(strDateFormat).format(new Date(System.currentTimeMillis()));
	}
	
}
