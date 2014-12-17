package com.virginia.edu.cs2110_group22;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

public class GameThread extends Thread {

	private static final String TAG = GameThread.class.getSimpleName();
	
	private static final int FPS = 50;
	private static final int PERIOD = 1000 / FPS;
	private static final int MAX_SKIPS = 0;
	
	private Object mPauseLock = new Object();  
	private boolean mPaused;
	
	private boolean running;
	private SurfaceHolder surfaceHolder;
	private MovingView movingView;

	public boolean paused;
	
	public GameThread(SurfaceHolder surfaceHolder, MovingView movingView) {
		super();
		this.surfaceHolder = surfaceHolder;
		this.movingView = movingView;
		this.paused = false;
	}
	
	public void setRunning(boolean running) {
		this.running = running;
	}

	public void onPause() {
	    synchronized (mPauseLock) {
	        mPaused = true;
	    }
	}

	public void onResume() {
	    synchronized (mPauseLock) {
	        mPaused = false;
	        mPauseLock.notifyAll();
	    }
	}
	
	@Override
	public void run() {
		Log.d(TAG, "Starting loop");
		Canvas canvas;
		long beginTime;
		
		while (running) {
			canvas = null;
			int sleepTime;
			int framesSkipped = 0;
			
			try {
				canvas = this.surfaceHolder.lockCanvas();
				synchronized (surfaceHolder) {
					beginTime = System.currentTimeMillis();
					framesSkipped = 0;
					
					// Update everything on the movingView
					this.movingView.update();
				
					// Render everything
					this.movingView.render(canvas);
					
					// Finds amount of time this instance of the loop took
					long diff = System.currentTimeMillis() - beginTime;
					
					// Sleep time is difference in period and amount of time frame took
					sleepTime = (int) (PERIOD - diff);
					
					if (PERIOD > diff) {
						try {
							Thread.sleep(sleepTime);
						} catch (InterruptedException e) {
						}
					}
				}
				
				while (sleepTime < 0 && framesSkipped < MAX_SKIPS) {

					this.movingView.update(); 
					// add frame period to check if in next frame
					sleepTime += PERIOD;  
					framesSkipped++;

				}

			} finally {
				if (canvas != null) {
					surfaceHolder.unlockCanvasAndPost(canvas);
				}
			}
			synchronized (mPauseLock) {
			    while (mPaused) {
			        try {
			            mPauseLock.wait();
			        } catch (InterruptedException e) {
			        }
			    }
			}
			
		}
	}
}
