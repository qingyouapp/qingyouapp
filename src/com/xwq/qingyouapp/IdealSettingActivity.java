package com.xwq.qingyouapp;

import org.json.JSONException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.xwq.qingyouapp.EditInfoActivity.TAG_TYPE;
import com.xwq.qingyouapp.bean.AgeBT;
import com.xwq.qingyouapp.bean.FriendStandards;
import com.xwq.qingyouapp.bean.GradeBT;
import com.xwq.qingyouapp.bean.HeightBT;
import com.xwq.qingyouapp.bean.UserMetadata;
import com.xwq.qingyouapp.bean.WeightBT;
import com.xwq.qingyouapp.command.CommandCallback;
import com.xwq.qingyouapp.command.Processor;
import com.xwq.qingyouapp.util.LocalStorage;
import com.xwq.qingyouapp.util.StringHandler;
import com.xwq.qingyouapp.util.ThisApp;
import com.xwq.qingyouapp.view.LineBreakLayout;
import com.xwq.qingyouapp.view.RangeSeekBar;
import com.xwq.qingyouapp.view.RangeSeekBar.OnRangeSeekBarChangeListener;

public class IdealSettingActivity extends Activity {

	private RelativeLayout ageLayout, heightLayout, weightLayout;
	private TextView submitView, cancelView;

	// friend standards
	private RadioGroup radioGroup;
	private RadioButton maleRadio, femaleRadio, allRadio;
	private RangeSeekBar<Integer> ageBar, heightBar, weightBar;
	private TextView ageMin, ageMax, heightMin, heightMax, weightMin, weightMax;
	private LineBreakLayout taPersonalLBL;
	private String taPersonalStr;
	private int ageS = 16;
	private int ageE = 30;
	private int heightS = 140;
	private int heightE = 230;
	private int weightS = 30;
	private int weightE = 100;

	private LocalStorage localStorage;
	private Processor processor;
	private UserMetadata user;
	private FriendStandards fs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_ideal_setting);
		
		getComponents();
		localStorage = new LocalStorage(this);
		processor = Processor.instance(this);
		user = localStorage.getUser();

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
		taPersonalLBL.setOnClickListener(tagsLis);

		pullInLocalStorage();
	}

	public void getComponents() {
		ageLayout = (RelativeLayout) this.findViewById(R.id.age_layout);
		heightLayout = (RelativeLayout) this.findViewById(R.id.height_layout);
		weightLayout = (RelativeLayout) this.findViewById(R.id.weight_layout);
		ageMin = (TextView) this.findViewById(R.id.age_min);
		ageMax = (TextView) this.findViewById(R.id.age_max);
		heightMin = (TextView) this.findViewById(R.id.height_min);
		heightMax = (TextView) this.findViewById(R.id.height_max);
		weightMin = (TextView) this.findViewById(R.id.weight_min);
		weightMax = (TextView) this.findViewById(R.id.weight_max);

		cancelView = (TextView) this.findViewById(R.id.cancel_back);
		submitView = (TextView) this.findViewById(R.id.edit_submit);

		radioGroup = (RadioGroup) this.findViewById(R.id.radioGroup);
		maleRadio = (RadioButton) this.findViewById(R.id.radioMale);
		femaleRadio = (RadioButton) this.findViewById(R.id.radioFemale);
		allRadio = (RadioButton) this.findViewById(R.id.radioAll);

		taPersonalLBL = (LineBreakLayout) this.findViewById(R.id.ta_persoanl_break_layout);
	}

	public void pullInLocalStorage() {

		if (!StringHandler.isNull(user.getFriendStandards())) {
			System.out.println("111:" + user.getFriendStandards());
			String fsStr = user.getFriendStandards();
			Gson gson = new Gson();
			FriendStandards fs = gson.fromJson(fsStr, FriendStandards.class);

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
			EditInfoActivity.addTagsToBreakLayout(taPersonalLBL, taPersonalStr,
					TAG_TYPE.Ta_personal_tag, this);
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
		short g = user.getGrade();
		short g_min = (short) (g - 1 < 0 ? 0 : g - 1);
		short g_max = (short) (g + 1 > 10 ? 10 : g + 1);
		GradeBT grade = new GradeBT(g_min, g_max);
		// 城市默认为本人所在城市，目前UI上没有展现
		int hometown = user.getHometownCity();
		fs = new FriendStandards(sex, height, weight, age, grade, taPersonalStr, hometown);
		return fs;
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
			Intent intent = new Intent(IdealSettingActivity.this, TagsEditActivity.class);
			intent.putExtra("tag_code", EditInfoActivity.TAG_TA_PERSONAL);
			intent.putExtra("tag_string", taPersonalStr);
			startActivityForResult(intent, EditInfoActivity.TAG_TA_PERSONAL);
		}
	};

	OnRangeSeekBarChangeListener<Integer> taAgeLis = new OnRangeSeekBarChangeListener<Integer>() {
		@Override
		public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue,
				Integer maxValue) {
			ageMin.setText(minValue.toString());
			ageMax.setText(maxValue.toString());
		}
	};

	OnRangeSeekBarChangeListener<Integer> taHeightLis = new OnRangeSeekBarChangeListener<Integer>() {
		@Override
		public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue,
				Integer maxValue) {
			heightMin.setText(minValue.toString());
			heightMax.setText(maxValue.toString());
		}
	};

	OnRangeSeekBarChangeListener<Integer> taWeightLis = new OnRangeSeekBarChangeListener<Integer>() {
		@Override
		public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue,
				Integer maxValue) {
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

	OnClickListener submitLis = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			try {
				fs = getFriendStandards();
				user.setFriendStandards(StringHandler.objectToJsonString(fs));
				String url = getResources().getString(R.string.url_base)
						+ getResources().getString(R.string.url_update);
				processor.runCommand(url, StringHandler.userToJsonString(user), updateCallback);
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
	};

	CommandCallback updateCallback = new CommandCallback() {
		@SuppressLint("ShowToast")
		@Override
		public void excute(String jb) {
			if ("valid".equals(jb.trim())) {
				localStorage.setUser(user);
				IdealSettingActivity.this.finish();
				Toast.makeText(getApplication(), getResources().getString(R.string.submit_success),
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(getApplication(),
						getResources().getString(R.string.server_exception), Toast.LENGTH_SHORT)
						.show();
			}
		}
	};

	// 取消更改
	public void backWithoutSave() {
		Intent intent = new Intent();
		intent.putExtra("user", "cancelled");
		setResult(1, intent);
		IdealSettingActivity.this.finish();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_CANCELED)
			switch (requestCode) {
			case EditInfoActivity.TAG_TA_PERSONAL:
				taPersonalStr = data.getStringExtra("tagString");
				EditInfoActivity.addTagsToBreakLayout(taPersonalLBL, taPersonalStr,
						TAG_TYPE.Ta_personal_tag, this);
				break;
			}

		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onBackPressed() {
		this.finish();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.ideal_setting, menu);
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
