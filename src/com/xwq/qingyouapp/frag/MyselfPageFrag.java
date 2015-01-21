package com.xwq.qingyouapp.frag;

import java.util.ArrayList;

import org.json.JSONException;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xwq.qingyouapp.EditInfoActivity;
import com.xwq.qingyouapp.IdealSettingActivity;
import com.xwq.qingyouapp.LoginActivity;
import com.xwq.qingyouapp.MyShowActivity;
import com.xwq.qingyouapp.R;
import com.xwq.qingyouapp.bean.School;
import com.xwq.qingyouapp.bean.UserMetadata;
import com.xwq.qingyouapp.util.JsonHandler;
import com.xwq.qingyouapp.util.KeyValue;
import com.xwq.qingyouapp.util.LocalStorage;
import com.xwq.qingyouapp.util.PhotoHandler;
import com.xwq.qingyouapp.util.ThisApp;

public class MyselfPageFrag extends Fragment {

	private TextView titleView, nameView, schoolView, idealTaView, myPageView, exitAccountView;
	private ImageView photoView;
	private RelativeLayout layout;

	private JsonHandler jsonHandler;
	private LocalStorage localStorage;
	private PhotoHandler photoHandler;
	private UserMetadata user;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.frag_myself_page, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		localStorage = new LocalStorage(getActivity());
		photoHandler = new PhotoHandler(getActivity());
		try {
			jsonHandler = new JsonHandler(getActivity());
		} catch (JSONException e) {
			e.printStackTrace();
		}

		// 初始化UI
		getComponents();

		// 填充数据到UI
		user = localStorage.getUser();
		try {
			pullInLocalStorage(user);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		layout.setOnClickListener(layoutLis);
		idealTaView.setOnClickListener(idealTaLis);
		myPageView.setOnClickListener(myPageViewLis);
		exitAccountView.setOnClickListener(exitAccountLis);
	}

	public void getComponents() {
		titleView = (TextView) this.getView().findViewById(R.id.toolbar_title);
		nameView = (TextView) this.getView().findViewById(R.id.name);
		schoolView = (TextView) this.getView().findViewById(R.id.school);
		photoView = (ImageView) this.getView().findViewById(R.id.photo);
		layout = (RelativeLayout) this.getView().findViewById(R.id.relativeLayout1);
		idealTaView = (TextView) this.getView().findViewById(R.id.ideal_ta_setting);
		myPageView = (TextView) this.getView().findViewById(R.id.my_page_show);
		exitAccountView = (TextView) this.getView().findViewById(R.id.exit_account);
	}

	public void pullInLocalStorage(UserMetadata user) throws JSONException {
		// 获取数据
		String nickname = user.getNickname();
		Object school = jsonHandler.getBeanById(user.getUniversity(), JsonHandler.TYPE_SCHOOL);
		String schoolStr = ((School) school).getName();

		titleView.setText(getResources().getString(R.string.me));
		nameView.setText(nickname);
		schoolView.setText(schoolStr);
		Bitmap bitmap = photoHandler.getHeadPortrait(user);
		if (bitmap != null)
			photoView.setImageBitmap(bitmap);
	}

	OnClickListener myPageViewLis = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			ThisApp.SHOW_USER_ID = user.getUserid();
			Intent intent = new Intent(getActivity(), MyShowActivity.class);
			startActivity(intent);
		}
	};

	OnClickListener layoutLis = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent(getActivity(), EditInfoActivity.class);
			startActivityForResult(intent, 1);
		}
	};

	OnClickListener idealTaLis = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent(getActivity(), IdealSettingActivity.class);
			startActivity(intent);
		}
	};

	OnClickListener exitAccountLis = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			new AlertDialog.Builder(getActivity()).setTitle(null).setMessage("确定退出账号吗？")
					.setPositiveButton("是", exitAccountTrue).setNegativeButton("否", null).show();
		}
	};

	android.content.DialogInterface.OnClickListener exitAccountTrue = new android.content.DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface arg0, int arg1) {
			getActivity().finish();
			// 清除登录密码
			ArrayList<KeyValue> list = new ArrayList<KeyValue>();
			list.add(new KeyValue("my_pwd", ""));
			localStorage.addData(list);
			// 跳转到登录页面
			Intent intent = new Intent(getActivity(), LoginActivity.class);
			startActivity(intent);
		}
	};

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		String str = data.getStringExtra("user");
		if (!str.equals("cancelled")) {
			Gson gson = new Gson();
			UserMetadata user = gson.fromJson(str, UserMetadata.class);
			try {
				pullInLocalStorage(user);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

}