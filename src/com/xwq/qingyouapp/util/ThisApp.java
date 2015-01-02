package com.xwq.qingyouapp.util;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Application;


public class ThisApp extends Application {
	
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
