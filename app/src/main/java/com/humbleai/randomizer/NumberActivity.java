package com.humbleai.randomizer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.security.SecureRandom;
import java.util.Date;

public class NumberActivity extends AppCompatActivity {
    private int kacTaneItem=1;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_number, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number);
        Intent intent = getIntent();

        final String setTitle= intent.getStringExtra("setTitle");
        final String setDescription= intent.getStringExtra("setDescription");
        final int setIcon = Integer.parseInt(intent.getStringExtra("setIcon"));


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(setIcon);
        toolbar.setTitle(setTitle);
        toolbar.setSubtitle(setDescription);

        setSupportActionBar(toolbar);

        NumberPicker np = (NumberPicker) findViewById(R.id.numberpicker_kac_tane);
        np.setMinValue(1);
        np.setMaxValue(8);
        np.setWrapSelectorWheel(true);
        np.setDisplayedValues(getResources().getStringArray(R.array.array_kac_tane));
        np.setValue(1);

        final EditText editTextFrom = (EditText) findViewById(R.id.editTextStartNumber);
        final EditText editTextTo = (EditText) findViewById(R.id.editTextEndNumber);
        final EditText editTextDecimals = (EditText) findViewById(R.id.editTextDecimals);
        final TextView textViewSonuc = (TextView) findViewById(R.id.textViewSonuc);


        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                kacTaneItem = newVal;
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (editTextFrom.length() < 1) {
                    makeToast(getString(R.string.from_empty), view.getContext());
                    return;
                }
                if (editTextTo.length() < 1) {
                    makeToast(getString(R.string.to_empty), view.getContext());
                    return;
                }
                if (editTextDecimals.length() < 1) {
                    editTextDecimals.setText("0");
                }

                double from = Double.parseDouble(editTextFrom.getText().toString());
                double to = Double.parseDouble(editTextTo.getText().toString());
                int decimals = Integer.parseInt(editTextDecimals.getText().toString());

                if (to <= from) {
                    makeToast(getString(R.string.from_bigger), view.getContext());
                    return;
                }

                SecureRandom rand = new SecureRandom();
                rand.setSeed(new Date().getTime());
                String sonuclar = "";
                String lineEnd = "";

                double randomNum;

                if (kacTaneItem > 1) lineEnd ="\n";
                int maxItemLen = 1;
                for (int i = 0; i < kacTaneItem; i++) {
                    randomNum = rand.nextDouble() * (to - from) + from;
                    String oneItem = String.format("%." + decimals + "f", randomNum);
                    if (oneItem.length() > maxItemLen) maxItemLen = oneItem.length();
                    sonuclar += oneItem + lineEnd;
                }


                float textSize = (200f / (maxItemLen * kacTaneItem)) + 20;
                textViewSonuc.setTextSize(textSize);

                SharedPreferences settings = getSharedPreferences(ScrollingActivity.PREFS_NAME, 0);

                textViewSonuc.setText(sonuclar);

                if (settings.getBoolean("action_settings_animations_text", false)) {
                    textViewSonuc.setAlpha(0);
                    textViewSonuc.animate().setDuration(1000);
                    textViewSonuc.animate().alpha(1);
                    textViewSonuc.startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.result_anim));
                }



            }

        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void makeToast(String mesaj, Context con) {
        Toast toast = Toast.makeText(con, mesaj, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_share:
                TextView textViewSonuc = (TextView) findViewById(R.id.textViewSonuc);
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, String.valueOf(textViewSonuc.getText().toString()));
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.share)));
                return true;
            case R.id.action_settings:
                Intent settingsIntent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(settingsIntent);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
