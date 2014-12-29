package com.xwq.qingyouapp.frag;

import com.xwq.qingyouapp.EditInfoActivity;
import com.xwq.qingyouapp.R;
import com.xwq.qingyouapp.TaMainActivity;
import com.xwq.qingyouapp.view.RangeSeekBar;

import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ShowPageFrag extends Fragment {

	private ImageButton editBtn;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.frag_show_page, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		if (this.getActivity().getClass().equals(TaMainActivity.class)) {
			getActivity().findViewById(R.id.showpage_title).setVisibility(View.GONE);
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
					LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
			lp.setMargins(0, 0, 0, 0);
			getActivity().findViewById(R.id.showpage_scrollView).setLayoutParams(lp);
		}
		getComponents();
		editBtn.setOnClickListener(editBtnLis);

	}

	public void getComponents() {
		editBtn = (ImageButton) getActivity().findViewById(R.id.edit_btn);
	}

	OnClickListener editBtnLis = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent(getActivity(), EditInfoActivity.class);
			startActivity(intent);
		}

	};

}

// LinearLayout layout = new LinearLayout(this.getActivity());//
// 创建LinearLayout对象
// layout = (LinearLayout) this.getView().findViewById(R.id.mylayout);//
// 实例化LinearLayout对象
// //// 设置Button组件的宽和高
// //LinearLayout.LayoutParams Params = new LinearLayout.LayoutParams(
// // ViewGroup.LayoutParams.MATCH_PARENT,
// // ViewGroup.LayoutParams.WRAP_CONTENT);
// // for (int i = 0; i < 20; i++) {// 设置循环创建Button对系那个
// // Button button = new Button(this.getActivity());
// // button.setText("button" + i);// 设置标题
// // button.setGravity(Gravity.CENTER);// 设置对齐方式
// // layout.addView(button, Params);// 添加组件
// // }
//
//
// RangeSeekBar2<Integer> seekBar = new RangeSeekBar2<Integer>(60, 140,
// this.getActivity());
// seekBar.setOnRangeSeekBarChangeListener(null);
//
// // add RangeSeekBar to pre-defined layout
//
// layout.addView(seekBar);
