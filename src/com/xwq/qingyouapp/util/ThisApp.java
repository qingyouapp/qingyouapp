package com.xwq.qingyouapp.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;

import android.app.Activity;
import android.app.Application;


public class ThisApp extends Application {
	
	public static String currSystemAddress = "1.2.3.4";
	public static float capacity = -1;
	public static Timer timer;
	public static Date lastDate;
	
	private static List<Activity> activityList = new ArrayList<Activity>();
	
	public static void clearActivities() {
		for (Activity acti : activityList) {
			acti.finish();
		}
	}

	public static void addActivity(Activity activity) {
		activityList.add(activity);
	}

}
