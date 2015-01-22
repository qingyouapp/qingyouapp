package com.xwq.qingyouapp.chat;

//import com.appdynamics.eumagent.runtime.Instrumentation;
//import com.appdynamics.eumagent.runtime.Instrumentation;
import android.app.Application;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

public class MyApplication extends Application {
	public static final String APPKEY = "60528a45-af9c-439c-805e-2e607b0a1331";
	
	@Override
	public void onCreate() { 
		super.onCreate();
		//异常拦截记录
//		CrashApplication.getInstance(this).onCreate();
//		//初始化          
//		
//		SpeechUtility.createUtility(MyApplication.this, SpeechConstant.APPID +"=547e8752");
		//Android手机测试性能
		//Instrumentation.start("AD-AAB-AAA-PWM", getApplicationContext());
		
	}
}
