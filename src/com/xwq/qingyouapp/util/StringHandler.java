package com.xwq.qingyouapp.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
public class StringHandler {
	
	public static String getStringFromInputStream(InputStream inputStream) {  
	    java.io.InputStreamReader inputStreamReader = null;  
	    try {  
	        inputStreamReader = new java.io.InputStreamReader(inputStream, "utf-8");  
	    } catch (java.io.UnsupportedEncodingException e1) {  
	        e1.printStackTrace();  
	    }  
	    BufferedReader reader = new BufferedReader(inputStreamReader);  
	    StringBuffer sb = new StringBuffer("");  
	    String line;  
	    try {  
	        while ((line = reader.readLine()) != null) {  
	            sb.append(line);  
	            sb.append("\n");  
	        }  
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    }  
	    return sb.toString();  
	}  

	public static String objectToJsonString(Object obj) throws JSONException {
		Gson gson = new Gson();
		String json = gson.toJson(obj);
		JSONObject jo = new JSONObject(json);
		jo.put("key", "60528a45-af9c-439c-805e-2e607b0a1331");

		return jo.toString();
	}
	
	public static String limitLength(String str, int length) {

		return str.length() > length ? str.substring(0, length) + "..." : str;
	}

	public static String firstToUpper(String s) {
		if (Character.isUpperCase(s.charAt(0)))
			return s;
		else
			return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0)))
					.append(s.substring(1)).toString();
	}

	public static boolean isMobile(String phone) {
		phone = phone.replace(" ", "");
		Pattern p = null;
		Matcher m = null;
		boolean b = false;
		p = Pattern.compile("^[1][3,4,5,8][0-9]{9}$"); // 验证手机号
		m = p.matcher(phone);
		b = m.matches();
		return b;
	}

	public static boolean isEmail(String email) {
		Pattern emailPattern = Pattern
				.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
		Matcher matcher = emailPattern.matcher(email);
		if (matcher.find()) {
			return true;
		}
		return false;
	}

	public static String formatString(String str) {
		str.replace("\n", "");
		str.replace(" ", "");
		return str;
	}

	public static String removeTagFromString(String string, String tag) {
		int length = tag.length();
		int start = string.indexOf(tag);

		if (start == 0)
			string = string.substring(length + 1);
		else if (start + length == string.length())
			string = string.substring(0, start - 1);
		else
			string = string.replace("，" + tag, "");

		return string;
	}

	public static boolean tagInString(String string, String tag) {
		boolean b = false;
		String[] strArr = formatString(string).split("，");
		for (String str : strArr) {
			if (str.equals(tag)) {
				b = true;
				break;
			}
		}
		return b;
	}

}
