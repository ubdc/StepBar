package com.example.test;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class StepBar extends View {
	private static final String TAG = "stepbar";
	private int stepCount;
	private int currentStep;
	private CharSequence[] stepArray;
	private int colorA, colorB, stepNumberColor;
	private float barHeight, cornerRadius, circleRadius, textPadding, textSize, stepNumberSize;
	private TextPaint stepNumPaint, stepTextPaint;
	
	private GradientDrawable barA;
	private GradientDrawable barB;
	private GradientDrawable circleA;
	private GradientDrawable circleB;

	public StepBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.StepBar, defStyle, 0);
		stepCount = a.getInteger(R.styleable.StepBar_stepCount, 1);
		currentStep = a.getInteger(R.styleable.StepBar_currentStep, 0);
		stepArray = a.getTextArray(R.styleable.StepBar_stepArray);
		barHeight = a.getDimension(R.styleable.StepBar_barHeight, 10);
		cornerRadius = a.getDimension(R.styleable.StepBar_cornerRadius, 0);
		circleRadius = a.getDimension(R.styleable.StepBar_circleRadius, 20);
		textPadding = a.getDimension(R.styleable.StepBar_textPadding, 0);
		textSize = a.getDimension(R.styleable.StepBar_textSize, 24);
		stepNumberColor = a.getColor(R.styleable.StepBar_stepNumberColor, 0xffffffff);
		stepNumberSize = a.getDimension(R.styleable.StepBar_stepNumberSize, 24);
		colorA = a.getColor(R.styleable.StepBar_colorA, 0xffff0000);
		colorB = a.getColor(R.styleable.StepBar_colorB, 0xffcccccc);
		Log.i(TAG, "step=" + stepCount);
		Log.i(TAG, "stepArray=" + stepArray);
		Log.i(TAG, "cornerRadius=" + cornerRadius);
		Log.i(TAG, "textPadding=" + textPadding);
		Log.i(TAG, "textSize=" + textSize);
		Log.i(TAG, "colorA=" + Integer.toHexString(colorA));
		Log.i(TAG, "colorB=" + Integer.toHexString(colorB));
		a.recycle();
		
		stepNumPaint = new TextPaint();
		stepNumPaint.setAntiAlias(true);
		stepNumPaint.setStrokeWidth(3);
		stepNumPaint.setColor(stepNumberColor);
		stepNumPaint.setTextSize(stepNumberSize);
		
		stepTextPaint = new TextPaint();
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
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int height = MeasureSpec.getSize(heightMeasureSpec);
		int mode = MeasureSpec.getMode(heightMeasureSpec);
		if (mode == MeasureSpec.AT_MOST) {
			float h = getPaddingTop() + getPaddingBottom() + circleRadius * 2;
			if (stepArray != null) {
				float max = 0;
				Rect bounds = new Rect();
				for (int i = 0; i < stepArray.length; i++) {
					stepTextPaint.getTextBounds(stepArray[i].toString(), 0, stepArray[i].length(), bounds);
					max = Math.max(max, bounds.height());
				}
				if (max > 0) {
					h += textPadding;
				}
				h += max;
				height = (int) (h + 0.5f);
				mode = MeasureSpec.EXACTLY;
			}
		}
		setMeasuredDimension(widthMeasureSpec, MeasureSpec.makeMeasureSpec(height, mode));
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
		barB.setBounds(0, 0, (getWidth() - getPaddingLeft() - getPaddingRight()) / stepCount, (int) barHeight);
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
		int halfStepWidth = barAWidth / stepCount / 2;
		
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
		for (int i = 0; i < stepCount; i++) {
			canvas.save();
			canvas.translate(getPaddingLeft(), getPaddingTop());
			canvas.translate(halfStepWidth * (i * 2 + 1) - circleRadius, 0);
			
			if (currentStep == i) {
				circleB.draw(canvas);
			} else {
				circleA.draw(canvas);
			}
			
			//从1开始，符合日常计数习惯
			String s = Integer.toString(i + 1);
			Rect bounds = new Rect();
			stepNumPaint.getTextBounds(s, 0, s.length(), bounds);
			float numWidth = stepNumPaint.measureText(s);
			float numHeight = stepNumPaint.descent() - stepNumPaint.ascent();
			int height = bounds.height();
			Log.i(TAG, "文字边框宽高：" + bounds.width() + ", " + bounds.height());
			Log.i(TAG, "文字宽高：" + numWidth + ", " + numHeight);
			canvas.drawText(
					s, 
					circleRadius - bounds.width() / 2, 
					circleRadius + bounds.height() / 2, 
					stepNumPaint);
			
			Log.i(TAG, "stepNumPaint's bounds's height is " + bounds.height());
			Log.i(TAG, "stepNumPaint's text descent is " + stepNumPaint.descent());
			Log.i(TAG, "stepNumPaint's text ascent is " + stepNumPaint.ascent());
			Log.i(TAG, "stepNumPaint's text descent - ascent is " + (stepNumPaint.descent() - stepNumPaint.ascent()));
			
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
						circleRadius * 2 + textPadding + bounds.height(), 
						stepTextPaint);
			}
			canvas.restore();
		}
	}
	
	/**
	 * 跳转第几步
	 * @param step
	 */
	public void stepTo(int step) {
		if (step == currentStep) {
			return;
		}
		if (step < 0) {
			currentStep = 0;
		} else if (step > stepCount - 1) {
			currentStep = stepCount - 1;
		} else {
			currentStep = step;
		}
		invalidate();
	}
}
