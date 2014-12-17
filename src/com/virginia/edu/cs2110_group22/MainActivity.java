package com.virginia.edu.cs2110_group22;

import com.virginia.edu.cs2110_group22.R;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends Activity{

	// T101-22
	// Daniel Codos dbc5ba
	// Jessica Bleser jjb2qg
	// Tyler Anderson tfa2fp
	// Dylan Saunders dbs2xt

	
	private static final String TAG = MainActivity.class.getSimpleName();
	MovingView gameView;
	private TextView killText;
	private ImageButton tripleBut;
	private ImageButton bombBut;
	boolean loaded = false;
	public MediaPlayer mp;
	private int id;
	boolean flag = false;
	private SoundPool soundPool1;
	private SoundPool soundPool2;
    int soundId1;
    int soundId2;
	private TextView levelText;
	private SoundPool soundPool3;
	private int soundId3;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);      
		setContentView(R.layout.activity_main);
		gameView = (MovingView) findViewById(R.id.moving_view);
		killText = (TextView) findViewById(R.id.kill_text);
		levelText = (TextView) findViewById(R.id.level_text);
		tripleBut = (ImageButton) findViewById(R.id.triple_button);
		bombBut = (ImageButton) findViewById(R.id.bomb_button);

		mp = MediaPlayer.create(this, R.raw.background);
		mp.start();
		
		soundPool1 = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        soundId1 = soundPool1.load(this, R.raw.shoot, 1);
		
        soundPool2 = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        soundId2 = soundPool2.load(this, R.raw.coin, 1);
        
        soundPool3 = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        soundId3 = soundPool3.load(this, R.raw.death, 1);

	}
	
	public void playShootSound() {
		soundPool1.play(soundId1, 1f, 1f, 1, 0, 2f);
	}
	
    public void playCoinSound() {
        soundPool2.play(soundId2, 1f, 1f, 1, 0, 1f);
    }
    
    public void playDeathSound() {
        soundPool3.play(soundId3, 1f, 1f, 1, 0, 1f);
    }
    
    public void tripleShot(View view) {
    	gameView.setTripleShot(true);
    	gameView.removeCoins(2);
    }
    
    public void fireBomb(View view) {
    	gameView.fireBomb();
    	gameView.removeCoins(10);
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
    }
    
    public void setKillCountView(final String txt){
        MainActivity.this.runOnUiThread(new Runnable() {     
            public void run() {         
                killText.setText(txt);     
            } 
         });
    }
    
    public void setLevelView(final String txt){
        MainActivity.this.runOnUiThread(new Runnable() {     
            public void run() {         
                levelText.setText(txt);     
            } 
         });
    }
    
    public void setTripleEnabled(final boolean b) {
        MainActivity.this.runOnUiThread(new Runnable() {     
            public void run() {         
            	tripleBut.setEnabled(b);  
            } 
         });
    }
    
    public void setBombEnabled(final boolean b) {
        MainActivity.this.runOnUiThread(new Runnable() {     
            public void run() {         
            	bombBut.setEnabled(b);  
            } 
         });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    public void onPause() {
    	gameView.gameThread.onPause();
        super.onPause();
        Log.d(TAG, "PAUSING");
        mp.release();
//        gameView.gameThread.setRunning(false);
    }
    
    @Override
    public void onResume() {
    	gameView.gameThread.onResume();
    	Log.d(TAG, "RESUMING");
    	super.onResume();
    	mp.start();
    	
    	
    	//    	if (firstStart) {
//    		super.onResume();
//    	}
//    	if (!firstStart) {
//    		gameView = backup;
//    		gameView.gameThread = new GameThread(gameView.getHolder(), gameView);
//	    	gameView.gameThread.setRunning(true);
//	    	gameView.gameThread.start();
//    	}
//    	firstStart = false;
    }
}
