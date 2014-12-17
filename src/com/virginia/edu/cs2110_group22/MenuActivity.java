package com.virginia.edu.cs2110_group22;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MenuActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
	}
	
	public void startGame(View view) {
		Intent intent = new Intent(this, CharName.class);
		intent.putExtra("players", 1);
		startActivity(intent);
	}
	
	public void startMultiClient(View view) {
		Intent intent = new Intent(this, MainActivity.class);
		intent.putExtra("players", 2);
		intent.putExtra("server", false);
		startActivity(intent);
	}
	
	public void startMultiServer(View view) {
		Intent intent = new Intent(this, MainActivity.class);
		intent.putExtra("players", 2);
		intent.putExtra("server", true);
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu, menu);
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
}
