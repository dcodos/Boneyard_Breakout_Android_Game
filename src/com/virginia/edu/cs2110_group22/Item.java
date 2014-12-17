package com.virginia.edu.cs2110_group22;

import android.graphics.Bitmap;
import android.util.Log;

public class Item extends MovingObject {
	
	private long creationTime;
	private int duration;
	private long lastImgChange;
	private int curImg;
	
	public Item(Bitmap bitmap, double x, double y, MovingView movingView, int duration) {
		super(bitmap, x, y, movingView);
		this.duration = duration;
		this.creationTime = System.currentTimeMillis();
		lastImgChange = creationTime;
		curImg = 0;
	}
	
	public long getCreationTime() {
		return this.creationTime;
	}

	public int getDuration() {
		return this.duration;
	}
	
	public int getCurImg() {
		long timeDif = System.currentTimeMillis() - lastImgChange;
		if (timeDif > 800) {
			curImg = 0;
			lastImgChange = System.currentTimeMillis();
		} else if (timeDif > 700) {
			curImg = 7;
		} else if (timeDif > 600) {
			curImg = 6;
		} else if (timeDif > 500) {
			curImg = 5;
		} else if (timeDif > 400) {
			curImg = 4;
		} else if (timeDif > 300) {
			curImg = 3;
		} else if (timeDif > 200) {
			curImg = 2;
		} else if (timeDif > 100) {
			curImg = 1;
		}
		return curImg;
	}
	
}
