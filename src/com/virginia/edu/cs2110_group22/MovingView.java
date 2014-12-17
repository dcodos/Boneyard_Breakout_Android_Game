package com.virginia.edu.cs2110_group22;

import java.util.ArrayList;

import com.virginia.edu.cs2110_group22.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

@SuppressLint("ClickableViewAccessibility")
public class MovingView extends SurfaceView implements SurfaceHolder.Callback, SensorEventListener {

	private static final String TAG = MovingView.class.getSimpleName();
	private static final long TRIPLE_DURATION = 4000;
	private static final long LEVEL_DURATION = 5000;
	private static final double spinDuration = 3000;
	private static final int GOLD_TIME = 7000;
	
	public GameThread gameThread;
	private ArrayList<Ghost> ghosts;
	public Character mainChar;
	Bitmap scaled1;
	Bitmap scaled2;
	Bitmap scaled3;
	Bitmap scaledGhost;
	public boolean paused;
	public ArrayList<Bullet> bulletList = new ArrayList<Bullet>();
	public ArrayList<Item> items;
	public ArrayList<Bitmap> itemImgs;
	SensorManager mSensorManager;
	boolean sensorMove;
	private int score;
	private int coinCount;
	private int health;
	private long lastUpdate;
	private boolean firstRun;
	private boolean isServer;
	private int players;
	private boolean gameIsRunning;
	private boolean tripleShot;
	private boolean lastTripleCheck;
	private long tripleTime;
	private int mapId;
	private long levelTime;
	private int randomStart;

	public int height;

	public int width;
	private Bitmap coin1BitScaled;
	private Bitmap coin2BitScaled;
	private Bitmap coin3BitScaled;
	private Bitmap coin4BitScaled;
	private Bitmap coin5BitScaled;
	private Bitmap coin6BitScaled;
	private Bitmap coin7BitScaled;
	private Bitmap coin8BitScaled;
	private boolean waitForKills;
	private int level;
	private boolean canChangeLevel;
	private ArrayList<Bitmap> kidImgs;
	private int currentKid;
	public boolean spinning;
	private long spinTime;
	private long lastKidUpdate;
	public long spinStart;
	public float finalY;
	public float finalX;
	private boolean isGold;
	private long goldTime;
	private double goldY;
	private double goldX;
	private long goldStartTime;
	private Bitmap scaledGoldBit;
	private CollisionBox goldBox;
	
	public MovingView(Context context, AttributeSet attrs) {
		super(context, attrs);		
		Intent intent = ((MainActivity) context).getIntent();
		players = intent.getIntExtra("players", 1);
		isServer = intent.getBooleanExtra("server", false);
		
		// ArrayList for ghosts currently on screen.
		ghosts = new ArrayList<Ghost>();
		bulletList = new ArrayList<Bullet>();
		items = new ArrayList<Item>();
		itemImgs = new ArrayList<Bitmap>();
		kidImgs = new ArrayList<Bitmap>();
		
		
		// Create new GameThread for game loop
		gameThread = new GameThread(getHolder(), this);
		getHolder().addCallback(this);
		setFocusable(true);
		
		// Accelerometer sensor setup
		mSensorManager = (SensorManager) getContext().getSystemService(Activity.SENSOR_SERVICE);
		Sensor mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
		sensorMove = false;
		
		// Define full health
		health = 100;
		
		// Define initial time for randomly spawning ghosts
		lastUpdate = System.currentTimeMillis();
		
		// Set first run to true
		firstRun = true;
		
		if (players > 1) {
			BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
			if (mBluetoothAdapter == null) {
			    // Device does not support Bluetooth
			}
			if (isServer) {
				if (!mBluetoothAdapter.isEnabled()) {
				    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
				    ((MainActivity) context).startActivityForResult(enableBtIntent, 1);
				}
			}
		}
		gameIsRunning = false;
		
		tripleShot = false;
		lastTripleCheck = false;
		mapId = 1;
		randomStart = 5000;
		levelTime = System.currentTimeMillis();
		waitForKills = false;
		level = 1;
		spinning = false;
		currentKid = 0;
		isGold = false;
		goldTime = System.currentTimeMillis();
	}
	
	public MovingView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// Start gameThread
		gameThread.setRunning(true);
		Log.d(TAG, "CREATING");
		if (!gameIsRunning) {
			gameThread.start();
		} else {
			Log.d(TAG, "RESUMING");
			gameThread.onResume();
			gameIsRunning = true;
		}
		
		
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		gameThread.onPause();
		for (int i = 0; i < 100; ++i) {
		Log.d(TAG, "PAUSING 2");
		}
//				boolean retry = true;
//		while (retry) {
//			try {
//				gameThread.join();
//				retry = false;
//			} catch (Exception e) {
//			}
//		}
	}
	
	
	// This method run on every click event
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		((MainActivity) getContext()).playShootSound();
		Log.d(TAG, "Coords: x=" + event.getX() + ",y=" + event.getY());
		Log.d(TAG, "Height: " + getHeight());
		
		// Find difference in y values and x values of click and main Character location
		double yDif = (double) (event.getY() - (mainChar.getY() + (mainChar.getHeight() / 2)));
		double xDif = (double) (event.getX() - (mainChar.getX() + (mainChar.getWidth() / 2)));
		
		// Find ratio of y to x to use for bullet velocity
		double ratio = yDif / xDif;
		
		// Create new bullets
		if (xDif < 0) {
			bulletList.add(mainChar.fireBullet(this, ratio, true));
		} else {
			bulletList.add(mainChar.fireBullet(this, ratio, false));
		}
		
		double distance = Math.sqrt(Math.pow(yDif, 2) + Math.pow(xDif, 2));
		double angle = Math.atan(yDif / xDif);
		
		double angleShift = Math.PI / 8;
		
		
		if (tripleShot) {
			double yDif2 = distance * Math.sin(angle - angleShift);
			double xDif2 = distance * Math.cos(angle - angleShift);
			double yDif3 = distance * Math.sin(angle + angleShift);
			double xDif3 = distance * Math.cos(angle + angleShift);
			double ratio2 = yDif2 / xDif2;
			double ratio3 = yDif3 / xDif3;
			if (xDif2 < 0) {
				if (xDif < 0) {
					bulletList.add(mainChar.fireBullet(this, ratio2, true, true));
				} else {
					bulletList.add(mainChar.fireBullet(this, ratio2, true));
				}
			} else {
				if (xDif < 0) {
					bulletList.add(mainChar.fireBullet(this, ratio2, false, true));
				} else {
					bulletList.add(mainChar.fireBullet(this, ratio2, false));
				}
			}
			if (xDif3 < 0) {
				if (xDif < 0) {
					bulletList.add(mainChar.fireBullet(this, ratio3, true, true));
				} else {
					bulletList.add(mainChar.fireBullet(this, ratio3, true));
				}
			} else {
				if (xDif < 0) {
					bulletList.add(mainChar.fireBullet(this, ratio3, false, true));
				} else {
					bulletList.add(mainChar.fireBullet(this, ratio3, false));
				}
			}
		}
		return super.onTouchEvent(event);
	}
	
	public void setTripleShot(boolean b) {
		this.tripleShot = b;
	}

	public void update() {
		
		// If first run, store width and height
		if (firstRun) {
			this.width = getWidth();
			this.height = getHeight();
			// Get background image and scale it to screen size
			
			Bitmap background1 = BitmapFactory.decodeResource(getResources(), R.drawable.boneyard);
			Bitmap background2 = BitmapFactory.decodeResource(getResources(), R.drawable.iceboneyard);
			Bitmap background3 = BitmapFactory.decodeResource(getResources(), R.drawable.hellboneyard);
		    scaled1 = Bitmap.createScaledBitmap(background1, this.width, this.height, true);
		    scaled2 = Bitmap.createScaledBitmap(background2, this.width, this.height, true);
		    scaled3 = Bitmap.createScaledBitmap(background3, this.width, this.height, true);
		    
			// Get main Character image and scaled image
			Bitmap mainBitR = Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.kidboneyard));
			Bitmap mainBitRScaled = Bitmap.createScaledBitmap(mainBitR, (int) (0.1125 * this.width), (int) (0.1 * this.height), true);
			Bitmap mainBitL = Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.leftkidboneyard));
			Bitmap mainBitLScaled = Bitmap.createScaledBitmap(mainBitL, (int) (0.1125 * this.width), (int) (0.1 * this.height), true);
			Bitmap mainBitU = Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.backkidboneyard));
			Bitmap mainBitUScaled = Bitmap.createScaledBitmap(mainBitU, (int) (0.1125 * this.width), (int) (0.1 * this.height), true);
			Bitmap mainBitD = Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.frontkidboneyard));
			Bitmap mainBitDScaled = Bitmap.createScaledBitmap(mainBitD, (int) (0.1125 * this.width), (int) (0.1 * this.height), true);
			
			// Create new MovingObject for main character at location
			mainChar = new Character(mainBitRScaled, mainBitLScaled, mainBitUScaled, mainBitDScaled, 15, 580, this, mapId);
			
		    // Get ghost image and scaled image
			Bitmap ghostImg = BitmapFactory.decodeResource(getResources(), R.drawable.boneyardghost);
			scaledGhost = Bitmap.createScaledBitmap(ghostImg, (int) (0.1375 * this.width), (int) (0.0913 * this.height), true);
			
			firstRun = false;
			
			// Create item images
			Bitmap coin1Bit = Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.coin1));
			coin1BitScaled = Bitmap.createScaledBitmap(coin1Bit, (int) (0.07 * this.width), (int) (0.07 * this.width), true);
			Bitmap coin2Bit = Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.coin2));
			coin2BitScaled = Bitmap.createScaledBitmap(coin2Bit, (int) (0.07 * this.width), (int) (0.07 * this.width), true);
			Bitmap coin3Bit = Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.coin3));
			coin3BitScaled = Bitmap.createScaledBitmap(coin3Bit, (int) (0.07 * this.width), (int) (0.07 * this.width), true);
			Bitmap coin4Bit = Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.coin4));
			coin4BitScaled = Bitmap.createScaledBitmap(coin4Bit, (int) (0.07 * this.width), (int) (0.07 * this.width), true);
			Bitmap coin5Bit = Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.coin5));
			coin5BitScaled = Bitmap.createScaledBitmap(coin5Bit, (int) (0.07 * this.width), (int) (0.07 * this.width), true);
			Bitmap coin6Bit = Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.coin6));
			coin6BitScaled = Bitmap.createScaledBitmap(coin6Bit, (int) (0.07 * this.width), (int) (0.07 * this.width), true);
			Bitmap coin7Bit = Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.coin7));
			coin7BitScaled = Bitmap.createScaledBitmap(coin7Bit, (int) (0.07 * this.width), (int) (0.07 * this.width), true);
			Bitmap coin8Bit = Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.coin8));
			coin8BitScaled = Bitmap.createScaledBitmap(coin8Bit, (int) (0.07 * this.width), (int) (0.07 * this.width), true);
			itemImgs.add(coin1BitScaled);
			itemImgs.add(coin2BitScaled);
			itemImgs.add(coin3BitScaled);
			itemImgs.add(coin4BitScaled);
			itemImgs.add(coin5BitScaled);
			itemImgs.add(coin6BitScaled);
			itemImgs.add(coin7BitScaled);
			itemImgs.add(coin8BitScaled);
			
			Bitmap kid1Bit = Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.kidboneyard1));
			Bitmap kid2Bit = Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.kidboneyard2));
			Bitmap kid3Bit = Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.kidboneyard3));
			Bitmap kid4Bit = Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.kidboneyard4));
			Bitmap kid5Bit = Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.kidboneyard5));
			Bitmap kid6Bit = Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.kidboneyard6));
			Bitmap kid7Bit = Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.kidboneyard7));
			Bitmap kid8Bit = Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.kidboneyard8));
			
			kidImgs.add(kid1Bit);
			kidImgs.add(kid2Bit);
			kidImgs.add(kid3Bit);
			kidImgs.add(kid4Bit);
			kidImgs.add(kid5Bit);
			kidImgs.add(kid6Bit);
			kidImgs.add(kid7Bit);
			kidImgs.add(kid8Bit);
			
			Bitmap goldBit = Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.goldbarboneyard));
			scaledGoldBit = Bitmap.createScaledBitmap(goldBit, (int) (0.1 * this.width), (int) (0.1 * this.width), true);
			
		}		
		
		if (System.currentTimeMillis() - levelTime > LEVEL_DURATION) {
			++level;
			((MainActivity) getContext()).setLevelView("Level: " + level);
			levelTime = System.currentTimeMillis();
			waitForKills = true;
			if (randomStart > 2000) {
				randomStart -= 1000;
			} else if (randomStart > 1000) {
				randomStart -= 500;
			} else if (randomStart > 250) {
				randomStart -= 250;
			}
		}
		
		if (!isGold && System.currentTimeMillis() - goldTime > GOLD_TIME) {
			isGold = true;
			goldX = Math.random() * this.getWidth() + 1;
			goldY = Math.random() * this.getHeight() + 1;
			goldBox = new CollisionBox(goldX, goldY, scaledGoldBit.getWidth(), scaledGoldBit.getHeight());
			goldStartTime = System.currentTimeMillis();
			Log.d(TAG, "PUTTING GOLD NOW");

		}
		if (isGold && System.currentTimeMillis() - goldStartTime > 4000) {
			isGold = false;
			goldTime = System.currentTimeMillis();
			Log.d(TAG, "REMOVING GOLD NOW");
		}
		
		if (isGold && goldBox.Intersects(mainChar.getBox())) {
			isGold = false;
			goldTime = System.currentTimeMillis();
			coinCount += 10;
		}
		
		if (waitForKills) {
			levelTime = System.currentTimeMillis();
		}

		if (ghosts.size() == 0) {
			waitForKills = false;
		}
		if (!waitForKills) {
			// Find random time to spawn ghosts
			int rand1 = (int) ((Math.random() * randomStart) + 200);

			// If time to spawn ghost, create new one
			if (System.currentTimeMillis() - lastUpdate > rand1) {

				// Get random x and y to spawn ghost at
				int randW = (int) ((Math.random() * getWidth()));
				int randH = (int) ((Math.random() * getHeight()));

				// Add ghost to list of ghosts
				Ghost ghost1 = new Ghost(scaledGhost, randW, randH, this);
				while (ghost1.startIntersects(mainChar)) {
					randW = (int) ((Math.random() * getWidth()));
					randH = (int) ((Math.random() * getHeight()));
					ghost1.setX(randW);
					ghost1.setY(randH);
				}
				ghosts.add(ghost1);

				// Update last time ghost was added
				lastUpdate = System.currentTimeMillis();
			}
		}
		// Check if main character is hitting into path walls (MAY NOT USE)
		mainChar.checkWall(mapId);
		
		// Update main character location
		mainChar.update(this.getWidth(),  this.getHeight());
		
		// Loop for every ghost
		for (int i = ghosts.size() - 1; i > -1; --i) {
			Ghost ghost = ghosts.get(i);
			
			// Check if given ghost intersects main character
			if (!spinning) {
				if (ghost.intersects(mainChar)) {

					finalX = (float) mainChar.getX();
					finalY = (float) mainChar.getY();

					// Reduce health if there is an intersection
					health -= 15;

					// Restore ghost and main character to their starting points
					ghost.restore();
					mainChar.restore();
				}
			}
			
			// Loop to check if ghosts are hitting other ghosts
			for (int j = 0; j < ghosts.size(); ++j) {
				Ghost ghost2 = ghosts.get(j);
				
				// Changes ghost's directions if it intersect another ghost
				ghost2.intersectionChanges(ghost);
			}
			
			// Loop to check if any bullets intersect the current ghost
			for (int k = bulletList.size() - 1; k > -1; --k) {
				Bullet bullet = bulletList.get(k);
				
				// If bullet intersects ghost
				if (bullet.intersects(ghost)) {
					Item newItem = new Item(itemImgs.get(0), ghost.getX(), ghost.getY(), this, 3000);
					items.add(newItem);
					// Add a kill
					score += 5;
					
					// Remove ghost and bullet
					ghosts.remove(i);
					bulletList.remove(k);
				}
			}
			
			// Update ghost location
			ghost.update(this.getWidth(), this.getHeight());
		}
		
		// Loop to update each bullet's location
		for (int m = bulletList.size() - 1; m > -1; --m) {
			Bullet bullet = bulletList.get(m);
			bullet.update();
		}
		
		// Check if triple shot needs to be cancelled
		if (!lastTripleCheck && tripleShot) {
			tripleTime = System.currentTimeMillis();
			lastTripleCheck = true;
		}
		if ((System.currentTimeMillis() - tripleTime) > TRIPLE_DURATION) {
			this.tripleShot = false;
			this.lastTripleCheck = false;
		}
		
		for (int i = items.size() - 1; i > -1; --i) {
			Item item = items.get(i);
			if ((System.currentTimeMillis() - item.getCreationTime()) > item.getDuration()) {
				items.remove(i);
				break;
			}
			if (item.intersects(mainChar)) {
				items.remove(i);
				((MainActivity) getContext()).playCoinSound();
				score += 2;
				++coinCount;
			}
		}
		
		// Check whether to enable purchase buttons
		if (coinCount >= 5) {
			((MainActivity) getContext()).setTripleEnabled(true);
		} else {
			((MainActivity) getContext()).setTripleEnabled(false);
		}
		if (coinCount >= 10) {
			((MainActivity) getContext()).setBombEnabled(true);
		} else {
			((MainActivity) getContext()).setBombEnabled(false);
		}
		
		if (level % 3 == 2) {
			canChangeLevel = true;
		}
		
		if (level > 1 && level % 3 == 1 && canChangeLevel) {
			++mapId;
			randomStart = 5000 - (1000 * mapId);
			((MainActivity) getContext()).setLevelView("Level: " + level);
			canChangeLevel = false;
		}
	}

	// Method to draw everything to screen
	public void render(Canvas canvas) {

		// Draw background
		if (mapId == 1) {
			canvas.drawBitmap(scaled1, 0, 0, null);
		} else if (mapId == 2) {
			canvas.drawBitmap(scaled2, 0, 0, null);
		} else {
			canvas.drawBitmap(scaled3, 0, 0, null);
		}
		
		// Create rectangle for full health and current health
		Rect fullHealth = new Rect(50, 50, 250, 20);
		Rect curHealth = new Rect(50, 50, (50 + (health * 2)), 20);
		
		// Set up paints for gray, green, yellow, red
		Paint gray = new Paint();
		gray.setColor(Color.GRAY);
		Paint green = new Paint();
		green.setColor(Color.GREEN);
		Paint yellow = new Paint();
		yellow.setColor(Color.YELLOW);
		Paint red = new Paint();
		red.setColor(Color.RED);
		
		// Draw fullHealth gray rectangle
		canvas.drawRect(fullHealth, gray);
		
		// Determing color of current health and draw rectangle (End game if health is 0)
		if (health > 50) {
			canvas.drawRect(curHealth, green);
		} else if (health > 25) {
			canvas.drawRect(curHealth, yellow);
		} else if (health > 0){
			canvas.drawRect(curHealth, red);
		} else if (!spinning){
			((MainActivity)getContext()).mp.release();
			((MainActivity)getContext()).playDeathSound();
			spinStart = System.currentTimeMillis();
			spinning = true;
		}
		
		// Loop to draw bullets
		for (int j = bulletList.size() - 1; j > -1; --j) {
			Bullet bullet = bulletList.get(j);
			
			// If bullet has reached edge of screen, shouldDelete() is true
			if (bullet.shouldDelete()) {
				// If shouldDelete, remove bullet and continue to next bullet
				bulletList.remove(j);
				continue;
			} else {
				// Draw bullet if it shouldn't be removed
				canvas.drawBitmap(bullet.getBitmap(), (float) bullet.getX(), (float) bullet.getY(), null);
			}
		}

		// Draw main character
		if (spinning) {
			double scaleFactor = 1 - (spinTime / spinDuration);
			Log.d(TAG, "Scale Factor: " + scaleFactor);
			int width = (int) (0.1 * this.height * scaleFactor);
			int height = (int) (0.1 * this.height * scaleFactor);
			if (width < 1) {
				width = 1;
				height = 1;
			}
			Bitmap scaledCurrent = Bitmap.createScaledBitmap(kidImgs.get(currentKid), width, height, true);
			canvas.drawBitmap(scaledCurrent, finalX + (mainChar.getBitmap().getWidth() / 2),  finalY + (mainChar.getBitmap().getWidth() / 2), null);
			Log.d(TAG, "PRINT KID #: " + currentKid);
			if (System.currentTimeMillis() - lastKidUpdate > 75) {
				lastKidUpdate = System.currentTimeMillis();
				++currentKid;
				if (currentKid > 7) {
					currentKid = 0;
				}
			}
			if (System.currentTimeMillis() - spinStart > spinDuration) {
				endGame();
			}
			spinTime = System.currentTimeMillis() - spinStart;
		} else {
			canvas.drawBitmap(mainChar.getBitmap(), (float) mainChar.getX(),  (float) mainChar.getY(), null);
		

		// Loop to draw each ghost
		for (int i = 0; i < ghosts.size(); ++i){
			canvas.drawBitmap(ghosts.get(i).getBitmap(), (float) ghosts.get(i).getX(), (float) ghosts.get(i).getY(), null);
		}
		}
		
		// Loop for items
		for (int k = 0; k < items.size(); ++k) {
			Item item = items.get(k);
			int bitmapIndex = item.getCurImg();
			canvas.drawBitmap(itemImgs.get(bitmapIndex), (float) item.getX(), (float) item.getY(), null);
		}

		if (isGold) {
			canvas.drawBitmap(scaledGoldBit, (float) goldX, (float) goldY, null);
		}
		// Update kill count
		((MainActivity) getContext()).setKillCountView("Score: " + score + "   Coins: " + coinCount);
	}

	// Method to sense device tilt
	@Override
	public void onSensorChanged(SensorEvent event) {
		// x and y acceleration values
		double x = event.values[0];
		double y = event.values[1];

		// Device resting point where main character is stationary
		double xCenter = 0;
		double yCenter = 3;

		// Only detects movement after mainChar initialized
		if (!firstRun) {
			// Resets movement so character doesn't move when not tilting.
			if (sensorMove) {
				mainChar.touchRight(false);
				mainChar.touchLeft(false);
				mainChar.touchUp(false);
				mainChar.touchDown(false);
				sensorMove = false;
			}

			// Checks if tilting left
			if (x > (xCenter + 1.5)) {
				mainChar.touchLeft(true);
				mainChar.touchRight(false);

				// Checks if also tilting up
				if (y < (yCenter - 1.5)) {
					mainChar.touchUp(true);
					mainChar.touchDown(false);
				}
				// Checks if also tilting down
				if (y > (yCenter + 1.5)) {
					mainChar.touchDown(true);
					mainChar.touchUp(false);
				}
				sensorMove = true;
			}

			// Checks if tilting right
			else if (x < (xCenter - 1.5)) {
				mainChar.touchRight(true);
				mainChar.touchLeft(false);

				// Checks if also tilting up
				if (y < (yCenter - 1.5)) {
					mainChar.touchUp(true);
					mainChar.touchDown(false);
				}

				// Checks if also tilting down
				if (y > (yCenter + 1.5)) {
					mainChar.touchDown(true);
					mainChar.touchUp(false);
				}
				sensorMove = true;

				// Checks if tilting up only
			} else if (y < (yCenter - 1.5)) {
				mainChar.touchUp(true);
				mainChar.touchDown(false);
				mainChar.touchRight(false);
				mainChar.touchLeft(false);
				sensorMove = true;

				// Checks if tilting down only
			} else if (y > (yCenter + 1.5)) {
				mainChar.touchDown(true);
				mainChar.touchUp(false);
				mainChar.touchRight(false);
				mainChar.touchLeft(false);
				sensorMove = true;
			}	
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		
		
	}
	
	public void removeCoins(int i) {
		coinCount -= i;
	}
	
	// Method to end game
	public void endGame() {
		// Sets game thread to stop running
		gameThread.setRunning(false);
		
		
		// Opens game over activity with intent
		Intent intent = new Intent(((MainActivity) getContext()), GameOverActivity.class);
		
		// Puts amount of kills in intent
		intent.putExtra("kills", this.score);
		
		// Starts intent
		((MainActivity) getContext()).startActivity(intent);
	}
	

	public void fireBomb() {
		int numGhosts = this.ghosts.size();
		int addScore = numGhosts * 5;
		this.score += addScore;
		this.ghosts.clear();
	}
}

