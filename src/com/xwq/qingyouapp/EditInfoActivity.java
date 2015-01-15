package com.xwq.qingyouapp;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;

import org.json.JSONException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.xwq.qingyouapp.bean.AgeBT;
import com.xwq.qingyouapp.bean.Discipline;
import com.xwq.qingyouapp.bean.FriendStandards;
import com.xwq.qingyouapp.bean.Grade;
import com.xwq.qingyouapp.bean.GradeBT;
import com.xwq.qingyouapp.bean.HeightBT;
import com.xwq.qingyouapp.bean.School;
import com.xwq.qingyouapp.bean.UserMetadata;
import com.xwq.qingyouapp.bean.WeightBT;
import com.xwq.qingyouapp.command.CommandCallback;
import com.xwq.qingyouapp.command.Processor;
import com.xwq.qingyouapp.util.DateHandler;
import com.xwq.qingyouapp.util.JsonHandler;
import com.xwq.qingyouapp.util.LocalStorage;
import com.xwq.qingyouapp.util.PhotoHandler;
import com.xwq.qingyouapp.util.PhotoHandler.ImageType;
import com.xwq.qingyouapp.util.StringHandler;
import com.xwq.qingyouapp.view.LineBreakLayout;
import com.xwq.qingyouapp.view.RangeSeekBar;
import com.xwq.qingyouapp.view.RangeSeekBar.OnRangeSeekBarChangeListener;

@SuppressLint("ResourceAsColor")
public class EditInfoActivity extends Activity {

	private TextView submitView, birthdayText, cancelView;
	private RelativeLayout ageLayout, heightLayout, weightLayout;
	private LineBreakLayout hobbyLBR, personalLBL, taPersonalLBL;
	private String nickname, shuoshuo, birthday, height, weight, school, discipline, grade,
			hobbyStr, personalStr, taPersonalStr;
	private int prov_city;
	private EditText nicknameEdit, shuoshuoEdit;
	private FriendStandards fs;
	private Spinner heightSpin, weightSpin, schoolSpin, discipSpin, gradeSpin, provinceSpin,
			citySpin;
	private ArrayAdapter<String> provinceAdapter, cityAdapter;
	private String[] provinceArr, cityArr;

	// friend standards
	private RadioGroup radioGroup;
	private RadioButton maleRadio, femaleRadio, allRadio;
	private RangeSeekBar<Integer> ageBar, heightBar, weightBar;
	private TextView ageMin, ageMax, heightMin, heightMax, weightMin, weightMax;
	private int ageS = 16;
	private int ageE = 30;
	private int heightS = 140;
	private int heightE = 230;
	private int weightS = 30;
	private int weightE = 100;

	// upload photo
	private boolean photosChanged = false;
	private ImageView uploadImage;
	private LinearLayout photoLinearLyaout;
	private HashSet<String> photoPathsBegin = new HashSet<String>();
	private HashSet<String> photoPathsNow = new HashSet<String>();
	private HashSet<String> photoPathsNew = new HashSet<String>();
	private ArrayList<Integer> deletesPhotoList = new ArrayList<Integer>();
	private String deletesPhotos = "";
	private String allPhotos = "";
	private final String IMAGE_FILE_LOCATION = "file:///sdcard/qingyoutemp.png";// temp
																				// file
	private Uri IMAGE_URL = Uri.parse(IMAGE_FILE_LOCATION);// The Uri to store
															// the big bitmap

	public static final int TAG_HOBBY = 4;
	public static final int TAG_PERSONAL = 5;
	public static final int TAG_TA_PERSONAL = 6;

	private LocalStorage localStorage;
	private UserMetadata user;
	private JsonHandler jsonHandler;
	private Processor processor;
	private PhotoHandler photoHandler;

	public enum TAG_TYPE {
		Hobby_tag, Personal_tag, Ta_personal_tag
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_edit_info);

		getComponents();
		photoHandler = new PhotoHandler(this);

		// Spinner
		try {
			jsonHandler = new JsonHandler(this);
			setProvCitySpinner();
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		heightSpin.setAdapter(createArrayAdapter(R.array.height));
		weightSpin.setAdapter(createArrayAdapter(R.array.weight));
		schoolSpin.setAdapter(createArrayAdapter(R.array.school));
		discipSpin.setAdapter(createArrayAdapter(R.array.discipline));
		gradeSpin.setAdapter(createArrayAdapter(R.array.grade));

		// RangerBar
		ageBar = new RangeSeekBar<Integer>(ageS, ageE, this);
		heightBar = new RangeSeekBar<Integer>(heightS, heightE, this);
		weightBar = new RangeSeekBar<Integer>(weightS, weightE, this);
		ageBar.setOnRangeSeekBarChangeListener(taAgeLis);
		heightBar.setOnRangeSeekBarChangeListener(taHeightLis);
		weightBar.setOnRangeSeekBarChangeListener(taWeightLis);
		ageLayout.addView(ageBar);
		heightLayout.addView(heightBar);
		weightLayout.addView(weightBar);

		// Listeners
		cancelView.setOnClickListener(backLis);
		submitView.setOnClickListener(submitLis);
		birthdayText.setOnClickListener(birthdayLis);
		hobbyLBR.setOnClickListener(tagsLis);
		personalLBL.setOnClickListener(tagsLis);
		taPersonalLBL.setOnClickListener(tagsLis);
		uploadImage.setOnClickListener(uploadImageLis);

		// 本地存储
		try {
			localStorage = new LocalStorage(this);
			user = localStorage.getUser();
			pullInLocalStorage();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		// photos

		String url = photoHandler.getLocalAbsolutePath(user.getUserid(), ImageType.Album);
		photoPathsBegin = photoHandler.getLocalBitmapPaths(url);

	}

	public void getComponents() {

		cancelView = (TextView) this.findViewById(R.id.cancel_back);
		submitView = (TextView) this.findViewById(R.id.edit_submit);
		birthdayText = (TextView) this.findViewById(R.id.birthday_value);

		// RangeSeekBar
		ageLayout = (RelativeLayout) this.findViewById(R.id.age_layout);
		heightLayout = (RelativeLayout) this.findViewById(R.id.height_layout);
		weightLayout = (RelativeLayout) this.findViewById(R.id.weight_layout);
		ageMin = (TextView) this.findViewById(R.id.age_min);
		ageMax = (TextView) this.findViewById(R.id.age_max);
		heightMin = (TextView) this.findViewById(R.id.height_min);
		heightMax = (TextView) this.findViewById(R.id.height_max);
		weightMin = (TextView) this.findViewById(R.id.weight_min);
		weightMax = (TextView) this.findViewById(R.id.weight_max);

		// LinearBreakLayout
		hobbyLBR = (LineBreakLayout) this.findViewById(R.id.hobby_break_layout);
		personalLBL = (LineBreakLayout) this.findViewById(R.id.persoanl_break_layout);
		taPersonalLBL = (LineBreakLayout) this.findViewById(R.id.ta_persoanl_break_layout);

		// edittext
		nicknameEdit = (EditText) this.findViewById(R.id.nickname_value);
		shuoshuoEdit = (EditText) this.findViewById(R.id.shuoshuo_value);

		// spinner
		heightSpin = (Spinner) this.findViewById(R.id.height_value);
		weightSpin = (Spinner) this.findViewById(R.id.weight_value);
		schoolSpin = (Spinner) this.findViewById(R.id.school_value);
		discipSpin = (Spinner) this.findViewById(R.id.discipline_value);
		gradeSpin = (Spinner) this.findViewById(R.id.grade_value);
		provinceSpin = (Spinner) this.findViewById(R.id.province);
		citySpin = (Spinner) this.findViewById(R.id.city);

		// friend standards
		radioGroup = (RadioGroup) this.findViewById(R.id.radioGroup);
		maleRadio = (RadioButton) this.findViewById(R.id.radioMale);
		femaleRadio = (RadioButton) this.findViewById(R.id.radioFemale);
		allRadio = (RadioButton) this.findViewById(R.id.radioAll);

		// upload photo
		uploadImage = (ImageView) this.findViewById(R.id.upload_image);
		photoLinearLyaout = (LinearLayout) this.findViewById(R.id.photo_linearLayout);
	}

	private void setProvCitySpinner() throws JSONException {

		provinceArr = jsonHandler.getProvinceArray();
		provinceAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, provinceArr);
		provinceSpin.setAdapter(provinceAdapter);
		provinceSpin.setSelection(0, true);

		try {
			cityArr = jsonHandler.getCityArrayByProvince("北京");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		cityAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, cityArr);
		citySpin.setAdapter(cityAdapter);
		citySpin.setSelection(0, true);

		// 省级下拉框监听
		provinceSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
				String proName = provinceSpin.getSelectedItem().toString();
				try {
					cityArr = jsonHandler.getCityArrayByProvince(proName);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				cityAdapter = new ArrayAdapter<String>(EditInfoActivity.this,
						R.layout.spinner_item, cityArr);
				// 设置二级下拉列表的选项内容适配器
				citySpin.setAdapter(cityAdapter);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
	}

	public void pullInLocalStorage() throws JSONException {
		// 获取基本数据
		int height = user.getHeight();
		String birth = DateHandler.dateToString(new Date(user.getBirthday()));
		Object school = jsonHandler.getBeanById(user.getUniversity(), JsonHandler.TYPE_SCHOOL);
		Object discipline = jsonHandler.getBeanById(user.getMajor(), JsonHandler.TYPE_DISCIPLINE);
		Object grade = jsonHandler.getBeanById(user.getGrade(), JsonHandler.TYPE_GRADE);
		int schoolInt = ((School) school).getId();
		short disciplineSrt = ((Discipline) discipline).getId();
		short gradeSrt = ((Grade) grade).getId();

		// 更新UI
		nicknameEdit.setText(user.getNickname());
		heightSpin.setSelection(height - 140, true);
		schoolSpin.setSelection(schoolInt, true);
		discipSpin.setSelection(disciplineSrt, true);
		gradeSpin.setSelection(gradeSrt, true);
		birthdayText.setText(birth);
		// 非基本信息，值可能为空
		if (!StringHandler.isNull(user.getSignature()))
			shuoshuoEdit.setText(user.getSignature());
		if (user.getWeight() != null) {
			int weight = user.getWeight();
			weightSpin.setSelection(weight - 30, true);
		}
		if (user.getHometownCity() != null) {
			long[] proCity = StringHandler.intToLongs(user.getHometownCity());
			provinceSpin.setSelection((int) proCity[0], true);
			citySpin.setSelection((int) proCity[1], true);
		}
		// tag标签 LinearBreakLayout
		hobbyStr = "";
		personalStr = "";
		taPersonalStr = "";
		if (user.getInterests() != null) {
			hobbyStr = user.getInterests();
		}
		if (user.getSelfValue() != null) {
			personalStr = user.getSelfValue();
		}
		addTagsToBreakLayout(hobbyLBR, hobbyStr, TAG_TYPE.Hobby_tag, this);
		addTagsToBreakLayout(personalLBL, personalStr, TAG_TYPE.Personal_tag, this);

		// friend standards
		if (!StringHandler.isNull(user.getFriendStandards())) {
			String fsStr = user.getFriendStandards();
			Gson gson = new Gson();
			FriendStandards fs = gson.fromJson(fsStr, FriendStandards.class);
			// update UI
			// sex
			switch (fs.getSex()) {
			case 0:
				maleRadio.setChecked(true);
				break;
			case 1:
				femaleRadio.setChecked(true);
				break;
			case 2:
				allRadio.setChecked(true);
				break;
			}
			// tags
			taPersonalStr = fs.getTags();
			addTagsToBreakLayout(taPersonalLBL, taPersonalStr, TAG_TYPE.Ta_personal_tag, this);
			// age, height, weight
			int ageL = fs.getAge().getAgeleft();
			int ageR = fs.getAge().getAgeright();
			int heightL = fs.getHeight().getHeightleft();
			int heightR = fs.getHeight().getHeightright();
			int weightL = fs.getWeight().getWeightleft();
			int weightR = fs.getWeight().getWeightright();

			heightMin.setText(heightL + "");
			heightMax.setText(heightR + "");
			weightMin.setText(weightL + "");
			weightMax.setText(weightR + "");
			ageMin.setText(ageL + "");
			ageMax.setText(ageR + "");

			initRangeSeekBar(ageBar, ageS, ageE, ageL, ageR);
			initRangeSeekBar(heightBar, heightS, heightE, heightL, heightR);
			initRangeSeekBar(weightBar, weightS, weightE, weightL, weightR);
		}
		// 加载相册
		String url = photoHandler.getLocalAbsolutePath(user.getUserid(), ImageType.AlbumThumbnail);
		ArrayList<Bitmap> list = photoHandler.getLocalBitmap(url);
		if (list != null && list.size() > 0)
			addPhotoListToLayout(list);
	}

	public void initRangeSeekBar(RangeSeekBar<Integer> bar, int start, int end, int min, int max) {
		double a = (double) (min - start) / (end - start);
		double b = (double) (max - start) / (end - start);
		bar.setLeft(min);
		bar.setRight(max);
		bar.setNormalizedMinValue(a);
		bar.setNormalizedMaxValue(b);
	}

	OnClickListener tagsLis = new OnClickListener() {
		@Override
		public void onClick(View view) {
			int num = 0;
			String string = "";
			if (view == hobbyLBR) {
				num = TAG_HOBBY;
				string = hobbyStr;
			} else if (view == personalLBL) {
				num = TAG_PERSONAL;
				string = personalStr;
			} else if (view == taPersonalLBL) {
				num = TAG_TA_PERSONAL;
				string = taPersonalStr;
			}
			Intent intent = new Intent(EditInfoActivity.this, TagsEditActivity.class);
			intent.putExtra("tag_code", num);
			intent.putExtra("tag_string", string);
			startActivityForResult(intent, num);
		}
	};
	OnClickListener birthdayLis = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			DatePickerDialog datePicker = new DatePickerDialog(EditInfoActivity.this, dateSetLis,
					1990, 5, 15);
			datePicker.show();
		}
	};

	OnDateSetListener dateSetLis = new OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int month, int day) {
			birthdayText.setText(year + "-" + (month + 1) + "-" + day);
		}
	};

	OnRangeSeekBarChangeListener<Integer> taAgeLis = new OnRangeSeekBarChangeListener<Integer>() {
		@Override
		public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue,
				Integer maxValue) {
			// TODO Auto-generated method stub
			ageMin.setText(minValue.toString());
			ageMax.setText(maxValue.toString());
		}
	};

	OnRangeSeekBarChangeListener<Integer> taHeightLis = new OnRangeSeekBarChangeListener<Integer>() {
		@Override
		public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue,
				Integer maxValue) {
			// TODO Auto-generated method stub
			heightMin.setText(minValue.toString());
			heightMax.setText(maxValue.toString());
		}
	};

	OnRangeSeekBarChangeListener<Integer> taWeightLis = new OnRangeSeekBarChangeListener<Integer>() {
		@Override
		public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue,
				Integer maxValue) {
			// TODO Auto-generated method stub
			weightMin.setText(minValue.toString());
			weightMax.setText(maxValue.toString());
		}
	};

	OnClickListener backLis = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			backWithoutSave();
		}
	};

	OnClickListener uploadImageLis = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			showPhotoSelectDialog();
		}
	};

	OnClickListener submitLis = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			// 首先update图片，更新图片目录，然后再提交基本信息（包含了最新的图片路径信息）
			try {
				processor = Processor.instance(EditInfoActivity.this);
				refreshPhotos();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	};

	public void refreshPhotos() throws JSONException {
		// 处理图片删除
		if (deletesPhotoList.size() > 0) {
			photosChanged = true;
			Collections.sort(deletesPhotoList);
			int l = deletesPhotoList.size();
			for (int i = l - 1; i >= 0; i--) {
				deleteLocalBitmap(deletesPhotoList.get(i));
			}
		}
		// 处理图片添加
		int count = photoLinearLyaout.getChildCount();
		for (int i = 0; i < count; i++) {
			ImageView view = (ImageView) photoLinearLyaout.getChildAt(i);
			if ("new".equals(view.getContentDescription().toString())) {
				photosChanged = true;
				BitmapDrawable drawable = (BitmapDrawable) view.getDrawable();
				Bitmap bitmap = drawable.getBitmap();
				// 分别保存原图和缩略图
				String iamgeName = user.getUserid() + "_"
						+ Calendar.getInstance().getTimeInMillis() + ".png";
				photoHandler
						.saveBitmapToLocal(bitmap, iamgeName, user.getUserid(), ImageType.Album);
				photoHandler.saveBitmapToLocal(ThumbnailUtils.extractThumbnail(bitmap, 240, 240),
						iamgeName, user.getUserid(), ImageType.AlbumThumbnail);
			}
		}
		// 图片有改动时提交,否则直接更新基本信息
		if (photosChanged) {
			String url = photoHandler.getLocalAbsolutePath(user.getUserid(), ImageType.Album);
			photoPathsNow = photoHandler.getLocalBitmapPaths(url);
			photoPathsNew = photoHandler.getNewPhotosPath(photoPathsBegin, photoPathsNow);
			processor.refreshPhoto(photoPathsNew, deletesPhotos, user, photoCallback);
		} else {
			updateBasicInfo();
		}
	}

	public void updateBasicInfo() throws JSONException {
		user = getSubmitUser();
		String url = getResources().getString(R.string.url_base)
				+ getResources().getString(R.string.url_update);
		processor.runCommand(url, StringHandler.userToJsonString(user), updateCallback);
	}

	CommandCallback photoCallback = new CommandCallback() {
		@SuppressLint("ShowToast")
		@Override
		public void excute(String jb) {
			if ("200".equals(jb.trim())) {
				try {
					updateBasicInfo();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				Toast.makeText(getApplication(),
						getResources().getString(R.string.server_exception), Toast.LENGTH_SHORT)
						.show();
			}
		}
	};

	CommandCallback updateCallback = new CommandCallback() {
		@SuppressLint("ShowToast")
		@Override
		public void excute(String jb) {
			if ("valid".equals(jb.trim())) {
				saveSucceedAndBack();
			} else {
				Toast.makeText(getApplication(),
						getResources().getString(R.string.server_exception), Toast.LENGTH_SHORT)
						.show();
			}
		}
	};

	// 保存成功
	public void saveSucceedAndBack() {
		localStorage.setUser(user);
		Intent intent = new Intent();
		Gson gson = new Gson();
		intent.putExtra("user", gson.toJson(user));
		setResult(1, intent);
		EditInfoActivity.this.finish();
		Toast.makeText(getApplication(), getResources().getString(R.string.submit_success),
				Toast.LENGTH_SHORT).show();
	}

	// 取消更改
	public void backWithoutSave() {
		Intent intent = new Intent();
		intent.putExtra("user", "cancelled");
		setResult(1, intent);
		EditInfoActivity.this.finish();
	}

	// 获取提交的USER信息
	public UserMetadata getSubmitUser() throws JSONException {
		// 获取基本的输入值
		nickname = nicknameEdit.getText().toString();
		shuoshuo = shuoshuoEdit.getText().toString();
		birthday = birthdayText.getText().toString();
		height = heightSpin.getSelectedItem().toString();
		weight = weightSpin.getSelectedItem().toString();
		school = schoolSpin.getSelectedItemId() + "";
		discipline = discipSpin.getSelectedItemId() + "";
		grade = gradeSpin.getSelectedItemId() + "";
		long p = provinceSpin.getSelectedItemId();
		long c = citySpin.getSelectedItemId();
		prov_city = StringHandler.longToInt(p, c);
		// !important 放在最后，因为要用到上面的值
		fs = getFriendStandards();
		String url = photoHandler.getLocalAbsolutePath(user.getUserid(), ImageType.Album);
		allPhotos = photoHandler.getLocalBitmapNames(url);

		user = localStorage.getUser();
		user.setNickname(nickname);
		user.setSignature(shuoshuo);
		user.setBirthday(DateHandler.stringToDate(birthday).getTime());
		user.setHeight((short) Integer.parseInt(height));
		user.setWeight((short) Integer.parseInt(weight));
		user.setUniversity(Integer.parseInt(school));
		user.setMajor((short) Integer.parseInt(discipline));
		user.setGrade((short) Integer.parseInt(grade));
		user.setInterests(hobbyStr);// hobbyStr, personalStr, taPersonalStr可直接使用
		user.setSelfValue(personalStr);
		user.setHometownCity(prov_city);
		user.setFriendStandards(StringHandler.objectToJsonString(fs));
		user.setPhotoAlbum(allPhotos);

		return user;
	}

	// 提取交友标准
	public FriendStandards getFriendStandards() {
		// 性别。提前把radiobutton的hint值设为了0，1，2
		RadioButton box = (RadioButton) this.findViewById(radioGroup.getCheckedRadioButtonId());
		String hint = box.getHint().toString();
		short sex = (short) Integer.parseInt(hint);
		// 身高，体重，年龄
		short h_min = (short) Integer.parseInt(heightMin.getText().toString());
		short h_max = (short) Integer.parseInt(heightMax.getText().toString());
		short w_min = (short) Integer.parseInt(weightMin.getText().toString());
		short w_max = (short) Integer.parseInt(weightMax.getText().toString());
		int a_min = Integer.parseInt(ageMin.getText().toString());
		int a_max = Integer.parseInt(ageMax.getText().toString());
		HeightBT height = new HeightBT(h_min, h_max);
		WeightBT weight = new WeightBT(w_min, w_max);
		AgeBT age = new AgeBT(a_min, a_max);
		// 年级，目前UI上没有展现
		short g = (short) Integer.parseInt(grade);
		short g_min = (short) (g - 1 < 0 ? 0 : g - 1);
		short g_max = (short) (g + 1 > 10 ? 10 : g + 1);
		GradeBT grade = new GradeBT(g_min, g_max);
		// 城市默认为本人所在城市，目前UI上没有展现
		fs = new FriendStandards(sex, height, weight, age, grade, taPersonalStr, prov_city);
		return fs;
	}

	public static void addTagsToBreakLayout(LineBreakLayout layout, String tagString,
			TAG_TYPE tagType, Context context) {
		layout.removeAllViews();// 清空已有标签
		if (tagString == null || tagString.equals(""))
			tagString = "未填写";
		String[] tags = StringHandler.formatString(tagString).split("，");// 中文逗号“，”为分隔符
		int bg_color = 0;
		switch (tagType) {
		case Hobby_tag:
			bg_color = context.getResources().getColor(R.color.hobby_tag_bg);
			break;
		case Personal_tag:
			bg_color = context.getResources().getColor(R.color.personal_tag_bg);
			break;
		case Ta_personal_tag:
			bg_color = context.getResources().getColor(R.color.personal_tag_bg);
			break;
		}
		if (tags.length > 0) {
			for (String tag : tags) {
				TextView view = new TextView(context);
				view.setText(tag);
				view.setTextColor(context.getResources().getColor(R.color.white));
				view.setTextSize(15);
				view.setPadding(10, 3, 10, 3);
				view.setBackgroundColor(bg_color);
				view.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
						LayoutParams.WRAP_CONTENT));
				layout.addView(view);
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_CANCELED)
			switch (requestCode) {
			// 处理上传图片的回传结果
			case ImageUploadActivity.IMAGE_REQUEST_CODE:
				startPhotoZoom(data.getData());
				break;
			case ImageUploadActivity.CAMERA_REQUEST_CODE:
				if (ImageUploadActivity.hasSdcard()) {
					File tempFile = new File(Environment.getExternalStorageDirectory() + "/"
							+ ImageUploadActivity.IMAGE_FILE_NAME);
					startPhotoZoom(Uri.fromFile(tempFile));
				} else {
					Toast.makeText(EditInfoActivity.this, "未找到存储卡，无法存储照片！", Toast.LENGTH_LONG)
							.show();
				}
				break;
			case ImageUploadActivity.RESULT_REQUEST_CODE:
				getImageToView();
				break;
			// 标签的回传结果
			case TAG_HOBBY:
				hobbyStr = data.getStringExtra("tagString");
				addTagsToBreakLayout(hobbyLBR, hobbyStr, TAG_TYPE.Hobby_tag, this);
				break;
			case TAG_PERSONAL:
				personalStr = data.getStringExtra("tagString");
				addTagsToBreakLayout(personalLBL, personalStr, TAG_TYPE.Personal_tag, this);
				break;
			case TAG_TA_PERSONAL:
				taPersonalStr = data.getStringExtra("tagString");
				addTagsToBreakLayout(taPersonalLBL, taPersonalStr, TAG_TYPE.Ta_personal_tag, this);
				break;
			}

		super.onActivityResult(requestCode, resultCode, data);
	}

	private void showPhotoSelectDialog() {
		new AlertDialog.Builder(this)
				.setTitle(null)
				.setItems(ImageUploadActivity.items, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0:
							Intent intentFromGallery = new Intent();
							intentFromGallery.setType("image/*"); // 设置文件类型
							intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
							startActivityForResult(intentFromGallery,
									ImageUploadActivity.IMAGE_REQUEST_CODE);
							break;
						case 1:
							Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
							// 判断存储卡是否可以用，可用进行存储
							if (ImageUploadActivity.hasSdcard()) {
								intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri
										.fromFile(new File(Environment
												.getExternalStorageDirectory(),
												ImageUploadActivity.IMAGE_FILE_NAME)));
							}
							startActivityForResult(intentFromCapture,
									ImageUploadActivity.CAMERA_REQUEST_CODE);
							break;
						}
					}
				})
				.setNegativeButton(getResources().getString(R.string.cancel),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
							}
						}).show();

	}

	/**
	 * 保存裁剪之后的图片数据
	 * 
	 * @param picdata
	 */
	private void getImageToView() {
		Bitmap photo = decodeUriAsBitmap(IMAGE_URL);
		@SuppressWarnings("deprecation")
		Drawable drawable = new BitmapDrawable(photo);
		addPhotoToLayout(drawable);
		photosChanged = true;
	}

	private Bitmap decodeUriAsBitmap(Uri uri) {
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		return bitmap;
	}

	public void deleteLocalBitmap(int position) {
		String url = photoHandler.getLocalAbsolutePath(user.getUserid(), ImageType.Album);
		File album = new File(url);
		File[] files = album.listFiles();
		String thumbUrl = photoHandler.getLocalAbsolutePath(user.getUserid(),
				ImageType.AlbumThumbnail);
		File albumThumb = new File(thumbUrl);
		File[] filesThumb = albumThumb.listFiles();

		if (deletesPhotos.equals("")) {
			deletesPhotos = files[position].getName();
		} else
			deletesPhotos += "," + files[position].getName();
		// 分别删除原图和缩略图
		files[position].delete();
		filesThumb[position].delete();
	}

	// 将图片放入UI中
	public void addPhotoToLayout(Drawable drawable) {
		ImageView view = new ImageView(this);
		view.setLayoutParams(new LayoutParams(240, 240));
		view.setPadding(3, 3, 3, 3);
		view.setImageDrawable(drawable);
		view.setContentDescription("new");// 表示是新图片
		photoLinearLyaout.addView(view);
		view.setOnClickListener(photoLis);
	}

	// 将图片放入UI中
	public void addPhotoListToLayout(ArrayList<Bitmap> list) {
		if (list != null)
			for (int i = 0; i < list.size(); i++) {
				Bitmap bitmap = list.get(i);
				@SuppressWarnings("deprecation")
				Drawable drawable = new BitmapDrawable(bitmap);
				ImageView view = new ImageView(this);
				view.setLayoutParams(new LayoutParams(240, 240));
				view.setPadding(3, 3, 3, 3);
				view.setImageDrawable(drawable);
				view.setContentDescription(i + "");// 将图片的位置下标作为参数传入ContentDescription
				photoLinearLyaout.addView(view);
				view.setOnClickListener(photoLis);
			}
	}

	OnClickListener photoLis = new OnClickListener() {
		@Override
		public void onClick(final View view) {
			new AlertDialog.Builder(EditInfoActivity.this)
					.setTitle(null)
					.setItems(new String[] { "设为头像", "删除照片" },
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									switch (which) {
									case 0:
										break;
									case 1:
										String conDes = view.getContentDescription().toString();
										if (!"new".equals(conDes)) {
											deletesPhotoList.add(Integer.parseInt(conDes));
										}
										photoLinearLyaout.removeView(view);
										break;
									}
								}
							})
					.setNegativeButton(getResources().getString(R.string.cancel),
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									dialog.dismiss();
								}
							}).show();
		}
	};

	/**
	 * 裁剪图片方法实现
	 * 
	 * @param uri
	 */
	public void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 设置裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 控制宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 720);
		intent.putExtra("outputY", 720);
		intent.putExtra("return-data", false);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, IMAGE_URL);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());
		intent.putExtra("noFaceDetection", true); // no face detection
		startActivityForResult(intent, ImageUploadActivity.RESULT_REQUEST_CODE);
	}

	public ArrayAdapter<String> createArrayAdapter(int Rid) {
		return new ArrayAdapter<String>(this, R.layout.spinner_item, getResources().getStringArray(
				Rid));
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			backWithoutSave();
		}
		return false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.edit_info, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
