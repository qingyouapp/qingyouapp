package com.xwq.qingyouapp;

import com.xwq.qingyouapp.util.StringHandler;
import com.xwq.qingyouapp.view.LineBreakLayout;
import com.xwq.qingyouapp.view.RangeSeekBar;
import com.xwq.qingyouapp.view.RangeSeekBar.OnRangeSeekBarChangeListener;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

@SuppressLint("ResourceAsColor")
public class EditInfoActivity extends Activity {

	private HorizontalScrollView photoScrollView;
	private ImageButton backBtn;
	private TextView submitView, birthdayText;
	private LinearLayout photoLinearLyaout;
	private TextView ageMin, ageMax, heightMin, heightMax, weightMin, weightMax;
	private RelativeLayout ageLayout, heightLayout, weightLayout;
	private LineBreakLayout hobbyLBR, personalLBL, taPersonalLBL;
	private String hobbyStr, personalStr, taPersonalStr;

	public static final int TAG_HOBBY = 1;
	public static final int TAG_PERSONAL = 2;
	public static final int TAG_TA_PERSONAL = 3;

	public enum TAG_TYPE {
		Hobby_tag, Personal_tag, Ta_personal_tag
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_edit_info);

		getComponents();

		hobbyStr = new String("美食，音乐，电影，旅游，泡妞，自由自在，读书");
		personalStr = new String("身材好，大美女，体魄壮，爱说话，自信，幽默风趣，活泼，爱运动");
		taPersonalStr = new String("活泼，爱运动，身材好，性感，自信，幽默风趣，潇洒");

		backBtn.setOnClickListener(backLis);
		submitView.setOnClickListener(submitLis);
		birthdayText.setOnClickListener(birthdayLis);

		RangeSeekBar<Integer> ageBar = new RangeSeekBar<Integer>(16, 30, this);
		RangeSeekBar<Integer> heightBar = new RangeSeekBar<Integer>(140, 230, this);
		RangeSeekBar<Integer> weightBar = new RangeSeekBar<Integer>(30, 100, this);

		ageBar.setOnRangeSeekBarChangeListener(taAgeLis);
		heightBar.setOnRangeSeekBarChangeListener(taHeightLis);
		weightBar.setOnRangeSeekBarChangeListener(taWeightLis);

		ageLayout.addView(ageBar);
		heightLayout.addView(heightBar);
		weightLayout.addView(weightBar);

		addTagsToBreakLayout(hobbyLBR, hobbyStr, TAG_TYPE.Hobby_tag, this);
		addTagsToBreakLayout(personalLBL, personalStr, TAG_TYPE.Personal_tag, this);
		addTagsToBreakLayout(taPersonalLBL, taPersonalStr, TAG_TYPE.Ta_personal_tag, this);

		hobbyLBR.setOnClickListener(tagsLis);
		personalLBL.setOnClickListener(tagsLis);
		taPersonalLBL.setOnClickListener(tagsLis);
	}

	public void getComponents() {
		photoScrollView = (HorizontalScrollView) this.findViewById(R.id.photo_row_edit);
		// photoLinearLyaout = (LinearLayout)
		// this.findViewById(R.id.photo_row_1);
		backBtn = (ImageButton) this.findViewById(R.id.edit_back);
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

	OnClickListener backLis = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			EditInfoActivity.this.finish();
		}
	};

	OnClickListener submitLis = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			EditInfoActivity.this.finish();
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

	public static void addTagsToBreakLayout(LineBreakLayout layout, String tagString,
			TAG_TYPE tagType, Context context) {
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
		String str = data.getStringExtra("tagString");
		switch (requestCode) {
		case TAG_HOBBY:
			hobbyStr = str;
			hobbyLBR.removeAllViews();
			addTagsToBreakLayout(hobbyLBR, str, TAG_TYPE.Hobby_tag, this);
			break;
		case TAG_PERSONAL:
			personalStr = str;
			personalLBL.removeAllViews();
			addTagsToBreakLayout(personalLBL, str, TAG_TYPE.Personal_tag, this);
			break;
		case TAG_TA_PERSONAL:
			taPersonalStr = str;
			taPersonalLBL.removeAllViews();
			addTagsToBreakLayout(taPersonalLBL, str, TAG_TYPE.Ta_personal_tag, this);
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_info, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
