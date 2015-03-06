package com.zhy.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
/**
 * 
 * @author zhy
 *
 */
public class AutoScrollTextView extends View
{

	private Paint mPaint;

	private int mPreNum;
	private int mCurNum;
	private int mNextNum;
	private int mTargetNum = 0;

	private int mTextCenterX;

	private float mFraction;

	private int mSpeed = 3;

	private int mTextHeight;
	private Rect mTextBounds = new Rect();

	private int mTextSize = sp2px(130);
	private int mTextColor = 0xFFFA6703;

	public enum Mode
	{
		DOWN, UP, DOWN_AND_UP
	};

	private Mode mMode = Mode.DOWN_AND_UP;
	
	public void setMode(Mode mode)
	{
		mMode = mode ; 
	}

	public AutoScrollTextView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		mPaint = new Paint();
		mPaint.setTextAlign(Align.CENTER);

		// TODO get attrs

		mPaint.setTextSize(mTextSize);
		mPaint.setColor(mTextColor);

		measureTextHeight();

	}

	private void measureTextHeight()
	{
		mPaint.getTextBounds(mCurNum + "", 0, 1, mTextBounds);
		mTextHeight = mTextBounds.height();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		int width = measureWidth(widthMeasureSpec);
		int height = measureHeight(heightMeasureSpec);
		setMeasuredDimension(width, height);

		mTextCenterX = (getMeasuredWidth() - getPaddingLeft() - getPaddingRight()) / 2;
	}

	private int measureHeight(int measureSpec)
	{
		int mode = MeasureSpec.getMode(measureSpec);
		int val = MeasureSpec.getSize(measureSpec);
		int result = 0;
		switch (mode)
		{
		case MeasureSpec.EXACTLY:
			result = val;
			break;
		case MeasureSpec.AT_MOST:
		case MeasureSpec.UNSPECIFIED:
			mPaint.getTextBounds("0", 0, 1, mTextBounds);
			result = mTextBounds.height();
			break;
		}
		result = mode == MeasureSpec.AT_MOST ? Math.min(result, val) : result;
		return result + getPaddingTop() + getPaddingBottom();
	}

	private int measureWidth(int measureSpec)
	{
		int mode = MeasureSpec.getMode(measureSpec);
		int val = MeasureSpec.getSize(measureSpec);
		int result = 0;
		switch (mode)
		{
		case MeasureSpec.EXACTLY:
			result = val;
			break;
		case MeasureSpec.AT_MOST:
		case MeasureSpec.UNSPECIFIED:
			mPaint.getTextBounds("0", 0, 1, mTextBounds);
			result = mTextBounds.width();
			break;
		}
		result = mode == MeasureSpec.AT_MOST ? Math.min(result, val) : result;
		return result + getPaddingLeft() + getPaddingRight();
	}


	@Override
	protected void onDraw(Canvas canvas)
	{
		mPaint.setColor(mTextColor);

		switch (mMode)
		{
		case DOWN:
			if (mCurNum != mTargetNum )
			{
				postDelayed(mDownRunnable, mSpeed);
				if (mFraction >= 1)
				{
					mFraction = 0;
					initNum(mCurNum + 1);
				}
			}
			break;
		case UP:
			if (mCurNum != mTargetNum )
			{
				postDelayed(mUpRunnable, mSpeed);
				if (mFraction <= -1)
				{
					mFraction = 0;
					initNum(mCurNum + 1);
				}
			}
			break;

		default:
			if (mCurNum > mTargetNum )
			{
				postDelayed(mDownRunnable, mSpeed);

				if (mFraction >= 1)
				{
					mFraction = 0;
					initNum(mCurNum - 1);
				}
			} else if (mCurNum < mTargetNum)
			{
				postDelayed(mUpRunnable, mSpeed);

				if (mFraction <= -1)
				{
					mFraction = 0;
					initNum(mCurNum + 1);
				}
			}
			break;
		}

		canvas.translate(0, mFraction * getMeasuredHeight());
		drawPre(canvas);
		drawSelf(canvas);
		drawNext(canvas);
		canvas.restore();
	}

	public void setNumber(int number)
	{
		if (number < 0 || number > 9)
			throw new RuntimeException("invalidate number , should in [0,9]");
		initNum(number);
		mFraction = 0 ;
		invalidate();
	}

	public void setTextSize(int mTextSize)
	{
		this.mTextSize = dp2px(mTextSize);
		requestLayout();
		invalidate();
	}

	public void setTextColor(int mTextColor)
	{
		this.mTextColor = mTextColor;
		invalidate();
	}

	private void initNum(int number)
	{
		number = number == -1 ? 9 : number;
		number = number == 10 ? 0 : number;
		mCurNum = number;

		Log.e("TAG", mCurNum + " , " + mFraction);

		switch (mMode)
		{
		case DOWN:
			mPreNum = number + 1 == 10 ? 0 : number + 1;
			break;
		case UP:
			mNextNum = number + 1 == 10 ? 0 : number + 1;
			break;
		default:
			mNextNum = number + 1 == 10 ? 0 : number + 1;
			mPreNum = number - 1 == -1 ? 9 : number - 1;
			break;
		}

	}

	private Runnable mUpRunnable = new Runnable()
	{
		@Override
		public void run()
		{
			mFraction -= 0.02f;
			invalidate();
		}
	};

	private Runnable mDownRunnable = new Runnable()
	{
		@Override
		public void run()
		{
			mFraction += 0.02f;
			invalidate();
		}
	};

	private void drawNext(Canvas canvas)
	{
		int y = getMeasuredHeight() * 3 / 2;
		canvas.drawText(mNextNum + "", mTextCenterX, y + mTextHeight / 2,
				mPaint);
	}

	private void drawSelf(Canvas canvas)
	{
		int y = getMeasuredHeight() / 2;
		canvas.drawText(mCurNum + "", mTextCenterX, y + mTextHeight / 2, mPaint);
	}

	private void drawPre(Canvas canvas)
	{
		int y = (getMeasuredHeight() / 2 - mTextHeight / 2) * -1;
		canvas.drawText(mPreNum + "", mTextCenterX, y, mPaint);
	}

	public void setTargetNumber(int nextNum)
	{
		this.mTargetNum = nextNum;
		invalidate();
	}

	private int dp2px(float dpVal)
	{
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				dpVal, getResources().getDisplayMetrics());
	}

	private int sp2px(float dpVal)
	{
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
				dpVal, getResources().getDisplayMetrics());
	}

}
