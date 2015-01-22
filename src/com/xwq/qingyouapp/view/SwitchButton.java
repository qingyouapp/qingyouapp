package com.xwq.qingyouapp.view;

import com.xwq.qingyouapp.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class SwitchButton extends View {

	private Bitmap open, close;
	private boolean isOn = false;

	public SwitchButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		getDrawables();
	}

	public SwitchButton(Context context) {
		super(context);
		getDrawables();
	}

	@SuppressLint("DrawAllocation")
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		if (isOn)
			canvas.drawBitmap(open, 0, 0, null);
		else
			canvas.drawBitmap(close, 0, 0, null);

	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		int iAction = event.getAction();
		if (iAction == MotionEvent.ACTION_CANCEL || iAction == MotionEvent.ACTION_MOVE) {
			return false;
		}

		if (isOn) {
			isOn = false;
			invalidate();
		} else {
			isOn = true;
			invalidate();
		}
		return super.onTouchEvent(event);
	}

	private void getDrawables() {
		open = BitmapFactory.decodeResource(getResources(), R.drawable.switch_btn_open);
		close = BitmapFactory.decodeResource(getResources(), R.drawable.switch_btn_close);
	}

	public boolean isOn() {
		return isOn;
	}

	public void setOn(boolean isOn) {
		this.isOn = isOn;
	}

}
