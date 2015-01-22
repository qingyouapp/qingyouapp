package com.xwq.qingyouapp.sms;

import java.util.HashMap;
import java.util.Map;

public class SmsHandler {
	
	public static void sendMessage(String photoNumber, String identifyNumber) {
		/**
		 * F4yTr612VAbH模板内容为：【青柚科技】您本次的验证码是：%P%。请勿向其他人泄露此验证码。如非您本人操作，请忽略本消息。
		 */
		Map<String, String> para = new HashMap<String, String>();
		para.put("mob", photoNumber);
		para.put("uid", "sgejRxiZAZYL");
		para.put("pas", "24avmy8j");
		para.put("type", "json");
		para.put("cid", "F4yTr612VAbH");
		para.put("p1", identifyNumber);

		try {
			System.out.println(HttpClientHelper.convertStreamToString(
					HttpClientHelper.get("http://api.weimi.cc/2/sms/send.html", para), "UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 短信接口二，触发类模板短信接口，可以设置动态参数变量。适合：验证码、订单短信等。
	 */
	public static void sms_api2() {

		Map<String, String> para = new HashMap<String, String>();

		/**
		 * 目标手机号码，多个以“,”分隔，一次性调用最多100个号码，示例：139********,138********
		 */
		para.put("mob", "<enter your mobiles>");

		/**
		 * 微米账号的接口UID
		 */
		para.put("uid", "<enter your UID>");

		/**
		 * 微米账号的接口密码
		 */
		para.put("pas", "<enter your UID Pass>");

		/**
		 * 接口返回类型：json、xml、txt。默认值为txt
		 */
		para.put("type", "json");

		/**
		 * 短信模板cid，通过微米后台创建，由在线客服审核。必须设置好短信签名，签名规范： <br>
		 * 1、模板内容一定要带签名，签名放在模板内容的最前面；<br>
		 * 2、签名格式：【***】，签名内容为三个汉字以上（包括三个）；<br>
		 * 3、短信内容不允许双签名，即短信内容里只有一个“【】”<br>
		 */
		para.put("cid", "<enter your cid>");

		/**
		 * 传入模板参数。<br>
		 * <br>
		 * 短信模板示例：<br>
		 * 【微米网】您的验证码是：%P%，%P%分钟内有效。如非您本人操作，可忽略本消息。<br>
		 * <br>
		 * 传入两个参数：<br>
		 * p1：610912<br>
		 * p2：3<br>
		 * 最终发送内容：<br>
		 * 【微米网】您的验证码是：610912，3分钟内有效。如非您本人操作，可忽略本消息。
		 */
		para.put("p1", "610912");
		para.put("p2", "3");

		try {
			System.out.println(HttpClientHelper.convertStreamToString(
					HttpClientHelper.get("http://api.weimi.cc/2/sms/send.html", para), "UTF-8"));

			System.out.println(HttpClientHelper.convertStreamToString(
					HttpClientHelper.post("http://api.weimi.cc/2/sms/send.html", para), "UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 短信接口一，自写短信内容。该接口提交的短信均由人工审核，下发后请联系在线客服。适合：节假日祝福、会员营销群发等。
	 */
	public static void sms_api1() {

		Map<String, String> para = new HashMap<String, String>();

		/**
		 * 目标手机号码，多个以“,”分隔，一次性调用最多100个号码，示例：139********,138********
		 */
		para.put("mob", "<enter your mobiles>");

		/**
		 * 微米账号的接口UID
		 */
		para.put("uid", "<enter your UID>");

		/**
		 * 微米账号的接口密码
		 */
		para.put("pas", "<enter your UID Pass>");

		/**
		 * 接口返回类型：json、xml、txt。默认值为txt
		 */
		para.put("type", "json");

		/**
		 * 短信内容。必须设置好短信签名，签名规范： <br>
		 * 1、短信内容一定要带签名，签名放在短信内容的最前面；<br>
		 * 2、签名格式：【***】，签名内容为三个汉字以上（包括三个）；<br>
		 * 3、短信内容不允许双签名，即短信内容里只有一个“【】”
		 * 
		 */
		para.put("con", "【微米网】您的验证码是：610912，3分钟内有效。如非您本人操作，可忽略本消息。");

		try {
			System.out.println(HttpClientHelper.convertStreamToString(
					HttpClientHelper.get("http://api.weimi.cc/2/sms/send.html", para), "UTF-8"));

			System.out.println(HttpClientHelper.convertStreamToString(
					HttpClientHelper.post("http://api.weimi.cc/2/sms/send.html", para), "UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] a) {

		// 测试短信接口一
		sms_api1();

		// 测试短信接口二
		sms_api2();

	}

}
