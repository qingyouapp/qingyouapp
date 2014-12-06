package com.xwq.qingyouapp.util;

import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;

public class DefaultGestureListener extends SimpleOnGestureListener {

	private OnFlingListener onFlingLeftListener;
	private OnFlingListener onFlingRightListener;

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		float e1x = e1.getX();
		float e2x = e2.getX();
		if (Math.abs(e1x - e2x) > 50) {
			if(e2x < e1x){
				if(onFlingLeftListener != null) onFlingLeftListener.excute();
			}else{
				if(onFlingRightListener != null) onFlingRightListener.excute();
			} 
		}
		return super.onFling(e1, e2, velocityX, velocityY);
	}

	public interface OnFlingListener {
		public void excute();
	}

	public OnFlingListener getOnFlingLeftListener() {
		return onFlingLeftListener;
	}

	public void setOnFlingLeftListener(OnFlingListener onFlingLeftListener) {
		this.onFlingLeftListener = onFlingLeftListener;
	}

	public OnFlingListener getOnFlingRightListener() {
		return onFlingRightListener;
	}

	public void setOnFlingRightListener(OnFlingListener onFlingRightListener) {
		this.onFlingRightListener = onFlingRightListener;
	}

}
