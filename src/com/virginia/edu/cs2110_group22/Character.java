package com.virginia.edu.cs2110_group22;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Character extends MovingObject {

	protected boolean touchRight;
	protected boolean touchLeft;
	protected boolean touchDown;
	protected boolean touchUp;
	private Bitmap right;
	private Bitmap left;
	private Bitmap up;
	private Bitmap down;
	
	public Character(Bitmap bitmap, double x, double y, MovingView movingView,
			int map) {
		super(bitmap, x, y, movingView);
		this.map = map;
		this.touchRight = false;
		this.touchLeft = false;
		this.touchDown = false;
		this.touchUp = false;
	}
	
	public Character(Bitmap mainBitRScaled, Bitmap mainBitLScaled,
			Bitmap mainBitUScaled, Bitmap mainBitDScaled, int x, int y,
			MovingView movingView, int mapId) {
		super(mainBitRScaled, x, y, movingView);
		this.map = map;
		this.touchRight = false;
		this.touchLeft = false;
		this.touchDown = false;
		this.touchUp = false;
		this.right = mainBitRScaled;
		this.left = mainBitLScaled;
		this.up = mainBitUScaled;
		this.down = mainBitDScaled;
	}

	// Updates location based on sensor movement
	// NOTE: "touch" is used for accelerometer input
	// I.E. tilting the device up sets touchUp to true
	public void update(int width, int height) {
		double topSpeed = maxSpeed / 8 * 0.01 * movingView.width;
		
		double dirLimit = topSpeed / Math.sqrt(2);
		
		double speedLimit;
		
		double reduceAmt = 2;
		if (this.map == 2) {
			reduceAmt = 0.2;
		}
		
		if (this.touchDown && (this.touchRight || this.touchLeft)) {
			speedLimit = dirLimit;
		} else if (this.touchUp && (this.touchRight || this.touchLeft)) {
			speedLimit = dirLimit;
		} else {
			speedLimit = topSpeed;
		}
		// Adds x speed if being touched and below speedLimit
		if (this.touchLeft && this.Vx >= -speedLimit) {
			this.Vx -= 2;
		} else if (this.touchRight && this.Vx <= speedLimit){ 
			this.Vx += 2;
			
		// Reduces speed if not being touched
		} else if (this.Vx > 0) {
			this.Vx -= reduceAmt;
		} else if (this.Vx < 0) {
			this.Vx += reduceAmt;
		}
		
		// Adds y speed if being touched and below speedLimit
		if (this.touchDown && this.Vy <= speedLimit) {
			this.Vy += 2;
		} else if (this.touchUp && this.Vy >= -speedLimit) {
			this.Vy -= 2;
		
		// Reduces speed if not being touched
		} else if (this.Vy > 0) {
			this.Vy -= reduceAmt;
		} else if (this.Vy < 0) {
			this.Vy += reduceAmt;
		}
		
		// Bounce off right side of screen
		if (this.x + this.bitmap.getWidth() > (width - 70)) {
			this.x = (width - 70) - this.bitmap.getWidth() - 5;
			this.Vx = 0;
		}
		
		// Bounce off left side of screen
		if (this.x < 70) {
			this.x = 75;
			this.Vx = 0;
		}
		
		// Bounce off bottom of screen
		if (this.y + this.bitmap.getHeight() > (height - 95)) {
			this.y = (height - 95) - this.bitmap.getHeight() - 5;
			this.Vy = 0;
		}
		
		// Bounce off top of screen
		if (this.y < 95) {
			this.y = 100;
			this.Vy = 0;
		}
		
		// Update bitmap
		if (this.Vx > 0) {
			this.bitmap = this.right;
		} else if (this.Vx < 0) {
			this.bitmap = this.left;
		} else if (this.Vy > 0) {
			this.bitmap = this.down;
		} else if (this.Vy < 0) {
			this.bitmap = this.up;
		}
		
		// Update location and CollisionBox
		this.x += this.Vx;
		this.y += this.Vy;
		this.updateBox();
	}
	
	// Fires bullet from the main character's current location
	public Bullet fireBullet(MovingView movingView, double ratio, boolean isXNeg) {
		Bitmap bulletImg = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(movingView.getResources(), R.drawable.bullet), (int) (0.0125 * movingView.width), (int) (0.0125 * movingView.width), true);
		Bullet bullet = new Bullet(bulletImg, (this.x + (this.getWidth() / 2)), (this.y + (this.getHeight() / 2)), ratio, isXNeg, movingView);
		return bullet;
	}
	
	public void touchRight(boolean b) {
		this.touchRight = b;
	}
	
	public void touchLeft(boolean b) {
		this.touchLeft = b;
	}
	
	public void touchDown(boolean b) {
		this.touchDown = b;
	}
	
	public void touchUp(boolean b) {
		this.touchUp = b;
	}
	
	// Restores main character to initial location
	public void restore() {
		Vx = 0;
		Vy = 0;
		this.x = 15;
		this.y = 580;
	}
	
	// Checks if character is hitting walls of the paths on the map
	public void checkWall (int i) {
		if (i == 2) {
			CollisionBox hole = new CollisionBox(245, 940, 100, 90);
			if (this.box.Intersects(hole)) {
				if (!movingView.spinning) {
					((MainActivity)movingView.getContext()).mp.release();
					((MainActivity)movingView.getContext()).playDeathSound();
					movingView.spinStart = System.currentTimeMillis();
					movingView.finalX = 245;
					movingView.finalY = 940;
					movingView.spinning = true;
				}
			}
		}
		if (i == 3) {
			CollisionBox hole = new CollisionBox(350, 510, 150, 90);
			if (this.box.Intersects(hole)) {
				if (!movingView.spinning) {
					((MainActivity)movingView.getContext()).mp.release();
					((MainActivity)movingView.getContext()).playDeathSound();
					movingView.spinStart = System.currentTimeMillis();
					movingView.finalX = 350;
					movingView.finalY = 510;
					movingView.spinning = true;
				}
			}
		}
//		if ( i == 1) {
//			if (this.x < 255) {
//				if (this.y < 560 && this.y > 550) {
//					this.y = 561;
//					this.Vy = 0;
//				} else if (this.y < 555) {
//					this.x = 256;
//					this.Vx = 0;
//				}
//				else if ((this.y + this.getHeight()) > 700 && (this.y + this.getHeight()) < 710) {
//					this.y = 699 - this.getHeight();
//					this.Vy = 0;
//				}
//				else if (this.x > 245 && this.y > 610 && this.y < 806) {
//					this.x = 256;
//					this.Vx = 0;
//				}
//				if (this.y < 806 && this.y > 796) {
//					this.y = 807;
//					this.Vy = 0;
//				}
//				if (this.x < 122 && this.y > 806) {
//					this.x = 123;
//					this.Vx = 0;
//				}
//			}
//			if (this.y < 175) {
//				this.y = 176;
//				this.Vy = 0;
//			}
//			if ((this.y + this.getHeight()) > 1105) {
//				this.y = 1104 - this.getHeight();
//				this.Vy = 0;
//			}
//			if ((this.x + this.getWidth()) > 250) {
//				if ((this.y + this.getHeight()) > 930 && (this.y + this.getHeight() < 940)) {
//					this.y = (929 - this.getHeight());
//					this.Vy = 0;
//				}
//				if ((this.x + this.getWidth()) < 260 && (this.y + this.getHeight()) > 930 && this.y < 965) {
//					this.x = (249 - this.getWidth());
//					this.Vx = 0;
//				}
//				if (this.y < 969 && this.y > 959) {
//					this.y = 970;
//					this.Vy = 0;
//				}
//			}
//			if ((this.x + this.getWidth()) > 605) {
//				this.x = (604 -  this.getWidth());
//				this.Vx = 0;
//			}
//			if ((this.y + this.getHeight()) > 275 && (this.y + this.getHeight()) < 930 && (this.x + this.getWidth()) > 380 && (this.x + this.getWidth()) < 390) {
//				this.x = (379 - this.getWidth());
//				this.Vx = 0;
//			}
//			if ((this.y + this.getHeight()) > 280 && (this.y + this.getHeight()) < 290 && (this.x + this.getWidth()) > 380) {
//				this.y = (279 - this.getHeight());
//				this.Vy = 0;
//			}
//		}
	}

	public Bullet fireBullet(MovingView movingView, double ratio2, boolean isXNeg,
			boolean changeY) {
		Bitmap bulletImg = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(movingView.getResources(), R.drawable.bulletboneyard), (int) (0.0125 * movingView.width), (int) (0.0125 * movingView.width), true);
		Bullet bullet = new Bullet(bulletImg, (this.x + (this.getWidth() / 2)), (this.y + (this.getHeight() / 2)), ratio2, isXNeg, changeY, movingView);
		return bullet;
	}

}
