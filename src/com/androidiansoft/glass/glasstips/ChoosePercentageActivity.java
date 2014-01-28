package com.androidiansoft.glass.glasstips;

/*
Copyright 2014 Aaron Smentkowski

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

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
