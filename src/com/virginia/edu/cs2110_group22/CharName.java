package com.virginia.edu.cs2110_group22;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class CharName extends Activity {

	static String EXTRA_MESSAGE = "com.virginia.edu.cs2110_group22.MESSAGE";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_char_name);
		
	    // Set the text view as the activity layout
	   // setContentView(new ScribbleView(this));
	    
	}
	public void sendMessage(View view) {
		Intent intent = new Intent(this, StoryActivity.class);
		EditText editText = (EditText) findViewById(R.id.editText1);
		String message = editText.getText().toString();
		intent.putExtra(EXTRA_MESSAGE, message);
		startActivity(intent);
	}

}

