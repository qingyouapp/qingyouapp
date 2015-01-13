package com.xwq.qingyouapp.util;

import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.xwq.qingyouapp.bean.UserMetadata;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

@SuppressLint("CommitPrefEdits")
public class LocalStorage {

	private SharedPreferences sharedPref;
	private SharedPreferences.Editor editor;
	private Map<String, String> map;

	@SuppressWarnings("unchecked")
	public LocalStorage(Context context) {
		sharedPref = context.getSharedPreferences("local_storage", Context.MODE_PRIVATE);
		editor = sharedPref.edit();
		map = (Map<String, String>) sharedPref.getAll();
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
		Gson gson = new Gson();
		return gson.fromJson(userString, UserMetadata.class);
	}

	public void setUser(UserMetadata user) {
		Gson gson = new Gson();
		String userString = gson.toJson(user);
		editor.putString("userString", userString);
		editor.commit();
		map = (Map<String, String>) sharedPref.getAll();
	}

}
