package com.humbleai.randomizer;


import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class ItemsActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private final List<SetItem> myDataset = new ArrayList<>();
    private SetItemSQLiteHelper db;
    private int setId, currentColor, setIcon;
    private String setDescription, setTitle, setPrime, setSetType;
    private int lastItemId = 0;
    private int itemCount = -1;
    public static final String PREFS_NAME = "uc_randomizer_shared_prefs_file";
    private StaggeredGridLayoutManager mLayoutManagerStaggered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewItemList);
        mRecyclerView.setHasFixedSize(true);

        mAdapter = new SetItemAdapter(myDataset, this);
        mRecyclerView.setAdapter(mAdapter);
        arrangeColumns();
        db = new SetItemSQLiteHelper(this);

        onNewIntent(getIntent());



        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private boolean loading = true;
            int pastVisiblesItems, visibleItemCount, totalItemCount;
            int[] visItems;
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) //check for scroll down
                {
                    visibleItemCount = mLayoutManagerStaggered.getChildCount();
                    totalItemCount = mLayoutManagerStaggered.getItemCount();

                    mLayoutManagerStaggered.findLastVisibleItemPositions(visItems);
                    if(visItems != null && visItems.length > 0) {
                        pastVisiblesItems = visItems[0];
                    }

                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {

                            loading = false;

                            dbisleri(false);

                            loading = true;

                        }
                    }
                }
            }
        });

        FloatingActionButton fabGetOne = (FloatingActionButton) findViewById(R.id.fabGetOne);
        fabGetOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int incCount = db.getCount(setId, true).intValue();
                if (incCount < 1) {
                    Toast toast = Toast.makeText(getApplicationContext(), R.string.no_included, Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }

                Intent intent = new Intent(view.getContext(), ViewSetActivity.class);
                intent.putExtra("setID", String.valueOf(setId));
                intent.putExtra("setTitle", setTitle);
                intent.putExtra("setIcon", String.valueOf(setIcon));
                intent.putExtra("setDescription", setDescription);
                intent.putExtra("setPrime", setPrime);
                intent.putExtra("setSetTYpe", String.valueOf(setSetType));
                view.getContext().startActivity(intent);


            }

        });



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onNewIntent(Intent intent){

        Bundle extras = intent.getExtras();

        setTitle= extras.getString("setTitle");
        setId = Integer.parseInt(extras.getString("setID"));
        setIcon = Integer.parseInt(extras.getString("setIcon"));
        setPrime= extras.getString("setPrime");
        setSetType= extras.getString("setSetType");
        setDescription = extras.getString("setDescription");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(setIcon);
        toolbar.setTitle(setTitle);
        toolbar.setSubtitle(setDescription);

        checkCounts();

        dbisleri(false);

        setSupportActionBar(toolbar);

        // if (savedInstanceState == null) {
        if ( intent.getStringExtra("recievedIntent") != null) {
            new insertIntentText(ItemsActivity.this).execute(intent.getStringExtra("recievedIntent"));
        }
        // }

    }

    private void arrangeColumns() {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        int count;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            count = 2;
            if (isTablet(this)) count = 4;
        } else {
            count = 1;
            if (isTablet(this) || settings.getBoolean("action_settings_min_two_notes", false)) count = 2;
        }


        mLayoutManagerStaggered = new StaggeredGridLayoutManager(count, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManagerStaggered);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_items, menu);
        return true;
    }

    @Override
    public void onResume(){
        super.onResume();
        arrangeColumns();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        checkCounts();
        MenuItem showallitem = menu.findItem(R.id.action_show_all);

        if (itemCount <= myDataset.size()) showallitem.setEnabled(false);
        showallitem.setTitle(getString(R.string.show_all) + " (" + itemCount + " " + getString(R.string.items) + ")");
        menu.findItem(R.id.action_shuffle).setTitle(getString(R.string.shuffle) +  " " + "(" + myDataset.size() + " " + getString(R.string.items) + ")");
        menu.findItem(R.id.action_share).setTitle(getString(R.string.share) + " " + "(" + myDataset.size() + " " + getString(R.string.items) + ")");
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_shuffle:
                if (myDataset.size() < 1) {
                    Toast toast = Toast.makeText(getApplicationContext(), R.string.no_item, Toast.LENGTH_SHORT);
                    toast.show();
                    return true;
                }

                Collections.shuffle(myDataset);

                mAdapter.notifyDataSetChanged();
                mRecyclerView.scrollToPosition(0);
                return true;
            case R.id.action_add_new_item:

                LayoutInflater layoutInflater = LayoutInflater.from(ItemsActivity.this);
                final View promptView = layoutInflater.inflate(R.layout.input_dialog, mRecyclerView, false);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ItemsActivity.this);
                alertDialogBuilder.setView(promptView);

                final EditText editText = (EditText) promptView.findViewById(R.id.edittext);


                final ImageView mImageViewNewIcon = (ImageView) promptView.findViewById(R.id.imageViewNewItemIcon);
                 currentColor = randomColor();
                mImageViewNewIcon.setImageResource(currentColor);

                mImageViewNewIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentColor = randomColor();
                        mImageViewNewIcon.setImageResource(currentColor);
                    }
                });

                alertDialogBuilder.setCancelable(true)
                        .setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int id) {

                                if (TextUtils.isEmpty(editText.getText()))
                                    editText.setText(R.string.untitled);

                                SetItem newItem = new SetItem(setId, currentColor, editText.getText().toString());
                                Long inserted = db.addSetItem(newItem);
                               // db.close();
                                if (inserted == -1) {
                                    Toast toast = Toast.makeText(getApplicationContext(), R.string.newListFail, Toast.LENGTH_SHORT);
                                    toast.show();
                                } else {
                                    newItem.setId(inserted.intValue());
                                    myDataset.add(newItem);
                                    mAdapter.notifyItemInserted(myDataset.indexOf(newItem));
                                    mRecyclerView.scrollToPosition(myDataset.indexOf(newItem));

                                    Toast toast = Toast.makeText(getApplicationContext(), R.string.newItemSuccess, Toast.LENGTH_SHORT);
                                    toast.show();
                                }


                            }
                        })
                        .setNegativeButton(R.string.cancel,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                // create an alert dialog
                AlertDialog alert = alertDialogBuilder.create();
                alert.show();
                return true;
            case R.id.action_share:
                String textToShare = "";
                for (int i = 0; i < myDataset.size(); i++) {
                    textToShare += myDataset.get(i).getTitle() + "\n";
                }

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, textToShare);
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.share)));

                return true;
            case R.id.action_show_all:
                lastItemId = 0;
                myDataset.clear();
                dbisleri(true);
                mRecyclerView.scrollToPosition(0);
                return true;
            case R.id.action_import_file:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("text/plain");
                startActivityForResult(intent, 42);
                return true;
            case R.id.action_settings:
                Intent settingsIntent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(settingsIntent);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void checkCounts(){
        itemCount = db.getCount(setId, false).intValue();
       // db.close();
    }

    private void dbisleri(boolean full) {
       if (itemCount <= myDataset.size()) return;

        myDataset.addAll(db.getAllSetItems(setId, lastItemId, full));

        mAdapter.notifyDataSetChanged();
        lastItemId = myDataset.get(myDataset.size() - 1).getId();
        checkCounts();
       // db.close();
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 42 && data != null) {
            // Get the Uri of the selected file
            InputStream inputStream;
            Uri uri = data.getData();
            try {
                inputStream = getContentResolver().openInputStream(uri);
            } catch (FileNotFoundException e) {
                Toast toast = Toast.makeText(getApplicationContext(), R.string.newListFail, Toast.LENGTH_SHORT);
                toast.show();
                return;
            }

            if (inputStream != null) {
                Scanner s = new Scanner(inputStream).useDelimiter("\\A");

                if (s.hasNext()) {
                    new insertIntentText(ItemsActivity.this).execute(s.next());
                }
            } else {
                Toast toast = Toast.makeText(getApplicationContext(), R.string.newListFail, Toast.LENGTH_SHORT);
                toast.show();
            }

        }
    }

    private int randomColor() {
        TypedArray colors_icons = getResources().obtainTypedArray(R.array.colors_icons);
        final Random random = new Random();
        final int randColor = random.nextInt(colors_icons.length());
        final int iconRes = colors_icons.getResourceId(randColor, R.color.colorMainBg);
        colors_icons.recycle();
        return iconRes;
    }

    public boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    private class insertIntentText extends AsyncTask<Object, Integer, Integer> {

        private final Activity activity;
        private final NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        private Notification n;

        public insertIntentText(Activity activity) {
            this.activity = activity;
        }

        @Override
        protected Integer doInBackground(Object... params) {

            String incoming = (String) params[0];
            String[] lines = incoming.split(System.getProperty("line.separator"));




            if (lines.length > 0) {
                int c=0;
                for (String line:lines) {
                    if (line.trim().length() > 0) {
                        SetItem newItem = new SetItem(setId, randomColor(), line.trim());
                        db.addSetItem(newItem);
                        c++;
                        publishProgress(c, lines.length);
                    }
                   // db.close();
                }
                return 1;
            }
            return 0;

        }

        @Override
        protected void onPreExecute() {

            Toast toast = Toast.makeText(getApplicationContext(), R.string.import_start, Toast.LENGTH_LONG);
            toast.show();



            n  = new Notification.Builder(getApplicationContext())
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText(getString(R.string.importing))
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setTicker(getString(R.string.importing))
                    .setAutoCancel(true)
                    .build();
            notificationManager.notify(setId, n);

            finish();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {

            super.onProgressUpdate(values);

            if ((values[0] % 100) == 0) {
                n  = new Notification.Builder(getApplicationContext())
                        .setContentTitle(getString(R.string.importing))
                        .setContentText(String.valueOf(values[0]) + " " + getString(R.string.of) + " " + String.valueOf(values[1]) + " " + getString(R.string.imported))
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setAutoCancel(true)
                        .setOngoing(true)
                        .build();
                notificationManager.notify(setId, n);
            }

        }

        protected void onPostExecute(Integer values) {

            super.onProgressUpdate(values);

            dbisleri(false);

            Intent intent = new Intent(ItemsActivity.this, ItemsActivity.class);

            intent.putExtra("setID", String.valueOf(setId));
            intent.putExtra("setTitle", setTitle);
            intent.putExtra("setIcon", String.valueOf(setIcon));
            intent.putExtra("setDescription", setDescription);
            intent.putExtra("setPrime", setPrime);
            intent.putExtra("setSetType", setSetType);
            PendingIntent pIntent = PendingIntent.getActivity(ItemsActivity.this, (int) System.currentTimeMillis(), intent, PendingIntent.FLAG_UPDATE_CURRENT );

            n  = new Notification.Builder(getApplicationContext())
                    .setContentTitle(getString(R.string.app_name) + " - " + setTitle)
                    .setContentText(getString(R.string.imported))
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setTicker(getString(R.string.imported))
                    .setContentIntent(pIntent)
                    .setAutoCancel(true)
                    .build();
            notificationManager.notify(setId, n);



        }

    }




}


