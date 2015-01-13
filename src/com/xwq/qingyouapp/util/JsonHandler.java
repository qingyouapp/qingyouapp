package com.xwq.qingyouapp.util;

import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xwq.qingyouapp.R;
import com.xwq.qingyouapp.bean.Discipline;
import com.xwq.qingyouapp.bean.Grade;
import com.xwq.qingyouapp.bean.School;

public class JsonHandler {

	public static final int TYPE_SCHOOL = 0;
	public static final int TYPE_DISCIPLINE = 1;
	public static final int TYPE_GRADE = 2;

	private JSONObject jb;
	private Gson gson;

	public JsonHandler(Context context) throws JSONException {
		InputStream is = context.getResources().openRawResource(R.raw.json);
		String str = StringHandler.getStringFromInputStream(is);
		this.jb = new JSONObject(str);
		this.gson = new Gson();
	}

	public String[] getProvinceArray() throws JSONException {
		JSONArray provinces = jb.getJSONArray("province");
		String proStr = provinces.toString();
		return gson.fromJson(proStr, new TypeToken<String[]>() {}.getType());
	}

	public String[] getCityArrayByProvince(String provinceName) throws JSONException {
		JSONArray provinces = jb.getJSONArray(provinceName);
		String proStr = provinces.toString();
		return gson.fromJson(proStr, new TypeToken<String[]>() {}.getType());
	}

	public Object getBeanByName(String name, int beanType) throws JSONException {
		Object object = null;
		switch (beanType) {
		case TYPE_SCHOOL:
			School school = null;
			JSONArray schools = jb.getJSONArray("school");
			ArrayList<School> schoolArr = gson.fromJson(schools.toString(),
					new TypeToken<ArrayList<School>>() {
					}.getType());
			for (School sch : schoolArr) {
				if (sch.getName().equals(name)) {
					school = sch;
					break;
				}
			}
			object = school;
			break;
		case TYPE_DISCIPLINE:
			Discipline discipline = null;
			JSONArray disciplines = jb.getJSONArray("discipline");
			ArrayList<Discipline> disciplineArr = gson.fromJson(disciplines.toString(),
					new TypeToken<ArrayList<Discipline>>() {
					}.getType());
			for (Discipline disc : disciplineArr) {
				if (disc.getName().equals(name)) {
					discipline = disc;
					break;
				}
			}
			object = discipline;
			break;

		case TYPE_GRADE:
			Grade grade = null;
			JSONArray grades = jb.getJSONArray("grade");
			ArrayList<Grade> gradeArr = gson.fromJson(grades.toString(),
					new TypeToken<ArrayList<Grade>>() {
					}.getType());
			for (Grade gra : gradeArr) {
				if (gra.getName().equals(name)) {
					grade = gra;
					break;
				}
			}
			object = grade;
			break;
		}

		return object;
	}

	public Object getBeanById(int id, int beanType) throws JSONException {
		Object object = null;

		switch (beanType) {
		case TYPE_SCHOOL:
			School school = null;
			JSONArray schools = jb.getJSONArray("school");
			ArrayList<School> schoolArr = gson.fromJson(schools.toString(),
					new TypeToken<ArrayList<School>>() {
					}.getType());
			for (School sch : schoolArr) {
				if (sch.getId() == id) {
					school = sch;
					break;
				}
			}
			object = school;
			break;
		case TYPE_DISCIPLINE:
			Discipline discipline = null;
			JSONArray disciplines = jb.getJSONArray("discipline");
			ArrayList<Discipline> disciplineArr = gson.fromJson(disciplines.toString(),
					new TypeToken<ArrayList<Discipline>>() {
					}.getType());
			for (Discipline disc : disciplineArr) {
				if (disc.getId() == (short) id) {
					discipline = disc;
					break;
				}
			}
			object = discipline;
			break;

		case TYPE_GRADE:
			Grade grade = null;
			JSONArray grades = jb.getJSONArray("grade");
			ArrayList<Grade> gradeArr = gson.fromJson(grades.toString(),
					new TypeToken<ArrayList<Grade>>() {
					}.getType());
			for (Grade gra : gradeArr) {
				if (gra.getId() == (short) id) {
					grade = gra;
					break;
				}
			}
			object = grade;
			break;
		}

		return object;
	}

}
