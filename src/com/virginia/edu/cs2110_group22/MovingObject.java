package com.virginia.edu.cs2110_group22;

import android.graphics.Bitmap;

public abstract class MovingObject {

	protected static final int maxSpeed = 10;
	
	protected Bitmap bitmap;
	protected double x;
	protected double y;
	protected double Vx;
	protected double Vy;
	protected CollisionBox box;
	protected MovingView movingView;
	protected int map;
	
	// Constructor takes in Bitmap and location
	public MovingObject(Bitmap bitmap, double x, double y, MovingView movingView) {
		// Store values to fields
		this.bitmap = bitmap;
		this.x = x;
		this.y = y;
		this.movingView = movingView;
		
		// Create new CollisionBox
		box = new CollisionBox((x + 5), (y + 5), (this.bitmap.getWidth() - 5), (this.bitmap.getHeight() - 5));
	}
	
	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
		
		// Create new CollisionBox
		box = new CollisionBox((x + 5), (y + 5), (this.bitmap.getWidth() - 5), (this.bitmap.getHeight() - 5));
	}

	public Bitmap getBitmap() {
		return this.bitmap;
	}
	
	public double getX() {
		return this.x;
	}
	
	public double getY() {
		return this.y;
	}
	
	public CollisionBox getBox() {
		return this.box;
	}
	
	// Checks if the collision box of one object intersects another
	public boolean intersects(MovingObject o) {
		if (o.getBox().Intersects(this.box)) {
			return true;
		} 
		return false;
	}
	
	public double getVx() {
		return Vx;
	}

	public void setVx(double d) {
		Vx = d;
	}

	public double getVy() {
		return Vy;
	}

	public void setVy(double vy) {
		Vy = vy;
	}	
	
	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}
	
	public int getHeight() {
		int height = this.bitmap.getHeight();
		return height;
	}
	
	public int getWidth() {
		int width = this.bitmap.getWidth();
		return width;
	}
	
	public void updateBox() {
		box.setx(this.x);
		box.sety(this.y);
	}
}
