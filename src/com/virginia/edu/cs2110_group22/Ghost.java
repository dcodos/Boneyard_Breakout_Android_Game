package com.virginia.edu.cs2110_group22;

import android.graphics.Bitmap;

public class Ghost extends MovingObject {
	
	private int initX;
	private int initY;
	public Ghost(Bitmap decodeResource, int x, int y, MovingView movingView) {
		super(decodeResource, x, y, movingView);
		
		// Sets initial location
		this.initX = x;
		this.initY = y;
		
		// Finds random initial velocity
		this.Vx = (int) (Math.random() * 6) + 1;
		this.Vy = (int) (Math.random() * 6) + 1;
	}

	// Updates ghost location and checks if hitting walls
	public void update(int width, int height) {
		
		if (this.x + this.bitmap.getWidth() > width) {
			this.x = width - this.bitmap.getWidth() - 5;
			this.Vx = this.Vx * -1;
		}
		
		if (this.x < 0) {
			this.x = 5;
			this.Vx = this.Vx * -1;
		}
		
		if (this.y + this.bitmap.getHeight() > height) {
			this.y = height - this.bitmap.getHeight() - 5;
			this.Vy = this.Vy * -1;
		}
		
		if (this.y < 0) {
			this.y = 5;
			this.Vy = this.Vy * -1;
		}
		this.x += this.Vx;
		this.y += this.Vy;
		this.updateBox();
	}

	// Restores ghost to initial location
	public void restore() {
		this.Vx = (int) (Math.random() * 6) + 1;
		this.Vy = (int) (Math.random() * 6) + 1;
		this.x = initX;
		this.y = initY;
	}
	
	// Method to bounce ghosts off each other
	public void intersectionChanges(MovingObject o) {
		if (this.intersects(o)){
			if (this.getBox().leftIntersects(o.getBox())) {
				this.setVx(this.getVx() * -1);
				this.setX(o.getX() + o.getWidth());
			}else if(this.getBox().rightIntersects(o.getBox())) {
				this.setVx(this.getVx() * -1);
				this.setX(o.getX() - this.getWidth());
			}else if (this.getBox().topIntersects(o.getBox())) {
				this.setVy(this.getVy() * -1);
				this.setY(o.getY() + o.getHeight());
			}else if (this.getBox().bottomIntersects(o.getBox())) {
				this.setVy(this.getVy() * -1);
				this.setY(o.getY() - this.getHeight());
			}
		}
	}

	public boolean startIntersects(Character mainChar) {
		CollisionBox startBox = new CollisionBox((this.x - 30), (this.y - 30), (this.bitmap.getWidth() + 30), this.bitmap.getHeight() + 30);
		if (startBox.Intersects(mainChar.getBox())) {
			return true;
		}
		return false;
	}
	
}
