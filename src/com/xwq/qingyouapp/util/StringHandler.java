package com.xwq.qingyouapp.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringHandler {

	public static String limitLength(String str, int length) {

		return str.length() > length ? str.substring(0, length) + "..." : str;
	}

	public static String firstToUpper(String s) {
		if (Character.isUpperCase(s.charAt(0)))
			return s;
		else
			return (new StringBuilder())
					.append(Character.toUpperCase(s.charAt(0)))
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

}
