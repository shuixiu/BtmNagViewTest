package com.test.sixpro.utils;


import android.util.Log;

/**
 * log 打印类
 */
public class LogInfo {
	private final static int DEBUG_LEVEL = 4;
	private static boolean isLog = true;
	public static void log(String Tag, String msg){
		if(isLog){
			switch (DEBUG_LEVEL) {
			case 0:
				Log.i(Tag, msg);
				break;
			case 1:
				Log.d(Tag, msg);
				break;
			case 2:
				Log.v(Tag, msg);
				break;
			case 3:
				Log.w(Tag, msg);
				break;
			case 4:
				Log.e(Tag, msg);
				break;
			default:
				Log.i(Tag, msg);
				break;
			}
		}
	}
}

