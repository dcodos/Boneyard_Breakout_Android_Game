package com.virginia.edu.cs2110_group22;

import android.graphics.Bitmap;

// Class extends MovingObject class (See that for more info)
public class Bullet extends MovingObject {
	
	private static final int SPEED = 30;
	
	private MovingView movingView;

	// Constructor takes in bitmap, location, ratio, and if the x value is negative.
	public Bullet(Bitmap bitmap, double x, double y, double ratio,  boolean isXNeg, MovingView movingView) {
		super(bitmap, x, y, movingView);
		this.movingView = movingView;
		
		// Calculates x and y velocity based on ratio and speed above
		if (isXNeg) {
			this.Vx = -1 * SPEED / (Math.sqrt((1 + Math.pow(ratio, 2))));
			this.Vy = 1 * this.Vx * ratio;
		} else {
			this.Vx = SPEED / (Math.sqrt((1 + Math.pow(ratio, 2))));
			this.Vy = this.Vx * ratio;
		}
		
	}
	
	public Bullet(Bitmap bitmap, double x, double y, double ratio,
			boolean isXNeg, boolean changeY, MovingView movingView) {
		super(bitmap, x, y, movingView);
		this.movingView = movingView;
		
		// Calculates x and y velocity based on ratio and speed above
		if (isXNeg) {
			this.Vx = -1 * SPEED / (Math.sqrt((1 + Math.pow(ratio, 2))));
			this.Vy = 1 * this.Vx * ratio;
		} else {
			this.Vx = SPEED / (Math.sqrt((1 + Math.pow(ratio, 2))));
			this.Vy = this.Vx * ratio;
		}
		if (changeY) {
			this.Vy *= -1;
			this.Vx *= -1;
		}
	}

	// Checks if bullet is off of the screen and returns accordingly
	public boolean shouldDelete() {
		int width = movingView.getWidth();
		int height = movingView.getHeight();
		if (this.x > width || this.x < 0 || this.y > height || this.y < 0) {
			return true;
		}
		return false;
	}

	// Updates location based on velocity and collision box
	public void update() {
		this.x += this.Vx;
		this.y += this.Vy;
		this.updateBox();
	}
}
