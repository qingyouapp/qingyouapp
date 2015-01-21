package com.xwq.qingyouapp.frag;

import java.util.Arrays;
import java.util.Date;

import org.json.JSONException;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xwq.qingyouapp.EditInfoActivity;
import com.xwq.qingyouapp.EditInfoActivity.TAG_TYPE;
import com.xwq.qingyouapp.PhotoShowActivity;
import com.xwq.qingyouapp.R;
import com.xwq.qingyouapp.bean.Discipline;
import com.xwq.qingyouapp.bean.Grade;
import com.xwq.qingyouapp.bean.School;
import com.xwq.qingyouapp.bean.UserMetadata;
import com.xwq.qingyouapp.util.DateHandler;
import com.xwq.qingyouapp.util.JsonHandler;
import com.xwq.qingyouapp.util.LocalStorage;
import com.xwq.qingyouapp.util.PhotoHandler;
import com.xwq.qingyouapp.util.PhotoHandler.ImageType;
import com.xwq.qingyouapp.util.StringHandler;
import com.xwq.qingyouapp.util.ThisApp;
import com.xwq.qingyouapp.view.LineBreakLayout;

public class ShowPageFrag extends Fragment {

	private TextView sexAgeView, constellationView, heightView, shuoshuoView,
			hometownView, eduView;
	private String hobbyStr;
	private String personalStr;
	private LineBreakLayout hobbyLBR, personalLBL;
	private LinearLayout photoLinearLayout;
	private UserMetadata user;

	private LocalStorage localStorage;
	private JsonHandler jsonHandler;
	private ImageLoader imageLoader;
	private PhotoHandler ph;
	private String[] photoNames;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.frag_show_page, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		getComponents();

		try {
			jsonHandler = new JsonHandler(getActivity());
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		localStorage = new LocalStorage(getActivity());
		imageLoader = ThisApp.imageLoader;
		ph = new PhotoHandler(getActivity());

		// 通过SHOW_USER_ID获取需要显示的用户信息
		user = localStorage.getUserById(ThisApp.SHOW_USER_ID);
		try {
			pullInLocalStorage(user);
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	public void getComponents() {
		sexAgeView = (TextView) getActivity().findViewById(R.id.sex_age);
		constellationView = (TextView) getActivity().findViewById(R.id.constellation);
		heightView = (TextView) getActivity().findViewById(R.id.height_cm);
		shuoshuoView = (TextView) getActivity().findViewById(R.id.shuoshuo_value);
		hometownView = (TextView) getActivity().findViewById(R.id.hometown_value);
		eduView = (TextView) getActivity().findViewById(R.id.education_value);
		hobbyLBR = (LineBreakLayout) getActivity().findViewById(R.id.hobby_break_layout);
		personalLBL = (LineBreakLayout) getActivity().findViewById(R.id.persoanl_break_layout);
		photoLinearLayout = (LinearLayout) getActivity().findViewById(R.id.photo_layout);
	}

	OnClickListener editBtnLis = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent(getActivity(), EditInfoActivity.class);
			startActivityForResult(intent, 1);
		}
	};

	@SuppressWarnings("deprecation")
	public void pullInLocalStorage(UserMetadata user) throws JSONException {

		// 获取基本数据
		String sex = user.getSex() == 0 ? "男" : "女";
		String birthday = DateHandler.dateToString(new Date(user.getBirthday()));
		String[] bitrhArr = birthday.split("-");
		int age = user.getAge();
		String constellation = DateHandler.constellationArr[user.getConstellation()];
		String height = user.getHeight() + "";
		// 获取需转换的数据
		Object school = jsonHandler.getBeanById(user.getUniversity(), JsonHandler.TYPE_SCHOOL);
		Object discipline = jsonHandler.getBeanById(user.getMajor(), JsonHandler.TYPE_DISCIPLINE);
		Object grade = jsonHandler.getBeanById(user.getGrade(), JsonHandler.TYPE_GRADE);
		String schoolStr = ((School) school).getName();
		String disciplineStr = ((Discipline) discipline).getName();
		String gradeStr = ((Grade) grade).getName();
		if (user.getHometownCity() != null && user.getHometownCity() != 0) {
			long[] lo = StringHandler.intToLongs(user.getHometownCity());
			String[] proArr = jsonHandler.getProvinceArray();
			String pro = proArr[(int) lo[0]];
			String[] cityArr = jsonHandler.getCityArrayByProvince(pro);
			String city = cityArr[(int) lo[1]];
			hometownView.setText(pro + "-" + city);
		}
		// 更新UI
		sexAgeView.setText(sex + age);
		constellationView.setText(constellation);
		heightView.setText(height + "cm");
		eduView.setText(schoolStr + "-" + disciplineStr + gradeStr);

		// 非基本信息，值可能为空
		if (!StringHandler.isNull(user.getSignature()))
			shuoshuoView.setText(user.getSignature());

		// tag标签 LinearBreakLayout
		hobbyStr = user.getInterests();
		personalStr = user.getSelfValue();
		EditInfoActivity
				.addTagsToBreakLayout(hobbyLBR, hobbyStr, TAG_TYPE.Hobby_tag, getActivity());
		EditInfoActivity.addTagsToBreakLayout(personalLBL, personalStr, TAG_TYPE.Personal_tag,
				getActivity());

		// 加载相册信息
		photoLinearLayout.removeAllViews();
		photoNames = user.getPhotoAlbum().trim().split(",");
		ImageView[] views = addImageViewsToLayout(photoNames.length);// 创建ImageView
		// 将头像放在最前面
		Arrays.sort(photoNames);
		for (int i = 0; i < photoNames.length; i++) {
			if (photoNames[i].equals(user.getHeadPortrait())) {
				String s = photoNames[i];
				photoNames[i] = photoNames[0];
				photoNames[0] = s;
			}
		}

		// 显示在UI
		String url = ph.getLocalAbsolutePath(user.getUserid(), ImageType.AlbumThumbnail);
		for (int i = 0; i < photoNames.length; i++) {
			if (ph.isExisted(url, photoNames[i])) {
				imageLoader.displayImage("file:///mnt" + url + photoNames[i], views[i]);
			} else {
				imageLoader.displayImage(ph.getServerPath(user.getUserid()) + photoNames[i],
						views[i]);
				ph.downloadImageFromServer(user.getUserid(), photoNames[i]);
			}
		}
	}

	@SuppressLint("ResourceAsColor")
	public ImageView[] addImageViewsToLayout(int size) {
		ImageView[] views = new ImageView[size];
		for (int i = 0; i < size; i++) {
			ImageView view = new ImageView(getActivity());
			view.setLayoutParams(new LayoutParams(ThisApp.Thumb_Width, ThisApp.Thumb_Width));
			view.setPadding(3, 3, 3, 3);
			view.setScaleType(ScaleType.FIT_XY);
			view.setContentDescription(i + "");// 将图片的位置下标作为参数传入ContentDescription
			photoLinearLayout.addView(view);
			view.setOnClickListener(photoLis);
			views[i] = view;
		}
		return views;
	}

	OnClickListener photoLis = new OnClickListener() {
		@SuppressLint("ShowToast")
		@Override
		public void onClick(View view) {
			ThisApp.USER_ID_SELECTED = user.getUserid();
			ThisApp.PHOTOS_SELECTED_ITEM = Integer
					.parseInt(view.getContentDescription().toString());
			ThisApp.PHOTO_NAME_ARRAY = photoNames;
			Intent intent = new Intent(getActivity(), PhotoShowActivity.class);
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