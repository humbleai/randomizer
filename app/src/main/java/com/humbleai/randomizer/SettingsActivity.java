package com.humbleai.randomizer;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.CompoundButton;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CheckBox checkAnimImage = (CheckBox) findViewById(R.id.action_settings_animations_bg);
        CheckBox checkAnimText = (CheckBox) findViewById(R.id.action_settings_animations_text);
        CheckBox checkAnimList = (CheckBox) findViewById(R.id.action_settings_animations_list);
        CheckBox checkMinTwoNotebooks = (CheckBox) findViewById(R.id.action_settings_min_two_notebooks);
        CheckBox checkMinTwoNotes = (CheckBox) findViewById(R.id.action_settings_min_two_notes);

        SharedPreferences settings = getSharedPreferences(ScrollingActivity.PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();

        checkAnimImage.setChecked(settings.getBoolean("action_settings_animations_bg", false));
        checkAnimText.setChecked(settings.getBoolean("action_settings_animations_text", false));
        checkAnimList.setChecked(settings.getBoolean("action_settings_animations_list", false));
        checkMinTwoNotebooks.setChecked(settings.getBoolean("action_settings_min_two_notebooks", false));
        checkMinTwoNotes.setChecked(settings.getBoolean("action_settings_min_two_notes", false));

        editor.apply();

        checkAnimImage.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences settings = getSharedPreferences(ScrollingActivity.PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("action_settings_animations_bg", isChecked);
                editor.apply();
            }
        });

        checkAnimText.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences settings = getSharedPreferences(ScrollingActivity.PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("action_settings_animations_text", isChecked);
                editor.apply();
            }
        });

        checkAnimList.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences settings = getSharedPreferences(ScrollingActivity.PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("action_settings_animations_list", isChecked);
                editor.apply();
            }
        });

        checkMinTwoNotebooks.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences settings = getSharedPreferences(ScrollingActivity.PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("action_settings_min_two_notebooks", isChecked);
                editor.apply();
            }
        });

        checkMinTwoNotes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences settings = getSharedPreferences(ScrollingActivity.PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("action_settings_min_two_notes", isChecked);
                editor.apply();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
