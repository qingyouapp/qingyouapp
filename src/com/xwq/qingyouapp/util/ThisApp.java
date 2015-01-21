package com.xwq.qingyouapp.util;

import java.util.ArrayList;
import java.util.List;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;

public class ThisApp extends Application {

	// 缩略图的像素点
	public static int Thumb_Width = GraphicsUtil.dipToPixel(120);

	// 浏览图片时使用的全局变量
	public static int USER_ID_SELECTED = 0;
	public static int PHOTOS_SELECTED_ITEM = 0;
	public static String[] PHOTO_NAME_ARRAY;

	// 查看主页时存储的UserId
	public static int SHOW_USER_ID = 0;

	public static ImageLoader imageLoader;

	private static List<Activity> activityList = new ArrayList<Activity>();

	@SuppressLint("NewApi")
	public static void clearActivities() {
		for (Activity acti : activityList) {
			activityList.remove(acti);
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

	}
}
