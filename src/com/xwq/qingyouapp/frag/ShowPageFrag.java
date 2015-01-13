package com.xwq.qingyouapp.frag;

import java.util.ArrayList;
import java.util.Date;

import org.json.JSONException;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xwq.qingyouapp.EditInfoActivity;
import com.xwq.qingyouapp.EditInfoActivity.TAG_TYPE;
import com.xwq.qingyouapp.PhotoShowActivity;
import com.xwq.qingyouapp.R;
import com.xwq.qingyouapp.TaMainActivity;
import com.xwq.qingyouapp.bean.Discipline;
import com.xwq.qingyouapp.bean.Grade;
import com.xwq.qingyouapp.bean.School;
import com.xwq.qingyouapp.bean.UserMetadata;
import com.xwq.qingyouapp.util.DateHandler;
import com.xwq.qingyouapp.util.JsonHandler;
import com.xwq.qingyouapp.util.LocalStorage;
import com.xwq.qingyouapp.util.PhotoHandler;
import com.xwq.qingyouapp.util.StringHandler;
import com.xwq.qingyouapp.util.ThisApp;
import com.xwq.qingyouapp.util.PhotoHandler.ImageType;
import com.xwq.qingyouapp.view.LineBreakLayout;

public class ShowPageFrag extends Fragment {

	private ImageButton editBtn;
	private TextView titleView, sexAgeView, constellationView, heightView, shuoshuoView,
			hometownView, eduView;
	private String hobbyStr;
	private String personalStr;
	private LineBreakLayout hobbyLBR, personalLBL;
	private LocalStorage localStorage;
	private JsonHandler jsonHandler;
	private LinearLayout photoLinearLayout;
	private UserMetadata user;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.frag_show_page, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		// 当showpage用于显示他人的信息时，隐藏title并更改margin设置，用于适应TaMain的总体布局
		if (this.getActivity().getClass().equals(TaMainActivity.class)) {
			getActivity().findViewById(R.id.showpage_title).setVisibility(View.GONE);
			@SuppressWarnings("deprecation")
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
					LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
			lp.setMargins(0, 0, 0, 0);
			getActivity().findViewById(R.id.showpage_scrollView).setLayoutParams(lp);
		}

		getComponents();

		try {
			jsonHandler = new JsonHandler(getActivity());
		} catch (JSONException e1) {
			e1.printStackTrace();
		}

		// 本地存储
		localStorage = new LocalStorage(getActivity());
		user = localStorage.getUser();
		try {
			pullInLocalStorage(user);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		editBtn.setOnClickListener(editBtnLis);
	}

	public void getComponents() {
		titleView = (TextView) getActivity().findViewById(R.id.homepage_title);
		editBtn = (ImageButton) getActivity().findViewById(R.id.edit_btn);
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
		titleView.setText(user.getNickname());
		sexAgeView.setText(sex + age);
		constellationView.setText(constellation);

		heightView.setText(height + "cm");
		eduView.setText(schoolStr + "-" + disciplineStr + gradeStr);
		// 非基本信息，值可能为空
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
		PhotoHandler ph = new PhotoHandler(getActivity());
		String url = ph.getLocalAbsolutePath(user.getUserid(), ImageType.Album);
		ArrayList<Bitmap> list = ph.getLocalBitmap(url);
		if (list != null && list.size() > 0) {
			addPhotoListToLayout(list);
		}
	}

	public void addPhotoListToLayout(ArrayList<Bitmap> list) {
		if (list != null)
			for (int i = 0; i < list.size(); i++) {
				Bitmap bitmap = list.get(i);
				bitmap = ThumbnailUtils.extractThumbnail(bitmap, 240, 240);
				@SuppressWarnings("deprecation")
				Drawable drawable = new BitmapDrawable(bitmap);
				ImageView view = new ImageView(getActivity());
				view.setLayoutParams(new LayoutParams(240, 240));
				view.setPadding(3, 3, 3, 3);
				view.setImageDrawable(drawable);
				view.setContentDescription(i + "");// 将图片的位置下标作为参数传入ContentDescription
				photoLinearLayout.addView(view);
				view.setOnClickListener(photoLis);
			}
	}

	OnClickListener photoLis = new OnClickListener() {
		@SuppressLint("ShowToast")
		@Override
		public void onClick(View view) {
			ThisApp.USER_ID_SELECTED = user.getUserid();
			ThisApp.PHOTOS_SELECTED_ITEM = Integer
					.parseInt(view.getContentDescription().toString());
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