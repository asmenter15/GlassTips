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

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.glass.app.Card;

public class TipCalculationActivity extends Activity {

	public static final String PERCENTAGE1 = "percentage1";
	public static final String PERCENTAGE2 = "percentage2";
	public static final String PERCENTAGE3 = "percentage3";
	public static final int PERCENTAGE_SELECTION_TASK = 69;
	public static final int SPEECH_TASK = 70;
	public static final String PERCENTAGE_EXTRA = "percentage_extra";
	public static final String TAG = "as";
	SharedPreferences sp;
	SharedPreferences.Editor editor;
	ArrayList<String> voiceResults;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "oncreate");
		sp = PreferenceManager.getDefaultSharedPreferences(this);
		voiceResults = getIntent().getExtras().getStringArrayList(
				RecognizerIntent.EXTRA_RESULTS);
		Log.d(TAG, "onResume AT0: " + voiceResults.get(0));
		createView(voiceResults);
	}

	protected void onResume() {
		super.onResume();
	}

	private void createView(ArrayList<String> voiceResults) {
		Log.d(TAG, "createView " + voiceResults);
		ArrayList<Integer> percentages = new ArrayList<Integer>();
		String[] words;
		double finalBill = 0;
		Card card = new Card(this);

		if (voiceResults != null) {
			words = voiceResults.get(0).split(" ");

			percentages.add(sp.getInt(PERCENTAGE1, 15));
			percentages.add(sp.getInt(PERCENTAGE2, 20));
			percentages.add(sp.getInt(PERCENTAGE3, 25));

			for (int i = 0; i < words.length; i++) {
				String temp = words[i];
				boolean exitFor = false;
				for (int j = 0; j < words[i].length(); j++) {
					if (!exitFor) {
						try {
							Double.parseDouble(temp.substring(j));
							Log.d(TAG,
									"parsed substring = "
											+ words[i].substring(j));
							finalBill = Double.parseDouble(words[i]
									.substring(j));
							exitFor = true;
						} catch (NumberFormatException nfe) {

						}
					}
				}
			}

			Log.d(TAG, "bill parsed = " + finalBill);

			if (finalBill == 0) {
				card.setText(R.string.voice_error);
			} else {
				double firstTip = finalBill * (percentages.get(0) * .01);
				double secondTip = finalBill * (percentages.get(1) * .01);
				double thirdTip = finalBill * (percentages.get(2) * .01);

				card.setText(percentages.get(0) + "% = $"
						+ String.format("%.2f", firstTip) + "\n"
						+ percentages.get(1) + "% = $"
						+ String.format("%.2f", secondTip) + "\n"
						+ percentages.get(2) + "% = $"
						+ String.format("%.2f", thirdTip));

			}
		} else {
			card.setText(R.string.voice_error);
		}

		setContentView(card.toView());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.percentage_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent in = new Intent(TipCalculationActivity.this,
				ChoosePercentageActivity.class);
		switch (item.getItemId()) {
		case R.id.first:
			in.putExtra(PERCENTAGE_EXTRA, TipCalculationActivity.PERCENTAGE1);
			startActivityForResult(in, PERCENTAGE_SELECTION_TASK);
			return true;
		case R.id.second:
			in.putExtra(PERCENTAGE_EXTRA, TipCalculationActivity.PERCENTAGE2);
			startActivityForResult(in, PERCENTAGE_SELECTION_TASK);
			return true;
		case R.id.third:
			in.putExtra(PERCENTAGE_EXTRA, TipCalculationActivity.PERCENTAGE3);
			startActivityForResult(in, PERCENTAGE_SELECTION_TASK);
			return true;
		case R.id.new_tip:
			Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
			intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
					getString(R.string.well_how_much_bill));
			startActivityForResult(intent, SPEECH_TASK);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case PERCENTAGE_SELECTION_TASK:
			openOptionsMenu();
			break;
		case SPEECH_TASK:
			if (resultCode == RESULT_OK) {
				voiceResults = data
						.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
				Log.d(TAG, "VoiceResults from speech intent AT0: "
						+ voiceResults.get(0));
				if (voiceResults != null) {
					createView(voiceResults);
				}
			}
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.d(TAG, "KeyEvent = " + event);
		if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
			openOptionsMenu();
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return false;
	}

	@Override
	public void onOptionsMenuClosed(Menu menu) {
	}
}
