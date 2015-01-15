package com.xwq.qingyouapp.chat.util;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {
	public static void show(Context context, String content) {
		Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
	}
}
