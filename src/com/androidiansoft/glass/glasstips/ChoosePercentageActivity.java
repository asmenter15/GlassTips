package com.androidiansoft.glass.glasstips;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.glass.app.Card;

public class ChoosePercentageActivity extends Activity {

	public String percentage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Intent in = getIntent();

		percentage = in.getStringExtra(TipCalculationActivity.PERCENTAGE_EXTRA);
		openOptionsMenu();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.percentage_choice_menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(ChoosePercentageActivity.this);
		SharedPreferences.Editor editor = sp.edit();
		
		editor.putInt(percentage, item.getOrder());
		editor.commit();
		Log.d(TipCalculationActivity.TAG, "Editor Committed " + percentage + ":" + item.getOrder());
		setResult(RESULT_OK);
		finish();
		return true;
	}
	
}
