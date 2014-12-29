package com.xwq.qingyouapp.adapter;

import com.xwq.qingyouapp.EditInfoActivity;
import com.xwq.qingyouapp.R;
import com.xwq.qingyouapp.EditInfoActivity.TAG_TYPE;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TagsGirdAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private String[] totalArr, seletedArr;
	private int tagCode;
	private int seletedColorId = 0;

	public TagsGirdAdapter() {
		// TODO Auto-generated constructor stub
	}

	public TagsGirdAdapter(Context contex, String[] totalArr, String[] seletedArr, int tagCode) {
		this.inflater = LayoutInflater.from(contex);
		this.totalArr = totalArr;
		this.seletedArr = seletedArr;
		this.tagCode = tagCode;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return totalArr.length;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return totalArr[arg0];
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View itemView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder holder;
		if (itemView == null) {
			holder = new ViewHolder();
			itemView = inflater.inflate(R.layout.item_tags_grid, null);
			holder.textView = (TextView) itemView.findViewById(R.id.item_text);
			itemView.setTag(holder);
		} else {
			holder = (ViewHolder) itemView.getTag();
		}
		
		switch (tagCode) {
		case EditInfoActivity.TAG_HOBBY:
			seletedColorId = R.color.hobby_tag_bg;
			break;
		case EditInfoActivity.TAG_PERSONAL:
			seletedColorId = R.color.personal_tag_bg;
			break;
		case EditInfoActivity.TAG_TA_PERSONAL:
			seletedColorId = R.color.personal_tag_bg;
			break;
		}

		holder.textView.setText(totalArr[position]);
		for(String string:seletedArr){
			if(string.equals(totalArr[position])){
				holder.textView.setBackgroundResource(seletedColorId);
				holder.textView.setTextColor(Color.WHITE);
				break;
			}
		}

		return itemView;
	}

	private class ViewHolder {
		public TextView textView;
	}

}
