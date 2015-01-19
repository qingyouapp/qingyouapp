package com.xwq.qingyouapp.command;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.xwq.qingyouapp.R;
import com.xwq.qingyouapp.bean.UserMetadata;

public class Processor {

	private AsyncTask<?, ?, ?> task;
	private static Processor p = null;
	private Context context;

	public static Processor instance(Context context) {
		p = instance();
		p.setContext(context);
		return p;
	}

	public static Processor instance() {
		if (p == null) {
			p = new Processor();
		}
		return p;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	/**
	 * 处理以下业务请求： 登录、 注册、 手机号验证、
	 * 
	 * @param url
	 * @param json
	 * @param callback
	 */
	@SuppressWarnings("unchecked")
	public void runCommand(final String url, final String json, final CommandCallback callback) {

		task = new AsyncTask<Object, Object, String>() {
			@Override
			protected String doInBackground(Object... params) {

				System.out.println("post url--" + url);
				System.out.println("post data--" + json);
				String result = null;
				if (new ConnectionDetector(context).isConnectingToInternet()) {
					try {
						result = post(url, json);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					result = "network_exception";
				}
				return result;
			}

			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);
				System.out.println("post result--" + result);
				if (result == "network_exception") {
					Toast.makeText(context,
							context.getResources().getString(R.string.network_exception),
							Toast.LENGTH_SHORT).show();
				} else if (result == null) {
					Toast.makeText(context,
							context.getResources().getString(R.string.server_exception),
							Toast.LENGTH_SHORT).show();
				} else if (callback != null)
					callback.excute(result);
			}
		};
		task.execute();
	}

	public String post(String url, String json) throws IOException, ClientProtocolException,
			UnsupportedEncodingException, JSONException {

		HttpClient httpclient = new DefaultHttpClient();
		HttpPost request = new HttpPost(url);
		StringEntity input = new StringEntity(json, "utf-8");
		input.setContentType("application/json");
		request.setEntity(input);
		HttpResponse response = httpclient.execute(request);
		BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity()
				.getContent(), "utf-8"));
		StringBuilder sb = new StringBuilder();
		String line = null;
		while ((line = br.readLine()) != null) {
			sb.append(line);
		}
		return sb.toString();
	}

	/**
	 * 处理图片上传
	 * 
	 * @param paths
	 * @param deletes
	 * @param user
	 * @param callback
	 */
	@SuppressWarnings("unchecked")
	public void refreshPhoto(final HashSet<String> paths, final String deletes,
			final UserMetadata user, final CommandCallback callback) {
		task = new AsyncTask<Object, Object, Integer>() {
			@Override
			protected Integer doInBackground(Object... params) {

				int result = 0;
				if (new ConnectionDetector(context).isConnectingToInternet()) {
					try {
						result = photoPost(paths, deletes, user);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					result = -1;
				}
				return result;
			}

			@Override
			protected void onPostExecute(Integer result) {
				super.onPostExecute(result);
				System.out.println("post result--" + result);
				if (result == -1) {
					Toast.makeText(context,
							context.getResources().getString(R.string.network_exception),
							Toast.LENGTH_SHORT).show();
				} else if (result == 0) {
					Toast.makeText(context,
							context.getResources().getString(R.string.server_exception),
							Toast.LENGTH_SHORT).show();
				} else if (callback != null)
					callback.excute(result + "");
			}
		};
		task.execute();
	}

	/**
	 * 上传图片
	 * 
	 * @param paths
	 * @param deletes
	 * @param user
	 * @return 上传结果码，成功为200
	 */
	public int photoPost(HashSet<String> paths, String deletes, UserMetadata user) {
		HttpClient httpclient = new DefaultHttpClient();
		int statusCode = 0;
		deletes = deletes == null ? "" : deletes;
		
		try {
			JSONObject jo = new JSONObject();
			jo.put("key", "60528a45-af9c-439c-805e-2e607b0a1331");
			jo.put("userid", user.getUserid());
			jo.put("password", user.getPassword());
			HttpPost httppost = new HttpPost(context.getResources().getString(
					R.string.url_photos_refresh));

			StringBody jsonBody = new StringBody(jo.toString(), ContentType.APPLICATION_JSON);
			StringBody deletesBody = new StringBody(deletes, ContentType.TEXT_PLAIN);
			MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
			if (paths != null && paths.size() > 0)
				for (String filePath : paths) {
					FileBody fileBody = new FileBody(new File(filePath));
					multipartEntityBuilder.addPart(filePath, fileBody);
				}
			multipartEntityBuilder.addPart("json", jsonBody);
			multipartEntityBuilder.addPart("deletes", deletesBody);

			HttpEntity reqEntity = multipartEntityBuilder.build();
			httppost.setEntity(reqEntity);

			System.out.println("executing request " + httppost.getRequestLine());
			HttpResponse response = httpclient.execute(httppost);
			statusCode = response.getStatusLine().getStatusCode();

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return statusCode;
	}

}
