package com.xwq.qingyouapp;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.xwq.qingyouapp.bean.Discipline;
import com.xwq.qingyouapp.bean.Grade;
import com.xwq.qingyouapp.bean.School;
import com.xwq.qingyouapp.bean.UserMetadata;
import com.xwq.qingyouapp.command.CommandCallback;
import com.xwq.qingyouapp.command.Processor;
import com.xwq.qingyouapp.util.DateHandler;
import com.xwq.qingyouapp.util.JsonHandler;
import com.xwq.qingyouapp.util.LocalStorage;
import com.xwq.qingyouapp.util.PhotoHandler;
import com.xwq.qingyouapp.util.PhotoHandler.ImageType;
import com.xwq.qingyouapp.util.StringHandler;
import com.xwq.qingyouapp.util.ThisApp;
import com.xwq.qingyouapp.view.LineBreakLayout;
import com.xwq.qingyouapp.view.RangeSeekBar;

@SuppressLint("ResourceAsColor")
public class EditInfoActivity extends Activity {

	private TextView submitView, birthdayText, cancelView;
	private LineBreakLayout hobbyLBR, personalLBL;
	private String nickname, shuoshuo, height, weight, school, discipline, grade,
			hobbyStr, personalStr;
	private int prov_city;
	private Date birthdayDate;
	private EditText nicknameEdit, shuoshuoEdit;
	private Spinner heightSpin, weightSpin, schoolSpin, discipSpin, gradeSpin, provinceSpin,
			citySpin;
	private ArrayAdapter<String> provinceAdapter, cityAdapter;
	private String[] provinceArr, cityArr;
	private int yearInt = 1990;
	private int monthInt = 5;
	private int dayInt = 15;

	// upload photo
	private boolean photosChanged = false;
	private ImageView uploadImage;
	private LinearLayout photoLinearLyaout;
	private HashSet<String> photoPathsBegin = new HashSet<String>();
	private HashSet<String> photoPathsNow = new HashSet<String>();
	private HashSet<String> photoPathsNew = new HashSet<String>();
	private ArrayList<String> deletesPhotoList = new ArrayList<String>();
	private String deletesPhotos = "";
	private String allPhotos = "";
	private boolean headPicChanged = false;
	private Bitmap headPicBitmap = null;

	// The Uri to store the big bitmap
	@SuppressLint("SdCardPath")
	public static Uri IMAGE_URL = Uri.parse("file:///sdcard/.qingyoutemp.png");

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

		// Listeners
		cancelView.setOnClickListener(backLis);
		submitView.setOnClickListener(submitLis);
		birthdayText.setOnClickListener(birthdayLis);
		hobbyLBR.setOnClickListener(tagsLis);
		personalLBL.setOnClickListener(tagsLis);
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

		// LinearBreakLayout
		hobbyLBR = (LineBreakLayout) this.findViewById(R.id.hobby_break_layout);
		personalLBL = (LineBreakLayout) this.findViewById(R.id.persoanl_break_layout);

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
		birthdayDate = new Date(user.getBirthday());
		String birth = DateHandler.dateToString(birthdayDate);
		Object school = jsonHandler.getBeanById(user.getUniversity(), JsonHandler.TYPE_SCHOOL);
		Object discipline = jsonHandler.getBeanById(user.getMajor(), JsonHandler.TYPE_DISCIPLINE);
		Object grade = jsonHandler.getBeanById(user.getGrade(), JsonHandler.TYPE_GRADE);
		int schoolInt = ((School) school).getId();
		short disciplineSrt = ((Discipline) discipline).getId();
		short gradeSrt = ((Grade) grade).getId();
		birthdayDate = new Date(user.getBirthday());
		String[] birthArr = DateHandler.dateToString(birthdayDate).split("-");
		yearInt = Integer.parseInt(birthArr[0]);
		monthInt = Integer.parseInt(birthArr[1]);
		dayInt = Integer.parseInt(birthArr[2]);

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
		if (user.getInterests() != null) {
			hobbyStr = user.getInterests();
		}
		if (user.getSelfValue() != null) {
			personalStr = user.getSelfValue();
		}
		addTagsToBreakLayout(hobbyLBR, hobbyStr, TAG_TYPE.Hobby_tag, this);
		addTagsToBreakLayout(personalLBL, personalStr, TAG_TYPE.Personal_tag, this);

		// 加载相册
		ArrayList<Bitmap> list = photoHandler.getLocalBitmaps(user, ImageType.AlbumThumbnail);
		if (list != null && list.size() > 0)
			addPhotoListToLayout(list);
	}

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
			deleteLocalBitmap(deletesPhotoList);
		}
		// 处理图片添加
		int count = photoLinearLyaout.getChildCount();
		for (int i = 0; i < count; i++) {
			ImageView view = (ImageView) photoLinearLyaout.getChildAt(i);
			if ("new".equals(view.getContentDescription().toString())) {
				photosChanged = true;
				BitmapDrawable drawable = (BitmapDrawable) view.getBackground();
				Bitmap bitmap = drawable.getBitmap();
				// 分别保存原图和缩略图
				String imageName = "." + user.getUserid() + "_"
						+ Calendar.getInstance().getTimeInMillis() + ".png";
				photoHandler.saveBitmaps(user.getUserid(), imageName, bitmap);
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

	public void updateBasicInfo() throws JSONException {
		user = getSubmitUser();
		String url = getResources().getString(R.string.url_base)
				+ getResources().getString(R.string.url_update);
		processor.runCommand(url, StringHandler.userToJsonString(user), updateCallback);
	}

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
		height = heightSpin.getSelectedItem().toString();
		weight = weightSpin.getSelectedItem().toString();
		school = schoolSpin.getSelectedItemId() + "";
		discipline = discipSpin.getSelectedItemId() + "";
		grade = gradeSpin.getSelectedItemId() + "";
		long p = provinceSpin.getSelectedItemId();
		long c = citySpin.getSelectedItemId();
		prov_city = StringHandler.longToInt(p, c);
		int age = new Date().getYear() + 1900 - yearInt;
		int constallation = DateHandler.getConstellationInt(monthInt + 1, dayInt);
		// !important 放在最后，因为要用到上面的值
		String url = photoHandler.getLocalAbsolutePath(user.getUserid(), ImageType.Album);
		allPhotos = photoHandler.getLocalBitmapNames(url);

		user.setNickname(nickname);
		user.setSignature(shuoshuo);
		user.setBirthday(birthdayDate.getTime());
		user.setAge(age);
		user.setConstellation((short) constallation);
		user.setHeight((short) Integer.parseInt(height));
		user.setWeight((short) Integer.parseInt(weight));
		user.setUniversity(Integer.parseInt(school));
		user.setMajor((short) Integer.parseInt(discipline));
		user.setGrade((short) Integer.parseInt(grade));
		user.setInterests(hobbyStr);// hobbyStr, personalStr, taPersonalStr可直接使用
		user.setSelfValue(personalStr);
		user.setHometownCity(prov_city);
		user.setPhotoAlbum(allPhotos);

		// 处理头像更改
		if (headPicChanged) {
			String name = photoHandler.getBitmapName(user.getUserid(), headPicBitmap,
					ImageType.AlbumThumbnail);
			System.out.println("22222:" + name);
			user.setHeadPortrait(name);
		}

		return user;
	}

	@SuppressLint("NewApi")
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
					yearInt, monthInt, dayInt);
			datePicker.show();
		}
	};

	OnDateSetListener dateSetLis = new OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int month, int day) {
			birthdayText.setText(year + "-" + (month + 1) + "-" + day);
			birthdayDate = new Date(year - 1900, month, day);
			yearInt = year;
			monthInt = month;
			dayInt = day;
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

	@SuppressLint("NewApi")
	public void deleteLocalBitmap(ArrayList<String> bitmapNameList) {
		// 得到删除的字符串
		deletesPhotos = StringHandler.listToString(bitmapNameList);
		// 删除原图
		String url = photoHandler.getLocalAbsolutePath(user.getUserid(), ImageType.Album);
		File album = new File(url);
		File[] files = album.listFiles();
		for (File file : files) {
			if (deletesPhotos.indexOf(file.getName()) >= 0)
				file.delete();
		}
		// 删除缩略图
		String thumbUrl = photoHandler.getLocalAbsolutePath(user.getUserid(),
				ImageType.AlbumThumbnail);
		File albumThumb = new File(thumbUrl);
		File[] filesThumb = albumThumb.listFiles();
		for (File file : filesThumb) {
			if (deletesPhotos.indexOf(file.getName()) >= 0)
				file.delete();
		}
	}

	// 将图片放入UI中
	@SuppressWarnings("deprecation")
	public void addPhotoToLayout(Drawable drawable) {
		ImageView view = new ImageView(this);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ThisApp.Thumb_Width,
				ThisApp.Thumb_Width);
		lp.setMargins(3, 3, 3, 3);
		view.setLayoutParams(lp);
		view.setBackgroundDrawable(drawable);
		view.setContentDescription("new");// 表示是新图片
		photoLinearLyaout.addView(view);
		view.setOnClickListener(photoLis);
	}

	// 将图片放入UI中
	@SuppressWarnings("deprecation")
	public void addPhotoListToLayout(ArrayList<Bitmap> list) {
		if (list != null)
			for (int i = 0; i < list.size(); i++) {
				Bitmap bitmap = list.get(i);
				Drawable drawable = new BitmapDrawable(bitmap);
				ImageView view = new ImageView(this);
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ThisApp.Thumb_Width,
						ThisApp.Thumb_Width);
				lp.setMargins(3, 3, 3, 3);
				view.setLayoutParams(lp);
				view.setBackgroundDrawable(drawable);
				view.setContentDescription(i + "");// 将图片的位置下标作为参数传入ContentDescription
				photoLinearLyaout.addView(view);

				if (i == 0)
					view.setImageDrawable(getResources().getDrawable(R.drawable.head_pic_flag));
				else
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
										// 去掉当前头像的标识戳
										ImageView firstView = (ImageView) photoLinearLyaout
												.getChildAt(0);
										firstView.setImageDrawable(null);
										firstView.setOnClickListener(photoLis);
										// 将新头像放在第一个位置
										view.setOnClickListener(null);
										((ImageView) view).setImageDrawable(getResources()
												.getDrawable(R.drawable.head_pic_flag));
										photoLinearLyaout.removeView(view);
										photoLinearLyaout.addView(view, 0);
										// 保存头像信息，缩略图被再次压缩的话，得到的还是原图
										Drawable drawable = view.getBackground();
										headPicBitmap = ThumbnailUtils.extractThumbnail(
												((BitmapDrawable) drawable).getBitmap(),
												ThisApp.Thumb_Width, ThisApp.Thumb_Width);
										headPicChanged = true;
										break;
									case 1:
										// 获取被删除的bitmap
										String conDes = view.getContentDescription().toString();
										Drawable drawable1 = view.getBackground();
										Bitmap currBitmap = ThumbnailUtils.extractThumbnail(
												((BitmapDrawable) drawable1).getBitmap(),
												ThisApp.Thumb_Width, ThisApp.Thumb_Width);
										// 如果是旧照片，则删除本地
										if (!"new".equals(conDes)) {
											String name = photoHandler.getBitmapName(
													user.getUserid(), currBitmap,
													ImageType.AlbumThumbnail);
											deletesPhotoList.add(name);
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
		intent.putExtra("outputX", 1080);
		intent.putExtra("outputY", 1080);
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
	public void onBackPressed() {
		backWithoutSave();
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
