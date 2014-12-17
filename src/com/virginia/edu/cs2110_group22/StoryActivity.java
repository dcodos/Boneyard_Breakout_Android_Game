package com.virginia.edu.cs2110_group22;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;

public class StoryActivity extends Activity {

	static String EXTRA_MESSAGE = "com.virginia.edu.cs2110_group22.MESSAGE";
	public String userName;
	public Typewriter typer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_story);
		typer = (Typewriter) this.findViewById(R.id.type_writer);

		Intent intent = getIntent();
		userName = intent.getStringExtra(CharName.EXTRA_MESSAGE);
		
		String txt = "It was a night like any other in " + userName + "’s hometown, " + "but little did "
				+ userName + " know it was one he wouldn’t soon forget. Late at night after a casual "
				+ "stroll in the park wielding his favorite AK-47 vacuum, " + userName
				+ " had taken the shortcut through the local cemetery" + " his mother had always warned him about. "
				+ userName + " didn’t care though," + " he wasn’t some baby-faced coward. " + userName
				+ " was 11 years old and had "
				+ "obviously seen his fair share of drug deals and bar fights gone wrong."
				+ " But then he’d had his crew, tonight was different, " + "tonight " + userName
				+ " would have to face the haunted cemetery alone.";
		
		//Typeface font = Typeface.createFromAsset(getAssets(), "sFont.ttf");
		
		
		typer.setCharacterDelay(50);
		typer.setTextSize(35);
		typer.setTextColor(Color.WHITE);
		typer.animateText(txt);
		
//		typer.animateText("It was a night like any other in " + userName + "’s hometown, " + "but little did "
//				+ userName + " know it was one he wouldn’t soon forget. Late at night after a casual "
//				+ "stroll in the park wielding his favorite AK-47 vacuum, " + userName
//				+ " had taken the shortcut through the local cemetery" + " his mother had always warned him about. "
//				+ userName + " didn’t care though," + " he wasn’t some baby-faced coward. " + userName
//				+ " was 11 years old and had "
//				+ "obviously seen his fair share of drug deals and bar fights gone wrong."
//				+ " But then he’d had his crew, tonight was different, " + "tonight " + userName
//				+ " would have to face the haunted cemetery alone.");

	}

	public void startGame(View view) {
		Intent intent = new Intent(this, MainActivity.class);
		intent.putExtra("players", 1);
		startActivity(intent);
	}

}
