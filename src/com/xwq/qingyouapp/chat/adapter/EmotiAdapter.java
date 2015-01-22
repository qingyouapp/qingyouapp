package com.xwq.qingyouapp.chat.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.xwq.qingyouapp.R;
import com.xwq.qingyouapp.chat.util.GraphicsUtil;
import com.xwq.qingyouapp.chat.util.ImageCache;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;



public class EmotiAdapter extends BaseAdapter {
	private Context mContext;
	private Resources mResources;
	private List<Integer> data;
	private HashMap<String, Integer> faceMap;
	String[] icons;

	public EmotiAdapter(Context context, Resources resources) {
		this.mContext = context;
		this.mResources = resources;

		data = new ArrayList<Integer>();
		faceMap = new HashMap<String, Integer>();
		Resources rs = resources;
		TypedArray ty = rs.obtainTypedArray(R.array.gotye_smiley_resid_array);
		icons = rs.getStringArray(R.array.gotye_smiley_resid_array);
		for(int i = 0;i < icons.length;i++){
			data.add(ty.getResourceId(i, R.drawable.gotye_smiley_1));
			faceMap.put(icons[i], ty.getResourceId(i, R.drawable.gotye_smiley_1));
		}
		ty.recycle();
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView i;

		if (convertView == null) {
			i = new ImageView(mContext);
			i.setAdjustViewBounds(true);
			i.setLayoutParams(new GridView.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			i.setPadding(10, 10, 10, 10);
		} else {
			i = (ImageView) convertView;
		}

		Bitmap d = ImageCache.getInstance().get("emoji"+position);
		if(d == null){
			d = GraphicsUtil.drawableToBitmap(mResources.getDrawable(data.get(position)));
			ImageCache.getInstance().put("emoji"+position, d);
		}
		i.setImageBitmap(d);
		return i;
	}

	public int getCount() {
		return data.size();
	}

	public Drawable getItem(int position) {
		return /*mContext.getResources()*/mResources.getDrawable(data.get(position));
	}

	public long getItemId(int position) {
		return position;
	}

	public int getDrawableID(int id){
		return data.get(id - 1);
	}
}
