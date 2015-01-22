package com.xwq.qingyouapp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xwq.qingyouapp.bean.School;
import com.xwq.qingyouapp.util.StringHandler;

public class TestActivity extends Activity {

	private static String uploadFile = "/WebRoot/test.jpg";
	private static String actionUrl = "http://192.168.1.222:8080/pic/upload";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);


	}

	public static void method() {
		HttpClient httpclient = new DefaultHttpClient();

		try {
			JSONObject jo = new JSONObject();
			jo.put("key", "60528a45-af9c-439c-805e-2e607b0a1331");
			jo.put("userid", 1);
			jo.put("password", "123456");
			HttpPost httppost = new HttpPost(actionUrl);
			FileBody bin = new FileBody(new File(System.getProperty("user.dir") + uploadFile));
			StringBody comment = new StringBody(jo.toString(), ContentType.APPLICATION_JSON);

			HttpEntity reqEntity = MultipartEntityBuilder.create().addPart("bin", bin)
					.addPart("comment", comment).build();

			httppost.setEntity(reqEntity);

			System.out.println("executing request " + httppost.getRequestLine());
			HttpResponse response = httpclient.execute(httppost);
			System.out.println("----------------------------------------");
			System.out.println(response.getStatusLine());
			HttpEntity resEntity = response.getEntity();
			if (resEntity != null) {
				System.out.println("Response content length: " + resEntity.getContentLength());
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
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
