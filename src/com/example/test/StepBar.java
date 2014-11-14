package com.example.test;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class StepBar extends View {
	private static final String TAG = "stepbar";
	private int step;
	private int currentStep;
	private CharSequence[] stepArray;
	private int colorA, colorB;
	private float barHeight, cornerRadius, circleRadius, textPadding, textSize;
	private Paint stepNumPaint, stepTextPaint;
	
	private GradientDrawable barA;
	private GradientDrawable barB;
	private GradientDrawable circleA;
	private GradientDrawable circleB;

	public StepBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.StepBar, defStyle, 0);
		step = a.getInteger(R.styleable.StepBar_step, 1);
		currentStep = a.getInteger(R.styleable.StepBar_currentStep, 0);
		stepArray = a.getTextArray(R.styleable.StepBar_stepArray);
		barHeight = a.getDimension(R.styleable.StepBar_barHeight, 10);
		cornerRadius = a.getDimension(R.styleable.StepBar_cornerRadius, 0);
		circleRadius = a.getDimension(R.styleable.StepBar_circleRadius, 20);
		textPadding = a.getDimension(R.styleable.StepBar_textPadding, 0);
		textSize = a.getDimension(R.styleable.StepBar_textSize, 24);
		colorA = a.getColor(R.styleable.StepBar_colorA, 0xffff0000);
		colorB = a.getColor(R.styleable.StepBar_colorB, 0xffcccccc);
		Log.i(TAG, "step=" + step);
		Log.i(TAG, "stepArray=" + stepArray);
		Log.i(TAG, "cornerRadius=" + cornerRadius);
		Log.i(TAG, "textPadding=" + textPadding);
		Log.i(TAG, "textSize=" + textSize);
		Log.i(TAG, "colorA=" + Integer.toHexString(colorA));
		Log.i(TAG, "colorB=" + Integer.toHexString(colorB));
		a.recycle();
		
		stepNumPaint = new Paint();
		stepNumPaint.setAntiAlias(true);
		stepNumPaint.setStrokeWidth(3);
		stepNumPaint.setColor(0xffffffff);
		stepNumPaint.setTextSize(24);
		
		stepTextPaint = new Paint();
		stepTextPaint.setAntiAlias(true);
		stepTextPaint.setStrokeWidth(3);
		stepTextPaint.setColor(colorB);
		stepTextPaint.setTextSize(textSize);
	}

	public StepBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public StepBar(Context context) {
		this(context, null);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		
		barA = new GradientDrawable();
		barA.setShape(GradientDrawable.RECTANGLE);
		barA.setBounds(0, 0, getWidth() - getPaddingLeft() - getPaddingRight(), (int) barHeight);
		barA.setCornerRadius(cornerRadius);
		barA.setColor(colorA);
		
		barB = new GradientDrawable();
		barB.setShape(GradientDrawable.RECTANGLE);
		barB.setBounds(0, 0, (getWidth() - getPaddingLeft() - getPaddingRight()) / step, (int) barHeight);
		barB.setCornerRadius(cornerRadius);
		barB.setColor(colorB);
		
		circleA = new GradientDrawable();
		circleA.setShape(GradientDrawable.OVAL);
		circleA.setBounds(0, 0, (int) (circleRadius * 2), (int) (circleRadius * 2));
		circleA.setColor(colorA);
		
		circleB = new GradientDrawable();
		circleB.setShape(GradientDrawable.OVAL);
		circleB.setBounds(0, 0, (int) (circleRadius * 2), (int) (circleRadius * 2));
		circleB.setColor(colorB);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		int barAWidth = barA.getBounds().width();
		int barBWidth = barB.getBounds().width();
		int halfStepWidth = barAWidth / step / 2;
		
		//绘制底部bar
		canvas.save();
		canvas.translate(
				getPaddingLeft(), 
				getPaddingTop() + circleRadius - barHeight / 2);
		barA.draw(canvas);
		canvas.restore();
		
		//绘制选中步
		canvas.save();
		canvas.translate(getPaddingLeft() + barBWidth * currentStep, getPaddingTop() + circleRadius - barHeight / 2);
		barB.draw(canvas);
		canvas.restore();
		
		//绘制底部节点
		for (int i = 0; i < step; i++) {
			canvas.save();
			canvas.translate(getPaddingLeft(), getPaddingTop());
			canvas.translate(halfStepWidth * (i * 2 + 1) - circleRadius, 0);
			
			if (currentStep == i) {
				circleB.draw(canvas);
			} else {
				circleA.draw(canvas);
			}
			
			String s = Integer.toString(i);
			Rect bounds = new Rect();
			stepNumPaint.getTextBounds(s, 0, s.length(), bounds);
			Log.i(TAG, "文字边框：" + bounds);
			canvas.drawText(
					s, 
					circleRadius - bounds.width() / 2, 
					circleRadius + bounds.height() / 2, 
					stepNumPaint);
			
			if (stepArray != null && i < stepArray.length) {
				if (currentStep == i) {
					stepTextPaint.setColor(colorB);
				} else {
					stepTextPaint.setColor(colorA);
				}
				s = stepArray[i].toString();
				stepTextPaint.getTextBounds(s, 0, s.length(), bounds);
				canvas.drawText(
						s, 
						circleRadius - bounds.width() / 2, 
						circleRadius * 2 + textPadding + bounds.height() / 2, 
						stepTextPaint);
			}
			canvas.restore();
		}
	}
}
