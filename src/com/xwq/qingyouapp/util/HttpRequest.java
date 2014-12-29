package com.xwq.qingyouapp.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.xwq.qingyouapp.bean.UserMetadata;

public class HttpRequest {

	public static void main(String[] args) throws JSONException, ClientProtocolException,
			IOException, SQLException {
		UserMetadata um = new UserMetadata();
		um.setAge(1);
		um.setBirthday(new Date());
		um.setEmail("q@q.com");
		um.setSex("男");
		um.setPassword("123");
		um.setPhonenum("15253200737");
		Gson gson = new Gson();

		String json = gson.toJson(um);
		JSONObject jo = new JSONObject(json);
		jo.put("key", "60528a45-af9c-439c-805e-2e607b0a1331");

		String newJson = jo.toString();
		String inputEncode = "UTF-8";
		String outputEncode = "UTF-8";
		String url = "http://localhost:8080/qy/usermetadata/login?phonenum=123&password=戚拂晓";
		// System.out.println(get(url,outputEncode));
		// String url =
		// "http://localhost:8080/qy/usermetadata/login?phonenum=123&password=sdf";
		// System.out.println(get(url,inputEncode));

		post("http://localhost:8080/qy/usermetadata/regist", newJson, inputEncode, outputEncode);
	}

	public static JSONObject post(String url, String json, String inputEncode, String outputEncode)
			throws JSONException, IOException, ClientProtocolException,
			UnsupportedEncodingException {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost request = new HttpPost(url);
		StringEntity input = new StringEntity(json, inputEncode);
		input.setContentType("application/json");
		request.setEntity(input);
		HttpResponse response = httpclient.execute(request);
		BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity()
				.getContent(), outputEncode));
		StringBuilder sb = new StringBuilder();
		String line = null;
		while ((line = br.readLine()) != null) {
			sb.append(line + "\n");
		}
		httpclient.close();
		return new JSONObject(sb.toString());
	}

	public static JSONObject get(String url, String outputEncode) throws JSONException,
			IOException, ClientProtocolException, UnsupportedEncodingException {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet request = new HttpGet(url);
		HttpResponse response = httpclient.execute(request);
		BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity()
				.getContent(), outputEncode));
		StringBuilder sb = new StringBuilder();
		String line = null;
		while ((line = br.readLine()) != null) {
			sb.append(line + "\n");
		}
		httpclient.close();
		return new JSONObject(sb.toString());
	}
}