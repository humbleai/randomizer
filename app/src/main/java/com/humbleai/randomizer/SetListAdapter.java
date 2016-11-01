package com.humbleai.randomizer;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class SetListAdapter extends RecyclerView.Adapter<SetListAdapter.ViewHolder> {

    private final List<SetList> mDataset;
    private final Context mContext;
    private SetListSQLiteHelper db;
    private final SharedPreferences settings;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

        public final CardView mCard;
        public final TextView mTextViewTitle;
        public final TextView mTextViewDescription;
        public final ImageView mImageViewSetIcon;
        public final ImageButton mImageButton;

        public ViewHolder(View v) {
            super(v);
            mCard = (CardView) v.findViewById(R.id.card_view);

            mTextViewDescription = (TextView) v.findViewById(R.id.textViewDescription);
            mTextViewTitle = (TextView) v.findViewById(R.id.textViewTitle);
            mImageViewSetIcon = (ImageView) v.findViewById(R.id.imageViewSetIcon);
            mImageButton = (ImageButton) v.findViewById(R.id.imageButtonDeleteSet);

        }
    }

    public SetListAdapter(List<SetList> mDataset, Context mContext ) {
        this.mDataset = mDataset;
        this.mContext =mContext;

        settings = mContext.getSharedPreferences(ScrollingActivity.PREFS_NAME, 0);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public SetListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycle_item, parent, false);
        // set the view's size, margins, paddings and layout parameters

        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
         final SetList setListItem = mDataset.get(position);
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        if (settings.getBoolean("action_settings_animations_list", false)) {
            holder.mCard.setAlpha(0);
            holder.mCard.animate().setDuration(800);
            holder.mCard.animate().alpha(1);
        }

        holder.mTextViewTitle.setText(setListItem.getTitle());
        holder.mTextViewDescription.setText(setListItem.getDescription());
        holder.mImageViewSetIcon.setImageResource(setListItem.getIcon());

        if (setListItem.getSetType().equals("user")) {

            holder.mImageButton.setVisibility(View.VISIBLE);
        } else {
            holder.mImageButton.setVisibility(View.INVISIBLE);
        }

        holder.mCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // SetList clickedItem = mDataset.get(position);
                Intent intent;
                if (String.valueOf(setListItem.getPrime()).contains("Number")) {
                    intent = new Intent(mContext, NumberActivity.class);
                } else {
                    intent = new Intent(mContext, ItemsActivity.class);
                }
                if (settings.getBoolean("action_settings_animations_list", false)) {
                    v.setAlpha(0.5f);
                    v.animate().setDuration(200);
                    v.animate().alpha(1);
                }


                intent.putExtra("setID", String.valueOf(setListItem.getId()));
                intent.putExtra("setTitle", String.valueOf(setListItem.getTitle()));
                intent.putExtra("setIcon", String.valueOf(setListItem.getIcon()));
                intent.putExtra("setDescription", String.valueOf(setListItem.getDescription()));
                intent.putExtra("setPrime", String.valueOf(setListItem.getPrime()));
                intent.putExtra("setSetType", String.valueOf(setListItem.getSetType()));
                mContext.startActivity(intent);

            }
        });

        holder.mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                PopupMenu popup = new PopupMenu(mContext, v);
                popup.getMenu().add(R.string.delete);
                popup.getMenu().add(R.string.edit);

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        if (item.getTitle().toString().contentEquals(mContext.getString(R.string.delete))) {

                            db = new SetListSQLiteHelper(mContext);
                            db.deleteSetList(setListItem.getId());
                           // db.close();
                            mDataset.remove(setListItem);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, getItemCount());
                        }

                        if (item.getTitle().toString().contentEquals(mContext.getString(R.string.edit))) {

                            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
                            final View promptView = layoutInflater.inflate(R.layout.input_dialog_new_set, null, false);
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
                            alertDialogBuilder.setView(promptView);

                            final EditText editTextNewTitle = (EditText) promptView.findViewById(R.id.editTextNewTitle);
                            final EditText editTextNewDescription = (EditText) promptView.findViewById(R.id.editTextNewDescription);
                            final TextView previewNewTitle = (TextView) promptView.findViewById(R.id.textViewTitle);
                            final ImageView imageViewSetIconPreview = (ImageView) promptView.findViewById(R.id.imageViewSetIconPreview);
                            final TextView textViewtitlenewset = (TextView) promptView.findViewById(R.id.textViewtitlenewset);

                            textViewtitlenewset.setText(R.string.edit);

                            editTextNewTitle.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {

                                    previewNewTitle.setText(s);

                                    String titleText = editTextNewTitle.getText().toString();
                                    int letterId = R.drawable.ic_new_list_item_48dp;
                                    if (titleText.length() > 0) {
                                        String initialLetter = titleText.substring(0, 1).toLowerCase();
                                        String alphabet = "abcdefghijklmnopqrstuvwxyz1234567890";

                                        if (alphabet.contains(initialLetter)) {
                                            letterId = mContext.getResources().getIdentifier("ik_" + initialLetter + "_48", "drawable", mContext.getPackageName());
                                        }
                                    }

                                    imageViewSetIconPreview.setImageResource(letterId);

                                }

                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                    // TODO Auto-generated method stub
                                }

                                @Override
                                public void afterTextChanged(Editable s) {

                                    // TODO Auto-generated method stub
                                }
                            });

                            final TextView previewNewDescription = (TextView) promptView.findViewById(R.id.textViewDescription);


                            editTextNewDescription.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {

                                    previewNewDescription.setText(s);
                                }

                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                    // TODO Auto-generated method stub
                                }

                                @Override
                                public void afterTextChanged(Editable s) {

                                    // TODO Auto-generated method stub
                                }
                            });


                            editTextNewTitle.setText(setListItem.getTitle());
                            editTextNewDescription.setText(setListItem.getDescription());

                            alertDialogBuilder.setCancelable(true)
                                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                                        public void onClick(DialogInterface dialog, int id) {

                                            if (TextUtils.isEmpty(editTextNewTitle.getText()))
                                                editTextNewTitle.setText(R.string.untitled);
                                            String titleText = editTextNewTitle.getText().toString();
                                            String initialLetter = titleText.substring(0, 1).toLowerCase();
                                            String alphabet = "abcdefghijklmnopqrstuvwxyz1234567890";
                                            int letterId;
                                            if (alphabet.contains(initialLetter)) {
                                                letterId = mContext.getResources().getIdentifier("ik_" + initialLetter + "_48", "drawable", mContext.getPackageName());
                                            } else {
                                                letterId = R.drawable.ic_new_list_item_48dp;
                                            }


                                            setListItem.setIcon(letterId);
                                            setListItem.setTitle(titleText);
                                            setListItem.setDescription(editTextNewDescription.getText().toString());

                                            db = new SetListSQLiteHelper(mContext);
                                            int insertedId = db.updateSetList(setListItem);
                                          //  db.close();
                                            if (insertedId == -1) {
                                                Toast toast = Toast.makeText(mContext, R.string.newListFail, Toast.LENGTH_SHORT);
                                                toast.show();
                                            } else {
                                                notifyItemChanged(position);

                                                Toast toast = Toast.makeText(mContext, R.string.editListSuccess, Toast.LENGTH_SHORT);
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

                            AlertDialog alert = alertDialogBuilder.create();
                            alert.show();


                        }

                        return false;
                    }
                });
                popup.show();
            }
        });



    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return (null != mDataset ? mDataset.size() : 0);
    }
}