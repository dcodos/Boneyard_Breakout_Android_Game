package com.virginia.edu.cs2110_group22;

import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class GameOverActivity extends Activity {

	private int killCount;
	private TextView killText;
	private TextView highScoresView;
	private EditText nameEdit;
	MediaPlayer mp;
	boolean flag = false;
	private Button submitBut;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_over);
		
		this.killCount = getIntent().getIntExtra("kills", 0);
		killText = (TextView) findViewById(R.id.count_text);
		killText.setText("Score: " + this.killCount);
		
		highScoresView = (TextView) findViewById(R.id.high_scores);
		nameEdit = (EditText) findViewById(R.id.name_edit);
		submitBut = (Button) findViewById(R.id.score_button);
		
		SharedPreferences scores = getPreferences(0);
		HashMap highScores = (HashMap) scores.getAll();
		String scoresString = "High Scores:";
		for (Object name : highScores.keySet()) {
			scoresString = scoresString + "\n" + name + ": " + highScores.get(name);
		}
		highScoresView.setText(scoresString);
		
		mp = MediaPlayer.create(this, R.raw.gameover);
		mp.start();
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game_over, menu);
		return true;
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
	public void onResume() {
		super.onResume();
		mp.start();
	}
	
	public void submitScore(View view) {
		SharedPreferences scores = getPreferences(0);
		SharedPreferences.Editor editor = scores.edit();
		HashMap highScores = (HashMap) scores.getAll();
		
		int lowestHighScore = 0;
		String lowestHighScoreName = null;
		boolean firstRun = true;
		for (Object name : highScores.keySet()) {
			if (firstRun) {
				lowestHighScoreName = (String) name;
				lowestHighScore = (Integer) highScores.get(name);
			}
			if ((Integer) highScores.get(name) < lowestHighScore) {
				lowestHighScore = (Integer) highScores.get(name);
				lowestHighScoreName = (String) name;
			}
			firstRun = false;
		}
		
		if (highScores.size() < 5) {
			editor.putInt(nameEdit.getText().toString(), killCount);
			editor.commit();
		} else if (killCount > lowestHighScore) {
			editor.remove(lowestHighScoreName);
			editor.putInt(nameEdit.getText().toString(), killCount);
		}
		
		
		scores = getPreferences(0);
		highScores = (HashMap) scores.getAll();
		
		String scoresString = "High Scores:";
		for (Object name : highScores.keySet()) {
			scoresString = scoresString + "\n" + name + ": " + highScores.get(name);
		}
		highScoresView.setText(scoresString);
		nameEdit.setVisibility(View.GONE);
		submitBut.setVisibility(View.GONE);
		
	}
	
	@Override
    public void onPause() {
        super.onPause();
        mp.release();
    }
	
	public void clearScores(View view) {
		SharedPreferences scores = getPreferences(0);
		SharedPreferences.Editor editor = scores.edit();
		HashMap highScores = (HashMap) scores.getAll();
		editor.clear();
		editor.commit();
		scores = getPreferences(0);
		highScores = (HashMap) scores.getAll();
		String scoresString = "High Scores:";
		for (Object name : highScores.keySet()) {
			scoresString = scoresString + "\n" + name + ": " + highScores.get(name);
		}
		highScoresView.setText(scoresString);
	}
	
	public void newGame(View view) {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}
	
}

