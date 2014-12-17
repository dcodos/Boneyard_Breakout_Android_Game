package com.virginia.edu.cs2110_group22;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.TextView;

public class Typewriter extends TextView {

	private CharSequence mText;
	private int mIndex;
	private long mDelay = 500;
	private boolean finish = false;

	public Typewriter (Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(attrs);
	}
	
	public Typewriter(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(attrs);
		
	}
	
	public Typewriter(Context context) {
		super(context);
		init(null);
	}
	

	private Handler mHandler = new Handler();
	private Runnable characterAdder = new Runnable() {
		@Override
		public void run() {
			setText(mText.subSequence(0, mIndex++));
			if (mIndex <= mText.length()) {
				mHandler.postDelayed(characterAdder, mDelay);
			} else {
				setFinish(true);
			}
		}
	};

	public void animateText(CharSequence text) {
	
		mText = text;
		mIndex = 0;

		setText("");
		mHandler.removeCallbacks(characterAdder);
		mHandler.postDelayed(characterAdder, mDelay);
	}

	public void setCharacterDelay(long millis) {
		mDelay = millis;
	}

	public boolean isFinish() {
		return finish;
	}

	public void setFinish(boolean finish) {
		this.finish = finish;
	}
	private void init(AttributeSet attrs) {
		if (attrs!=null) {
			 TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.Typewriter);
			 String fontName = a.getString(R.styleable.Typewriter_fontName);
			 if (fontName!=null) {
				 Typeface myTypeface = Typeface.createFromAsset(getContext().getAssets(), fontName);
				 setTypeface(myTypeface);
			 }
			 a.recycle();
		}
	}

}