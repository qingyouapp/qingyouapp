package com.xwq.qingyouapp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.xwq.qingyouapp.bean.School;
import com.xwq.qingyouapp.util.StringHandler;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class TestActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);

		final TextView textView = (TextView) this.findViewById(R.id.textView1);

		new AsyncTask<Object, Object, String>() {

			@Override
			protected String doInBackground(Object... arg0) {

				InputStream is = getResources().openRawResource(R.raw.json);
				String str = StringHandler.getStringFromInputStream(is);

				try {
					JSONObject jb = new JSONObject(str);
					JSONArray schools = jb.getJSONArray("school");
					Gson gson = new Gson();
					ArrayList<School> schoolArr = gson.fromJson(schools.toString(),
							new TypeToken<ArrayList<School>>() {
							}.getType());

					System.out.println(schoolArr.get(4).getName());
				} catch (JSONException e) {
					e.printStackTrace();
				}

				return null;
			}
		}.execute();

		// System.out.println("11111:"+isr.toString());;
		// textView.setText(xmlParser.getAttributeCount());

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.test, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
