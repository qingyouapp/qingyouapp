package com.xwq.qingyouapp.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xwq.qingyouapp.bean.UserMetadata;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

@SuppressLint("CommitPrefEdits")
public class LocalStorage {

	private SharedPreferences sharedPref;
	private SharedPreferences.Editor editor;
	private Map<String, String> map;
	private Gson gson;

	@SuppressWarnings("unchecked")
	public LocalStorage(Context context) {
		sharedPref = context.getSharedPreferences("local_storage", Context.MODE_PRIVATE);
		editor = sharedPref.edit();
		map = (Map<String, String>) sharedPref.getAll();
		gson = new Gson();
	}

	@SuppressWarnings("unchecked")
	public void addData(List<KeyValue> list) {
		// Local storage
		for (KeyValue kv : list) {
			editor.putString(kv.getKey(), kv.getValue());
		}
		editor.commit();
		map = (Map<String, String>) sharedPref.getAll();
	}

	public String getData(String string) {
		return map.get(string);
	}

	public UserMetadata getUser() {
		String userString = map.get("userString");
		return gson.fromJson(userString, UserMetadata.class);
	}

	// 用户本人
	@SuppressWarnings("unchecked")
	public void setUser(UserMetadata user) {
		String userString = gson.toJson(user);
		editor.putString("userString", userString);
		// 存入单独的对象
		editor.putString("" + user.getUserid(), userString);
		editor.commit();
		map = (Map<String, String>) sharedPref.getAll();
	}

	public ArrayList<UserMetadata> getRecommendList() {
		String string = map.get("recommendList");
		return gson.fromJson(string, new TypeToken<ArrayList<UserMetadata>>() {
		}.getType());
	}

	// 推荐列表
	@SuppressWarnings("unchecked")
	public void setRecommendList(ArrayList<UserMetadata> list) {
		String string = gson.toJson(list);
		editor.putString("recommendList", string);
		// 存入单独的对象
		for (UserMetadata user : list) {
			editor.putString("" + user.getUserid(), gson.toJson(user));
		}
		editor.commit();
		map = (Map<String, String>) sharedPref.getAll();
	}

	public UserMetadata getUserById(int userid) {
		String userString = map.get("" + userid);
		return gson.fromJson(userString, UserMetadata.class);
	}

}
