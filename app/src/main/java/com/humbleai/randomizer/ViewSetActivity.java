package com.humbleai.randomizer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.NumberPicker;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ViewSetActivity extends AppCompatActivity {

    private RecyclerView.Adapter mAdapter;
    private SetItemSQLiteHelper db;
    private final List<SetItem> myDatasetSonuc= new ArrayList<>();
  //  private int count;
    private int kacTaneItem=1;
    private ImageView scrollImage;
    private boolean scrollIconShown = false;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_view_set, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_set);
        Intent intent = getIntent();

        final RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewStack);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        db = new SetItemSQLiteHelper(this);

        mAdapter = new SetItemAdapter(myDatasetSonuc, this);
        mRecyclerView.setAdapter(mAdapter);

        NumberPicker np = (NumberPicker) findViewById(R.id.numberpicker_kac_tane);
        np.setMinValue(1);
        np.setMaxValue(8);
        np.setWrapSelectorWheel(true);
        np.setDisplayedValues(getResources().getStringArray(R.array.array_kac_tane));

        final String setTitle= intent.getStringExtra("setTitle");
        final String setDescription= intent.getStringExtra("setDescription");
        final int setId = Integer.parseInt(intent.getStringExtra("setID"));
        final int setIcon = Integer.parseInt(intent.getStringExtra("setIcon"));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarViewSet);
        toolbar.setLogo(setIcon);
        toolbar.setTitle(setTitle);
        toolbar.setSubtitle(setDescription);
        setSupportActionBar(toolbar);

        getThings(setId);

        FloatingActionButton fabAdd = (FloatingActionButton) findViewById(R.id.fabGetOne);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getThings(setId);
                mRecyclerView.scrollToPosition(0);

                if (!scrollIconShown) {
                    scrollImage = (ImageView) findViewById(R.id.scrollIcon);
                    if (kacTaneItem > 1) {
                        SharedPreferences settings = getSharedPreferences(ScrollingActivity.PREFS_NAME, 0);
                        Animation anim;
                        if (settings.getBoolean("action_settings_animations_list", false)) {
                            anim = AnimationUtils.loadAnimation(view.getContext(), R.anim.scrollicon_anim);
                        } else {
                            anim = AnimationUtils.loadAnimation(view.getContext(), R.anim.scroll_anim_off);
                        }

                        scrollImage.setImageResource(R.drawable.scroll);
                        scrollImage.startAnimation(anim);
                        scrollIconShown = true;
                        anim.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {

                                scrollImage.setImageDrawable(null);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                    } else {
                        scrollImage.setImageDrawable(null);
                    }
                }

            }

        });



        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                kacTaneItem = newVal;
            }
        });


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    private void getThings(int setId) {

        SecureRandom rand = new SecureRandom();
        rand.setSeed(new Date().getTime());
        int incCount = db.getCount(setId, true).intValue();

        myDatasetSonuc.clear();
        for (int i = 0; i < kacTaneItem; i++) {
            SetItem sonucItem = db.getRandomSetItem(setId, rand.nextInt(incCount));

            sonucItem.setItemViewType(); // sonuc view

            myDatasetSonuc.add(sonucItem);

        }

      //  db.close();

        mAdapter.notifyDataSetChanged();

    }


    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case android.R.id.home:
                finish();
                break;
            case R.id.action_share:

                String textToShare = "";
                for (int i = 0; i < myDatasetSonuc.size(); i++) {
                    textToShare += myDatasetSonuc.get(i).getTitle() + "\n";
                }

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, textToShare);
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.share)));
                return true;
            case R.id.action_settings:
                Intent settingsIntent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(settingsIntent);

                return true;
            default:
                break;
        }

        return true;
    }
}
