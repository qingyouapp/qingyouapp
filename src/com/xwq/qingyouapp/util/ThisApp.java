package com.xwq.qingyouapp.util;

import java.util.ArrayList;
import java.util.List;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.xwq.qingyouapp.chat.CrashApplication;
import com.xwq.qingyouapp.chat.MyApplication;

import android.app.Activity;
import android.app.Application;

public class ThisApp extends Application {

	// 浏览图片时使用的全局变量
	public static int USER_ID_SELECTED = 0;
	public static int PHOTOS_SELECTED_ITEM = 0;

	public static ImageLoader imageLoader;

	private static List<Activity> activityList = new ArrayList<Activity>();

	public static void clearActivities() {
		for (Activity acti : activityList) {
			acti.finish();
		}
	}

	public static void addActivity(Activity activity) {
		activityList.add(activity);
	}

	@Override
	public void onCreate() {
		super.onCreate();

		// 创建默认的ImageLoader配置参数
		ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(this);

		// Initialize ImageLoader with configuration.
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(configuration);

		//chat
		//异常拦截记录
		CrashApplication.getInstance(this).onCreate();
		//初始化          

		SpeechUtility.createUtility(ThisApp.this, SpeechConstant.APPID +"=547e8752");
		//Android手机测试性能
		//Instrumentation.start("AD-AAB-AAA-PWM", getApplicationContext());

	}
}
